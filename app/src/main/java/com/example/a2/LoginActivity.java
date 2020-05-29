package com.example.a2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import java.util.ArrayList;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a2.RegisterData.DBOpenHelper;
import com.example.a2.RegisterData.User;

import android.widget.CheckBox;
import android.content.SharedPreferences;

/**
 *
 */
public class LoginActivity extends AppCompatActivity {

    private DBOpenHelper mDBOpenHelper;
    private TextView mTvRegister;
    private EditText mEtUsername;
    private EditText mEtPassword;
    private Button mBtLogin;
    private Button mBtUserClear;
    private Button mBtPwdClear;
    private CheckBox mCheckRemember;
    private CheckBox mCheckAutoLogin;
    private SharedPreferences sp;

    //中介变量context
    @SuppressLint("StaticFieldLeak")
    private static LoginActivity context = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Assign the layout login activity_login to LoginActivity
        setContentView(R.layout.activity_login);

        //初始化context
        context = this;

        sp = this.getSharedPreferences("Account Data", 0);

        initView();

        mDBOpenHelper = new DBOpenHelper(this);

        restoreInfo();

        mCheckRemember.setChecked(true);

        if (sp.getBoolean("REM_ISCheck", false)) {
            mCheckRemember.setChecked(true);
            restoreInfo();
            Log.e("Remember Password", "State: " + mCheckRemember.isChecked());

            if (sp.getBoolean("AUTO_ISCHECK", false)) {
                //设置默认是自动登录状态
                mCheckAutoLogin.setChecked(true);
                Log.e("Auto-Login", "State: " + mCheckAutoLogin.isChecked());
                //跳转界面
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }
    }

    private void memInfo(String usr, String pwd, Boolean rem_isCheck, Boolean auto_isCheck) {
        SharedPreferences.Editor editor = getSharedPreferences("Account Data", 0).edit();
        editor.putString("username", usr);
        editor.putString("password", pwd);
        editor.putBoolean("REM_ISCheck", rem_isCheck);
        editor.putBoolean("AUTO_ISCHECK", auto_isCheck);
        editor.apply();
        Log.e("Checked_RP", "Account：" + usr +
                "\n" + "Password: " + pwd +
                "\n" + "Whether remember password: " + rem_isCheck +
                "\n" + "Whether auto login: " + auto_isCheck);
        editor.commit();
    }

    private void restoreInfo() {
        SharedPreferences sp = getSharedPreferences("Account Data", 0);
        mEtUsername.setText(sp.getString("username", ""));
        mEtPassword.setText(sp.getString("password", ""));
    }

    private void initView() {
        // Initial Controls
        mBtLogin = findViewById(R.id.login);
        mBtUserClear = findViewById(R.id.bt_usename_clear);
        mBtPwdClear = findViewById(R.id.bt_pwd_clear);
        mTvRegister = findViewById(R.id.tv_loginactivity_register);
        mEtUsername = findViewById(R.id.et_loginactivity_username);
        mEtPassword = findViewById(R.id.et_loginactivity_password);
        mCheckRemember = findViewById(R.id.check_remember);
        mCheckAutoLogin = findViewById(R.id.check_autoLogin);

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
                        if (mCheckRemember.isChecked()) {
                            mCheckRemember.setChecked(true);
                            String usr = mEtUsername.getText().toString();
                            String pwd = mEtPassword.getText().toString();
                            memInfo(usr, pwd, mCheckRemember.isChecked(), mCheckAutoLogin.isChecked());

                            if (sp.getBoolean("AUTO_ISCHECK", false)) {
                                //设置默认是自动登录状态
                                mCheckAutoLogin.setChecked(true);
                                //跳转界面
                                Intent intent1 = new Intent(LoginActivity.this, MainActivity.class);
                                LoginActivity.this.startActivity(intent1);
                            }
                        } else {
                            SharedPreferences.Editor et = getSharedPreferences("Account Data", 0).edit();
                            et.clear();
                            et.apply();
                        }
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

        // Set remember password checked box function.
        mCheckRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = getSharedPreferences("Account Data", 0).edit();
                if (mCheckRemember.isChecked()) {

                    Toast.makeText(getApplicationContext(), "Remember account has checked", Toast.LENGTH_SHORT).show();
                    editor.putBoolean("REM_ISCheck", true).apply();

                } else {

                    Toast.makeText(getApplicationContext(), "Remember account didn't check", Toast.LENGTH_SHORT).show();
                    editor.putBoolean("REM_ISCheck", false).commit();

                }
            }
        });

        // Set auto login checked box function.
        mCheckAutoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = getSharedPreferences("Account Data", 0).edit();
                if (mCheckAutoLogin.isChecked()) {

                    Toast.makeText(getApplicationContext(), "Auto login has checked", Toast.LENGTH_SHORT).show();
                    editor.putBoolean("AUTO_ISCHECK", true).apply();

                } else {
                    Toast.makeText(getApplicationContext(), "Auto login didn't check", Toast.LENGTH_SHORT).show();
                    editor.putBoolean("AUTO_ISCHECK", false).commit();

                }
            }
        });

        // Sign up button function
        mTvRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });

        // Clear text of Users
        mBtUserClear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mEtUsername.getText().clear();
            }
        });

        // Clear text of pwd
        mBtPwdClear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mEtPassword.getText().clear();
            }
        });
    }
}
