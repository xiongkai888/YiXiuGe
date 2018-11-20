package com.medui.yixiu.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.medui.yixiu.ui.course.CourseFragment;
import com.medui.yixiu.ui.home.HomeFragment;
import com.medui.yixiu.ui.message.MessageFragment;
import com.medui.yixiu.ui.mine.MineFragment;


public class MainPagerAdapter extends FragmentPagerAdapter {

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();//首页
            case 1:
                return new MessageFragment();//消息
            case 2:
                return new CourseFragment();//教程
            case 3:
                return new MineFragment();//我的
        }
        return new HomeFragment();
    }

    @Override
    public int getCount() {
        return 4;
    }

}
