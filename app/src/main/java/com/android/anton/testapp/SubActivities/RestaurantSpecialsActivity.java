package com.android.anton.testapp.SubActivities;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.anton.testapp.R;

public class RestaurantSpecialsActivity extends AppCompatActivity {

    private ProgressDialog dialog;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_specials);

        String appId = getIntent().getStringExtra("appId");

        webView = (WebView) findViewById(R.id.activity_restaurant_specials_webview);
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

        webView.loadUrl("https://api.appmc2.net/offers.aspx?appid=" + appId);
        dialog = ProgressDialog.show(this, "", "Loading...", true);
    }

    public void onBack(View v) {
        finish();
    }
}
