package com.example.myanimal;


import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import android.app.AlertDialog;
import android.content.DialogInterface;

import yuku.ambilwarna.AmbilWarnaDialog;


public class ProfileFragment extends Fragment {
    private NavBar navBar;
    private static final int REQUEST_PICK_IMAGE = 101;
    private View view;
    private OnChangeImageButtonClickListener changeImageButtonClickListener;
    private HungerViewModel viewModel;
    private int selectedColor;
    private ImageView circleImg;




    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_fragment, container, false);
        Log.d("ProfileFragment", "Fragment instance: " + this.toString());

        viewModel = new ViewModelProvider(requireActivity()).get(HungerViewModel.class);
        TextView messageTextView = view.findViewById(R.id.profileTextView);
        messageTextView.setText("This is Profile Page");

        if (getActivity() instanceof NavBar) {
            navBar = (NavBar) getActivity();
        }

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

        if (getActivity() instanceof NavBar) {
            navBar = (NavBar) getActivity();
            setOnChangeImageButtonClickListener(navBar);
        }

        if ((!viewModel.getImageUri().equals(null)) && (!viewModel.getImageUri().equals(""))){
            Log.d("Not null Uri", "imageUri");
            Uri imageUri = Uri.parse(viewModel.getImageUri().toString());
            onImageSelected(imageUri);
        } else {
            Log.d("Null Uri", "imageUri");
            int resourceId = R.drawable.pokemon;
            Uri imageUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + resourceId);
            onImageSelected(imageUri);
        }

        circleImg = view.findViewById(R.id.circleImageView);

        Drawable originalDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.circle_background);
        Drawable drawableWithColor = originalDrawable.mutate();
        drawableWithColor.setColorFilter(viewModel.getColorPicked(), PorterDuff.Mode.SRC_IN);
        circleImg.setImageDrawable(drawableWithColor);

        ImageButton colorPicker = view.findViewById(R.id.colorPickerButton);
        colorPicker.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                showColorPickerDialog();
            }
        });

        return view;
    }

    public void onImageSelected(Uri selectedImageUri) {
            Log.d("onImage Selected", String.valueOf(selectedImageUri));
            viewModel.setImageUri(selectedImageUri.toString());
            ImageView profileImg = view.findViewById(R.id.profileImageView);

            Glide.with(this)
                    .load(selectedImageUri)
                    .placeholder(R.drawable.pokemon)
                    .error(R.drawable.pokemon)
                    .apply(RequestOptions.circleCropTransform())
                    .into(profileImg);
    }

    public interface OnChangeImageButtonClickListener {
        void onChangeImageButtonClick();
    }

    public void setOnChangeImageButtonClickListener(OnChangeImageButtonClickListener listener) {
        this.changeImageButtonClickListener = listener;
    }
    private void showColorPickerDialog() {
        int initialColor = selectedColor != 0 ? selectedColor : Color.WHITE;
        AmbilWarnaDialog colorPickerDialog = new AmbilWarnaDialog(requireContext(), initialColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                // Do nothing when the user cancels the color picker
            }

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
