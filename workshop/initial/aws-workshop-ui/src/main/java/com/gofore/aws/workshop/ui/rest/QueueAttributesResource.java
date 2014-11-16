package com.gofore.aws.workshop.ui.rest;

import javax.inject.Inject;

import com.amazonaws.services.sqs.model.GetQueueAttributesResult;
import com.gofore.aws.workshop.common.properties.ApplicationProperties;
import com.gofore.aws.workshop.common.rest.RestServerResource;
import com.gofore.aws.workshop.common.sqs.SqsClient;
import org.restlet.resource.Get;

public class QueueAttributesResource extends RestServerResource {

    private final ApplicationProperties properties;
    private final SqsClient sqsClient;
    
    @Inject
    public QueueAttributesResource(ApplicationProperties properties, SqsClient sqsClient) {
        this.properties = properties;
        this.sqsClient = sqsClient;
    }
    
    @Get("json")
    public GetQueueAttributesResult getAttributes() throws Exception {
        String queueUrl = properties.lookup(getAttribute("name"));
        String attributeNames = getQueryValueAsString("attr").orElse("All");
        // TODO: Task 1: SQS request
        /**
         * @see com.amazonaws.services.sqs.AmazonSQS#getQueueAttributes(com.amazonaws.services.sqs.model.GetQueueAttributesRequest)
         */
        throw new UnsupportedOperationException("Task 1 not implemented");
    }
}
