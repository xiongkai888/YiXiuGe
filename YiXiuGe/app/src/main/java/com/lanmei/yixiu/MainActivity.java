package com.lanmei.yixiu;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.lanmei.yixiu.adapter.MainPagerAdapter;
import com.lanmei.yixiu.event.AddCourseEvent;
import com.lanmei.yixiu.event.LogoutEvent;
import com.lanmei.yixiu.helper.TabHelper;
import com.lanmei.yixiu.update.UpdateAppConfig;
import com.lanmei.yixiu.utils.CommonUtils;
import com.xson.common.app.BaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.InjectView;

public class MainActivity extends BaseActivity implements TabLayout.OnTabSelectedListener{

    @InjectView(R.id.viewPager)
    ViewPager mViewPager;
    @InjectView(R.id.tabLayout)
    TabLayout mTabLayout;

    @Override
    public int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {

        new TabHelper(this, mTabLayout);

        mViewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));
        mViewPager.setOffscreenPageLimit(2);
        mTabLayout.addOnTabSelectedListener(this);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

//        mViewPager.setNoScroll(true);
        EventBus.getDefault().register(this);
        CommonUtils.loadUserInfo(this,null);
        UpdateAppConfig.requestStoragePermission(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int position = tab.getPosition();
        mViewPager.setCurrentItem(position);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    //退出登录时调用
    @Subscribe
    public void logoutEvent(LogoutEvent event){
        finish();
    }

    //添加视频教程后调用
    @Subscribe
    public void AddCourseEvent(AddCourseEvent event) {
        mViewPager.setCurrentItem(2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
