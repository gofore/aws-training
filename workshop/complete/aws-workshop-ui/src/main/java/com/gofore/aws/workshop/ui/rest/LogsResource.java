package com.gofore.aws.workshop.ui.rest;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.gofore.aws.workshop.common.functional.Consumers;
import com.gofore.aws.workshop.common.properties.CloudFormationOutputsPropertyLoader;
import com.gofore.aws.workshop.common.rest.RestServerResource;
import com.gofore.aws.workshop.common.sqs.SqsClient;
import com.google.inject.Inject;
import org.restlet.resource.Get;

public class LogsResource extends RestServerResource {
    
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final SqsClient sqsClient;
    private final String queueUrl;

    @Inject
    public LogsResource(CloudFormationOutputsPropertyLoader properties, SqsClient sqsClient) {
        this.sqsClient = sqsClient;
        this.queueUrl = properties.lookup("QueueLogsUrl");
    }
    
    @Get("json")
    public JsonNode logs() {
        ReceiveMessageRequest request = new ReceiveMessageRequest(queueUrl)
                .withWaitTimeSeconds(20)
                .withMaxNumberOfMessages(10);
        return sqsClient.receiveMessage(request)
                .thenApply(ReceiveMessageResult::getMessages)
                .whenComplete(Consumers.onSuccess(msgs -> msgs.forEach(deleteMessage())))
                .thenApply(msgs -> msgs.stream().map(readNode()))
                .thenApply(toArray())
                .join();
    }
    
    private Function<Message, JsonNode> readNode() {
        return m -> {
            try {
                return MAPPER.readTree(m.getBody());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        };
    }
    
    private Function<Stream<JsonNode>, JsonNode> toArray() {
        return nodes -> {
            ArrayNode array = MAPPER.createArrayNode();
            nodes.forEachOrdered(array::add);
            return array;
        };
    }
    
    private Consumer<Message> deleteMessage() {
        return m -> sqsClient.deleteMessage(
                new DeleteMessageRequest(queueUrl, m.getReceiptHandle())
        );
    }
}
