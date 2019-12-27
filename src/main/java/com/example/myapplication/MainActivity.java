package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsCallback;
import androidx.browser.customtabs.CustomTabsClient;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.browser.customtabs.CustomTabsServiceConnection;
import androidx.browser.customtabs.CustomTabsSession;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.lockscreen.service.OnLock_Service;
import com.example.myapplication.lockscreen.service.Undead_Service;
import com.example.myapplication.webview.util.BackPressCloseHandler;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.igaworks.adpopcorn.IgawAdpopcorn;
import com.igaworks.adpopcorn.IgawAdpopcornExtension;

import org.jetbrains.annotations.NotNull;

import java.net.URL;

public class MainActivity extends AppCompatActivity {
    public WebView webView;
    private WebSettings webSettings;
    private Intent serviceIntent;
    private Intent foregroundServiceIntent;
    private Button retryButton;


    // RewardAd
    public static RewardedAd rewardedAd;
    public static InterstitialAd mInterstitialAd;



    /*private CustomTabsServiceConnection tabConnection = new CustomTabsServiceConnection() {

        @Override
        public void onCustomTabsServiceConnected(ComponentName componentName, final CustomTabsClient customTabsClient) {
            CustomTabsSession session = customTabsClient.newSession(new CustomTabsCallback(){
                @Override
                public void onNavigationEvent(int navigationEvent, Bundle extras) {
                    super.onNavigationEvent(navigationEvent, extras);
                    switch (navigationEvent) {
                        case CustomTabsCallback.NAVIGATION_STARTED:
                            Log.e("psbs", "NAVIGATION_STARTED");
                            break;
                        case CustomTabsCallback.NAVIGATION_FINISHED:
                            Log.e("psbs", "NAVIGATION_FINISHED");
                            break;
                        case CustomTabsCallback.NAVIGATION_FAILED:
                            Log.e("psbs", "NAVIGATION_FAILED");
                            break;
                        case CustomTabsCallback.NAVIGATION_ABORTED:
                            Log.e("psbs", "NAVIGATION_ABORTED");
                            break;
                    }
                }
            });

            CustomTabsIntent.Builder customTabsIntentBuilder =
                    new CustomTabsIntent.Builder();

            *//* Custom Menu
            String menuItemTitle = "my menu";
            Intent actionIntent = new Intent(
                    getApplicationContext(), MainActivity.class);
            PendingIntent menuItemPendingIntent = PendingIntent.getActivity(
                    getApplicationContext(), 1, actionIntent, 0);

            customTabsIntentBuilder.addMenuItem(menuItemTitle, menuItemPendingIntent);

             *//*
            // Custom header color
            customTabsIntentBuilder.setToolbarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));

            // To show the title of the webpage do
            customTabsIntentBuilder.setShowTitle(true);

            //To add animations on the cusom chrome tab launch do
            customTabsIntentBuilder.setStartAnimations(getApplicationContext(), android.R.anim.fade_in, android.R.anim.fade_out);
            customTabsIntentBuilder.setExitAnimations(getApplicationContext(), android.R.anim.fade_in, android.R.anim.fade_out);

            // Adding a shrare action
            customTabsIntentBuilder.addDefaultShareMenuItem();
            // Custom the close Button
            *//*customTabsIntentBuilder.setCloseButtonIcon(BitmapFactory.decodeResource(
                    getResources(), R.drawable.ic_arrow_back
            ));*//*
            customTabsIntentBuilder.setInstantAppsEnabled(true);
            CustomTabsIntent customTabsIntent = customTabsIntentBuilder.build();
            customTabsIntent.intent.setPackage("com.android.chrome");
            customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            customTabsIntent.launchUrl(getApplicationContext(), Uri.parse("https://100wonmall.com"));
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };*/


    @Override
    protected void onCreate(Bundle savedInstancceState) {
        // Activity FullScreen
        super.onCreate(savedInstancceState);
        setContentView(R.layout.activity_main);
        IgawAdpopcorn.checkRequiredPermission(this);
        IgawAdpopcornExtension.useFlagShowWhenLocked(getBaseContext(), true);
        // LockScreen Service
        serviceIntent = new Intent(
                getApplicationContext(), OnLock_Service.class
        );
       // startService(serviceIntent);

        // Undead Service
        if(Undead_Service.serviceIntent==null) {
            foregroundServiceIntent = new Intent(this, Undead_Service.class);
            startService(foregroundServiceIntent);
            Toast.makeText(getApplicationContext(), "start undead service", Toast.LENGTH_LONG).show();
       } else {
           foregroundServiceIntent= Undead_Service.serviceIntent;
           Toast.makeText(getApplicationContext(), "Already", Toast.LENGTH_LONG).show();
       }

        // Opening a Chrome Custom Tab
       /* String url = "https://100wonmall.com";
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(url));

        builder.setToolbarColor(Color.BLUE);*/


        // Chrome Custom Tab
       /* CustomTabsClient.bindCustomTabsService(
                this,
                "com.android.chrome",
                tabConnection);*/

        // Webview
        // xml 자바코드 연결
       /* webView = (WebView) findViewById(R.id.web_view);
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
        webView.loadUrl("https://100wonmall.com");*/
    }

   /* @Override
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
    }*/

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
        //unbindService(tabConnection);
        /*webView.destroy();
        webView = null;*/
    }


    private void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and restart the game.
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            //Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
            //startGame();
        }
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

