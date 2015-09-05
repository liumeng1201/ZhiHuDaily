package com.android.liumeng.zhihudaily.net;

import android.content.Context;

import com.android.liumeng.zhihudaily.R;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

public class VolleyErrorHelper {
	/**
	 * 返回错误信息
	 * 
	 * @param error
	 * @param context
	 * @return
	 */
	public static String getMessage(Object error, Context context) {
		if (error instanceof TimeoutError) {
			return context.getResources().getString(R.string.generic_server_down);
		} else if (isServerProblem(error)) {
			return handleServerError(error, context);
		} else if (isNetworkProblem(error)) {
			return context.getResources().getString(R.string.generic_server_down);
		}
		return context.getResources().getString(R.string.generic_server_down);
	}

	/**
	 * 是否是网络错误
	 * 
	 * @param error
	 * @return
	 */
	private static boolean isNetworkProblem(Object error) {
		return (error instanceof NetworkError)
				|| (error instanceof NoConnectionError);
	}

	/**
	 * 是否是服务器错误
	 * 
	 * @param error
	 * @return
	 */
	private static boolean isServerProblem(Object error) {
		return (error instanceof ServerError)
				|| (error instanceof AuthFailureError);
	}

	/**
	 * 处理服务器错误
	 * 
	 * @param err
	 * @param context
	 * @return
	 */
	private static String handleServerError(Object err, Context context) {
		VolleyError error = (VolleyError) err;

		NetworkResponse response = error.networkResponse;

		if (response != null) {
			switch (response.statusCode) {
			case 404:
			case 422:
			case 401:
				try {
					// 如果服务器返回如下错误信息 { "error": "Some error occured" }
					// 则使用Gson来解析错误信息
					HashMap<String, String> result = new Gson().fromJson(
							new String(response.data),
							new TypeToken<Map<String, String>>() {
							}.getType());
					if (result != null && result.containsKey("error")) {
						return result.get("error");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				// 无效的请求
				return error.getMessage();
			default:
				return context.getResources().getString(R.string.generic_server_down);
			}
		}
		return context.getResources().getString(R.string.generic_server_down);
	}
}