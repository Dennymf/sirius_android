package edu.sirius.examples.android.lesson3;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by a.halaidzhy on 05.01.2018.
 */

public class WebViewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        Intent startedIntent = getIntent();
        if (startedIntent.getAction().equals(Intent.ACTION_VIEW) && startedIntent.getData() != null) {
            Uri uri = startedIntent.getData();
            WebView view = findViewById(R.id.webView);
            WebSettings webSettings = view.getSettings();
            webSettings.setJavaScriptEnabled(true);
            view.setWebViewClient(new WebViewClient());
            view.loadUrl(uri.toString());
        }
    }
}
