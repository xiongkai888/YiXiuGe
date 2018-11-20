package com.medui.yixiu.ui.home.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.medui.yixiu.R;
import com.medui.yixiu.adapter.NewsSubAdapter;
import com.medui.yixiu.api.YiXiuGeApi;
import com.medui.yixiu.bean.AdBean;
import com.medui.yixiu.bean.NewsClassifyListBean;
import com.medui.yixiu.event.NewsOperationEvent;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.utils.StringUtils;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.InjectView;

/**
 * 资讯列表sub
 */
public class NewsSubActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    NewsSubAdapter mAdapter;
    private SwipeRefreshController<NoPageListBean<NewsClassifyListBean>> controller;

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
        actionbar.setTitle(R.string.news);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        YiXiuGeApi api = new YiXiuGeApi("post/index");
        api.addParams("cid", 1);
        api.addParams("uid", api.getUserId(this));

        mAdapter = new NewsSubAdapter(this);

        smartSwipeRefreshLayout.initWithLinearLayout();
        smartSwipeRefreshLayout.setAdapter(mAdapter);
        controller = new SwipeRefreshController<NoPageListBean<NewsClassifyListBean>>(this, smartSwipeRefreshLayout, api, mAdapter) {
        };
        controller.loadFirstPage();
        loadAd();
    }

    private void loadAd() {
        YiXiuGeApi api = new YiXiuGeApi("app/adpic");
        api.addParams("classid", 2);
        HttpClient.newInstance(this).request(api, new BeanRequest.SuccessListener<NoPageListBean<AdBean>>() {
            @Override
            public void onResponse(NoPageListBean<AdBean> response) {
                if (isFinishing()) {
                    return;
                }
                List<AdBean> list = response.data;
                if (StringUtils.isEmpty(list)) {
                    return;
                }
                mAdapter.setList(list);
            }
        });
    }

    //资讯评论、点击收藏时
    @Subscribe
    public void newsOperationEvent(NewsOperationEvent event){
        String id = event.getId();
        List<NewsClassifyListBean> list = mAdapter.getData();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            NewsClassifyListBean bean = list.get(i);
            if (StringUtils.isSame(id,bean.getId())){
                bean.setFavoured(event.getFavoured());
                bean.setReviews(event.getReviews());
                mAdapter.notifyDataSetChanged();
                return;
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
