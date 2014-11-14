package com.gofore.aws.workshop.fetcher;

import javax.inject.Inject;

import com.gofore.aws.workshop.common.properties.ApplicationProperties;
import com.gofore.aws.workshop.common.rest.GuiceApplication;
import com.gofore.aws.workshop.common.rest.RestletServer;
import com.gofore.aws.workshop.common.sqs.SqsClient;
import com.gofore.aws.workshop.common.sqs.SqsService;
import com.gofore.aws.workshop.fetcher.images.ImagesMessageHandler;
import com.google.inject.Singleton;
import org.restlet.ext.guice.FinderFactory;

@Singleton
public class FetcherApplication extends GuiceApplication {

    @Inject
    public FetcherApplication(ApplicationProperties properties, FinderFactory finderFactory,
                              SqsClient sqsClient, ImagesMessageHandler imagesMessageHandler) {
        super(finderFactory);
        SqsService sqsService = new SqsService(sqsClient, properties.lookup("images.queue.url"))
                .addMessageHandler(imagesMessageHandler);
        getServices().add(sqsService);
    }

    public static void main(String[] args) throws Exception {
        new RestletServer()
                .port(9003)
                .modules(new FetcherModule())
                .application(FetcherApplication.class)
                .start();
    }
}
