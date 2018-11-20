package com.medui.yixiu.ui.message.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.medui.yixiu.R;
import com.medui.yixiu.adapter.SiXinAdapter;
import com.medui.yixiu.api.YiXiuGeApi;
import com.medui.yixiu.bean.SiXinBean;
import com.medui.yixiu.utils.CommonUtils;
import com.medui.yixiu.widget.SwipeItemLayout;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import butterknife.InjectView;

/**
 * 私信
 */
public class SiXinActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    private SiXinAdapter adapter;
    private SwipeRefreshController<NoPageListBean<SiXinBean>> controller;

    @Override
    public int getContentViewId() {
        return R.layout.activity_single_listview_no;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.private_letter);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        YiXiuGeApi api = new YiXiuGeApi("app/sixin");
        api.addParams("uid", api.getUserId(this));

        adapter = new SiXinAdapter(this);
        smartSwipeRefreshLayout.initWithLinearLayout();
        smartSwipeRefreshLayout.setAdapter(adapter);
        smartSwipeRefreshLayout.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(this));
        controller = new SwipeRefreshController<NoPageListBean<SiXinBean>>(this, smartSwipeRefreshLayout, api, adapter) {
        };
        adapter.setDeleteSiXinListener(new SiXinAdapter.DeleteSiXinListener() {
            @Override
            public void delete(String id, int position) {
                deleteSiXin(id,position);
            }
        });
        controller.loadFirstPage();
    }

    private void deleteSiXin(String id,final int position){
        YiXiuGeApi api = new YiXiuGeApi("app/del_sixin");
        api.addParams(CommonUtils.uid,api.getUserId(this));
        api.addParams("sid",id);
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()){
                    return;
                }
                adapter.getData().remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyDataSetChanged();
            }
        });
    }
}
