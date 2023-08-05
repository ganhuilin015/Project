package com.example.myanimal;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class ShopFragment extends Fragment {
    private NavBar navBar;
    private NavController navController;
    private HungerViewModel viewModel;
    private ImageButton  foodsButton;
    private int coinCount;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shop_fragment, container, false);

        //Initialize viewModel to get the data from hunger view model for the same profile picture
        viewModel = new ViewModelProvider(requireActivity()).get(HungerViewModel.class);

        //Initialize the navcontroller for navigation
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        if (getActivity() instanceof NavBar) {
            navBar = (NavBar) getActivity();
        }


        foodsButton = view.findViewById(R.id.foodsButton);
        foodsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.to_foodsshop);
                navBar.main.setBackgroundColor(Color.parseColor("#FEFA9D"));
            }
        });

        int[] imageButtonIds = {
                R.id.pokepets,
                R.id.pokepets1,
                R.id.pokepets2,
                R.id.pokepets3,
                R.id.pokepets4,
                R.id.pokepets5,
                R.id.pokepets6,
                R.id.pokepets7,
        };

        int[] textViewIds = {
                R.id.petsTextView,
                R.id.petsTextView1,
                R.id.petsTextView2,
                R.id.petsTextView3,
                R.id.petsTextView4,
                R.id.petsTextView5,
                R.id.petsTextView6,
                R.id.petsTextView7,
        };

        for (int i = 0; i < imageButtonIds.length; i++) {
            int index = i;
            ImageButton buyButton = view.findViewById(imageButtonIds[index]);
            buyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    coinCount = navBar.getCoinCount();

                    TextView costText = view.findViewById(textViewIds[index]) ;
                    CharSequence costChar = costText.getText();
                    int cost = Integer.parseInt(costChar.toString());


                    // Inflate the custom layout for the dialog
                    View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.description_fragment, null);

                    // Create the AlertDialog
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(requireContext(),  R.style.TransparentAlertDialog);
                    alertDialogBuilder.setView(dialogView);

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
