package com.medui.yixiu.ui.teacher.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.medui.yixiu.R;
import com.medui.yixiu.adapter.SelectTestStudentAdapter;
import com.medui.yixiu.api.YiXiuGeApi;
import com.medui.yixiu.bean.StudentsBean;
import com.medui.yixiu.event.SelectTestStudentEvent;
import com.medui.yixiu.utils.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;

import butterknife.InjectView;

/**
 * 选择考试学生
 */
public class SelectTestStudentActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar toolbar;
    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;

    @Override
    public int getContentViewId() {
        return R.layout.activity_single_listview_no;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("选择考试学生");
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        YiXiuGeApi api = new YiXiuGeApi("app/assess_user");
        api.addParams(CommonUtils.uid, api.getUserId(this));
        api.addParams("id", getIntent().getStringExtra("value"));

        SelectTestStudentAdapter adapter = new SelectTestStudentAdapter(this);
        adapter.setSelectStudentListener(new SelectTestStudentAdapter.SelectStudentListener() {
            @Override
            public void select(StudentsBean bean) {
                EventBus.getDefault().post(new SelectTestStudentEvent(bean));
                finish();
            }
        });
        smartSwipeRefreshLayout.initWithLinearLayout();
        smartSwipeRefreshLayout.setAdapter(adapter);
        SwipeRefreshController<NoPageListBean<StudentsBean>> controller = new SwipeRefreshController<NoPageListBean<StudentsBean>>(this, smartSwipeRefreshLayout, api, adapter) {
        };
        smartSwipeRefreshLayout.setMode(SmartSwipeRefreshLayout.Mode.NO_PAGE);
        controller.loadFirstPage();

    }

}
