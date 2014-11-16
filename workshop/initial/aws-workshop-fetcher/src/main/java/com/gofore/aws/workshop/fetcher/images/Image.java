package com.gofore.aws.workshop.fetcher.images;

public class Image {
    
    private final String thumbnailUrl;
    private final String imageUrl;
    private final String description;

    public Image(String thumbnailUrl, String imageUrl, String description) {
        this.thumbnailUrl = thumbnailUrl;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }
}
