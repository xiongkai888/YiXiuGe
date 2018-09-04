package com.lanmei.yixiu.ui.mine.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.adapter.ExaminationAnswerAdapter;
import com.lanmei.yixiu.adapter.ExaminationTopicAdapter;
import com.lanmei.yixiu.bean.ExaminationAnswerBean;
import com.lanmei.yixiu.bean.ExaminationTopicBean;
import com.xson.common.app.BaseActivity;
import com.xson.common.utils.UIHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 随堂测试
 */
public class Examination1Activity extends BaseActivity {


    @InjectView(R.id.toolbar_name_tv)
    TextView toolbarNameTv;
    @InjectView(R.id.menu_tv)
    TextView menuTv;
    @InjectView(R.id.recyclerView_topic)
    RecyclerView recyclerViewTopic;//试题
    @InjectView(R.id.recyclerView_answer)
    RecyclerView recyclerViewAnswer;//答案
    ExaminationTopicAdapter examinationTopicAdapter;
    ExaminationAnswerAdapter examinationAnswerAdapter;

    @Override
    public int getContentViewId() {
        return R.layout.activity_examination;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        toolbarNameTv.setText("随堂测试");
        menuTv.setText(String.format(getString(R.string.examination_num),"1","20"));


        recyclerViewTopic.setLayoutManager(new LinearLayoutManager(this));
        examinationTopicAdapter = new ExaminationTopicAdapter(this);
        examinationTopicAdapter.setData(getExaminationTopicBeanList());
        recyclerViewTopic.setAdapter(examinationTopicAdapter);
        recyclerViewTopic.setNestedScrollingEnabled(false);
        examinationTopicAdapter.setMultiterm(false);

        recyclerViewAnswer.setLayoutManager(new GridLayoutManager(this, 4));
        examinationAnswerAdapter = new ExaminationAnswerAdapter(this);
        examinationAnswerAdapter.setData(getExaminationAnswerBeanList());
        recyclerViewAnswer.setAdapter(examinationAnswerAdapter);
        recyclerViewAnswer.setNestedScrollingEnabled(false);
    }


    private List<ExaminationTopicBean> getExaminationTopicBeanList() {
        List<ExaminationTopicBean> listTopic = new ArrayList<>();
        ExaminationTopicBean bean1 = new ExaminationTopicBean();
        bean1.setItem("A");
        bean1.setTopic("功能升高或兴奋");
        ExaminationTopicBean bean2 = new ExaminationTopicBean();
        bean2.setItem("B");
        bean2.setTopic("功能降低或抑制");
        ExaminationTopicBean bean3 = new ExaminationTopicBean();
        bean3.setItem("C");
        bean3.setTopic("兴奋或抑制");
        ExaminationTopicBean bean4 = new ExaminationTopicBean();
        bean4.setItem("D");
        bean4.setTopic("产生新的功能");
        listTopic.add(bean1);
        listTopic.add(bean2);
        listTopic.add(bean3);
        listTopic.add(bean4);
        return listTopic;
    }

    private List<ExaminationAnswerBean> getExaminationAnswerBeanList() {
        List<ExaminationAnswerBean> listTopic = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            ExaminationAnswerBean answerBean = new ExaminationAnswerBean();
            answerBean.setItem("" + (i + 1));
            answerBean.setTopic("ABCD");
            listTopic.add(answerBean);
        }
        return listTopic;
    }

    @OnClick({R.id.on_a_bt, R.id.next_bt, R.id.back_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.on_a_bt:
                UIHelper.ToastMessage(this,R.string.developing);
                break;
            case R.id.next_bt:
                UIHelper.ToastMessage(this,R.string.developing);
                break;
            case R.id.back_iv:
                finish();
                break;
        }
    }
}
