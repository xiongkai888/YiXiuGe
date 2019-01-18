package com.medui.yixiu.ui.mine.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chatuidemo.DemoHelper;
import com.medui.yixiu.R;
import com.medui.yixiu.YiXiuApp;
import com.medui.yixiu.event.LogoutEvent;
import com.medui.yixiu.ui.login.LoginActivity;
import com.medui.yixiu.ui.login.ProtocolActivity;
import com.medui.yixiu.ui.login.RegisterActivity;
import com.medui.yixiu.update.UpdateAppConfig;
import com.medui.yixiu.update.UpdateEvent;
import com.medui.yixiu.utils.AKDialog;
import com.medui.yixiu.utils.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.helper.DataCleanManager;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.UIHelper;
import com.xson.common.utils.UserHelper;
import com.xson.common.widget.CenterTitleToolbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;


/**
 * 设置
 */

public class SettingActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.cache_count)
    TextView mCleanCacheTv;


    @Override
    public int getContentViewId() {
        return R.layout.activity_setting;
    }


    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.setting);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        EventBus.getDefault().register(this);

        try {
            mCleanCacheTv.setText(DataCleanManager.getCacheSize(getCacheDir()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void logoutHX() {
        final ProgressDialog pd = new ProgressDialog(this);
        String st = getResources().getString(R.string.Are_logged_out);
        pd.setMessage(st);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        DemoHelper.getInstance().logout(true, new EMCallBack() {

            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        pd.dismiss();
                        logout();
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        pd.dismiss();
                        Toast.makeText(getContext(), "退出登录失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void logout() {
        JPushInterface.stopPush(YiXiuApp.applicationContext);//停止接收极光的推送
        UserHelper.getInstance(getContext()).cleanLogin();//再清空数据
        IntentUtil.startActivity(getContext(), LoginActivity.class);
        EventBus.getDefault().post(new LogoutEvent());//通知MainActivity退出
        finish();
    }

    public void showClearCache() {
        try {
            DataCleanManager.cleanInternalCache(getApplicationContext());
            DataCleanManager.cleanExternalCache(getApplicationContext());
            mCleanCacheTv.setText(DataCleanManager.getCacheSize(getCacheDir()));
            UIHelper.ToastMessage(this, "清理完毕");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Subscribe
    public void updateEvent(UpdateEvent event) {
        UIHelper.ToastMessage(this, event.getContent());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.user_protocol_tv, R.id.ll_clean_cache, R.id.version_information_tv, R.id.change_password_tv, R.id.back_login,R.id.about_us_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.user_protocol_tv://
                IntentUtil.startActivity(this,ProtocolActivity.class,getString(R.string.privacy_policy));
                break;
            case R.id.ll_clean_cache://清除缓存
                showClearCache();
                break;
            case R.id.version_information_tv://版本信息
                UpdateAppConfig.requestStoragePermission(this);
                break;
            case R.id.change_password_tv://修改密码
                IntentUtil.startActivity(this, RegisterActivity.class, CommonUtils.isThree);
                break;
            case R.id.back_login://退出登录
                AKDialog.getAlertDialog(this, "确认要退出登录？", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logoutHX();
                    }
                });
                break;
            case R.id.about_us_tv://关于我们
                IntentUtil.startActivity(this, AboutUsActivity.class);
                break;
        }
    }
}
