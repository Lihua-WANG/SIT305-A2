package com.example.a2;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a2.RegisterData.DBOpenHelper;
import com.example.a2.RegisterData.User;


public class LoginActivity extends AppCompatActivity {

    private DBOpenHelper mDBOpenHelper;
    private TextView mTvRegister;
    private EditText mEtUsername;
    private EditText mEtPassword;
    private Button mBtLogin;
    private Button mBtUserClear;
    private Button mBtPwdClear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        playbgmRing();
        // Assign the layout login activity_login to LoginActivity
        setContentView(R.layout.activity_login);

        initView();

        mDBOpenHelper = new DBOpenHelper(this);

        //Set Login button function.
        mBtLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String name = mEtUsername.getText().toString().trim();
                String password = mEtPassword.getText().toString().trim();
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)) {
                    ArrayList<User> data = mDBOpenHelper.getAllData();
                    boolean match = false;
                    for (int i = 0; i < data.size(); i++) {
                        User user = data.get(i);
                        if (name.equals(user.getName()) && password.equals(user.getPassword())) {
                            match = true;
                            break;
                        } else {
                            match = false;
                        }
                    }
                    if (match) {
                        Toast.makeText(getApplicationContext(), "Login successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();//Destroy this Activity
                    } else {
                        Toast.makeText(getApplicationContext(), "Username or password is incorrect\nplease re-enter", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter your username or password", Toast.LENGTH_SHORT).show();
                }

            }
        });

        mTvRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });

        mBtUserClear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mEtUsername.getText().clear();
            }
        });

        mBtPwdClear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mEtPassword.getText().clear();
            }
        });

    }

    private void initView() {
        // Initial Controls
        mBtLogin = findViewById(R.id.login);
        mBtUserClear = findViewById(R.id.bt_usename_clear);
        mBtPwdClear = findViewById(R.id.bt_pwd_clear);
        mTvRegister = findViewById(R.id.tv_loginactivity_register);
        mEtUsername = findViewById(R.id.et_loginactivity_username);
        mEtPassword = findViewById(R.id.et_loginactivity_password);
    }


//    public AssetManager assetManager;
//
//    public MediaPlayer playbgmRing() {
//        MediaPlayer mediaPlayer = null;
//        try {
//            mediaPlayer = new MediaPlayer();
//            assetManager = getAssets();
//            AssetFileDescriptor fileDescriptor = assetManager.openFd("backgroundMusic.mp3");
//            mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),fileDescriptor.getStartOffset(),
//                    fileDescriptor.getStartOffset());
//            mediaPlayer.prepare();
//            mediaPlayer.start();
//            mediaPlayer.setLooping(true);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return mediaPlayer;
//    }
}
