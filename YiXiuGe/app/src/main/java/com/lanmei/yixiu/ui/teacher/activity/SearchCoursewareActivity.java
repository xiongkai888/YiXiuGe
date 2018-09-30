package com.lanmei.yixiu.ui.teacher.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.adapter.TutorialCoursewareListAdapter;
import com.lanmei.yixiu.api.YiXiuGeApi;
import com.lanmei.yixiu.bean.NotesBean;
import com.lanmei.yixiu.event.PublishCoursewareEvent;
import com.lanmei.yixiu.utils.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.DrawClickableEditText;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.InjectView;

/**
 * 搜索课件
 */
public class SearchCoursewareActivity extends BaseActivity  implements TextView.OnEditorActionListener {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    private SwipeRefreshController<NoPageListBean<NotesBean>> controller;
    private YiXiuGeApi api;
    @InjectView(R.id.keywordEditText)
    DrawClickableEditText mKeywordEditText;

    @Override
    public int getContentViewId() {
        return R.layout.activity_single_listview_search;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        mKeywordEditText.setFocusable(true);
        mKeywordEditText.setCursorVisible(true);
        mKeywordEditText.setFocusableInTouchMode(true);
        mKeywordEditText.requestFocus();//设置可编辑
        mKeywordEditText.setOnEditorActionListener(this);

        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.search_courseware);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        api = new YiXiuGeApi("app/notelist");
        api.addParams("type",2);
        TutorialCoursewareListAdapter adapter = new TutorialCoursewareListAdapter(this);
        smartSwipeRefreshLayout.initWithLinearLayout();
        smartSwipeRefreshLayout.setAdapter(adapter);
        controller = new SwipeRefreshController<NoPageListBean<NotesBean>>(this, smartSwipeRefreshLayout, api, adapter) {
        };
        smartSwipeRefreshLayout.setMode(SmartSwipeRefreshLayout.Mode.ONLY_PULL_UP);
        adapter.notifyDataSetChanged();
    }


    //删除课件的时候调用
    @Subscribe
    public void publishCoursewareEvent(PublishCoursewareEvent event) {
        controller.loadFirstPage();
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            String key = CommonUtils.getStringByTextView(v);
            if (StringUtils.isEmpty(key)) {
                UIHelper.ToastMessage(this, R.string.input_keyword);
                return false;
            }
            loadSearchCourse(key);
            return true;
        }
        return false;
    }

    private void loadSearchCourse(String keyword) {
        api.addParams("keyword", keyword);
        controller.loadFirstPage();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
