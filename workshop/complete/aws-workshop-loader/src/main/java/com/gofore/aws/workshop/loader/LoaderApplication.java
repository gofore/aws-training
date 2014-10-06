package com.gofore.aws.workshop.loader;

import javax.inject.Inject;

import com.gofore.aws.workshop.common.rest.GuiceApplication;
import com.gofore.aws.workshop.common.rest.RestletServer;
import com.gofore.aws.workshop.loader.rest.GoogleImagesUpsertResource;
import com.gofore.aws.workshop.loader.rest.QueueAttributesResource;
import org.restlet.Restlet;
import org.restlet.ext.guice.FinderFactory;
import org.restlet.routing.Router;

public class LoaderApplication extends GuiceApplication {

    @Inject
    public LoaderApplication(FinderFactory finderFactory) {
        super(finderFactory);
    }

    @Override
    public Restlet createInboundRoot() {
        Router router = new Router(getContext());
        router.attach("/google/images", target(GoogleImagesUpsertResource.class));
        router.attach("/queue", target(QueueAttributesResource.class));
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
