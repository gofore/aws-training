package com.gofore.aws.workshop.ui;

import javax.inject.Inject;

import com.gofore.aws.workshop.common.rest.ConfigurationResource;
import com.gofore.aws.workshop.common.rest.GuiceApplication;
import com.gofore.aws.workshop.common.rest.RestletServer;
import com.gofore.aws.workshop.common.rest.UtfDirectory;
import com.gofore.aws.workshop.ui.rest.HealthCheckResource;
import com.gofore.aws.workshop.ui.rest.QueriesResource;
import com.gofore.aws.workshop.ui.rest.QueueAttributesResource;
import com.gofore.aws.workshop.ui.rest.SearchResource;
import com.google.inject.Singleton;
import org.restlet.Restlet;
import org.restlet.ext.guice.FinderFactory;
import org.restlet.resource.Directory;
import org.restlet.routing.Redirector;
import org.restlet.routing.Router;
import org.restlet.routing.Template;

@Singleton
public class UiApplication extends GuiceApplication {

    private static final String INDEX = "index.html";
    
    @Inject
    public UiApplication(FinderFactory finderFactory) {
        super(finderFactory);
    }

    @Override
    public Restlet createInboundRoot() {
        // this is a workaround for restlet not able to handle static index for classpath resources
        Redirector redirector = new Redirector(getContext(), INDEX, Redirector.MODE_CLIENT_PERMANENT);
        
        Router router = new Router(getContext());
        router.attach("/api/properties/{name}", target(ConfigurationResource.class));
        router.attach("/api/queues/{name}", target(QueueAttributesResource.class));
        router.attach("/api/queries", target(QueriesResource.class));
        router.attach("/api/search", target(SearchResource.class));
        router.attach("/healthcheck",target(HealthCheckResource.class));
        router.attach("/webjars", createWebjars());
        router.attach("/", redirector).setMatchingMode(Template.MODE_EQUALS);
        router.attach("/", createRoot());
        return router;
    }
   
    private Directory createRoot() {
        Directory directory = new UtfDirectory(getContext(), "clap://class/static/");
        directory.setDeeplyAccessible(true);
        directory.setIndexName(INDEX);
        return directory;
    }
    
    private Directory createWebjars() {
        return new UtfDirectory(getContext(), "clap://class/META-INF/resources/webjars");
    }

    public static void main(String[] args) throws Exception {
        new RestletServer()
                .port(9001)
                .modules(new UiModule())
                .application(UiApplication.class)
                .start();
    }
}
