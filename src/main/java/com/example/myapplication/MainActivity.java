package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.myapplication.lockscreen.service.OnLock_Service;

public class MainActivity extends AppCompatActivity {
    private WebView webView;
    private WebSettings webSettings;

    @Override
    protected void onCreate(Bundle savedInstancceState) {
       Intent intent = new Intent(
                getApplicationContext(), OnLock_Service.class
        );
        startService(intent);
        // Activity FullScreen
        super.onCreate(savedInstancceState);
        setContentView(R.layout.activity_main);

        webView = (WebView)findViewById(R.id.web_view);
        webView.setWebViewClient(new WebViewClient());

        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.loadUrl("https://www.100wonmall.com");
    }
}
