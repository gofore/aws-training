package com.gofore.aws.workshop.common.properties;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Loads AWS access and secret key from environment variables in addition with
 * AWS user name.
 */
public class AwsCredentialsEnvLoader extends AbstractPropertyLoader {

    private final Map<String, String> properties;

    public AwsCredentialsEnvLoader() {
        properties = new HashMap<>();
        Optional.ofNullable(System.getenv("AWS_WORKSHOP_USER"))
                .ifPresent(v -> properties.put("aws.user", v));
        Optional.ofNullable(System.getenv("AWS_ACCESS_KEY"))
                .ifPresent(v -> properties.put("aws.access.key", v));
        Optional.ofNullable(System.getenv("AWS_SECRET_KEY"))
                .ifPresent(v -> properties.put("aws.secret.key", v));
    }

    @Override
    public Optional<String> lookupOptional(String name) {
        return Optional.ofNullable(properties.get(name));
    }
}
