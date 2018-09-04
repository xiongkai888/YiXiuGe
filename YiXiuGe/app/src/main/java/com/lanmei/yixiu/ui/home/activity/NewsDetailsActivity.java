package com.lanmei.yixiu.ui.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.adapter.NewsDetailsAdapter;
import com.lanmei.yixiu.api.YiXiuGeApi;
import com.lanmei.yixiu.bean.NewsClassifyListBean;
import com.lanmei.yixiu.bean.NewsCommentBean;
import com.lanmei.yixiu.event.NewsOperationEvent;
import com.lanmei.yixiu.utils.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.bean.DataBean;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;

import butterknife.InjectView;
import butterknife.OnClick;


/**
 * 资讯详情
 */
public class NewsDetailsActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.compile_comment_et)
    EditText mCompileCommentEt;//写评论
    NewsDetailsAdapter mAdapter;
    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    private String id;//资讯ID

    private SwipeRefreshController<NoPageListBean<NewsCommentBean>> controller;

    @Override
    public int getContentViewId() {
        return R.layout.activity_news_details;
    }

    @Override
    public void initIntent(Intent intent) {
        super.initIntent(intent);

    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.news_details);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        id = getIntent().getStringExtra("value");
        L.d(L.TAG,id);
        loadNewsDetails();

        smartSwipeRefreshLayout.initWithLinearLayout();

        YiXiuGeApi api = new YiXiuGeApi("app/post_reviews_list");
        api.addParams("id",id);
        api.addParams("mod","post");//资讯
        api.addParams("uid",api.getUserId(this));
        mAdapter = new NewsDetailsAdapter(this);
        smartSwipeRefreshLayout.setAdapter(mAdapter);
        controller = new SwipeRefreshController<NoPageListBean<NewsCommentBean>>(this, smartSwipeRefreshLayout, api, mAdapter) {
        };
        controller.loadFirstPage();
    }

    //资讯详情
    private void loadNewsDetails() {
        YiXiuGeApi api = new YiXiuGeApi("post/details");
        api.addParams("id", id);
        api.addParams("uid",api.getUserId(this));
        HttpClient.newInstance(this).request(api, new BeanRequest.SuccessListener<DataBean<NewsClassifyListBean>>() {
            @Override
            public void onResponse(DataBean<NewsClassifyListBean> response) {
                if (isFinishing()) {
                    return;
                }
                mAdapter.setNewsBean(response.data);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_more, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_more:
                UIHelper.ToastMessage(this,R.string.developing);
                break;
        }

        return super.onOptionsItemSelected(item);
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
        if (StringUtils.isEmpty(content)){
            UIHelper.ToastMessage(this,getString(R.string.input_comment));
            return;
        }
        pushReviews(content);
    }

    //评论
    private void pushReviews(String content) {
        YiXiuGeApi api = new YiXiuGeApi("app/post_reviews");
        api.addParams("id",id);
        api.addParams("content",content);
        api.addParams("mod","post");//资讯
        api.addParams("uid",api.getUserId(this));
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()){
                    return;
                }
                mCompileCommentEt.setText("");
                controller.loadFirstPage();
                EventBus.getDefault().post(new NewsOperationEvent());//通知资讯列表刷新
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter.removeWebView();
    }
}
