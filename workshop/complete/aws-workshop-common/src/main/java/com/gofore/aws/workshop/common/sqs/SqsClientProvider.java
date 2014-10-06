package com.gofore.aws.workshop.common.sqs;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import com.gofore.aws.workshop.common.properties.ApplicationProperties;

@Singleton
public class SqsClientProvider implements Provider<SqsClient> {

    private final ApplicationProperties properties;

    @Inject
    public SqsClientProvider(ApplicationProperties properties) {
        this.properties = properties;
    }

    @Override
    public SqsClient get() {
        String accessKey = properties.lookup("aws.access.key");
        String secretKey = properties.lookup("aws.secret.key");
        String endpoint = properties.lookup("aws.sqs.endpoint");
        return new SqsClient(accessKey, secretKey, endpoint);
    }
}
