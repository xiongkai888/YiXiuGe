package com.medui.yixiu.helper;


import android.content.Context;

import com.medui.yixiu.bean.ExaminationBean;
import com.medui.yixiu.bean.ExaminationTopicBean;
import com.medui.yixiu.utils.CommonUtils;
import com.xson.common.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xkai on 2018/1/20.
 */

public class ExaminationPresenter implements ExaminationContract.Presenter {

    private ExaminationContract.View view;
    private Context context;

    public ExaminationPresenter(Context context, ExaminationContract.View view) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void getAnswerAtTopic(List<ExaminationTopicBean> list) {
        view.setAnswer(getAnswers(list));
    }

    @Override
    public void getAnswerList(List<ExaminationBean.ExamBean> list) {
        view.submit(getAnswersList(list));
    }

    private String getAnswers(List<ExaminationTopicBean> list) {
        StringBuilder builder = new StringBuilder();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            ExaminationTopicBean bean = list.get(i);
            if (bean.isSelect()) {
                builder.append(CommonUtils.getLetterByPosition(context, i));
            }
        }
        return builder.toString();
    }

    private List<String> getAnswersList(List<ExaminationBean.ExamBean> list) {
        StringBuilder builder = new StringBuilder();
        List<String> stringList = new ArrayList<>();
        for (ExaminationBean.ExamBean bean : list) {
            List<ExaminationTopicBean> topicBeans = bean.getList();
            if (!StringUtils.isEmpty(topicBeans)) {
                int size = topicBeans.size();
                for (int i = 0; i < size; i++) {
                    ExaminationTopicBean topicBean = topicBeans.get(i);
                    if (topicBean.isSelect()) {
                        builder.append(String.valueOf(i + 1)).append(",");
                    }
                }
                if (builder.length() > 0) {
                    builder.deleteCharAt(builder.length() - 1);
                    stringList.add(builder.toString());
                    builder.setLength(0);
                }
            }
        }
        return stringList;
    }
}
