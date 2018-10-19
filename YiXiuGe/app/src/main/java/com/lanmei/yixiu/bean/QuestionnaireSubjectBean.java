package com.lanmei.yixiu.bean;

import java.util.List;

/**
 * Created by xkai on 2018/10/19.
 */

public class QuestionnaireSubjectBean {
    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public List<AddQuestionnaireOptionBean> getList() {
        return list;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setList(List<AddQuestionnaireOptionBean> list) {
        this.list = list;
    }

    private String title;

    private String type;
    private List<AddQuestionnaireOptionBean> list;
}
