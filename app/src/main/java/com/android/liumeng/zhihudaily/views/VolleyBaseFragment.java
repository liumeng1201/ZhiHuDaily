package com.android.liumeng.zhihudaily.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.android.liumeng.zhihudaily.ZhiHuDailyApp;
import com.android.liumeng.zhihudaily.events.EventVolleyResponse;
import com.android.liumeng.zhihudaily.net.RequestManager;
import com.android.liumeng.zhihudaily.net.VolleyErrorHelper;
import com.android.liumeng.zhihudaily.utils.Utils;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import de.greenrobot.event.EventBus;

/**
 * Created by liumeng on 2015/9/10.
 */
public abstract class VolleyBaseFragment extends Fragment {
    protected AppCompatActivity activity;
    protected ZhiHuDailyApp application;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (AppCompatActivity) getActivity();
        application = (ZhiHuDailyApp) activity.getApplication();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        RequestManager.cancelAllRequests(this);
    }

    protected void executeRequest(Request<?> request) {
        request.setRetryPolicy(new DefaultRetryPolicy(15000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestManager.addToRequestQueue(request, this);
    }

    protected Response.Listener<JSONObject> responseListener(final int requestCode) {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String payload = response.toString();
                response(payload, requestCode);
            }
        };
    }

    public abstract void response(String payload, int requestCode);

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.showToastShort(activity, VolleyErrorHelper.getMessage(error, activity));
                EventBus.getDefault().post(new EventVolleyResponse(Utils.VOLLEY_RESPONSE_ERROR));
            }
        };
    }
}
