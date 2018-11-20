package com.medui.yixiu.ui.teacher.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import com.medui.yixiu.R;
import com.medui.yixiu.adapter.CheckInManageAdapter;
import com.medui.yixiu.api.YiXiuGeApi;
import com.medui.yixiu.bean.CheckingInManageBean;
import com.medui.yixiu.event.AddCheckInEvent;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.utils.IntentUtil;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.InjectView;

/**
 * 考勤管理（老是端）
 */
public class CheckingInManageActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;

    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    CheckInManageAdapter adapter;
    private SwipeRefreshController<NoPageListBean<CheckingInManageBean>> controller;
    private String cid;

    @Override
    public int getContentViewId() {
        return R.layout.activity_checking_in_manage;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.checking_in_manage);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        cid = getIntent().getStringExtra("value");

        YiXiuGeApi api = new YiXiuGeApi("app/attendance");
        api.addParams("id",cid);

        adapter = new CheckInManageAdapter(this);
        smartSwipeRefreshLayout.initWithLinearLayout();
        smartSwipeRefreshLayout.setAdapter(adapter);
        controller = new SwipeRefreshController<NoPageListBean<CheckingInManageBean>>(this, smartSwipeRefreshLayout, api, adapter) {
        };
        smartSwipeRefreshLayout.setMode(SmartSwipeRefreshLayout.Mode.NO_PAGE);
        controller.loadFirstPage();
    }

    //添加考勤后调用
    @Subscribe
    public void addCheckInEvent(AddCheckInEvent event){
        controller.loadFirstPage();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_check_in, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add_check_in) {
            IntentUtil.startActivity(this,AddCheckInActivity.class,cid);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
