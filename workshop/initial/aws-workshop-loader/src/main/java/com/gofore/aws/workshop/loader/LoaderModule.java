package com.gofore.aws.workshop.loader;

import com.gofore.aws.workshop.common.cloudformation.CloudFormationClient;
import com.gofore.aws.workshop.common.di.AwsModule;
import com.gofore.aws.workshop.common.properties.ApplicationProperties;
import com.gofore.aws.workshop.common.properties.CloudFormationOutputsPropertyLoader;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class LoaderModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new AwsModule());
    }

    @Provides
    @Singleton
    public ApplicationProperties applicationProperties() {
        return new ApplicationProperties()
                .withSystemPropertyLoader()
                .withEnvironmentPropertyLoader()
                .withAwsCredentialsCsvLoader("credentials.csv")
                .withAwsCredentialsEnvLoader()
                .withClasspathPropertyLoader("common.properties");
    }

    @Provides
    @Singleton
    public CloudFormationOutputsPropertyLoader cloudFormationOutputsPropertyLoader(
            ApplicationProperties properties, CloudFormationClient cloudFormationClient) {
        return new CloudFormationOutputsPropertyLoader(properties, cloudFormationClient);
    }
}
