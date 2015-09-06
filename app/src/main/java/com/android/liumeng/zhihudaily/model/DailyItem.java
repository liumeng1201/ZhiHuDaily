package com.android.liumeng.zhihudaily.model;

import java.util.List;

/**
 * 日报列表单个item
 * Created by liumeng on 2015/9/6.
 */
public class DailyItem {
    // 图像地址（官方 API 使用数组形式。目前暂未有使用多张图片的情形出现，曾见无 images 属性的情况，请在使用中注意 ）
    public List<String> images;

    // 作用未知
    public int type;

    // url 与 share_url 中最后的数字（应为内容的 id）
    public long id;

    // 供 Google Analytics 使用
    public String ga_prefix;

    // 新闻标题
    public String title;

    // 消息是否包含多张图片（仅出现在包含多图的新闻中）
    public boolean multipic;
}
