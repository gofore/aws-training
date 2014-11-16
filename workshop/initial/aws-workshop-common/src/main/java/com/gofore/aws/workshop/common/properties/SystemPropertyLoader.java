package com.gofore.aws.workshop.common.properties;

import java.util.Optional;

public class SystemPropertyLoader extends AbstractPropertyLoader {

    @Override
    public Optional<String> lookupOptional(String name) {
        return Optional.ofNullable(System.getProperty(name));
    }
}
