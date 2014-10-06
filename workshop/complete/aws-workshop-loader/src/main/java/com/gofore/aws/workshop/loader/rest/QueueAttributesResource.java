package com.gofore.aws.workshop.loader.rest;

import javax.inject.Inject;

import com.amazonaws.services.sqs.model.GetQueueAttributesRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesResult;
import com.gofore.aws.workshop.common.properties.ApplicationProperties;
import com.gofore.aws.workshop.common.rest.RestServerResource;
import com.gofore.aws.workshop.common.sqs.SqsClient;
import org.restlet.resource.Get;

public class QueueAttributesResource extends RestServerResource {

    private final SqsClient sqsClient;
    private final String queueUrl;
    
    @Inject
    public QueueAttributesResource(ApplicationProperties properties, SqsClient sqsClient) {
        this.sqsClient = sqsClient;
        this.queueUrl = properties.lookup("aws.sqs.queue.url");
    }
    
    @Get("json")
    public GetQueueAttributesResult getAttributes() {
        String url = getQueryValueAsString("url").orElse(queueUrl);
        String attr = getQueryValueAsString("attr").orElse("All");
        GetQueueAttributesRequest request = new GetQueueAttributesRequest()
                .withQueueUrl(url)
                .withAttributeNames(attr);
        return sqsClient.getQueueAttributes(request).join();
    }
}
