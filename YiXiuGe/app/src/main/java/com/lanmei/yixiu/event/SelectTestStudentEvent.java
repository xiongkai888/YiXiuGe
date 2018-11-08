package com.lanmei.yixiu.event;

import com.lanmei.yixiu.bean.StudentsBean;

/**
 * Created by xkai on 2018/11/8.
 */

public class SelectTestStudentEvent {

    private StudentsBean bean;

    public SelectTestStudentEvent(StudentsBean bean){
        this.bean = bean;
    }

    public StudentsBean getBean() {
        return bean;
    }
}
