package com.genius.hrms.activity.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.genius.hrms.R;

import im.delight.android.webview.AdvancedWebView;

public class WebViewActivity extends AppCompatActivity implements AdvancedWebView.Listener{
    String imageurl;
    AdvancedWebView wbUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        initView();
    }
    private void initView(){
        imageurl=getIntent().getStringExtra("imageurl");
        wbUrl=(AdvancedWebView)findViewById(R.id.wbUrl);
        wbUrl.setListener(this, this);
        wbUrl.loadUrl(imageurl);
    }
    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        super.onResume();
        wbUrl.onResume();
        // ...
    }

    @SuppressLint("NewApi")
    @Override
    protected void onPause() {
        wbUrl.onPause();
        // ...
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        wbUrl.onDestroy();
        // ...
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        wbUrl.onActivityResult(requestCode, resultCode, intent);
        // ...
    }

    @Override
    public void onBackPressed() {
        if (!wbUrl.onBackPressed()) { return; }
        // ...
        super.onBackPressed();
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) { }

    @Override
    public void onPageFinished(String url) { }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) { }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) { }

    @Override
    public void onExternalPageRequest(String url) { }
}
