package com.lanmei.yixiu.ui.teacher.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.adapter.ExaminationTopicAdapter;
import com.lanmei.yixiu.adapter.StudentTestAnswerAdapter;
import com.lanmei.yixiu.api.YiXiuGeApi;
import com.lanmei.yixiu.bean.StudentTestAnswerBean;
import com.lanmei.yixiu.bean.StudentTestBean;
import com.lanmei.yixiu.bean.StudentsBean;
import com.lanmei.yixiu.helper.ClickAnswerListener;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 学生线下考试试题
 */
public class StudentTestActivity extends BaseActivity {


    @InjectView(R.id.toolbar_name_tv)
    TextView toolbarNameTv;
    @InjectView(R.id.menu_tv)
    TextView menuTv;
    @InjectView(R.id.next_bt)
    Button nextBt;//下一题
    @InjectView(R.id.title_tv)
    TextView titleTv;//题目
    @InjectView(R.id.ll_bt)
    LinearLayout llBt;//上一题下一题按钮
    @InjectView(R.id.recyclerView_topic)
    RecyclerView recyclerViewTopic;//试题
    @InjectView(R.id.recyclerView_answer)
    RecyclerView recyclerViewAnswer;//答案
    ExaminationTopicAdapter examinationTopicAdapter;//选择题
    StudentTestAnswerAdapter answerAdapter;//答案

    private StudentsBean bean;//学生信息
    private String id;//评估id
    private String time;//评估时间

    private List<StudentTestBean> list;//考试题目列表
    private List<StudentTestAnswerBean> beanList;//答案列表
    private int number;//总的题目数量
    private int position;//答题下标
    private boolean isSubmit;//是否提交过
    private boolean isSave;//保存的周期和应用的生命周期一样长（暂时先这样）


    @Override
    public int getContentViewId() {
        return R.layout.activity_student_test;
    }


    @Override
    public void initIntent(Intent intent) {
        super.initIntent(intent);
        Bundle bundle = intent.getBundleExtra("bundle");
        if (bundle != null){
            bean = (StudentsBean)bundle.getSerializable("bean");
            id = bundle.getString("id");
            time = bundle.getString("time");
        }
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        toolbarNameTv.setText("学号：8888-病人采集");
        //试题
        recyclerViewTopic.setLayoutManager(new LinearLayoutManager(this));
        examinationTopicAdapter = new ExaminationTopicAdapter(this);
        recyclerViewTopic.setAdapter(examinationTopicAdapter);
//        recyclerViewTopic.setNestedScrollingEnabled(false);

        //试题答案
        recyclerViewAnswer.setLayoutManager(new GridLayoutManager(this, 5));
        answerAdapter = new StudentTestAnswerAdapter(this);
        recyclerViewAnswer.setNestedScrollingEnabled(false);
        answerAdapter.setClickAnswerListener(new ClickAnswerListener() {
            @Override
            public void onClick(int position) {
                setAnswerPosition(position);
            }
        });

        loadTestList();
    }

    private void loadTestList() {
        YiXiuGeApi api = new YiXiuGeApi("app/assess_quest");
        api.addParams("id",id);
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<NoPageListBean<StudentTestBean>>() {
            @Override
            public void onResponse(NoPageListBean<StudentTestBean> response) {
                if (isFinishing()){
                    return;
                }
                list = response.data;
                if (StringUtils.isEmpty(list)){
                    return;
                }
                beanList = new ArrayList<>();
                for (StudentTestBean bean:list){
                    StudentTestAnswerBean answerBean = new StudentTestAnswerBean();
                    answerBean.setType(bean.getType());
                    beanList.add(answerBean);
                }
                answerAdapter.setData(beanList);
                recyclerViewAnswer.setAdapter(answerAdapter);
            }
        });
    }

    //
    private void setAnswerPosition(int position) {
        this.position = position;
//        nextBt.setText((position + 1 == number) ? R.string.submit : R.string.next_topic);
//        menuTv.setText(String.format(getString(R.string.examination_num), String.valueOf(position + 1), String.valueOf(number)));
//        ExaminationBean.ExamBean examBean = list.get(position);
//        examinationTopicAdapter.setData(examBean.getList());
//        boolean model = StringUtils.isSame(examBean.getModel(), CommonUtils.isTwo);//2多选1单选
//        titleTv.setText(String.format(getString(R.string.topic_title), String.valueOf((position + 1)), examBean.getTitle(), model ? getString(R.string.multiple_choice) : getString(R.string.single_choice)));
//        examinationTopicAdapter.setMultiterm(model);//是否是多选题
//        examinationTopicAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.on_a_bt, R.id.next_bt, R.id.back_iv})
    public void onViewClicked(View view) {
        if (StringUtils.isEmpty(bean)) {
            return;
        }
        switch (view.getId()) {
            case R.id.on_a_bt:
                if (position == 0) {
                    break;
                }
                position -= 1;
                setAnswerPosition(position);
                break;
            case R.id.next_bt:
                if (position == number - 1) {
                    break;
                }
                position += 1;
                setAnswerPosition(position);//nextBt
                break;
            case R.id.back_iv:
                finish();
                break;
        }
    }

}
