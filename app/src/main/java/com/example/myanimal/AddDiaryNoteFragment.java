package com.example.myanimal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddDiaryNoteFragment extends Fragment {

    private HungerViewModel viewModel;
    private NavController navController;
    private NavBar navBar;

    private ImageButton exitButton, doneButton;
    EditText diaryTitle, diaryText;
    private DiaryAdapter diaryAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.adddiarynote_fragment, container, false);

        //Initialize viewModel to get the data from hunger view model for the same profile picture
        viewModel = new ViewModelProvider(requireActivity()).get(HungerViewModel.class);

        //Initialize the navcontroller for navigation
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        diaryAdapter = new DiaryAdapter(requireContext(), viewModel.getDiaryNoteList());

        if (getActivity() instanceof NavBar) {
            navBar = (NavBar) getActivity();
        }

        diaryTitle = view.findViewById(R.id.titleEditText);
        diaryText = view.findViewById(R.id.noteEditText);

        exitButton = view.findViewById(R.id.exitButton);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.to_diary);
            }
        });

        doneButton = view.findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Diary newDiary = new Diary(diaryTitle.getText().toString(), diaryText.getText().toString(), getCurrentDateAndTime());
                viewModel.addDiaryNoteList(newDiary);
                diaryAdapter.notifyItemInserted(viewModel.getDiaryNoteList().size() - 1);
                navController.navigate(R.id.to_diary);
            }
        });

    return view;
    }

    private String getCurrentDateAndTime(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

}
