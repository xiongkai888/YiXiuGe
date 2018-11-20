package com.medui.yixiu.ui.teacher.fragment;

import android.os.Bundle;

import com.medui.yixiu.R;
import com.medui.yixiu.adapter.ClassHourListAdapter;
import com.medui.yixiu.api.YiXiuGeApi;
import com.medui.yixiu.bean.ClassHourBean;
import com.medui.yixiu.event.PublishClassHourEvent;
import com.xson.common.app.BaseFragment;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.utils.StringUtils;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.InjectView;


/**
 * Created by Administrator on 2017/4/27.
 * 课时列表
 */

public class ClassHourListFragment extends BaseFragment {

    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    ClassHourListAdapter mAdapter;
    private SwipeRefreshController<NoPageListBean<ClassHourBean>> controller;
    private String over;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_single_listview_no;
    }


    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initSwipeRefreshLayout();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    private void initSwipeRefreshLayout() {
        over = getArguments().getString("over");

        smartSwipeRefreshLayout.initWithLinearLayout();
        YiXiuGeApi api = new YiXiuGeApi("app/courselist");
        api.addParams("uid", api.getUserId(context));
        api.addParams("over", over);
        mAdapter = new ClassHourListAdapter(context);
        smartSwipeRefreshLayout.setAdapter(mAdapter);
        controller = new SwipeRefreshController<NoPageListBean<ClassHourBean>>(context, smartSwipeRefreshLayout, api, mAdapter) {
        };
        controller.loadFirstPage();
    }

    //发布新课时时未上课时刷新列表 over = 2
    @Subscribe
    public void publishClassHourEvent(PublishClassHourEvent event) {
        if (StringUtils.isSame(over, event.getOver())) {
            controller.loadFirstPage();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
