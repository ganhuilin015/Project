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

public class PetsAdapter extends RecyclerView.Adapter<com.example.myanimal.PetsAdapter.PetsViewHolder> {

    private List<Pets> PetsAdapter;
    private OnItemClickListener itemClickListener;



    public PetsAdapter(List<Pets> petsItemList, OnItemClickListener itemClickListener) {

        this.PetsAdapter = petsItemList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public PetsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pets_list, parent, false);
        return new PetsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PetsViewHolder holder, int position) {
        Pets item = PetsAdapter.get(position);

        Glide.with(holder.itemView.getContext())
                .load(item.getPetsImage())
                .placeholder(R.drawable.person)
                .error(R.drawable.person)
                .into(holder.petsView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("in inventory click", "inventory click");
                itemClickListener.onItemClick(item);
            }
        });

        holder.petsname.setText(item.getPetsName());

    }

    @Override
    public int getItemCount() {

        return PetsAdapter.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Pets item);
    }

    static class PetsViewHolder extends RecyclerView.ViewHolder {
        ImageView petsView;
        TextView petsname;

        PetsViewHolder(View itemView) {
            super(itemView);
            petsView = itemView.findViewById(R.id.petsview);
            petsname = itemView.findViewById(R.id.petsname);
        }
    }
}

