package com.gofore.aws.workshop.common.rest;

import java.lang.annotation.Annotation;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.ext.guice.FinderFactory;
import org.restlet.resource.ServerResource;

public abstract class GuiceApplication extends Application {
    
    private final FinderFactory finderFactory;

    protected GuiceApplication(FinderFactory finderFactory) {
        this.finderFactory = finderFactory;
    }
    
    protected Restlet target(Class<? extends ServerResource> type) {
        return finderFactory.finder(type);
    }
    
    protected Restlet target(Class<? extends ServerResource> type, Class<? extends Annotation> qualifier) {
        return finderFactory.finder(type, qualifier);
    }
}
