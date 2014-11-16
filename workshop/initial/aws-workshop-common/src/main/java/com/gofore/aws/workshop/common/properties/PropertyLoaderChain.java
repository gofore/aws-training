package com.gofore.aws.workshop.common.properties;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class PropertyLoaderChain extends AbstractPropertyLoader {

    private final List<PropertyLoader> loaders = new ArrayList<>();
    
    public PropertyLoaderChain add(PropertyLoader loader) {
        loaders.add(loader);
        return this;
    }

    @Override
    public Optional<String> lookupOptional(String name) {
        return loaders.stream()
                .map(loader -> loader.lookupOptional(name))
                .flatMap(o -> o.map(Stream::of).orElseGet(Stream::empty))
                .findFirst();
    }
    
}
