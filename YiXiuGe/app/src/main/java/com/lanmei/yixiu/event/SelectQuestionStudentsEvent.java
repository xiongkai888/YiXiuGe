package com.lanmei.yixiu.event;

import com.lanmei.yixiu.bean.SelectQuestionStudentsBean;

import java.util.List;

/**
 * Created by xkai on 2018/10/22.
 * 选择问卷学生事件
 */

public class SelectQuestionStudentsEvent {

    private List<SelectQuestionStudentsBean.StudentBean> list;

    public String getGetCids() {
        return getCids;
    }

    private String getCids;

    public List<SelectQuestionStudentsBean.StudentBean> getList() {
        return list;
    }

    public SelectQuestionStudentsEvent(List<SelectQuestionStudentsBean.StudentBean> list,String getCids){
        this.list = list;
        this.getCids = getCids;
    }
}
