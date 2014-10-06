package com.gofore.aws.workshop.common.properties;

import java.util.NoSuchElementException;
import java.util.Optional;

public class SystemPropertyLoader implements PropertyLoader {

    @Override
    public String lookup(String name) throws NoSuchElementException {
        return lookupOptional(name).get();
    }

    @Override
    public Optional<String> lookupOptional(String name) {
        return Optional.ofNullable(System.getProperty(name));
    }
}
