package com.example.myanimal;

import android.graphics.Color;
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

public class GalleryFragment extends Fragment {
    private View view;
    private NavController navController;
    private NavBar navBar;
    private HungerViewModel viewModel;
    private ImageButton backButton;
    private GalleryAdapter galleryAdapter;
    private RecyclerView galleryRecyclerView;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.gallery_fragment, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(HungerViewModel.class);

        //Initialize the navcontroller for navigation
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        //Initialize NavBar
        if (getActivity() instanceof NavBar) {
            navBar = (NavBar) getActivity();
        }

        backButton = view.findViewById(R.id.backbutton);
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                navController.navigate(R.id.to_draw);
                navBar.main.setBackgroundColor(Color.parseColor("#B1D5FF"));
            }
        });

        List<GalleryItem> galleries = viewModel.getGalleryList();
        galleryAdapter = new GalleryAdapter(galleries);

        galleryRecyclerView = view.findViewById(R.id.recyclerViewGallery);
        galleryRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        galleryRecyclerView.setAdapter(galleryAdapter);
        return view;
    }
}
