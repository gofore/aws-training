package com.gofore.aws.workshop.common.simpledb;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import com.gofore.aws.workshop.common.properties.ApplicationProperties;

@Singleton
public class SimpleDBClientProvider implements Provider<SimpleDBClient> {

    private final ApplicationProperties properties;

    @Inject
    public SimpleDBClientProvider(ApplicationProperties properties) {
        this.properties = properties;
    }

    @Override
    public SimpleDBClient get() {
        String accessKey = properties.lookup("aws.access.key");
        String secretKey = properties.lookup("aws.secret.key");
        String endpoint = properties.lookup("aws.simpledb.endpoint");
        return new SimpleDBClient(accessKey, secretKey, endpoint);
    }
}
