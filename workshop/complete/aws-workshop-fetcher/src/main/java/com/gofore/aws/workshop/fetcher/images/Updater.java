package com.gofore.aws.workshop.fetcher.images;

import static com.gofore.aws.workshop.common.functional.Objects.setPresent;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;
import com.gofore.aws.workshop.common.functional.Consumers;
import com.gofore.aws.workshop.common.net.HttpClient;
import com.gofore.aws.workshop.common.properties.ApplicationProperties;
import com.gofore.aws.workshop.common.s3.S3Client;
import com.gofore.aws.workshop.common.simpledb.SimpleDBClient;
import com.gofore.aws.workshop.fetcher.utils.TermsParser;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.fluent.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class Updater {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Updater.class);
    
    private static final String THUMBNAILS_SUBDIR = "thumbnails";
    
    private final S3Client s3Client;
    private final SimpleDBClient simpleDBClient;
    private final HttpClient httpClient;
    private final TermsParser termsParser;
    private final HashFunction hashFunction;
    private final String s3endpoint;
    private final String s3bucket;
    private final String domain;
    private final String user;

    @Inject
    public Updater(ApplicationProperties properties, S3Client s3Client, SimpleDBClient simpleDBClient, HttpClient httpClient, TermsParser termsParser) {
        this.s3Client = s3Client;
        this.simpleDBClient = simpleDBClient;
        this.httpClient = httpClient;
        this.termsParser = termsParser;
        this.hashFunction = Hashing.murmur3_128();
        this.s3endpoint = properties.lookup("aws.s3.endpoint");
        this.s3bucket = properties.lookup("aws.s3.bucket");
        this.domain = properties.lookup("images.domain");
        this.user = properties.lookup("aws.user");
    }

    public CompletableFuture<Image> update(Image image) {
        String id = getImageHash(image);
        return httpClient.get(image.getThumbnailUrl())
                .thenCompose(upload(id))
                .thenCombine(save(id, image), (u, s) -> image);
    }
    
    private Function<Response, CompletableFuture<PutObjectResult>> upload(String id) {
        return (r) -> {
            try {
                HttpEntity entity = r.returnResponse().getEntity();
                ObjectMetadata meta = createContentMetaData(entity);
                String key = getImageKey(id);
                PutObjectRequest request = new PutObjectRequest(s3bucket, key, entity.getContent(), meta)
                        .withAccessControlList(createContentAcl());
                return s3Client.putObject(request).whenComplete(Consumers.consumer(
                        (v) -> LOGGER.info("Successfully uploaded thumbnail {} for image {}", key, id),
                        (e) -> LOGGER.error("Failed to save thumbnail {} for image {}", key, id, e)
                ));
            } catch (IOException ex) {
                LOGGER.error("Failed to load thumbnail for image {}", id, ex);
                CompletableFuture future = new CompletableFuture();
                future.completeExceptionally(ex);
                return future;
            }
        };
    }
    
    private CompletableFuture<Void> save(String id, Image image) {
        Stream<ReplaceableAttribute> meta = Arrays.asList(
                new ReplaceableAttribute("thumbnailUrl", getThumbnailUrl(id), true),
                new ReplaceableAttribute("imageUrl", image.getImageUrl(), true),
                new ReplaceableAttribute("description", image.getDescription(), true)
        ).stream();
        Stream<ReplaceableAttribute> terms = termsParser
                .parse(image.getDescription())
                .map(v -> new ReplaceableAttribute("term", v, true));
        List<ReplaceableAttribute> attributes = Stream.concat(meta, terms).collect(Collectors.toList());
        PutAttributesRequest request = new PutAttributesRequest(domain, id, attributes);
        CompletableFuture<Void> future = simpleDBClient.putAttributes(request);
        return future.whenComplete(Consumers.consumer(
                (v) -> LOGGER.info("Successfully saved image {} metadata", id),
                (e) -> LOGGER.error("Failed to save image {} metadata", id, e)
        ));
    }

    private ObjectMetadata createContentMetaData(HttpEntity entity) {
        ObjectMetadata meta = new ObjectMetadata();
        setPresent(meta::setContentLength, Optional.of(entity.getContentLength()));
        setPresent(meta::setContentEncoding, Optional.ofNullable(entity.getContentEncoding()).map(Header::getValue));
        setPresent(meta::setContentType, Optional.ofNullable(entity.getContentType()).map(Header::getValue));
        return meta;
    }
    
    private AccessControlList createContentAcl() {
        AccessControlList acl = new AccessControlList();
        acl.grantPermission(GroupGrantee.AllUsers, Permission.Read);
        return acl;
    }
    
    private String getImageHash(Image image) {
        return hashFunction.hashUnencodedChars(image.getImageUrl()).toString();
    }

    private String getImageKey(String id) {
        return THUMBNAILS_SUBDIR + "/" + user + "/" + id;
    }
    
    private String getThumbnailUrl(String id) {
        return "https://" + s3endpoint + "/" + s3bucket + "/" + getImageKey(id);
    }
}
