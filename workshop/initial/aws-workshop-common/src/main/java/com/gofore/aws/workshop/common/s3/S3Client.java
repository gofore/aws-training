package com.gofore.aws.workshop.common.s3;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.gofore.aws.workshop.common.async.ShutdownHelper;

public class S3Client {
    
    private final ExecutorService executor;
    private final AmazonS3 s3;
    
    public S3Client(AmazonS3 s3, ExecutorService executor) {
        this.executor = executor;
        this.s3 = s3;
        ShutdownHelper.addShutdownHook(() -> this.executor);
    }

    /**
     * Gets the AWS interface.
     *
     * @return the original interface
     */
    public AmazonS3 getS3() {
        return s3;
    }

    /**
     * Completable future wrapper for AmazonS3#putObject
     * 
     * @param request the put object request
     * @return future of put object result
     * @see com.amazonaws.services.s3.AmazonS3#putObject(com.amazonaws.services.s3.model.PutObjectRequest)
     */
    public CompletableFuture<PutObjectResult> putObject(PutObjectRequest request) {
        return CompletableFuture.supplyAsync(() -> s3.putObject(request), executor);
    }
}
