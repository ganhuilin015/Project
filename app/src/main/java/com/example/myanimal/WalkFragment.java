package com.example.myanimal;

import static androidx.core.content.ContextCompat.getSystemService;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;

public class WalkFragment extends Fragment implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor stepCounterSensor;
    private View view;
    private static final int REQUEST_ACTIVITY_RECOGNITION_PERMISSION = 100;
    private boolean isStartButtonPressed = false;
    private  int initialStepCount, stepsTaken;
    private HungerViewModel viewModel;
    private TextView stepCountTextView;
    private ImageButton walkButton, backArrow;
    private boolean isSensorListenerRegistered = false;
    private LifecycleOwner viewLifecycleOwner;
    private NavBar navBar;
    private NavController navController;




    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.walk_fragment, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(HungerViewModel.class);

        ImageView gifImageView = view.findViewById(R.id.gifImageView);
        Glide.with(this).asGif().load(R.raw.petswalking_nobackground).into(gifImageView);

        // Initialize the SensorManager
        sensorManager = (SensorManager) requireContext().getSystemService(Context.SENSOR_SERVICE);

        // Get the step counter sensor
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        // Check if the permission is granted, and request it if not
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACTIVITY_RECOGNITION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACTIVITY_RECOGNITION},
                    REQUEST_ACTIVITY_RECOGNITION_PERMISSION);
        } else {
            // Permission is already granted
        }


        //Initialize the navcontroller for navigation
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        //Initialize NavBar
        if (getActivity() instanceof NavBar) {
            navBar = (NavBar) getActivity();
        }

        viewLifecycleOwner = getViewLifecycleOwner();

        walkButton = view.findViewById(R.id.walkingButton);
        walkButton.setImageResource(viewModel.getWalkImageUri());
        stepCountTextView = view.findViewById(R.id.stepCountTextView);
        updateStepCount(stepsTaken);
        isStartButtonPressed = viewModel.getStepsBool();
        isSensorListenerRegistered = viewModel.getRegisteredBool();

        walkButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (!isStartButtonPressed){
                    isStartButtonPressed = true;
                    viewModel.setStepsBool(true);
                    walkButton.setImageResource(R.drawable.endwalking);
                    viewModel.setWalkImageUri(R.drawable.endwalking);
                    onStart();
                } else{
                    isStartButtonPressed = false;
                    viewModel.setStepsBool(false);
                    walkButton.setImageResource(R.drawable.startwalking);
                    viewModel.setWalkImageUri(R.drawable.startwalking);
                    viewModel.setSteps(0);
                    updateStepCount(stepsTaken);
                    onStop();
                }

            }
        });
        backArrow = view.findViewById(R.id.backarrow);
        backArrow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                navController.navigate(R.id.to_activity);
                navBar.main.setBackgroundColor(Color.parseColor("#CED5D5"));
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Observe the steps LiveData from HungerViewModel
        viewModel.getSteps().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer steps) {
                updateStepCount(steps); // Update the step count TextView
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Register the step counter sensor listener
        if (isStartButtonPressed && !isSensorListenerRegistered){
            if (stepCounterSensor != null) {
                Log.d("Started walking", "walking");
                sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL);
                isSensorListenerRegistered = true;
                viewModel.setRegisteredBool(true);
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        // Unregister the sensor listener to save battery
        if (!isStartButtonPressed && isSensorListenerRegistered){
            Log.d("stopped walking", "walking");
            sensorManager.unregisterListener(this);
            isSensorListenerRegistered = false;
            viewModel.setRegisteredBool(false);
            viewModel.setInitialSteps(0);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            initialStepCount = viewModel.getInitialSteps();
            if (initialStepCount == 0) {
                initialStepCount = (int) event.values[0];
                viewModel.setInitialSteps(initialStepCount);
            }
            int currentStepCount = (int) event.values[0];
            stepsTaken = currentStepCount - initialStepCount;
            Log.d("Step Taken", String.valueOf(stepsTaken));
            viewModel.setSteps(stepsTaken);
            updateStepCount(stepsTaken);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used in this example
    }

    private void updateStepCount(int steps) {
        stepCountTextView.setText(getString(R.string.total_steps, steps));
        Log.d("Step Taken Updated", String.valueOf(stepsTaken));
    }

}
