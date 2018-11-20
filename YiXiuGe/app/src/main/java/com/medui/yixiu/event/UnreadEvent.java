package com.medui.yixiu.event;

/**
 * Created by xkai on 2018/10/31.
 * 未读消息数
 */

public class UnreadEvent {

    private int count;

    public int getCount() {
        return count;
    }

    public UnreadEvent(int count){
        this.count = count;
    }
}
