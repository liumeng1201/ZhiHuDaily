package com.android.liumeng.zhihudaily.views;

import android.os.Bundle;
import android.widget.ImageView;

import com.android.liumeng.zhihudaily.R;

public class LaunchActivity extends VolleyBaseCompatActivity {
    private ImageView imageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        imageview = (ImageView) findViewById(R.id.launch_imageview);
    }

    @Override
    public void response(String payload, int requestCode) {

    }

}
