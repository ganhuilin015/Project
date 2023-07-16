package com.example.myanimal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.widget.Button;


public class SettingFragment extends Fragment {
    private NavBar navBar;
    private Button buttonNavigateToLogin;
    private Button buttonNavigateToRegister;
    private NavController navController;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_fragment, container, false);

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        buttonNavigateToLogin = view.findViewById(R.id.SettingToLogin_Button);
        buttonNavigateToRegister = view.findViewById(R.id.SettingToRegister_Button);

        buttonNavigateToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Login
                navController.navigate(R.id.Setting_to_Login);
            }
        });

        buttonNavigateToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Register
                navController.navigate(R.id.Setting_to_Register);
            }
        });

        return view;
    }
}
