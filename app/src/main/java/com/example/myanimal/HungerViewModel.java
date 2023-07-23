package com.example.myanimal;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HungerViewModel extends ViewModel {
    private MutableLiveData<Integer> hunger = new MutableLiveData<>();
    private MutableLiveData<Integer> tired = new MutableLiveData<>();
    private String profileImage = new String();
    private int colorPicked = 0xFFFFFFFF ;
    private String profileName = "Your Name";
    private String profileDOB = "Date-of-birth";
    private String profileBio = "Your Bio";




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

    public String getProfileName(){

        return profileName;
    }

    public void setProfileName(String name){

        this.profileName = name;
    }

    public String getProfileBio(){

        return profileBio;
    }

    public void setProfileBio(String bio){
        this.profileBio = bio;
    }

    public String getProfileDOB(){

        return profileDOB;
    }

    public void setProfileDOB(String dob){

        this.profileDOB = dob;
    }

}