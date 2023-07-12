package com.example.myanimal;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    private NavBar navBar;
    private int coinCount;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        Log.d("Home", "In Home Page");
        TextView messageTextView = view.findViewById(R.id.homeTextView);
        messageTextView.setText("This is Home Page");

        // Rest of your code

        return view;
    }
}
