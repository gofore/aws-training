package com.gofore.aws.workshop.common.functional;

import java.util.Optional;
import java.util.function.Consumer;

public class Objects {

    /**
     * Sets only value that is present.
     * 
     * @param setter the setter method reference
     * @param value value to be set
     */
    public static <T> void setPresent(Consumer<T> setter, Optional<T> value) {
        if (value.isPresent()) {
            setter.accept(value.get());
        }
    }

    /**
     * Sets only value that is not <code>null</code>.
     * 
     * @param setter the setter method reference
     * @param value value to be set
     */
    public static <T> void setNonNull(Consumer<T> setter, T value) {
        setPresent(setter, Optional.ofNullable(value));
    }
    
    private Objects() { }
    
}
