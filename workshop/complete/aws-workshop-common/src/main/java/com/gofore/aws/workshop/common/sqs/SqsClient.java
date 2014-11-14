package com.gofore.aws.workshop.common.sqs;

import java.util.concurrent.CompletableFuture;

import com.amazonaws.handlers.AsyncHandler;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesResult;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class SqsClient {
    
    private final AmazonSQSAsync sqs;
    
    @Inject
    public SqsClient(AmazonSQSAsync sqs) {
        this.sqs = sqs;
    }

    public AmazonSQSAsync getSqs() {
        return sqs;
    }

    public CompletableFuture<SendMessageResult> sendMessage(SendMessageRequest request) {
        CompletableFuture<SendMessageResult> future = new CompletableFuture<>();
        sqs.sendMessageAsync(request, new AsyncHandler<SendMessageRequest, SendMessageResult>() {
            @Override
            public void onError(Exception exception) {
                future.completeExceptionally(exception);
            }

            @Override
            public void onSuccess(SendMessageRequest request, SendMessageResult sendMessageResult) {
                future.complete(sendMessageResult);
            }
        });
        return future;
    }
    
    public CompletableFuture<ReceiveMessageResult> receiveMessage(ReceiveMessageRequest request) {
        CompletableFuture<ReceiveMessageResult> future = new CompletableFuture<>();
        sqs.receiveMessageAsync(request, new AsyncHandler<ReceiveMessageRequest, ReceiveMessageResult>() {
            @Override
            public void onError(Exception exception) {
                future.completeExceptionally(exception);
            }

            @Override
            public void onSuccess(ReceiveMessageRequest request, ReceiveMessageResult receiveMessageResult) {
                future.complete(receiveMessageResult);
            }
        });
        return future;
    }
    
    public CompletableFuture<Void> deleteMessage(DeleteMessageRequest request) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        sqs.deleteMessageAsync(request, new AsyncHandler<DeleteMessageRequest, Void>() {
            @Override
            public void onError(Exception exception) {
                future.completeExceptionally(exception);
            }

            @Override
            public void onSuccess(DeleteMessageRequest request, Void result) {
                future.complete(result);
            }
        });
        return future;
    }
    
    public CompletableFuture<GetQueueAttributesResult> getQueueAttributes(GetQueueAttributesRequest request) {
        CompletableFuture<GetQueueAttributesResult> future = new CompletableFuture<>();
        sqs.getQueueAttributesAsync(request, new AsyncHandler<GetQueueAttributesRequest, GetQueueAttributesResult>() {
            @Override
            public void onError(Exception exception) {
                future.completeExceptionally(exception);
            }

            @Override
            public void onSuccess(GetQueueAttributesRequest request, GetQueueAttributesResult getQueueAttributesResult) {
                future.complete(getQueueAttributesResult);
            }
        });
        return future;
    }
}
