package com.medui.yixiu.ui.mine.fragment;

import android.os.Bundle;

import com.medui.yixiu.R;
import com.medui.yixiu.adapter.EvaluateListAdapter;
import com.medui.yixiu.api.YiXiuGeApi;
import com.medui.yixiu.bean.EvaluateListBean;
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

    @Override
    public int getContentViewId() {
        return R.layout.fragment_single_listview_no;
    }


    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        smartSwipeRefreshLayout.initWithLinearLayout();
        YiXiuGeApi api = new YiXiuGeApi("app/my_comment");
        api.addParams("uid", api.getUserId(context));
        api.addParams("type", getArguments().getString("type"));
        EvaluateListAdapter mAdapter = new EvaluateListAdapter(context);
        smartSwipeRefreshLayout.setAdapter(mAdapter);
        SwipeRefreshController<NoPageListBean<EvaluateListBean>> controller = new SwipeRefreshController<NoPageListBean<EvaluateListBean>>(context, smartSwipeRefreshLayout, api, mAdapter) {
        };
        controller.loadFirstPage();
    }
}
