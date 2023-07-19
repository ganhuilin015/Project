package com.example.myanimal;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HungerViewModel extends ViewModel {
    private MutableLiveData<Integer> hunger = new MutableLiveData<>();
    private MutableLiveData<Integer> tired = new MutableLiveData<>();
    private MutableLiveData<String> profileImage = new MutableLiveData<>();


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
        profileImage.setValue(profileImg);
        Log.d("profile Img", String.valueOf(profileImg));
        Log.d("profile Image", String.valueOf(profileImage));
    }

    public LiveData<String> getImageUri(){
        Log.d("get profile image", String.valueOf(profileImage));
        return profileImage;
    }
}