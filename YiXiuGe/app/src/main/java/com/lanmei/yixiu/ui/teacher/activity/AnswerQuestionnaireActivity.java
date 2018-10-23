package com.lanmei.yixiu.ui.teacher.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.adapter.AnswerQuestionnaireAdapter;
import com.lanmei.yixiu.api.YiXiuGeApi;
import com.lanmei.yixiu.bean.QuestionnaireManagementBean;
import com.lanmei.yixiu.event.AnswerQuestionnaireEvent;
import com.lanmei.yixiu.utils.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
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
        if (bundle != null) {
            managementBean = (QuestionnaireManagementBean) bundle.getSerializable("bean");
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
        if (StringUtils.isEmpty(list)) {
            UIHelper.ToastMessage(this,"暂无问卷题目");
            return;
        }
        List<String> stringList = answerNum(list);
        if (stringList.size() != list.size()){
            UIHelper.ToastMessage(this,"请先完善问卷答案");
            return;
        }
        YiXiuGeApi api = new YiXiuGeApi("app/quest_student_add");
        api.addParams("uid",api.getUserId(this)).addParams("qid",managementBean.getId()).addParams("result",CommonUtils.getJSONArrayByList(stringList));
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()){
                    return;
                }
                UIHelper.ToastMessage(getContext(),response.getMsg());
                EventBus.getDefault().post(new AnswerQuestionnaireEvent());
                finish();
            }
        });
    }

    private List<String> answerNum(List<QuestionnaireManagementBean.QuestBean> list) {
        List<String> stringList = new ArrayList<>();
        for (QuestionnaireManagementBean.QuestBean bean : list) {
            if (StringUtils.isSame(bean.getType(), CommonUtils.isOne)) {
                List<QuestionnaireManagementBean.QuestBean.SelectBean> beanList = bean.getSelect();
                for (QuestionnaireManagementBean.QuestBean.SelectBean selectBean : beanList) {
                    if (selectBean.isSelect()) {
                        stringList.add(selectBean.getText());
                    }
                }
            } else {
                if (StringUtils.isEmpty(bean.getAnswer())) {
                    stringList.add(bean.getAnswer());
                }
            }
        }
        return stringList;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }
}

