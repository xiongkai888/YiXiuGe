package com.medui.yixiu.helper;

import com.medui.yixiu.bean.ExaminationBean;
import com.medui.yixiu.bean.ExaminationTopicBean;

import java.util.List;

/**
 * Created by xkai on 2018/1/20.
 */

public class ExaminationContract {

    public interface View  {

        void setAnswer(String answer);
        void submit(List<String> list);

    }

    public interface Presenter  {

        void getAnswerAtTopic(List<ExaminationTopicBean> list);
        void getAnswerList(List<ExaminationBean.ExamBean> list);
    }
}
