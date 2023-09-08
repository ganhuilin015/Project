package com.example.myanimal;

import android.graphics.Matrix;

public class Diary {

    private String title;
    private String diaryText;
    private String dateAndTime;

    public Diary(String title, String diaryText, String dateAndTime) {
        this.title = title;
        this.diaryText = diaryText;
        this.dateAndTime = dateAndTime;
    }

    public String getDiaryTitle() {
        return title;
    }

    public String getDiaryText() {
        return diaryText;
    }

    public String getDiaryDateandTime(){
        return dateAndTime;
    }

}
