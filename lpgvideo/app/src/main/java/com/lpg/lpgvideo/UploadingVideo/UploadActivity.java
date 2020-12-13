package com.lpg.lpgvideo.UploadingVideo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lpg.lpgvideo.R;
import com.lpg.lpgvideo.UploadingVideo.Interface.videoService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UploadActivity extends AppCompatActivity {
    private final Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api-sjtu-camp.bytedance.com/").
            addConverterFactory(GsonConverterFactory.create()).client(client).build();
    private static final OkHttpClient client = new OkHttpClient.Builder().
            connectTimeout(60, TimeUnit.SECONDS).
            readTimeout(60, TimeUnit.SECONDS).
            writeTimeout(60, TimeUnit.SECONDS).build();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        Button postButton = findViewById(R.id.button_post);
        TextView listsText = findViewById(R.id.text_lists);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UploadActivity.this, LoadingActivity.class);
                startActivity(intent);
                MultipartBody.Part imagePart = getAssets("cover_image","cov_image.png");
                MultipartBody.Part videoPart = getAssets("video","game_video.mp4");

                videoService videoService = retrofit.create(videoService.class);
                Call<PostResult> call = videoService.postVideo("123","呜呜呜","是的",imagePart,videoPart);
                call.enqueue(new Callback<PostResult>() {
                    @Override
                    public void onResponse(Call<PostResult> call, Response<PostResult> response) {
                        LoadingActivity.setmProgress(100);
                        Log.d("main", "onResponse: 上传完毕");
                    }
                    @Override
                    public void onFailure(Call<PostResult> call, Throwable t) {
                        listsText.setText("上传失败\n");
                        t.printStackTrace();
                    }
                });
            }
        });
    }

    private MultipartBody.Part getAssets( String partKey, String localFileName) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),fileNameToByte(localFileName));
        return MultipartBody.Part.createFormData(partKey,localFileName,requestFile);
    }

    private byte[]  fileNameToByte(String filename) {
        try {
            final AssetManager assetManager = this.getAssets();
            final InputStream inputStream = assetManager.open(filename);
            final ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[40960];
            int n = 0;
            while (-1 != (n = inputStream.read(buffer))) {
                output.write(buffer, 0, n);
            }
            return output.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[1];
        }
    }
}