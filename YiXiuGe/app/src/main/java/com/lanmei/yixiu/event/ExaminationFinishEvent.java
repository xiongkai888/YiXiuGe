package com.lanmei.yixiu.event;

/**
 * Created by xkai on 2018/10/26.
 * 考试完成事件
 */

public class ExaminationFinishEvent {

    private String type;

    public String getType() {
        return type;
    }

    public ExaminationFinishEvent(String type) {
        this.type= type;
    }
}
