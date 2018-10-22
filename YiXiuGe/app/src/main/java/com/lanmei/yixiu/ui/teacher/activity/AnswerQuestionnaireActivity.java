package com.lanmei.yixiu.ui.teacher.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.adapter.AnswerQuestionnaireAdapter;
import com.lanmei.yixiu.bean.QuestionnaireManagementBean;
import com.lanmei.yixiu.utils.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.utils.StringUtils;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import java.util.List;

import butterknife.InjectView;

/**
 * 学生回答调查问卷
 */
public class AnswerQuestionnaireActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    private QuestionnaireManagementBean managementBean;
    private AnswerQuestionnaireAdapter adapter;

    public int getContentViewId() {
        return R.layout.activity_single_listview_no;
    }

    @Override
    public void initIntent(Intent intent) {
        super.initIntent(intent);
        Bundle bundle = intent.getBundleExtra("bundle");
        if (bundle != null){
            managementBean = (QuestionnaireManagementBean)bundle.getSerializable("bean");
        }
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(managementBean.getTitle());
        actionbar.setHomeAsUpIndicator(R.drawable.back);

//        EventBus.getDefault().register(this);

        adapter = new AnswerQuestionnaireAdapter(this);
        adapter.setData(managementBean.getQuest());
        smartSwipeRefreshLayout.initWithLinearLayout();
        smartSwipeRefreshLayout.setMode(SmartSwipeRefreshLayout.Mode.NO_PAGE);
        smartSwipeRefreshLayout.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_submit, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_submit:
                answer();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void answer() {
        List<QuestionnaireManagementBean.QuestBean> list = adapter.getData();
        if (StringUtils.isEmpty(list)){
            return;
        }

    }

    private boolean isAllAnswer(List<QuestionnaireManagementBean.QuestBean> list){
        for (QuestionnaireManagementBean.QuestBean bean:list){
            if (StringUtils.isSame(bean.getType(), CommonUtils.isOne)){
                List<QuestionnaireManagementBean.QuestBean.SelectBean> beanList = bean.getSelect();
                for (QuestionnaireManagementBean.QuestBean.SelectBean selectBean:beanList){
                    if (!selectBean.isSelect()){

                    }
                }
            }else {

            }
        }
        return false;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }
}

