package com.genius.payhrms.activity.activity;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import android.os.Build;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.genius.payhrms.R;

import im.delight.android.webview.AdvancedWebView;

public class WebViewActivity extends AppCompatActivity implements AdvancedWebView.Listener{
    private static final String TAG = "WebViewActivity";
    String imageurl;
    AdvancedWebView wbUrl;
    private static final int CLICK_ON_WEBVIEW = 1;
    private static final int CLICK_ON_URL = 2;
    WebView printWeb;
    PrintJob printJob;

    // a boolean to check the status of printing
    boolean printBtnPressed = false;
    TextView tvDownload;
    String month,year;
    LinearLayout lnDownload;
    String flag;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        initView();
    }
    private void initView(){
        flag = getIntent().getStringExtra("flag");
        tvDownload = (TextView) findViewById(R.id.tvDownload);
        String text="Download " +flag+" in PDF";
        tvDownload.setText(text);
        lnDownload=(LinearLayout)findViewById(R.id.lnDownload);
        month=getIntent().getStringExtra("month");
        year=getIntent().getStringExtra("year");
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        imageurl=getIntent().getStringExtra("imageurl");
        Log.d(TAG, "initView: imageurl: "+imageurl);
        wbUrl=(AdvancedWebView)findViewById(R.id.wbUrl);
        wbUrl.setListener(this, this);
        wbUrl.loadUrl(imageurl);

        wbUrl.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {

                progressDialog.dismiss();
                printWeb=wbUrl;
            }
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error){
                //Your code to do
                view.loadUrl(imageurl);
                progressDialog.dismiss();
            }
        });

        lnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (printWeb != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        // Calling createWebPrintJob()
                        PrintTheWebPage(printWeb);
                    } else {
                        // Showing Toast message to user
                        Toast.makeText(WebViewActivity.this, "Not available for device below Android LOLLIPOP", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Showing Toast message to user
                    Toast.makeText(WebViewActivity.this, "WebPage not fully loaded", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        super.onResume();
        wbUrl.onResume();
        if (printJob != null && printBtnPressed) {
            if (printJob.isCompleted()) {
                // Showing Toast Message
                Toast.makeText(this, flag+" has been downloaded", Toast.LENGTH_SHORT).show();
            } else if (printJob.isStarted()) {
                // Showing Toast Message
                Toast.makeText(this, "isStarted", Toast.LENGTH_SHORT).show();
            } else if (printJob.isBlocked()) {
                // Showing Toast Message
                Toast.makeText(this, "isBlocked", Toast.LENGTH_SHORT).show();
            } else if (printJob.isCancelled()) {
                // Showing Toast Message
                Toast.makeText(this, "isCancelled", Toast.LENGTH_SHORT).show();
            } else if (printJob.isFailed()) {
                // Showing Toast Message
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
            } else if (printJob.isQueued()) {
                // Showing Toast Message
                Toast.makeText(this, "isQueued", Toast.LENGTH_SHORT).show();
            }
            // set printBtnPressed false
            printBtnPressed = false;
        }
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void PrintTheWebPage(WebView webView) {

        // set printBtnPressed true
        printBtnPressed = true;

        // Creating  PrintManager instance
        PrintManager printManager = (PrintManager) this
                .getSystemService(Context.PRINT_SERVICE);

        // setting the name of job
        String jobName = flag+"-"+month+"-"+year;

        // Creating  PrintDocumentAdapter instance
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter(jobName);

        // Create a print job with name and adapter instance
        assert printManager != null;
        printJob = printManager.print(jobName, printAdapter,
                new PrintAttributes.Builder().build());
    }





}
