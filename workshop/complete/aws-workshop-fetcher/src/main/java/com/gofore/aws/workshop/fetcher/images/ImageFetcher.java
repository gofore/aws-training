package com.gofore.aws.workshop.fetcher.images;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public interface ImageFetcher {

    CompletableFuture<Stream<Image>> fetchImages(String url);
    
}
