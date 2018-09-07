package com.lanmei.yixiu.ui.teacher.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.adapter.PublishCourseClassifyAdapter;
import com.lanmei.yixiu.api.YiXiuGeApi;
import com.lanmei.yixiu.bean.CourseClassifyBean;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;

import butterknife.InjectView;

/**
 * 发布教程
 */
public class PublishCourseActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    @InjectView(R.id.name_tv)
    TextView nameTv;
    private PublishCourseClassifyAdapter adapter;

    @Override
    public int getContentViewId() {
        return R.layout.activity_publish_course;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.publish_course);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        adapter = new PublishCourseClassifyAdapter(this);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setChooseCourseClassifyListener(new PublishCourseClassifyAdapter.ChooseCourseClassifyListener() {
            @Override
            public void chooseCourseClassify(CourseClassifyBean bean) {
                nameTv.setText(bean.getName());
            }
        });
        loadCourseClassify();
    }

    private void loadCourseClassify() {
        YiXiuGeApi api = new YiXiuGeApi("app/course_list");
        HttpClient.newInstance(this).request(api, new BeanRequest.SuccessListener<NoPageListBean<CourseClassifyBean>>() {
            @Override
            public void onResponse(NoPageListBean<CourseClassifyBean> response) {
                if (isFinishing()) {
                    return;
                }
                adapter.setData(response.data);
                recyclerView.setAdapter(adapter);
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
            UIHelper.ToastMessage(this,R.string.developing);
        }
        return super.onOptionsItemSelected(item);
    }

}
