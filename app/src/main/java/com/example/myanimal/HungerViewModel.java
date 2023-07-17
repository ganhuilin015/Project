package com.example.myanimal;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HungerViewModel extends ViewModel {
    private MutableLiveData<Integer> hunger = new MutableLiveData<>();
    private MutableLiveData<Integer> tired = new MutableLiveData<>();


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
}