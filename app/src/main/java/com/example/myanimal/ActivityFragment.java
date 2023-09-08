package com.example.myanimal;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class ActivityFragment extends Fragment {

    private NavBar navBar;
    private int coinCount;
    private NavController navController;
    private HungerViewModel viewModel;
    private ImageButton earnButton, walkButton, vetButton, diaryButton;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment, container, false);

        //Initialize viewModel to get the data from hunger view model for the same profile picture
        viewModel = new ViewModelProvider(requireActivity()).get(HungerViewModel.class);

        //Initialize the navcontroller for navigation
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        if (getActivity() instanceof NavBar) {
            navBar = (NavBar) getActivity();
        }

        earnButton = view.findViewById(R.id.earnButton);
        earnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.to_earn);
                navBar.main.setBackgroundColor(Color.parseColor("#B1D5FF"));
            }
        });

        walkButton = view.findViewById(R.id.walkButton);
        walkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.to_walk);
                navBar.main.setBackgroundColor(Color.parseColor("#FFC7C2"));
            }
        });

        diaryButton = view.findViewById(R.id.diaryButton);
        diaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate((R.id.to_diary));
                navBar.main.setBackgroundColor(Color.parseColor("#81FEC2"));
            }
        });

        vetButton = view.findViewById(R.id.vetButton);
        vetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.to_vet);
                navBar.main.setBackgroundColor(Color.parseColor("#EFCCFF"));
            }
        });

        return view;
    }
}
