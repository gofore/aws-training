package com.gofore.aws.workshop.loader;

import javax.inject.Inject;

import com.gofore.aws.workshop.common.properties.ApplicationProperties;
import com.gofore.aws.workshop.common.rest.GuiceApplication;
import com.gofore.aws.workshop.common.rest.RestletServer;
import com.gofore.aws.workshop.common.sqs.SqsClient;
import com.gofore.aws.workshop.common.sqs.SqsService;
import com.gofore.aws.workshop.loader.pages.QueriesMessageHandler;
import com.gofore.aws.workshop.loader.rest.GoogleImagesUpsertResource;
import com.google.inject.Singleton;
import org.restlet.Restlet;
import org.restlet.ext.guice.FinderFactory;
import org.restlet.routing.Router;

@Singleton
public class LoaderApplication extends GuiceApplication {

    @Inject
    public LoaderApplication(ApplicationProperties properties, FinderFactory finderFactory,
                             SqsClient sqsClient, QueriesMessageHandler queriesMessageHandler) {
        super(finderFactory);
        SqsService sqsService = new SqsService(sqsClient, properties.lookup("queries.queue.url"))
                .addMessageHandler(queriesMessageHandler);
        getServices().add(sqsService);
    }

    @Override
    public Restlet createInboundRoot() {
        Router router = new Router(getContext());
        router.attach("/google/images", target(GoogleImagesUpsertResource.class));
        return router;
    }
    
    public static void main(String[] args) throws Exception {
        new RestletServer()
                .port(9002)
                .modules(new LoaderModule())
                .application(LoaderApplication.class)
                .start();
    }
}
