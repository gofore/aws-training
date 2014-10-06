package com.gofore.aws.workshop.fetcher;

import javax.inject.Inject;

import com.gofore.aws.workshop.fetcher.images.Receiver;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class FetcherApplication implements Runnable {

    private final Receiver receiver;

    @Inject
    public FetcherApplication(Receiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void run() {
        receiver.start();
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new FetcherModule());
        FetcherApplication application = injector.getInstance(FetcherApplication.class);
        application.run();
    }
}
