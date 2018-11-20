package com.medui.yixiu.event;

import com.medui.yixiu.bean.SelectQuestionStudentsBean;

import java.util.List;

/**
 * Created by xkai on 2018/10/22.
 * 选择问卷学生事件
 */

public class SelectQuestionStudentsEvent {

    private List<SelectQuestionStudentsBean.StudentBean> list;
    private  List<SelectQuestionStudentsBean> beanList;

    public List<SelectQuestionStudentsBean> getBeanList() {
        return beanList;
    }

    public List<SelectQuestionStudentsBean.StudentBean> getList() {
        return list;
    }

    public SelectQuestionStudentsEvent(List<SelectQuestionStudentsBean.StudentBean> list,List<SelectQuestionStudentsBean> beanList){
        this.list = list;
        this.beanList = beanList;
    }
}
