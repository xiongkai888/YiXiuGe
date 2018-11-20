package com.medui.yixiu.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.medui.yixiu.ui.mine.fragment.EvaluateListFragment;


/**
 * 教程
 */
public class MyEvaluateAdapter extends FragmentPagerAdapter {


    public MyEvaluateAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        EvaluateListFragment fragment = new EvaluateListFragment();
        Bundle bundle = new Bundle();
        switch (position) {//1|2|3=>课时|教师|同学
            case 0:
                bundle.putString("type","1");//课时
                break;
            case 1:
                bundle.putString("type","2");//教师
                break;
            case 2:
                bundle.putString("type","3");//同学
                break;

        }
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "课程";
            case 1:
                return "教师";
            case 2:
                return "同学";
        }
        return "";
    }


}
