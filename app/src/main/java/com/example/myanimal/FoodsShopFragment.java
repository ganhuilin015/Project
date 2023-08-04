package com.example.myanimal;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class FoodsShopFragment extends Fragment {
    private NavController navController;
    private NavBar navBar;
    private int coinCount;

    private HungerViewModel viewModel;
    private ImageButton petsButton;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.food_shop_fragment, container, false);

        //Initialize viewModel to get the data from hunger view model for the same profile picture
        viewModel = new ViewModelProvider(requireActivity()).get(HungerViewModel.class);

        //Initialize the navcontroller for navigation
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        if (getActivity() instanceof NavBar) {
            navBar = (NavBar) getActivity();
        }
        petsButton = view.findViewById(R.id.petsButton);

        petsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.to_shop);
                navBar.main.setBackgroundColor(Color.parseColor("#FEFA9D"));
            }
        });

        int[] imageButtonIds = {
                R.id.pokefoods,
                R.id.pokefoods1,
                R.id.pokefoods2,
                R.id.pokefoods3,
                R.id.pokefoods4,
                R.id.pokefoods5,
                R.id.pokefoods6,
                R.id.pokefoods7,
        };

        int[] textViewIds = {
                R.id.foodsTextView,
                R.id.foodsTextView1,
                R.id.foodsTextView2,
                R.id.foodsTextView3,
                R.id.foodsTextView4,
                R.id.foodsTextView5,
                R.id.foodsTextView6,
                R.id.foodsTextView7,
        };

        for (int i = 0; i < imageButtonIds.length; i++) {
            int index = i;
            ImageButton earnButton = view.findViewById(imageButtonIds[index]);
            earnButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    coinCount = navBar.getCoinCount();

                    TextView costText = view.findViewById(textViewIds[index]) ;
                    CharSequence costChar = costText.getText();
                    int cost = Integer.parseInt(costChar.toString());

                    // Inflate the custom layout for the dialog
                    View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.description_fragment, null);

                    TextView messageTextView = dialogView.findViewById(R.id.description_text);
                    messageTextView.setText(String.valueOf(cost));
                    ImageButton purchaseButton = dialogView.findViewById(R.id.purchaseButton);
                    purchaseButton.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            if (coinCount > cost){
                                coinCount = coinCount - cost;
                                navBar.updateCoinCount(coinCount);
                            } else {
                                Toast.makeText(requireContext(), "Insufficient coin", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    // Create the AlertDialog
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(requireContext(),  R.style.TransparentAlertDialog);
                    alertDialogBuilder.setView(dialogView);


                    // Show the dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                    ImageButton crossButton = dialogView.findViewById(R.id.crossButton);
                    crossButton.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            alertDialog.dismiss();
                        }
                    });

                }
            });
        }

        return view;
        }
    }
