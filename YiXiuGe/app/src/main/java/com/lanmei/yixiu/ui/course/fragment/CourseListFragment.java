package com.lanmei.yixiu.ui.course.fragment;

import android.os.Bundle;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.adapter.CourseListAdapter;
import com.lanmei.yixiu.api.YiXiuGeApi;
import com.lanmei.yixiu.bean.CourseClassifyListBean;
import com.lanmei.yixiu.event.CourseOperationEvent;
import com.xson.common.app.BaseFragment;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.utils.StringUtils;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.InjectView;


/**
 * Created by Administrator on 2017/4/27.
 * 教程列表
 */

public class CourseListFragment extends BaseFragment {

    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    CourseListAdapter mAdapter;
    private SwipeRefreshController<NoPageListBean<CourseClassifyListBean>> controller;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_single_listview;
    }


    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initSwipeRefreshLayout();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    private void initSwipeRefreshLayout() {
        smartSwipeRefreshLayout.initWithLinearLayout();
        YiXiuGeApi api = new YiXiuGeApi("app/video_index");
        api.addParams("cid", getArguments().getString("cid"));
        api.addParams("uid", api.getUserId(context));
        mAdapter = new CourseListAdapter(context);
        smartSwipeRefreshLayout.setAdapter(mAdapter);
        controller = new SwipeRefreshController<NoPageListBean<CourseClassifyListBean>>(context, smartSwipeRefreshLayout, api, mAdapter) {
        };
        controller.loadFirstPage();
    }

    //教程详情点赞是调用
    @Subscribe
    public void courseOperationEvent(CourseOperationEvent event) {
        String id = event.getId();
        List<CourseClassifyListBean> list = mAdapter.getData();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            CourseClassifyListBean bean = list.get(i);
            if (StringUtils.isSame(id,bean.getId())){
                bean.setLiked(event.getLiked());
                bean.setView(event.getViewNum());
                bean.setFavoured(event.getFavoured());
                bean.setReviews(event.getReviews());
                bean.setLike(event.getLike());
                mAdapter.notifyDataSetChanged();
                return;
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
