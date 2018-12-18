package com.medui.yixiu.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.medui.yixiu.R;
import com.medui.yixiu.ui.mine.fragment.ExaminationFragment;
import com.medui.yixiu.ui.mine.fragment.TestListFragment;


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
        switch (position) {
            case 0:
                ExaminationFragment fragment1 = new ExaminationFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putInt("type",2);// 1|2=>综合考试|随堂考试
                fragment1.setArguments(bundle1);
                return fragment1;
            case 1:
                ExaminationFragment fragment2 = new ExaminationFragment();
                Bundle bundle2 = new Bundle();
                bundle2.putInt("type",1);// 1|2=>综合考试|随堂考试
                fragment2.setArguments(bundle2);
                return fragment2;
            case 2:
                return new TestListFragment();//我的评估
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.quiz);
            case 1:
                return context.getString(R.string.comprehensive_examination);
            case 2:
                return context.getString(R.string.my_tests);
        }
        return "";
    }


}
