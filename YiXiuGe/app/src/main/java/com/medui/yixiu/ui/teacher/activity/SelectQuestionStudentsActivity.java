package com.medui.yixiu.ui.teacher.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import com.medui.yixiu.R;
import com.medui.yixiu.adapter.SelectQuestionStudentsAdapter;
import com.medui.yixiu.api.YiXiuGeApi;
import com.medui.yixiu.bean.SelectQuestionStudentsBean;
import com.medui.yixiu.event.SelectQuestionStudentsEvent;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * 选择问卷学生
 */
public class SelectQuestionStudentsActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar toolbar;
    SelectQuestionStudentsAdapter adapter;
    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    private SwipeRefreshController<NoPageListBean<SelectQuestionStudentsBean>> controller;
    private List<SelectQuestionStudentsBean> list;//所有的班级和学生列表

    @Override
    public int getContentViewId() {
        return R.layout.activity_single_listview_no;
    }

    @Override
    public void initIntent(Intent intent) {
        super.initIntent(intent);
        Bundle bundle = intent.getBundleExtra("bundle");
        if (bundle != null) {
            list = (List<SelectQuestionStudentsBean>) bundle.getSerializable("list");
        }
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(R.string.choose_questionnaires_students);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        adapter = new SelectQuestionStudentsAdapter(this);
        smartSwipeRefreshLayout.setMode(SmartSwipeRefreshLayout.Mode.NO_PAGE);
        smartSwipeRefreshLayout.initWithLinearLayout();

        if (!StringUtils.isEmpty(list)) {
            adapter.setData(list);
            smartSwipeRefreshLayout.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            return;
        }

        YiXiuGeApi api = new YiXiuGeApi("app/getclassuser");
        adapter = new SelectQuestionStudentsAdapter(this);
        smartSwipeRefreshLayout.setAdapter(adapter);
        controller = new SwipeRefreshController<NoPageListBean<SelectQuestionStudentsBean>>(this, smartSwipeRefreshLayout, api, adapter) {
            @Override
            public boolean onSuccessResponse(NoPageListBean<SelectQuestionStudentsBean> response) {
                if (isFinishing()) {
                    return true;
                }
                list = response.data;
                if (!StringUtils.isEmpty(list)) {
                    int size = list.size();
                    for (int i = 0; i < size; i++) {
                        SelectQuestionStudentsBean bean = list.get(i);
                        List<SelectQuestionStudentsBean.StudentBean> studentBeanList = bean.getStudent();
                        if (!StringUtils.isEmpty(studentBeanList)) {
                            int sizeJ = studentBeanList.size();
                            for (int j = 0; j < sizeJ; j++) {
                                studentBeanList.get(j).setParent_id(bean.getParent_id());
                            }
                        }
                    }
                    return false;
                }
                return false;
            }
        };
        controller.loadFirstPage();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sure, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sure:
                List<SelectQuestionStudentsBean> list = adapter.getData();
                List<SelectQuestionStudentsBean.StudentBean> listList = isSelectStudent(list);
                if (StringUtils.isEmpty(list) || StringUtils.isEmpty(listList)) {
                    UIHelper.ToastMessage(this, R.string.choose_questionnaires_students_q);
                    break;
                }
                EventBus.getDefault().post(new SelectQuestionStudentsEvent(listList, list));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private List<SelectQuestionStudentsBean.StudentBean> isSelectStudent(List<SelectQuestionStudentsBean> list) {
        if (StringUtils.isEmpty(list)) {
            return null;
        }
        List<SelectQuestionStudentsBean.StudentBean> beanList = new ArrayList<>();
        for (SelectQuestionStudentsBean bean : list) {
            List<SelectQuestionStudentsBean.StudentBean> studentBeanList = bean.getStudent();
            for (SelectQuestionStudentsBean.StudentBean studentBean : studentBeanList) {
                if (studentBean.isSelect()) {
                    beanList.add(studentBean);
                }
            }
        }
        return beanList;
    }

}
