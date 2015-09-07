package com.android.liumeng.zhihudaily.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.liumeng.zhihudaily.R;
import com.android.liumeng.zhihudaily.adapter.DailyListAdapter;
import com.android.liumeng.zhihudaily.listener.MyRecyclerViewScrollListener;
import com.android.liumeng.zhihudaily.listener.RecyclerClickListner;
import com.android.liumeng.zhihudaily.model.DailyBefore;
import com.android.liumeng.zhihudaily.model.DailyItem;
import com.android.liumeng.zhihudaily.model.DailyLatest;
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
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends VolleyBaseCompatActivity {
    private Toolbar toolbar;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView listView;
    private SwipeRefreshLayout.OnRefreshListener refreshListener;
    private RecyclerView.LayoutManager layoutManager;
    private MyRecyclerViewScrollListener scrollListener;
    private DailyListAdapter adapter;

    private List<DailyItem> dailyList;
    private String date;

    private Gson gson;
    private final int GET_LATEST_DAILIES = 3000;
    private final int GET_DAILY_BEFORE = 3001;
    private final int GET_LAUNCH_IMG = 3002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        listView = (RecyclerView) findViewById(R.id.listview);

        gson = new Gson();
        dailyList = new ArrayList<DailyItem>();
        refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                getLatestDailies();
            }
        };
        scrollListener = new MyRecyclerViewScrollListener(){
            @Override
            public void onBottom() {
                super.onBottom();
                getDailiesBefore(date);
            }
        };

        layoutManager = new LinearLayoutManager(activity);
        adapter = new DailyListAdapter(activity, dailyList);
        adapter.setOnClickListener(new RecyclerClickListner() {
            @Override
            public void onItemClick(View view, int postion) {
                Intent intent = new Intent(activity, DailyDetailsActivity.class);
                intent.putExtra("id", dailyList.get(postion).id);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int postion) { }
        });

        setSupportActionBar(toolbar);
        refreshLayout.setOnRefreshListener(refreshListener);
        listView.setHasFixedSize(true);
        listView.setLayoutManager(layoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.setAdapter(adapter);
        listView.addOnScrollListener(scrollListener);

        getLatestDailies();
        getLaunchImg();
    }

    private void getLatestDailies() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                Urls.get_latest_dailies, null, responseListener(GET_LATEST_DAILIES), errorListener()) { };

        RequestManager.addToRequestQueue(request, activity);
    }

    private void getDailiesBefore(final String date) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                Urls.get_dailies_before + date, null, responseListener(GET_DAILY_BEFORE), errorListener()) { };

        RequestManager.addToRequestQueue(request, activity);
    }

    private void getLaunchImg() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                Urls.get_launch_img, null, responseListener(GET_LAUNCH_IMG), errorListener()) { };

        RequestManager.addToRequestQueue(request, activity);
    }

    @Override
    public void response(String payload, int requestCode) {
        refreshLayout.setRefreshing(false);
        switch (requestCode) {
            case GET_LATEST_DAILIES:
                DailyLatest lates = gson.fromJson(payload, DailyLatest.class);
                date = lates.date;
                dailyList = lates.stories;
                break;
            case GET_DAILY_BEFORE:
                DailyBefore before = gson.fromJson(payload, DailyBefore.class);
                date = before.date;
                dailyList.addAll(before.stories);
                break;
            case GET_LAUNCH_IMG:
                LaunchImgItem imgItem = gson.fromJson(payload, LaunchImgItem.class);
                if (!StringUtils.isEmpty(imgItem.img)) {
                    final DownloadHelper downloadHelper = new DownloadHelper((int) System.currentTimeMillis(), imgItem.img, Utils.getFolderPath(Utils.LAUNCH_IMG_DIR));
                    downloadHelper.setOnDownloadListener(new DownloadHelper.OnDownloadListener() {
                        @Override
                        public void onStart(int downloadId, long fileSize) { }

                        @Override
                        public void onPublish(int downloadId, long size) { }

                        @Override
                        public void onSuccess(int downloadId) {
                            application.setLaunchImg(Utils.getFolderPath(Utils.LAUNCH_IMG_DIR) + "/" + downloadHelper.getFileName());
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
                    }
                }
                break;
        }
        adapter.refresh(dailyList);
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
        }

        return super.onOptionsItemSelected(item);
    }
}
