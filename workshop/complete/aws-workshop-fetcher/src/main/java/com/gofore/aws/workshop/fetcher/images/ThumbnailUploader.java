package com.gofore.aws.workshop.fetcher.images;

import static com.gofore.aws.workshop.common.functional.Objects.setPresent;

import java.io.InputStream;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.gofore.aws.workshop.common.functional.Consumers;
import com.gofore.aws.workshop.common.net.HttpClient;
import com.gofore.aws.workshop.common.properties.ApplicationProperties;
import com.gofore.aws.workshop.common.s3.S3Client;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class ThumbnailUploader {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThumbnailUploader.class);
    private static final String THUMBNAILS_SUBDIR = "thumbnails";
    
    private final HttpClient httpClient;
    private final S3Client s3Client;
    private final String s3endpoint;
    private final String s3bucket;
    private final String user;

    @Inject
    public ThumbnailUploader(ApplicationProperties properties, HttpClient httpClient, S3Client s3Client) {
        this.httpClient = httpClient;
        this.s3Client = s3Client;
        this.s3endpoint = properties.lookup("aws.s3.endpoint");
        this.s3bucket = properties.lookup("aws.s3.bucket");
        this.user = properties.lookup("aws.user");
    }

    public String getThumbnailUrl(String id) {
        return "https://" + s3endpoint + "/" + s3bucket + "/" + getThumbnailKey(id);
    }

    public CompletableFuture<Image> upload(String id, Image image) {
        return httpClient.get(image.getThumbnailUrl())
                .thenApply(HttpClient::getEntity)
                .thenCompose(upload(id))
                .thenApply(p -> new Image(getThumbnailUrl(id), image.getImageUrl(), image.getDescription()));
    }

    private Function<HttpEntity, CompletableFuture<PutObjectResult>> upload(String id) {
        return (entity) -> {
            String key = getThumbnailKey(id);
            ObjectMetadata metadata = createContentMetaData(entity);
            InputStream content = HttpClient.getContent(entity);
            // TODO: Task 3: Put object to S3
            PutObjectRequest request = new PutObjectRequest(s3bucket, key, content, metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead);
            return s3Client.putObject(request).whenComplete(Consumers.consumer(
                    (v) -> LOGGER.info("Successfully uploaded thumbnail {} for image {}", key, id),
                    (e) -> LOGGER.error("Failed to save thumbnail {} for image {}", key, id, e)
            ));
        };
    }

    private ObjectMetadata createContentMetaData(HttpEntity entity) {
        ObjectMetadata meta = new ObjectMetadata();
        // TODO: Task 3: Put object to S3
        setPresent(meta::setContentLength, Optional.of(entity.getContentLength()));
        setPresent(meta::setContentEncoding, Optional.ofNullable(entity.getContentEncoding()).map(Header::getValue));
        setPresent(meta::setContentType, Optional.ofNullable(entity.getContentType()).map(Header::getValue));
        return meta;
    }

    private String getThumbnailKey(String id) {
        return THUMBNAILS_SUBDIR + "/" + user + "/" + id;
    }

}
