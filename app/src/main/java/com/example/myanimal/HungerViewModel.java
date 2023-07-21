package com.example.myanimal;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HungerViewModel extends ViewModel {
    private MutableLiveData<Integer> hunger = new MutableLiveData<>();
    private MutableLiveData<Integer> tired = new MutableLiveData<>();
    private String profileImage = new String();
    private int colorPicked = 0xFFFFFFFF ;


    public LiveData<Integer> getHunger(){
        return hunger;
    }

    public void setHunger(int value){
        hunger.setValue(value);
    }

    public LiveData<Integer> getTired(){
        return tired;
    }

    public void setTired(int value){

        tired.setValue(value);
    }

    public void setImageUri(String profileImg){
        this.profileImage = profileImg;
    }

    public String getImageUri(){
        return profileImage;
    }

    public void setColorPicked(int color){
        this.colorPicked = color;
    }

    public int getColorPicked(){
        return colorPicked;
    }
}