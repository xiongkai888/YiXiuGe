package com.medui.yixiu.bean;

/**
 * Created by xkai on 2018/8/21.
 *
 */

public class ExaminationTopicBean {

    private String topic;//
    private String item;//abcd
    private boolean isSelect;//是否被选择


    public void setSelect(boolean select) {
        isSelect = select;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public String getTopic() {
        return topic;
    }

    public String getItem() {
        return item;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setItem(String item) {
        this.item = item;
    }
}
