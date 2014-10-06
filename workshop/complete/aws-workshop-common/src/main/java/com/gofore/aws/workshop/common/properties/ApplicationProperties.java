package com.gofore.aws.workshop.common.properties;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;

public class ApplicationProperties implements PropertyLoader {

    private final List<PropertyLoader> loaders = new ArrayList<>();
    
    public ApplicationProperties withSystemPropertyLoader() {
        loaders.add(new SystemPropertyLoader());
        return this;
    }
    
    public ApplicationProperties withClasspathPropertyLoader(String propertyFile) {
        loaders.add(new ClasspathPropertyLoader(propertyFile));
        return this;
    }

    @Override
    public String lookup(String name) throws NoSuchElementException {
        return lookupOptional(name).get();
    }

    @Override
    public Optional<String> lookupOptional(String name) {
        return loaders.stream()
                .map(loader -> loader.lookupOptional(name))
                .flatMap(o -> o.map(Stream::of).orElseGet(Stream::empty))
                .findFirst();
    }
}
