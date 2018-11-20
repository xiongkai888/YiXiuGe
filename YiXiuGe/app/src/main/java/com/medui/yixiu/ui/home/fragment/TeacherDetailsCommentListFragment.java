package com.medui.yixiu.ui.home.fragment;

import android.os.Bundle;

import com.medui.yixiu.R;
import com.medui.yixiu.adapter.TeacherDetailsCommentListAdapter;
import com.medui.yixiu.api.YiXiuGeApi;
import com.medui.yixiu.bean.TeacherDetailsCommentBean;
import com.xson.common.app.BaseFragment;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import butterknife.InjectView;


/**
 * Created by Administrator on 2017/4/27.
 * 教师详情评论列表
 */

public class TeacherDetailsCommentListFragment extends BaseFragment {

    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    TeacherDetailsCommentListAdapter mAdapter;
    private SwipeRefreshController<NoPageListBean<TeacherDetailsCommentBean>> controller;
    private int type;
    private String tid;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_single_listview;
    }


    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initSwipeRefreshLayout();
//        if (!EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().register(this);
//        }
    }

    private void initSwipeRefreshLayout() {
        type = getArguments().getInt("type");
        tid = getArguments().getString("tid");//教师id
        YiXiuGeApi api = new YiXiuGeApi("app/teacher_comment");
        api.addParams("type", type);
        api.addParams("tid", tid);
        api.addParams("uid", api.getUserId(context));
        mAdapter = new TeacherDetailsCommentListAdapter(context);
        smartSwipeRefreshLayout.initWithLinearLayout();
        smartSwipeRefreshLayout.setAdapter(mAdapter);
        controller = new SwipeRefreshController<NoPageListBean<TeacherDetailsCommentBean>>(context, smartSwipeRefreshLayout, api, mAdapter) {
        };
//        smartSwipeRefreshLayout.setNestedScrollingEnabled(false);
        smartSwipeRefreshLayout.setMode(SmartSwipeRefreshLayout.Mode.ONLY_PULL_UP);
        controller.loadFirstPage();
    }

//    //教程详情点赞是调用
//    @Subscribe
//    public void courseOperationEvent(CourseOperationEvent event) {
//        String id = event.getId();
//        List<CourseClassifyListBean> list = mAdapter.getData();
//        int size = list.size();
//        for (int i = 0; i < size; i++) {
//            CourseClassifyListBean bean = list.get(i);
//            if (StringUtils.isSame(id, bean.getId())) {
//                bean.setLiked(event.getLiked());
//                bean.setView(event.getViewNum());
//                bean.setFavoured(event.getFavoured());
//                bean.setReviews(event.getReviews());
//                bean.setLike(event.getLike());
//                bean.setFavour(event.getFavour());
//                mAdapter.notifyDataSetChanged();
//                return;
//            }
//        }
//    }

//    //添加视频教程后调用
//    @Subscribe
//    public void AddCourseEvent(AddCourseEvent event) {
//        if (StringUtils.isSame(event.getCid(), cid) && controller != null) {
//            controller.loadFirstPage();
//        }
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//    }

}
