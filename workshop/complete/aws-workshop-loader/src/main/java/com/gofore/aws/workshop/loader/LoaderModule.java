package com.gofore.aws.workshop.loader;

import javax.inject.Singleton;

import com.gofore.aws.workshop.common.properties.ApplicationProperties;
import com.gofore.aws.workshop.common.sqs.SqsClient;
import com.gofore.aws.workshop.common.sqs.SqsClientProvider;
import com.gofore.aws.workshop.loader.rest.GoogleImagesUpsertResource;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class LoaderModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(LoaderApplication.class).in(Singleton.class);
        bind(GoogleImagesUpsertResource.class);
        
        bind(SqsClient.class).toProvider(SqsClientProvider.class).in(Singleton.class);
    }

    @Provides @Singleton
    public ApplicationProperties provideApplicationProperties() {
        return new ApplicationProperties().withSystemPropertyLoader()
                                          .withClasspathPropertyLoader("loader.properties");
    }
}
