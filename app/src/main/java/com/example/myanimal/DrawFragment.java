package com.example.myanimal;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import yuku.ambilwarna.AmbilWarnaDialog;

public class DrawFragment extends Fragment {
    private static final int REQUEST_CODE_STORAGE_PERMISSION = 99;
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 123;
    private HungerViewModel viewModel;
    private NavController navController;
    private NavBar navBar;
    private ImageButton colorPickerButton;
    private int selectedColor = Color.BLACK;
    private DrawingView drawingView;
    private PetsAdapter galleryAdapter;
    private Bitmap drawingBitmap;
    private int coinCount;
    private MediaPlayer mediaPlayer;





    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.draw_fragment, container, false);

        //Initialize viewModel to get the data from hunger view model for the same profile picture
        viewModel = new ViewModelProvider(requireActivity()).get(HungerViewModel.class);

        //Initialize the navcontroller for navigation
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        if (getActivity() instanceof NavBar) {
            navBar = (NavBar) getActivity();
        }

        ImageButton switchClickButton = view.findViewById(R.id.switch_click_button);
        switchClickButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                navController.navigate(R.id.to_earn);
            }
        });

        ImageButton blockButton = view.findViewById(R.id.block_button);
        blockButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                navController.navigate(R.id.to_block);
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

        colorPickerButton = view.findViewById(R.id.colorPickerButton);

        colorPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showColorPickerDialog();
            }
        });

        drawingView = view.findViewById(R.id.drawingView);
//        galleryAdapter = new PetsAdapter(galleryList);

        ImageButton saveDrawing = view.findViewById(R.id.saveDrawing);
        saveDrawing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingBitmap = drawingView.getDrawingBitmap();
                // Save the bitmap to a file
                String imagePath = saveBitmapToFile(drawingBitmap);
                Log.d("Draw fragment", String.valueOf(imagePath));
                //Add new record
//                String currentDateandTime = getCurrentDateAndTime();
//                PetsItem newGallery = new PetsItem(currentDateandTime, imagePath);
//                galleryList.add(newGallery);
//                galleryAdapter.notifyItemInserted(galleryList.size() - 1);
//                viewModel.addGallery(newGallery);
                Toast.makeText(requireContext(),"Saved", Toast.LENGTH_SHORT).show();


                coinCount = navBar.getCoinCount();
                // Update the coin count
                coinCount = coinCount + 20;
                navBar.updateCoinCount(coinCount);


//                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                    Log.d("Draw fragment", "requesting");
//                    requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//
//                } else {
//                    // Save the bitmap to a file
//                    String imagePath = saveBitmapToFile(drawingBitmap);
//                    Log.d("Draw fragment", String.valueOf(imagePath));
//                    //Add new record
//                    String currentDateandTime = getCurrentDateAndTime();
//                    GalleryItem newGallery = new GalleryItem(currentDateandTime, imagePath);
//                    galleryList.add(newGallery);
//                    galleryAdapter.notifyItemInserted(galleryList.size() - 1);
//                    viewModel.addGallery(newGallery);
//                    Toast.makeText(requireContext(),"Saved", Toast.LENGTH_SHORT).show();
//                }

            }
        });

        ImageButton eraserButton = view.findViewById(R.id.eraserButton);
        eraserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.toggleEraser(true);
            }
        });

        ImageButton drawingButton = view.findViewById(R.id.drawingButton);
        drawingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.toggleEraser(false);
            }
        });

        ImageButton clearButton = view.findViewById(R.id.clearDrawing);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear the drawing view
                drawingView.clear();
            }
        });

//        ImageButton galleryButton = view.findViewById(R.id.gallery);
//        galleryButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                navController.navigate(R.id.to_gallery);
////                navBar.main.setBackgroundColor(Color.parseColor("#FEFA9D"));
//                int permissionStatus = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
//                Log.d("permission status", String.valueOf(permissionStatus));
//                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                    if (permissionStatus == PackageManager.PERMISSION_DENIED) {
//                        if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                            Log.d("requesting read storage", "requesting");
//
//                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                            Uri uri = Uri.fromParts("package", requireContext().getPackageName(), null);
//                            intent.setData(uri);
//                            startActivity(intent);
//
//                            // ...
//                        } else {
//
//                        }
//                    }
//                    requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
//                } else {
//                    navController.navigate(R.id.to_gallery);
//                    navBar.main.setBackgroundColor(Color.parseColor("#FEFA9D"));
//                }
//
//            }
//        });


        return view;
    }

    //For user to pick the color of their brush
    private void showColorPickerDialog() {
    int initialColor = selectedColor != 0 ? selectedColor : Color.WHITE;
    AmbilWarnaDialog colorPickerDialog = new AmbilWarnaDialog(requireContext(), initialColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
        @Override
        public void onCancel(AmbilWarnaDialog dialog) {
            // Do nothing when the user cancels the color picker
        }

        @Override
        public void onOk(AmbilWarnaDialog dialog, int color) {
            selectedColor = color;
            drawingView.setColor(selectedColor);

        }
    });

    colorPickerDialog.show();
    }

    //Save to user gallery storage
    private String saveBitmapToFile(Bitmap bitmap) {
        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "DrawingApp");

        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = "drawing_" + System.currentTimeMillis() + ".png";
        File file = new File(directory, fileName);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    private String getCurrentDateAndTime(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }


//    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
//        new ActivityResultContracts.RequestPermission(),
//    isGranted -> {
//        Log.d("Is Granted", String.valueOf(isGranted));
//
//        if (isGranted) {
//            navController.navigate(R.id.to_gallery);
//            navBar.main.setBackgroundColor(Color.parseColor("#FEFA9D"));
//        } else {
//            // Permission denied, show a message to the user
//            Toast.makeText(requireContext(), "Permission denied. Cannot load drawing.", Toast.LENGTH_SHORT).show();
//        }
//    }
//);

}
