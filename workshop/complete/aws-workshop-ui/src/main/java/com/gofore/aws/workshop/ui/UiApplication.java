package com.gofore.aws.workshop.ui;

import javax.inject.Inject;

import com.gofore.aws.workshop.common.rest.GuiceApplication;
import com.gofore.aws.workshop.common.rest.RestletServer;
import org.restlet.Restlet;
import org.restlet.ext.guice.FinderFactory;
import org.restlet.resource.Directory;
import org.restlet.routing.Router;

public class UiApplication extends GuiceApplication {

    @Inject
    public UiApplication(FinderFactory finderFactory) {
        super(finderFactory);
    }

    @Override
    public Restlet createInboundRoot() {
        Router router = new Router(getContext());
        router.attach("/", createStaticResources());
        return router;
    }
    
    private Directory createStaticResources() {
        Directory directory = new Directory(getContext(), "clap://class/static/");
        directory.setDeeplyAccessible(true);
        directory.setIndexName("index.html");
        return directory;
    }

    public static void main(String[] args) throws Exception {
        new RestletServer()
                .port(9001)
                .modules(new UiModule())
                .application(UiApplication.class)
                .start();
    }
}
