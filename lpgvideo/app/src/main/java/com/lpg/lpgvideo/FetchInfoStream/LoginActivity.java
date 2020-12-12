package com.lpg.lpgvideo.FetchInfoStream;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lpg.lpgvideo.MainActivity;
import com.lpg.lpgvideo.R;

public class LoginActivity extends AppCompatActivity {

    private static String studentId;
    private static String userName;

    public static void setStudentId(String id) {studentId = id;}
    public static void setUserName(String name){userName = name;}
    public static String getStudentId(){return studentId;}
    public static String getUserName(){return userName;}

    private ImageView mLoginButton;
    private EditText mStuIdText, mUsernameText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        String idRegex = "[0-9]*", usernameRegex = "[A-Za-z0-9]*";
        mLoginButton = findViewById(R.id.button_login);
        mStuIdText = findViewById(R.id.login_edit_stuid);
        mUsernameText = findViewById(R.id.login_edit_username);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mStuIdText.getText().toString().matches(idRegex) || "".equals(mStuIdText.getText().toString())) {
                    Toast.makeText(LoginActivity.this,"请输入正确学号",Toast.LENGTH_SHORT).show();
                } else if (!mUsernameText.getText().toString().matches(usernameRegex) || "".equals(mUsernameText.getText().toString())) {
                    Toast.makeText(LoginActivity.this,"请输入由字母和数字构成的用户名",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this,"登陆成功",Toast.LENGTH_SHORT).show();
                    setStudentId(mStuIdText.getText().toString());
                    setUserName(mUsernameText.getText().toString());
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}