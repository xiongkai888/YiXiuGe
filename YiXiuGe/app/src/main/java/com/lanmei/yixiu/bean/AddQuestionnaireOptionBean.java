package com.lanmei.yixiu.bean;

import java.io.Serializable;

/**
 * Created by xkai on 2018/8/27.
 * 添加问题选项
 */

public class AddQuestionnaireOptionBean implements Serializable {

    private String text;
    private int id;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setText(String option) {
        this.text = option;
    }

    public String getText() {
        return text;
    }
}
