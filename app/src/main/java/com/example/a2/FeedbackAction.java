package com.example.a2;

import android.content.Context;

public class FeedbackAction {

    private Context mContext = null;
    private static final int SUCCESS = 0;
    private static final int FAILURE = 1;

    public FeedbackAction(Context context) {
        mContext = context;
    }

    /**
     * @return
     */
    public int sendFeedbackMessage() {
        return SUCCESS;
    }

}
