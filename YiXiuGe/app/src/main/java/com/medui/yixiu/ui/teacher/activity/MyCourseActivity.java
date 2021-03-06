package com.medui.yixiu.ui.teacher.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import com.medui.yixiu.R;
import com.medui.yixiu.adapter.MyCourseAdapter;
import com.medui.yixiu.api.YiXiuGeApi;
import com.medui.yixiu.bean.MyCourseBean;
import com.medui.yixiu.event.AddCourseEvent;
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
 * 我的教程
 */
public class MyCourseActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    private SwipeRefreshController<NoPageListBean<MyCourseBean>> controller;


    public int getContentViewId() {
        return R.layout.activity_single_listview_no;
    }


    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.my_course);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        EventBus.getDefault().register(this);

        YiXiuGeApi api = new YiXiuGeApi("app/my_video");
        api.addParams("uid",api.getUserId(this));

        MyCourseAdapter adapter = new MyCourseAdapter(this);
        smartSwipeRefreshLayout.initWithLinearLayout();
        smartSwipeRefreshLayout.setAdapter(adapter);
        controller = new SwipeRefreshController<NoPageListBean<MyCourseBean>>(this, smartSwipeRefreshLayout, api, adapter) {
        };
        controller.loadFirstPage();
    }

    //添加教程时调用
    @Subscribe
    public void addCourseEvent(AddCourseEvent event){
        controller.loadFirstPage();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_course, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_course:
                IntentUtil.startActivity(this, UploadVideoListActivity.class);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}

