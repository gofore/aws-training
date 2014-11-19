package com.gofore.aws.workshop.ui.rest;

import java.io.IOException;
import java.util.Optional;

import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gofore.aws.workshop.common.functional.Consumers;
import com.gofore.aws.workshop.common.properties.CloudFormationOutputsPropertyLoader;
import com.gofore.aws.workshop.common.rest.RestServerResource;
import com.gofore.aws.workshop.common.sqs.SqsClient;
import com.google.inject.Inject;
import org.restlet.resource.Put;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueriesResource extends RestServerResource {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(QueriesResource.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();
    
    private final SqsClient sqsClient;
    private final String queueUrl;

    @Inject
    public QueriesResource(CloudFormationOutputsPropertyLoader properties, SqsClient sqsClient) {
        this.sqsClient = sqsClient;
        this.queueUrl = properties.lookup("QueueQueriesUrl");
    }

    @Put("json")
    public SendMessageResult submitQuery(SubmitRequest request) throws Exception {
        String query = Optional.of(request.query).get();
        long limit = Optional.ofNullable(request.limit).orElse(1L);
        String messageBody = createJson(query, limit);
        // TODO: Task 2: SQS message send
        /**
         * @see com.amazonaws.services.sqs.AmazonSQS#sendMessage(com.amazonaws.services.sqs.model.SendMessageRequest)
         */
        return sqsClient
                .sendMessage(new SendMessageRequest(queueUrl, messageBody))
                .whenComplete(Consumers.consumer(
                        (v) -> LOGGER.info("Successfully sent {} to {}", messageBody, queueUrl),
                        (e) -> LOGGER.error("Failed to send {} to {}", messageBody, queueUrl, e)
                )).join();
    }
    
    private String createJson(String query, long limit) throws IOException {
        ObjectNode node = MAPPER.createObjectNode();
        node.put("q", query);
        node.put("l", limit);
        return MAPPER.writeValueAsString(node);
    }
    
    private static class SubmitRequest {
        @JsonProperty("q") public String query;
        @JsonProperty("l") public Long limit;
    }
}
