package com.sarriel.pass;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;


public class AboutActivity extends AppCompatActivity {

    /**
     * Initialize content of about activity.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ((WebView)findViewById(R.id.webView)).loadUrl("file:///android_asset/about.html");
    }
}
