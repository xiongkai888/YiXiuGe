package com.medui.yixiu.ui.mine.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.TextView;

import com.medui.yixiu.R;
import com.medui.yixiu.adapter.CheckInAdapter;
import com.medui.yixiu.api.YiXiuGeApi;
import com.medui.yixiu.bean.MyCheckingBean;
import com.medui.yixiu.utils.FormatTime;
import com.othershe.calendarview.utils.SolarUtil;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.utils.StringUtils;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 我的考勤（学生）
 */
public class MyCheckingInActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.time_tv)
    TextView timeTv;
    private FormatTime formatTime;
    private YiXiuGeApi api;
    private int year;
    private int month;
    private String currentTime;//
    private int monthDays;
    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    CheckInAdapter mAdapter;
    private SwipeRefreshController<NoPageListBean<MyCheckingBean>> controller;

    @Override
    public int getContentViewId() {
        return R.layout.activity_checking_in;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.my_checking_in);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        formatTime = new FormatTime(this);
        formatTime.setApplyToTimeYearMonthDay();
        year = formatTime.getYear();
        month = formatTime.getMonth();

        currentTime = year+"-"+month;

        timeTv.setText(getString(R.string.year_month,String.valueOf(year),String.valueOf(month)));

        monthDays = SolarUtil.getMonthDays(year, month);

        api = new YiXiuGeApi("app/my_attend");
        api.addParams("uid", api.getUserId(this));

        try {
            api.addParams("start_time", formatTime.dateToStampLongSub(year + "-" + month + "-" + 1));
            api.addParams("end_time", formatTime.dateToStampLongSub(year + "-" + month + "-" + monthDays) + 86400);
            mAdapter = new CheckInAdapter(this);
            smartSwipeRefreshLayout.initWithLinearLayout();
            smartSwipeRefreshLayout.setAdapter(mAdapter);
            controller = new SwipeRefreshController<NoPageListBean<MyCheckingBean>>(this, smartSwipeRefreshLayout, api, mAdapter) {
            };
            smartSwipeRefreshLayout.setMode(SmartSwipeRefreshLayout.Mode.NO_PAGE);
            controller.loadFirstPage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @OnClick({R.id.last_month_iv, R.id.next_month_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.last_month_iv:
                String[] strings = formatTime.getMonthAgoOrNext(year,month,false);
                if (!StringUtils.isEmpty(strings) && strings.length == 2){
                    loadcheckingIn(Integer.valueOf(strings[0]),Integer.valueOf(strings[1]));
                }
                break;
            case R.id.next_month_iv:
                if (currentTime.contains(year+"-"+month)){
                    return;
                }
                String[] string = formatTime.getMonthAgoOrNext(year,month,true);
                if (!StringUtils.isEmpty(string) && string.length == 2){
                    loadcheckingIn(Integer.valueOf(string[0]),Integer.valueOf(string[1]));
                }
                break;
        }
    }


    public void loadcheckingIn(int year,int month){
        this.year = year;
        this.month = month;
        monthDays = SolarUtil.getMonthDays(year, month);
        timeTv.setText(getString(R.string.year_month,String.valueOf(year),String.valueOf(month)));
        try {
            api.addParams("start_time", formatTime.dateToStampLongSub(year + "-" + month + "-" + 1));
            api.addParams("end_time", formatTime.dateToStampLongSub(year + "-" + month + "-" + monthDays) + 86400);
            controller.loadFirstPage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
