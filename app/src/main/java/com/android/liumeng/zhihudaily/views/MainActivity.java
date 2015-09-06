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
import com.android.liumeng.zhihudaily.net.RequestManager;
import com.android.liumeng.zhihudaily.net.Urls;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

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

    private final int GET_LATEST_DAILIES = 3000;
    private final int GET_DAILY_BEFORE = 3001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        listView = (RecyclerView) findViewById(R.id.listview);

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

    @Override
    public void response(String payload, int requestCode) {
        refreshLayout.setRefreshing(false);
        Gson gson = new Gson();
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
