package ru.mail.park.advandroid.helloword.lection2;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        Intent startedIntent = getIntent();
        if (startedIntent.getAction().equals(Intent.ACTION_VIEW) && startedIntent.getData() != null) {
            Uri uri = startedIntent.getData();
            WebView view = (WebView)findViewById(R.id.webview);
            WebSettings webSettings = view.getSettings();
            webSettings.setJavaScriptEnabled(true);
            view.setWebViewClient(new WebViewClient());

            view.loadUrl(uri.toString());
        }
    }
}
