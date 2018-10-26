package com.lanmei.yixiu.ui.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lanmei.yixiu.R;
import com.lanmei.yixiu.adapter.ExaminationResultAdapter;
import com.lanmei.yixiu.api.YiXiuGeApi;
import com.lanmei.yixiu.bean.ExaminationBean;
import com.lanmei.yixiu.bean.ExaminationResultBean;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.DataBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.DoubleUtil;
import com.xson.common.utils.StringUtils;
import com.xson.common.widget.CenterTitleToolbar;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;


/**
 * 考试结果
 */
public class ExaminationResultActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    @InjectView(R.id.score_tv)
    TextView scoreTv;//得分
    @InjectView(R.id.total_points_tv)
    TextView totalPointsTv;//总分
    @InjectView(R.id.number1_tv)
    TextView number1Tv;//
    @InjectView(R.id.number2_tv)
    TextView number2Tv;//
    @InjectView(R.id.accuracy_tv)
    TextView accuracyTv;//
    @InjectView(R.id.number3_tv)
    TextView number3Tv;//


    private String id;//考试id
    private ExaminationBean bean;//考试信息
    private List<ExaminationBean.ExamBean> list;//考试题目列表
    private int number;//总的题目数量
    //    private List<ExaminationAnswerBean> beanList;//答案列表
    private List<String> studentAnswerList;//学生的答案
    private int rightNum;//答对题目数
    private int totalPoints;//试题总fen
    private int score;//考试得分
    private int scoreTopic;//每一题分数
    private double accuracy;//正确率


    @Override
    public int getContentViewId() {
        return R.layout.activity_examination_result;
    }


    @Override
    public void initIntent(Intent intent) {
        super.initIntent(intent);
        id = intent.getStringExtra("value");
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.examination_result);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        if (StringUtils.isEmpty(id)) {
            return;
        }

        YiXiuGeApi api = new YiXiuGeApi("app/examre");
        api.addParams("uid", api.getUserId(this)).addParams("id", id);
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<DataBean<ExaminationBean>>() {
            @Override
            public void onResponse(DataBean<ExaminationBean> response) {
                if (isFinishing()) {
                    return;
                }
                bean = response.data;
                if (StringUtils.isEmpty(bean)) {
                    return;
                }
                scoreTopic = Integer.valueOf(bean.getScore());
                list = bean.getExam();
                if (StringUtils.isEmpty(list)) {
                    return;
                }
                number = list.size();
                totalPoints = scoreTopic * number;
                studentAnswerList = JSON.parseArray(bean.getResult(), String.class);
                if (!StringUtils.isEmpty(list) && !StringUtils.isEmpty(studentAnswerList) && number == studentAnswerList.size()) {
                    recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 10));
                    ExaminationResultAdapter  examinationResultAdapter = new ExaminationResultAdapter(getContext());
                    examinationResultAdapter.setData(getList());
                    recyclerView.setAdapter(examinationResultAdapter);
                    recyclerView.setNestedScrollingEnabled(false);
                }
                accuracy = DoubleUtil.divide((rightNum*100*1.0),number*1.0);
                scoreTv.setText(String.valueOf(score));
                totalPointsTv.setText(String.format(getString(R.string.total_points),String.valueOf(totalPoints)));
                number1Tv.setText(String.format(getString(R.string.number1),String.valueOf(number)));
                number2Tv.setText(String.format(getString(R.string.number2),String.valueOf(number)));
                BigDecimal bd =new BigDecimal(accuracy).setScale(0, BigDecimal.ROUND_HALF_UP);
                accuracyTv.setText(String.format(getString(R.string.accuracy),Integer.parseInt(bd.toString())+"%"));
                number3Tv.setText(String.format(getString(R.string.number3),String.valueOf(number),String.valueOf(rightNum)));
//                for (String s : studentAnswerList) {
//                    L.d(L.TAG, s);
//                }
            }
        });
    }

    private List<ExaminationResultBean> getList() {
        List<ExaminationResultBean> list = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            ExaminationResultBean bean = new ExaminationResultBean();
            boolean b = StringUtils.isSame(this.list.get(i).getAnswer(), studentAnswerList.get(i));
            bean.setRight(b);
            if (b) {
                rightNum += 1;
                score += scoreTopic;
            }
            list.add(bean);
        }
        return list;
    }

}
