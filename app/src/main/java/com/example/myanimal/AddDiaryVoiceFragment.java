package com.example.myanimal;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddDiaryVoiceFragment extends Fragment {

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 88;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 99;
    private HungerViewModel viewModel;
    private NavController navController;
    private NavBar navBar;

    private ImageButton exitButton, doneButton;
    EditText diaryTitle;
    ImageButton diaryAudio;
    private DiaryAudioAdapter diaryAdapter;
    private boolean isAudioOn = true;
    private String outputFilePath;
    private Context context;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.adddiaryvoice_fragment, container, false);

        //Initialize viewModel to get the data from hunger view model for the same profile picture
        viewModel = new ViewModelProvider(requireActivity()).get(HungerViewModel.class);

        //Initialize the navcontroller for navigation
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        diaryAdapter = new DiaryAudioAdapter(requireContext(), viewModel.getDiaryVoiceList());

        if (getActivity() instanceof NavBar) {
            navBar = (NavBar) getActivity();
        }

        diaryTitle = view.findViewById(R.id.titleEditText);

        exitButton = view.findViewById(R.id.exitButton);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.to_diary);
            }
        });

        // Initialize MediaRecorder
        MediaRecorder mediaRecorder = new MediaRecorder();

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
        }

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION);
        }

        diaryAudio = view.findViewById(R.id.audioButton);
        diaryAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAudioOn){
                    String outputDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/com.example.myanimal/";
//                    if (dir != null) {
//                        String fileName = "recorded_audio_" + System.currentTimeMillis() + ".mp3";
//                        outputFilePath = new File(dir, fileName).getAbsolutePath();
//                        Log.d(TAG, "Output File Path: " + outputFilePath);
//
//                    } else {
//                        Log.e(TAG, "External storage not available.");
//                    }
                    File dir = new File(outputDir);

                    // Create the directory if it doesn't exist
                    if (!dir.exists()) {
                        dir.mkdirs();
                        Log.d("dk mkdir", String.valueOf(dir.mkdirs()));
                    }

                    String fileName = "recorded_audio_" + System.currentTimeMillis() + ".mp3";
                    outputFilePath = outputDir + fileName;
                    Log.d(TAG, "Output File Path: " + outputFilePath);

                    diaryAudio.setImageResource(R.drawable.records);
                    isAudioOn = false;


                    // Start recording
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                    mediaRecorder.setOutputFile(outputFilePath);
                    try {
                        mediaRecorder.prepare();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    mediaRecorder.start();
                } else{
                    diaryAudio.setImageResource(R.drawable.diarytitle);
                    // Stop recording
                    mediaRecorder.stop();
                    mediaRecorder.release();
                    isAudioOn = true;
                }
            }
        });


        doneButton = view.findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DiaryAudio newDiaryAudio = new DiaryAudio(outputFilePath, diaryTitle.getText().toString(), getCurrentDateAndTime());
                viewModel.addDiaryVoiceList(newDiaryAudio);
                diaryAdapter.notifyItemInserted(viewModel.getDiaryVoiceList().size() - 1);
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
