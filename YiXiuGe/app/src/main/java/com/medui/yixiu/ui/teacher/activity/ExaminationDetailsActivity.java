package com.medui.yixiu.ui.teacher.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.medui.yixiu.R;
import com.medui.yixiu.adapter.ExaminationDetailsListAdapter;
import com.medui.yixiu.api.YiXiuGeApi;
import com.medui.yixiu.bean.ExaminationDetailsBean;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import butterknife.InjectView;

/**
 * 考试详情(教师端)
 */
public class ExaminationDetailsActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    private SwipeRefreshController<NoPageListBean<ExaminationDetailsBean>> controller;


    @Override
    public int getContentViewId() {
        return R.layout.activity_single_listview_no;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.examination_details);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        String id = getIntent().getStringExtra("value");

        YiXiuGeApi api = new YiXiuGeApi("app/examination");
//        api.addParams("uid",api.getUserId(this));
        api.addParams("id",id);
//        api.addParams("id", 13);

        ExaminationDetailsListAdapter adapter = new ExaminationDetailsListAdapter(this);
        smartSwipeRefreshLayout.initWithLinearLayout();
        smartSwipeRefreshLayout.setAdapter(adapter);
        controller = new SwipeRefreshController<NoPageListBean<ExaminationDetailsBean>>(this, smartSwipeRefreshLayout, api, adapter) {
        };
        smartSwipeRefreshLayout.setMode(SmartSwipeRefreshLayout.Mode.NO_PAGE);
        controller.loadFirstPage();
    }

}
