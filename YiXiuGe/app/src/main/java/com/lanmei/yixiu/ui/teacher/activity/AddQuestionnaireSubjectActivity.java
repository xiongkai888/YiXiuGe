package com.lanmei.yixiu.ui.teacher.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.adapter.QuestionnaireSubjectAdapter;
import com.lanmei.yixiu.bean.QuestionnaireSubjectBean;
import com.lanmei.yixiu.event.QuestionnaireSubjectEvent;
import com.xson.common.app.BaseActivity;
import com.xson.common.utils.IntentUtil;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.EmptyRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * 添加问卷题目
 */
public class AddQuestionnaireSubjectActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;

    @InjectView(R.id.recyclerView)
    EmptyRecyclerView mRecyclerView;
    @InjectView(R.id.empty_view)
    View mEmptyView;
    private List<QuestionnaireSubjectBean> list;
    private QuestionnaireSubjectAdapter adapter;

    @Override
    public int getContentViewId() {
        return R.layout.include_single_recyclerview;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.add_questionnaire_subject);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        EventBus.getDefault().register(this);

        list = new ArrayList<>();

        adapter = new QuestionnaireSubjectAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setEmptyView(mEmptyView);
        mRecyclerView.setAdapter(adapter);
    }

    @Subscribe
    public void questionnaireSubjectEvent(QuestionnaireSubjectEvent event){
        list.add(event.getBean());
        adapter.setData(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_option, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_option:
                IntentUtil.startActivity(this,AddQuestionnaireOptionActivity.class);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
