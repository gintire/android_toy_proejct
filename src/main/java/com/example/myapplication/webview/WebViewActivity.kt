package com.example.myapplication.webview

import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.MainActivity
import com.example.myapplication.webview.util.BackPressCloseHandler
import com.example.myapplication.R
import java.net.URL

class WebViewActivity : AppCompatActivity() {
    var webView: WebView? = null
    private var webSettings: WebSettings? = null
    val backpressCloseHandler = BackPressCloseHandler(this);

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Webview
        //webView = findViewById<View>(R.id.web_view) as WebView
        webView!!.webViewClient = WebViewClient()

        webSettings = webView!!.settings
        webSettings!!.setJavaScriptEnabled(true)

        webView!!.loadUrl("https://100wonmall.com")
    }

    override fun onBackPressed() {
        if(webView!!.canGoBack()){
            webView?.goBack();
        }else{
            backpressCloseHandler.onBackPressed();
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        webView!!.destroy()
        webView = null
    }
}