package com.lanmei.yixiu.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lanmei.yixiu.ui.mine.fragment.ExaminationFragment;


/**
 * 考试
 */
public class ExaminationAdapter extends FragmentPagerAdapter {


    public ExaminationAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        ExaminationFragment fragment = new ExaminationFragment();
        Bundle bundle = new Bundle();
        switch (position) {//0待支付|1待接单|2待处理|3出发中|4已到达|5服务中|6服务完成|7确认完成|8待评价|9 全部|10取消
            case 0:
                bundle.putString("status","");//全部
                break;
            case 1:
                bundle.putString("status","1");//执行中
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
                return "随堂测试";
            case 1:
                return "综合考试";
        }
        return "";
    }


}
