package com.android.liumeng.zhihudaily.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;

public class DailyDetailsActivity extends VolleyBaseCompatActivity {
    private Toolbar toolbar;
    private WebView webView;
    private ImageView imageView;
    private TextView imageSource;
    private TextView title;
    private RelativeLayout dailyTitleLayout;

    private DailyDetails details;
    private String css;

    private Gson gson;
    private final int GET_DAILY_DETAILS = 3100;

    private UMSocialService shareController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_details);

        shareController = UMServiceFactory.getUMSocialService("com.umeng.share");
        shareController.getConfig().removePlatform(SHARE_MEDIA.RENREN, SHARE_MEDIA.DOUBAN, SHARE_MEDIA.QZONE, SHARE_MEDIA.TENCENT);
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(activity, "1104860670", "KxyWdMIjdOZyCdV2");
        qqSsoHandler.addToSocialSDK();

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        gson = new Gson();

        imageView = (ImageView) findViewById(R.id.image);
        imageSource = (TextView) findViewById(R.id.image_source);
        title = (TextView) findViewById(R.id.title);
        dailyTitleLayout = (RelativeLayout) findViewById(R.id.daily_title_layout);
        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);

        long id = getIntent().getLongExtra("id", -1);
        if (id > 0) {
            getDailyDetails(id);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /* 使用SSO授权必须添加如下代码 */
        UMSsoHandler ssoHandler = shareController.getConfig().getSsoHandler(requestCode) ;
        if(ssoHandler != null){
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
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
                if (!StringUtils.isEmpty(details.image)) {
                    dailyTitleLayout.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(details.image, imageView);
                } else {
                    dailyTitleLayout.setVisibility(View.GONE);
                }
                if (!StringUtils.isEmpty(details.image_source)) {
                    imageSource.setText(details.image_source);
                }
                if (!StringUtils.isEmpty(details.title)) {
                    title.setText(details.title);
                }
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
            shareController.setShareContent(details.title + "  " + details.share_url);
            shareController.setShareMedia(new UMImage(activity, details.image));

            shareController.openShare(activity, false);
            return true;
        } else if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
