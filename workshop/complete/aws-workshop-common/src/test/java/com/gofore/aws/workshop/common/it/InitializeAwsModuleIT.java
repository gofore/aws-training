package com.gofore.aws.workshop.common.it;

import static org.junit.Assert.*;

import javax.inject.Singleton;

import com.amazonaws.services.identitymanagement.AmazonIdentityManagementAsync;
import com.amazonaws.services.identitymanagement.model.User;
import com.gofore.aws.workshop.common.di.AwsModule;
import com.gofore.aws.workshop.common.properties.ApplicationProperties;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Stage;
import org.junit.Test;

public class InitializeAwsModuleIT {
    
    @Test
    public void testAwsModuleInitialization() {
        Injector injector = Guice.createInjector(Stage.PRODUCTION, new AwsModule() {
            @Provides
            @Singleton
            public ApplicationProperties applicationProperties() {
                return new ApplicationProperties()
                        .withAwsCredentialsCsvLoader("aws-workshop-credentials.csv")
                        .withClasspathPropertyLoader("common.properties");
            }
        });
        ApplicationProperties properties = injector.getInstance(ApplicationProperties.class);
        AmazonIdentityManagementAsync iam = injector.getInstance(AmazonIdentityManagementAsync.class);
        User user = iam.getUser().getUser();
        assertEquals(properties.lookup("aws.user"), user.getUserName());
    }
    
}
