package com.medui.yixiu.ui.mine.fragment;

import android.os.Bundle;

import com.medui.yixiu.R;
import com.medui.yixiu.adapter.ExaminationListAdapter;
import com.medui.yixiu.api.YiXiuGeApi;
import com.medui.yixiu.bean.ExaminationListBean;
import com.medui.yixiu.event.ExaminationFinishEvent;
import com.xson.common.app.BaseFragment;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.utils.StringUtils;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.InjectView;


/**
 * Created by Administrator on 2018/8/21.
 * type = 1|2 =>综合考试|随堂考试
 */

public class ExaminationFragment extends BaseFragment {

    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    private SwipeRefreshController<NoPageListBean<ExaminationListBean>> controller;
    private String type;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_single_listview_no;
    }


    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {

        type = String.valueOf(getArguments().getInt("type"));

        initSwipeRefreshLayout();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    private void initSwipeRefreshLayout() {
        smartSwipeRefreshLayout.initWithLinearLayout();
        YiXiuGeApi api = new YiXiuGeApi("app/sexamlist");
        api.addParams("uid", api.getUserId(context));
        api.addParams("type", type);
        ExaminationListAdapter adapter = new ExaminationListAdapter(context);
        smartSwipeRefreshLayout.setAdapter(adapter);
        controller = new SwipeRefreshController<NoPageListBean<ExaminationListBean>>(context, smartSwipeRefreshLayout, api, adapter) {
        };
        controller.loadFirstPage();
    }


    //考试提交完成时候调用
    @Subscribe
    public void examinationFinishEvent(ExaminationFinishEvent event){
        if (StringUtils.isSame(event.getType(),type)){
            controller.loadFirstPage();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
