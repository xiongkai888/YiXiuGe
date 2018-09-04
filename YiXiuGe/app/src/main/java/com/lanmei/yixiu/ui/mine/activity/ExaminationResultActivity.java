package com.lanmei.yixiu.ui.mine.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.adapter.ExaminationResultAdapter;
import com.lanmei.yixiu.bean.ExaminationResultBean;
import com.xson.common.app.BaseActivity;
import com.xson.common.widget.CenterTitleToolbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;


/**
 * 考试结果
 */
public class ExaminationResultActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    ExaminationResultAdapter examinationResultAdapter;


    @Override
    public int getContentViewId() {
        return R.layout.activity_examination_result;
    }


    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.examination_result);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 10));
        examinationResultAdapter = new ExaminationResultAdapter(this);
        examinationResultAdapter.setData(getList());
        recyclerView.setAdapter(examinationResultAdapter);
        recyclerView.setNestedScrollingEnabled(false);

    }


    private List<ExaminationResultBean> getList() {
        List<ExaminationResultBean> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            ExaminationResultBean bean = new ExaminationResultBean();
            if (i%2 == 0){
                bean.setRight(true);
            }
            list.add(bean);
        }
        return list;
    }


}
