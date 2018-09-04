package com.lanmei.yixiu.ui.teacher.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.helper.AddQuestionnaireOptionHelper;
import com.lanmei.yixiu.utils.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;

import butterknife.InjectView;

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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_submit:
                if (StringUtils.isEmpty(CommonUtils.getStringByEditText(questionnaireTitleTv)) ){
                    UIHelper.ToastMessage(this, R.string.input_questionnaire_option);
                    break;
                }
                if (!helper.isComplete()) {
                    UIHelper.ToastMessage(this, "请完善问卷选项");
                    break;
                }
                CommonUtils.developing(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
