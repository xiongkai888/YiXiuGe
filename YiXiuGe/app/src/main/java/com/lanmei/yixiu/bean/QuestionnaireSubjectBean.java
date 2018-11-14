package com.lanmei.yixiu.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xkai on 2018/10/19.
 */

public class QuestionnaireSubjectBean implements Serializable{

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    private int id;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String title;

    private String type;

    private List<AddQuestionnaireOptionBean> select;

    public void setSelect(List<AddQuestionnaireOptionBean> select) {
        this.select = select;
    }

    public List<AddQuestionnaireOptionBean> getSelect() {
        return select;
    }
}
