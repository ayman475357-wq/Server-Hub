package com.serverhub.app;

import android.app.Activity;
<<<<<<< HEAD
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.Enumeration;

public class MainActivity extends Activity {
    private static final String DEFAULT_URL = "http://127.0.0.1:8080/";
    private static final String PREFS = "server_hub";
    private static final String KEY_URL = "url";

    private EditText urlInput;
    private TextView status;
    private ProgressBar progress;
    private WebView webView;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        urlInput = findViewById(R.id.urlInput);
        status = findViewById(R.id.status);
        progress = findViewById(R.id.progress);
        webView = findViewById(R.id.webView);

        String saved = prefs.getString(KEY_URL, DEFAULT_URL);
        urlInput.setText(saved);

        setupWebView();
        updateLanIp();

        Button connect = findViewById(R.id.connect);
        Button copy = findViewById(R.id.copy);

        connect.setOnClickListener(v -> connectToServer());
        copy.setOnClickListener(v -> copyUrl());
        urlInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_DONE
                    || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                connectToServer();
                return true;
            }
            return false;
        });

        connectToServer();
    }

    private void setupWebView() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);
        settings.setLoadWithOverviewMode(false);
        settings.setUseWideViewPort(true);

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progress.setVisibility(newProgress < 100 ? ProgressBar.VISIBLE : ProgressBar.GONE);
                progress.setProgress(newProgress);
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri uri = request.getUrl();
                view.loadUrl(uri.toString());
                return true;
            }
        });
    }

    private String getUrl() {
        String value = urlInput.getText().toString().trim();
        if (value.isEmpty()) value = DEFAULT_URL;
        if (!value.startsWith("http://") && !value.startsWith("https://")) {
            value = "http://" + value;
        }
        if (!value.endsWith("/")) value += "/";
        return value;
    }

    private void connectToServer() {
        final String target = getUrl();
        urlInput.setText(target);
        prefs.edit().putString(KEY_URL, target).apply();
        status.setText("● جاري الاتصال...");
        status.setTextColor(Color.rgb(255, 193, 7));

        new Thread(() -> {
            boolean online = false;
            int code = -1;
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) new URL(target).openConnection();
                connection.setConnectTimeout(2500);
                connection.setReadTimeout(2500);
                connection.setRequestMethod("GET");
                connection.setInstanceFollowRedirects(true);
                code = connection.getResponseCode();
                online = code >= 200 && code < 500;
            } catch (Exception ignored) {
            } finally {
                if (connection != null) connection.disconnect();
            }

            final boolean result = online;
            final int responseCode = code;
            runOnUiThread(() -> {
                if (result) {
                    status.setText("● السيرفر متصل (" + responseCode + ")");
                    status.setTextColor(Color.rgb(76, 175, 80));
                    webView.loadUrl(target);
                } else {
                    status.setText("● السيرفر غير متصل");
                    status.setTextColor(Color.rgb(244, 67, 54));
                    Toast.makeText(this, "تأكد أن Server Hub يعمل في Termux", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    private void copyUrl() {
        String target = getUrl();
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newPlainText("Server Hub URL", target));
        Toast.makeText(this, "تم نسخ الرابط", Toast.LENGTH_SHORT).show();
    }

    private void updateLanIp() {
        TextView lanIp = findViewById(R.id.lanIp);
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface network = interfaces.nextElement();
                Enumeration<InetAddress> addresses = network.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    if (!address.isLoopbackAddress() && address instanceof Inet4Address) {
                        lanIp.setText("IP الشبكة: " + address.getHostAddress());
                        return;
                    }
                }
            }
        } catch (Exception ignored) {
        }
        lanIp.setText("IP الشبكة: غير متاح");
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.stopLoading();
            webView.destroy();
        }
        super.onDestroy();
    }
=======
import android.os.Bundle;
import android.widget.*;
import java.net.*;

public class MainActivity extends Activity {
    EditText url, user, pass;
    TextView status;

    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_main);
        url=findViewById(R.id.url);
        user=findViewById(R.id.user);
        pass=findViewById(R.id.pass);
        status=findViewById(R.id.status);

        findViewById(R.id.login).setOnClickListener(v -> login());
        findViewById(R.id.signup).setOnClickListener(v ->
            status.setText("واجهة إنشاء الحساب جاهزة للربط مع API السيرفر"));
    }

    void login() {
        status.setText("جارٍ الاتصال...");
        new Thread(() -> {
            try {
                URL u=new URL(url.getText().toString().trim()+"/login");
                HttpURLConnection c=(HttpURLConnection)u.openConnection();
                c.setRequestMethod("POST");
                c.setDoOutput(true);
                c.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                String x="username="+URLEncoder.encode(user.getText().toString(),"UTF-8")
                    +"&password="+URLEncoder.encode(pass.getText().toString(),"UTF-8");
                c.getOutputStream().write(x.getBytes("UTF-8"));
                int code=c.getResponseCode();
                runOnUiThread(() -> status.setText(
                    code>=200 && code<400 ? "تم الاتصال بالسيرفر" : "فشل الدخول: "+code));
            } catch(Exception e) {
                runOnUiThread(() -> status.setText("تعذر الاتصال: "+e.getMessage()));
            }
        }).start();
    }
>>>>>>> 96895eb (Server Hub Android new project)
}
