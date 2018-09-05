package com.lanmei.yixiu.ui.mine.fragment;

import android.os.Bundle;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.adapter.CollectNewsAdapter;
import com.lanmei.yixiu.api.YiXiuGeApi;
import com.lanmei.yixiu.bean.NewsClassifyListBean;
import com.lanmei.yixiu.event.NewsCollectEvent;
import com.lanmei.yixiu.event.NewsOperationEvent;
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
 * 收藏资讯
 */

public class CollectNewsFragment extends BaseFragment {

    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    CollectNewsAdapter mAdapter;
    private SwipeRefreshController<NoPageListBean<NewsClassifyListBean>> controller;

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
        api.addParams("mod", "post");
        mAdapter = new CollectNewsAdapter(context);
        smartSwipeRefreshLayout.setAdapter(mAdapter);
        controller = new SwipeRefreshController<NoPageListBean<NewsClassifyListBean>>(context, smartSwipeRefreshLayout, api, mAdapter) {
        };
        controller.loadFirstPage();
    }


    //资讯评论、点击收藏时
    @Subscribe
    public void newsOperationEvent(NewsOperationEvent event) {
        String id = event.getId();
        List<NewsClassifyListBean> list = mAdapter.getData();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            NewsClassifyListBean bean = list.get(i);
            if (StringUtils.isSame(id, bean.getId())) {
                bean.setFavoured(event.getFavoured());
                bean.setReviews(event.getReviews());
                mAdapter.notifyDataSetChanged();
                return;
            }
        }
    }
    //点击资讯收藏时
    @Subscribe
    public void newsCollectEvent(NewsCollectEvent event) {
        controller.loadFirstPage();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
