package com.gofore.aws.workshop.common.async;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FutureHelper {

    public static <T> CompletableFuture<Stream<T>> sequence(Stream<CompletableFuture<T>> futures) {
        List<CompletableFuture<T>> collected = futures.collect(Collectors.toList());
        CompletableFuture<Void> all = CompletableFuture.allOf(collected.toArray(new CompletableFuture[collected.size()]));
        return all.thenApply(v ->  {
            return collected.stream().map(f -> {
                try {
                    return f.join();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });
        });
    }
    
}
