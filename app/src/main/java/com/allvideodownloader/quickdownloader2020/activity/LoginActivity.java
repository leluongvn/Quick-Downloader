package com.allvideodownloader.quickdownloader2020.activity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.allvideodownloader.quickdownloader2020.R;
import com.allvideodownloader.quickdownloader2020.util.AppLangSessionManager;
import com.allvideodownloader.quickdownloader2020.util.SharePrefs;

import static com.allvideodownloader.quickdownloader2020.util.Utils.setLocale;


public class LoginActivity extends AppCompatActivity {
    LoginActivity activity;
    private String cookies;
    private SwipeRefreshLayout swipeRefreshLayout;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        activity = this;
        loadPage();
        swipeRefreshLayout.setOnRefreshListener(() -> loadPage());

    }

    public void loadPage() {

        CookieManager cookieManager = CookieManager.getInstance();

        cookieManager.removeAllCookies(value -> Toast.makeText(activity, "" + value, Toast.LENGTH_SHORT).show());


        webView.getSettings().setJavaScriptEnabled(true);
        webView.clearCache(true);
        webView.setWebViewClient(new MyBrowser());


        webView.loadUrl("https://www.instagram.com/accounts/login/");
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                swipeRefreshLayout.setRefreshing(progress != 100);
            }
        });
    }

    public String getCookie(String siteName, String cookieName) {
        String cookieValue = null;

        CookieManager cookieManager = CookieManager.getInstance();
        String cookies = cookieManager.getCookie(siteName);
        if (cookies != null && !cookies.isEmpty()) {
            String[] temp = cookies.split(";");
            for (String ar1 : temp) {
                if (ar1.contains(cookieName)) {
                    String[] temp1 = ar1.split("=");
                    cookieValue = temp1[1];
                    break;
                }
            }
        }
        return cookieValue;
    }

    private void initView() {
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        webView = findViewById(R.id.webView);
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }


        @Override
        public void onPageFinished(WebView webView, String str) {
            super.onPageFinished(webView, str);

            cookies = CookieManager.getInstance().getCookie(str);

            try {
                String sessionid = getCookie(str, "sessionid");
                String csrftoken = getCookie(str, "csrftoken");
                String userid = getCookie(str, "ds_user_id");
                if (sessionid != null && csrftoken != null && userid != null) {
                    SharePrefs.getInstance(activity).putString(SharePrefs.cookies, cookies);
                    SharePrefs.getInstance(activity).putString(SharePrefs.csrf, csrftoken);
                    SharePrefs.getInstance(activity).putString(SharePrefs.sessionId, sessionid);
                    SharePrefs.getInstance(activity).putString(SharePrefs.userId, userid);
                    SharePrefs.getInstance(activity).putBoolean(SharePrefs.isInstaLogin, true);

                    webView.destroy();
                    Intent intent = new Intent();
                    intent.putExtra("result", "result");
                    setResult(RESULT_OK, intent);
                    finish();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }
}