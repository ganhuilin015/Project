package com.example.myanimal;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class EarnFragment extends Fragment {

    private NavBar navBar;
    private int coinCount;
    private NavController navController;
    private HungerViewModel viewModel;
    private ImageButton earning;
    private MediaPlayer mediaPlayer;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.earn_fragment, container, false);

        //Initialize viewModel to get the data from hunger view model for the same profile picture
        viewModel = new ViewModelProvider(requireActivity()).get(HungerViewModel.class);

        //Initialize the navcontroller for navigation
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        if (getActivity() instanceof NavBar) {
            navBar = (NavBar) getActivity();
        }

        ImageButton drawButton = view.findViewById(R.id.draw_button);
        drawButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                navController.navigate(R.id.to_draw);
            }
        });

        ImageButton blockButton = view.findViewById(R.id.block_button);
        blockButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                navController.navigate(R.id.to_block);
            }
        });


        earning = view.findViewById(R.id.earningButton);
        earning.setOnTouchListener(new View.OnTouchListener(){

            float startY;
            @Override
            public boolean onTouch(View v, MotionEvent event){
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startY = event.getY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        float deltaY = event.getY() - startY;
                        if (deltaY < -50) { // Swipe up
                            performSwipeUpAction();
                        } else if (deltaY > 50) { // Swipe down
                            performSwipeDownAction();
                        }
                        return true;
                }
                return false;

            }
        });

        ImageButton backArrow = view.findViewById(R.id.backarrow);
        backArrow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
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

        return view;
    }

    private void performSwipeUpAction() {
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.switch_click);

        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.release();
            }
        });

        earning.setImageResource(R.drawable.switch_on);
        coinCount = navBar.getCoinCount();
        // Update the coin count
        coinCount++;
        navBar.updateCoinCount(coinCount);

    }

    private void performSwipeDownAction() {
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.switch_click);

        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.release();
            }
        });
        earning.setImageResource(R.drawable.switch_off);
        coinCount = navBar.getCoinCount();
        // Update the coin count
        coinCount++;
        navBar.updateCoinCount(coinCount);

    }

}
