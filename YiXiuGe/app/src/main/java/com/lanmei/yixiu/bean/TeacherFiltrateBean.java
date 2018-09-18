package com.lanmei.yixiu.bean;

/**
 * @author xkai 教师筛选
 */
public class TeacherFiltrateBean {

    private boolean isSelect;
    private String id = "";
    private String name;

    public boolean isSelect() {
        return isSelect;
    }

    public String getName() {
        return name;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}