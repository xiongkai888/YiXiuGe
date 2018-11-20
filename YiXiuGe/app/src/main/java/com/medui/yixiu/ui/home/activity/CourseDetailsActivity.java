package com.medui.yixiu.ui.home.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.medui.yixiu.R;
import com.medui.yixiu.adapter.CourseDetailsAdapter;
import com.medui.yixiu.api.YiXiuGeApi;
import com.medui.yixiu.bean.CourseClassifyListBean;
import com.medui.yixiu.bean.NewsCommentBean;
import com.medui.yixiu.event.CourseOperationEvent;
import com.medui.yixiu.helper.ShareHelper;
import com.medui.yixiu.helper.ShareListener;
import com.medui.yixiu.utils.CommonUtils;
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
    private CourseClassifyListBean bean;
    private ShareHelper shareHelper;

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

        shareHelper = new ShareHelper(this);

        YiXiuGeApi api = new YiXiuGeApi("app/post_reviews_list");
        if (bean != null) {
            api.addParams("id", bean.getId());
            loadCurse();//为的是更新浏览数
        }
        api.addParams("mod", "video");//教程
//        api.addParams("uid", api.getUserId(this));
        adapter = new CourseDetailsAdapter(this);
        smartSwipeRefreshLayout.initWithLinearLayout();
        smartSwipeRefreshLayout.setAdapter(adapter);
        smartSwipeRefreshLayout.setMode(SmartSwipeRefreshLayout.Mode.ONLY_PULL_UP);
        controller = new SwipeRefreshController<NoPageListBean<NewsCommentBean>>(this, smartSwipeRefreshLayout, api, adapter) {
        };
        controller.loadFirstPage();
        adapter.setShare(new ShareListener() {
            @Override
            public void share(String url) {
                shareHelper.share(url);
//                CommonUtils.developing(getContext());
            }
        });
        mJzvdStd.setUp(bean.getVideo(), bean.getTitle(), JzvdStd.SCREEN_WINDOW_NORMAL);
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
                adapter.notifyDataSetChanged();
                EventBus.getDefault().post(new CourseOperationEvent(bean.getId(), bean.getLiked(), bean.getLike(),"" + (StringUtils.toInt(bean.getView(), 0) + 1), bean.getReviews(), bean.getFavoured(), bean.getFavour()));
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
        shareHelper.onDestroy();
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
                EventBus.getDefault().post(new CourseOperationEvent(bean.getId(), bean.getLiked(), bean.getLike(), "" + (StringUtils.toInt(bean.getView(), 0) + 1), bean.getReviews(), bean.getFavoured(), bean.getFavour()));
                adapter.notifyDataSetChanged();
            }
        });
    }


    /**
     * 结果返回
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        shareHelper.onActivityResult(requestCode, resultCode, data);
    }

}
