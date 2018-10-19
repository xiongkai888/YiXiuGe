package com.lanmei.yixiu.ui.teacher.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.utils.CommonUtils;
import com.lanmei.yixiu.utils.FormatTime;
import com.xson.common.app.BaseActivity;
import com.xson.common.utils.IntentUtil;
import com.xson.common.widget.CenterTitleToolbar;

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


    @Override
    public int getContentViewId() {
        return R.layout.activity_add_questionnaire;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
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
                if (timeType == 1) {
                    startTimeTv.setText(year + "-" + month + "-" + day + " " + hour + ":" + minute);
                } else {
                    endTimeTv.setText(year + "-" + month + "-" + day + " " + hour + ":" + minute);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_submit:
                CommonUtils.developing(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.ll_student_num, R.id.ll_question_num,R.id.start_time_tv,R.id.end_time_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_student_num:
                CommonUtils.developing(this);
//                IntentUtil.startActivity(this, SelectQuestionStudentsActivity.class);
                break;
            case R.id.ll_question_num:
                IntentUtil.startActivity(this, AddQuestionnaireSubjectActivity.class);
                break;
            case R.id.start_time_tv:
                timeType = 1;
                picker.show();
                break;
            case R.id.end_time_tv:
                timeType = 2;
                picker.show();
                break;
        }
    }

}
