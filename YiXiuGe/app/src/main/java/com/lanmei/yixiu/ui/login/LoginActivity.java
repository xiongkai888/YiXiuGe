package com.lanmei.yixiu.ui.login;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.data.volley.Response;
import com.data.volley.error.VolleyError;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chatuidemo.DemoHelper;
import com.hyphenate.chatuidemo.db.DemoDBManager;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.lanmei.yixiu.MainActivity;
import com.lanmei.yixiu.R;
import com.lanmei.yixiu.YiXiuApp;
import com.lanmei.yixiu.api.YiXiuGeApi;
import com.lanmei.yixiu.event.RegisterEvent;
import com.lanmei.yixiu.utils.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.DataBean;
import com.xson.common.bean.UserBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.helper.SharedAccount;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.utils.UserHelper;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.DrawClickableEditText;
import com.xson.common.widget.ProgressHUD;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 登录
 */
public class LoginActivity extends BaseActivity {

    private static final String TAG = "LoginHX";
    @InjectView(R.id.toolbar)
    CenterTitleToolbar toolbar;
    @InjectView(R.id.phone_et)
    DrawClickableEditText phoneEt;
    @InjectView(R.id.pwd_et)
    DrawClickableEditText pwdEt;
    @InjectView(R.id.showPwd_iv)
    ImageView showPwdIv;
    UserBean bean;
    /**
     * 登录等待
     */
    ProgressHUD mProgressHUD;

    private void initProgressDialog() {
        mProgressHUD = ProgressHUD.show(this, "正在登陆...", true, false, null);
        mProgressHUD.cancel();
        mProgressHUD.setCancelable(true);
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_login;
    }


    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initProgressDialog();

        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(R.string.login);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.back);
        String mobile = SharedAccount.getInstance(this).getMobile();
        phoneEt.setText(mobile);
        EventBus.getDefault().register(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_register:
                IntentUtil.startActivity(this, RegisterActivity.class, CommonUtils.isOne);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.showPwd_iv, R.id.forgotPwd_tv, R.id.login_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.showPwd_iv://显示密码
                showPwd();
                break;
            case R.id.forgotPwd_tv://忘记密码
                IntentUtil.startActivity(this, RegisterActivity.class, CommonUtils.isTwo);
                break;
            case R.id.login_bt://登录
                login();
                break;
        }
    }

    String phone;

    private void login() {
        phone = CommonUtils.getStringByEditText(phoneEt);
        if (StringUtils.isEmpty(phone)) {
            Toast.makeText(this, R.string.input_phone_number, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!StringUtils.isMobile(phone)) {
            Toast.makeText(this, R.string.not_mobile_format, Toast.LENGTH_SHORT).show();
            return;
        }
        String pwd = CommonUtils.getStringByEditText(pwdEt);
        if (StringUtils.isEmpty(pwd) || pwd.length() < 6) {
            Toast.makeText(this, R.string.input_password_count, Toast.LENGTH_SHORT).show();
            return;
        }
        mProgressHUD.show();
        YiXiuGeApi api = new YiXiuGeApi("app/login");
        api.addParams("phone", phone);
        api.addParams("password", pwd);
        HttpClient.newInstance(this).request(api, new BeanRequest.SuccessListener<DataBean<UserBean>>() {
            @Override
            public void onResponse(DataBean<UserBean> response) {
                if (isFinishing()) {
                    return;
                }
                bean = response.data;
                loginHX();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (isFinishing()) {
                    return;
                }
                mProgressHUD.cancel();
                UIHelper.ToastMessage(getContext(),error.getMessage());
            }
        });
    }


    public void loginHX() {
        if (!EaseCommonUtils.isNetWorkConnected(this)) {
            Toast.makeText(this, R.string.network_isnot_available, Toast.LENGTH_SHORT).show();
            return;
        }
        String currentUsername = CommonUtils.HX_USER_HEAD + bean.getId();
        String currentPassword = "123456";

        if (TextUtils.isEmpty(currentUsername)) {
            Toast.makeText(this, R.string.User_name_cannot_be_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(currentPassword)) {
            Toast.makeText(this, R.string.Password_cannot_be_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        // After logout，the DemoDB may still be accessed due to async callback, so the DemoDB will be re-opened again.
        // close it before login to make sure DemoDB not overlap
        DemoDBManager.getInstance().closeDB();

        // reset current user name before login
        DemoHelper.getInstance().setCurrentUserName(currentUsername);

        // call login method
        L.d(TAG, "EMClient.getInstance().login:currentUsername = "+currentUsername+",currentPassword = "+currentPassword);
        EMClient.getInstance().login(currentUsername, currentPassword, new EMCallBack() {

            @Override
            public void onSuccess() {
                L.d(TAG, "login: onSuccess");


                // ** manually load all local groups and conversation
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();

                // update current user's display name for APNs
//                boolean updatenick = EMClient.getInstance().pushManager().updatePushNickname(
//                        DemoApplication.currentUserNick.trim());
//                if (!updatenick) {
//                    L.d("LoginActivity", "update current user nick fail");
//                }

                if (!isFinishing() && mProgressHUD.isShowing()) {
                    mProgressHUD.cancel();
                }
                // get user's info (this should be get from App's server or 3rd party service)
                DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();

                saveUser(bean);

                runOnUiThread(new Runnable() {
                    public void run() {
                        UIHelper.ToastMessage(getContext(), "登录成功");
                        YiXiuApp.getInstance().initJiGuang();
                    }
                });
                SharedAccount.getInstance(getContext()).saveMobile(phone);
                finish();
            }

            @Override
            public void onProgress(int progress, String status) {
                L.d(TAG, "login: onProgress");
            }

            @Override
            public void onError(final int code, final String message) {
                L.d(TAG, "login: onError: " + code);
                if (isFinishing()) {
                    return;
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (!isFinishing() && mProgressHUD.isShowing()) {
                            mProgressHUD.cancel();
                        }
                        Toast.makeText(getApplicationContext(), getString(R.string.Login_failed),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void saveUser(UserBean bean) {
        if (bean != null) {
            UserHelper.getInstance(this).saveBean(bean);
            IntentUtil.startActivity(this, MainActivity.class);
        }
    }

    private boolean isShowPwd = false;//是否显示密码

    private void showPwd() {
        if (!isShowPwd) {//显示密码
            pwdEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            showPwdIv.setImageResource(R.drawable.pwd_on);
        } else {//隐藏密码
            pwdEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            showPwdIv.setImageResource(R.drawable.pwd_off);
        }
        isShowPwd = !isShowPwd;
    }

    //注册后调用
    @Subscribe
    public void respondRegisterEvent(RegisterEvent event) {
        phoneEt.setText(event.getPhone());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}