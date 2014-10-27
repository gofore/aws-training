package com.gofore.aws.workshop.loader;

import javax.inject.Singleton;

import com.gofore.aws.workshop.common.di.AwsModule;
import com.gofore.aws.workshop.common.properties.ApplicationProperties;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class LoaderModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new AwsModule());
    }

    @Provides @Singleton
    public ApplicationProperties applicationProperties() {
        return new ApplicationProperties().withSystemPropertyLoader()
                                          .withAwsCredentialsCsvLoader("aws-workshop-credentials.csv")
                                          .withClasspathPropertyLoader("loader.properties")
                                          .withClasspathPropertyLoader("common.properties");
    }
}
