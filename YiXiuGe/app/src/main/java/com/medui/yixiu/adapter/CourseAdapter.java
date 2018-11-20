package com.medui.yixiu.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.medui.yixiu.R;
import com.medui.yixiu.bean.CourseClassifyBean;
import com.medui.yixiu.ui.course.fragment.CourseListFragment;
import com.xson.common.utils.StringUtils;

import java.util.List;


/**
 * 教程
 */
public class CourseAdapter extends FragmentPagerAdapter {


    private List<CourseClassifyBean> list;

    public void setList(Context context,List<CourseClassifyBean> list) {
        this.list = list;
        if (list != null){
            CourseClassifyBean bean = new CourseClassifyBean();
            bean.setId("");
            bean.setName(context.getString(R.string.all));
            list.add(0,bean);
        }
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
