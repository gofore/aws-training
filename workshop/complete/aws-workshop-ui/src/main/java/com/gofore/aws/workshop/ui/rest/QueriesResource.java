package com.gofore.aws.workshop.ui.rest;

import java.io.IOException;

import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gofore.aws.workshop.common.properties.ApplicationProperties;
import com.gofore.aws.workshop.common.rest.RestServerResource;
import com.gofore.aws.workshop.common.sqs.SqsClient;
import com.google.inject.Inject;
import org.restlet.resource.Put;

public class QueriesResource extends RestServerResource {
    
    private static final ObjectMapper MAPPER = new ObjectMapper();
    
    private final SqsClient sqsClient;
    private final String queueUrl;

    @Inject
    public QueriesResource(ApplicationProperties properties, SqsClient sqsClient) {
        this.sqsClient = sqsClient;
        this.queueUrl = properties.lookup("queries.queue.url");
    }

    @Put("json")
    public SendMessageResult sendQuery() throws Exception {
        String query = getQueryValueAsString("q").get();
        long limit = getQueryValueAsLong("l").orElse(1L);
        String message = createJson(query, limit);
        return sqsClient.sendMessage(new SendMessageRequest(queueUrl, message)).get();
    }
    
    private String createJson(String query, long limit) throws IOException {
        ObjectNode node = MAPPER.createObjectNode();
        node.put("q", query);
        node.put("l", limit);
        node.put("origin", queueUrl);
        return MAPPER.writeValueAsString(node);
    }
}
