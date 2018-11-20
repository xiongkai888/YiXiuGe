package com.medui.yixiu.ui.message.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.medui.yixiu.R;
import com.medui.yixiu.adapter.SelectQuestionStudentsAdapter;
import com.medui.yixiu.api.YiXiuGeApi;
import com.medui.yixiu.bean.SelectQuestionStudentsBean;
import com.medui.yixiu.utils.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * 选择群成员
 */
public class GroupPickContactsActivity extends BaseActivity {


    @InjectView(R.id.toolbar)
    CenterTitleToolbar toolbar;
    SelectQuestionStudentsAdapter adapter;
    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    private SwipeRefreshController<NoPageListBean<SelectQuestionStudentsBean>> controller;
    /** members already in the group */
    private List<String> existMembers;
    /** if this is a new group */
    protected boolean isCreatingNewGroup;

    @Override
    public int getContentViewId() {
        return R.layout.activity_single_listview_no;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("选择群成员");
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        String groupId = getIntent().getStringExtra("groupId");
        if (StringUtils.isEmpty(groupId)) {// create new group
            isCreatingNewGroup = true;
        } else {
            // get members of the group
            EMGroup group = EMClient.getInstance().groupManager().getGroup(groupId);
            existMembers = group.getMembers();
            existMembers.add(group.getOwner());
            existMembers.addAll(group.getAdminList());
        }
        if(existMembers == null){
            existMembers = new ArrayList<>();
        }

        adapter = new SelectQuestionStudentsAdapter(this);

        YiXiuGeApi api = new YiXiuGeApi("app/getclassuser");
        api.addParams("uid",api.getUserId(this));
        api.addParams("type",1);
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
                List<String> stringList = getToBeAddMembers(list);
                for (String s:stringList){
                    L.d(L.TAG,s);
                }
                setResult(RESULT_OK, new Intent().putExtra("newmembers", stringList.toArray(new String[stringList.size()])));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * get selected members
     *
     * @return
     */
    private List<String> getToBeAddMembers(List<SelectQuestionStudentsBean> list) {
        List<String> members = new ArrayList<>();
        List<SelectQuestionStudentsBean.StudentBean> studentBeanList = isSelectStudent(list);
        int length = studentBeanList.size();
        for (int i = 0; i < length; i++) {
            String username = CommonUtils.HX_USER_HEAD+studentBeanList.get(i).getId();
            if (!existMembers.contains(username)) {
                members.add(username);
            }
        }
        return members;
    }

    private List<SelectQuestionStudentsBean.StudentBean> isSelectStudent(List<SelectQuestionStudentsBean> list){
        List<SelectQuestionStudentsBean.StudentBean> beanList = new ArrayList<>();
        if (StringUtils.isEmpty(list)){
            return beanList;
        }
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
