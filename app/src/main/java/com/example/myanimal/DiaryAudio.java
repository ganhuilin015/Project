package com.example.myanimal;

public class DiaryAudio {
    private String audioFilePath;
    private String title;
    private String date;

    public DiaryAudio(String audioFilePath, String title, String date) {
        this.audioFilePath = audioFilePath;
        this.title = title;
        this.date = date;
    }

    public String getAudioFilePath() {
        return audioFilePath;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }
}
