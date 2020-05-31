package com.example.a2.RegisterPolicy;

import android.app.Dialog;
import android.content.Context;

import com.example.a2.R;

/**
 * Dialog pop-up before entering sign up interface.
 */

public class PrivacyDialog extends Dialog {

    public PrivacyDialog(Context context) {
        super(context, R.style.PrivacyThemeDialog);

        setContentView(R.layout.dialog_privacy);

        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }
}
