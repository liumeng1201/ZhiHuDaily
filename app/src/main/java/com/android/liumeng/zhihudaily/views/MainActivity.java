package com.android.liumeng.zhihudaily.views;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.android.liumeng.zhihudaily.R;
import com.android.liumeng.zhihudaily.components.CustomDialog;
import com.android.liumeng.zhihudaily.model.LaunchImgItem;
import com.android.liumeng.zhihudaily.net.RequestManager;
import com.android.liumeng.zhihudaily.net.Urls;
import com.android.liumeng.zhihudaily.utils.DownloadHelper;
import com.android.liumeng.zhihudaily.utils.FileUtils;
import com.android.liumeng.zhihudaily.utils.StringUtils;
import com.android.liumeng.zhihudaily.utils.Utils;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import java.io.File;

public class MainActivity extends VolleyBaseCompatActivity implements CustomDialog.CustomDialogClickListener {
    private Toolbar toolbar;
    private ViewPager viewPager;
    private Gson gson;
    private final int GET_LAUNCH_IMG = 3002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gson = new Gson();

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return new DailyListFragment();
            }

            @Override
            public int getCount() {
                return 1;
            }
        };
        viewPager.setAdapter(adapter);

        getLaunchImg();
    }

    private void getLaunchImg() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                Urls.get_launch_img, null, responseListener(GET_LAUNCH_IMG), errorListener()) { };

        RequestManager.addToRequestQueue(request, activity);
    }

    @Override
    public void response(String payload, int requestCode) {
        switch (requestCode) {
            case GET_LAUNCH_IMG:
                final LaunchImgItem imgItem = gson.fromJson(payload, LaunchImgItem.class);
                if (!StringUtils.isEmpty(imgItem.img)) {
                    final DownloadHelper downloadHelper = new DownloadHelper((int) System.currentTimeMillis(), imgItem.img, Utils.getFolderPath(Utils.LAUNCH_IMG_DIR));
                    downloadHelper.setOnDownloadListener(new DownloadHelper.OnDownloadListener() {
                        @Override
                        public void onStart(int downloadId, long fileSize) { }

                        @Override
                        public void onPublish(int downloadId, long size) { }

                        @Override
                        public void onSuccess(int downloadId) {
                            LaunchImgItem item = new LaunchImgItem();
                            item.img = Utils.getFolderPath(Utils.LAUNCH_IMG_DIR) + "/" + downloadHelper.getFileName();
                            item.text = imgItem.text;
                            application.setLaunchImg(item);
                        }

                        @Override
                        public void onPause(int downloadId) { }

                        @Override
                        public void onError(int downloadId) {
                            FileUtils.deleteFile(Utils.getFolderPath(Utils.LAUNCH_IMG_DIR) + "/" + downloadHelper.getFileName());
                        }

                        @Override
                        public void onCancel(int downloadId) {
                            FileUtils.deleteFile(Utils.getFolderPath(Utils.LAUNCH_IMG_DIR) + "/" + downloadHelper.getFileName());
                        }

                        @Override
                        public void onGoon(int downloadId, long localSize) { }
                    });
                    if (!downloadHelper.downloadFileExist(new File(Utils.getFolderPath(Utils.LAUNCH_IMG_DIR)), downloadHelper.getFileName())) {
                        downloadHelper.start(false);
                    } else {
                        LaunchImgItem item = new LaunchImgItem();
                        item.img = Utils.getFolderPath(Utils.LAUNCH_IMG_DIR) + "/" + downloadHelper.getFileName();
                        item.text = imgItem.text;
                        application.setLaunchImg(item);
                    }
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_about) {
            CustomDialog customDialog = new CustomDialog();
            Bundle args = new Bundle();
            args.putString(CustomDialog.TITLE, getString(R.string.action_about));
            args.putString(CustomDialog.MESSAGE, getString(R.string.about_message));
            args.putString(CustomDialog.POSITIVE, getString(R.string.ok));
            customDialog.setArguments(args);
            customDialog.show(getSupportFragmentManager(), "about dialog");
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void positiveClick(DialogInterface dialog) {
        dialog.dismiss();
    }

    @Override
    public void negativeClick(DialogInterface dialog) {
        dialog.dismiss();
    }
}
