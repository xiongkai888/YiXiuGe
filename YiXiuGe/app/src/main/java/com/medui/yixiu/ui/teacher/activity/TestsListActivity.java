package com.medui.yixiu.ui.teacher.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.medui.yixiu.R;
import com.medui.yixiu.adapter.TestsListAdapter;
import com.medui.yixiu.api.YiXiuGeApi;
import com.medui.yixiu.bean.TestListBean;
import com.medui.yixiu.event.TestFinishEvent;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.InjectView;

/**
 * 我的评估（老师端）
 */
public class TestsListActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    private TestsListAdapter adapter;
    private SwipeRefreshController<NoPageListBean<TestListBean>> controller;

    @Override
    public int getContentViewId() {
        return R.layout.activity_single_listview_no;
    }



    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);

        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.back);
        actionbar.setTitle(R.string.my_tests);

        YiXiuGeApi api = new YiXiuGeApi("app/assesslist");
        api.addParams("uid", api.getUserId(this));

        adapter = new TestsListAdapter(this);
        smartSwipeRefreshLayout.initWithLinearLayout();
        smartSwipeRefreshLayout.setAdapter(adapter);
        controller = new SwipeRefreshController<NoPageListBean<TestListBean>>(this, smartSwipeRefreshLayout, api, adapter) {
        };
        controller.loadFirstPage();
//        adapter.notifyDataSetChanged();
    }

    //测试结束的时候调用
    @Subscribe
    public void testFinishEvent(TestFinishEvent event) {
        controller.loadFirstPage();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
