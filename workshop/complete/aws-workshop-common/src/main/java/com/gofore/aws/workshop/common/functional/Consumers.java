package com.gofore.aws.workshop.common.functional;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Consumers {

    /**
     * Creates a bi consumer where the first parameter is the success value and
     * the second parameter is the error value. Only error consumer is accepted
     * there's an exception.
     * 
     * @param success value of successful operation
     * @param error exception of failed operation
     * @return bi consumer to be used in functional interfaces
     */
    public static <T> BiConsumer<T, Throwable> consumer(Consumer<T> success, Consumer<Throwable> error) {
        return (s, e) -> {
            if (e != null) {
                error.accept(e);
            } else {
                success.accept(s);
            }
        };
    }
    
}
