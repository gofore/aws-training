package com.gofore.aws.workshop.common.simpledb;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.handlers.AsyncHandler;
import com.amazonaws.services.simpledb.AmazonSimpleDBAsync;
import com.amazonaws.services.simpledb.AmazonSimpleDBAsyncClient;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.gofore.aws.workshop.common.async.ShutdownHelper;

public class SimpleDBClient {
    
    private final AmazonSimpleDBAsync simpleDb;
    
    public SimpleDBClient(String accessKey, String secretKey, String endpoint) {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        AmazonSimpleDBAsyncClient client = new AmazonSimpleDBAsyncClient(credentials, executor);
        client.setEndpoint(endpoint);
        ShutdownHelper.addShutdownHook(client::getExecutorService, client::shutdown);
        this.simpleDb = client;
    }
    
    public CompletableFuture<Void> putAttributes(PutAttributesRequest request) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        simpleDb.putAttributesAsync(request, new AsyncHandler<PutAttributesRequest, Void>() {
            @Override
            public void onError(Exception exception) {
                future.completeExceptionally(exception);
            }

            @Override
            public void onSuccess(PutAttributesRequest request, Void result) {
                future.complete(result);
            }
        });
        return future;
    }
}
