package com.example.myanimal;


import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import yuku.ambilwarna.AmbilWarnaDialog;


public class ProfileFragment extends Fragment{
    private NavBar navBar;
    private static final int REQUEST_PICK_IMAGE = 101;
    private static final int REQUEST_CAMERA_PERMISSION = 100;

    private View view;
    private OnChangeImageButtonClickListener changeImageButtonClickListener;
    private HungerViewModel viewModel;
    private TextView textViewTitle, bioTitle, dobTitle;
    private NavController navController;
    private FirebaseAuth firebaseAuth;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICKER = 2;

    private List<FeedItem> feedItemList;
    private FeedAdapter feedadapter;



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_fragment, container, false);
        Log.d("ProfileFragment", "Fragment instance: " + this.toString());

        //Initialize viewModel to get the data from hunger view model for the same profile picture
        viewModel = new ViewModelProvider(requireActivity()).get(HungerViewModel.class);

        //Initialize the navcontroller for navigation
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        //Initialize NavBar
        if (getActivity() instanceof NavBar) {
            navBar = (NavBar) getActivity();
        }

        //Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        ImageButton sign_out_button = view.findViewById(R.id.signoutButton);
        sign_out_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // Inflate the custom layout for the dialog
                View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.signout_confirmation, null);

                // Create the AlertDialog
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(requireContext(),  R.style.TransparentAlertDialog);
                alertDialogBuilder.setView(dialogView);

                // Show the dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                ImageButton yesButton = dialogView.findViewById(R.id.yesButton);
                yesButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        signOut(v);
                    }
                });
                ImageButton noButton = dialogView.findViewById(R.id.noButton);
                noButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        alertDialog.dismiss();
                    }
                });
            }
        });

        //If image uri is not null or empty string, then get the uri image that user chose and set as profile picture
        if ((!viewModel.getImageUri().equals(null)) && (!viewModel.getImageUri().equals(""))){
            Uri imageUri = Uri.parse(viewModel.getImageUri().toString());
            onImageSelected(imageUri);
        } else {
            //If image uri is null, will be set to default image pokemon
            int resourceId = R.drawable.pokemon;
            Uri imageUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + resourceId);
            onImageSelected(imageUri);
        }

//        //Circle border of profile picture
//        circleImg = view.findViewById(R.id.circleImageView);
//
//        //Enable user to pick from color picker for the color of border
//        ImageButton colorPicker = view.findViewById(R.id.colorPickerButton);
//        colorPicker.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View v){
//
//                showColorPickerDialog();
//            }
//        });
//
//        //Enable use to choose color they want for the border color
//        Drawable originalDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.circle_background);
//        Drawable drawableWithColor = originalDrawable.mutate();
//        drawableWithColor.setColorFilter(viewModel.getColorPicked(), PorterDuff.Mode.SRC_IN);
//        circleImg.setImageDrawable(drawableWithColor);

        //Display user information
        textViewTitle = view.findViewById(R.id.textViewTitle);
        bioTitle = view.findViewById(R.id.BioTitle);
        dobTitle = view.findViewById(R.id.DobTitle);

        //Get profile information from hunger view model
        String profile_name = viewModel.getProfileName().trim();
        String profile_bio = viewModel.getProfileBio().trim();
        String profile_dob = viewModel.getProfileDOB().trim();

        System.out.println(profile_name);

        //Then set the information in profile from view model if not empty
        if (!profile_name.equals("") || !profile_name.equals(null) ){
            textViewTitle.setText(profile_name);
        }
        if (!profile_bio.equals("") || !profile_bio.equals(null) ){
            bioTitle.setText(profile_bio);
        }
        if (!profile_dob.equals("") || !profile_dob.equals(null) ){
            dobTitle.setText(profile_dob);
        }

        //Button to edit the profile page
        ImageButton editProfile = view.findViewById(R.id.edit_profile);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.to_update_profile);
                navBar.main.setBackgroundColor(Color.parseColor("#CED5D5"));
            }
        });

        feedItemList = new ArrayList<>();
        feedItemList = viewModel.getFeedList();
        feedadapter = new FeedAdapter(feedItemList);


        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewFeed);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(feedadapter);

        ImageButton plusButton = view.findViewById(R.id.plusButton);
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_IMAGE_PICKER);

            }
        });

        return view;
    }

    //Activate when Image from gallery is selected and upload it
    public void onImageSelected(Uri selectedImageUri) {
            viewModel.setImageUri(selectedImageUri.toString());
            ImageView profileImg = view.findViewById(R.id.profileImageView);

            Glide.with(this)
                    .load(selectedImageUri)
                    .placeholder(R.drawable.pokemon)
                    .error(R.drawable.pokemon)
                    .apply(RequestOptions.circleCropTransform())
                    .into(profileImg);
    }

    //When change image button click, send signal to navbar for the image changed
    public interface OnChangeImageButtonClickListener {
        void onChangeImageButtonClick();
    }

    //Show the color picker bar for user to pick color
//    private void showColorPickerDialog() {
//        int initialColor = selectedColor != 0 ? selectedColor : Color.WHITE;
//        AmbilWarnaDialog colorPickerDialog = new AmbilWarnaDialog(requireContext(), initialColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
//            @Override
//            public void onCancel(AmbilWarnaDialog dialog) {
//                // Do nothing when the user cancels the color picker
//            }
//
//            //When color picked OK, perform the action to change the profile border color
//            @Override
//            public void onOk(AmbilWarnaDialog dialog, int color) {
//                selectedColor = color;
//                viewModel.setColorPicked(color);
//                Drawable originalDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.circle_background);
//                Drawable drawableWithColor = originalDrawable.mutate();
//                drawableWithColor.setColorFilter(color, PorterDuff.Mode.SRC_IN);
//                circleImg.setImageDrawable(drawableWithColor);
//
//            }
//        });
//
//        colorPickerDialog.show();
//    }

    public void signOut(View view) {
        firebaseAuth.signOut();
        navController.navigate(R.id.to_AuthenticationActivity);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_PICKER && data != null) {
                Uri imageUri = data.getData();
                viewModel.setFeedImageUri(imageUri != null ? imageUri.toString() : null);
                Log.d("navigate to addfeed image", String.valueOf(imageUri.toString()));
                navController.navigate(R.id.to_addfeed);

            }
        }
    }
}
