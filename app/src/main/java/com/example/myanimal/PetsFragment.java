package com.example.myanimal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class PetsFragment extends Fragment {
    private NavBar navBar;
    private int coinCount;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pets_fragment, container, false);

        if (getActivity() instanceof NavBar) {
            navBar = (NavBar) getActivity();
        }

        int[] imageButtonIds = {
                R.id.pokepets,
                R.id.pokepets1,
                R.id.pokepets2,
                R.id.pokepets4,
                R.id.pokepets5,
                R.id.pokepets6,
                R.id.pokepets7,
                R.id.pokepets8,
                R.id.pokepets9,
                R.id.pokepets10,
                R.id.pokepets11,
                R.id.pokepets12
        };

        int[] textViewIds = {
                R.id.petsTextView,
                R.id.petsTextView1,
                R.id.petsTextView2,
                R.id.petsTextView4,
                R.id.petsTextView5,
                R.id.petsTextView6,
                R.id.petsTextView7,
                R.id.petsTextView8,
                R.id.petsTextView9,
                R.id.petsTextView10,
                R.id.petsTextView11,
                R.id.petsTextView12
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

                    coinCount = coinCount - cost;
                    navBar.updateCoinCount(coinCount);

                    TextView messageTextView = view.findViewById(textViewIds[index]);
                    messageTextView.setText(String.valueOf(cost));
                }
            });
        }

        return view;
    }
}
