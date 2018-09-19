package com.lanmei.yixiu.ui.course;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.adapter.CourseAdapter;
import com.lanmei.yixiu.api.YiXiuGeApi;
import com.lanmei.yixiu.bean.CourseClassifyBean;
import com.lanmei.yixiu.ui.course.activity.SearchCourseActivity;
import com.xson.common.app.BaseFragment;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.IntentUtil;
import com.xson.common.widget.CenterTitleToolbar;

import java.lang.reflect.Field;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * Created by xkai on 2018/7/13.
 * 教程
 */

public class CourseFragment extends BaseFragment {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar toolbar;
    @InjectView(R.id.viewPager)
    ViewPager mViewPager;
    @InjectView(R.id.tabLayout)
    TabLayout mTabLayout;
    CourseAdapter mAdapter;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_course;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        toolbar.setTitle(getString(R.string.jiao_cheng));

        mAdapter = new CourseAdapter(getChildFragmentManager());
//        mViewPager.setOffscreenPageLimit(3);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        loadCourseClassify();

    }


    private void loadCourseClassify() {
        YiXiuGeApi api = new YiXiuGeApi("app/course_list");
        HttpClient.newInstance(context).request(api, new BeanRequest.SuccessListener<NoPageListBean<CourseClassifyBean>>() {
            @Override
            public void onResponse(NoPageListBean<CourseClassifyBean> response) {
                if (mViewPager == null) {
                    return;
                }
                mAdapter.setList(response.data);
                mViewPager.setAdapter(mAdapter);
                mTabLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        setIndicator(mTabLayout, 5, 5);
                    }
                });
            }
        });
    }

    public void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.keywordEditText)
    public void onViewClicked() {
        IntentUtil.startActivity(context, SearchCourseActivity.class);
    }
}
