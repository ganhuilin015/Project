package com.example.myanimal;

import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class HungerViewModel extends ViewModel {
    private MutableLiveData<Integer> hunger= new MutableLiveData<>();
    private MutableLiveData<Integer> tired =  new MutableLiveData<>();
    private MutableLiveData<Integer> health =  new MutableLiveData<>();
    private MutableLiveData<Integer> happy =  new MutableLiveData<>();
    private MutableLiveData<Integer> totalSteps =  new MutableLiveData<>();

    private MutableLiveData<List<FeedItem>> feedItemList = new MutableLiveData<>();
//    private MutableLiveData<PointF> imageTranslation = new MutableLiveData<>();


    private String profileImage = new String();
    private String feedImage = new String();

    private int lightImage = R.drawable.lamp_light_;
    private int walkImage = R.drawable.startwalking;


    private int colorPicked = 0xFFFFFFFF ;
    private String profileName = "Your Name";
    private String profileDOB = "Date-of-birth";
    private String profileBio = "Your Bio";
    private int initialSteps;
    private int homeBackground = Color.parseColor("#81FEC2");
    private boolean stepsBool, regBool;
    private List<Record> recordList = new ArrayList<>();

    private List<FeedItem> feedList = new ArrayList<>();
    private List<Pets> petsList = new ArrayList<>();

    private MutableLiveData<List<FeedItem>> feedItemsLiveData = new MutableLiveData<>();
    private final MutableLiveData<PointF> imageTranslation = new MutableLiveData<>();
    private boolean isPurchased = true;
    private int purchasedImg = R.drawable.purchasebutton;
    private ArrayList<Boolean> isPetsPurchasedList = new ArrayList<>();
    private List<Diary> diaryNoteList = new ArrayList<>();
    private List<DiaryAudio> diaryVoiceList = new ArrayList<>();







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

    public LiveData<Integer> getHealth(){

        return health;
    }

    public void setHealth(int value){

        health.setValue(value);
    }

    public LiveData<Integer> getHappy(){

        return happy;
    }

    public void setHappy(int value){

        happy.setValue(value);
    }

    public void setImageUri(String profileImg){

        this.profileImage = profileImg;
    }

    public String getImageUri(){

        return profileImage;
    }

//    public void setColorPickedDraw(int color){
//
//        this.colorPicked = color;
//    }
//
//    public int getColorPickedDraw(){
//
//        return colorPicked;
//    }

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

    public void setLightImageUri(int lightImg){

        this.lightImage = lightImg;
    }

    public int getLightImageUri(){

        return lightImage;
    }

    public void setWalkImageUri(int walkImg){

        this.walkImage = walkImg;
    }

    public int getWalkImageUri(){

        return walkImage;
    }

    public void setSteps(int steps){

        totalSteps.setValue(steps);
    }

    public LiveData<Integer>  getSteps(){

        return totalSteps;
    }

    public void setInitialSteps(int steps){

        this.initialSteps = steps;
    }

    public int getInitialSteps(){

        return initialSteps;
    }

    public void setStepsBool(boolean stepsBool){

        this.stepsBool = stepsBool;
    }

    public boolean getStepsBool(){

        return stepsBool;
    }

    public int getHomeBackground(){
        return homeBackground;
    }

    public void setHomeBackground(int backColor){
        this.homeBackground = backColor;
    }

    public void setRegisteredBool(boolean reg){

        this.regBool = reg;
    }

    public boolean getRegisteredBool(){

        return regBool;
    }

    public void addRecord(Record record) {
        recordList.add(0, record);
    }
    public List<Record> getRecordList() {
        return recordList;
    }

    public void addFeed(FeedItem feeds) {
        feedList.add(0, feeds);
    }

    public List<FeedItem> getFeedList() {
        return feedList;
    }

    public List<Pets> getPetsList() {
        return petsList;
    }
    public void addPetsList(Pets pets){
        petsList.add(0, pets);
    }

    public void addDiaryNoteList(Diary diarynote){
        diaryNoteList.add(0, diarynote);
    }

    public List<Diary> getDiaryNoteList() {
        return diaryNoteList;
    }

    public void addDiaryVoiceList(DiaryAudio diaryvoice){
        diaryVoiceList.add(0, diaryvoice);
    }

    public List<DiaryAudio> getDiaryVoiceList() {
        return diaryVoiceList;
    }



    public void setFeedImageUri(String feedImg){

        this.feedImage = feedImg;
    }

    public String getFeedImageUri(){

        return feedImage;
    }

    public ArrayList<Boolean> getIsPetsPurchasedList(){
        return isPetsPurchasedList;
    }

    public void addIsPetsPurchasedList(ArrayList<Boolean> purchasedlist){
        isPetsPurchasedList = purchasedlist;
    }


}