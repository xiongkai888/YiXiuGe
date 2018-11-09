package com.lanmei.yixiu.ui.teacher.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.YiXiuApp;
import com.lanmei.yixiu.adapter.StudentTestAnswerAdapter;
import com.lanmei.yixiu.api.YiXiuGeApi;
import com.lanmei.yixiu.bean.StudentTestAnswerBean;
import com.lanmei.yixiu.bean.StudentTestBean;
import com.lanmei.yixiu.bean.StudentsBean;
import com.lanmei.yixiu.event.TestFinishEvent;
import com.lanmei.yixiu.event.TestTimeEvent;
import com.lanmei.yixiu.helper.ClickAnswerListener;
import com.lanmei.yixiu.helper.SimpleTextWatcher;
import com.lanmei.yixiu.ui.teacher.service.TestService;
import com.lanmei.yixiu.utils.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.JsonUtil;
import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.OptionPicker;

/**
 * 学生线下考试试题
 */
public class StudentTestActivity extends BaseActivity {


    @InjectView(R.id.toolbar_name_tv)
    TextView toolbarNameTv;
    @InjectView(R.id.time_tv)
    TextView timeTv;
    @InjectView(R.id.menu_tv)
    TextView menuTv;
    @InjectView(R.id.next_bt)
    Button nextBt;//下一题
    @InjectView(R.id.title_tv)
    TextView titleTv;//题目
    @InjectView(R.id.ll_bt)
    LinearLayout llBt;//上一题下一题按钮
    @InjectView(R.id.recyclerView_answer)
    RecyclerView recyclerViewAnswer;//答案
    StudentTestAnswerAdapter answerAdapter;//答案

    @InjectView(R.id.right_tv)
    TextView rightTv;//对
    @InjectView(R.id.wrong_tv)
    TextView wrongTv;//错
    @InjectView(R.id.ll_judge)
    LinearLayout llJudge;//判断题
    @InjectView(R.id.score_tv)
    TextView scoreTv;//打分题分数
    @InjectView(R.id.score_range_tv)
    TextView scoreRangeTv;//打分题分数范围
    @InjectView(R.id.ll_mark)
    LinearLayout llMark;//打分题
    @InjectView(R.id.ll_remark)
    LinearLayout llRemark;//备注
    @InjectView(R.id.remark_et)
    EditText remarkEt;//备注

    private StudentsBean bean;//学生信息
    private String id;//评估id
    private String time;//评估时间
    private String title;//评估标题

    private List<StudentTestBean> list;//考试题目列表
    private List<StudentTestAnswerBean> beanList;//答案列表
    private int number;//总的题目数量
    private int index;//答题下标
    private OptionPicker picker;//分数选择器
    private boolean isSubmit;//是否提交了
    private int timeRemaining = 0;//考试剩余时间


    @Override
    public int getContentViewId() {
        return R.layout.activity_student_test;
    }


    @Override
    public void initIntent(Intent intent) {
        super.initIntent(intent);
        Bundle bundle = intent.getBundleExtra("bundle");
        if (bundle != null) {
            bean = (StudentsBean) bundle.getSerializable("bean");
            id = bundle.getString("id");
            time = bundle.getString("time");
            title = bundle.getString("title");
        }
    }

