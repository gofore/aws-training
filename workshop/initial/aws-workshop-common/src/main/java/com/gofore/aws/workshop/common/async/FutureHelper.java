package com.gofore.aws.workshop.common.async;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FutureHelper {

    /**
     * Converts a stream of futures to a future of stream. The future will
     * complete when all the underlying futures are completed.
     * 
     * @param futures stream of futures
     * @return future of stream
     */
    public static <T> CompletableFuture<Stream<T>> sequence(Stream<CompletableFuture<T>> futures) {
        List<CompletableFuture<T>> collected = futures.collect(Collectors.toList());
        CompletableFuture<Void> all = CompletableFuture.allOf(collected.toArray(new CompletableFuture[collected.size()]));
        return all.thenApply(v -> collected.stream().map(CompletableFuture::join));
    }
    
}
