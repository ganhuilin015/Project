package com.example.myanimal;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

public class ProfileFragment extends Fragment {
    private NavBar navBar;
    private static final int REQUEST_PICK_IMAGE = 101;
    private View view;
    private OnChangeImageButtonClickListener changeImageButtonClickListener;
    private HungerViewModel viewModel;



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_fragment, container, false);
        Log.d("ProfileFragment", "Fragment instance: " + this.toString());

        viewModel = new ViewModelProvider(this).get(HungerViewModel.class);
        TextView messageTextView = view.findViewById(R.id.profileTextView);
        messageTextView.setText("This is Profile Page");

        if (getActivity() instanceof NavBar) {
            navBar = (NavBar) getActivity();
        }

        ImageButton changeImage = view.findViewById(R.id.changePictureButton);
        changeImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d("Onclick", "Choosing profile picture3");
                if (changeImageButtonClickListener != null) {
                    changeImageButtonClickListener.onChangeImageButtonClick();
                }
            }
        });

        if (getActivity() instanceof NavBar) {
            navBar = (NavBar) getActivity();
            setOnChangeImageButtonClickListener(navBar);
        }

        if (viewModel.getImageUri() != null){
            Log.d("Not null Uri", "imageUri");
            Uri imageUri = Uri.parse(viewModel.getImageUri().toString());
            onImageSelected(imageUri);
        } else {
            // If there is no image URI in the ViewModel, set the default image.
            int resourceId = R.drawable.pokemon;
            Uri imageUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + resourceId);
            onImageSelected(imageUri);
        }

        ImageView circleImg = view.findViewById(R.id.circleImageView);
        Glide.with(this)
                .load(R.drawable.circle_background)
                .apply(RequestOptions.circleCropTransform())
                .placeholder(R.drawable.circle_background)
                .into(circleImg);

        return view;
    }

    public void onImageSelected(Uri selectedImageUri) {
        Log.d("onImage Selected", String.valueOf(selectedImageUri));
        viewModel.setImageUri(selectedImageUri.toString());
        ImageView profileImg = view.findViewById(R.id.profileImageView);

        Glide.with(this)
                .load(selectedImageUri)
                .placeholder(R.drawable.pokemon)
                .apply(RequestOptions.circleCropTransform())
                .into(profileImg);

    }

    public interface OnChangeImageButtonClickListener {
        void onChangeImageButtonClick();
    }

    public void setOnChangeImageButtonClickListener(OnChangeImageButtonClickListener listener) {
        this.changeImageButtonClickListener = listener;
    }
}
