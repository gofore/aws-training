package com.gofore.aws.workshop.common.functional;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Consumers {
    
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
