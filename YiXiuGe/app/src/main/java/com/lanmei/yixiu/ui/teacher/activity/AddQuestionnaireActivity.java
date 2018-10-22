package com.lanmei.yixiu.ui.teacher.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.lanmei.yixiu.R;
import com.lanmei.yixiu.api.YiXiuGeApi;
import com.lanmei.yixiu.bean.QuestionnaireSubjectBean;
import com.lanmei.yixiu.bean.SelectQuestionStudentsBean;
import com.lanmei.yixiu.event.AddQuestionnaireEvent;
import com.lanmei.yixiu.event.AddQuestionnaireSubjectEvent;
import com.lanmei.yixiu.event.SelectQuestionStudentsEvent;
import com.lanmei.yixiu.utils.CommonUtils;
import com.lanmei.yixiu.utils.FormatTime;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.JsonUtil;
import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.text.ParseException;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DateTimePicker;

/**
 * 添加问卷
 */
public class AddQuestionnaireActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.title_et)
    EditText titleEt;
    @InjectView(R.id.student_num_tv)
    TextView studentNumTv;
    @InjectView(R.id.notice_et)
    EditText noticeEt;
    @InjectView(R.id.question_num_tv)
    TextView questionNumTv;
    @InjectView(R.id.start_time_tv)
    TextView startTimeTv;
    @InjectView(R.id.end_time_tv)
    TextView endTimeTv;

    private long startTime;
    private long endTime;
    private List<SelectQuestionStudentsBean.StudentBean> studentBeanList;//选择的学生
    private List<QuestionnaireSubjectBean> questionnaireSubjectBeanList;//问卷题目
    private String cids;


    @Override
    public int getContentViewId() {
        return R.layout.activity_add_questionnaire;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.add_questionnaire);
        actionbar.setHomeAsUpIndicator(R.drawable.back);
        initDatePicker();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_submit, menu);
        return true;
    }

    private FormatTime time;
    private DateTimePicker picker;
    private int timeType;

    private void initDatePicker() {
        picker = new DateTimePicker(this, DateTimePicker.HOUR_24);
        time = new FormatTime(this);
        time.setTime(System.currentTimeMillis() / 1000 + 60);
        int year = time.getYear();
        int month = time.getMonth();
        int day = time.getDay();
        int hour = time.getHour();
        int minute = time.getMinute();
        picker.setDateRangeStart(year, month, day);
        picker.setDateRangeEnd(year + 1, month, day);
        picker.setSelectedItem(year, month, day, hour, minute);
//        picker.setLabel("年","月","日",":","");
        picker.setOnDateTimePickListener(new DateTimePicker.OnYearMonthDayTimePickListener() {
            @Override
            public void onDateTimePicked(String year, String month, String day, String hour, String minute) {
                try {
                    String timeStr = year + "-" + month + "-" + day + " " + hour + ":" + minute;
                    long timeLong = time.dateToStampLong(timeStr);
                    if (timeType == 1) {
                        if (timeLong < System.currentTimeMillis() / 1000) {
                            UIHelper.ToastMessage(getContext(), "开始时间不能小于当前时间");
                            setTimeNull();
                        } else if (endTime != 0 && timeLong > endTime) {
                            UIHelper.ToastMessage(getContext(), "开始时间不能大于当结束时间");
                            setTimeNull();
                        } else {
                            startTime = timeLong;
                            startTimeTv.setText(timeStr);
                        }
                    } else {
                        if (startTime > timeLong) {
                            UIHelper.ToastMessage(getContext(), "结束时间不能小于开始时间");
                            setTimeNull();
                        } else {
                            endTime = timeLong;
                            endTimeTv.setText(timeStr);
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    //选错时间重新选择（清空）
    public void setTimeNull() {
        startTime = 0;
        endTime = 0;
        startTimeTv.setText("");
        endTimeTv.setText("");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_submit:
                submit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void submit() {
        String title = CommonUtils.getStringByEditText(titleEt);
        if (StringUtils.isEmpty(title)) {
            UIHelper.ToastMessage(this, R.string.input_questionnaire_option);
            return;
        }
        String startTime = CommonUtils.getStringByTextView(startTimeTv);
        if (StringUtils.isEmpty(startTime)) {
            UIHelper.ToastMessage(this, "请选择开始时间");
            return;
        }
        String endTime = CommonUtils.getStringByTextView(endTimeTv);
        if (StringUtils.isEmpty(endTime)) {
            UIHelper.ToastMessage(this, "请选择结束时间");
            return;
        }
        if (StringUtils.isEmpty(studentBeanList)) {
            UIHelper.ToastMessage(this, R.string.choose_students);
            return;
        }
        JSONArray array = JsonUtil.getJSONArrayByList(questionnaireSubjectBeanList);
        JSONArray arrayPeople = JsonUtil.getJSONArrayByList(studentBeanList);

        YiXiuGeApi api = new YiXiuGeApi("app/questions_add");
        api.addParams("uid", api.getUserId(this)).addParams("title", title).addParams("number", arrayPeople)
                .addParams("content", CommonUtils.getStringByEditText(noticeEt)).addParams("quest_num", questionnaireSubjectBeanList.size())
                .addParams("cid", cids).addParams("quest", array).addParams("starttime", this.startTime)
                .addParams("endtime", this.endTime);
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
                UIHelper.ToastMessage(getContext(),response.getMsg());
                EventBus.getDefault().post(new AddQuestionnaireEvent());
                finish();
            }
        });

    }


    @OnClick({R.id.ll_student_num, R.id.ll_question_num, R.id.start_time_tv, R.id.end_time_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_student_num:
//                CommonUtils.developing(this);
                IntentUtil.startActivity(this, SelectQuestionStudentsActivity.class);
                break;
            case R.id.ll_question_num:
                Bundle bundle = new Bundle();
                bundle.putSerializable("list", (Serializable) questionnaireSubjectBeanList);
                IntentUtil.startActivity(this, AddQuestionnaireSubjectActivity.class, bundle);
                break;
            case R.id.start_time_tv:
                timeType = 1;
                picker.show();
                break;
            case R.id.end_time_tv:
                if (startTime == 0) {
                    UIHelper.ToastMessage(this, "先选择开始时间");
                    return;
                }
                timeType = 2;
                picker.show();
                break;
        }
    }

    //选择学生后调用
    @Subscribe
    public void selectQuestionStudentsEvent(SelectQuestionStudentsEvent event) {
        studentBeanList = event.getList();
        cids = event.getGetCids();
        L.d(L.TAG, cids);
        studentNumTv.setText(String.format(getString(R.string.num_person), String.valueOf(studentBeanList.size())));
    }

    //添加问卷题目后调用
    @Subscribe
    public void addquestionnaireSubjectEvent(AddQuestionnaireSubjectEvent event) {
        questionnaireSubjectBeanList = event.getList();
        questionNumTv.setText(String.format(getString(R.string.num_question), String.valueOf(questionnaireSubjectBeanList.size())));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        picker = null;
        EventBus.getDefault().unregister(this);
    }
}
