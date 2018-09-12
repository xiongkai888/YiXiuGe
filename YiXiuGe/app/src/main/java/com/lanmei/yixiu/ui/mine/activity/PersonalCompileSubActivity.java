package com.lanmei.yixiu.ui.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.utils.AKDialog;
import com.lanmei.yixiu.utils.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;

import butterknife.InjectView;
import butterknife.OnClick;

public class PersonalCompileSubActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;

    @InjectView(R.id.compile_et)
    EditText mCompileEt;

    @InjectView(R.id.ll_input_name)
    LinearLayout mLLinputName;
    @InjectView(R.id.phone_iv)
    ImageView mImageView;//电话图标
    @InjectView(R.id.save_bt)
    Button mButton;//保存或者更换手机号

    @Override
    public int getContentViewId() {
        return R.layout.activity_personal_compile_sub;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("个人资料");
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        initPersonal(actionbar);
    }

    int typeInt = 0;
    String value = "";

    private void initPersonal(ActionBar actionbar) {
        Intent intent = getIntent();
        typeInt = intent.getIntExtra("type", 0);
        value = intent.getStringExtra("value");
        switch (typeInt) {//101:我的昵称  102：我的qq  103:我的邮箱  104：我的电话  105：我的联系地址
            case 100:
                actionbar.setTitle("我的名字");
                mCompileEt.setHint(R.string.input_name);
                mCompileEt.setInputType(InputType.TYPE_CLASS_TEXT);
                mCompileEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
                break;
            case 101:
                actionbar.setTitle("我的电话");
                mLLinputName.setVisibility(View.GONE);
                mImageView.setVisibility(View.VISIBLE);
                mButton.setText("更换手机号");
                break;
            case 102:
                actionbar.setTitle("详细地址");
                mCompileEt.setHint(R.string.input_address);
                mCompileEt.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            case 103:
                actionbar.setTitle("邮箱");
                mCompileEt.setHint("请输入邮箱");
                mCompileEt.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            case 104:
                actionbar.setTitle("微信");
                mCompileEt.setHint("请输入微信号");
                mCompileEt.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            case 105:
                actionbar.setTitle("毕业院校");
                mCompileEt.setHint("请输入毕业院校");
                mCompileEt.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            case 106:
                actionbar.setTitle("单位");
                mCompileEt.setHint("请输入单位名称");
                mCompileEt.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
        }
        mCompileEt.setText(value);
    }

    AlertDialog alertDialog;

    @OnClick(R.id.save_bt)
    public void showSave() {
        String compile = CommonUtils.getStringByEditText(mCompileEt);
        switch (typeInt) {
            case 100://姓名
                if (StringUtils.isEmpty(compile)) {
                    UIHelper.ToastMessage(this, R.string.input_name);
                    return;
                }
                break;
            case 101://电话号码
                if (alertDialog != null && !alertDialog.isShowing()) {
                    alertDialog.show();
                    return;
                }
                if (StringUtils.isEmpty(value)) {
                    UIHelper.ToastMessage(this, "您还没有绑定手机号，请先绑定");
                    return;
                }
                alertDialog = AKDialog.getChangePhoneDialog(this, "验证码将以短息方式发送至您的手机，点击获取验证码按钮后请在60s内输入验证码！", value, "changePhone", new AKDialog.ChangePhoneListener() {
                    @Override
                    public void succeed(String newPhone) {
                        alertDialog.cancel();
                        setCompileIntent(newPhone);
                    }

                    @Override
                    public void unBound() {

                    }
                });
                alertDialog.show();
                break;
            case 102://详细地址
                if (StringUtils.isEmpty(compile)) {
                    UIHelper.ToastMessage(this, R.string.input_address);
                    return;
                }
                break;
            case 103://详细地址
                if (StringUtils.isEmpty(compile)) {
                    UIHelper.ToastMessage(this, "请输入邮箱");
                    return;
                }
                if (!StringUtils.isEmail(compile)) {
                    UIHelper.ToastMessage(this, "邮箱格式不正确");
                    return;
                }
                break;
            case 104://微信
                if (StringUtils.isEmpty(compile)) {
                    UIHelper.ToastMessage(this, "请输入微信号");
                    return;
                }
                break;
            case 105://
                if (StringUtils.isEmpty(compile)) {
                    UIHelper.ToastMessage(this, "请输入毕业院校");
                    return;
                }
                break;
            case 106://
                if (StringUtils.isEmpty(compile)) {
                    UIHelper.ToastMessage(this, "请输入单位名称");
                    return;
                }
                break;

        }
        if (typeInt == 101){
            return;
        }
        setCompileIntent(compile);
    }

    private void setCompileIntent(String compile) {
        Intent intent = new Intent();
        intent.putExtra("compile", compile);
        setResult(RESULT_OK, intent);
        finish();
    }

}
