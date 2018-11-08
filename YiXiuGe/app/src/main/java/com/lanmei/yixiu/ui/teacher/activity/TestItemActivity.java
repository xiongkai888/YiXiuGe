package com.lanmei.yixiu.ui.teacher.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.TextView;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.bean.StudentsBean;
import com.lanmei.yixiu.bean.TestListBean;
import com.lanmei.yixiu.event.SelectTestStudentEvent;
import com.lanmei.yixiu.utils.CommonUtils;
import com.lanmei.yixiu.utils.FormatTime;
import com.xson.common.app.BaseActivity;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 测试科目
 */
public class TestItemActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar toolbar;
    @InjectView(R.id.test_student_tv)
    TextView testStudentTv;
    @InjectView(R.id.test_time_tv)
    TextView testTimeTv;
    @InjectView(R.id.test_evaluate_name_tv)
    TextView testEvaluateNameTv;

    private TestListBean testListBean;
    private StudentsBean bean;
    private FormatTime formatTime;

    @Override
    public void initIntent(Intent intent) {
        super.initIntent(intent);
        Bundle bundle = intent.getBundleExtra("bundle");
        if (bundle != null) {
            testListBean = (TestListBean) bundle.getSerializable("bean");
        }
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_test_item;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        formatTime = new FormatTime(this);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        testStudentTv.setText(String.format(getString(R.string.test_student), ""));
        testTimeTv.setText(String.format(getString(R.string.test_time), ""));
        testEvaluateNameTv.setText(String.format(getString(R.string.test_evaluate_name), CommonUtils.getUserBean(this).getRealname()));

        if (testListBean == null) {
            return;
        }
        actionbar.setTitle(testListBean.getTitle());
        testTimeTv.setText(String.format(getString(R.string.test_time), testListBean.getE_time()));
    }

    @Subscribe
    public void selectTestStudentEvent(SelectTestStudentEvent event) {
        bean = event.getBean();
        testStudentTv.setText(String.format(getString(R.string.test_student), bean.getRealname()));
    }

    @OnClick({R.id.start_bt, R.id.test_student_tv})
    public void onViewClicked(View view) {
        if (testListBean == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.test_student_tv://选择评估学生
                IntentUtil.startActivity(this, SelectTestStudentActivity.class, testListBean.getId());
                break;
            case R.id.start_bt://开始评估
                if (bean == null) {
                    UIHelper.ToastMessage(this, "请选择评估学生");
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean", bean);
                bundle.putString("time",testListBean.getE_time());
                bundle.putString("id",testListBean.getId());
                IntentUtil.startActivity(this, StudentTestActivity.class,bundle);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
