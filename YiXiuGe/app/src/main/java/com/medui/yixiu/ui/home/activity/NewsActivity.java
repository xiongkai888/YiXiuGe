package com.medui.yixiu.ui.home.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;

import com.medui.yixiu.R;
import com.medui.yixiu.adapter.NewsClassifyAdapter;
import com.medui.yixiu.api.YiXiuGeApi;
import com.medui.yixiu.bean.NewsClassifyBean;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.widget.CenterTitleToolbar;

import butterknife.InjectView;

/**
 * 资讯
 */
public class NewsActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;

    @InjectView(R.id.viewPager)
    ViewPager mViewPager;
    @InjectView(R.id.tabLayout)
    TabLayout mTabLayout;
    NewsClassifyAdapter adapter;

    @Override
    public int getContentViewId() {
        return R.layout.activity_news;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.news);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        adapter = new NewsClassifyAdapter(getSupportFragmentManager());

//        mViewPager.setOffscreenPageLimit(3);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        YiXiuGeApi api = new YiXiuGeApi("post/classlist");
        HttpClient.newInstance(this).request(api, new BeanRequest.SuccessListener<NoPageListBean<NewsClassifyBean>>() {
            @Override
            public void onResponse(NoPageListBean<NewsClassifyBean> response) {
                if (isFinishing()) {
                    return;
                }
                adapter.setList(response.data);
                mViewPager.setAdapter(adapter);
            }
        });
    }
}
