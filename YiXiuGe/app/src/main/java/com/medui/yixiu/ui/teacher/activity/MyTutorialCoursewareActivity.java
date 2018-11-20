package com.medui.yixiu.ui.teacher.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import com.medui.yixiu.R;
import com.medui.yixiu.adapter.TutorialCoursewareListAdapter;
import com.medui.yixiu.api.YiXiuGeApi;
import com.medui.yixiu.bean.NotesBean;
import com.medui.yixiu.event.PublishCoursewareEvent;
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
 *我的课件
 */
public class MyTutorialCoursewareActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    TutorialCoursewareListAdapter mAdapter;
    private SwipeRefreshController<NoPageListBean<NotesBean>> controller;

    @Override
    public int getContentViewId() {
        return R.layout.activity_single_listview;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.my_courseware);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        YiXiuGeApi api = new YiXiuGeApi("app/notelist");
        api.addParams("uid", api.getUserId(this));
        mAdapter = new TutorialCoursewareListAdapter(this);
        smartSwipeRefreshLayout.initWithLinearLayout();
        smartSwipeRefreshLayout.setAdapter(mAdapter);
        controller = new SwipeRefreshController<NoPageListBean<NotesBean>>(this, smartSwipeRefreshLayout, api, mAdapter) {
        };
        controller.loadFirstPage();
    }


    //发布或者删除课件的时候调用
    @Subscribe
    public void publishCoursewareEvent(PublishCoursewareEvent event){
        controller.loadFirstPage();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_uploading_courseware,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_uploading_courseware){
            IntentUtil.startActivity(this,PublishCoursewareActivity.class);
        }
        return super.onOptionsItemSelected(item);
    }
}
