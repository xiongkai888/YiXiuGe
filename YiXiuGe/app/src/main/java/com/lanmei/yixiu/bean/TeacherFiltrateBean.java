package com.lanmei.yixiu.bean;

/**
 * @author xkai 教师筛选
 */
public class TeacherFiltrateBean {

    public TeacherFiltrateBean(String name){
        this.name = name;
    }

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

    private boolean isSelect;
    private String name;
}