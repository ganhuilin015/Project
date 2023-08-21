package com.example.myanimal;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;

public class AddFeedFragment extends Fragment implements View.OnTouchListener{

    private NavBar navBar;
    private NavController navController;
    private View view;
    private ImageView imageView;
    private HungerViewModel viewModel;
    private String feedImageUri;
    private FeedAdapter feedadapter;
    private float lastTouchY, lastTouchX;
    private boolean isDragging = false;
    private Scroller scroller;
    private Matrix matrix;
    private ImageButton post_button, exit_button;
    private List<FeedItem> feedItemList = new ArrayList<>();;



    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_feed_fragment, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(HungerViewModel.class);

        //Initialize the navcontroller for navigation
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        //Initialize NavBar
        if (getActivity() instanceof NavBar) {
            navBar = (NavBar) getActivity();
        }

        imageView = view.findViewById(R.id.imageViewPost);

        //Enable touching and scrolling on matrix imageview
        imageView.setOnTouchListener(this);
        scroller = new Scroller(requireContext());

        feedadapter = new FeedAdapter(feedItemList);
        feedImageUri = viewModel.getFeedImageUri();

        //Add image into the matrix image view
        Glide.with(this)
                .load(feedImageUri)
                .placeholder(R.drawable.person)
                .error(R.drawable.person)
                .into(imageView);


        post_button = view.findViewById(R.id.postButton);
        post_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                postImageAndCaption();
            }
        });

        exit_button = view.findViewById(R.id.exitButton);
        exit_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.to_profile);
                navBar.main.setBackgroundColor(Color.parseColor("#FFC7C2"));
            }
        });
        return view;
    }

    //Will call this function when post button is pressed
    private void postImageAndCaption() {

        EditText captionEditText = view.findViewById(R.id.captionEditText);

        String caption = captionEditText.getText().toString().trim();
        if (!TextUtils.isEmpty(caption)) {

            //Create a new feed item and add it to view model to store
            FeedItem newFeed = new FeedItem(feedImageUri, caption, matrix);
            viewModel.addFeed(newFeed);

            //Notify the adapter that new feed is inserted so recycler view will know when to update its content accordingly
            feedadapter.notifyItemInserted(0);

            // Clear the image and caption fields after posting
            imageView.setImageResource(R.drawable.person);
            captionEditText.setText("");

            //Navigate back to profile after successfully posted
            navController.navigate(R.id.to_profile);
            navBar.main.setBackgroundColor(Color.parseColor("#FFC7C2"));
            Toast.makeText(requireContext(), "Post successfully.", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(requireContext(), "Please enter a caption.", Toast.LENGTH_SHORT).show();

        }
    }

   //This function will be called if the image in matrix ImageView is moved
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            //When touch down is detected
            case MotionEvent.ACTION_DOWN:

                //Get the first touch position
                lastTouchY = event.getY();
                lastTouchX = event.getX();
                isDragging = true;
                break;

            case MotionEvent.ACTION_MOVE:

                if (isDragging) {

                    //Get the difference between the last touch and new touch
                    float dy = event.getY() - lastTouchY;
                    float dx = event.getX() - lastTouchX;

                    //Set new last touch for the next movement
                    lastTouchY = event.getY();
                    lastTouchX = event.getX();

                    //Get matrix of the current image position and add new translation to update the new matrix
                    matrix = imageView.getImageMatrix();
                    matrix.postTranslate(dx, dy); // Move the image vertically

                    // Limit the movement of the image within the fixed height of the ImageView
                    float[] matrixValues = new float[9];
                    matrix.getValues(matrixValues);
                    float translateY = matrixValues[Matrix.MTRANS_Y];
                    float translateX = matrixValues[Matrix.MTRANS_X];

                    //Get the height and width of image view and drawable image
                    int imageViewHeight = imageView.getHeight();
                    int imageViewWidth = imageView.getWidth();
                    Bitmap drawableBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                    int drawableHeight = drawableBitmap.getHeight();
                    int drawableWidth = drawableBitmap.getWidth();

                    // Check if the image goes beyond the top or bottom of the ImageView
                    if (translateY > 0) {
                        // Move back to the top
                        matrix.postTranslate(0, -translateY);
                    } else if (drawableHeight + translateY < imageViewHeight) {
                        matrix.postTranslate(0, imageViewHeight - drawableHeight - translateY);
                    }

                    // Check if the image goes beyond the left or right of the ImageView
                    if (translateX > 0) {
                        // Move back to the left
                        matrix.postTranslate(-translateX, 0);
                    } else if (drawableWidth + translateX < imageViewWidth) {
                        matrix.postTranslate(imageViewWidth - drawableWidth - translateX, 0); // Move back to the right
                    }

                    //Set image in image view with new matrix
                    imageView.setImageMatrix(matrix);

                    //To update the appearance of view in response to some change
                    imageView.invalidate();

                }
                break;

            default:
                return false;
        }
        return true;
    }
}
