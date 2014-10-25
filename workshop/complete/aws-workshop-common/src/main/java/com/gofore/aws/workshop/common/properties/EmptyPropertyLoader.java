package com.gofore.aws.workshop.common.properties;

import java.util.Optional;

public class EmptyPropertyLoader extends AbstractPropertyLoader {

    @Override
    public Optional<String> lookupOptional(String name) {
        return Optional.empty();
    }
}
