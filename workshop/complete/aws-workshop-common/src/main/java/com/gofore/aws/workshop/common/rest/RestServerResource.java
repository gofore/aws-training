package com.gofore.aws.workshop.common.rest;

import java.util.Optional;
import java.util.function.Function;

import org.restlet.resource.ServerResource;

public abstract class RestServerResource extends ServerResource {
    
    protected Optional<String> getQueryValueAsString(String name) {
        return convertQueryValue(name, Function.identity());
    }
    
    protected Optional<Integer> getQueryValueAsInteger(String name) {
        return convertQueryValue(name, Integer::parseInt);
    }
    
    protected Optional<Long> getQueryValueAsLong(String name) {
        return convertQueryValue(name, Long::parseLong);
    }
    
    private <T> Optional<T> convertQueryValue(String name, Function<String, T> f) {
        return convert(getQueryValue(name), f);
    }
    
    private <T> Optional<T> convert(String value, Function<String, T> f) {
        if (value == null) {
            return Optional.empty();
        } else {
            return Optional.of(f.apply(value));
        }
    }
}
