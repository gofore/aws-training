package com.gofore.aws.workshop.fetcher.images;

import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.gofore.aws.workshop.common.net.HttpClient;
import com.gofore.aws.workshop.common.properties.ApplicationProperties;
import com.gofore.aws.workshop.common.s3.S3Client;
import com.google.inject.Inject;
import com.google.inject.Singleton;
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
            /**
             * @see com.amazonaws.services.s3.AmazonS3#putObject(com.amazonaws.services.s3.model.PutObjectRequest)
             */
            throw new UnsupportedOperationException("Task 3 not implemented");
        };
    }

    private ObjectMetadata createContentMetaData(HttpEntity entity) {
        // TODO: Task 3: Put object to S3
        /**
         * Get content length, optional content encoding and optional content
         * type from the http entity and create the object metadata.
         */
        throw new UnsupportedOperationException("Task 3 not implemented");
    }

    private String getThumbnailKey(String id) {
        return THUMBNAILS_SUBDIR + "/" + user + "/" + id;
    }

}
