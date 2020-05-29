package com.example.a2.RegisterPolicy;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.example.a2.R;

/**
 * Users Terms Policy content
 * viewed from html page.
 */
public class TermsActivity extends Activity {

    private FrameLayout web_view_container;
    private WebView web_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        initView();
    }

    private void initView() {
        web_view_container = findViewById(R.id.web_view_container);
        web_view = new WebView(getApplicationContext());
        // Set web-view content layout
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        web_view.setLayoutParams(params);
        // Set web-view content
        web_view.setWebViewClient(new WebViewClient());
        // Dynamically add WebView，
        // Solve the reference to WebView holding Activity's Context object in xml,
        // which result in memory leak
        web_view_container.addView(web_view);
        web_view.loadUrl("file:///android_asset/user_agreement.html");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        web_view_container.removeAllViews();
        web_view.destroy();
    }
}
