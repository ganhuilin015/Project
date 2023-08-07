package com.example.myanimal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {
    private List<Record> recordList;

    public RecordAdapter(List<Record> recordList) {
        this.recordList = recordList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_record_fragment, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Record record = recordList.get(position);
        holder.dateTextView.setText(record.getDateandTime());
        holder.stepsTextView.setText(String.valueOf(record.getStepsTaken()));
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView, stepsTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            stepsTextView = itemView.findViewById(R.id.entryTextView);
        }
    }
}

