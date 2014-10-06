package com.gofore.aws.workshop.fetcher.images;

import java.util.function.Consumer;
import java.util.stream.Stream;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.gofore.aws.workshop.common.async.FutureHelper;
import com.gofore.aws.workshop.common.functional.Consumers;
import com.gofore.aws.workshop.common.properties.ApplicationProperties;
import com.gofore.aws.workshop.common.sqs.SqsClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class Receiver {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);
    
    private final SqsClient sqsClient;
    private final ImageFetcher imageFetcher;
    private final Updater updater;
    private final String queueUrl;

    private volatile boolean running; 
    
    @Inject
    public Receiver(ApplicationProperties properties, SqsClient sqsClient, ImageFetcher imageFetcher, Updater updater) {
        this.sqsClient = sqsClient;
        this.imageFetcher = imageFetcher;
        this.updater = updater;
        this.queueUrl = properties.lookup("aws.sqs.queue.url");
    }
    
    public void start() {
        running = true;
        while (running) {
            try {
                ReceiveMessageRequest request = new ReceiveMessageRequest(queueUrl).withWaitTimeSeconds(20);
                ReceiveMessageResult result = sqsClient.receiveMessage(request).get();
                result.getMessages().stream().forEach(this::handleMessage);
            } catch (Exception ex) {
                LOGGER.error("Failed to receive mesage from {}", queueUrl, ex);
            }
        }
    }
    
    public void stop() {
        running = false;
    }
    
    private void handleMessage(Message message) {
        imageFetcher.fetchImages(message.getBody())
                .thenApply(images -> images.map(updater::update))
                .thenCompose(FutureHelper::sequence)
                .whenComplete(Consumers.consumer(
                        handleUpdated(message),
                        (e) -> LOGGER.error("Failed to handle message {}", message, e)
                ));
    }
    
    private Consumer<Stream<Image>> handleUpdated(Message message) {
        return (images) -> {
            DeleteMessageRequest request = new DeleteMessageRequest(queueUrl, message.getReceiptHandle());
            sqsClient.deleteMessage(request).whenComplete(Consumers.consumer(
                    (v) -> LOGGER.info("Successfully deleted {}", message),
                    (e) -> LOGGER.error("Failed to delete {}", message, e)
            ));
        };
    }
}
