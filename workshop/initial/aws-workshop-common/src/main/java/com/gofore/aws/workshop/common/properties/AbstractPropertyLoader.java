package com.gofore.aws.workshop.common.properties;

import java.nio.charset.Charset;
import java.util.NoSuchElementException;
import java.util.Optional;

public abstract class AbstractPropertyLoader implements PropertyLoader {

    protected static final Charset PROPERTIES_ENCODING = Charset.forName("UTF-8");
    
    @Override
    public String lookup(String name) throws NoSuchElementException {
        Optional<String> value = lookupOptional(name);
        if (value.isPresent()) {
            return value.get();
        } else {
            throw new NoSuchElementException("Property " + name + " not found");
        }
    }

}
