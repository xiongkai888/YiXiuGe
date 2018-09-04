package com.lanmei.yixiu.ui.mine.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.TextView;

import com.lanmei.yixiu.R;
import com.othershe.calendarview.bean.DateBean;
import com.othershe.calendarview.listener.OnPagerChangeListener;
import com.othershe.calendarview.listener.OnSingleChooseListener;
import com.othershe.calendarview.weiget.CalendarView;
import com.xson.common.app.BaseActivity;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.L;
import com.xson.common.widget.CenterTitleToolbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 我的课程表
 */
public class MyClassScheduleActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.calendar)
    CalendarView calendarView;
    @InjectView(R.id.title)
    TextView title;

    @Override
    public int getContentViewId() {
        return R.layout.activity_class_schedule;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.my_class_schedule);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        List<String> list = new ArrayList<>();
        list.add("2018.8.6");
        list.add("2018.8.7");
        list.add("2018.8.8");
        list.add("2018.8.9");
        list.add("2018.8.10");
        list.add("2018.8.13");
        list.add("2018.8.14");
        list.add("2018.8.15");
        list.add("2018.8.16");
        list.add("2018.8.17");

        calendarView.setStartEndDate("2018.6", "2018.12")
                .setDisableStartEndDate("2018.7.1", "2018.11.30")
                .setInitDate("2018.8")
                .setMultiDate(list)
                .init();

        title.setText(2018 + "年" + 8 + "月");

        calendarView.setOnSingleChooseListener(new OnSingleChooseListener() {
            @Override
            public void onSingleChoose(View view, DateBean date) {
//                TextView solarDay = (TextView) view.findViewById(com.othershe.calendarview.R.id.solar_day);
//                TextView lunarDay = (TextView) view.findViewById(com.othershe.calendarview.R.id.lunar_day);
//                solarDay.setText("111");
//                lunarDay.setText("222");
                IntentUtil.startActivity(getContext(),ClassDetailsActivity.class);
            }
        });

        calendarView.setOnPagerChangeListener(new OnPagerChangeListener() {
            @Override
            public void onPagerChanged(int[] date) {
                L.d(L.TAG, date[0] + "年" + date[1] + "月");
                title.setText(date[0] + "年" + date[1] + "月");
            }
        });

    }


    @OnClick({R.id.last_month_iv, R.id.next_month_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.last_month_iv:
                calendarView.lastMonth();
                break;
            case R.id.next_month_iv:
                calendarView.nextMonth();
                break;
        }
    }
}
