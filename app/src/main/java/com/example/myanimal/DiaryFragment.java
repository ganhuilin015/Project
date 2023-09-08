package com.example.myanimal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DiaryFragment extends Fragment {
    private NavBar navBar;
    private NavController navController;
    private HungerViewModel viewModel;
    private ImageButton addDiaryButton, backButton;
    private RecyclerView diaryRecyclerView;
    private DiaryAdapter diaryAdapter;
    private MediaPlayer mediaPlayer;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.diary_fragment, container, false);

        //Initialize viewModel to get the data from hunger view model for the same profile picture
        viewModel = new ViewModelProvider(requireActivity()).get(HungerViewModel.class);

        //Initialize the navcontroller for navigation
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        if (getActivity() instanceof NavBar) {
            navBar = (NavBar) getActivity();
        }

        backButton = view.findViewById(R.id.backArrow);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mediaPlayer = MediaPlayer.create(requireContext(), R.raw.click_back);

                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mediaPlayer.release();
                    }
                });

                navController.navigate(R.id.to_activity);
                navBar.main.setBackgroundColor(Color.parseColor("#CED5D5"));
            }
        });

        addDiaryButton = view.findViewById(R.id.plusButton);
        addDiaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                navController.navigate(R.id.to_adddiarynote);

//                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
//                builder.setTitle("Choose an Option")
//                        .setItems(new CharSequence[]{"Writing", "Voice Recording"}, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                if (which == 0) {
//                                    navController.navigate(R.id.to_adddiarynote);
//                                } else if (which == 1) {
//                                    navController.navigate((R.id.to_adddiaryvoice));
//                                }
//                            }
//                        });
//
//                AlertDialog dialog = builder.create();
//                dialog.show();

            }
        });

        List<Diary> diaries = viewModel.getDiaryNoteList();
        diaryAdapter = new DiaryAdapter(requireContext(), diaries);

        diaryRecyclerView = view.findViewById(R.id.recyclerViewDiary);
        diaryRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        diaryRecyclerView.setAdapter(diaryAdapter);

        return view;
    }
}
