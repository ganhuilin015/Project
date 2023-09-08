package com.example.myanimal;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ShopFragment extends Fragment{
    private NavBar navBar;
    private NavController navController;
    private HungerViewModel viewModel;
    private HomeFragment homeFragment;
    private List<Pets> petsList = new ArrayList<>();

    private ImageButton  foodsButton;
    private int coinCount;
    private PetsAdapter petsAdapter;
    private MediaPlayer mediaPlayer;
    private boolean isPetsPurchased = true;
    private ArrayList<Boolean> isPetsPurchasedList = new ArrayList<>();


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

        int[] nameViewIds = {
                R.id.petsNameTextView,
                R.id.petsNameTextView1,
                R.id.petsNameTextView2,
                R.id.petsNameTextView3,
                R.id.petsNameTextView4,
                R.id.petsNameTextView5,
                R.id.petsNameTextView6,
                R.id.petsNameTextView7,
        };

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
            if (viewModel.getIsPetsPurchasedList().size() - 1 < index){
                isPetsPurchasedList.add(true);
                viewModel.addIsPetsPurchasedList(isPetsPurchasedList);
            }

            ImageButton buyButton = view.findViewById(imageButtonIds[index]);
            buyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    coinCount = navBar.getCoinCount();

                    TextView petsNameText = view.findViewById(nameViewIds[index]);
                    CharSequence petsName = petsNameText.getText();

                    TextView costText = view.findViewById(textViewIds[index]) ;
                    CharSequence costChar = costText.getText();
                    int cost = Integer.parseInt(costChar.toString());

                    ImageView imageDes = view.findViewById(imageButtonIds[index]);
                    Drawable imageDrawable = imageDes.getDrawable();

                    // Inflate the custom layout for the dialog
                    View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.description_fragment, null);

                    // Create the AlertDialog
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(requireContext(),  R.style.TransparentAlertDialog);
                    alertDialogBuilder.setView(dialogView);

                    ImageView descriptionImage = dialogView.findViewById(R.id.description_image);
                    descriptionImage.setImageDrawable(imageDrawable);

                    TextView messageTextView = dialogView.findViewById(R.id.description_text);
                    messageTextView.setText(String.valueOf(cost));

                    TextView petsnametext = dialogView.findViewById(R.id.petsName_text);
                    petsnametext.setText(petsName);

                    ImageButton purchaseButton = dialogView.findViewById(R.id.purchaseButton);
                    purchaseButton.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            if (coinCount >= cost){
                                mediaPlayer = MediaPlayer.create(requireContext(), R.raw.coin_pickup);

                                mediaPlayer.start();
                                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mp) {
                                        mediaPlayer.release();
                                    }
                                });

                                coinCount = coinCount - cost;
                                navBar.updateCoinCount(coinCount);

                                Pets purchasedPet = new Pets(petsName.toString(), imageDrawable);
                                viewModel.addPetsList(purchasedPet);
                                petsList.add(purchasedPet);

                                purchaseButton.setEnabled(false);
                                isPetsPurchasedList.set(index, false);
                                viewModel.addIsPetsPurchasedList(isPetsPurchasedList);

                                purchaseButton.setImageResource(R.drawable.purchased);
                                Toast.makeText(requireContext(), "Successfully Purchased", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(requireContext(), "Insufficient coin", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    isPetsPurchasedList = viewModel.getIsPetsPurchasedList();
                    Log.d("is pets purchase", String.valueOf(index));
                    Log.d("is pets purchase 1", String.valueOf(isPetsPurchasedList));

                    isPetsPurchased = isPetsPurchasedList.get(index);
                    purchaseButton.setEnabled(isPetsPurchased);
                    if (isPetsPurchased == false) {
                        purchaseButton.setImageResource(R.drawable.purchased);
                    }

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
