package com.lanmei.yixiu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.data.volley.Response;
import com.data.volley.error.VolleyError;
import com.hyphenate.EMClientListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chatuidemo.Constant;
import com.hyphenate.chatuidemo.DemoHelper;
import com.lanmei.yixiu.adapter.MainPagerAdapter;
import com.lanmei.yixiu.api.YiXiuGeApi;
import com.lanmei.yixiu.event.AddCourseEvent;
import com.lanmei.yixiu.event.KaoQinEvent;
import com.lanmei.yixiu.event.LogoutEvent;
import com.lanmei.yixiu.event.UnreadEvent;
import com.lanmei.yixiu.helper.TabHelper;
import com.lanmei.yixiu.update.UpdateAppConfig;
import com.lanmei.yixiu.utils.AKDialog;
import com.lanmei.yixiu.utils.BaiduLocation;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.widget.ProgressHUD;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.InjectView;

public class MainActivity extends BaseActivity{

    @InjectView(R.id.viewPager)
    ViewPager mViewPager;
    @InjectView(R.id.tabLayout)
    TabLayout mTabLayout;
    private ProgressHUD mProgressHUD;
    private BroadcastReceiver broadcastReceiver;
    private LocalBroadcastManager broadcastManager;

    @Override
    public int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {

        EMClient.getInstance().addClientListener(clientListener);
        registerBroadcastReceiver();

        mViewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));
        mViewPager.setOffscreenPageLimit(3);
        mTabLayout.setupWithViewPager(mViewPager);

        new TabHelper(this, mTabLayout);

//        mViewPager.setNoScroll(true);
        EventBus.getDefault().register(this);
//        CommonUtils.loadUserInfo(this, null);
        UpdateAppConfig.requestStoragePermission(this);

        initPermission();//百度定位权限
    }

    private void registerBroadcastReceiver() {
        broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_CONTACT_CHANAGED);
        intentFilter.addAction(Constant.ACTION_GROUP_CHANAGED);
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                updateUnreadLabel();
            }
        };
        broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    private void unregisterBroadcastReceiver(){
        broadcastManager.unregisterReceiver(broadcastReceiver);
    }

    EMClientListener clientListener = new EMClientListener() {
        @Override
        public void onMigrate2x(boolean success) {
            Toast.makeText(getContext(), "onUpgradeFrom 2.x to 3.x " + (success ? "success" : "fail"), Toast.LENGTH_LONG).show();
            if (success) {
                refreshUIWithMessage();
            }
        }
    };


    EMMessageListener messageListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            // notify new message
            for (EMMessage message: messages) {
                DemoHelper.getInstance().getNotifier().vibrateAndPlayTone(message);
            }
            refreshUIWithMessage();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            refreshUIWithMessage();
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
        }

        @Override
        public void onMessageRecalled(List<EMMessage> messages) {
            refreshUIWithMessage();
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {}
    };


    @Override
    protected void onResume() {
        super.onResume();

        updateUnreadLabel();
        // unregister this event listener when this activity enters the
        // background
        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.pushActivity(this);

        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    @Override
    protected void onPause() {
        super.onPause();

        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
        EMClient.getInstance().removeClientListener(clientListener);
        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.popActivity(this);
    }

    private void refreshUIWithMessage() {
        runOnUiThread(new Runnable() {
            public void run() {
                // refresh unread count
                updateUnreadLabel();
            }
        });
    }

    /**
     * update unread message count
     */
    public void updateUnreadLabel() {
        int count = getUnreadMsgCountTotal();
        EventBus.getDefault().post(new UnreadEvent(count));
    }

    /**
     * get unread message count
     *
     * @return
     */
    public int getUnreadMsgCountTotal() {
        return EMClient.getInstance().chatManager().getUnreadMsgsCount();
    }

    private boolean initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                return false;
            }
        }
        return true;
    }

    //点击考勤的时候调用
    @Subscribe
    public void kaoQinEvent(KaoQinEvent event) {
        if (!initPermission()) {
            return;
        }
        mProgressHUD = ProgressHUD.show(this, getString(R.string.in_attendance), true, true, null);
        new BaiduLocation(this, new BaiduLocation.WHbdLocationListener() {
            @Override
            public void bdLocationListener(LocationClient locationClient, BDLocation location) {
                if (isFinishing()) {
                    return;
                }
                if (location != null) {
                    loadLocation(location);
                } else {
                    notice("定位失败，请确认网络和GPS是否开启");
                }
                locationClient.stop();
                locationClient = null;
            }
        });
    }


    private void loadLocation(BDLocation location) {
        YiXiuGeApi api = new YiXiuGeApi("app/sign");
        api.addParams("uid", api.getUserId(this)).addParams("lat", location.getLatitude()).addParams("lon", location.getLongitude());
        HttpClient.newInstance(this).request(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
                notice(response.getMsg());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (isFinishing()) {
                    return;
                }
                notice(error.getMessage());
            }
        });
    }


    private void dismiss() {
        if (mProgressHUD != null) {
            mProgressHUD.dismiss();
            mProgressHUD = null;
        }
    }

    private void notice(String info) {
        dismiss();
        AKDialog.getMessageDialog(this, "", info, R.string.i_know, null).show();
    }

    //退出登录时调用
    @Subscribe
    public void logoutEvent(LogoutEvent event) {
        finish();
    }

    //添加视频教程后调用
    @Subscribe
    public void AddCourseEvent(AddCourseEvent event) {
        mViewPager.setCurrentItem(2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

        unregisterBroadcastReceiver();
    }
}
