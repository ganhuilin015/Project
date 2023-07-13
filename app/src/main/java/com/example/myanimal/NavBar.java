package com.example.myanimal;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

public class NavBar extends AppCompatActivity {

    private TextView coinCountTextView;
    private MeowBottomNavigation bottomNavigation;
    private AppBarConfiguration appBarConfiguration;
    private NavController navController;
    RelativeLayout main;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        main = findViewById(R.id.main);

        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.add(new MeowBottomNavigation.Model(1,R.drawable.home));
        bottomNavigation.add(new MeowBottomNavigation.Model(2,R.drawable.pets));
        bottomNavigation.add(new MeowBottomNavigation.Model(3,R.drawable.earn));
        bottomNavigation.add(new MeowBottomNavigation.Model(4,R.drawable.profile));

        int homeColor = ContextCompat.getColor(this, R.color.white);
        int petsColor = ContextCompat.getColor(this, R.color.light_yellow);
        int earnColor = ContextCompat.getColor(this, R.color.light_pink);
        int profileColor = ContextCompat.getColor(this, R.color.teal_200);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        getWindow().setNavigationBarColor(Color.parseColor("#EEE7A5"));
        getWindow().setStatusBarColor(Color.parseColor("#EEE7A5"));


        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                // your codes
                switch (item.getId()){
                    case 1:
                        bottomNavigation.setBackgroundBottomColor(Color.parseColor("#EEE7A5"));
                        main.setBackgroundColor(Color.parseColor("#2196F3"));
                        bottomNavigation.setCircleColor(homeColor);
                        break;

                    case 2:
                        getWindow().setNavigationBarColor(Color.parseColor("#2196F3"));
                        bottomNavigation.setBackgroundBottomColor(Color.parseColor("#2196F3"));
                        getWindow().setStatusBarColor(Color.parseColor("#2196F3"));
                        main.setBackgroundColor(Color.parseColor("#FF03DAC5"));
                        bottomNavigation.setCircleColor(petsColor);
                        break;

                    case 3:
                        getWindow().setNavigationBarColor(Color.parseColor("#FF03DAC5"));
                        bottomNavigation.setBackgroundBottomColor(Color.parseColor("#FF03DAC5"));
                        getWindow().setStatusBarColor(Color.parseColor("#FF03DAC5"));
                        main.setBackgroundColor(Color.parseColor("#FFEB3B"));
                        bottomNavigation.setCircleColor(earnColor);
                        break;

                    case 4:
                        getWindow().setNavigationBarColor(Color.parseColor("#FFBB86FC"));
                        bottomNavigation.setBackgroundBottomColor(Color.parseColor("#FFBB86FC"));
                        getWindow().setStatusBarColor(Color.parseColor("#FFBB86FC"));
                        main.setBackgroundColor(Color.parseColor("#EEE7A5"));
                        bottomNavigation.setCircleColor(profileColor);
                        break;
                }

            }
        });

        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                switch (item.getId()){
                    case 1:
                        navController.navigate(R.id.destination_home);
                        break;

                    case 2:
                        navController.navigate(R.id.destination_pets);
                        break;

                    case 3:
                        navController.navigate(R.id.destination_earn);
                        break;

                    case 4:
                        navController.navigate(R.id.destination_profile);
                        break;

                }
            }
        });

        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                // Handle the reselect event for the item
                // Left it empty if you don't need to perform any action on reselect
            }
        });



        coinCountTextView = findViewById(R.id.coinCountTextView);

    }

    public void updateCoinCount(int count) {
        coinCountTextView.setText(String.valueOf(count));
    }

    public int getCoinCount() {
        String text = coinCountTextView.getText().toString();
        int coin = Integer.parseInt(text);
        return coin;
    }
    }
