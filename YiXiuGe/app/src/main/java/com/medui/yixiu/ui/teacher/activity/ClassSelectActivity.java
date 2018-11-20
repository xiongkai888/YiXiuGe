package com.medui.yixiu.ui.teacher.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import com.medui.yixiu.R;
import com.medui.yixiu.adapter.ClassSelectAdapter;
import com.medui.yixiu.bean.ClassSelectBean;
import com.medui.yixiu.event.ClassSelectEvent;
import com.xson.common.app.BaseActivity;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * 班级选择
 */
public class ClassSelectActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    private List<ClassSelectBean> classSelectBeans;

    public int getContentViewId() {
        return R.layout.activity_single_listview_no;
    }

    @Override
    public void initIntent(Intent intent) {
        super.initIntent(intent);
        Bundle bundle = intent.getBundleExtra("bundle");
        if (bundle != null){
            classSelectBeans = (List<ClassSelectBean>)bundle.getSerializable("bean");
        }
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.class_select);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

//        EventBus.getDefault().register(this);

        ClassSelectAdapter adapter = new ClassSelectAdapter(this);
        adapter.setData(classSelectBeans);
        smartSwipeRefreshLayout.initWithLinearLayout();
        smartSwipeRefreshLayout.setMode(SmartSwipeRefreshLayout.Mode.NO_PAGE);
        smartSwipeRefreshLayout.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
                if (!isChoose()){
                    UIHelper.ToastMessage(this,"请先选择班级");
                    break;
                }
                EventBus.getDefault().post(new ClassSelectEvent(getList()));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isChoose(){
        for (ClassSelectBean bean : classSelectBeans){
            List<ClassSelectBean.XiajiBean> xiajiBeanList = bean.getXiaji();
            for (ClassSelectBean.XiajiBean xiajiBean:xiajiBeanList){
                if (xiajiBean.isChoose()){
                    return xiajiBean.isChoose();
                }
            }
        }
        return false;
    }

    private List<ClassSelectBean.XiajiBean> getList(){
        List<ClassSelectBean.XiajiBean> list = new ArrayList<>();
        for (ClassSelectBean bean : classSelectBeans){
            List<ClassSelectBean.XiajiBean> xiajiBeanList = bean.getXiaji();
            for (ClassSelectBean.XiajiBean xiajiBean:xiajiBeanList){
                if (xiajiBean.isChoose()){
                    list.add(xiajiBean);
                }
            }
        }
        return list;
    }

//    @Subscribe
//    public void classSelectEvent(ClassSelectEvent event){
//        finish();
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }
}

