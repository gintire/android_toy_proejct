package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

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
        webView.setWebChromeClient(new WebChromeClient());

        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.loadUrl("https://www.100wonmall.com");
    }
}
