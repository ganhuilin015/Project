package com.example.myanimal;


import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {

    private List<GalleryItem> galleryItemList;



    public GalleryAdapter(List<GalleryItem> galleryItemList) {

        this.galleryItemList = galleryItemList;
    }

    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery_fragment, parent, false);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder holder, int position) {
        GalleryItem item = galleryItemList.get(position);

        Log.d("imge uri gallery", String.valueOf(item.getImageUri()));
        Log.d("holder item view", String.valueOf(holder.itemView.getContext()));
        Glide.with(holder.itemView.getContext())
                .load(Uri.parse(item.getImageUri()))
                .placeholder(R.drawable.person)
                .error(R.drawable.person)
                .into(holder.galleryView);


        holder.dateTextView.setText(item.getDateandTime());

    }

    @Override
    public int getItemCount() {

        return galleryItemList.size();
    }

    static class GalleryViewHolder extends RecyclerView.ViewHolder {
        ImageView galleryView;
        TextView dateTextView;

        GalleryViewHolder(View itemView) {
            super(itemView);
            galleryView = itemView.findViewById(R.id.galleryView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
        }
    }
}

