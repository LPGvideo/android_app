package com.lpg.lpgvideo.Camera;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Surface;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.lpg.lpgvideo.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends AppCompatActivity {

    int REQUEST_IMAGE_CAPTURE = 0;
    int REQUEST_VIDEO_CAPTURE = 0;
    int MEDIA_TYPE_IMAGE = 0;
    int MEDIA_TYPE_VIDEO = 1;
    int REQUEST_CAMERA = 1;
    private ImageView picture;
    private VideoView videoView;

    Uri photoURI;
    private static final int PERMISSION_CAMERA_REQUEST_CODE = 0x00000012;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_camera);

        videoView= findViewById(R.id.video);
        try {

            if(ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                dispatchTakePictureIntent();
            }else {
                ActivityCompat.requestPermissions(CameraActivity.this , new String[]{Manifest.permission.CAMERA},PERMISSION_CAMERA_REQUEST_CODE);
            }

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_CAMERA_REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                try {
                    dispatchTakePictureIntent();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }else {
                Toast.makeText(this,"拒绝拍照",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void dispatchTakePictureIntent() throws IOException{
        Intent intentPicture = new Intent( MediaStore.ACTION_VIDEO_CAPTURE);

        File photoFile = null;
        Uri myUri;

        photoFile = getOutputMediaFile(MEDIA_TYPE_VIDEO);

        if (photoFile != null) {

            myUri = FileProvider.getUriForFile(this, getPackageName()+".fileprovider", photoFile);
            photoURI = myUri;
            intentPicture.putExtra(MediaStore.EXTRA_OUTPUT, myUri);
            startActivityForResult(intentPicture, REQUEST_VIDEO_CAPTURE);
        }
    }

    private File getOutputMediaFile (int type){
        File mediaStorageDir = getExternalFilesDir( Environment.DIRECTORY_PICTURES);
        Log.d("com", "checkPermissionAndStartRecord");
        if (!mediaStorageDir.exists()){
            if(!mediaStorageDir.mkdirs()){
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if(type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath()+File.separator + "IMG_" + timeStamp +".jpg");
        }else if (type == MEDIA_TYPE_VIDEO){
            mediaFile = new File(mediaStorageDir.getPath()+ File.separator + "VID_" + ".mp4");
        }else {
            return null;
        }
        return mediaFile;
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        显示照片
//        if (REQUEST_IMAGE_CAPTURE == 1 ){
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            picture.setImageBitmap(imageBitmap);
//        }

//        显示视频
        if(REQUEST_VIDEO_CAPTURE == 1){
            videoView.setVideoURI(photoURI);
            videoView.start();
        }
    }

}