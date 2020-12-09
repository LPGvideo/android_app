package com.lpg.lpgvideo.PlayRemoteVideo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.lpg.lpgvideo.R;

public class PlayVideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        Log.d("play_video", "onCreate: "+ getIntent().getStringExtra("videoUrl"));
    }
}