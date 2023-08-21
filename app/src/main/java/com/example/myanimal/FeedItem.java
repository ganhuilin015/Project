package com.example.myanimal;

import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;

public class FeedItem {
    private String imageUri;
    private String caption;
    private Matrix finalPosition;


    public FeedItem(String imageUri, String caption, Matrix finalPosition) {
        this.imageUri = imageUri;
        this.caption = caption;
        this.finalPosition = finalPosition;
    }

    public String getImageUri() {
        return imageUri;
    }

    public String getCaption() {
        return caption;
    }

    public Matrix getFinalPosition(){
        return finalPosition;
    }

}

