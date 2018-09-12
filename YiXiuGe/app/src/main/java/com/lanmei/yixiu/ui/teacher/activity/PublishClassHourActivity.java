package com.lanmei.yixiu.ui.teacher.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.api.YiXiuGeApi;
import com.lanmei.yixiu.utils.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.widget.CenterTitleToolbar;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 发布课时
 */
public class PublishClassHourActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.subjects_tv)
    TextView subjectsTv;
    @InjectView(R.id.class_tv)
    TextView classTv;
    @InjectView(R.id.classroom_tv)
    TextView classroomTv;
    @InjectView(R.id.start_time_tv)
    TextView startTimeTv;
    @InjectView(R.id.end_time_tv)
    TextView endTimeTv;

    public int getContentViewId() {
        return R.layout.activity_publish_class_hour;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.publish_class_hour);
        actionbar.setHomeAsUpIndicator(R.drawable.back);
    }

    //获取课程（科目）
    private void initSubjects() {
        YiXiuGeApi api = new YiXiuGeApi("app/getteaching");
        api.addParams("uid", api.getUserId(this));
        HttpClient.newInstance(this).request(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {

            }
        });
    }

    //获取教室
    private void initClassroom() {
        YiXiuGeApi api = new YiXiuGeApi("app/getroom");
        HttpClient.newInstance(this).request(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {

            }
        });
    }

    //获取班级
    private void initClass() {
        YiXiuGeApi api = new YiXiuGeApi("app/getclass");
        HttpClient.newInstance(this).request(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_publish, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_publish) {
            CommonUtils.developing(this);
        }
        return super.onOptionsItemSelected(item);
    }


    @OnClick({R.id.ll_subjects, R.id.ll_class, R.id.ll_classroom, R.id.ll_start_time, R.id.ll_end_time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_subjects://选择科目
//                initSubjects();
                CommonUtils.developing(this);
                break;
            case R.id.ll_class://选择班级
                CommonUtils.developing(this);
//                initClass();
                break;
            case R.id.ll_classroom://所在教室
//                initClassroom();
                CommonUtils.developing(this);
                break;
            case R.id.ll_start_time://开始时间
                CommonUtils.developing(this);
                break;
            case R.id.ll_end_time://结束时间
                CommonUtils.developing(this);
                break;
        }
    }
}