    private void initPicker(List<String> list) {
        picker = new OptionPicker(this, list);
        picker.setOffset(3);
        picker.setSelectedIndex(1);
        picker.setTextSize(18);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                scoreTv.setText(String.format(getString(R.string.score), item));
                setAnswer(item);
            }
        });
    }


    private void setAnswer(String answer) {
        StudentTestAnswerBean answerBean = beanList.get(index);
        if (StringUtils.isSame(answer, answerBean.getScore())) {
            return;
        }
        answerBean.setScore(answer);
        answerAdapter.notifyItemChanged(index);
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);

        beanList = YiXiuApp.getInstance().getTestAnswerBean(bean.getUid());

        toolbarNameTv.setText("学号：" + bean.getUid() + "-" + title);
        //试题答案
        recyclerViewAnswer.setLayoutManager(new GridLayoutManager(this, 5));
        answerAdapter = new StudentTestAnswerAdapter(this);
        recyclerViewAnswer.setNestedScrollingEnabled(false);
        answerAdapter.setClickAnswerListener(new ClickAnswerListener() {
            @Override
            public void onClick(int position) {
                if (index == position || isSubmit) {
                    return;
                }
                setAnswerPosition(position);
            }
        });

        remarkEt.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!StringUtils.isEmpty(beanList)) {
                    beanList.get(index).setText(s + "");
                }
            }
        });

        loadTestList();
    }

    private void loadTestList() {
        YiXiuGeApi api = new YiXiuGeApi("app/assess_quest");
        api.addParams("id", id);
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<NoPageListBean<StudentTestBean>>() {
            @Override
            public void onResponse(NoPageListBean<StudentTestBean> response) {
                if (isFinishing()) {
                    return;
                }
                list = response.data;
                if (StringUtils.isEmpty(list)) {
                    return;
                }
                boolean hasJudge = false;
                number = list.size();
                if (StringUtils.isEmpty(beanList)) {
                    beanList = new ArrayList<>();
                    for (StudentTestBean bean : list) {
                        StudentTestAnswerBean answerBean = new StudentTestAnswerBean();
                        if (!hasJudge && StringUtils.isSame(CommonUtils.isTwo, bean.getType())) {
                            hasJudge = true;//有打分题
                        }
                        answerBean.setType(bean.getType());
                        beanList.add(answerBean);
                    }
                    answerAdapter.setData(beanList);
                    recyclerViewAnswer.setAdapter(answerAdapter);
                    Intent intent = new Intent(getContext(), TestService.class);
                    intent.putExtra("testTime", time);
                    intent.putExtra("bean", bean);
                    startService(intent);
                } else {
                    answerAdapter.setData(beanList);
                    recyclerViewAnswer.setAdapter(answerAdapter);
                    timeTv.setText("考试已超时");
                }
                llBt.setVisibility(View.VISIBLE);
                setAnswerPosition(0);
            }
        });
    }

    //
    private void setAnswerPosition(int position) {
        this.index = position;
        nextBt.setText((position + 1 == number) ? R.string.submit : R.string.next_topic);
        menuTv.setText(String.format(getString(R.string.examination_num), String.valueOf(position + 1), String.valueOf(number)));
        StudentTestBean bean = list.get(position);
        boolean isJudge = StringUtils.isSame(CommonUtils.isTwo, bean.getType());
        String title = isJudge ? bean.getTitle1() + bean.getTitle2() + bean.getTitle() : bean.getTitle();
        llJudge.setVisibility(isJudge ? View.GONE : View.VISIBLE);
        llMark.setVisibility(isJudge ? View.VISIBLE : View.GONE);
        titleTv.setText(String.format(getString(R.string.topic_title1), String.valueOf((position + 1)), title));
        StudentTestAnswerBean answerBean = beanList.get(position);
        String answer = answerBean.getScore();
        if (!isJudge) {//判断题
            if (StringUtils.isEmpty(answer)) {
                rightTv.setBackgroundResource(R.drawable.circle_topic_off);
                wrongTv.setBackgroundResource(R.drawable.circle_topic_off);
            } else {
                setRightOrWrong(StringUtils.isSame(CommonUtils.isOne, answer) ? rightTv : wrongTv, answer);
            }
        } else {//打分题
            initPicker(getMarkList(bean.getMin_mark(), bean.getMax_mark()));
            scoreTv.setText(StringUtils.isEmpty(answer) ? String.format(getString(R.string.score), bean.getMin_mark()) : String.format(getString(R.string.score), answer));
            scoreRangeTv.setText(String.format(getString(R.string.score_range), bean.getMax_mark(), bean.getMin_mark()));
        }
        remarkEt.setText(answerBean.getText());
    }

    @OnClick({R.id.on_a_bt, R.id.next_bt, R.id.back_iv, R.id.ll_right, R.id.ll_wrong, R.id.score_tv})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.back_iv) {
            finish();
            return;
        }
        if (StringUtils.isEmpty(list) || isSubmit) {
            return;
        }
        switch (id) {
            case R.id.on_a_bt:
                if (index == 0) {
                    break;
                }
                index -= 1;
                setAnswerPosition(index);
                break;
            case R.id.next_bt:
                if (index == number - 1) {
                    submit();
                    break;
                }
                index += 1;
                setAnswerPosition(index);//nextBt
                break;
            case R.id.ll_right:
                setRightOrWrong(rightTv, CommonUtils.isOne);
                break;
            case R.id.ll_wrong:
                setRightOrWrong(wrongTv, CommonUtils.isZero);
                break;
            case R.id.score_tv:
                if (picker != null) {
                    picker.show();
                }
                break;
        }
    }

    private void submit() {
        if (!isPerfect()) {
            UIHelper.ToastMessage(this, "请先完成学生评估内容再提交");
            return;
        }
        YiXiuGeApi api = new YiXiuGeApi("app/assess_submit");
        api.addParams("result", JsonUtil.getJSONArrayByList(beanList));//评估结果 score=>分数，text=>备注
        api.addParams("sid", bean.getUid());//学生id
        api.addParams("uid", api.getUserId(this));//教师id
        api.addParams("id", id);//评估id
        int stipulatetime = Integer.valueOf(time) * 60;
        int total = stipulatetime - timeRemaining;
        api.addParams("overtime", (total > stipulatetime) ? 1 : 0);//评估id
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
                isSubmit = true;
                UIHelper.ToastMessage(getContext(), response.getMsg());
                YiXiuApp.getInstance().removeTestAnswerBean(bean.getUid());
                llJudge.setVisibility(View.GONE);
                llMark.setVisibility(View.GONE);
                llRemark.setVisibility(View.GONE);
                llBt.setVisibility(View.GONE);
                answerAdapter.setSubmit(isSubmit);
                answerAdapter.notifyDataSetChanged();
                int total = Integer.valueOf(time) * 60 - timeRemaining;
                titleTv.setText("用时：" + getTotalTime(total) + "  " + "得分：" + getScore());

                EventBus.getDefault().post(new TestFinishEvent());
            }
        });
    }


    private String getTotalTime(int s) {

        int minute = s / 60;
        int second = s % 60;

        return minute + "分" + second + "秒";
    }


    private String getScore() {
        int score = 0;
        for (StudentTestAnswerBean bean : beanList) {
            score += Integer.valueOf(bean.getScore());
        }
        return score + "";
    }

    //学生是否答完题
    private boolean isPerfect() {
        for (StudentTestAnswerBean bean : beanList) {
            if (StringUtils.isEmpty(bean.getScore())) {
                return false;
            }
        }
        return true;
    }

    private List<String> getMarkList(String min_mark, String max_mark) {
        List<String> list = new ArrayList<>();
        int min = Integer.valueOf(min_mark);
        int max = Integer.valueOf(max_mark);
        for (int i = min; i < max + 1; i++) {
            list.add(String.valueOf(i));
        }
        return list;
    }

    private void setRightOrWrong(TextView view, String answer) {
        rightTv.setBackgroundResource(R.drawable.circle_topic_off);
        wrongTv.setBackgroundResource(R.drawable.circle_topic_off);

        view.setBackgroundResource(R.drawable.circle_topic_on);
        setAnswer(answer);
    }

    @Subscribe
    public void testTimeEvent(TestTimeEvent event) {
        L.d(L.TAG, event.getTime());
        timeRemaining = event.getSecond();
        timeTv.setText(String.format(getString(R.string.count_down), event.getTime()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (!isSubmit) {
            YiXiuApp.getInstance().saveTestAnswerBean(bean.getUid(), beanList);
        } else {
            YiXiuApp.getInstance().removeTestAnswerBean(bean.getUid());
        }
    }
}
