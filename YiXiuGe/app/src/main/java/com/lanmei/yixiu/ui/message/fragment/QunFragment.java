package com.lanmei.yixiu.ui.message.fragment;

import android.os.Bundle;

import com.lanmei.yixiu.R;
import com.xson.common.app.BaseFragment;


/**
 * Created by Administrator on 2017/4/27.
 * 消息-群聊
 */

public class QunFragment extends BaseFragment {


//    @InjectView(R.id.pull_refresh_rv)
//    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
//    MessageQunAdapter mAdapter;
//    private SwipeRefreshController<NoPageListBean<CourseClassifyBean>> controller;

    @Override
    public int getContentViewId() {
//        return R.layout.fragment_single_listview;
        return R.layout.include_developing;
    }


    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
//        initSwipeRefreshLayout();
//        if (!EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().register(this);
//        }
    }

    private void initSwipeRefreshLayout() {
//        smartSwipeRefreshLayout.initWithLinearLayout();
//        YiXiuGeApi api = new YiXiuGeApi("app/adpic");
//        api.addParams("uid", api.getUserId(context));
//        api.addParams("row", 20);
//        mAdapter = new MessageQunAdapter(context);
//        smartSwipeRefreshLayout.setAdapter(mAdapter);
//        controller = new SwipeRefreshController<NoPageListBean<CourseClassifyBean>>(context, smartSwipeRefreshLayout, api, mAdapter) {
//        };
////        controller.loadFirstPage();
//        mAdapter.notifyDataSetChanged();
    }

}
