package com.gofore.aws.workshop.loader;

import java.util.concurrent.ExecutorService;
import javax.inject.Inject;

import com.gofore.aws.workshop.common.properties.ApplicationProperties;
import com.gofore.aws.workshop.common.rest.GuiceApplication;
import com.gofore.aws.workshop.common.rest.RestletServer;
import com.gofore.aws.workshop.common.sqs.SqsClient;
import com.gofore.aws.workshop.common.sqs.SqsService;
import com.gofore.aws.workshop.loader.rest.GoogleImagesUpsertResource;
import com.gofore.aws.workshop.loader.service.GoogleImagesHandler;
import com.google.inject.Singleton;
import org.restlet.Restlet;
import org.restlet.ext.guice.FinderFactory;
import org.restlet.routing.Router;

@Singleton
public class LoaderApplication extends GuiceApplication {

    @Inject
    public LoaderApplication(ApplicationProperties properties, FinderFactory finderFactory, SqsClient sqsClient, ExecutorService sqsExecutor) {
        super(finderFactory);
        getServices().add(createSqsService(properties, sqsClient, sqsExecutor));
    }

    @Override
    public Restlet createInboundRoot() {
        Router router = new Router(getContext());
        router.attach("/google/images", target(GoogleImagesUpsertResource.class));
        return router;
    }

    private SqsService createSqsService(ApplicationProperties properties, SqsClient sqsClient, ExecutorService sqsExecutor) {
        return new SqsService(sqsClient, properties.lookup("queries.queue.url"), sqsExecutor)
                .addHandler(new GoogleImagesHandler(properties, sqsClient));
    }
    
    public static void main(String[] args) throws Exception {
        new RestletServer()
                .port(9002)
                .modules(new LoaderModule())
                .application(LoaderApplication.class)
                .start();
    }
}
