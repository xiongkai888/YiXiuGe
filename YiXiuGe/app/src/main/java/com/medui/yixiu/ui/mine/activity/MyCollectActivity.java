package com.medui.yixiu.ui.mine.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.medui.yixiu.R;
import com.medui.yixiu.adapter.MyCollectAdapter;
import com.xson.common.app.BaseActivity;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 我的收藏
 */
public class MyCollectActivity extends BaseActivity {

    @InjectView(R.id.tabLayout)
    TabLayout mTabLayout;
    @InjectView(R.id.viewPager)
    ViewPager mViewPager;

    @Override
    public int getContentViewId() {
        return R.layout.activity_my_evaluate;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {

        mViewPager.setAdapter(new MyCollectAdapter(getSupportFragmentManager()));
        mTabLayout.setupWithViewPager(mViewPager);

    }


    @OnClick(R.id.back_iv)
    public void onViewClicked() {
        finish();
    }
}
