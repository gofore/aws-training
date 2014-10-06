package com.gofore.aws.workshop.common.properties;

import java.util.NoSuchElementException;
import java.util.Optional;

public interface PropertyLoader {
    
    String lookup(String name) throws NoSuchElementException;
    
    Optional<String> lookupOptional(String name);
    
}
