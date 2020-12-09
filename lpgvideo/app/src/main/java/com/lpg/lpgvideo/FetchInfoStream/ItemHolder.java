package com.lpg.lpgvideo.FetchInfoStream;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lpg.lpgvideo.PlayRemoteVideo.PlayVideoActivity;
import com.lpg.lpgvideo.R;
public class ItemHolder extends RecyclerView.ViewHolder {
    TextView name, author, time;
    ImageView cover;

    public ItemHolder(@NonNull View itemView) {
        super(itemView);
        name =itemView.findViewById(R.id.text_name);
        author = itemView.findViewById(R.id.text_author);
        time = itemView.findViewById(R.id.text_time);
        cover = itemView.findViewById(R.id.image_cover);

    }
}
