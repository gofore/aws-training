package com.gofore.aws.workshop.common.s3;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.gofore.aws.workshop.common.async.ShutdownHelper;

public class S3Client {
    
    private final ExecutorService executor;
    private final AmazonS3 s3;
    
    public S3Client(String accessKey, String secretKey, String endpoint) {
        this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        ShutdownHelper.addShutdownHook(() -> this.executor);
        
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        AmazonS3Client client = new AmazonS3Client(credentials);
        client.setEndpoint(endpoint);
        this.s3 = client;
    }
    
    public CompletableFuture<PutObjectResult> putObject(PutObjectRequest request) {
        return CompletableFuture.supplyAsync(() -> s3.putObject(request), executor);
    }
}
