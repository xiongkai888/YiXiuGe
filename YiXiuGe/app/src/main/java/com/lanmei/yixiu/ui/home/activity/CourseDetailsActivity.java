package com.lanmei.yixiu.ui.home.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.adapter.CourseDetailsAdapter;
import com.lanmei.yixiu.api.YiXiuGeApi;
import com.lanmei.yixiu.bean.CourseClassifyListBean;
import com.lanmei.yixiu.bean.NewsCommentBean;
import com.lanmei.yixiu.event.CourseOperationEvent;
import com.lanmei.yixiu.utils.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.bean.DataBean;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.helper.ImageHelper;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

/**
 * 教程详情(详情)
 */
public class CourseDetailsActivity extends BaseActivity {

    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    CourseDetailsAdapter adapter;
    @InjectView(R.id.compile_comment_et)
    EditText mCompileCommentEt;//写评论
    @InjectView(R.id.jz_video)
    JzvdStd mJzvdStd;
    private SwipeRefreshController<NoPageListBean<NewsCommentBean>> controller;
    CourseClassifyListBean bean;

    @Override
    public int getContentViewId() {
        return R.layout.activity_course;//
    }

    @Override
    public void initIntent(Intent intent) {
        super.initIntent(intent);
        Bundle bundle = intent.getBundleExtra("bundle");
        if (bundle == null) {
            return;
        }
        bean = (CourseClassifyListBean) bundle.getSerializable("bean");
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        YiXiuGeApi api = new YiXiuGeApi("app/post_reviews_list");
        if (bean != null) {
            api.addParams("id", bean.getId());
            loadCurse();
        }
        api.addParams("mod", "video");//教程
//        api.addParams("uid", api.getUserId(this));
        adapter = new CourseDetailsAdapter(this);
        smartSwipeRefreshLayout.initWithLinearLayout();
        smartSwipeRefreshLayout.setAdapter(adapter);
        controller = new SwipeRefreshController<NoPageListBean<NewsCommentBean>>(this, smartSwipeRefreshLayout, api, adapter) {
        };
        smartSwipeRefreshLayout.setMode(SmartSwipeRefreshLayout.Mode.DISABLED);
        controller.loadFirstPage();

        adapter.setCourseClassifyListBean(bean);
        mJzvdStd.setUp(bean.getVideo(), bean.getTitle()
                , JzvdStd.SCREEN_WINDOW_NORMAL);
        ImageHelper.load(this, bean.getPic(), mJzvdStd.thumbImageView, null, true, R.drawable.default_pic, R.drawable.default_pic);
        Jzvd.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        Jzvd.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

//
    }

    //教程详情
    private void loadCurse() {
        YiXiuGeApi api = new YiXiuGeApi("app/video_details");
        api.addParams("id", bean.getId());
        api.addParams("uid", api.getUserId(this));

        HttpClient.newInstance(this).request(api, new BeanRequest.SuccessListener<DataBean<CourseClassifyListBean>>() {
            @Override
            public void onResponse(DataBean<CourseClassifyListBean> response) {
                if (isFinishing()) {
                    return;
                }
                bean = response.data;
                adapter.setCourseClassifyListBean(bean);
                EventBus.getDefault().post(new CourseOperationEvent(bean.getId(), bean.getLiked(), bean.getLike(),"" + (StringUtils.toInt(bean.getView(), 0) + 1), bean.getReviews(), bean.getFavoured()));
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();

        //Change these two variables back
        Jzvd.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_SENSOR;
        Jzvd.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mJzvdStd = null;
    }

    @OnClick({R.id.back_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
        }
    }

    @OnClick({R.id.send_info_tv})
    public void showInfo(View view) {//发送消息
        if (!CommonUtils.isLogin(this)) {
            return;
        }
        switch (view.getId()) {
            case R.id.send_info_tv:
                ajaxSend(CommonUtils.getStringByEditText(mCompileCommentEt));
                break;
        }
    }

    /**
     * @param content 评论内容
     */
    private void ajaxSend(String content) {
        if (StringUtils.isEmpty(content)) {
            UIHelper.ToastMessage(this, getString(R.string.input_comment));
            return;
        }
        if (bean == null) {
            return;
        }
        pushReviews(content);
    }

    //评论
    private void pushReviews(String content) {
        YiXiuGeApi api = new YiXiuGeApi("app/post_reviews");
        api.addParams("id", bean.getId());
        api.addParams("content", content);
        api.addParams("mod", "video");//教程
        api.addParams("uid", api.getUserId(this));
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
                mCompileCommentEt.setText("");
                controller.loadFirstPage();
                bean.setReviews("" + (StringUtils.toInt(bean.getReviews()) + 1));
                EventBus.getDefault().post(new CourseOperationEvent(bean.getId(), bean.getLiked(), bean.getLike(), "" + (StringUtils.toInt(bean.getView(), 0) + 1), bean.getReviews(), bean.getFavoured()));
                adapter.notifyDataSetChanged();
            }
        });
    }


}
