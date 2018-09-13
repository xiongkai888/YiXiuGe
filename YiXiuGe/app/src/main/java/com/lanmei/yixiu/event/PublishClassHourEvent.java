package com.lanmei.yixiu.event;

/**
 * Created by xkai on 2018/9/13.
 * 发布课时事件
 */

public class PublishClassHourEvent {

    public PublishClassHourEvent(String over){
        this.over = over;
    }

    public String getOver() {
        return over;
    }

    private String over;

}
