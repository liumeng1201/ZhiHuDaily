package com.android.liumeng.zhihudaily.events;

/**
 * Created by liumeng on 2015/9/5.
 */
public class EventVolleyResponse {
    private int eventType;

    public EventVolleyResponse(int eventType) {
        this.eventType = eventType;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }
}
