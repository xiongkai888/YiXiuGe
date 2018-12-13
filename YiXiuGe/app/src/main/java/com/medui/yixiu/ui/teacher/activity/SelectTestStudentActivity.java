package com.medui.yixiu.ui.teacher.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.medui.yixiu.R;
import com.medui.yixiu.adapter.SelectTestStudentAdapter;
import com.medui.yixiu.api.YiXiuGeApi;
import com.medui.yixiu.bean.StudentsBean;
import com.medui.yixiu.event.SelectTestStudentEvent;
import com.medui.yixiu.helper.SimpleTextWatcher;
import com.medui.yixiu.utils.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.DrawClickableEditText;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.InjectView;

/**
 * 选择考试学生
 */
public class SelectTestStudentActivity extends BaseActivity implements TextView.OnEditorActionListener {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar toolbar;
    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    @InjectView(R.id.keywordEditText)
    DrawClickableEditText mKeywordEditText;
    private SwipeRefreshController<NoPageListBean<StudentsBean>> controller;
    private YiXiuGeApi api;
    private List<StudentsBean> studentsBeanList;
    private SelectTestStudentAdapter adapter;
    private boolean aBoolean;

    @Override
    public int getContentViewId() {
        return R.layout.activity_single_listview_search;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(R.string.select_students);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        mKeywordEditText.setFocusable(true);
        mKeywordEditText.setCursorVisible(true);
        mKeywordEditText.setFocusableInTouchMode(true);
        mKeywordEditText.requestFocus();//设置可编辑
        mKeywordEditText.setOnEditorActionListener(this);
        mKeywordEditText.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (StringUtils.isEmpty(s+"")){
                    adapter.setData(studentsBeanList);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        api = new YiXiuGeApi("app/assess_user");
        api.addParams(CommonUtils.uid, api.getUserId(this));
        api.addParams("id", getIntent().getStringExtra("value"));

        adapter = new SelectTestStudentAdapter(this);
        adapter.setSelectStudentListener(new SelectTestStudentAdapter.SelectStudentListener() {
            @Override
            public void select(StudentsBean bean) {
                EventBus.getDefault().post(new SelectTestStudentEvent(bean));
                finish();
            }
        });
        smartSwipeRefreshLayout.initWithLinearLayout();
        smartSwipeRefreshLayout.setAdapter(adapter);
        controller = new SwipeRefreshController<NoPageListBean<StudentsBean>>(this, smartSwipeRefreshLayout, api, adapter) {
            @Override
            public boolean onSuccessResponse(NoPageListBean<StudentsBean> response) {
                if (isFinishing()){
                    return false;
                }
                if (!aBoolean){
                    studentsBeanList = response.data;
                    aBoolean = !aBoolean;
                }
                return super.onSuccessResponse(response);
            }
        };
        smartSwipeRefreshLayout.setMode(SmartSwipeRefreshLayout.Mode.NO_PAGE);
        controller.loadFirstPage();

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            String key = CommonUtils.getStringByTextView(v);
            if (StringUtils.isEmpty(key)) {
                UIHelper.ToastMessage(this, R.string.input_keyword);
                return true;
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

}
