package com.lanmei.yixiu.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lanmei.yixiu.ui.teacher.fragment.ClassHourListFragment;
import com.lanmei.yixiu.utils.CommonUtils;

//我的课时
public class ClassHourTabAdapter extends FragmentPagerAdapter {

    public ClassHourTabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        ClassHourListFragment fragment = new ClassHourListFragment();
        Bundle bundle = new Bundle();
        switch (position) {
            case 0:
                bundle.putString("over", CommonUtils.isOne);
                break;
            case 1:
                bundle.putString("over", CommonUtils.isTwo);
                break;
        }
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "已上课时";
            case 1:
                return "未上课时";
        }
        return super.getPageTitle(position);
    }
}
