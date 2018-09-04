package com.lanmei.yixiu.ui.mine.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chatuidemo.DemoHelper;
import com.lanmei.yixiu.R;
import com.lanmei.yixiu.event.LogoutEvent;
import com.lanmei.yixiu.ui.login.LoginActivity;
import com.lanmei.yixiu.utils.AKDialog;
import com.xson.common.app.BaseActivity;
import com.xson.common.helper.DataCleanManager;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.UIHelper;
import com.xson.common.utils.UserHelper;
import com.xson.common.widget.CenterTitleToolbar;

import org.greenrobot.eventbus.EventBus;

import butterknife.InjectView;
import butterknife.OnClick;


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

        try {
            mCleanCacheTv.setText(DataCleanManager.getCacheSize(getCacheDir()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @OnClick({R.id.ll_about_us, R.id.back_login, R.id.ll_info_setting,
            R.id.ll_help_info, R.id.ll_clean_cache, R.id.ll_reset_pwd, R.id.ll_versions})
    public void showSettingInfo(View view) {//
        switch (view.getId()) {
            case R.id.ll_about_us://关于我们
                UIHelper.ToastMessage(this, R.string.developing);
                break;
            case R.id.back_login://退出登录
                AKDialog.getAlertDialog(this, "确认要退出登录？", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logoutHX();
                    }
                });
                break;
            case R.id.ll_info_setting://消息设置
                UIHelper.ToastMessage(this, R.string.developing);
                break;
            case R.id.ll_help_info://帮助信息
                UIHelper.ToastMessage(this, R.string.developing);
                break;
            case R.id.ll_clean_cache://清除缓存
                showClearCache();
                break;
            case R.id.ll_reset_pwd://修改密码
                UIHelper.ToastMessage(this, R.string.developing);
//                RegisterActivity.startActivity(this, RegisterActivity.RESET_PWD_STYLE);
                break;
            case R.id.ll_versions://版本信息
                UIHelper.ToastMessage(this, R.string.developing);
                break;
        }

    }


    void logoutHX() {
        final ProgressDialog pd = new ProgressDialog(this);
        String st = getResources().getString(R.string.Are_logged_out);
        pd.setMessage(st);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        DemoHelper.getInstance().logout(true,new EMCallBack() {

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
        UserHelper.getInstance(getContext()).cleanLogin();//再清空数据
        IntentUtil.startActivity(getContext(), LoginActivity.class);
        EventBus.getDefault().post(new LogoutEvent());//通知MainActivity退出
        finish();
    }

    public void showClearCache() {
        try {
            DataCleanManager.cleanExternalCache(getApplicationContext());
            mCleanCacheTv.setText(DataCleanManager.getCacheSize(getCacheDir()));
            UIHelper.ToastMessage(this, "清理完毕");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
