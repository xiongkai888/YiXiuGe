package com.lanmei.yixiu.ui.home.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.adapter.TeacherDetailsPublishAdapter;
import com.xson.common.app.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 老师详情
 */
public class TeacherDetailsActivity extends BaseActivity {


    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    @InjectView(R.id.good_reputation_tv)
    TextView goodReputationTv;
    @InjectView(R.id.middle_reputation_tv)
    TextView middleReputationTv;
    @InjectView(R.id.bad_reputation_tv)
    TextView badReputationTv;

    TextView[] textViews = new TextView[3];

    @Override
    public int getContentViewId() {
        return R.layout.activity_teacher_details;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        textViews[0] = goodReputationTv;
        textViews[1] = middleReputationTv;
        textViews[2] = badReputationTv;

        fullScreen(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        TeacherDetailsPublishAdapter adapter = new TeacherDetailsPublishAdapter(this);
        adapter.setData(getList());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
    }


    private List<String> getList() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add("https://goss.veer.com/creative/vcg/veer/800water/veer-305535622.jpg");
        }
        return list;
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

    @OnClick({R.id.back_iv, R.id.message_iv, R.id.good_reputation_tv, R.id.middle_reputation_tv, R.id.bad_reputation_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.message_iv:
                break;
            case R.id.good_reputation_tv://好评
                setTextViewBg(goodReputationTv);
                break;
            case R.id.middle_reputation_tv://中评
                setTextViewBg(middleReputationTv);
                break;
            case R.id.bad_reputation_tv://差评
                setTextViewBg(badReputationTv);
                break;
        }
    }

    private void setTextViewBg(TextView view) {
        int size = textViews.length;
        for (int i = 0; i < size; i++) {
            textViews[i].setTextColor(getResources().getColor(R.color.color666));
            textViews[i].setBackgroundColor(getResources().getColor(R.color.colorF4F4));
        }
        view.setTextColor(getResources().getColor(R.color.color1593f0));
        view.setBackgroundColor(getResources().getColor(R.color.white));
    }

}
