package com.gofore.aws.workshop.common.functional;

import java.util.Optional;
import java.util.function.Consumer;

public class Objects {
    
    public static <T> void set(Consumer<T> setter, Optional<T> value) {
        if (value.isPresent()) {
            setter.accept(value.get());
        }
    }
    
    public static <T> void set(Consumer<T> setter, T value) {
        set(setter, Optional.ofNullable(value));
    }
    
    private Objects() { }
    
}
