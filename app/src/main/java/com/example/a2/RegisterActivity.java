package com.example.a2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.os.Bundle;

import com.example.a2.RegisterData.Code;
import com.example.a2.RegisterData.DBOpenHelper;
import com.example.a2.UrAgreementTextView;
import com.example.a2.UrImageSpan;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private String realCode;
    private DBOpenHelper mDBOpenHelper;
    private Button mBtRegisteractivityRegister;
    private RelativeLayout mRlRegisteractivityTop;
    private ImageView mIvRegisteractivityBack;
    private LinearLayout mLlRegisteractivityBody;
    private EditText mEtRegisteractivityUsername;
    private EditText mEtRegisteractivityPassword1;
    private EditText mEtRegisteractivityPassword2;
    private EditText mEtRegisteractivityPhonecodes;
    private ImageView mIvRegisteractivityShowcode;
    private UrAgreementTextView mTvAgreement;
    Context mcontext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        initView();

        UrAgreementTextView urAgreementTextView = new UrAgreementTextView(mcontext);

        mDBOpenHelper = new DBOpenHelper(this);

        // Display the verification code as a picture
        mIvRegisteractivityShowcode.setImageBitmap(Code.getInstance().createBitmap());
        realCode = Code.getInstance().getCode().toLowerCase();


    }

    private void initView() {
        mBtRegisteractivityRegister = findViewById(R.id.bt_registeractivity_register);
        mRlRegisteractivityTop = findViewById(R.id.rl_registeractivity_top);
        mIvRegisteractivityBack = findViewById(R.id.iv_registeractivity_back);
        mLlRegisteractivityBody = findViewById(R.id.ll_registeractivity_body);
        mEtRegisteractivityUsername = findViewById(R.id.et_registeractivity_username);
        mEtRegisteractivityPassword1 = findViewById(R.id.et_registeractivity_password1);
        mEtRegisteractivityPassword2 = findViewById(R.id.et_registeractivity_password2);
        mEtRegisteractivityPhonecodes = findViewById(R.id.et_registeractivity_phoneCodes);
        mIvRegisteractivityShowcode = findViewById(R.id.iv_registeractivity_showCode);
        mTvAgreement = findViewById(R.id.tv_agreement);

        /**
         * There are three places you can click on the registration page
         * Back arrow at top, refresh verification code picture, register button
         */
        mIvRegisteractivityBack.setOnClickListener(this);
        mIvRegisteractivityShowcode.setOnClickListener(this);
        mBtRegisteractivityRegister.setOnClickListener(this);
        mTvAgreement.setAgreementClickListener((UrAgreementTextView.OnAgreementClickListener) this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_registeractivity_back: //Back to login page
                Intent intent1 = new Intent(this, LoginActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.iv_registeractivity_showCode:    //Change the generation of random verification codes
                mIvRegisteractivityShowcode.setImageBitmap(Code.getInstance().createBitmap());
                realCode = Code.getInstance().getCode().toLowerCase();
                break;
            case R.id.tv_agreement:
                if ()
                    case R.id.bt_registeractivity_register:    //Register button
                // Get the username, password, and verification code entered by the user
                String username = mEtRegisteractivityUsername.getText().toString().trim();
                String password = mEtRegisteractivityPassword2.getText().toString().trim();
                String phoneCode = mEtRegisteractivityPhonecodes.getText().toString().toLowerCase();
                // Registration verification
                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(phoneCode)) {
                    if (phoneCode.equals(realCode)) {
                        //Add the username and password to the database
                        mDBOpenHelper.add(username, password);
                        Intent intent2 = new Intent(this, MainActivity.class);
                        startActivity(intent2);
                        finish();
                        Toast.makeText(this, "Verification passed, registration succeeded", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Verification code error, registration failed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Incomplete information, registration failed", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
