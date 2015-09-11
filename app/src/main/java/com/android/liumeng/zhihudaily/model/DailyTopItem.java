package com.android.liumeng.zhihudaily.model;

/**
 * Created by liumeng on 2015/9/11.
 */
public class DailyTopItem {
    // 图像地址
    public String image;

    // 作用未知
    public int type;

    // url 与 share_url 中最后的数字（应为内容的 id）
    public long id;

    // 供 Google Analytics 使用
    public String ga_prefix;

    // 新闻标题
    public String title;
}
