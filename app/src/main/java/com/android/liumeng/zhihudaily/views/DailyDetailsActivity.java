package com.android.liumeng.zhihudaily.views;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import com.android.liumeng.zhihudaily.R;
import com.android.liumeng.zhihudaily.model.DailyDetails;
import com.android.liumeng.zhihudaily.net.RequestManager;
import com.android.liumeng.zhihudaily.net.Urls;
import com.android.liumeng.zhihudaily.utils.StringUtils;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

public class DailyDetailsActivity extends VolleyBaseCompatActivity {
    private Toolbar toolbar;
    private WebView webView;

    private DailyDetails details;
    private String css;

    private Gson gson;
    private final int GET_DAILY_DETAILS = 3100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_details);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        gson = new Gson();

        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);

        long id = getIntent().getLongExtra("id", -1);
        if (id > 0) {
            getDailyDetails(id);
        }
    }

    private void getDailyDetails(final long id) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                Urls.get_daily_details + id, null, responseListener(GET_DAILY_DETAILS), errorListener()) { };

        RequestManager.addToRequestQueue(request, activity);
    }

    private void getCss(final String url) {
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                css = "<style type=\"text/css\">" + s + "</style>";
                webView.loadDataWithBaseURL(null, details.body + css, "text/html", "utf-8", null);
            }
        }, errorListener()) { };

        RequestManager.addToRequestQueue(request, activity);
    }

    @Override
    public void response(String payload, int requestCode) {
        switch (requestCode) {
            case GET_DAILY_DETAILS:
                details = gson.fromJson(payload, DailyDetails.class);
                if (StringUtils.isEmpty(details.css.get(0))) {
                    webView.loadDataWithBaseURL(null, details.body, "text/html", "utf-8", null);
                } else {
                    getCss(details.css.get(0));
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_daily_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
