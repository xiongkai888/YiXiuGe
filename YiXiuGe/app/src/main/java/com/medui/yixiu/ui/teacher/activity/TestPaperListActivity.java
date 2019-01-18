package com.medui.yixiu.ui.teacher.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.medui.yixiu.R;
import com.medui.yixiu.adapter.TestPaperListAdapter;
import com.medui.yixiu.api.YiXiuGeApi;
import com.medui.yixiu.bean.TestPaperBean;
import com.medui.yixiu.utils.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import butterknife.InjectView;

/**
 * 考试管理
 */
public class TestPaperListActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;

    @Override
    public int getContentViewId() {
        return R.layout.activity_single_listview;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {

        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.my_test_paper);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        YiXiuGeApi api = new YiXiuGeApi("app/texamlist");
        api.addParams(CommonUtils.uid, api.getUserId(this));

        TestPaperListAdapter adapter = new TestPaperListAdapter(this);
        smartSwipeRefreshLayout.initWithLinearLayout();
        smartSwipeRefreshLayout.setAdapter(adapter);
        SwipeRefreshController<NoPageListBean<TestPaperBean>> controller = new SwipeRefreshController<NoPageListBean<TestPaperBean>>(this, smartSwipeRefreshLayout, api, adapter) {
        };
        controller.loadFirstPage();
    }

}
