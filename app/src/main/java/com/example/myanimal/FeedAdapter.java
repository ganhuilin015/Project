package com.example.myanimal;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {

    private List<FeedItem> feedItemList;

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
        FeedItem item = feedItemList.get(position);
        holder.imageView.setImageURI(Uri.parse(item.getImageUri()));
        holder.captionTextView.setText(item.getCaption());
    }

    @Override
    public int getItemCount() {
        return feedItemList.size();
    }

    static class FeedViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView captionTextView;

        FeedViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            captionTextView = itemView.findViewById(R.id.captionTextView);
        }
    }
}

