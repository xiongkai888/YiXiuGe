package com.medui.yixiu.ui.teacher.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.medui.yixiu.R;
import com.medui.yixiu.YiXiuApp;
import com.medui.yixiu.adapter.StudentTestAnswerAdapter;
import com.medui.yixiu.api.YiXiuGeApi;
import com.medui.yixiu.bean.StudentTestAnswerBean;
import com.medui.yixiu.bean.StudentTestBean;
import com.medui.yixiu.bean.StudentsBean;
import com.medui.yixiu.event.TestFinishEvent;
import com.medui.yixiu.event.TestTimeEvent;
import com.medui.yixiu.helper.StudentsTestTopicHelper;
import com.medui.yixiu.ui.teacher.service.TestService;
import com.medui.yixiu.utils.AKDialog;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.JsonUtil;
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
 * 学生线下考试试题(修改后)
 */
public class StudentTestSubActivity extends BaseActivity {


    @InjectView(R.id.toolbar_name_tv)
    TextView toolbarNameTv;
    @InjectView(R.id.time_tv)
    TextView timeTv;
    @InjectView(R.id.menu_tv)
    TextView menuTv;

    @InjectView(R.id.ll_root)
    LinearLayout root;

    @InjectView(R.id.recyclerView_answer)
    RecyclerView recyclerViewAnswer;//答案
    StudentTestAnswerAdapter answerAdapter;//答案

    private StudentsBean bean;//学生信息
    private String id;//评估id
    private String time;//评估时间
    private String title;//评估标题

    private List<StudentTestBean> list;//考试题目列表
    private List<StudentTestAnswerBean> beanList;//答案列表
    private OptionPicker picker;//分数选择器
    private boolean isSubmit;//是否提交了
    private int timeRemaining = 0;//考试剩余时间
    private boolean isOverTime = true;

    private StudentsTestTopicHelper testTopicHelper;

    @Override
    public int getContentViewId() {
        return R.layout.activity_student_test_sub;
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

    private void initPicker(List<String> list, final int position) {
        picker = new OptionPicker(this, list);
        picker.setOffset(3);
        picker.setSelectedIndex(1);
        picker.setTextSize(18);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                testTopicHelper.setDataByPosition(item, position);
            }
        });
        picker.show();
    }


    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        menuTv.setText(R.string.submit);
        testTopicHelper = new StudentsTestTopicHelper(this, root);
        testTopicHelper.setPickListener(new StudentsTestTopicHelper.PickListener() {
            @Override
            public void setMarkList(String min_mark, String max_mark, int position) {
                if (isSubmit) {
                    return;
                }
                initPicker(getMarkList(min_mark, max_mark), position);
            }
        });
        beanList = YiXiuApp.getInstance().getTestAnswerBean(bean.getUid());
        toolbarNameTv.setText("学号：" + bean.getUid() + "-" + title);
        //试题答案
        recyclerViewAnswer.setLayoutManager(new GridLayoutManager(this, 5));
        answerAdapter = new StudentTestAnswerAdapter(this);
        recyclerViewAnswer.setNestedScrollingEnabled(false);
//        answerAdapter.setClickAnswerListener(new ClickAnswerListener() {
//            @Override
//            public void onClick(int position) {
//
//            }
//        });
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
                if (StringUtils.isEmpty(beanList)) {
                    beanList = new ArrayList<>();
                    for (StudentTestBean bean : list) {
                        StudentTestAnswerBean answerBean = new StudentTestAnswerBean();
                        answerBean.setType(bean.getType());
                        answerBean.setId(bean.getId());
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
                    timeTv.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (isFinishing()) {
                                return;
                            }
                            if (isOverTime) {
                                timeTv.setText("考试已超时");
                            }
                        }
                    }, 1200);
                }

                testTopicHelper.setBeanList(beanList);
                testTopicHelper.setData(list);
                testTopicHelper.setAnswerAdapter(answerAdapter);
            }
        });
    }

    private void submit() {
        YiXiuGeApi api = new YiXiuGeApi("app/assess_submit");
        api.addParams("result", JsonUtil.getJSONArrayByList(beanList));//评估结果 score=>分数，text=>备注
        api.addParams("sid", bean.getUid());//学生id
        api.addParams("uid", api.getUserId(this));//教师id
        api.addParams("id", id);//评估id
        api.addParams("grade", (grade+1));//1|2|3|4=>不通过|待定|通过|优秀
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
                answerAdapter.setSubmit(isSubmit);
                answerAdapter.notifyDataSetChanged();
                int total = Integer.valueOf(time) * 60 - timeRemaining;
                EventBus.getDefault().post(new TestFinishEvent());
                timeTv.setText("考试结束\u3000用时："+getTotalTime(total) + "\u3000" + "得分：" + getScore());
                menuTv.setVisibility(View.GONE);
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

    /**
     * 设置最大、最小分数
     * @param min_mark
     * @param max_mark
     * @return
     */
    private List<String> getMarkList(String min_mark, String max_mark) {
        List<String> list = new ArrayList<>();
        int min = Integer.valueOf(min_mark);
        int max = Integer.valueOf(max_mark);
        for (int i = min; i < max + 1; i++) {
            list.add(String.valueOf(i));
        }
        return list;
    }


    //测试时间（）
    @Subscribe
    public void testTimeEvent(TestTimeEvent event) {
        timeRemaining = event.getSecond();
//        L.d(L.TAG, event.getTime()+","+timeRemaining);
        timeTv.setText(String.format(getString(R.string.count_down), event.getTime()));
        isOverTime = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!isSubmit) {
            YiXiuApp.getInstance().saveTestAnswerBean(bean.getUid(), beanList);
        } else {
            YiXiuApp.getInstance().removeTestAnswerBean(bean.getUid());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    private int grade;

    @OnClick({R.id.back_iv, R.id.menu_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                onBackPressed();
                break;
            case R.id.menu_tv:
                if (!isPerfect()) {
                    UIHelper.ToastMessage(this, "请先完成学生评估内容再提交");
                    return;
                }
                AKDialog.getSingleChoiceDialog(this, "是否通过?", new String[]{"不通过", "待定", "通过", "优秀"}, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      grade = which;
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        submit();
                    }
                }).show();
                break;
        }
    }
}
