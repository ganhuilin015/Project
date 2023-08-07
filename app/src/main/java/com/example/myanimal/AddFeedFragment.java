package com.example.myanimal;

import static android.app.Activity.RESULT_OK;
import static android.app.usage.UsageEvents.Event.NONE;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

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
        imageView.setOnTouchListener(this);
        scroller = new Scroller(requireContext());


        feedImageUri = viewModel.getFeedImageUri();

        Log.d("feed image uri", String.valueOf(feedImageUri));
        Glide.with(this)
                .load(feedImageUri)
                .placeholder(R.drawable.person)
                .error(R.drawable.person)
                .into(imageView);


        ImageButton post_button = view.findViewById(R.id.postButton);
        post_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                postImageAndCaption();
            }
        });

        return view;
    }

    private void postImageAndCaption() {

        EditText captionEditText = view.findViewById(R.id.captionEditText);

        String caption = captionEditText.getText().toString().trim();
        if (!TextUtils.isEmpty(caption)) {

            // Add the new item to the top of the feed list
            FeedItem newItem = new FeedItem(feedImageUri, caption);
            viewModel.addFeed(newItem);
            feedadapter.notifyItemInserted(0);

            // Clear the image and caption fields after posting
            imageView.setImageResource(R.drawable.person);
            captionEditText.setText("");
        } else {
            Toast.makeText(requireContext(), "Please enter a caption.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // Handle touch events here
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                lastTouchY = event.getY();
                lastTouchX = event.getX();
                Log.d("action down", String.valueOf(lastTouchY));
                isDragging = true;
                break;

            case MotionEvent.ACTION_MOVE:
                if (isDragging) {
                    Log.d("In move ", "action");
                    float dy = event.getY() - lastTouchY;
                    float dx = event.getX() - lastTouchX;

                    lastTouchY = event.getY();
                    lastTouchX = event.getX();

                    matrix = imageView.getImageMatrix();
                    matrix.postTranslate(dx, dy); // Move the image vertically

                    // Limit the movement of the image within the fixed height of the ImageView
                    float[] matrixValues = new float[9];
                    matrix.getValues(matrixValues);

                    float translateY = matrixValues[Matrix.MTRANS_Y];
                    float translateX = matrixValues[Matrix.MTRANS_X];

                    int imageViewHeight = imageView.getHeight();
                    int imageViewWidth = imageView.getWidth();
                    Bitmap drawableBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                    int drawableHeight = drawableBitmap.getHeight();
                    int drawableWidth = drawableBitmap.getWidth();

                    // Check if the image goes beyond the top or bottom of the ImageView
                    if (translateY > 0) {
                        matrix.postTranslate(0, -translateY); // Move back to the top
                    } else if (drawableHeight + translateY < imageViewHeight) {
                        matrix.postTranslate(0, imageViewHeight - drawableHeight - translateY); // Move back to the bottom
                    }

                    // Check if the image goes beyond the left or right of the ImageView
                    if (translateX > 0) {
                        matrix.postTranslate(-translateX, 0); // Move back to the left
                    } else if (drawableWidth + translateX < imageViewWidth) {
                        matrix.postTranslate(imageViewWidth - drawableWidth - translateX, 0); // Move back to the right
                    }

                    imageView.setImageMatrix(matrix);
                    imageView.invalidate();
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;

            default:
                return false;
        }
        return true;
    }

}
