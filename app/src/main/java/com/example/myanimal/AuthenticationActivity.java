package com.example.myanimal;

import static android.content.ContentValues.TAG;
import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AuthenticationActivity extends AppCompatActivity implements AuthenticationInterface.OnAuthenticationSuccessListener{

    private static final int RC_SIGN_IN = 100;
    private NavController navController;
    private Button loginButton;
    private Button registerButton;
    private Button loginNavigateToSetting;

    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin;
    private FirebaseAuth mAuth;
    private Button registerNavigateToSetting;
    private Button buttonRegister;
    private  NavHostFragment navHostFragment;
    private GoogleSignInClient googleSignInClient;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Splash screen activity", "on create");
        FirebaseApp.initializeApp(this);

        if (isUserAuthenticated()) {
            Intent intent = new Intent(this, NavBar.class);
            startActivity(intent);
            finish(); // finish the authentication activity so it doesn't run in the background

        } else {
            Log.d("User not authenticated", "Going to AuthenticationFragment");
            setContentView(R.layout.authentication_fragment);

            //Set login button variable and change background color
            loginButton = findViewById(R.id.Login_Button);
            loginButton.setBackgroundColor(ContextCompat.getColor(this, R.color.white));

            //Set register button variable (sign in button) and change background color
            registerButton = findViewById(R.id.Register_Button);
            registerButton.setBackgroundColor(ContextCompat.getColor(this, R.color.white));

//            // Configure sign-in to request the user's ID, email address, and basic profile
//            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                    .requestEmail()
//                    .build();
//
//            // Build a GoogleSignInClient with the options specified by gso
//            googleSignInClient = GoogleSignIn.getClient(this, gso);

            //Initialized navHostFragment to call navController
            navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
            navController = navHostFragment.getNavController();

//            SignInButton googleSignIn = findViewById(R.id.google_sign_in);
//            googleSignIn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    signIn();
//                }
//            });

            //Set login button activity
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setContentView(R.layout.login_fragment);

                    //Back button to navigate back to Authentication activity and set background color
                    loginNavigateToSetting = findViewById(R.id.LoginToSetting_Button);
                    loginNavigateToSetting.setBackgroundColor(ContextCompat.getColor(AuthenticationActivity.this, R.color.white));

                    loginNavigateToSetting.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            navController.navigate(R.id.to_SplashActivity);
                        }
                    });

                    mAuth = FirebaseAuth.getInstance();
                    editTextEmail = findViewById(R.id.editTextEmail);
                    editTextPassword = findViewById(R.id.editTextPassword);

                    //Button to log in to proceed to home page and change background color
                    buttonLogin = findViewById(R.id.buttonLogin);
                    buttonLogin.setBackgroundColor(ContextCompat.getColor(AuthenticationActivity.this, R.color.white));

                    buttonLogin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String email = editTextEmail.getText().toString().trim();
                            String password = editTextPassword.getText().toString();

                            if (email.isEmpty() || password.isEmpty()) {
                                Toast.makeText(AuthenticationActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                            } else {
                                mAuth.signInWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(AuthenticationActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                                    onAuthenticationSuccess();
                                                } else {
                                                    Toast.makeText(AuthenticationActivity.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        }
                    });
                }

            });


            //Set register button activity
            registerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    setContentView(R.layout.register_fragment);

                    //Back button to navigate back to authentication activity and set button color
                    registerNavigateToSetting = findViewById(R.id.RegisterToSetting_Button);
                    registerNavigateToSetting.setBackgroundColor(ContextCompat.getColor(AuthenticationActivity.this, R.color.white));

                    registerNavigateToSetting.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            navController.navigate(R.id.to_SplashActivity);
                        }
                    });

                    mAuth = FirebaseAuth.getInstance();
                    editTextEmail = findViewById(R.id.editTextEmail);
                    editTextPassword = findViewById(R.id.editTextPassword);

                    //Button to register and then navigate to home page and change background color
                    buttonRegister = findViewById(R.id.buttonRegister);
                    buttonRegister.setBackgroundColor(ContextCompat.getColor(AuthenticationActivity.this, R.color.white));

                    buttonRegister.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String email = editTextEmail.getText().toString().trim();
                            String password = editTextPassword.getText().toString();

                            if (email.isEmpty() || password.isEmpty()) {
                                Toast.makeText(AuthenticationActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                            } else {
                                // Create a new user with email and password
                                mAuth.createUserWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(AuthenticationActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                                    onAuthenticationSuccess();
                                                } else {
                                                    Toast.makeText(AuthenticationActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        }
                    });



                }
            });
        }
    }

    @Override
    public void onAuthenticationSuccess() {
        Log.d("one authentication succses", "authentication success");
        Intent intent = new Intent(this, NavBar.class);
        startActivity(intent);
        finish();
    }

    private boolean isUserAuthenticated() {
        // Implement your logic to check if the user is authenticated
        // You can check if the authentication token exists in SharedPreferences or any other secure storage mechanism
        // Return true if the user is authenticated, false otherwise
        // Example:
        // SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        // String authToken = preferences.getString("authToken", null);
        // return authToken != null;
        return false;
    }

//    private void signIn() {
//        Log.d("Authentication activity","sign in");
//        Intent signInIntent = googleSignInClient.getSignInIntent();
//        startActivityForResult(signInIntent, RC_SIGN_IN);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        Log.d("Authentication activity","signing in");
//
//        // Handle the result of the sign-in process
//        if (requestCode == RC_SIGN_IN) {
//            Log.d("Authentication activity","signing in succesful");
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            handleSignInResult(task);
//        }
//    }

//    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
//        try {
//            Log.d("Authentication activity","handle sign in result");
//
//            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
//            // Signed in successfully, you can use the account object to get user details
//            // e.g., account.getId(), account.getDisplayName(), account.getEmail(), etc.
//            String userId = account.getId();
//            Log.d("userID", String.valueOf(userId));
//            String email = account.getEmail();
//            Log.d("email", String.valueOf(email));
//            String displayName = account.getDisplayName();
//            Log.d("displayname", String.valueOf(displayName));
//            String photoUrl = account.getPhotoUrl().toString();
//            Log.d("photourl", String.valueOf(photoUrl));
//
//
//            FirebaseFirestore db = FirebaseFirestore.getInstance();
//            DocumentReference userRef = db.collection("users").document(userId);
//
//            // Create a User object to store in Firestore
//            Map<String, Object> user = new HashMap<>();
//            user.put("email", email);
//            user.put("displayName", displayName);
//            user.put("photoUrl", photoUrl);
//
//            // Add the user details to Firestore
//            userRef.set(user)
//                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            Log.d("Firebase Authenticate","enter succesful");
//                            Toast.makeText(AuthenticationActivity.this, "Google sign in successful ", Toast.LENGTH_SHORT).show();
//
//                            // User details added to Firestore successfully
//                            // You can perform additional actions here if needed
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(AuthenticationActivity.this, "Google sign in failed ", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//
//        } catch (ApiException e) {
//            Log.w(TAG, "signInResult:failed code =" + e.getStatusCode());
//        }
//    }
}
