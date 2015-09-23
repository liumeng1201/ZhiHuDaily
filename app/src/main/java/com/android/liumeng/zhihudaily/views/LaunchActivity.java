package com.android.liumeng.zhihudaily.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.liumeng.zhihudaily.BuildConfig;
import com.android.liumeng.zhihudaily.R;
import com.android.liumeng.zhihudaily.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.utils.Log;

public class LaunchActivity extends VolleyBaseCompatActivity {
    private ImageView imageview;
    private TextView imageTitle;

    private final int START_MAIN_ACTIVITY = 2000;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case START_MAIN_ACTIVITY:
                    Intent intent = new Intent(activity, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.LOG = BuildConfig.DEBUG;
        // develop版本开启友盟统计debug模式，数据实时发送便于测试
        MobclickAgent.setDebugMode(BuildConfig.DEBUG);
        MobclickAgent.openActivityDurationTrack(false);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        imageTitle = (TextView) findViewById(R.id.launch_imageTitle);
        imageview = (ImageView) findViewById(R.id.launch_imageview);
        Animation scaleAnim = AnimationUtils.loadAnimation(activity, R.anim.start_page_scale_animation);
        scaleAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }
        });
        ImageLoader.getInstance().displayImage(StringUtils.isEmpty(application.getLaunchImg().img) ?
                "drawable://" + R.drawable.splash : "file://" + application.getLaunchImg().img, imageview);
        imageTitle.setText(application.getLaunchImg().text);
        imageview.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageview.startAnimation(scaleAnim);
        handler.sendEmptyMessageDelayed(START_MAIN_ACTIVITY, 2000);
    }

    @Override
    protected void onResume() {
        MobclickAgent.onPageStart(getClass().getSimpleName());
        super.onResume();
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPageEnd(getClass().getSimpleName());
        super.onPause();
    }

    @Override
    public void response(String payload, int requestCode) { }

}
