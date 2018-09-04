package com.lanmei.yixiu.ui.message;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.adapter.MessageAdapter;
import com.xson.common.app.BaseFragment;

import butterknife.InjectView;


/**
 * Created by xkai on 2018/7/13.
 * 我的
 */

public class MessageFragment extends BaseFragment {


    @InjectView(R.id.viewPager)
    ViewPager mViewPager;
    @InjectView(R.id.tabLayout)
    TabLayout mTabLayout;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_message;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        mViewPager.setAdapter(new MessageAdapter(getChildFragmentManager()));
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
