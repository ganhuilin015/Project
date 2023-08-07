package com.example.myanimal;

public class FeedItem {
    private String imageUri;
    private String caption;

    public FeedItem(String imageUri, String caption) {
        this.imageUri = imageUri;
        this.caption = caption;
    }

    public String getImageUri() {
        return imageUri;
    }

    public String getCaption() {
        return caption;
    }
}

