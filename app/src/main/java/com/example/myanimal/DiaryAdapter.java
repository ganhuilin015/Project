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

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder> {

    private List<Diary> diaryList;
    private Diary diaryitem;
    private Context context;


    public DiaryAdapter(Context context, List<Diary> diaryList) {
        this.context = context;
        this.diaryList = diaryList;
    }

    @NonNull
    @Override
    public DiaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.diary_note_fragment, parent, false);
        return new DiaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiaryViewHolder holder, int position) {
        diaryitem = diaryList.get(position);

        holder.diaryTitle.setText(diaryitem.getDiaryTitle());
        holder.diaryDateTime.setText(diaryitem.getDiaryDateandTime());
        holder.linearDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Inflate the custom layout for the dialog
                View dialogView = LayoutInflater.from(context).inflate(R.layout.diary_description_fragment, null);

                // Create the AlertDialog
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context,  R.style.TransparentAlertDialog);
                alertDialogBuilder.setView(dialogView);


                TextView diaryTitle = dialogView.findViewById(R.id.diary_title);
                TextView diaryDAT = dialogView.findViewById(R.id.diary_dateAndTime);
                TextView diaryContent = dialogView.findViewById(R.id.diary_content);
                diaryTitle.setText(diaryitem.getDiaryTitle());
                diaryDAT.setText(diaryitem.getDiaryDateandTime());
                diaryContent.setText(diaryitem.getDiaryText());

                // Show the dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                ImageButton returnButton = dialogView.findViewById(R.id.returnButton);
                returnButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return diaryList.size();
    }


    static class DiaryViewHolder extends RecyclerView.ViewHolder {
        TextView diaryTitle;
        TextView diaryDateTime;
        LinearLayout linearDiary;


        DiaryViewHolder(View itemView) {
            super(itemView);
            diaryTitle = itemView.findViewById(R.id.diaryTitleTextView);
            diaryDateTime = itemView.findViewById(R.id.dateTextView);
            linearDiary = itemView.findViewById(R.id.linearDiary);
        }
    }

}

