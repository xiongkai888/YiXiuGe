package com.lanmei.yixiu.ui.teacher.activity;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.adapter.TeacherFiltrateAdapter;
import com.lanmei.yixiu.adapter.TeachingAttachmentsAdapter;
import com.lanmei.yixiu.api.YiXiuGeApi;
import com.lanmei.yixiu.bean.CourseClassifyBean;
import com.lanmei.yixiu.bean.TeacherFiltrateBean;
import com.lanmei.yixiu.utils.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.utils.SysUtils;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 教学设备
 */
public class TeachingAttachmentsActivity extends BaseActivity {

    @InjectView(R.id.toolbar_name_tv)
    TextView toolbarNameTv;
    @InjectView(R.id.line_tv)
    TextView lineTv;
    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    TeachingAttachmentsAdapter mAdapter;
    private SwipeRefreshController<NoPageListBean<CourseClassifyBean>> controller;

    @Override
    public int getContentViewId() {
        return R.layout.activity_teacher;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        toolbarNameTv.setText("全部");
        setCompoundDrawables(R.color.color666,R.drawable.common_filter_arrow_down);

        YiXiuGeApi api = new YiXiuGeApi("app/adpic");
        api.addParams("uid", api.getUserId(this));
        api.addParams("row", 20);

        mAdapter = new TeachingAttachmentsAdapter(this);
        smartSwipeRefreshLayout.initWithLinearLayout();
        smartSwipeRefreshLayout.setAdapter(mAdapter);
        controller = new SwipeRefreshController<NoPageListBean<CourseClassifyBean>>(this, smartSwipeRefreshLayout, api, mAdapter) {
        };
//        controller.loadFirstPage();
        mAdapter.notifyDataSetChanged();
    }


    private List<TeacherFiltrateBean> getTeacherFiltrateListBean() {
        List<TeacherFiltrateBean> list = new ArrayList<>();
        TeacherFiltrateBean bean1 = new TeacherFiltrateBean("全部");
        bean1.setSelect(true);
        TeacherFiltrateBean bean2 = new TeacherFiltrateBean("妇产教室");
        TeacherFiltrateBean bean3 = new TeacherFiltrateBean("骨科教室");
        TeacherFiltrateBean bean4 = new TeacherFiltrateBean("牙科教室");
        TeacherFiltrateBean bean5 = new TeacherFiltrateBean("解剖教室");
        TeacherFiltrateBean bean6 = new TeacherFiltrateBean("活动教室");
        list.add(bean1);
        list.add(bean2);
        list.add(bean3);
        list.add(bean4);
        list.add(bean5);
        list.add(bean6);
        return list;
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
        setCompoundDrawables(R.color.color1593f0,R.drawable.common_filter_arrow_up);
        if (window != null) {
            window.showAsDropDown(lineTv);
            return;
        }
        RecyclerView view = new RecyclerView(this);
        view.setLayoutManager(new GridLayoutManager(this, 4));
        view.setBackgroundColor(getResources().getColor(R.color.white));

        TeacherFiltrateAdapter teacherFiltrateAdapter = new TeacherFiltrateAdapter(this);
        teacherFiltrateAdapter.setData(getTeacherFiltrateListBean());
        view.setAdapter(teacherFiltrateAdapter);
        teacherFiltrateAdapter.setTeacherFiltrateListener(new TeacherFiltrateAdapter.TeacherFiltrateListener() {
            @Override
            public void onFiltrate(String name) {
                toolbarNameTv.setText(name);
                window.dismiss();
                CommonUtils.developing(getContext());
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
                setCompoundDrawables(R.color.color666,R.drawable.common_filter_arrow_down);
            }
        });
    }

    /**
     * @param drawableId 项目图片id
     */
    private void setCompoundDrawables(int colorId,int drawableId) {
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
