    package com.example.myanimal;

    import androidx.appcompat.app.AppCompatActivity;
    import androidx.core.content.ContextCompat;
    import androidx.fragment.app.Fragment;
    import androidx.fragment.app.FragmentTransaction;
    import androidx.lifecycle.ViewModelProvider;
    import androidx.navigation.NavController;
    import androidx.navigation.Navigation;
    import android.content.Intent;
    import android.graphics.Color;
    import android.net.Uri;
    import android.os.Bundle;
    import android.os.Handler;
    import android.provider.MediaStore;
    import android.util.Log;
    import android.widget.RelativeLayout;
    import android.widget.TextView;
    import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
    import com.google.android.material.appbar.AppBarLayout;
    import com.google.android.material.appbar.MaterialToolbar;
    import com.google.firebase.FirebaseApp;
    import androidx.annotation.NonNull;
    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.ValueEventListener;
    import com.google.firebase.firestore.FirebaseFirestore;
    import com.google.firebase.firestore.FirebaseFirestoreSettings;


    public class NavBar extends AppCompatActivity implements ProfileFragment.OnChangeImageButtonClickListener, UpdateProfileFragment.ProfileUpdateListener{

        private TextView coinCountTextView;
        private MeowBottomNavigation bottomNavigation;
        private NavController navController;
        RelativeLayout main;
        MaterialToolbar toolbar;
        private Handler handler = new Handler();
        private Runnable updateRunnable;
        private HungerViewModel viewModel;
        private static final int REQUEST_PICK_IMAGE = 101;
        private static final int REQUEST_CAPTURE_IMAGE = 102;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            Log.d("Navbar", "navbar on create");

            main = findViewById(R.id.main);
            toolbar = findViewById(R.id.topNavBar);

            bottomNavigation = findViewById(R.id.bottomNavigation);

            bottomNavigation.show(3, true);

            bottomNavigation.add(new MeowBottomNavigation.Model(1,R.drawable.profile));
            bottomNavigation.add(new MeowBottomNavigation.Model(2,R.drawable.pets));
            bottomNavigation.add(new MeowBottomNavigation.Model(3,R.drawable.home));
            bottomNavigation.add(new MeowBottomNavigation.Model(4,R.drawable.earn));
            bottomNavigation.add(new MeowBottomNavigation.Model(5,R.drawable.setting));

            navController = Navigation.findNavController(this, R.id.nav_host_fragment);

            bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
                @Override
                public void onClickItem(MeowBottomNavigation.Model item) {
                    // your codes
                    switch (item.getId()){
                        case 1:
                            getWindow().setNavigationBarColor(Color.parseColor("#B1D5FF"));
                            bottomNavigation.setBackgroundBottomColor(Color.parseColor("#B1D5FF"));
                            getWindow().setStatusBarColor(Color.parseColor("#B1D5FF"));
                            main.setBackgroundColor(Color.parseColor("#FFC7C2"));
                            toolbar.setBackgroundColor(Color.parseColor("#B1D5FF"));
                            break;

                        case 2:
                            getWindow().setNavigationBarColor(Color.parseColor("#FFC7C2"));
                            bottomNavigation.setBackgroundBottomColor(Color.parseColor("#FFC7C2"));
                            getWindow().setStatusBarColor(Color.parseColor("#FFC7C2"));
                            main.setBackgroundColor(Color.parseColor("#FEFA9D"));
                            toolbar.setBackgroundColor(Color.parseColor("#FFC7C2"));
                            break;

                        case 3:
                            bottomNavigation.setBackgroundBottomColor(Color.parseColor("#FEFA9D"));
                            main.setBackgroundColor(Color.parseColor("#81FEC2"));
                            getWindow().setNavigationBarColor(Color.parseColor("#FEFA9D"));
                            getWindow().setStatusBarColor(Color.parseColor("#FEFA9D"));
                            toolbar.setBackgroundColor(Color.parseColor("#FEFA9D"));
                            break;


                        case 4:
                            getWindow().setNavigationBarColor(Color.parseColor("#81FEC2"));
                            bottomNavigation.setBackgroundBottomColor(Color.parseColor("#81FEC2"));
                            getWindow().setStatusBarColor(Color.parseColor("#81FEC2"));
                            main.setBackgroundColor(Color.parseColor("#CED5D5"));
                            toolbar.setBackgroundColor(Color.parseColor("#81FEC2"));
                            break;

                        case 5:
                            getWindow().setNavigationBarColor(Color.parseColor("#CED5D5"));
                            bottomNavigation.setBackgroundBottomColor(Color.parseColor("#CED5D5"));
                            getWindow().setStatusBarColor(Color.parseColor("#CED5D5"));
                            main.setBackgroundColor(Color.parseColor("#B1D5FF"));
                            toolbar.setBackgroundColor(Color.parseColor("#CED5D5"));
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
                            navController.navigate(R.id.destination_shop);
                            break;

                        case 3:
                            navController.navigate(R.id.destination_home);
                            break;

                        case 4:
                            navController.navigate(R.id.destination_activity);
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
                    // Left it empty because don't need to perform any action on reselect
                }
            });


            coinCountTextView = findViewById(R.id.coinCountTextView);

            FirebaseApp.initializeApp(this);

            FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                    .setPersistenceEnabled(true)
                    .build();
            FirebaseFirestore.getInstance().setFirestoreSettings(settings);


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
                    Log.w("Firebase", "Failed to read value.", error.toException());
                }
            });

            viewModel = new ViewModelProvider(this).get(HungerViewModel.class);
            viewModel.setHunger(120);
            viewModel.setTired(120);
            viewModel.setHealth(120);
            viewModel.setHappy(120);

            updateRunnable = new Runnable() {
                @Override
                public void run() {
                    int hungerValue = viewModel.getHunger().getValue();
                    int tiredValue = viewModel.getTired().getValue();
                    int healthValue = viewModel.getHealth().getValue();
                    int happyValue = viewModel.getHappy().getValue();

                    if (hungerValue > 0){
                        hungerValue--;
                        viewModel.setHunger(hungerValue);
                        myRef.setValue(hungerValue);
                    }

                    if (healthValue > 0){
                        healthValue--;
                        viewModel.setHealth(healthValue);
                        myRef.setValue(healthValue);
                    }

                    if (happyValue > 0){
                        happyValue--;
                        viewModel.setHappy(happyValue);
                        myRef.setValue(happyValue);
                    }

                    if (viewModel.getLightImageUri() == R.drawable.lamp_dim_ && tiredValue < 120){
                        tiredValue++;
                        viewModel.setTired(tiredValue);
                        myRef.setValue(tiredValue);
                    } else {
                        if (tiredValue > 0){
                            tiredValue--;
                            viewModel.setTired(tiredValue);
                            myRef.setValue(tiredValue);
                        }
                    }

                    handler.postDelayed(this, 20000); // Update every 20 seconds

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

        @Override
        public void onChangeImageButtonClick() {
            Intent pickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickImageIntent, REQUEST_PICK_IMAGE);
        }
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
                Uri selectedImageUri = data.getData();
                Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                Fragment fragment = navHostFragment.getChildFragmentManager().getPrimaryNavigationFragment();
                if (fragment instanceof UpdateProfileFragment) {
                    ((UpdateProfileFragment) fragment).onImageSelected(selectedImageUri);
                }

            } else if (requestCode == REQUEST_CAPTURE_IMAGE && resultCode == RESULT_OK) {
                // Handle the captured image here (save locally or upload to Firebase Storage)
            }


        }
        @Override
        public void onUpdateProfile(String name, String bio, String dob) {
            viewModel.setProfileName(name);
            viewModel.setProfileBio(bio);
            viewModel.setProfileDOB(dob);
        }

    }
