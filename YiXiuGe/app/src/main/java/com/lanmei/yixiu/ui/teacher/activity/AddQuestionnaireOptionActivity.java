package com.lanmei.yixiu.ui.teacher.activity;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.adapter.TeacherFiltrateAdapter;
import com.lanmei.yixiu.bean.QuestionnaireSubjectBean;
import com.lanmei.yixiu.bean.TeacherFiltrateBean;
import com.lanmei.yixiu.event.QuestionnaireSubjectEvent;
import com.lanmei.yixiu.helper.AddQuestionnaireOptionHelper;
import com.lanmei.yixiu.utils.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.SysUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 添加问卷选项
 */
public class AddQuestionnaireOptionActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.ll_root)
    LinearLayout llRoot;
    @InjectView(R.id.questionnaire_title_tv)
    EditText questionnaireTitleTv;
    AddQuestionnaireOptionHelper helper;
    @InjectView(R.id.line_tv)
    TextView lineTv;
    @InjectView(R.id.toolbar_name_tv)
    TextView toolbarNameTv;
    private String type = "1";

    @Override
    public int getContentViewId() {
        return R.layout.activity_add_questionnaire_option;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.add_questionnaire_option);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        helper = new AddQuestionnaireOptionHelper(this, llRoot);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_submit, menu);
        return true;
    }


    @OnClick({R.id.toolbar_name_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_name_tv:
                popupWindow();
                break;
        }
    }

    private List<TeacherFiltrateBean> getList(){
        List<TeacherFiltrateBean> list = new ArrayList<>();
        TeacherFiltrateBean bean1 = new TeacherFiltrateBean();
        bean1.setId("1");
        bean1.setName("选择题");
        TeacherFiltrateBean bean2 = new TeacherFiltrateBean();
        bean2.setId("2");
        bean2.setName("主观题");
        list.add(bean1);
        list.add(bean2);
        return list;
    }

    PopupWindow window;

    private void popupWindow() {
        setCompoundDrawables(R.color.color1593f0, R.drawable.common_filter_arrow_up);
        if (window != null) {
            window.showAsDropDown(lineTv);
            return;
        }
        RecyclerView view = new RecyclerView(this);
        view.setLayoutManager(new GridLayoutManager(this, 2));
        view.setBackgroundColor(getResources().getColor(R.color.white));

        TeacherFiltrateAdapter teacherFiltrateAdapter = new TeacherFiltrateAdapter(this);
        teacherFiltrateAdapter.setData(getList());
        view.setAdapter(teacherFiltrateAdapter);
        teacherFiltrateAdapter.setTeacherFiltrateListener(new TeacherFiltrateAdapter.TeacherFiltrateListener() {
            @Override
            public void onFiltrate(TeacherFiltrateBean bean) {
                toolbarNameTv.setText(bean.getName());
                window.dismiss();
                if (StringUtils.isSame(bean.getId(),CommonUtils.isOne)){
                    llRoot.setVisibility(View.VISIBLE);
                }else {
                    llRoot.setVisibility(View.GONE);
                }
                type = bean.getId();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_submit:
                if (StringUtils.isEmpty(CommonUtils.getStringByEditText(questionnaireTitleTv)) ){
                    UIHelper.ToastMessage(this, R.string.input_questionnaire_option);
                    break;
                }
                if (StringUtils.isSame(type,CommonUtils.isOne)){//
                    if (!helper.isComplete()) {
                        UIHelper.ToastMessage(this, "请完善问卷选项");
                        break;
                    }
                }
                QuestionnaireSubjectBean bean = new QuestionnaireSubjectBean();
                bean.setTitle(CommonUtils.getStringByEditText(questionnaireTitleTv));
                bean.setType(type);
                bean.setList(helper.getList());
                EventBus.getDefault().post(new QuestionnaireSubjectEvent(bean));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
