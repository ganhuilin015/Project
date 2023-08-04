package com.example.myanimal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.widget.Button;


public class FriendFragment extends Fragment {
    private NavBar navBar;
    private Button buttonNavigateToLogin;
    private Button buttonNavigateToRegister;
    private NavController navController;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.friend_fragment, container, false);

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        return view;
    }
}
