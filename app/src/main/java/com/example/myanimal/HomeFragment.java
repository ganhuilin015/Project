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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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
    private HungerViewModel viewModel;

    public HomeFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment, container, false);

        ImageView gifImageView = view.findViewById(R.id.gifImageView);
        Glide.with(this).asGif().load(R.raw.poke).into(gifImageView);

        ImageButton feedButton = view.findViewById(R.id.feedButton);
        feedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hunger++;
                viewModel.setHunger(hunger);
                System.out.println(hunger);
            }
        });

        return view;
    }
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(HungerViewModel.class);
        viewModel.getHunger().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer updatedHunger) {
                hunger = updatedHunger;
                Log.d("onHomeCreateView", String.valueOf(hunger));
                updateHungerStatus(hunger);
            }
        });
    }


    public void updateHungerStatus(int updateHunger) {
        Log.d("updateHungerStatus", String.valueOf(updateHunger));
        hunger = updateHunger;

        ImageView statusBar = view.findViewById(R.id.statusbar);

        // Get the image resource ID based on the hunger value
        int imageResId = getImageResourceId();

        // Set the status bar image resource
        statusBar.setImageResource(imageResId);
    }

    private int getImageResourceId() {
        if (this.hunger >= 110) {
            return R.drawable.statusbarfull;
        } else if (this.hunger < 110 && this.hunger >= 100) {
            return R.drawable.statusbar10;
        } else if (this.hunger < 100 && this.hunger >= 90) {
            return R.drawable.statusbar9;
        } else if (this.hunger < 90 && this.hunger >= 80) {
            return R.drawable.statusbar8;
        } else if (this.hunger < 80 && this.hunger >= 70) {
            return R.drawable.statusbar7;
        } else if (this.hunger < 70 && this.hunger >= 60) {
            return R.drawable.statusbar6;
        } else if (this.hunger < 60 && this.hunger >= 50) {
            return R.drawable.statusbar5;
        } else if (this.hunger < 50 && this.hunger >= 40) {
            return R.drawable.statusbar4;
        } else if (this.hunger < 40 && this.hunger >= 30) {
            return R.drawable.statusbar3;
        } else if (this.hunger < 30 && this.hunger >= 20) {
            return R.drawable.statusbar2;
        } else if (this.hunger < 20 && this.hunger >= 10) {
            return R.drawable.statusbar1;
        } else {
            return R.drawable.statusbar0;
        }
    }

    public int getHunger(){
        return this.hunger;
    }
}


