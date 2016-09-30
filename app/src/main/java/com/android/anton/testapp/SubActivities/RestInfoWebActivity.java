package com.android.anton.testapp.SubActivities;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.anton.testapp.R;

public class RestInfoWebActivity extends AppCompatActivity {

    private ProgressDialog dialog;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_rest_info_web);
        String website = getIntent().getStringExtra("website");

        Log.d("RestInfo", website);

        webView = (WebView) findViewById(R.id.activity_rest_info_web_webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setHorizontalScrollBarEnabled(true);
        webView.setVerticalScrollBarEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setUseWideViewPort(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String urlNewString) {return true;}

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {}

            @Override
            public void onPageFinished(WebView view, String url) {
                dialog.dismiss();
            }
        });

        webView.loadUrl(website);
        dialog = ProgressDialog.show(this, "", "Loading...", true);

    }

    public void onBack(View v) {
        finish();
    }
}
