package com.medui.yixiu.event;

import com.medui.yixiu.bean.QuestionnaireSubjectBean;

/**
 * Created by xkai on 2018/10/19.
 */

public class QuestionnaireSubjectEvent {

    private QuestionnaireSubjectBean bean;

    public QuestionnaireSubjectBean getBean() {
        return bean;
    }

    public QuestionnaireSubjectEvent(QuestionnaireSubjectBean bean){
        this.bean = bean;
    }
}
