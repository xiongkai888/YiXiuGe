package com.lanmei.yixiu.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lanmei.yixiu.ui.message.fragment.MessageFragment;
import com.lanmei.yixiu.ui.message.fragment.QunFragment;
import com.lanmei.yixiu.ui.mine.MineStudentFragment;


/**
 * 消息
 */
public class MessageAdapter extends FragmentPagerAdapter {


    public MessageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MessageFragment();
            case 1:
                return new QunFragment();
            case 2:
                return new MineStudentFragment();
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
                return "消息";
            case 1:
                return "群聊";
            case 2:
                return "我的学生";
        }
        return "I am in";
    }


}
