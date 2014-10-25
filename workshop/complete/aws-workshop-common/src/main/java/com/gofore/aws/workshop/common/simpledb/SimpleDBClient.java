package com.gofore.aws.workshop.common.simpledb;

import java.util.concurrent.CompletableFuture;

import com.amazonaws.handlers.AsyncHandler;
import com.amazonaws.services.simpledb.AmazonSimpleDBAsync;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;

public class SimpleDBClient {
    
    private final AmazonSimpleDBAsync simpleDb;
    
    public SimpleDBClient(AmazonSimpleDBAsync simpleDb) {
        this.simpleDb = simpleDb;
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
