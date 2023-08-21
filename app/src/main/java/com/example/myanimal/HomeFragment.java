package com.example.myanimal;


import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

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
    private int hunger, tired, health, happy = 120;
    private boolean IsLightOn = true;

    private static final String HUNGER_PREF = "hunger_pref";
    private static final String HUNGER_KEY = "hunger_key";
    private static final String IMAGE_RES_KEY = "image_res_key";
    private HungerViewModel viewModel;
    private MediaPlayer mediaPlayer;

    public HomeFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment, container, false);

        if (getActivity() instanceof NavBar) {
            navBar = (NavBar) getActivity();
        }

//        ImageView gifImageView = view.findViewById(R.id.gifImageView);
//        Glide.with(this).asGif().load(R.raw.poke).into(gifImageView);

        LinearLayout homeMain = view.findViewById(R.id.home_main);

        homeMain.setBackgroundColor(viewModel.getHomeBackground());
        Log.d("Initial home backgroun doclor", String.valueOf(viewModel.getHomeBackground()));
        navBar.main.setBackgroundColor(viewModel.getHomeBackground());

        ImageButton feedButton = view.findViewById(R.id.feedButton);
        feedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hunger < 110) {
                    hunger++;
                }
                viewModel.setHunger(hunger);
                int coinValue = navBar.getCoinCount();

                if (coinValue > 9 && viewModel.getLightImageUri() == R.drawable.lamp_light_){
                    coinValue = coinValue - 10;
                    Log.d("Home Coin", String.valueOf(coinValue));
                    navBar.updateCoinCount(coinValue);
                    System.out.println(hunger);
                } else if (viewModel.getLightImageUri() == R.drawable.lamp_dim_){
                    Toast.makeText(requireContext(), "Unable to feed because your pet is sleeping", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(requireContext(), "Insufficient coin", Toast.LENGTH_SHORT).show();
                }

            }
        });


        ImageButton sleepButton = view.findViewById(R.id.sleepButton);
        sleepButton.setImageResource(viewModel.getLightImageUri());
        sleepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IsLightOn) {
                    mediaPlayer = MediaPlayer.create(requireContext(), R.raw.pullcord_switch);

                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mediaPlayer.release();
                        }
                    });

                    sleepButton.setImageResource(R.drawable.lamp_dim_);
                    viewModel.setLightImageUri(R.drawable.lamp_dim_);
                    homeMain.setBackgroundColor(Color.parseColor("#81C2A3"));
                    navBar.main.setBackgroundColor(Color.parseColor("#81C2A3"));
                    viewModel.setHomeBackground(Color.parseColor("#81C2A3"));
                    IsLightOn = false;
                } else {
                    mediaPlayer = MediaPlayer.create(requireContext(), R.raw.pullcord_switch);

                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mediaPlayer.release();
                        }
                    });

                    sleepButton.setImageResource(R.drawable.lamp_light_);
                    viewModel.setLightImageUri(R.drawable.lamp_light_);
                    homeMain.setBackgroundColor(Color.parseColor("#81FEC2"));
                    navBar.main.setBackgroundColor(Color.parseColor("#81FEC2"));
                    viewModel.setHomeBackground(Color.parseColor("#81FEC2"));
                    IsLightOn = true;
                }
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

        viewModel.getTired().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer updatedTired) {
                tired = updatedTired;
                Log.d("onHomeCreateView_tired", String.valueOf(tired));
                updateTiredStatus(tired);
            }
        });

        viewModel.getHealth().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer updatedHealth) {
                health = updatedHealth;
                Log.d("onHomeCreateView_health", String.valueOf(health));
                updateHealthStatus(health);
            }
        });

        viewModel.getHappy().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer updatedHappy) {
                happy = updatedHappy;
                Log.d("onHomeCreateView_happy", String.valueOf(happy));
                updateHappyStatus(happy);
            }
        });
    }

    public void updateHungerStatus(int updateHunger) {
        Log.d("updateHungerStatus", String.valueOf(updateHunger));
        hunger = updateHunger;

        ImageView statusBar_Hunger = view.findViewById(R.id.statusbar_Hunger);

        // Get the image resource ID based on the hunger value
        int imageResId_Hunger = getImageResourceId_Hunger();

        // Set the status bar image resource
        statusBar_Hunger.setImageResource(imageResId_Hunger);
    }

    private int getImageResourceId_Hunger() {
        if (this.hunger >= 100) {
            return R.drawable.hungerstatusfull;
        } else if (this.hunger < 100 && this.hunger >= 90) {
            return R.drawable.hungerstatus9;
        } else if (this.hunger < 90 && this.hunger >= 80) {
            return R.drawable.hungerstatus8;
        } else if (this.hunger < 80 && this.hunger >= 70) {
            return R.drawable.hungerstatus7;
        } else if (this.hunger < 70 && this.hunger >= 60) {
            return R.drawable.hungerstatus6;
        } else if (this.hunger < 60 && this.hunger >= 50) {
            return R.drawable.hungerstatus5;
        } else if (this.hunger < 50 && this.hunger >= 40) {
            return R.drawable.hungerstatus4;
        } else if (this.hunger < 40 && this.hunger >= 30) {
            return R.drawable.hungerstatus3;
        } else if (this.hunger < 30 && this.hunger >= 20) {
            return R.drawable.hungerstatus2;
        } else if (this.hunger < 20 && this.hunger >= 10) {
            return R.drawable.hungerstatus1;
        } else {
            return R.drawable.hungerstatus0;
        }
    }

    public void updateTiredStatus(int updateTired) {
        Log.d("updateTiredStatus", String.valueOf(updateTired));
        tired = updateTired;

        ImageView statusBar_Tired = view.findViewById(R.id.statusbar_Tired);

        // Get the image resource ID based on the hunger value
        int imageResId_Tired = getImageResourceId_Energy();

        // Set the status bar image resource
        statusBar_Tired.setImageResource(imageResId_Tired);
    }

    private int getImageResourceId_Energy() {
        if (this.tired >= 100) {
            return R.drawable.energystatusfull;
        } else if (this.tired < 100 && this.tired >= 90) {
            return R.drawable.energystatus9;
        } else if (this.tired < 90 && this.tired >= 80) {
            return R.drawable.energystatus8;
        } else if (this.tired < 80 && this.tired >= 70) {
            return R.drawable.energystatus7;
        } else if (this.tired < 70 && this.tired >= 60) {
            return R.drawable.energystatus6;
        } else if (this.tired < 60 && this.tired >= 50) {
            return R.drawable.energystatus5;
        } else if (this.tired < 50 && this.tired >= 40) {
            return R.drawable.energystatus4;
        } else if (this.tired < 40 && this.tired >= 30) {
            return R.drawable.energystatus3;
        } else if (this.tired < 30 && this.tired >= 20) {
            return R.drawable.energystatus2;
        } else if (this.tired < 20 && this.tired >= 10) {
            return R.drawable.energystatus1;
        } else {
            return R.drawable.energystatus0;
        }
    }

    public void updateHealthStatus(int updateHealth) {
        Log.d("updateHealthStatus", String.valueOf(updateHealth));
        health = updateHealth;

        ImageView statusBar_Health = view.findViewById(R.id.statusbar_Health);

        // Get the image resource ID based on the hunger value
        int imageResId_Health = getImageResourceId_Health();

        // Set the status bar image resource
        statusBar_Health.setImageResource(imageResId_Health);
    }

    private int getImageResourceId_Health() {
        if (this.health >= 100) {
            return R.drawable.healthstatusfull;
        } else if (this.health < 100 && this.health >= 90) {
            return R.drawable.healthstatus9;
        } else if (this.health < 90 && this.health >= 80) {
            return R.drawable.healthstatus8;
        } else if (this.health < 80 && this.health >= 70) {
            return R.drawable.healthstatus7;
        } else if (this.health < 70 && this.health >= 60) {
            return R.drawable.healthstatus6;
        } else if (this.health < 60 && this.health >= 50) {
            return R.drawable.healthstatus5;
        } else if (this.health < 50 && this.health >= 40) {
            return R.drawable.healthstatus4;
        } else if (this.health < 40 && this.health >= 30) {
            return R.drawable.healthstatus3;
        } else if (this.health < 30 && this.health >= 20) {
            return R.drawable.healthstatus2;
        } else if (this.health < 20 && this.health >= 10) {
            return R.drawable.healthstatus1;
        } else {
            return R.drawable.healthstatus0;
        }
    }

    public void updateHappyStatus(int updateHappy) {
        Log.d("updateHappyStatus", String.valueOf(updateHappy));
        happy = updateHappy;

        ImageView statusBar_Happy = view.findViewById(R.id.statusbar_Happy);

        // Get the image resource ID based on the hunger value
        int imageResId_Happy = getImageResourceId_Happy();

        // Set the status bar image resource
        statusBar_Happy.setImageResource(imageResId_Happy);
    }

    private int getImageResourceId_Happy() {
        if (this.happy >= 100) {
            return R.drawable.happinessstatusfull;
        } else if (this.happy < 100 && this.happy >= 90) {
            return R.drawable.happinessstatus9;
        } else if (this.happy < 90 && this.happy >= 80) {
            return R.drawable.happinessstatus8;
        } else if (this.happy < 80 && this.happy >= 70) {
            return R.drawable.happinessstatus7;
        } else if (this.happy < 70 && this.happy >= 60) {
            return R.drawable.happinessstatus6;
        } else if (this.happy < 60 && this.happy >= 50) {
            return R.drawable.happinessstatus5;
        } else if (this.happy < 50 && this.happy >= 40) {
            return R.drawable.happinessstatus4;
        } else if (this.happy < 40 && this.happy >= 30) {
            return R.drawable.happinessstatus3;
        } else if (this.happy < 30 && this.happy >= 20) {
            return R.drawable.happinessstatus2;
        } else if (this.happy < 20 && this.happy >= 10) {
            return R.drawable.happinessstatus1;
        } else {
            return R.drawable.happinessstatus0;
        }
    }

}


