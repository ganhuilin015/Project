package com.example.myanimal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterFragment extends Fragment {

    private NavController navController;
    private Button registerNavigateToSetting;
    private EditText editTextEmail, editTextPassword;
    private Button buttonRegister;
    private FirebaseAuth mAuth;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_fragment, container, false);

        TextView messageTextView = view.findViewById(R.id.registerTextView);
        messageTextView.setText("This is Register Page");

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        registerNavigateToSetting = view.findViewById(R.id.RegisterToSetting_Button);

        registerNavigateToSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Setting
                navController.navigate(R.id.to_Setting);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        buttonRegister = view.findViewById(R.id.buttonRegister);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(requireContext(), "Please enter email and password", Toast.LENGTH_SHORT).show();
                } else {
                    // Create a new user with email and password
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // User registration successful
                                        Toast.makeText(requireContext(), "Registration successful", Toast.LENGTH_SHORT).show();
                                        // You can perform additional actions here, such as navigating to the main activity
                                    } else {
                                        // User registration failed
                                        Toast.makeText(requireContext(), "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });


        return view;
    }
}


