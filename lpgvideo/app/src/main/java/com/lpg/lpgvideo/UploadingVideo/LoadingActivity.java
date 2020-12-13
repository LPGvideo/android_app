package com.lpg.lpgvideo.UploadingVideo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lpg.lpgvideo.R;

public class LoadingActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    static private int mProgress=0;
    static public void setmProgress(int p) {
        mProgress = p;
    }

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
            progressBar=findViewById(R.id.progressBar);
            mHandler=new Handler(){
                @Override
                public void handleMessage(@NonNull Message msg) {
                    if(msg.what==0x111){
                        progressBar.setProgress(mProgress);
                    }else{
                        Toast.makeText(LoadingActivity.this,"上传成功",Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        mProgress=0;
                        finish();

                    }
                }
            };
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while(true){
                        mProgress=doWork();
                        Message m=new Message();
                        if(mProgress<100){
                            m.what=0x111;
                            mHandler.sendMessage(m);
                        }else{
                            m.what=0x110;
                            mHandler.sendMessage(m);
                            break;
                        }

                    }

                }
                private int doWork(){
                    if(mProgress==100){
                        return mProgress;
                    }
                    while (mProgress>90&&mProgress<100){

                    }
                    mProgress+=Math.random()*10;
                    try {
                        Thread.sleep(700);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return mProgress;
                }
            }).start();
        }

    }
