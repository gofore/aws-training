package com.gofore.aws.workshop.common.rest;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Injector;
import com.google.inject.Module;
import org.restlet.Application;
import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.ext.guice.RestletGuice;

public class RestletServer {

    static {
        try {
            Class.forName("org.restlet.ext.slf4j.Slf4jLoggerFacade");
            Class.forName("org.slf4j.Logger");
            System.setProperty("org.restlet.engine.loggerFacadeClass", "org.restlet.ext.slf4j.Slf4jLoggerFacade");
        } catch (ClassNotFoundException ex) {
            // slf4j or restlet slf4j extension not found
        }
    }
    
    private int port;
    private Class<? extends Application> application;
    private final List<Module> modules = new ArrayList<>();
    
    public RestletServer port(int port) {
        this.port = port;
        return this;
    }
    
    public RestletServer application(Class<? extends Application> application) {
        this.application = application;
        return this;
    }
    
    public RestletServer modules(Module... modules) {
        for (Module module : modules) {
            this.modules.add(module);
        }
        return this;
    }
    
    public void start() throws Exception {
        Injector injector = RestletGuice.createInjector(modules);
        Application instance = injector.getInstance(application);
        Component component = new Component();
        component.getServers().add(Protocol.HTTP, port);
        component.getClients().add(Protocol.FILE);
        component.getClients().add(Protocol.CLAP);
        component.getDefaultHost().attach(instance);
        component.start();
    }
    
}
