package com.lanmei.yixiu.event;

import com.lanmei.yixiu.bean.QuestionnaireSubjectBean;

import java.util.List;

/**
 * Created by xkai on 2018/10/22.
 */

public class AddQuestionnaireSubjectEvent {

    private List<QuestionnaireSubjectBean> list;

    public List<QuestionnaireSubjectBean> getList() {
        return list;
    }

    public AddQuestionnaireSubjectEvent(List<QuestionnaireSubjectBean> list){
        this.list = list;
    }
}
