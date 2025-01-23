package com.sp.doactivitysh;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Network extends AppCompatActivity {

    WebView myweb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_network);

        myweb = findViewById(R.id.myweb);

        myweb.getSettings().setJavaScriptEnabled(true);

        myweb.loadUrl("https://www.google.co.uk/");

    }
}