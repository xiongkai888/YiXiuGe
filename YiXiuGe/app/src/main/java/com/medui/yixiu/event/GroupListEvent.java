package com.medui.yixiu.event;

/**
 * Created by xkai on 2018/10/29.
 * 获取群列表事件
 */

public class GroupListEvent {

    private int type;

    public int getType() {
        return type;
    }

    public GroupListEvent(int type){
        this.type = type;
    }
}
