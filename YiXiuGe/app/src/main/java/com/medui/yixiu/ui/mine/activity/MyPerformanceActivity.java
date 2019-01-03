package com.medui.yixiu.ui.mine.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.medui.yixiu.R;
import com.medui.yixiu.adapter.MyPerformanceAdapter;
import com.medui.yixiu.api.YiXiuGeApi;
import com.medui.yixiu.bean.PerformanceBean;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import butterknife.InjectView;

/**
 * 我的成绩
 */
public class MyPerformanceActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;

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
        actionbar.setTitle(R.string.my_performance);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        YiXiuGeApi api = new YiXiuGeApi("app/yearmark");
        api.addParams("uid", api.getUserId(this));
        MyPerformanceAdapter adapter = new MyPerformanceAdapter(this);
        smartSwipeRefreshLayout.initWithLinearLayout();
        smartSwipeRefreshLayout.setAdapter(adapter);
        SwipeRefreshController<NoPageListBean<PerformanceBean>> controller = new SwipeRefreshController<NoPageListBean<PerformanceBean>>(this, smartSwipeRefreshLayout, api, adapter) {
        };
        controller.loadFirstPage();
    }

}
