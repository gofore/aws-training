package com.gofore.aws.workshop.ui.rest;

import javax.inject.Inject;

import com.amazonaws.services.sqs.model.GetQueueAttributesRequest;
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
        String url = properties.lookup(getAttribute("name"));
        String attributes = getQueryValueAsString("attr").orElse("All");
        GetQueueAttributesRequest request = new GetQueueAttributesRequest()
                .withQueueUrl(url)
                .withAttributeNames(attributes);
        return sqsClient.getQueueAttributes(request).join();
    }
}
