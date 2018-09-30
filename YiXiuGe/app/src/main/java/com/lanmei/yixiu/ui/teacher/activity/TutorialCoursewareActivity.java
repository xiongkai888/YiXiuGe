package com.lanmei.yixiu.ui.teacher.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.adapter.TutorialCoursewareAdapter;
import com.lanmei.yixiu.api.YiXiuGeApi;
import com.lanmei.yixiu.bean.CourseClassifyBean;
import com.lanmei.yixiu.utils.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.IntentUtil;
import com.xson.common.widget.CenterTitleToolbar;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 *教程课件
 */
public class TutorialCoursewareActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;

    @InjectView(R.id.viewPager)
    ViewPager mViewPager;
    @InjectView(R.id.tabLayout)
    TabLayout mTabLayout;
    TutorialCoursewareAdapter mAdapter;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_course;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.tutorial_courseware);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        mAdapter = new TutorialCoursewareAdapter(getSupportFragmentManager());
//        mViewPager.setOffscreenPageLimit(3);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        loadCourseClassify();
    }


    private void loadCourseClassify() {
        YiXiuGeApi api = new YiXiuGeApi("app/course_list");
        HttpClient.newInstance(this).request(api, new BeanRequest.SuccessListener<NoPageListBean<CourseClassifyBean>>() {
            @Override
            public void onResponse(NoPageListBean<CourseClassifyBean> response) {
                if (isFinishing()) {
                    return;
                }
                mAdapter.setList(getContext(),response.data);
                mViewPager.setAdapter(mAdapter);
                mTabLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        CommonUtils.setTabLayoutIndicator(mTabLayout, 5, 5);
                    }
                });
            }
        });
    }

    @OnClick(R.id.keywordEditText)
    public void onViewClicked() {
        IntentUtil.startActivity(this, SearchCoursewareActivity.class);
    }
}
