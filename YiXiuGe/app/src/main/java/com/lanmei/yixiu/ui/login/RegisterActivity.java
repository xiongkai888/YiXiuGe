package com.lanmei.yixiu.ui.login;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.api.YiXiuGeApi;
import com.lanmei.yixiu.event.RegisterEvent;
import com.lanmei.yixiu.utils.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.CodeCountDownTimer;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.DrawClickableEditText;

import org.greenrobot.eventbus.EventBus;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 注册、忘记密码、重设密码
 */
public class RegisterActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar toolbar;
    @InjectView(R.id.phone_et)
    DrawClickableEditText phoneEt;
    @InjectView(R.id.code_et)
    DrawClickableEditText codeEt;
    @InjectView(R.id.obtainCode_bt)
    Button obtainCodeBt;
    @InjectView(R.id.pwd_et)
    DrawClickableEditText pwdEt;
    @InjectView(R.id.showPwd_iv)
    ImageView showPwdIv;
    @InjectView(R.id.pwd_again_et)
    DrawClickableEditText pwdAgainEt;
    @InjectView(R.id.showPwd_again_iv)
    ImageView showPwdAgainIv;
    @InjectView(R.id.register_bt)
    Button button;
//    @InjectView(R.id.agree_protocol_tv)
//    FormatTextView agreeProtocolTv;

    String type;
    boolean isRegister;//是不是注册

    private CodeCountDownTimer mCountDownTimer;//获取验证码倒计时
    private boolean isShowPwd = false;//是否显示密码

    @Override
    public int getContentViewId() {
        return R.layout.activity_register;
    }


    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {

        //初始化倒计时
        mCountDownTimer = new CodeCountDownTimer(this, 10 * 1000, 1000, obtainCodeBt);

        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();

        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        type = getIntent().getStringExtra("value");

        isRegister = StringUtils.isSame(type, CommonUtils.isOne);

        if (isRegister) {//1是注册2是找回密码
            actionbar.setTitle(R.string.register);
        } else {
            actionbar.setTitle("找回密码");
            button.setText(R.string.sure);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_login:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    //注册或找回密码、修改密码
    private void registerOrRetrievePwd(final String phone, String code, final String pwd) {
        String apiUrl;
        if (isRegister){
            apiUrl = "app/registered";//注册
        }else {
            apiUrl = "app/changePassword";//修改密码
        }
        YiXiuGeApi api = new YiXiuGeApi(apiUrl);
        api.addParams("phone", phone);
        api.addParams("password", pwd);
        api.addParams("pcode", code);
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
                if (isRegister) {
                    UIHelper.ToastMessage(RegisterActivity.this, "注册成功");
                } else {
                    UIHelper.ToastMessage(RegisterActivity.this, "修改密码成功");
                }
                EventBus.getDefault().post(new RegisterEvent(phone));
                finish();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        onBackPressed();
        return true;
    }


    @OnClick({R.id.showPwd_iv, R.id.register_bt, R.id.obtainCode_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.showPwd_iv:
                if (!isShowPwd) {//显示密码
                    pwdEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    showPwdIv.setImageResource(R.drawable.pwd_on);
                } else {//隐藏密码
                    pwdEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    showPwdIv.setImageResource(R.drawable.pwd_off);
                }
                isShowPwd = !isShowPwd;
                break;
            case R.id.register_bt://注册
                loadRegister();
                break;
            case R.id.obtainCode_bt://获取验证码
                phone = CommonUtils.getStringByEditText(phoneEt);//电话号码
                if (StringUtils.isEmpty(phone)) {
                    UIHelper.ToastMessage(this, R.string.input_phone_number);
                    return;
                }
                if (!StringUtils.isMobile(phone)) {
                    Toast.makeText(this, R.string.not_mobile_format, Toast.LENGTH_SHORT).show();
                    return;
                }
                loadObtainCode(phone);
                break;
        }
    }


    private String phone;

    //获取验证码
    private void loadObtainCode(String phone) {
        YiXiuGeApi api = new YiXiuGeApi("app/send");
        api.addParams("phone", phone);//send
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
                mCountDownTimer.start();
                UIHelper.ToastMessage(RegisterActivity.this, getString(R.string.send_code_succeed));
            }
        });
    }

    //注册
    private void loadRegister() {
//        phone = CommonUtils.getStringByEditText(phoneEt);//电话号码
        if (StringUtils.isEmpty(phone)) {
//            UIHelper.ToastMessage(this, R.string.input_phone_number);
            UIHelper.ToastMessage(this, "先输入手机号获取验证码");
            return;
        }
        if (!StringUtils.isMobile(phone)) {
            Toast.makeText(this, R.string.not_mobile_format, Toast.LENGTH_SHORT).show();
            return;
        }
        String code = CommonUtils.getStringByEditText(codeEt);//
        if (StringUtils.isEmpty(code)) {
            UIHelper.ToastMessage(this, R.string.input_code);
            return;
        }
        String pwd = CommonUtils.getStringByEditText(pwdEt);//
        if (StringUtils.isEmpty(pwd) || pwd.length() < 6) {
            UIHelper.ToastMessage(this, R.string.input_password_count);
            return;
        }
        String pwdAgain = CommonUtils.getStringByEditText(pwdAgainEt);//
        if (StringUtils.isEmpty(pwdAgain)) {
            UIHelper.ToastMessage(this, R.string.input_pwd_again);
            return;
        }
        if (!StringUtils.isSame(pwd, pwdAgain)) {
            UIHelper.ToastMessage(this, R.string.password_inconformity);
            return;
        }

//        String phone1 = CommonUtils.getStringByEditText(referrerPhoneEt);//推荐人电话号码

//        if (StringUtils.isSame(type, CommonUtils.isOne)) {
//
//            if (StringUtils.isEmpty(phone1)) {
//                UIHelper.ToastMessage(this, "请输入推荐人手机号码");
//                return;
//            }
//            if (!StringUtils.isMobile(phone1)) {
//                UIHelper.ToastMessage(this, "推荐人手机号码格式不正确");
//                return;
//            }
//        }

        registerOrRetrievePwd(phone, code, pwd);
    }

}
