package com.gofore.aws.workshop.fetcher.images;

import java.util.concurrent.CompletableFuture;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

@Singleton
public class Updater {
    
    private final ThumbnailUploader thumbnailUploader;
    private final MetadataRepository metadataRepository;
    private final HashFunction hashFunction;

    @Inject
    public Updater(ThumbnailUploader thumbnailUploader, MetadataRepository metadataRepository) {
        this.thumbnailUploader = thumbnailUploader;
        this.metadataRepository = metadataRepository;
        this.hashFunction = Hashing.murmur3_128();
    }

    public CompletableFuture<Image> update(Image image) {
        String id = getImageHash(image);
        return upload(id, image).thenCombine(save(id, image), (uploaded, saved) -> uploaded);
    }
    
    private CompletableFuture<Image> upload(String id, Image image) {
        return thumbnailUploader.upload(id, image);
    }
    
    private CompletableFuture<Image> save(String id, Image image) {
        Image result = new Image(
                thumbnailUploader.getThumbnailUrl(id),
                image.getImageUrl(),
                image.getDescription()
        );
        return metadataRepository.save(id, result);
    }
    
    private String getImageHash(Image image) {
        return hashFunction.hashUnencodedChars(image.getImageUrl()).toString();
    }
}
