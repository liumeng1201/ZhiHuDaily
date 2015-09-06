package com.android.liumeng.zhihudaily.model;

import java.util.List;

/**
 * 日报详细信息
 * Created by liumeng on 2015/9/6.
 */
public class DailyDetails {
    // HTML 格式的新闻
    public String body;

    // 图片的内容提供方
    public String image_source;

    // 新闻标题
    public String title;

    // 获得的图片同 最新消息 获得的图片分辨率不同。这里获得的是在文章浏览界面中使用的大图。
    public String image;

    // 供在线查看内容与分享至 SNS 用的 URL
    public String share_url;

    //  供手机端的 WebView(UIWebView) 使用
    public List<String> js;

    // 这篇文章的推荐者
    public List<RecommenderItem> recommenders;

    // 供 Google Analytics 使用
    public String ga_prefix;

    // 栏目的信息
    public SectionItem section;

    // 新闻的类型
    public int type;

    // 新闻的 id
    public long id;

    // 供手机端的 WebView(UIWebView) 使用
    public List<String> css;
}
