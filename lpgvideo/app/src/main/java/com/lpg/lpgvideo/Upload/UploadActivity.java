package com.lpg.lpgvideo.Upload;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;

import com.lpg.lpgvideo.FetchInfoStream.LoginActivity;
import com.lpg.lpgvideo.R;

import android.content.Intent;
import android.content.res.AssetManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lpg.lpgvideo.Upload.Interface.videoService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
    private  String mPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPath  = getIntent().getStringExtra("path");
        setContentView(R.layout.activity_upload);
        Button postButton = findViewById(R.id.button_post);
        TextView listsText = findViewById(R.id.text_lists);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UploadActivity.this, LoadingActivity.class);
                startActivity(intent);
                MultipartBody.Part imagePart = getAssets2();
                MultipartBody.Part videoPart = getAssets1("video");

                videoService videoService = retrofit.create(videoService.class);
                Call<PostResult> call = videoService.postVideo(LoginActivity.getStudentId(),LoginActivity.getUserName(),"777",imagePart,videoPart);
                call.enqueue(new Callback<PostResult>() {
                    @Override
                    public void onResponse(Call<PostResult> call, Response<PostResult> response) {
                        LoadingActivity.setmProgress(100);
                        if (response.body().isSuccessful()) {
                            Log.d("main", "onResponse: 上传完毕");
                        } else {
                            Log.d("main", "onResponse: 失败");
                        }
                    }
                    @Override
                    public void onFailure(Call<PostResult> call, Throwable t) {
                        listsText.setText("上传失败\n");
                        Log.d("main", "onResponse: 上传失败");
                        t.printStackTrace();
                    }
                });
            }
        });
    }

    private MultipartBody.Part getAssets1( String partKey) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),fileNameToByte1(partKey));
        return MultipartBody.Part.createFormData(partKey,mPath,requestFile);
    }

    private byte[]  fileNameToByte1(String key) {
        try {
            File file= new File(mPath);
            final InputStream inputStream = new FileInputStream(file);
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


    private MultipartBody.Part getAssets2() {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),fileNameToByte2("cover_image.png"));
        return MultipartBody.Part.createFormData("cover_image", "cover_image.png",requestFile);
    }

    private byte[]  fileNameToByte2(String filename) {

//            final AssetManager assetManager = this.getAssets();
//            final InputStream inputStream = assetManager.open(filename);
//            final ByteArrayOutputStream output = new ByteArrayOutputStream();
//            byte[] buffer = new byte[40960];
//            int n = 0;
//            while (-1 != (n = inputStream.read(buffer))) {
//                output.write(buffer, 0, n);
//            }
            final ByteArrayOutputStream output = new ByteArrayOutputStream();
            Bitmap bitmap = null;
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(mPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime(-1);
            mediaMetadataRetriever.release();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,output);
            return output.toByteArray();
    }
}