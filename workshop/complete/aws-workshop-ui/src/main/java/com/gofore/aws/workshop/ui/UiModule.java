package com.gofore.aws.workshop.ui;

import javax.inject.Singleton;

import com.google.inject.AbstractModule;

public class UiModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(UiApplication.class).in(Singleton.class);
    }
}
