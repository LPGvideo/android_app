package com.lpg.lpgvideo.TakeVideo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.os.EnvironmentCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.lpg.lpgvideo.R;
import com.lpg.lpgvideo.Upload.UploadActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CameraActivity extends AppCompatActivity {

    private Button ivCamera;
    private ImageView ivPhoto;

    // 拍照的requestCode
    private static final int CAMERA_REQUEST_CODE = 0x00000010;
    // 申请相机权限的requestCode
    private static final int PERMISSION_CAMERA_REQUEST_CODE = 0x00000012;

//     用于保存拍照图片的uri

    private Uri mCameraUri;

//    用于保存图片的文件路径，Android 10以下使用图片路径访问图片
    private String mCameraImagePath;


    private boolean isAndroidQ = Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_takevideo);
        ivCamera = findViewById(R.id.camera);
        ivPhoto = findViewById(R.id.ivPhoto);

        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissionAndCamera();
            }
        });
    }

    /**
     * 检查权限并拍照。
     * 调用相机前先检查权限。
     */
    private void checkPermissionAndCamera() {
        int hasCameraPermission = ContextCompat.checkSelfPermission(getApplication(),
                Manifest.permission.CAMERA);
        if (hasCameraPermission == PackageManager.PERMISSION_GRANTED) {
            //有权限，调起相机拍照。
            openCamera();
        } else {
            //没有权限，申请权限。
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    PERMISSION_CAMERA_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            Intent intent = new Intent(CameraActivity.this, UploadActivity.class);
            intent.putExtra("path",mCameraImagePath);
            startActivity(intent);
//            if (resultCode == RESULT_OK) {
//                if (isAndroidQ) {
//                    // Android 10 使用图片uri加载
//                    ivPhoto.setImageURI(mCameraUri);
//                } else {
//                    // 使用图片路径加载
//                    ivPhoto.setImageBitmap( BitmapFactory.decodeFile(mCameraImagePath));
//                }
//            } else {
//                Toast.makeText(this, "取消", Toast.LENGTH_LONG).show();
//            }
        }
    }

    /**
     * 处理权限申请的回调。
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //允许权限，有调起相机拍照。
                openCamera();
            } else {
                //拒绝权限，弹出提示框。
                Toast.makeText(this, "拍照权限被拒绝", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * 调起相机拍照
     */
    private void openCamera() {
        Intent captureIntent = new Intent( MediaStore.ACTION_VIDEO_CAPTURE);
        // 判断是否有相机
        if (captureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            Uri photoUri = null;

//            if (isAndroidQ) {
//                // 适配android 10
//                photoUri = createImageUri();
//            } else {
                try {
                    photoFile = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            Log.d( "file", "openCamera: "+ photoFile.getAbsolutePath() );
                if (photoFile != null) {
                    mCameraImagePath = photoFile.getAbsolutePath();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        //适配Android 7.0文件权限，通过FileProvider创建一个content类型的Uri
                        photoUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", photoFile);
                        grantUriPermission(getPackageName(),photoUri,Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        captureIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    } else {
                        photoUri = Uri.fromFile(photoFile);
                    }
                }
//            }

            mCameraUri = photoUri;
            if (photoUri != null) {
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                captureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                captureIntent.putExtra( MediaStore.EXTRA_DURATION_LIMIT,15 );
                startActivityForResult(captureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    /**
     * 创建图片地址uri,用于保存拍照后的照片 Android 10以后使用这种方法
     *
     * @return 图片的uri
     */
    private Uri createImageUri() {
        String status = Environment.getExternalStorageState();
        // 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        } else {
            return getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, new ContentValues());
        }
    }

    /**
     * 创建保存图片的文件
     *
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        //String imageName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageName = "lpg";
        File storageDir = getExternalFilesDir("/lalala/");
        if (!storageDir.exists()) {
            storageDir.mkdir();
        }
        File tempFile = new File(storageDir, "VID_" +imageName+ ".mp4");
        if (!Environment.MEDIA_MOUNTED.equals( EnvironmentCompat.getStorageState(tempFile))) {
            return null;
        }
        Log.d( "File", "createImageFile: ok" +tempFile.getAbsolutePath());
        return tempFile;
    }
}
