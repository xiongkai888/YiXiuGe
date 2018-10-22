package com.lanmei.yixiu.ui.teacher.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.adapter.SelectQuestionStudentsAdapter;
import com.lanmei.yixiu.api.YiXiuGeApi;
import com.lanmei.yixiu.bean.SelectQuestionStudentsBean;
import com.lanmei.yixiu.event.SelectQuestionStudentsEvent;
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

    @Override
    public int getContentViewId() {
        return R.layout.activity_single_listview_no;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("选择问卷学生");
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        adapter = new SelectQuestionStudentsAdapter(this);

        YiXiuGeApi api = new YiXiuGeApi("app/getclassuser");

        adapter = new SelectQuestionStudentsAdapter(this);
        smartSwipeRefreshLayout.initWithLinearLayout();
        smartSwipeRefreshLayout.setAdapter(adapter);
        controller = new SwipeRefreshController<NoPageListBean<SelectQuestionStudentsBean>>(this, smartSwipeRefreshLayout, api, adapter) {
        };
        smartSwipeRefreshLayout.setMode(SmartSwipeRefreshLayout.Mode.NO_PAGE);
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
                if (StringUtils.isEmpty(list) || StringUtils.isEmpty(listList)){
                    UIHelper.ToastMessage(this,"请选择问卷学生");
                    break;
                }
                EventBus.getDefault().post(new SelectQuestionStudentsEvent(listList,getCids(list)));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public String getCids(List<SelectQuestionStudentsBean> list){
        String cids = "";
        if (StringUtils.isEmpty(list)){
            return cids;
        }
        for (SelectQuestionStudentsBean bean:list){
            List<SelectQuestionStudentsBean.StudentBean> studentBeanList = bean.getStudent();
            for (SelectQuestionStudentsBean.StudentBean studentBean:studentBeanList){
                if (studentBean.isSelect()){
                    cids += bean.getId()+",";
                    break;
                }
            }
        }
        return getSubString(cids);
    }

    /**
     * 去掉后面最后一个字符
     *
     * @param decs
     * @return
     */
    public String getSubString(String decs) {
        if (StringUtils.isEmpty(decs)) {
            return decs;
        }
        return decs.substring(0, decs.length() - 1);
    }

    private List<SelectQuestionStudentsBean.StudentBean> isSelectStudent(List<SelectQuestionStudentsBean> list){
        if (StringUtils.isEmpty(list)){
            return null;
        }
        List<SelectQuestionStudentsBean.StudentBean> beanList = new ArrayList<>();
        for (SelectQuestionStudentsBean bean:list){
            List<SelectQuestionStudentsBean.StudentBean> studentBeanList = bean.getStudent();
            for (SelectQuestionStudentsBean.StudentBean studentBean:studentBeanList){
                if (studentBean.isSelect()){
                    beanList.add(studentBean);
                }
            }
        }
        return beanList;
    }

}
