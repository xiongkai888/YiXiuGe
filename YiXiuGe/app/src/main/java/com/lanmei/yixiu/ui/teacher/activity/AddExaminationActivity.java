package com.lanmei.yixiu.ui.teacher.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.utils.CommonUtils;
import com.lanmei.yixiu.utils.FormatTime;
import com.lanmei.yixiu.widget.KaiSpinner;
import com.xson.common.app.BaseActivity;
import com.xson.common.utils.IntentUtil;
import com.xson.common.widget.CenterTitleToolbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DateTimePicker;

/**
 * 添加考试
 */
public class AddExaminationActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.spinner)
    KaiSpinner mSpinner;
    DateTimePicker picker;
    FormatTime time;
    @InjectView(R.id.start_time_tv)
    TextView startTimeTv;
    @InjectView(R.id.end_time_tv)
    TextView endTimeTv;
    private int timeType;

    @Override
    public int getContentViewId() {
        return R.layout.activity_add_examination;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.add_examination);
        actionbar.setHomeAsUpIndicator(R.drawable.back);
        List<String> list = new ArrayList<>();
        list.add("随堂测试");
        list.add("综合考试");

        setSpinner(list);
        initDatePicker();
    }

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

    private void setSpinner(List<String> list) {
        mSpinner.setListData(list);
        mSpinner.setOnItemSelectedListener(new KaiSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(String itemStr) {
//                UIHelper.ToastMessage(getContext(), itemStr);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_submit, menu);
        return true;
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


    @OnClick({R.id.start_time_tv, R.id.end_time_tv,R.id.ll_exam_topics,R.id.ll_select_student})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.start_time_tv:
                timeType = 1;
                picker.show();
                break;
            case R.id.end_time_tv:
                timeType = 2;
                picker.show();
                break;
            case R.id.ll_select_student://选择学生
                IntentUtil.startActivity(this,SelectStudentsActivity.class);
                break;
            case R.id.ll_exam_topics://选择考试题目
                IntentUtil.startActivity(this,ExamTopicsActivity.class);
                break;
        }
    }
}
