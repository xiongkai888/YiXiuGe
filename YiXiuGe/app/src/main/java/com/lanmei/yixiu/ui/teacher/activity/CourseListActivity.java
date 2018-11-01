package com.lanmei.yixiu.ui.teacher.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.adapter.CourseListAdapter;
import com.lanmei.yixiu.api.YiXiuGeApi;
import com.lanmei.yixiu.bean.CourseClassifyListBean;
import com.lanmei.yixiu.event.CourseOperationEvent;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.utils.StringUtils;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.InjectView;

/**
 * 老师的教程列表
 */
public class CourseListActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    private CourseListAdapter adapter;
    private SwipeRefreshController<NoPageListBean<CourseClassifyListBean>> controller;

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
        actionbar.setHomeAsUpIndicator(R.drawable.back);
        Bundle bundle = getIntent().getBundleExtra("bundle");
        actionbar.setTitle(bundle.getString("realname")+"的教程列表");

        YiXiuGeApi api = new YiXiuGeApi("app/video_index");
        api.addParams("uid", api.getUserId(this));
        api.addParams("tid", bundle.getString("id"));


        adapter = new CourseListAdapter(this);
        smartSwipeRefreshLayout.initWithLinearLayout();
        smartSwipeRefreshLayout.setAdapter(adapter);
        controller = new SwipeRefreshController<NoPageListBean<CourseClassifyListBean>>(this, smartSwipeRefreshLayout, api, adapter) {
        };
        controller.loadFirstPage();
    }

    //教程详情点赞是调用
    @Subscribe
    public void courseOperationEvent(CourseOperationEvent event) {
        String id = event.getId();
        List<CourseClassifyListBean> list = adapter.getData();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            CourseClassifyListBean bean = list.get(i);
            if (StringUtils.isSame(id, bean.getId())) {
                bean.setLiked(event.getLiked());
                bean.setView(event.getViewNum());
                bean.setFavoured(event.getFavoured());
                bean.setReviews(event.getReviews());
                bean.setLike(event.getLike());
                bean.setFavour(event.getFavour());
                adapter.notifyDataSetChanged();
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
