package com.android.liumeng.zhihudaily;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;

import com.android.liumeng.zhihudaily.model.LaunchImgItem;
import com.android.liumeng.zhihudaily.net.RequestManager;
import com.android.liumeng.zhihudaily.utils.Utils;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;

/**
 * Created by liumeng on 2015/9/5.
 */
public class ZhiHuDailyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        RequestManager.init(this);

//        if (BuildConfig.DEBUG) {
//            CrashHandler crashHandler = CrashHandler.getInstance();
//            crashHandler.init(getApplicationContext());
//        }

        // Create global configuration and initialize ImageLoader with this config
        File uilCacheDir = new File(Utils.getFolderPath(Utils.UIL_CACHE_DIR));
        int diskCacheMaxSize = 100 * 1024 * 1024;
        int memoryCacheMaxSize = 2 * 1024 * 1024;
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_launcher)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .cacheInMemory(true).cacheOnDisk(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(this)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(memoryCacheMaxSize))
                .memoryCacheSize(memoryCacheMaxSize)
                .diskCacheSize(diskCacheMaxSize)
                .diskCache(new UnlimitedDiskCache(uilCacheDir))
                .defaultDisplayImageOptions(options)
                .build();
        ImageLoader.getInstance().init(config);
    }

    // 设置启动封面的路径
    public void setLaunchImg(LaunchImgItem item) {
        SharedPreferences sharedPreferences = getSharedPreferences(Utils.APP_PREFERENCE, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("launch_image", new Gson().toJson(item));
        editor.apply();
    }

    // 获取启动封面的路径
    public LaunchImgItem getLaunchImg() {
        SharedPreferences sharedPreferences = getSharedPreferences(Utils.APP_PREFERENCE, Activity.MODE_PRIVATE);
        String info = sharedPreferences.getString("launch_image", null);
        LaunchImgItem item = new Gson().fromJson(info, LaunchImgItem.class);
        if (item == null) {
            item = new LaunchImgItem();
        }
        return item;
    }
}
