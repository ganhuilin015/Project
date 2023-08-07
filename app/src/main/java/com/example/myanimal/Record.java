package com.example.myanimal;

public class Record {

    private String dateandtime;
    private int stepsTaken;

    public Record(String DateandTime, int stepsTaken) {
        this.dateandtime = DateandTime;
        this.stepsTaken = stepsTaken;
    }

    public String getDateandTime(){
        return dateandtime;
    }

    public int getStepsTaken(){
        return stepsTaken;
    }

}
