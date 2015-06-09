package com.gofore.aws.workshop.common.properties;

import java.util.Map;
import java.util.Optional;

public class EnvironmentPropertyLoader extends AbstractPropertyLoader {

    private final Map<String, String> env;

    public EnvironmentPropertyLoader() {
        this.env = System.getenv();
    }

    @Override
    public Optional<String> lookupOptional(String name) {
        return Optional.ofNullable(env.get(name));
    }
}
