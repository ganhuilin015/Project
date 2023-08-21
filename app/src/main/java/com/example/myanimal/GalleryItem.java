package com.example.myanimal;

public class GalleryItem {

    private String dateandtime;
    private String imageUri;

    public GalleryItem(String DateandTime, String image) {
        this.dateandtime = DateandTime;
        this.imageUri = image;
    }

    public String getDateandTime(){
        return dateandtime;
    }

    public String getImageUri(){
        return imageUri;
    }

}
