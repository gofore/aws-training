package com.gofore.aws.workshop.common.s3;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import com.gofore.aws.workshop.common.properties.ApplicationProperties;

@Singleton
public class S3ClientProvider implements Provider<S3Client> {

    private final ApplicationProperties properties;

    @Inject
    public S3ClientProvider(ApplicationProperties properties) {
        this.properties = properties;
    }

    @Override
    public S3Client get() {
        String accessKey = properties.lookup("aws.access.key");
        String secretKey = properties.lookup("aws.secret.key");
        String endpoint = properties.lookup("aws.s3.endpoint");
        return new S3Client(accessKey, secretKey, endpoint);
    }
}
