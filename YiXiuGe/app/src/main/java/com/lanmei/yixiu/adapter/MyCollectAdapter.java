package com.lanmei.yixiu.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lanmei.yixiu.ui.mine.fragment.CollectCourseFragment;
import com.lanmei.yixiu.ui.mine.fragment.CollectNewsFragment;


/**
 * 我的收藏
 */
public class MyCollectAdapter extends FragmentPagerAdapter {


    public MyCollectAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new CollectCourseFragment();
            case 1:
                return new CollectNewsFragment();

        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "课程";
            case 1:
                return "资讯";
        }
        return "";
    }


}
