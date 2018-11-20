package com.medui.yixiu.ui.mine.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.widget.TextView;

import com.medui.yixiu.R;
import com.medui.yixiu.bean.MyTestsBean;
import com.xson.common.app.BaseActivity;
import com.xson.common.widget.CenterTitleToolbar;

import butterknife.InjectView;

/**
 * 评估结果（学生）
 */
public class StudentTestResultActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar toolbar;

    @InjectView(R.id.title_tv)
    TextView titleTv;
    @InjectView(R.id.grade_tv)
    TextView gradeTv;
    @InjectView(R.id.comment_tv)
    TextView commentTv;

    @Override
    public int getContentViewId() {
        return R.layout.activity_student_test_result;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.test_result);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

//        MyTestsBean bean =
        Bundle bundle = getIntent().getBundleExtra("bundle");
        if (bundle != null){
            MyTestsBean bean = (MyTestsBean)bundle.getSerializable("bean");
            if (bean != null){
                titleTv.setText(bean.getTitle());
                gradeTv.setText("评估结果："+bean.getGrade());
                commentTv.setText("学员整体表现情况："+bean.getComment());
            }
        }
    }

}
