package com.example.myanimal;

import android.graphics.drawable.Drawable;

public class Pets {
    private String name;
    private Drawable imageResId; // Drawable resource ID for the pet's image

    public Pets(String name, Drawable imageResId) {
        this.name = name;
        this.imageResId = imageResId;
    }

    public String getPetsName(){
        return name;
    }

    public Drawable getPetsImage(){
        return imageResId;
    }
}
