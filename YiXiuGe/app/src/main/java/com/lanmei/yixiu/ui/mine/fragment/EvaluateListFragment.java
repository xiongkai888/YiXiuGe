package com.lanmei.yixiu.ui.mine.fragment;

import android.os.Bundle;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.adapter.EvaluateListAdapter;
import com.lanmei.yixiu.api.YiXiuGeApi;
import com.lanmei.yixiu.bean.CourseClassifyBean;
import com.xson.common.app.BaseFragment;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import butterknife.InjectView;


/**
 * Created by Administrator on 2017/4/27.
 * 评价列表
 */

public class EvaluateListFragment extends BaseFragment {

    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    EvaluateListAdapter mAdapter;
    private SwipeRefreshController<NoPageListBean<CourseClassifyBean>> controller;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_single_listview_no;
    }


    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initSwipeRefreshLayout();
//        if (!EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().register(this);
//        }
    }

    private void initSwipeRefreshLayout() {
        smartSwipeRefreshLayout.initWithLinearLayout();
        YiXiuGeApi api = new YiXiuGeApi("app/adpic");
        api.addParams("uid", api.getUserId(context));
        api.addParams("row", 20);
        api.addParams("status", getArguments().getString("status"));
        mAdapter = new EvaluateListAdapter(context);
        smartSwipeRefreshLayout.setAdapter(mAdapter);
        controller = new SwipeRefreshController<NoPageListBean<CourseClassifyBean>>(context, smartSwipeRefreshLayout, api, mAdapter) {
        };
//        controller.loadFirstPage();
        mAdapter.notifyDataSetChanged();
    }





    @Override
    public void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }

}
