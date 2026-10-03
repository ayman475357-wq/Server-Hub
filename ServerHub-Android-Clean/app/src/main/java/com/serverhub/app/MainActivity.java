package com.serverhub.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.util.Locale;

public class MainActivity extends Activity {
    private static final String PREFS = "server_hub";
    private static final String KEY_URL = "server_url";
    private WebView web;
    private ProgressBar progress;
    private SharedPreferences prefs;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_main);
        prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        web = findViewById(R.id.web);
        progress = findViewById(R.id.progress);

        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setDatabaseEnabled(true);
        s.setAllowFileAccess(true);
        s.setMediaPlaybackRequiresUserGesture(false);
        s.setBuiltInZoomControls(false);
        s.setDisplayZoomControls(false);
        s.setLoadWithOverviewMode(false);
        s.setUseWideViewPort(false);
        s.setUserAgentString(s.getUserAgentString() + " ServerHubAndroid/1.0");

        web.setBackgroundColor(Color.rgb(11,15,20));
        web.setWebViewClient(new WebViewClient());
        web.setWebChromeClient(new WebChromeClient() {
            @Override public void onProgressChanged(WebView v, int p) {
                progress.setVisibility(p < 100 ? View.VISIBLE : View.GONE);
                progress.setProgress(p);
            }
        });

        findViewById(R.id.settings).setOnClickListener(v -> showServerDialog());
        loadHome();
    }

    private String baseUrl() {
        String u = prefs.getString(KEY_URL, "http://127.0.0.1:8080").trim();
        if (u.isEmpty()) u = "http://127.0.0.1:8080";
        while (u.endsWith("/")) u = u.substring(0, u.length() - 1);
        return u;
    }

    private void loadHome() {
        String url = baseUrl();
        if (!url.toLowerCase(Locale.US).startsWith("http://") && !url.toLowerCase(Locale.US).startsWith("https://")) {
            url = "http://" + url;
        }
        web.loadUrl(url + "/client");
    }

    private void showServerDialog() {
        final EditText input = new EditText(this);
        input.setSingleLine(true);
        input.setText(baseUrl());
        input.setHint("http://127.0.0.1:8080");
        input.setSelectAllOnFocus(true);

        new AlertDialog.Builder(this)
            .setTitle("عنوان Server Hub")
            .setMessage("إذا كان Termux على نفس الهاتف استخدم 127.0.0.1:8080. وإذا كان السيرفر على جهاز آخر استخدم عنوان LAN.")
            .setView(input)
            .setNegativeButton("إلغاء", null)
            .setPositiveButton("حفظ واتصال", (d, which) -> {
                String value = input.getText().toString().trim();
                if (value.isEmpty()) {
                    Toast.makeText(this, "اكتب عنوان السيرفر", Toast.LENGTH_SHORT).show();
                    return;
                }
                prefs.edit().putString(KEY_URL, value).apply();
                loadHome();
            }).show();
    }

    @Override public void onBackPressed() {
        if (web.canGoBack()) web.goBack(); else super.onBackPressed();
    }
}
