package com.lanmei.yixiu.event;

/**
 * Created by xkai on 2018/9/10.
 * 添加教程事件
 */

public class AddCourseEvent {

    private String cid;

    public String getCid() {
        return cid;
    }

    public AddCourseEvent(String cid){
        this.cid = cid;
    }
}
