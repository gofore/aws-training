package com.gofore.aws.workshop.common.sqs;

import static com.gofore.aws.workshop.common.async.FutureHelper.sequence;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.gofore.aws.workshop.common.async.Threads;
import com.gofore.aws.workshop.common.functional.Consumers;
import org.restlet.service.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqsService extends Service {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(SqsService.class);
    private static final long ARTIFICIAL_BUSY_MILLIS = 10000;
    private static final long WAIT_AFTER_FAILURE_MILLIS = 5000;
    private static final int LONG_POLL_SECONDS = 20;
    
    private final SqsClient sqsClient;
    private final String queueUrl;
    private final List<Function<Message, CompletableFuture<Message>>> messageHandlers;
    private Optional<BiConsumer<? super Message, ? super Throwable>> completeHandler;
    private Thread thread;

    public SqsService(SqsClient sqsClient, String queueUrl) {
        this.sqsClient = sqsClient;
        this.queueUrl = queueUrl;
        this.messageHandlers = new ArrayList<>();
        this.completeHandler = Optional.empty();
    }

    public SqsService addMessageHandler(Function<Message, CompletableFuture<Message>> messageHandler) {
        this.messageHandlers.add(messageHandler);
        return this;
    }
    
    public SqsService setCompleteHandler(BiConsumer<? super Message, ? super Throwable> completeHandler) {
        this.completeHandler= Optional.of(completeHandler);
        return this;
    }
    
    @Override
    public synchronized void start() throws Exception {
        if (isEnabled() && thread == null) {
            super.start();
            this.thread = new Thread(() -> {
                // loop that polls new messages with maximum long poll time
                while (isStarted()) {
                    try {
                        ReceiveMessageRequest request = new ReceiveMessageRequest(queueUrl)
                                .withWaitTimeSeconds(LONG_POLL_SECONDS);
                        ReceiveMessageResult result = sqsClient.getSqs().receiveMessage(request);
                        
                        // apply the message handle chain and finally delete message if
                        // everything else succeeds
                        result.getMessages().stream()
                                .map(this::handleMessage)
                                .map(this::completeMessage)
                                .map(this::deleteMessage)
                                .collect(Collectors.toList()); // required to terminate the stream
                        
                        Threads.sleep(ARTIFICIAL_BUSY_MILLIS);
                    } catch (RuntimeException ex) {
                        LOGGER.warn("SQS receive or message handling failed", ex);
                        Threads.sleep(WAIT_AFTER_FAILURE_MILLIS);
                    }
                }
            });
            this.thread.start();
        }
    }

    @Override
    public synchronized void stop() throws Exception {
        super.stop();
        if (thread != null) {
            thread.join();
            thread = null;
        }
    }

    /**
     * Starts message handling by applying all the message handlers.
     * 
     * @param message the sqs message
     * @return future that is completed when all message handlers are completed
     */
    protected CompletableFuture<Message> handleMessage(Message message) {
        return sequence(messageHandlers.stream().map(h -> h.apply(message)))
                .whenComplete(Consumers.consumer(
                        (v) -> LOGGER.info("Successfully handled {} with {} handler(s)", message, messageHandlers.size()),
                        (e) -> LOGGER.error("Failed to handle {}", message, e)
                )).thenApply(s -> message);
    }

    /**
     * Applies the complete handler if one is defined.
     * 
     * @param message the sqs message
     * @return future that is completed when optional complete handler is completed
     */
    protected CompletableFuture<Message> completeMessage(CompletableFuture<Message> message) {
        return completeHandler.map(message::whenComplete).orElse(message);
    }

    /**
     * Deletes the handled message.
     * 
     * @param message the sqs message
     * @return future that is completed when the message delete is succeeded or failed
     */
    protected CompletableFuture<Message> deleteMessage(CompletableFuture<Message> message) {
        return message.thenCompose(m -> {
            DeleteMessageRequest request = new DeleteMessageRequest(queueUrl, m.getReceiptHandle());
            return sqsClient.deleteMessage(request).whenComplete(Consumers.consumer(
                    (v) -> LOGGER.info("Successfully deleted {}", m),
                    (e) -> LOGGER.error("Failed to delete {}", m, e)
            )).thenApply(v -> m);
        });
    }
}
