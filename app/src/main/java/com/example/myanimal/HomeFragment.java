package com.example.myanimal;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

public class HomeFragment extends Fragment {

    private NavBar navBar;
    private int coinCount;
    private Handler handler = new Handler();
    private Runnable updateRunnable;
    private View view;
    ;
    private int hunger = 120;

    private static final String HUNGER_PREF = "hunger_pref";
    private static final String HUNGER_KEY = "hunger_key";
    private static final String IMAGE_RES_KEY = "image_res_key";

    public HomeFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment, container, false);

        ImageView gifImageView = view.findViewById(R.id.gifImageView);
        Glide.with(this).asGif().load(R.raw.poke).into(gifImageView);

        if (getArguments() != null) {
            hunger = getArguments().getInt(HUNGER_KEY);
        }

//        updateHungerStatus(hunger);

        return view;
    }

    public void updateHungerStatus(int hunger) {
        Log.d("Hunger Value", String.valueOf(hunger));
        this.hunger = hunger;

        ImageView statusBar = view.findViewById(R.id.statusbar);

        // Get the image resource ID based on the hunger value
        int imageResId = getImageResourceId();

        // Set the status bar image resource
        statusBar.setImageResource(imageResId);
    }

    private int getImageResourceId() {
        if (hunger >= 110) {
            return R.drawable.statusbarfull;
        } else if (hunger < 110 && hunger >= 100) {
            return R.drawable.statusbar10;
        } else if (hunger < 100 && hunger >= 90) {
            return R.drawable.statusbar9;
        } else if (hunger < 90 && hunger >= 80) {
            return R.drawable.statusbar8;
        } else if (hunger < 80 && hunger >= 70) {
            return R.drawable.statusbar7;
        } else if (hunger < 70 && hunger >= 60) {
            return R.drawable.statusbar6;
        } else if (hunger < 60 && hunger >= 50) {
            return R.drawable.statusbar5;
        } else if (hunger < 50 && hunger >= 40) {
            return R.drawable.statusbar4;
        } else if (hunger < 40 && hunger >= 30) {
            return R.drawable.statusbar3;
        } else if (hunger < 30 && hunger >= 20) {
            return R.drawable.statusbar2;
        } else if (hunger < 20 && hunger >= 10) {
            return R.drawable.statusbar1;
        } else {
            return R.drawable.statusbar0;
        }
    }

    public int getHunger(){
        return hunger;
    }
}


