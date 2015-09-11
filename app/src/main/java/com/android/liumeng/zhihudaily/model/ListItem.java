package com.android.liumeng.zhihudaily.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liumeng on 2015/9/11.
 */
public class ListItem {
    public static final int DATE = 100;
    public static final int ITEM = 101;
    public int type;
    public Object item;

    public ListItem() {}

    public ListItem(int type, Object object) {
        this.type = type;
        this.item = object;
    }

    public List<ListItem> setValues(int type, List<DailyItem> values) {
        List<ListItem> list = new ArrayList<ListItem>();
        for (Object value : values) {
            list.add(new ListItem(type, value));
        }
        return list;
    }
}
