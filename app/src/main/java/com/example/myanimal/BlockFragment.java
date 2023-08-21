package com.example.myanimal;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class BlockFragment extends Fragment implements View.OnTouchListener{

    private NavBar navBar;
    private int coinCount;
    private NavController navController;
    private HungerViewModel viewModel;
    private RelativeLayout gameLayout;
    private LinearLayout gameOverLayout;
    private List<Block> blocks = new ArrayList<>();
    private int initialDelayMillis = 800;
    private int blockCounter = 0;
    private int playerScore = 0;
    private View blockView;
    private View movingObject;
    private float initialX, initialY;
    private float offsetX, offsetY;
    private Handler handler;
    private TextView scoreBoard;
    private ImageButton restartButton, startGameButton;
    private float initialMovingObjectX, initialMovingObjectY;



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.block_fragment, container, false);

        //Initialize viewModel to get the data from hunger view model for the same profile picture
        viewModel = new ViewModelProvider(requireActivity()).get(HungerViewModel.class);

        //Initialize the navcontroller for navigation
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        if (getActivity() instanceof NavBar) {
            navBar = (NavBar) getActivity();
        }

        gameLayout = view.findViewById(R.id.gameLayout);
        gameOverLayout = view.findViewById(R.id.gameOverLayout);

        ImageButton backArrow = view.findViewById(R.id.backarrow);
        backArrow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                navController.navigate(R.id.to_activity);
                navBar.main.setBackgroundColor(Color.parseColor("#CED5D5"));
            }
        });

        ImageButton drawButton = view.findViewById(R.id.draw_button);
        drawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.to_draw);
            }
        });

        ImageButton switchClickButton = view.findViewById(R.id.switch_click_button);
        switchClickButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                navController.navigate(R.id.to_earn);
            }
        });

        scoreBoard = view.findViewById(R.id.score_board);


        startGameButton = view.findViewById(R.id.start_button);
        startGameButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startGame();
            }
        });

        restartButton = view.findViewById(R.id.restart_button);
        restartButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                gameOverLayout.setVisibility(View.INVISIBLE);
                startGame();
            }
        });

        // Initialize your moving object (e.g., an ImageView)
        movingObject = view.findViewById(R.id.moving_object);
        movingObject.setOnTouchListener(this);

        // Use ViewTreeObserver to get the dimensions after layout has been drawn
        gameLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Remove the listener to avoid duplicate calls
                gameLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                // Now you can get the width of the gameFrameLayout
                int screenWidth = gameLayout.getWidth();
                int screenHeight = gameLayout.getHeight();
                Log.d("screen width", String.valueOf(screenWidth));

                initialMovingObjectX = (screenWidth - movingObject.getWidth())/2 ;
                initialMovingObjectY = screenHeight/2;
            }
        });

        return view;
    }

    private  void startGame(){
        movingObject.setVisibility(View.VISIBLE);
        startGameButton.setVisibility(View.INVISIBLE);

        movingObject.setX(initialMovingObjectX);
        movingObject.setY(initialMovingObjectY);


        // Use a handler to generate blocks at regular intervals
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                scoreBoard.setText("Score: " + playerScore);
                playerScore ++;
                for (Block block : blocks) {

                    block.setY(block.getY() + block.getSpeed()); // Update Y coordinate based on speed
                    updateBlockPosition(block);

                    // Check if the block has reached the bottom of the screen
                    Log.d("game layout height", String.valueOf(gameLayout.getHeight()));
                    Log.d("block y", String.valueOf(block.getY()));

                }
                generateBlock();

                // Decrease the delay time with time or block generation
                if (blockCounter % 10 == 0) {
                    Log.d("initial delay millis", String.valueOf(initialDelayMillis));
                    if (initialDelayMillis > 500){
                        initialDelayMillis -= 30; // Decrease delay by 50 milliseconds every 10 blocks
                    }
                }

                handler.postDelayed(this, initialDelayMillis);
                blockCounter++;
            }
        }, initialDelayMillis);
    }
    private void generateBlock() {

        if (!isAdded()) {
            //Do not proceed if fragment not made
        } else{
            int screenWidth = getResources().getDisplayMetrics().widthPixels;
            int blockSize = 50;
            int color = Color.rgb(new Random().nextInt(256), new Random().nextInt(256), new Random().nextInt(256));

            int randomX;
            int maxAttempts = 10; // Set a maximum number of attempts
            int attempts = 0;     // Initialize the attempts counter

            do {
                randomX = new Random().nextInt(screenWidth - blockSize);
                attempts++;

                // Break the loop if maximum attempts are reached
                if (attempts >= maxAttempts) {
                    break;
                }
            } while (isOverlapping(randomX, blockSize, 0));

            if (attempts < maxAttempts) {
                // Successfully found a non-overlapping position for the block
                Block block = new Block(randomX, 0, blockSize, color, 30);
                blocks.add(block);

                blockView = new View(requireContext());
                blockView.setBackgroundColor(color);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(blockSize, blockSize);
                params.setMargins(randomX, 0, 0, 0);
                blockView.setLayoutParams(params);

                block.setView(blockView);
                gameLayout.addView(blockView);
            } else {
                Log.d("Max attempts reached", "Unable to find a non-overlapping position");
            }
        }


    }

    private void updateBlockPosition(Block block) {
        View blockView = gameLayout.getChildAt(blocks.indexOf(block));
        if (blockView != null) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) blockView.getLayoutParams();
            params.setMargins(block.getX(), block.getY(), 0, 0);
            blockView.setLayoutParams(params);
        }
    }

    private void removeAllBlocks() {
        for (Block block : blocks) {
            gameLayout.removeView(block.getView()); // Remove the view from the layout
        }
        blocks.clear(); // Clear the blocks list
    }

    private boolean isOverlapping(int x, int blockSize, int y) {
        for (Block block : blocks) {
            int blockX = block.getX();
            int blockY = block.getY();
            int blockWidth = block.getSize();

            // Check if the new block's X position overlaps with the existing block
            if (x < blockX + blockWidth && x + blockSize > blockX && y < blockY + blockWidth && y + blockSize > blockY) {
                return true; // Overlapping
            }
        }
        return false; // Not overlapping
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Capture the initial touch position
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

                // Check for collisions with falling blocks
                checkCollisionWithBlocks(newX, newY);

                break;
        }
        return true;
    }

    private void checkCollisionWithBlocks(float x, float y) {
        for (Block block : blocks) {
            int blockX = block.getX();
            int blockY = block.getY();
            int blockWidth = block.getSize();

            if (x < blockX + blockWidth && x + movingObject.getWidth() > blockX +20 &&
                    y + 20 < blockY + blockWidth && y + movingObject.getHeight() > blockY + 20) {
                // Collision detected, trigger game over
                handleGameOver();
                break;
            }
        }
    }

    private void handleGameOver() {
        coinCount = navBar.getCoinCount();
        // Update the coin count
        coinCount = coinCount + playerScore;
        navBar.updateCoinCount(coinCount);

        removeAllBlocks();
        gameOverLayout.setVisibility(View.VISIBLE);
        movingObject.setVisibility(View.INVISIBLE);

        playerScore = 0;

        handler.removeCallbacksAndMessages(null);
    }

}
