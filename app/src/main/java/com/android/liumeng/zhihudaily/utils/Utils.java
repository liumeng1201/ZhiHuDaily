package com.android.liumeng.zhihudaily.utils;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.File;

/**
 * Created by liumeng on 2015/9/5.
 */
public class Utils {
    public static final int VOLLEY_RESPONSE_ERROR = 1000;

    public static final String APP_PREFERENCE = "zhd_preference";

    public static final String UIL_CACHE_DIR = "zhihudaily/uil";
    public static final String LAUNCH_IMG_DIR = "zhihudaily/launch";

    public static String getFolderPath(String path) {
        String folderPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + path;
        File f = new File(folderPath);
        if (!f.exists()) {
            f.mkdirs();
        }
        return folderPath;
    }

    public static void showToastShort(AppCompatActivity activity, String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }
}
