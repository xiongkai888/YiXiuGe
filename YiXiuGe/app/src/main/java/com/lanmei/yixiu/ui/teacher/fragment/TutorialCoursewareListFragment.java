package com.lanmei.yixiu.ui.teacher.fragment;

import android.os.Bundle;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.adapter.TutorialCoursewareListAdapter;
import com.lanmei.yixiu.api.YiXiuGeApi;
import com.lanmei.yixiu.bean.NotesBean;
import com.lanmei.yixiu.event.PublishCoursewareEvent;
import com.xson.common.app.BaseFragment;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.InjectView;


/**
 * Created by Administrator on 2017/4/27.
 * 教程课件列表
 */

public class TutorialCoursewareListFragment extends BaseFragment {

    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    private SwipeRefreshController<NoPageListBean<NotesBean>> controller;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_single_listview;
    }


    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initSwipeRefreshLayout();
    }

    private void initSwipeRefreshLayout() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        smartSwipeRefreshLayout.initWithLinearLayout();
        YiXiuGeApi api = new YiXiuGeApi("app/notelist");
//        api.addParams("uid", api.getUserId(context));
        api.addParams("cid", getArguments().getString("cid"));
        api.addParams("type", 2);
        TutorialCoursewareListAdapter adapter = new TutorialCoursewareListAdapter(context);
        smartSwipeRefreshLayout.setAdapter(adapter);
        controller = new SwipeRefreshController<NoPageListBean<NotesBean>>(context, smartSwipeRefreshLayout, api, adapter) {
        };
        controller.loadFirstPage();
    }

    //删除课件的时候调用
    @Subscribe
    public void publishCoursewareEvent(PublishCoursewareEvent event) {
        controller.loadFirstPage();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
