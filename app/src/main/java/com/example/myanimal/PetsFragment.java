package com.example.myanimal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class PetsFragment extends Fragment {
    private NavBar navBar;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pets_fragment, container, false);


        TextView messageTextView = view.findViewById(R.id.petsTextView);
        messageTextView.setText("This is Pets Page");

        // Rest of your code

        return view;
    }
}
