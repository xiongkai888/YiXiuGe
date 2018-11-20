package com.medui.yixiu.event;

import com.medui.yixiu.bean.StudentsBean;

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
