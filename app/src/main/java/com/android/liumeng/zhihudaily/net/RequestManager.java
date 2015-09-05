package com.android.liumeng.zhihudaily.net;

import android.app.ActivityManager;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class RequestManager {
	public static final String TAG = "VolleyPatterns";
	private static RequestQueue mRequestQueue;
	private static ImageLoader mImageLoader;

	public static void init(Context context) {
		mRequestQueue = Volley.newRequestQueue(context);

		int memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
		// Use 1/16th of the available memory for this memory cache.
		int cacheSize = 1024 * 1024 * memClass / 16;
		mImageLoader = new ImageLoader(mRequestQueue, new BitmapLruCache(cacheSize));
	}

	/**
	 * 获取Volley Request队列
	 * 
	 * @return Volley Request队列
	 */
	public static RequestQueue getRequestQueue() {
		if (mRequestQueue != null) {
			return mRequestQueue;
		} else {
			throw new IllegalStateException("RequestQueue not initialized");
		}
	}

	/**
	 * 将request加入到全局Volley Request队列中并设置tag
	 * 
	 * @param req
	 * @param tag
	 */
	public static <T> void addToRequestQueue(Request<T> req, Object tag) {
		if (tag != null) {
			req.setTag(tag);
		}
		getRequestQueue().add(req);
		VolleyLog.d("Adding request to queue: %s", req.getUrl());
	}

	/**
	 * 通过特定的tag来取消对应的request
	 * 
	 * @param tag
	 */
	public static void cancelAllRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}
	
	public static ImageLoader getImageLoader() {
		if (mImageLoader != null) {
			return mImageLoader;
		} else {
			throw new IllegalStateException("ImageLoader not initialized");
		}
	}
}
