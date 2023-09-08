package com.example.myanimal;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import android.view.MotionEvent;

import java.util.Arrays;
import java.util.List;

public class VetFragment extends Fragment implements View.OnTouchListener{

    private NavBar navBar;
    private NavController navController;
    private HungerViewModel viewModel;
    private MediaPlayer mediaPlayer;
    private ImageButton backButton, nextButton, previousButton;
    private ImageView movingTools;
    private float initialX, initialY, offsetX, offsetY;
    private boolean isZoomed = false;
    private float originalWidth, originalHeight;
    private List<Integer> tools_list = Arrays.asList(R.drawable.stethoscope, R.drawable.pills, R.drawable.injection__needle_);
    private int tools_num = 1;
    private int tools_num_prev = 2;
    private float originalX, originalY;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vet_fragment, container, false);

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

        movingTools = view.findViewById(R.id.movingTools);
        movingTools.setOnTouchListener(this);

        // Store the original width and height of the "movingTools" ImageView
        originalWidth = movingTools.getWidth();
        originalHeight = movingTools.getHeight();

        // Inside onCreateView method
//        originalX = movingTools.getX();
//        originalY = movingTools.getY();
//        Log.d("original X", String.valueOf(originalX));
//        Log.d("original Y", String.valueOf(originalY));

        // Wait until the view is laid out on the screen
        movingTools.post(new Runnable() {
            @Override
            public void run() {
                int[] location = new int[2];
                movingTools.getLocationOnScreen(location);
                originalX = location[0]; // X coordinate
                originalY = location[1]; // Y coordinate
            }
        });

        nextButton = view.findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tools_num <= 0){
                    tools_num = 1;
                }
                int toolNum = tools_num % 3;

                int toolsView = tools_list.get(toolNum);
                movingTools.setImageResource(toolsView);
                tools_num ++;
            }
        });
        previousButton = view.findViewById(R.id.previous_button);
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tools_num -- ;
                int toolNum = tools_num % 3;
                if (tools_num <= 0) {
                    toolNum = 0;
                } else if (toolNum == 0){
                    toolNum = 2;
                }
                else {
                    toolNum = toolNum - 1;
                }
                int toolsView = tools_list.get(toolNum);
                movingTools.setImageResource(toolsView);
            }
        });

        return view;

}

    @Override
    public boolean onTouch(View view, MotionEvent event) {

        View parentView = (View) view.getParent();

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                // Capture the initial touch coordinates.
                initialX = event.getRawX();
                initialY = event.getRawY();
                offsetX = view.getX() - initialX;
                offsetY = view.getY() - initialY;

                break;
            case MotionEvent.ACTION_MOVE:
                // Calculate the new position based on touch movement
                float newX = event.getRawX() + offsetX;
                float newY = event.getRawY() + offsetY;

                // Update the view's position
                view.setX(newX);
                view.setY(newY);
                isZoomed = true;

                isEnlarged();

                break;
            case MotionEvent.ACTION_UP:
                isZoomed = false;
                // Get the parent's dimensions
                int parentWidth = parentView.getWidth();
                int parentHeight = parentView.getHeight();

                isEnlarged();
                break;
        }
        return true;
    }

    public void isEnlarged(){
        ViewGroup.LayoutParams params = movingTools.getLayoutParams();

        if (isZoomed){
            Log.d("moving tools params", String.valueOf(params));
            // Update the width and height
            params.width = 800;
            params.height = 800;

        } else{
            // Update the width and height
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;

            if (params instanceof FrameLayout.LayoutParams) {
                Log.d("framlaoyut", "in fram layout");
                FrameLayout.LayoutParams frameLayoutParams = (FrameLayout.LayoutParams) params;

                // Update the gravity of the view
                frameLayoutParams.gravity = Gravity.NO_GRAVITY;

                Log.d("oriX", String.valueOf(originalX));
                Log.d("oriY", String.valueOf(originalY));

                // Reset the position to the original coordinates
                movingTools.setX(originalX);
                movingTools.setY(originalY - 250);

                // Apply the modified layout parameters
//                movingTools.setLayoutParams(frameLayoutParams);
            }

        }
        movingTools.setLayoutParams(params);

    }

    }
