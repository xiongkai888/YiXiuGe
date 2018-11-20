package com.medui.yixiu.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.medui.yixiu.R;
import com.medui.yixiu.ui.mine.fragment.ExaminationFragment;


/**
 * 考试
 */
public class ExaminationAdapter extends FragmentPagerAdapter {

    private Context context;

    public ExaminationAdapter(Context context,FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        ExaminationFragment fragment = new ExaminationFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type",(position==0)?2:1);// 1|2=>综合考试|随堂考试
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
                return context.getString(R.string.quiz);
            case 1:
                return context.getString(R.string.comprehensive_examination);
        }
        return "";
    }


}
