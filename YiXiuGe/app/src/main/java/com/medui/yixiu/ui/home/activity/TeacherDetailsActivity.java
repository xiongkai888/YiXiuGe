package com.medui.yixiu.ui.home.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.medui.yixiu.R;
import com.medui.yixiu.adapter.TeacherDetailsCommentAdapter;
import com.medui.yixiu.adapter.TeacherDetailsPublishAdapter;
import com.medui.yixiu.api.YiXiuGeApi;
import com.medui.yixiu.bean.TeacherDetailsBean;
import com.medui.yixiu.utils.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.DataBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.helper.ImageHelper;
import com.xson.common.utils.StringUtils;
import com.xson.common.widget.CircleImageView;

import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 老师详情
 */
public class TeacherDetailsActivity extends BaseActivity {

    @InjectView(R.id.pic_iv)
    CircleImageView picIv;
    @InjectView(R.id.realname_tv)
    TextView realnameTv;
    @InjectView(R.id.teachingage_tv)
    TextView teachingageTv;
    @InjectView(R.id.info_tv)
    TextView infoTv;
    @InjectView(R.id.kec_tv)
    TextView kecTv;//主要科目
    @InjectView(R.id.intro_tv)
    TextView introTv;//简介
    @InjectView(R.id.ta_publish_tv)
    TextView taPublishTv;//TA的发布


    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
//    @InjectView(R.id.good_reputation_tv)
//    TextView goodReputationTv;
//    @InjectView(R.id.middle_reputation_tv)
//    TextView middleReputationTv;
//    @InjectView(R.id.bad_reputation_tv)
//    TextView badReputationTv;

    @InjectView(R.id.viewPager)
    ViewPager mViewPager;
    @InjectView(R.id.tabLayout)
    TabLayout mTabLayout;

    TeacherDetailsCommentAdapter teacherDetailsCommentAdapter;

    private String tid;
    private TeacherDetailsBean bean;//老师详情
//    TextView[] textViews = new TextView[3];

    @Override
    public int getContentViewId() {
        return R.layout.activity_teacher_details;
    }

    @Override
    public void initIntent(Intent intent) {
        super.initIntent(intent);
        tid = intent.getStringExtra("value");
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
//        textViews[0] = goodReputationTv;
//        textViews[1] = middleReputationTv;
//        textViews[2] = badReputationTv;

        fullScreen(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        loadTeacherDetails();


        teacherDetailsCommentAdapter = new TeacherDetailsCommentAdapter(getSupportFragmentManager(),tid);
//        mViewPager.setOffscreenPageLimit(3);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mViewPager.setAdapter(teacherDetailsCommentAdapter);

    }

    private void loadTeacherDetails() {
        YiXiuGeApi api = new YiXiuGeApi("app/teacher_details");
        api.addParams("tid", tid);
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<DataBean<TeacherDetailsBean>>() {
            @Override
            public void onResponse(DataBean<TeacherDetailsBean> response) {
                if (isFinishing()) {
                    return;
                }
                bean = response.data;
                setTeacherDetails();
            }
        });
    }

    private void setTeacherDetails() {
        if (bean == null) {
            return;
        }
        ImageHelper.load(this,bean.getPic(),picIv,null,true,R.drawable.default_pic,R.drawable.default_pic);
        kecTv.setText(bean.getKec());
        introTv.setText(bean.getIntro());
        realnameTv.setText(bean.getRealname());
        teachingageTv.setText(String.format(getString(R.string.teaching_age),bean.getTeachingage()+""));
        infoTv.setText(String.format(getString(R.string.teacher_info), StringUtils.isSame(bean.getSex(), CommonUtils.isOne)?getString(R.string.man):getString(R.string.woman),String.valueOf(bean.getAge()),bean.getCityname()));

        List<TeacherDetailsBean.VideoBean> list =  bean.getVideo();
        TeacherDetailsPublishAdapter adapter = new TeacherDetailsPublishAdapter(this);
        adapter.setData(list);
        recyclerView.setAdapter(adapter);


        taPublishTv.setText(String.format(getString(R.string.ta_publish),StringUtils.isEmpty(list)?CommonUtils.isZero:String.valueOf(list.size())));
    }


    /**
     * 通过设置全屏，设置状态栏透明
     *
     * @param activity
     */
    private void fullScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
                Window window = activity.getWindow();
                View decorView = window.getDecorView(); //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT); //导航栏颜色也可以正常设置 //
                window.setNavigationBarColor(Color.TRANSPARENT);
            } else {
                Window window = activity.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags |= flagTranslucentStatus; //
                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
            }
        }
    }

    @OnClick({R.id.back_iv, R.id.message_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.message_iv:
                break;
//            case R.id.good_reputation_tv://好评
//                setTextViewBg(goodReputationTv);
//                break;
//            case R.id.middle_reputation_tv://中评
//                setTextViewBg(middleReputationTv);
//                break;
//            case R.id.bad_reputation_tv://差评
//                setTextViewBg(badReputationTv);
//                break;
        }
    }

//    private void setTextViewBg(TextView view) {
//        int size = textViews.length;
//        for (int i = 0; i < size; i++) {
//            textViews[i].setTextColor(getResources().getColor(R.color.color666));
//            textViews[i].setBackgroundColor(getResources().getColor(R.color.colorF4F4));
//        }
//        view.setTextColor(getResources().getColor(R.color.color1593f0));
//        view.setBackgroundColor(getResources().getColor(R.color.white));
//    }

}
