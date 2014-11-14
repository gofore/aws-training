package com.gofore.aws.workshop.common.simpledb;

import java.util.concurrent.CompletableFuture;

import com.amazonaws.handlers.AsyncHandler;
import com.amazonaws.services.simpledb.AmazonSimpleDBAsync;
import com.amazonaws.services.simpledb.model.ListDomainsRequest;
import com.amazonaws.services.simpledb.model.ListDomainsResult;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.SelectRequest;
import com.amazonaws.services.simpledb.model.SelectResult;

public class SimpleDBClient {
    
    private final AmazonSimpleDBAsync simpleDb;
    
    public SimpleDBClient(AmazonSimpleDBAsync simpleDb) {
        this.simpleDb = simpleDb;
    }

    public AmazonSimpleDBAsync getSimpleDb() {
        return simpleDb;
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
    
    public CompletableFuture<SelectResult> select(SelectRequest request) {
        CompletableFuture<SelectResult> future = new CompletableFuture<>();
        simpleDb.selectAsync(request, new AsyncHandler<SelectRequest, SelectResult>() {
            @Override
            public void onError(Exception exception) {
                future.completeExceptionally(exception);
            }

            @Override
            public void onSuccess(SelectRequest request, SelectResult result) {
                future.complete(result);
            }
        });
        return future;
    }
    
    public CompletableFuture<ListDomainsResult> listDomains(ListDomainsRequest request) {
        CompletableFuture<ListDomainsResult> future = new CompletableFuture<>();
        simpleDb.listDomainsAsync(request, new AsyncHandler<ListDomainsRequest, ListDomainsResult>() {
            @Override
            public void onError(Exception exception) {
                future.completeExceptionally(exception);
            }

            @Override
            public void onSuccess(ListDomainsRequest request, ListDomainsResult result) {
                future.complete(result);
            }
        });
        return future;
    }
}
