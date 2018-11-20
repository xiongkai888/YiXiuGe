package com.medui.yixiu.ui.teacher.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.medui.yixiu.R;
import com.medui.yixiu.YiXiuApp;
import com.medui.yixiu.event.UpdateUploadEvent;
import com.medui.yixiu.ui.teacher.uploadvideo.UploadVideoBean;
import com.medui.yixiu.ui.teacher.uploadvideo.UploadVideoListAdapter;
import com.medui.yixiu.ui.teacher.uploadvideo.UploadVideoListContract;
import com.medui.yixiu.ui.teacher.uploadvideo.UploadVideoListPresenter;
import com.medui.yixiu.ui.teacher.uploadvideo.UploadVideoService;
import com.medui.yixiu.utils.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
        EventBus.getDefault().register(this);

        toolbar.setOnMenuItemClickListener(this);
        toolbar.setTitle(getString(R.string.upload_video));
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        presenter = new UploadVideoListPresenter(YiXiuApp.applicationContext, this);
        smartSwipeRefreshLayout.initWithLinearLayout();
        adapter = new UploadVideoListAdapter(this, presenter);
        smartSwipeRefreshLayout.setMode(SmartSwipeRefreshLayout.Mode.NO_PAGE);
        smartSwipeRefreshLayout.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        initService();
        setState();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUploadEvent(UpdateUploadEvent event) {
        int status = event.getStatus();
        switch (status) {
            case 0:
                L.d("AyncListObjects", "status：等待中：" + status);
                adapter.getData().set(0, event.getBean());
                adapter.notifyItemChanged(0);
                break;
            case 1:
                L.d("AyncListObjects", "status：正在上传：" + status);
                adapter.getData().set(0, event.getBean());
                adapter.notifyItemChanged(0);
                break;
            case 2:
                L.d("AyncListObjects", "status：上传视频阿里云成功：" + status);
                adapter.getData().set(0, event.getBean());
                adapter.notifyItemChanged(0);
                break;
            case 3:
                L.d("AyncListObjects", "status：上传视频到阿里云失败：" + status);
                adapter.getData().set(0, event.getBean());
                adapter.notifyItemChanged(0);
                break;
            case 4://上传视频成功后提交视频资料到后台也成功
                L.d("AyncListObjects", "status：上传视频成功后提交视频资料到后台也成功：" + status);
                adapter.setData(presenter.getUploadVideoList());
                adapter.notifyDataSetChanged();
                setState();
                break;
            case 5://上传视频成功后提交视频资料到后台失败
                L.d("AyncListObjects", "status：上传视频成功后提交视频资料到后台失败：" + status);
                adapter.getData().set(0, event.getBean());
                adapter.notifyItemChanged(0);
                break;
            case 6://启动发布视频
                L.d("AyncListObjects", "status：启动发布视频：" + status);
                initService();
                setState();
                break;
        }
    }

    private void setState() {
        if (isFinishing()) {
            return;
        }
        List<UploadVideoBean> list = adapter.getData();
        if (StringUtils.isEmpty(list)) {
            presenter.setEdit(false);
            showBottom(false);
            showTextView(false);
            toolbar.getMenu().clear();
        } else {
            boolean isEdit = presenter.isEdit();
            if (isEdit) {
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
                if (presenter.isAllSelect()) {//
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
                //                CommonUtils.developing(this);
                presenter.deleteBySelectBean();
                adapter.setData(presenter.getUploadVideoList());
                adapter.notifyDataSetChanged();
                initService();
                setState();
                break;
            case R.id.ll_upload_video://上传视频
                IntentUtil.startActivity(this, PublishCourseActivity.class);
                break;
        }

    }


    private void initService() {
        //构建绑定服务的Intent对象
        if (presenter.getUploadVideoCount() > 0) {
            adapter.setData(presenter.getUploadVideoList());
            adapter.notifyDataSetChanged();
            Intent uploadVideoService = new Intent(this, UploadVideoService.class);
            startService(uploadVideoService);
        }

    }

    @Override
    public void showTextView(boolean isAllSelect) {
        pauseTv.setText(isAllSelect ? R.string.all_pause : R.string.pause);
        allPitchOnTv.setText(isAllSelect ? R.string.all_cancel : R.string.all_pitch_on);
        deleteTv.setText(isAllSelect ? R.string.all_delete : R.string.delete);
    }


    @Override
    public void showBottom(boolean isShow) {
        ll_bottom.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

}
