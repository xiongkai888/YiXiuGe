package com.lanmei.yixiu.ui.teacher.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.ui.teacher.presenter.UploadVideoListAdapter;
import com.lanmei.yixiu.ui.teacher.presenter.UploadVideoListBean;
import com.lanmei.yixiu.ui.teacher.presenter.UploadVideoListContract;
import com.lanmei.yixiu.ui.teacher.presenter.UploadVideoListPresenter;
import com.lanmei.yixiu.utils.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 上传视频
 */
public class UploadVideoListActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener, UploadVideoListContract.View {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar toolbar;


    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    @InjectView(R.id.pause_tv)
    TextView pauseTv;
    @InjectView(R.id.all_pitch_on_tv)
    TextView allPitchOnTv;
    @InjectView(R.id.delete_tv)
    TextView deleteTv;
    @InjectView(R.id.ll_bottom)
    LinearLayout ll_bottom;//底部
    UploadVideoListAdapter adapter;

    UploadVideoListContract.Presenter presenter;

    @Override
    public int getContentViewId() {
        return R.layout.activity_upload_video_list;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {

        toolbar.setOnMenuItemClickListener(this);
        toolbar.setTitle(getString(R.string.upload_video));
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        presenter = new UploadVideoListPresenter(this, this);
        smartSwipeRefreshLayout.initWithLinearLayout();
        adapter = new UploadVideoListAdapter(this, presenter);
        List<UploadVideoListBean> list = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            UploadVideoListBean bean = new UploadVideoListBean();
            list.add(bean);
        }
        adapter.setData(list);
        presenter.setList(list);
        smartSwipeRefreshLayout.setMode(SmartSwipeRefreshLayout.Mode.NO_PAGE);
        smartSwipeRefreshLayout.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        setState();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void setState() {
        if (isFinishing()) {
            return;
        }
        List<UploadVideoListBean> list = adapter.getData();
        if (StringUtils.isEmpty(list)) {
            presenter.setEdit(false);
            showBottom(false);
            toolbar.getMenu().clear();
        } else {
            boolean isEdit = presenter.isEdit();
            if (isEdit) {
                toolbar.inflateMenu(R.menu.menu_done);
                presenter.setList(list);
                showTextView(false);
            }
            toolbar.getMenu().clear();
            toolbar.inflateMenu(isEdit ? R.menu.menu_done : R.menu.menu_edit);
            presenter.setEdit(isEdit);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (StringUtils.isEmpty(adapter.getData())) {
            return true;
        }
        boolean isEdit = false;//是否是编辑状态
        if (item.getItemId() == R.id.action_edit) {
//            presenter.setList(adapter.getData());
            toolbar.getMenu().clear();
            toolbar.inflateMenu(R.menu.menu_done);
            isEdit = true;
        } else if (item.getItemId() == R.id.action_done) {
            toolbar.getMenu().clear();
            toolbar.inflateMenu(R.menu.menu_edit);
            isEdit = false;
        }
        presenter.setEdit(isEdit);
        adapter.notifyDataSetChanged();
        return true;
    }

    @OnClick({R.id.pause_tv, R.id.all_pitch_on_tv, R.id.delete_tv, R.id.ll_upload_video})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pause_tv:
                String ids = presenter.getIdsBySelectedAndNoRead();
                if (!presenter.isSelect() || StringUtils.isEmpty(ids)) {
                    UIHelper.ToastMessage(this, "请先选择");
                    break;
                }
                if (presenter.isAllSelect()) {//全部为已读
                    CommonUtils.developing(this);
                } else {//设置部分为已读
                    CommonUtils.developing(this);
                }
                break;
            case R.id.all_pitch_on_tv:
                presenter.setAllSelect(!presenter.isAllSelect());
                adapter.notifyDataSetChanged();
                break;
            case R.id.delete_tv:
                if (!presenter.isSelect()) {
                    UIHelper.ToastMessage(this, "请先选择");
                    break;
                }
                if (presenter.isAllSelect()) {//删除全部

                } else {//删除部分

                }
                CommonUtils.developing(this);
                break;
            case R.id.ll_upload_video://上传视频
                IntentUtil.startActivity(this, PublishCourseActivity.class);
                break;
        }

    }


    @Override
    public void showTextView(boolean isAllSelect) {
        pauseTv.setText(isAllSelect?R.string.all_pause:R.string.pause);
        allPitchOnTv.setText(isAllSelect?R.string.all_cancel:R.string.all_pitch_on);
        deleteTv.setText(isAllSelect?R.string.all_delete:R.string.delete);
    }

    @Override
    public void showBottom(boolean isShow) {
        ll_bottom.setVisibility(isShow?View.VISIBLE:View.GONE);
    }
}
