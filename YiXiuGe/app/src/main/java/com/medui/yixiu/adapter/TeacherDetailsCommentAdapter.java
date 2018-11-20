package com.medui.yixiu.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.medui.yixiu.ui.home.fragment.TeacherDetailsCommentListFragment;


/**
 * 教程
 */
public class TeacherDetailsCommentAdapter extends FragmentPagerAdapter {


    private String tid;

    public TeacherDetailsCommentAdapter(FragmentManager fm,String tid) {
        super(fm);
        this.tid = tid;
    }

    @Override
    public Fragment getItem(int position) {
        TeacherDetailsCommentListFragment fragment = new TeacherDetailsCommentListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", position+1);
        bundle.putString("tid", tid);
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
                return "好评";
            case 1:
                return "中评";
            case 2:
                return "差评";
        }
        return "";
    }


}
