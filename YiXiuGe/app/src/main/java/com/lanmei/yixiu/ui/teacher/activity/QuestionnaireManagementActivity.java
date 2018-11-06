package com.lanmei.yixiu.ui.teacher.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.adapter.QuestionnaireManagementAdapter;
import com.lanmei.yixiu.api.YiXiuGeApi;
import com.lanmei.yixiu.bean.QuestionnaireManagementBean;
import com.lanmei.yixiu.event.AddQuestionnaireEvent;
import com.lanmei.yixiu.event.AnswerQuestionnaireEvent;
import com.lanmei.yixiu.utils.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.utils.IntentUtil;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.InjectView;

/**
 * 问卷调查管理
 */
public class QuestionnaireManagementActivity extends BaseActivity  {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    QuestionnaireManagementAdapter mAdapter;
    private SwipeRefreshController<NoPageListBean<QuestionnaireManagementBean>> controller;
    private boolean isStudent;//true 学生  false 老师

    @Override
    public int getContentViewId() {
        return R.layout.activity_single_listview_no;
    }

    @Override
    public void initIntent(Intent intent) {
        super.initIntent(intent);
        isStudent = CommonUtils.isStudent(this);
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);

        mToolbar.setTitle(R.string.questionnaire_management);
        mToolbar.setNavigationIcon(R.drawable.back);
        if (!isStudent){
            mToolbar.inflateMenu(R.menu.menu_add_questionnaire);
            mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int id = item.getItemId();
                    if (id == R.id.action_add_questionnaire) {
                        IntentUtil.startActivity(getContext(), AddQuestionnaireActivity.class);
                    }
                    return false;
                }
            });
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        YiXiuGeApi api = new YiXiuGeApi(isStudent?"app/quest_student_list":"app/questions_list");
        api.addParams("uid", api.getUserId(this));

        mAdapter = new QuestionnaireManagementAdapter(this);
        mAdapter.setStudent(isStudent);
        smartSwipeRefreshLayout.initWithLinearLayout();
        smartSwipeRefreshLayout.setAdapter(mAdapter);
        controller = new SwipeRefreshController<NoPageListBean<QuestionnaireManagementBean>>(this, smartSwipeRefreshLayout, api, mAdapter) {
        };
        controller.loadFirstPage();



    }

    //添加问卷成功调用
    @Subscribe
    public void addQuestionnaireEvent(AddQuestionnaireEvent event){
        controller.loadFirstPage();
    }

    //学生回答问卷完成事件
    @Subscribe
    public void answerQuestionnaireEvent(AnswerQuestionnaireEvent event){
        controller.loadFirstPage();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
