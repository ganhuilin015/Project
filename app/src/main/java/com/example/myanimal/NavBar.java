package com.example.myanimal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

import com.google.firebase.FirebaseApp;
import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class NavBar extends AppCompatActivity {

    private TextView coinCountTextView;
    private MeowBottomNavigation bottomNavigation;
    private AppBarConfiguration appBarConfiguration;
    private NavController navController;
    RelativeLayout main;
    private Handler handler = new Handler();
    private Runnable updateRunnable;
    private HomeFragment homeFragment;
    private HungerViewModel viewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        main = findViewById(R.id.main);

        bottomNavigation = findViewById(R.id.bottomNavigation);

        bottomNavigation.show(3, true);

        bottomNavigation.add(new MeowBottomNavigation.Model(1,R.drawable.profile));
        bottomNavigation.add(new MeowBottomNavigation.Model(2,R.drawable.pets));
        bottomNavigation.add(new MeowBottomNavigation.Model(3,R.drawable.home));
        bottomNavigation.add(new MeowBottomNavigation.Model(4,R.drawable.earn));
        bottomNavigation.add(new MeowBottomNavigation.Model(5,R.drawable.setting));


        int homeColor = ContextCompat.getColor(this, R.color.white);
        int petsColor = ContextCompat.getColor(this, R.color.light_yellow);
        int earnColor = ContextCompat.getColor(this, R.color.white);
        int profileColor = ContextCompat.getColor(this, R.color.teal_200);
        int settingColor = ContextCompat.getColor(this, R.color.white);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                // your codes
                switch (item.getId()){
                    case 1:
                        getWindow().setNavigationBarColor(Color.parseColor("#FFBB86FC"));
                        bottomNavigation.setBackgroundBottomColor(Color.parseColor("#FFBB86FC"));
                        getWindow().setStatusBarColor(Color.parseColor("#FFBB86FC"));
                        main.setBackgroundColor(Color.parseColor("#EEE7A5"));
                        bottomNavigation.setCircleColor(profileColor);
                        break;

                    case 2:
                        getWindow().setNavigationBarColor(Color.parseColor("#2196F3"));
                        bottomNavigation.setBackgroundBottomColor(Color.parseColor("#2196F3"));
                        getWindow().setStatusBarColor(Color.parseColor("#2196F3"));
                        main.setBackgroundColor(Color.parseColor("#FF03DAC5"));
                        bottomNavigation.setCircleColor(petsColor);
                        break;

                    case 3:
                        bottomNavigation.setBackgroundBottomColor(Color.parseColor("#EEE7A5"));
                        main.setBackgroundColor(Color.parseColor("#2196F3"));
                        bottomNavigation.setCircleColor(homeColor);
                        getWindow().setNavigationBarColor(Color.parseColor("#EEE7A5"));
                        getWindow().setStatusBarColor(Color.parseColor("#EEE7A5"));
                        break;


                    case 4:
                        getWindow().setNavigationBarColor(Color.parseColor("#FF03DAC5"));
                        bottomNavigation.setBackgroundBottomColor(Color.parseColor("#FF03DAC5"));
                        getWindow().setStatusBarColor(Color.parseColor("#FF03DAC5"));
                        main.setBackgroundColor(Color.parseColor("#EEA5C2"));
                        bottomNavigation.setCircleColor(earnColor);
                        break;

                    case 5:
                        getWindow().setNavigationBarColor(Color.parseColor("#2196F3"));
                        bottomNavigation.setBackgroundBottomColor(Color.parseColor("#2196F3"));
                        getWindow().setStatusBarColor(Color.parseColor("#2196F3"));
                        main.setBackgroundColor(Color.parseColor("#FF03DAC5"));
                        bottomNavigation.setCircleColor(settingColor);
                        break;


                }

            }
        });

        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                switch (item.getId()){
                    case 1:
                        navController.navigate(R.id.destination_profile);
                        break;

                    case 2:
                        navController.navigate(R.id.destination_pets);
                        break;

                    case 3:
                        navController.navigate(R.id.destination_home);
                        break;

                    case 4:
                        navController.navigate(R.id.destination_earn);
                        break;

                    case 5:
                        navController.navigate(R.id.destination_setting);
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

        FirebaseApp.initializeApp(this);

        // Get a reference to the Firebase Realtime Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        // Read data from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and whenever data at this location is updated.
                Long value = dataSnapshot.getValue(Long.class);
                if (value != null) {
                    Log.d("Firebase", "Database Reference: " + myRef.toString());
                    Log.d("Firebase", "Value is: " + value);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w("Firebase", "Failed to read value.", error.toException());
            }
        });


        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(HungerViewModel.class);
        viewModel.setHunger(120);
        viewModel.setTired(120);

        updateRunnable = new Runnable() {
            @Override
            public void run() {
                int hungerValue = viewModel.getHunger().getValue();
                int tiredValue = viewModel.getTired().getValue();

                if (hungerValue > 0){
                    hungerValue--;
                    viewModel.setHunger(hungerValue);
                    myRef.setValue(hungerValue);
                }

                if (tiredValue > 0){
                    tiredValue--;
                    viewModel.setTired(tiredValue);
                    myRef.setValue(tiredValue);
                }

                handler.postDelayed(this, 20000); // Update every 1 seconds

            }

        };

        handler.postDelayed(updateRunnable, 1000);

    }

    public void updateCoinCount(int count) {
        coinCountTextView.setText(String.valueOf(count));
    }

    public int getCoinCount() {
        String text = coinCountTextView.getText().toString();
        int coin = Integer.parseInt(text);
        return coin;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(updateRunnable);
    }


}
