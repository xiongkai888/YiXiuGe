package com.medui.yixiu.ui.teacher.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.medui.yixiu.R;
import com.medui.yixiu.adapter.SelectStudentsAdapter;
import com.medui.yixiu.api.YiXiuGeApi;
import com.medui.yixiu.bean.SelectStudentsBean;
import com.medui.yixiu.ui.teacher.presenter.SelectStudentsContract;
import com.medui.yixiu.ui.teacher.presenter.SelectStudentsPresenter;
import com.medui.yixiu.utils.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.EmptyRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 选择考试学生
 */
public class SelectStudentsActivity extends BaseActivity implements SelectStudentsContract.View {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar toolbar;
    @InjectView(R.id.ll_choose)
    LinearLayout llChoose;

    @InjectView(R.id.recyclerView)
    EmptyRecyclerView mRecyclerView;
    @InjectView(R.id.empty_view)
    View mEmptyView;

    @InjectView(R.id.all_select_iv)
    ImageView allSelectIv;
    @InjectView(R.id.all_select_tv)
    TextView allSelectTv;

    @InjectView(R.id.invert_select_iv)
    ImageView invertSelectIv;
    @InjectView(R.id.invert_select_tv)
    TextView invertSelectTv;

    SelectStudentsAdapter adapter;

    SelectStudentsPresenter presenter;

    @Override
    public int getContentViewId() {
        return R.layout.activity_select_students;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(R.string.select_students);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        presenter = new SelectStudentsPresenter(this);
        llChoose.setVisibility(View.VISIBLE);
        adapter = new SelectStudentsAdapter(this);
        adapter.setData(getSelectStudentsBeanList());
        presenter.setList(adapter.getData());
        adapter.setPresenter(presenter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setEmptyView(mEmptyView);
        mRecyclerView.setAdapter(adapter);

        YiXiuGeApi api = new YiXiuGeApi("app/getclassuser");
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()){
                    return;
                }
            }
        });

    }

    private List<SelectStudentsBean> getSelectStudentsBeanList() {
        List<SelectStudentsBean> list = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            SelectStudentsBean bean = new SelectStudentsBean();
            bean.setName(getString(R.string.name) + (i + 1));
            list.add(bean);
        }
        return list;
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
                if (!presenter.isSelect()) {
                    UIHelper.ToastMessage(this, getString(R.string.first_select_students));
                    break;
                }
                CommonUtils.developing(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void showAllSelect(boolean isAllSelect) {
        allSelectIv.setImageResource(isAllSelect ? R.drawable.pay_on : R.drawable.pay_off);
        allSelectTv.setText(isAllSelect ? getString(R.string.select_all) : getString(R.string.select_all_no));
    }

    @Override
    public void showInvertSelect(boolean isInvertSelect) {
        invertSelectIv.setImageResource(isInvertSelect ? R.drawable.pay_on : R.drawable.pay_off);
        invertSelectTv.setText(isInvertSelect ? getString(R.string.invert_all) : getString(R.string.invert_all_no));
    }

    @OnClick({R.id.ll_all_select, R.id.ll_invert_select})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_all_select://全选
                presenter.setAllSelect(!presenter.isAllSelect());
                adapter.notifyDataSetChanged();
                break;
            case R.id.ll_invert_select://反选
                presenter.setInvertSelect(!presenter.isInvert());
                adapter.notifyDataSetChanged();
                break;
        }
    }

}
