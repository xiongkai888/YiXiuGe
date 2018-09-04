package com.lanmei.yixiu.ui.home.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.adapter.NoticeAdapter;
import com.lanmei.yixiu.api.YiXiuGeApi;
import com.lanmei.yixiu.bean.NoticeBean;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import butterknife.InjectView;

/**
 * 公告通知
 */
public class NoticeActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    NoticeAdapter mAdapter;
    private SwipeRefreshController<NoPageListBean<NoticeBean>> controller;

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
        actionbar.setTitle(R.string.notice);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        YiXiuGeApi api = new YiXiuGeApi("app/notice");
        api.addParams("cid", 7);

        mAdapter = new NoticeAdapter(this);
        smartSwipeRefreshLayout.initWithLinearLayout();
        smartSwipeRefreshLayout.setAdapter(mAdapter);
        controller = new SwipeRefreshController<NoPageListBean<NoticeBean>>(this, smartSwipeRefreshLayout, api, mAdapter) {
        };
        controller.loadFirstPage();
    }
}
