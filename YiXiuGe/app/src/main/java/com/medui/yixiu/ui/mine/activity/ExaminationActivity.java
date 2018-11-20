package com.medui.yixiu.ui.mine.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.medui.yixiu.R;
import com.medui.yixiu.adapter.ExaminationAdapter;
import com.xson.common.app.BaseActivity;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * （学生）考试
 */
public class ExaminationActivity extends BaseActivity {

    @InjectView(R.id.tabLayout)
    TabLayout mTabLayout;
    @InjectView(R.id.viewPager)
    ViewPager mViewPager;

    @Override
    public int getContentViewId() {
        return R.layout.activity_my_evaluate;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {

        mViewPager.setAdapter(new ExaminationAdapter(this,getSupportFragmentManager()));
        mViewPager.setOffscreenPageLimit(2);
        mTabLayout.setupWithViewPager(mViewPager);

    }


    @OnClick(R.id.back_iv)
    public void onViewClicked() {
        finish();
    }

}
