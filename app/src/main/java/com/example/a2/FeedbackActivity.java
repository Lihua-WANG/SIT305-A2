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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.client.Firebase;

/**
 * Build feedback activity which can seed feedback massage to firebase
 * or directly create an email to developer
 */

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
    }

    private void initView() {
        mContactEdit = findViewById(R.id.feedback_contact_edit);
        mContentEdit = findViewById(R.id.feedback_content_edit);
        mLeftBtn = findViewById(R.id.left_btn);
        mRightBtn = findViewById(R.id.email_btn);
        mSubmitBtn = findViewById(R.id.submit_button);
        Firebase.setAndroidContext(this);

        // Define data structure in firebase
        String UniqueID =
                Settings.Secure.getString(getApplicationContext().getContentResolver(),
                        Settings.Secure.ANDROID_ID);
        firebase = new Firebase("https://sit305-a2.firebaseio.com/Users" + UniqueID);

        mContentEdit.requestFocus();
        mLeftBtn.setVisibility(View.GONE);

        // Call mailbox application to send email which has defined
        // receiver and message title and some content.
        mRightBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "wanglihua@deakin.edu.au", null));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback on Go Bang Application");
                intent.putExtra(Intent.EXTRA_TEXT, "What I would like to give feedback about:");
                startActivity(intent);
            }
        });

        // Require users only can submit feedback when complete content and contact field.
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
                    mContactEdit.setError("This is an required field!");
                } else {
                    mContactEdit.setError(null);
                    mSubmitBtn.setEnabled(true);
                }

                SendFeedbackTask task = new SendFeedbackTask(FeedbackActivity.this, content, contact);
                task.execute("");
            }
        });
    }

    // When processing feedback message sending,
    // AsyncTask do background operation on background thread and update on main thread.
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
        protected Object doInBackground(Object... objects) {
            return null;
        }

        @SuppressLint("ShowToast")
        @Override
        protected void onPostExecute(Object result) {
            if (mProgDialog != null) {
                mProgDialog.dismiss();
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
