package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.myapplication.lockscreen.service.OnLock_Service;
import com.example.myapplication.lockscreen.service.Undead_Service;
import com.example.myapplication.webview.util.BackPressCloseHandler;

import java.net.URL;

public class MainActivity extends AppCompatActivity {
    public static WebView webView;
    private WebSettings webSettings;
    private Intent serviceIntent;
    private Intent foregroundServiceIntent;
    private BackPressCloseHandler backPressCloseHandler = new BackPressCloseHandler(this);
    @Override
    protected void onCreate(Bundle savedInstancceState) {
        // Activity FullScreen
        super.onCreate(savedInstancceState);
        setContentView(R.layout.activity_main);

        // LockScreen Service
        serviceIntent = new Intent(
                getApplicationContext(), OnLock_Service.class
        );
        startService(serviceIntent);

        // Undead Service
        if(Undead_Service.serviceIntent==null) {
            foregroundServiceIntent = new Intent(this, Undead_Service.class);
            startService(foregroundServiceIntent);
            Toast.makeText(getApplicationContext(), "start undead service", Toast.LENGTH_LONG).show();
       } else {
           foregroundServiceIntent= Undead_Service.serviceIntent;
           Toast.makeText(getApplicationContext(), "Already", Toast.LENGTH_LONG).show();
       }

        // Webview
        // xml 자바코드 연결
        webView = (WebView) findViewById(R.id.web_view);
        // 자바 스크립트 허용
        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportMultipleWindows(true);

        //webView.setWebViewClient(new WebViewClient());
        // 웹뷰에 크롬 사용 허용 // 이부분이 없으면 크롬에서 alert가 뜨지 않는다.
        webView.setWebChromeClient(new WebViewChromeClientClass());
        webView.setWebViewClient(new WebViewClientClass());

        // 화면 비율 관련
        // wide viewport를 사용하도록 설정
        webSettings.setUseWideViewPort(true);
        // 컨텐츠가 웹뷰보다 클 경우 스크린 크기에 맞게 조절
        webSettings.setLoadWithOverviewMode(true);

        // 웹뷰 멀티 터치 가능하게 (줌기능)
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(false);

        webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        webView.loadUrl("https://100wonmall.com");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                backPressCloseHandler.onBackPressed();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /*@Override
    public void onBackPressed() {
        super.onBackPressed();
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            backPressCloseHandler.onBackPressed();
        }
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(foregroundServiceIntent!=null) {
            stopService(foregroundServiceIntent);
            foregroundServiceIntent= null;
        }
        webView.destroy();
        webView = null;
    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d("check URL", url);
            view.loadUrl(url);
            return true;
        }
    }

    private class WebViewChromeClientClass extends WebChromeClient {
        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
            WebView newWebView = new WebView(MainActivity.this);
            WebSettings webSettings = newWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);

            final Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(newWebView);
            dialog.show();

            newWebView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onCloseWindow(WebView window) {
                    dialog.dismiss();
                }
            });

            ((WebView.WebViewTransport)resultMsg.obj).setWebView(newWebView);
            resultMsg.sendToTarget();
            return true;
        }
    }
}

