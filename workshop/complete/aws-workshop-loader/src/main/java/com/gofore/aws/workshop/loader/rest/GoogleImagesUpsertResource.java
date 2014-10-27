package com.gofore.aws.workshop.loader.rest;

import static com.gofore.aws.workshop.common.async.FutureHelper.sequence;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.inject.Inject;

import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.gofore.aws.workshop.common.properties.ApplicationProperties;
import com.gofore.aws.workshop.common.rest.RestServerResource;
import com.gofore.aws.workshop.common.sqs.SqsClient;
import com.gofore.aws.workshop.loader.pages.GoogleImagesFinder;
import com.gofore.aws.workshop.loader.pages.PageFinder;
import org.restlet.resource.Put;

public class GoogleImagesUpsertResource extends RestServerResource {
    
    private final SqsClient sqsClient;
    private final String queueUrl;

    @Inject
    public GoogleImagesUpsertResource(ApplicationProperties properties, SqsClient sqsClient) {
        this.sqsClient = sqsClient;
        this.queueUrl = properties.lookup("images.queue.url");
    }

    @Put("json")
    public List<SendMessageResult> upsertImages() throws Exception {
        Optional<String> query = getQueryValueAsString("q");
        Optional<Long> limit = getQueryValueAsLong("l");
        PageFinder pageFinder = new GoogleImagesFinder(query.get(), limit);
        Stream<String> pageUrls = pageFinder.findPageUrls();
        Stream<SendMessageResult> results = sequence(pageUrls.map(this::sendUrl)).join();
        return results.collect(Collectors.toList());
    }

    private CompletableFuture<SendMessageResult> sendUrl(String url) {
        SendMessageRequest request = new SendMessageRequest(queueUrl, url);
        return sqsClient.sendMessage(request);
    }
    
}
