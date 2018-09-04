package com.lanmei.yixiu.ui.home.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.adapter.ExaminationTopicAdapter;
import com.lanmei.yixiu.bean.ExaminationTopicBean;
import com.xson.common.app.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

public class QuestionnaireActivity extends BaseActivity {


    @InjectView(R.id.toolbar_name_tv)
    TextView toolbarNameTv;
    @InjectView(R.id.menu_tv)
    TextView menuTv;

    @InjectView(R.id.recyclerView_topic)
    RecyclerView recyclerViewTopic;//试题
    ExaminationTopicAdapter examinationTopicAdapter;

    @Override
    public int getContentViewId() {
        return R.layout.activity_questionnaire;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        toolbarNameTv.setText(R.string.check_questionnaire);
        menuTv.setText(String.format(getString(R.string.examination_num),"1","3"));

        recyclerViewTopic.setLayoutManager(new LinearLayoutManager(this));
        examinationTopicAdapter = new ExaminationTopicAdapter(this);
        examinationTopicAdapter.setData(getExaminationTopicBeanList());
        recyclerViewTopic.setAdapter(examinationTopicAdapter);
        recyclerViewTopic.setNestedScrollingEnabled(false);
        examinationTopicAdapter.setMultiterm(false);
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

    @OnClick({R.id.back_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
        }
    }

}
