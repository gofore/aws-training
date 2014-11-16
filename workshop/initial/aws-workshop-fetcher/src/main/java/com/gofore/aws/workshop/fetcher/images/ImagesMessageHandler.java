package com.gofore.aws.workshop.fetcher.images;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import com.amazonaws.services.sqs.model.Message;
import com.gofore.aws.workshop.common.async.FutureHelper;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ImagesMessageHandler implements Function<Message, CompletableFuture<Message>> {

    private final ImageFetcher imageFetcher;
    private final Updater updater;

    @Inject
    public ImagesMessageHandler(ImageFetcher imageFetcher, Updater updater) {
        this.imageFetcher = imageFetcher;
        this.updater = updater;
    }

    @Override
    public CompletableFuture<Message> apply(Message message) {
        return imageFetcher.fetchImages(message.getBody())
                .thenApply(images -> images.map(updater::update))
                .thenCompose(FutureHelper::sequence)
                .thenApply(s -> message);
    }
}
