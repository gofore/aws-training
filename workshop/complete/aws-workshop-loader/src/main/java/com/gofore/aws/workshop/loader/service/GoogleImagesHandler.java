package com.gofore.aws.workshop.loader.service;

import static com.gofore.aws.workshop.common.async.FutureHelper.sequence;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Stream;

import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gofore.aws.workshop.common.functional.Consumers;
import com.gofore.aws.workshop.common.properties.ApplicationProperties;
import com.gofore.aws.workshop.common.sqs.SqsClient;
import com.gofore.aws.workshop.loader.pages.GoogleImagesFinder;
import com.gofore.aws.workshop.loader.pages.PageFinder;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class GoogleImagesHandler implements Consumer<Message> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleImagesHandler.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();
    
    private final SqsClient sqsClient;
    private final String queueUrl;

    @Inject
    public GoogleImagesHandler(ApplicationProperties properties, SqsClient sqsClient) {
        this.sqsClient = sqsClient;
        this.queueUrl = properties.lookup("images.queue.url");
    }

    @Override
    public void accept(Message message) {
        try {
            JsonNode root = MAPPER.readTree(message.getBody());
            String query = root.get("q").asText();
            Optional<Long> limit = Optional.ofNullable(root.get("l").numberValue()).map(Number::longValue);
            String originQueueUrl = root.get("origin").asText();
            PageFinder pageFinder = new GoogleImagesFinder(query, limit);
            Stream<String> pageUrls = pageFinder.findPageUrls();
            sequence(pageUrls.map(this::sendUrl)).thenRun(() -> deleteMessage(message, originQueueUrl));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private CompletableFuture<SendMessageResult> sendUrl(String url) {
        SendMessageRequest request = new SendMessageRequest(queueUrl, url);
        return sqsClient.sendMessage(request).whenComplete(Consumers.consumer(
                (v) -> LOGGER.info("Successfully sent {} to {}", url, queueUrl),
                (e) -> LOGGER.error("Failed to send {} to {}", url, queueUrl, e)
        ));
    }
    
    private CompletableFuture<Void> deleteMessage(Message message, String originQueueUrl) {
        DeleteMessageRequest request = new DeleteMessageRequest(originQueueUrl, message.getReceiptHandle());
        return sqsClient.deleteMessage(request).whenComplete(Consumers.consumer(
                (v) -> LOGGER.info("Successfully deleted {}", message),
                (e) -> LOGGER.error("Failed to delete {}", message, e)
        ));
    }
}
