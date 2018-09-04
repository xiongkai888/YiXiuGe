package com.lanmei.yixiu.bean;

/**
 * Created by xkai on 2018/8/24.
 * 选择考试学生
 */

public class SelectStudentsBean {

    public String getName() {
        return name;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    private String name;
    private boolean isSelect;

}
