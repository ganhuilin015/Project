package com.example.myanimal;

import android.util.Log;
import android.view.View;

public class Block {

    private int x, y;
    private int color;
    private int size;
    private int speed;
    private boolean isVisible = true;
    private View view;

    public Block (int x, int y, int size, int color, int speed){
        this.x = x;
        this.y = y;
        this.color = color;
        this.size = size;
        this.speed = speed;
    }

    public void move(){
        y += 5;
    }

    public boolean isTapped(int tapX, int tapY){
        Log.d("tapX", String.valueOf(tapX));
        Log.d("tapY", String.valueOf(tapY));
        Log.d("Y", String.valueOf(y));
        Log.d("X", String.valueOf(x));


        if (isVisible && tapX >= x && tapX <= x+size && tapY >= y && tapY <= y+size){
            isVisible = false;
            return true;
        }
        return false;
    }

    public void setY(int setY){
        this.y = setY;
    }

    public int getY(){
        return y;
    }

    public int getX(){
        return x;
    }

    public int getSize(){
        return size;
    }

    public View getView() {
        return view;
    }

    public void setView(View v) {
        this.view = v;
    }

    public int getSpeed(){
        return speed;
    }
}
