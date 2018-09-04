package com.lanmei.yixiu.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lanmei.yixiu.bean.CourseClassifyBean;
import com.lanmei.yixiu.ui.course.fragment.CourseListFragment;
import com.xson.common.utils.StringUtils;

import java.util.List;


/**
 * 教程
 */
public class CourseAdapter extends FragmentPagerAdapter {


    private List<CourseClassifyBean> list;

    public void setList(List<CourseClassifyBean> list) {
        this.list = list;
    }

    public CourseAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        CourseListFragment fragment = new CourseListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("cid", list.get(position).getId());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return StringUtils.isEmpty(list) ? 0 : list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return list.get(position).getName();
    }


}
