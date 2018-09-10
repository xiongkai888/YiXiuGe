package com.lanmei.yixiu.ui.mine.fragment;

import android.os.Bundle;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.adapter.CollectCourseAdapter;
import com.lanmei.yixiu.api.YiXiuGeApi;
import com.lanmei.yixiu.bean.CourseClassifyListBean;
import com.lanmei.yixiu.event.CourseCollectEvent;
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
 * 收藏教程
 */

public class CollectCourseFragment extends BaseFragment {

    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    CollectCourseAdapter mAdapter;
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
        YiXiuGeApi api = new YiXiuGeApi("app/favour_list");
        api.addParams("uid", api.getUserId(context));
        api.addParams("mod", "video");
        mAdapter = new CollectCourseAdapter(context);
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
                bean.setFavour(event.getFavour());
                mAdapter.notifyDataSetChanged();
                return;
            }
        }
    }
    //教程点击收藏或取消收藏时候调用
    @Subscribe
    public void courseCollectEvent(CourseCollectEvent event) {
        controller.loadFirstPage();
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
