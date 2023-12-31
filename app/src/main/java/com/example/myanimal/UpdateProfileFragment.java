package com.example.myanimal;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.textfield.TextInputEditText;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class UpdateProfileFragment extends Fragment {

    private View view;
    private NavBar navBar;
    private EditText editBio, editName;
    private TextInputEditText editDOB;
    private Calendar calendar;
    private ImageButton saveProfile, backProfile;
    private NavController navController;
    private ProfileUpdateListener profileUpdateListener;
    private HungerViewModel viewModel;
    private ProfileFragment.OnChangeImageButtonClickListener changeImageButtonClickListener;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            profileUpdateListener = (ProfileUpdateListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement ProfileUpdateListener");
        }

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.update_profile_fragment, container, false);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        //Initialize NavBar
        if (getActivity() instanceof NavBar) {
            navBar = (NavBar) getActivity();
        }

        viewModel = new ViewModelProvider(requireActivity()).get(HungerViewModel.class);

        editBio = view.findViewById(R.id.edit_bio);
        //Set maximum words for bio
        editBio.addTextChangedListener(textWatcher);
        //Set previous text
        editBio.setText(viewModel.getProfileBio());

        editDOB = view.findViewById(R.id.date_of_birth);
        editDOB.setText(viewModel.getProfileDOB());

        editName = view.findViewById(R.id.edit_name);
        editName.addTextChangedListener(nameWatcher);
        editName.setText(viewModel.getProfileName());

        calendar = Calendar.getInstance();
        updateLabel();
        editDOB.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                showDatePickerDialog();
            }
        });

        setProfileUpdateListener(profileUpdateListener);

        saveProfile = view.findViewById(R.id.save_profile_button);
        backProfile = view.findViewById(R.id.back_profile_button);

        saveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                updateProfile();
            }


        });

        backProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.to_profile);
                navBar.main.setBackgroundColor(Color.parseColor("#FFC7C2"));

            }
        });

        //Initialize card view image to make it as button for profile image change
        CardView profileCardView = view.findViewById(R.id.profile_card_view);
        profileCardView.setCardBackgroundColor(Color.WHITE);
        profileCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        return view;
    }

    public void setProfileUpdateListener(ProfileUpdateListener listener) {
        this.profileUpdateListener = listener;
    }
    private void updateProfile() {

        String name = editName.getText().toString();
        String bio = editBio.getText().toString();
        String Dob = editDOB.getText().toString();

        if (profileUpdateListener != null) {
            navController.navigateUp();
            navBar.main.setBackgroundColor(Color.parseColor("#FFC7C2"));
            profileUpdateListener.onUpdateProfile(name, bio, Dob);
            Toast.makeText(requireContext(), "updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "Fail to update", Toast.LENGTH_SHORT).show();
        }
    }

    public interface ProfileUpdateListener {
        void onUpdateProfile(String name, String bio, String dob);
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        // Get the current date
        Calendar calendar_min = Calendar.getInstance();

        // Subtract 100 year from the current date
        calendar_min.add(Calendar.YEAR, -100);

        // Get the time in milliseconds since 1903
        long minDateInMillis = calendar_min.getTimeInMillis();

        // Get the current date
        Calendar calendar_max = Calendar.getInstance();

        // Get the time in milliseconds from current date
        long maxDateInMillis = calendar_max.getTimeInMillis();

        // Set a minimum and maximum date
         datePickerDialog.getDatePicker().setMinDate(minDateInMillis);
         datePickerDialog.getDatePicker().setMaxDate(maxDateInMillis);

        datePickerDialog.show();
    }

    //Update label on date of birth edit
    private void updateLabel() {
        String dateFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
        editDOB.setText(sdf.format(calendar.getTime()));
    }

    private final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // Update the calendar with the selected date
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            // Update the EditText with the selected date
            updateLabel();
        }
    };

    //Set it to navBar
    public void setOnChangeImageButtonClickListener(ProfileFragment.OnChangeImageButtonClickListener listener) {
        this.changeImageButtonClickListener = listener;
    }

    public void onImageSelected(Uri selectedImageUri) {
        viewModel.setImageUri(selectedImageUri.toString());
        ImageView profileImg = view.findViewById(R.id.cardViewImage);

        Glide.with(this)
                .load(selectedImageUri)
                .placeholder(R.drawable.pokemon)
                .error(R.drawable.pokemon)
                .apply(RequestOptions.circleCropTransform())
                .into(profileImg);
    }

    //For bio to set a maximum amount of word so it does not goes over the limit
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            // Not used
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            // Not used
        }

        @Override
        public void afterTextChanged(Editable editable) {
            int maxWords = 100; // Set the maximum number of words here
            if (editable.length() > maxWords) {
                editable.replace(maxWords, editable.length(), "");
                Toast.makeText(requireContext(), "Reached maximum words", Toast.LENGTH_SHORT).show();
            }
        }
    };


    //For name to set a maximum amount of word so it does not goes over the limit
    private TextWatcher nameWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            // Not used
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            // Not used
        }

        @Override
        public void afterTextChanged(Editable editable) {
            int maxWords = 20; // Set the maximum number of words here
            if (editable.length() > maxWords) {
                editable.replace(maxWords, editable.length(), "");
                Toast.makeText(requireContext(), "Reached maximum words", Toast.LENGTH_SHORT).show();
            }
        }
    };

}