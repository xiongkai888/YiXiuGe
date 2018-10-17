package com.lanmei.yixiu.ui.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.adapter.ClassDetailsAdapter;
import com.lanmei.yixiu.api.YiXiuGeApi;
import com.lanmei.yixiu.bean.ClassDetailsBean;
import com.lanmei.yixiu.utils.FormatTime;
import com.othershe.calendarview.bean.DateBean;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import java.text.ParseException;

import butterknife.InjectView;

/**
 * 课程详情
 */
public class ClassDetailsActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    ClassDetailsAdapter mAdapter;
    private SwipeRefreshController<NoPageListBean<ClassDetailsBean>> controller;
    private DateBean date;
    private int currentPosition;

    @Override
    public int getContentViewId() {
        return R.layout.activity_single_listview_no;
    }

    @Override
    public void initIntent(Intent intent) {
        super.initIntent(intent);
        Bundle bundle = intent.getBundleExtra("bundle");
        if (bundle != null){
            date = (DateBean)bundle.getSerializable("bean");
            currentPosition = bundle.getInt("position");
        }
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.class_details);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        if (date == null){
            return;
        }
        FormatTime formatTime = new FormatTime(this);
        formatTime.setApplyToTimeYearMonthDay();
        YiXiuGeApi api = new YiXiuGeApi("app/syllabus");
        api.addParams("uid", api.getUserId(this));
        try {
            api.addParams("start_time", formatTime.dateToStampLongSub(date.getSolar()[0]+"-"+date.getSolar()[1]+"-"+date.getSolar()[2]));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mAdapter = new ClassDetailsAdapter(this,date,currentPosition);
        smartSwipeRefreshLayout.initWithLinearLayout();
        smartSwipeRefreshLayout.setAdapter(mAdapter);
        controller = new SwipeRefreshController<NoPageListBean<ClassDetailsBean>>(this, smartSwipeRefreshLayout, api, mAdapter) {
        };
        smartSwipeRefreshLayout.setMode(SmartSwipeRefreshLayout.Mode.NO_PAGE);
        controller.loadFirstPage();
    }




}
