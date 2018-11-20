package com.medui.yixiu.ui.home.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hyphenate.chatuidemo.Constant;
import com.hyphenate.chatuidemo.ui.ChatActivity;
import com.medui.yixiu.R;
import com.medui.yixiu.adapter.TeacherDetailsCommentAdapter;
import com.medui.yixiu.adapter.TeacherDetailsPublishAdapter;
import com.medui.yixiu.api.YiXiuGeApi;
import com.medui.yixiu.bean.TeacherDetailsBean;
import com.medui.yixiu.utils.CommonUtils;
import com.medui.yixiu.widget.SlideDetailsLayout;
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
public class TeacherDetailsSubActivity extends BaseActivity implements SlideDetailsLayout.OnSlideDetailsListener{

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

    @InjectView(R.id.slideDetailsLayout)
    SlideDetailsLayout slideDetailsLayout;
    @InjectView(R.id.fab_up)
    FloatingActionButton fabUp;
    @InjectView(R.id.scrollView)
    ScrollView scrollView;

    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;

    @InjectView(R.id.viewPager)
    ViewPager mViewPager;
    @InjectView(R.id.tabLayout)
    TabLayout mTabLayout;

    private String tid;
    private TeacherDetailsBean bean;//老师详情

    @Override
    public int getContentViewId() {
        return R.layout.activity_teacher_details_sub;
    }

    @Override
    public void initIntent(Intent intent) {
        super.initIntent(intent);
        tid = intent.getStringExtra("value");
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        loadTeacherDetails();
        fabUp.hide();
        slideDetailsLayout.setOnSlideDetailsListener(this);

        mViewPager.setOffscreenPageLimit(2);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mViewPager.setAdapter(new TeacherDetailsCommentAdapter(getSupportFragmentManager(),tid));

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
        adapter.setDetailsBean(bean);
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

    @OnClick({R.id.back_iv, R.id.message_iv,R.id.fab_up,R.id.pull_up_view})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.message_iv:
                if (bean == null){
                    return;
                }
                // start chat acitivity
                Intent intent = new Intent(this, ChatActivity.class);
                // it's single chat
                intent.putExtra(Constant.EXTRA_USER_ID, CommonUtils.HX_USER_HEAD+bean.getId());
                startActivity(intent);
                break;
            case R.id.fab_up://滚到顶部
                scrollView.smoothScrollTo(0, 0);
                slideDetailsLayout.smoothClose(true);
                break;
            case R.id.pull_up_view://上拉查看评论
                slideDetailsLayout.smoothOpen(true);
                break;
        }
    }

    @Override
    public void onStatusChanged(SlideDetailsLayout.Status status) {
        //当前为图文详情页
        if (status == SlideDetailsLayout.Status.OPEN) {
            fabUp.show();
        } else {
            //当前为商品详情页
            fabUp.hide();
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
