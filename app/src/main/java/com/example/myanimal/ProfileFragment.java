package com.example.myanimal;


import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import android.app.AlertDialog;
import android.content.DialogInterface;

import org.w3c.dom.Text;

import yuku.ambilwarna.AmbilWarnaDialog;


public class ProfileFragment extends Fragment{
    private NavBar navBar;
    private static final int REQUEST_PICK_IMAGE = 101;
    private View view;
    private OnChangeImageButtonClickListener changeImageButtonClickListener;
    private HungerViewModel viewModel;
    private int selectedColor;
    private ImageView circleImg;
    private TextView textViewTitle, bioTitle, dobTitle;
    private NavController navController;



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_fragment, container, false);
        Log.d("ProfileFragment", "Fragment instance: " + this.toString());

        //Initialize viewModel to get the data from hunger view model for the same profile picture
        viewModel = new ViewModelProvider(requireActivity()).get(HungerViewModel.class);

        //Initialize the navcontroller for navigation
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        //Profile page title
        TextView messageTextView = view.findViewById(R.id.profileTextView);
        messageTextView.setText("This is Profile Page");

        //Button to change profile image
        ImageButton changeImage = view.findViewById(R.id.changePictureButton);
        changeImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d("Onclick", "Choosing profile picture");
                if (changeImageButtonClickListener != null) {
                    changeImageButtonClickListener.onChangeImageButtonClick();
                }
            }
        });

        //Get instance of NavBar to use their method
        if (getActivity() instanceof NavBar) {
            navBar = (NavBar) getActivity();
            setOnChangeImageButtonClickListener(navBar);
        }

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

        //Circle border of profile picture
        circleImg = view.findViewById(R.id.circleImageView);

        //Enable user to pick from color picker for the color of border
        ImageButton colorPicker = view.findViewById(R.id.colorPickerButton);
        colorPicker.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                showColorPickerDialog();
            }
        });

        //Enable use to choose color they want for the border color
        Drawable originalDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.circle_background);
        Drawable drawableWithColor = originalDrawable.mutate();
        drawableWithColor.setColorFilter(viewModel.getColorPicked(), PorterDuff.Mode.SRC_IN);
        circleImg.setImageDrawable(drawableWithColor);

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

    //Set it to navBar
    public void setOnChangeImageButtonClickListener(OnChangeImageButtonClickListener listener) {
        this.changeImageButtonClickListener = listener;
    }

    //Show the color picker bar for user to pick color
    private void showColorPickerDialog() {
        int initialColor = selectedColor != 0 ? selectedColor : Color.WHITE;
        AmbilWarnaDialog colorPickerDialog = new AmbilWarnaDialog(requireContext(), initialColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                // Do nothing when the user cancels the color picker
            }

            //When color picked OK, perform the action to change the profile border color
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                selectedColor = color;
                viewModel.setColorPicked(color);
                Drawable originalDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.circle_background);
                Drawable drawableWithColor = originalDrawable.mutate();
                drawableWithColor.setColorFilter(color, PorterDuff.Mode.SRC_IN);
                circleImg.setImageDrawable(drawableWithColor);

            }
        });

        colorPickerDialog.show();
    }

}
