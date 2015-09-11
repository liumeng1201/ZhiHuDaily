package com.android.liumeng.zhihudaily.views;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.liumeng.zhihudaily.R;
import com.android.liumeng.zhihudaily.adapter.ListAdapter;
import com.android.liumeng.zhihudaily.listener.MyRecyclerViewScrollListener;
import com.android.liumeng.zhihudaily.listener.RecyclerClickListner;
import com.android.liumeng.zhihudaily.model.DailyBefore;
import com.android.liumeng.zhihudaily.model.DailyItem;
import com.android.liumeng.zhihudaily.model.DailyLatest;
import com.android.liumeng.zhihudaily.model.ListItem;
import com.android.liumeng.zhihudaily.net.RequestManager;
import com.android.liumeng.zhihudaily.net.Urls;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liumeng on 2015/9/10.
 */
public class DailyListFragment extends VolleyBaseFragment {
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView listView;
    private SwipeRefreshLayout.OnRefreshListener refreshListener;
    private RecyclerView.LayoutManager layoutManager;
    private MyRecyclerViewScrollListener scrollListener;
    private ListAdapter adapter;

    private List<ListItem> dailyList;
    private String date;

    private Gson gson;
    private final int GET_LATEST_DAILIES = 3000;
    private final int GET_DAILY_BEFORE = 3001;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily_list, container, false);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        listView = (RecyclerView) view.findViewById(R.id.listview);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        gson = new Gson();
        dailyList = new ArrayList<ListItem>();
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
        adapter = new ListAdapter(activity, dailyList);
        adapter.setOnClickListener(new RecyclerClickListner() {
            @Override
            public void onItemClick(View view, int postion) {
                Intent intent = new Intent(activity, DailyDetailsActivity.class);
                intent.putExtra("id", ((DailyItem)dailyList.get(postion).item).id);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int postion) { }
        });

        refreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
        refreshLayout.setOnRefreshListener(refreshListener);
        listView.setHasFixedSize(true);
        listView.setLayoutManager(layoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.setAdapter(adapter);
        listView.addOnScrollListener(scrollListener);

        refreshLayout.setRefreshing(true);
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
        switch (requestCode) {
            case GET_LATEST_DAILIES:
                DailyLatest lates = gson.fromJson(payload, DailyLatest.class);
                date = lates.date;
                dailyList.clear();
                dailyList.add(new ListItem(ListItem.DATE, date));
                dailyList.addAll(new ListItem().setValues(ListItem.ITEM, lates.stories));
                break;
            case GET_DAILY_BEFORE:
                DailyBefore before = gson.fromJson(payload, DailyBefore.class);
                date = before.date;
                dailyList.add(new ListItem(ListItem.DATE, date));
                dailyList.addAll(new ListItem().setValues(ListItem.ITEM, before.stories));
                break;
        }
        adapter.refresh(dailyList);
    }
}
