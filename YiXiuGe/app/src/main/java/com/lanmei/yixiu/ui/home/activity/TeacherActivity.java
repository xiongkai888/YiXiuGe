package com.lanmei.yixiu.ui.home.activity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.adapter.TeacherAdapter;
import com.lanmei.yixiu.adapter.TeacherFiltrateAdapter;
import com.lanmei.yixiu.api.YiXiuGeApi;
import com.lanmei.yixiu.bean.TeacherBean;
import com.lanmei.yixiu.bean.TeacherFiltrateBean;
import com.lanmei.yixiu.utils.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.SysUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.DrawClickableEditText;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 老师
 */
public class TeacherActivity extends BaseActivity  implements TextView.OnEditorActionListener {

    @InjectView(R.id.toolbar_name_tv)
    TextView toolbarNameTv;
    @InjectView(R.id.keywordEditText)
    DrawClickableEditText keywordEditText;//搜索老师
    @InjectView(R.id.line_tv)
    TextView lineTv;
    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    TeacherAdapter mAdapter;
    private SwipeRefreshController<NoPageListBean<TeacherBean>> controller;
    private List<TeacherFiltrateBean> list;
    private YiXiuGeApi api;

    @Override
    public int getContentViewId() {
        return R.layout.activity_teacher;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        toolbarNameTv.setText(R.string.all);
        CommonUtils.setCompoundDrawables(getContext(),toolbarNameTv,R.drawable.common_filter_arrow_down,R.color.color666,2);
//        keywordEditText.setFocusable(true);
        keywordEditText.setFocusableInTouchMode(true);
        keywordEditText.setOnEditorActionListener(this);
        api = new YiXiuGeApi("app/teacher_list");

        mAdapter = new TeacherAdapter(this);
        smartSwipeRefreshLayout.initWithLinearLayout();
        smartSwipeRefreshLayout.setAdapter(mAdapter);
        controller = new SwipeRefreshController<NoPageListBean<TeacherBean>>(this, smartSwipeRefreshLayout, api, mAdapter) {
        };
        controller.loadFirstPage();
        loadTeacherType();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            String key = CommonUtils.getStringByTextView(v);
            if (StringUtils.isEmpty(key)) {
                UIHelper.ToastMessage(this, R.string.input_keyword);
                return true;
            }
            loadSearch(key);
            return true;
        }
        return false;
    }

    private void loadSearch(String key) {
        api.addParams("keyword",key);
        controller.loadFirstPage();
    }

    //老师类型
    private void loadTeacherType() {
        YiXiuGeApi api = new YiXiuGeApi("app/teacher_type");
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<NoPageListBean<TeacherFiltrateBean>>() {
            @Override
            public void onResponse(NoPageListBean<TeacherFiltrateBean> response) {
                if (isFinishing()) {
                    return;
                }
                list = response.data;
                TeacherFiltrateBean bean = new TeacherFiltrateBean();
                bean.setSelect(true);
                bean.setName(getString(R.string.all));
                if (!StringUtils.isEmpty(list)){
                    list.add(0,bean);
                }
            }
        });
    }

    @OnClick({R.id.toolbar_name_tv, R.id.back_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_name_tv:
                if (StringUtils.isEmpty(list)) {
                    return;
                }
                popupWindow();
                break;
            case R.id.back_iv:
                finish();
                break;
        }
    }

    PopupWindow window;

    private void popupWindow() {
        CommonUtils.setCompoundDrawables(getContext(),toolbarNameTv,R.drawable.common_filter_arrow_up,R.color.color1593f0,2);
        if (window != null) {
            window.showAsDropDown(lineTv);
            return;
        }
        RecyclerView view = new RecyclerView(this);
        view.setLayoutManager(new GridLayoutManager(this, 4));
        view.setBackgroundColor(getResources().getColor(R.color.white));

        TeacherFiltrateAdapter teacherFiltrateAdapter = new TeacherFiltrateAdapter(this);
        teacherFiltrateAdapter.setData(list);
        view.setAdapter(teacherFiltrateAdapter);
        teacherFiltrateAdapter.setTeacherFiltrateListener(new TeacherFiltrateAdapter.TeacherFiltrateListener() {
            @Override
            public void onFiltrate(TeacherFiltrateBean bean) {
                toolbarNameTv.setText(bean.getName());
                window.dismiss();
                api.addParams("type",bean.getId());
                api.addParams("keyword","");
                keywordEditText.setText("");
                controller.loadFirstPage();
            }
        });
//        int width = UIBaseUtils.dp2pxInt(this, 80);
        window = new PopupWindow(view, SysUtils.getScreenWidth(this), ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setContentView(view);
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.setBackgroundDrawable(new BitmapDrawable());
//        int paddingRight = UIBaseUtils.dp2pxInt(this, 0);
//        int xoff = SysUtils.getScreenWidth(this) - width - paddingRight;
        window.showAsDropDown(lineTv);
//        L.d(L.TAG,"width:"+width+",paddingRight:"+paddingRight+",xoff:"+xoff);

        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                CommonUtils.setCompoundDrawables(getContext(),toolbarNameTv,R.drawable.common_filter_arrow_down,R.color.color666,2);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (window != null) {
            if (window.isShowing()) {
                window.dismiss();
            }
            window = null;
        }
    }
}
