package com.example.myanimal;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.PointF;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.List;

public class DiaryAudioAdapter extends RecyclerView.Adapter<DiaryAudioAdapter.DiaryViewHolder> {

    private List<DiaryAudio> diaryAudioList;
    private DiaryAudio diaryitem;
    private Context context;


    public DiaryAudioAdapter(Context context, List<DiaryAudio> diaryList) {
        this.context = context;
        this.diaryAudioList = diaryList;
    }

    @NonNull
    @Override
    public DiaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.diary_note_fragment, parent, false);
        return new DiaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiaryViewHolder holder, int position) {
        diaryitem = diaryAudioList.get(position);

        holder.diaryTitle.setText(diaryitem.getTitle());
        holder.diaryDateTime.setText(diaryitem.getDate());

    }

    @Override
    public int getItemCount() {
        return diaryAudioList.size();
    }


    static class DiaryViewHolder extends RecyclerView.ViewHolder {
        TextView diaryTitle;
        TextView diaryDateTime;


        DiaryViewHolder(View itemView) {
            super(itemView);
            diaryTitle = itemView.findViewById(R.id.diaryTitleTextView);
            diaryDateTime = itemView.findViewById(R.id.dateTextView);
        }
    }

}

