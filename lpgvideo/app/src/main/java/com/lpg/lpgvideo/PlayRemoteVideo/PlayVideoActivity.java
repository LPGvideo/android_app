package com.lpg.lpgvideo.PlayRemoteVideo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.lpg.lpgvideo.R;

public class PlayVideoActivity extends AppCompatActivity {
    private VideoView videoView;
    private View heart;
    private ContentLoadingProgressBar progressBar;
    private AnimatorSet animatorSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        Log.d("play_video", "onCreate: "+ getIntent().getStringExtra("videoUrl"));

        videoView = findViewById(R.id.videoView);
        videoView.setVideoURI(Uri.parse(getIntent().getStringExtra("videoUrl")));

        progressBar = findViewById(R.id.progressBar);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                progressBar.onDetachedFromWindow();
            }
        }

        );

        MediaController controller = new MediaController(this);
        videoView.setMediaController(controller);
        videoView.start();
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            @Override
            public void onCompletion(MediaPlayer mp)
            {
                //播放结束后的动作
                videoView.start();
            }
        });

        heart = findViewById(R.id.heart);
        heart.setAlpha(0);

        videoView.setOnClickListener(new DoubleClickListener() {
            @Override
            public void onMultiClick(View v) {
                heart.setAlpha(1f);

                //x方向
                ObjectAnimator animator2x = ObjectAnimator.ofFloat(heart,
                        "scaleX", 1, 4);
                //animator2x.setRepeatCount(1);
                animator2x.setDuration(250);
                animator2x.setRepeatMode(ObjectAnimator.RESTART);

                //y方向
                ObjectAnimator animator2y = ObjectAnimator.ofFloat(heart,
                        "scaleY", 1, 4);
                //animator2y.setRepeatCount(1);
                animator2y.setDuration(250);
                animator2y.setRepeatMode(ObjectAnimator.RESTART);

                //透明度
                ObjectAnimator animator3 = ObjectAnimator.ofFloat(heart,
                        "alpha", 1f, 0);
                //animator3.setRepeatCount(1);
                animator3.setDuration(250);
                animator3.setRepeatMode(ObjectAnimator.RESTART);

                animatorSet = new AnimatorSet();
                animatorSet.playTogether(animator2x, animator2y, animator3);
                animatorSet.start();

                heart.setAlpha(0);
            }

            @Override
            public void oneClick(View v) {
                if (videoView.isPlaying()) {
                    videoView.pause();
                } else {
                    videoView.start();;
                }
            }
        });



    }

    public abstract class DoubleClickListener implements View.OnClickListener {

        // 两次点击按钮之间的点击间隔
        private static final int MIN_CLICK_DELAY_TIME = 250;
        private long lastClickTime;

        public abstract void onMultiClick(View v);

        public abstract void  oneClick(View v);


        @Override
        public void onClick(View v) {
            long curClickTime = System.currentTimeMillis();
            if ((curClickTime - lastClickTime) <= MIN_CLICK_DELAY_TIME) {
                onMultiClick(v);
            } else {
                //TODO:单击双击冲突问题
                lastClickTime = curClickTime;
                oneClick(v);
            }
        }
    }



}