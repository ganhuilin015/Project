package com.example.myanimal;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class EarnFragment extends Fragment {

    private NavBar navBar;
    private int coinCount;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.earn_fragment, container, false);
        if (getActivity() instanceof NavBar) {
            navBar = (NavBar) getActivity();
        }

        Button earnButton = view.findViewById(R.id.earnButton);
        earnButton.setBackgroundResource(R.drawable.pokemon);
        earnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coinCount = navBar.getCoinCount();
                // Perform your desired action when the button is clicked
                // Update the coin count
                coinCount++;
                navBar.updateCoinCount(coinCount);
            }
        });
        TextView messageTextView = view.findViewById(R.id.messageTextView);
        messageTextView.setText("Press the button to earn coins");
        return view;
    }
}
