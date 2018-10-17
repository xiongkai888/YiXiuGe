package com.lanmei.yixiu.ui.teacher.activity;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import com.lanmei.yixiu.adapter.TeacherFiltrateAdapter;
import com.lanmei.yixiu.adapter.TeachingAttachmentsAdapter;
import com.lanmei.yixiu.api.YiXiuGeApi;
import com.lanmei.yixiu.bean.TeacherFiltrateBean;
import com.lanmei.yixiu.bean.TeachingAttachmentsBean;
import com.lanmei.yixiu.utils.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.SysUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.DrawClickableEditText;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 教学设备
 */
public class TeachingAttachmentsActivity extends BaseActivity implements TextView.OnEditorActionListener {

    @InjectView(R.id.toolbar_name_tv)
    TextView toolbarNameTv;
    @InjectView(R.id.keywordEditText)
    DrawClickableEditText keywordEditText;//搜索教学设备
    @InjectView(R.id.line_tv)
    TextView lineTv;
    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    private SwipeRefreshController<NoPageListBean<TeachingAttachmentsBean>> controller;
    private YiXiuGeApi api;

    @Override
    public int getContentViewId() {
        return R.layout.activity_teacher;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        toolbarNameTv.setText(R.string.all);
        setCompoundDrawables(R.color.color666, R.drawable.common_filter_arrow_down);
//        keywordEditText.setFocusable(true);
        keywordEditText.setFocusableInTouchMode(true);
        keywordEditText.setOnEditorActionListener(this);
        api = new YiXiuGeApi("app/devicelist");
        api.addParams("status",CommonUtils.isZero);
        TeachingAttachmentsAdapter adapter = new TeachingAttachmentsAdapter(this);
        smartSwipeRefreshLayout.initWithLinearLayout();
        smartSwipeRefreshLayout.setAdapter(adapter);
        controller = new SwipeRefreshController<NoPageListBean<TeachingAttachmentsBean>>(this, smartSwipeRefreshLayout, api, adapter) {
        };
        controller.loadFirstPage();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            String key = CommonUtils.getStringByTextView(v);
            if (StringUtils.isEmpty(key)) {
                UIHelper.ToastMessage(this, R.string.input_keyword);
                return false;
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

    @OnClick({R.id.toolbar_name_tv, R.id.back_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_name_tv:
                popupWindow();
                break;
            case R.id.back_iv:
                finish();
                break;
        }
    }

    PopupWindow window;

    private void popupWindow() {
        setCompoundDrawables(R.color.color1593f0, R.drawable.common_filter_arrow_up);
        if (window != null) {
            window.showAsDropDown(lineTv);
            return;
        }
        RecyclerView view = new RecyclerView(this);
        view.setLayoutManager(new GridLayoutManager(this, 4));
        view.setBackgroundColor(getResources().getColor(R.color.white));

        TeacherFiltrateAdapter teacherFiltrateAdapter = new TeacherFiltrateAdapter(this);
        teacherFiltrateAdapter.setData(getList());
        view.setAdapter(teacherFiltrateAdapter);
        teacherFiltrateAdapter.setTeacherFiltrateListener(new TeacherFiltrateAdapter.TeacherFiltrateListener() {
            @Override
            public void onFiltrate(TeacherFiltrateBean bean) {
                toolbarNameTv.setText(bean.getName());
                window.dismiss();
                api.addParams("status",bean.getId());
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
                setCompoundDrawables(R.color.color666, R.drawable.common_filter_arrow_down);
            }
        });
    }


    private List<TeacherFiltrateBean> getList(){
        List<TeacherFiltrateBean> list = new ArrayList<>();
        TeacherFiltrateBean bean1 = new TeacherFiltrateBean();
        bean1.setName(getString(R.string.all));
        bean1.setId(CommonUtils.isZero);
        TeacherFiltrateBean bean2 = new TeacherFiltrateBean();
        bean2.setName(getString(R.string.unused));
        bean2.setId(CommonUtils.isOne);
        TeacherFiltrateBean bean3 = new TeacherFiltrateBean();
        bean3.setName(getString(R.string.in_use));
        bean3.setId(CommonUtils.isTwo);
        TeacherFiltrateBean bean4 = new TeacherFiltrateBean();
        bean4.setName(getString(R.string.in_maintenance));
        bean4.setId(CommonUtils.isThree);
        list.add(bean1);
        list.add(bean2);
        list.add(bean3);
        list.add(bean4);
        return list;
    }

    /**
     * @param drawableId 项目图片id
     */
    private void setCompoundDrawables(int colorId, int drawableId) {
        Drawable img = getResources().getDrawable(drawableId);
// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
        img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
        toolbarNameTv.setTextColor(getResources().getColor(colorId));
        toolbarNameTv.setCompoundDrawables(null, null, img, null); //设置右图标
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
