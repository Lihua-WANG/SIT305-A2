package com.example.a2;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.client.Firebase;

public class FeedbackActivity extends AppCompatActivity {

    private EditText mContactEdit = null;
    private EditText mContentEdit = null;
    private ImageView mLeftBtn = null;
    private ImageView mRightBtn = null;
    private Button mSubmitBtn = null;
    private Firebase firebase = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        initView();

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.RESULT_UNCHANGED_SHOWN,
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void initView() {
        mContactEdit = findViewById(R.id.feedback_contact_edit);
        mContentEdit = findViewById(R.id.feedback_content_edit);
        mLeftBtn = findViewById(R.id.left_btn);
        mRightBtn = findViewById(R.id.email_btn);
        mSubmitBtn = findViewById(R.id.submit_button);
        Firebase.setAndroidContext(this);

        String UniqueID =
                Settings.Secure.getString(getApplicationContext().getContentResolver(),
                        Settings.Secure.ANDROID_ID);
        firebase = new Firebase("https://sit305-a2.firebaseio.com/Users" + UniqueID);

        mContentEdit.requestFocus();
        mLeftBtn.setVisibility(View.GONE);
        mRightBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "wanglihua@deakin.edu.au", null));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback on Go Bang Application");
                intent.putExtra(Intent.EXTRA_TEXT, "What I would like to give feedback about:");
                startActivity(intent);
            }
        });

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                final String content = mContentEdit.getText().toString().trim();
                final String contact = mContactEdit.getText().toString().trim();

                Firebase child_content = firebase.child("Feedback Content");
                child_content.setValue(content);
                if (content.isEmpty()) {
                    Toast.makeText(FeedbackActivity.this, R.string.request_content, Toast.LENGTH_SHORT).show();
                    mContentEdit.setError("This is an required field!");
                } else {
                    mContentEdit.setError(null);
                    mSubmitBtn.setEnabled(true);
                }

                Firebase child_contact = firebase.child("Feedback Contact");
                child_contact.setValue(contact);
                if (contact.isEmpty()) {
                    Toast.makeText(FeedbackActivity.this, R.string.request_contact, Toast.LENGTH_SHORT).show();
                    mContentEdit.setError("This is an required field!");
                } else {
                    mContentEdit.setError(null);
                    mSubmitBtn.setEnabled(true);
                }

                SendFeedbackTask task = new SendFeedbackTask(FeedbackActivity.this, content, contact);
                task.execute("");
            }
        });

    }

    @SuppressLint("StaticFieldLeak")
    private class SendFeedbackTask extends AsyncTask<Object, Object, Object> {

        private Context mContext = null;
        private String mContact = "";
        private String mContent = "";
        private ProgressDialog mProgDialog = null;

        public SendFeedbackTask(Context context, String content, String contact) {
            mContext = context;
            mContent = content;
            mContact = contact;
        }

        @Override
        protected Object doInBackground(Object... arg0) {
            return new FeedbackAction(mContext)
                    .sendFeedbackMessage();
        }

        @SuppressLint("ShowToast")
        @Override
        protected void onPostExecute(Object result) {
            if (mProgDialog != null) {
                mProgDialog.dismiss();
            }
            int resultCode = (Integer) result;
            if (resultCode == 0) {
                Toast.makeText(FeedbackActivity.this,
                        R.string.feedback_success, Toast.LENGTH_SHORT);
                //FeedbackActivity.this.finish();
            } else {
                Toast.makeText(FeedbackActivity.this, R.string.feedback_failed,
                        Toast.LENGTH_SHORT);
            }
        }

        @Override
        protected void onPreExecute() {
            mProgDialog = new ProgressDialog(FeedbackActivity.this);
            mProgDialog.setMessage(FeedbackActivity.this
                    .getString(R.string.waiting));
            mProgDialog.setCancelable(false);
            mProgDialog.show();
        }

    }
}
