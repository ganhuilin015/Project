package com.example.myanimal;

import android.graphics.PointF;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {

    private List<FeedItem> feedItemList;
    private FeedItem item;

    public FeedAdapter(List<FeedItem> feedItemList) {

        this.feedItemList = feedItemList;
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed_fragment, parent, false);
        return new FeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder holder, int position) {
        item = feedItemList.get(position);

        Log.d("feed adapter", String.valueOf(holder.itemView.getContext()));
        Glide.with(holder.itemView.getContext())
                .load(Uri.parse(item.getImageUri()))
                .placeholder(R.drawable.person)
                .error(R.drawable.person)
                .into(holder.imageView);

        holder.imageView.setImageMatrix(item.getFinalPosition());
        holder.captionTextView.setText(item.getCaption());
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedItemList.remove(position);
                notifyDataSetChanged();

            }
        });
    }

    @Override
    public int getItemCount() {
        return feedItemList.size();
    }


    static class FeedViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView captionTextView;
        ImageButton deleteButton;

        FeedViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            captionTextView = itemView.findViewById(R.id.captionTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);

        }
    }

}

