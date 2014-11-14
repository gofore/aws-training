package com.gofore.aws.workshop.loader.pages;

import static com.gofore.aws.workshop.common.async.FutureHelper.sequence;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Stream;

import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gofore.aws.workshop.common.functional.Consumers;
import com.gofore.aws.workshop.common.properties.ApplicationProperties;
import com.gofore.aws.workshop.common.sqs.SqsClient;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class QueriesMessageHandler implements Function<Message, CompletableFuture<Message>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueriesMessageHandler.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();
    
    private final SqsClient sqsClient;
    private final String queueUrl;

    @Inject
    public QueriesMessageHandler(ApplicationProperties properties, SqsClient sqsClient) {
        this.sqsClient = sqsClient;
        this.queueUrl = properties.lookup("images.queue.url");
    }

    @Override
    public CompletableFuture<Message> apply(Message message) {
        try {
            JsonNode root = MAPPER.readTree(message.getBody());
            String query = root.get("q").asText();
            Optional<Long> limit = Optional.ofNullable(root.get("l").numberValue()).map(Number::longValue);
            PageFinder pageFinder = new GoogleImagesFinder(query, limit);
            Stream<String> pageUrls = pageFinder.findPageUrls();
            return sequence(pageUrls.map(this::sendUrl)).thenApply(s -> message);
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
}
