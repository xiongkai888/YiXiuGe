package com.medui.yixiu.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.medui.yixiu.ui.message.fragment.MessageFragment;
import com.medui.yixiu.ui.message.fragment.QunFragment;


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
        }
        return "";
    }


}
