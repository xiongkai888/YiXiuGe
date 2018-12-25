package com.medui.yixiu;

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

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.data.volley.Response;
import com.data.volley.error.VolleyError;
import com.hyphenate.EMGroupChangeListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMucSharedFile;
import com.hyphenate.chatuidemo.Constant;
import com.hyphenate.chatuidemo.DemoHelper;
import com.hyphenate.exceptions.HyphenateException;
import com.medui.yixiu.adapter.MainPagerAdapter;
import com.medui.yixiu.api.YiXiuGeApi;
import com.medui.yixiu.event.AddCourseEvent;
import com.medui.yixiu.event.GroupListEvent;
import com.medui.yixiu.event.InvitationGroupEvent;
import com.medui.yixiu.event.KaoQinEvent;
import com.medui.yixiu.event.LogoutEvent;
import com.medui.yixiu.event.UnreadEvent;
import com.medui.yixiu.helper.TabHelper;
import com.medui.yixiu.update.UpdateAppConfig;
import com.medui.yixiu.utils.AKDialog;
import com.medui.yixiu.utils.BaiduLocation;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.L;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.ProgressHUD;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.InjectView;

public class MainActivity extends BaseActivity {

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

        registerBroadcastReceiver();

        mViewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));
        mViewPager.setOffscreenPageLimit(3);
        mTabLayout.setupWithViewPager(mViewPager);
        new TabHelper(this, mTabLayout);
        EventBus.getDefault().register(this);
        UpdateAppConfig.requestStoragePermission(this);
        initPermission();//百度定位权限
        eMClientListener();
    }


    private void eMClientListener() {
        EMClient.getInstance().groupManager().addGroupChangeListener(new EMGroupChangeListener() {

            @Override
            public void onInvitationReceived(String groupId, String groupName, String inviter, String reason) {
                //接收到群组加入邀请
                L.d("onRequestToJoinReceived", "接收到群组加入邀请 " + Thread.currentThread());
            }

            @Override
            public void onRequestToJoinReceived(String groupId, String groupName, String applyer, String reason) {
                //用户申请加入群
                L.d("onRequestToJoinReceived", "用户申请加入群 " + Thread.currentThread());
            }

            @Override
            public void onRequestToJoinAccepted(String groupId, String groupName, String accepter) {
                //加群申请被同意
                L.d("onRequestToJoinReceived", "加群申请被同意 " + Thread.currentThread());
            }

            @Override
            public void onRequestToJoinDeclined(String groupId, String groupName, String decliner, String reason) {
                //加群申请被拒绝
                L.d("onRequestToJoinReceived", "加群申请被拒绝" + Thread.currentThread());
            }

            @Override
            public void onInvitationAccepted(String groupId, String inviter, String reason) {
                //群组邀请被同意
                L.d("onRequestToJoinReceived", "群组邀请被同意 " + Thread.currentThread());
            }

            @Override
            public void onInvitationDeclined(String groupId, String invitee, String reason) {
                //群组邀请被拒绝
                L.d("onRequestToJoinReceived", "群组邀请被拒绝 " + Thread.currentThread());
            }

            @Override
            public void onUserRemoved(final String groupId,final String groupName) {
                //当前用户被管理员移除出群聊
                L.d("onRequestToJoinReceived", "当前用户被管理员移除出群聊 " + groupId + "," + groupName);
                EventBus.getDefault().post(new GroupListEvent(0));//被踢出某个群后，通知群聊列表刷新
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        UIHelper.ToastMessage(getContext(), "你被 "+groupName+" 管理员踢出了该群");
                    }
                });
            }

            @Override
            public void onGroupDestroyed(final String groupId,final String groupName) {
                //groupName 群解散后调用
                EventBus.getDefault().post(new GroupListEvent(0));//群解散后，通知群聊列表刷新
                L.d("onRequestToJoinReceived", "onGroupDestroyed " +groupId+","+groupId);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        UIHelper.ToastMessage(getContext(), "群主解散了 "+groupName+" 群");
                    }
                });
            }

            @Override
            public void onAutoAcceptInvitationFromGroup(final String groupId, String inviter, String inviteMessage) {
                //接收邀请时自动加入到群组的通知(被邀请进入某个群)
                L.d("onRequestToJoinReceived", "接收邀请时自动加入到群组的通知 groupId:" + groupId+",inviter:"+inviter+",inviteMessage:"+inviteMessage);
                EventBus.getDefault().post(new GroupListEvent(0));//被邀请进入某个群后，通知群聊列表刷新
                try {
                    EMGroup group = EMClient.getInstance().groupManager().getGroupFromServer(groupId);
                    EventBus.getDefault().post(new InvitationGroupEvent(group));
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMuteListAdded(final String groupId, List<String> mutes, long muteExpire) {
                //成员禁言的通知 (只有自己被禁言才调用)
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        EMGroup group = EMClient.getInstance().groupManager().getGroup(groupId);
                        UIHelper.ToastMessage(getContext(), "你被 "+group.getGroupName()+" 管理员禁言了");
                    }
                });
            }

            @Override
            public void onMuteListRemoved(final String groupId, final List<String> mutes) {
                //成员从禁言列表里移除通知 (只有自己被解除禁言才调用)
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        EMGroup group = EMClient.getInstance().groupManager().getGroup(groupId);
                        UIHelper.ToastMessage(getContext(), group.getGroupName() + " 管理员已经解除对你的禁言");
                    }
                });
            }

            @Override
            public void onAdminAdded(final String groupId, final String administrator) {
                L.d("onRequestToJoinReceived", "增加管理员的通知 " + administrator);
                //增加管理员的通知 (只有自己被增加为管理员才调用)
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        EMGroup group = EMClient.getInstance().groupManager().getGroup(groupId);
                        UIHelper.ToastMessage(getContext(), "你被 "+group.getGroupName()+" 群主升级为该群管理员");
                    }
                });
            }

            @Override
            public void onAdminRemoved(final String groupId, String administrator) {
                //管理员移除的通知 (只有自己的管理员被移除才调用)
                L.d("onRequestToJoinReceived", "管理员移除的通知" + Thread.currentThread());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        EMGroup group = EMClient.getInstance().groupManager().getGroup(groupId);
                        UIHelper.ToastMessage(getContext(), "你被 "+group.getGroupName()+" 群主撤销了管理员");
                    }
                });
            }

            @Override
            public void onOwnerChanged(String groupId, String newOwner, String oldOwner) {
                //群所有者变动通知
                L.d("onRequestToJoinReceived", "群所有者变动通知" + Thread.currentThread());
            }

            @Override
            public void onMemberJoined(final String groupId, final String member) {
                //群组加入新成员通知
                L.d("onRequestToJoinReceived", "群组加入新成员通知" + Thread.currentThread());
            }

            @Override
            public void onMemberExited(final String groupId, final String member) {
                //群成员退出通知
                L.d("onRequestToJoinReceived", "群成员退出通知" + Thread.currentThread());
            }

            @Override
            public void onAnnouncementChanged(String groupId, String announcement) {
                //群公告变动通知
                L.d("onRequestToJoinReceived", "群公告变动通知" + Thread.currentThread());
            }

            @Override
            public void onSharedFileAdded(String groupId, EMMucSharedFile sharedFile) {
                //增加共享文件的通知
                L.d("onRequestToJoinReceived", "增加共享文件的通知" + Thread.currentThread());
            }

            @Override
            public void onSharedFileDeleted(String groupId, String fileId) {
                //群共享文件删除通知
                L.d("onRequestToJoinReceived", "群共享文件删除通知" + Thread.currentThread());
            }
        });

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

    private void unregisterBroadcastReceiver() {
        broadcastManager.unregisterReceiver(broadcastReceiver);
    }

    EMMessageListener messageListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            //收到消息
            for (EMMessage message : messages) {
                DemoHelper.getInstance().getNotifier().vibrateAndPlayTone(message);//控制声音和震动
                L.d("onRequestToJoinReceived", "message " + message.getUserName());
            }
            refreshUIWithMessage();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //收到透传消息
            refreshUIWithMessage();
            L.d("onRequestToJoinReceived", "onCmdMessageReceived " + Thread.currentThread());
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
            //收到已读回执
            L.d("onRequestToJoinReceived", "onMessageRead " + Thread.currentThread());
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
            //收到已送达回执
            L.d("onRequestToJoinReceived", "onMessageDelivered " + Thread.currentThread());
        }

        @Override
        public void onMessageRecalled(List<EMMessage> messages) {
            //消息被撤回
            refreshUIWithMessage();
            L.d("onRequestToJoinReceived", "onMessageRecalled " + Thread.currentThread());
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            //消息状态变动
            L.d("onRequestToJoinReceived", "onMessageChanged " + Thread.currentThread());
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        updateUnreadLabel();
        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.pushActivity(this);
        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.popActivity(this);
    }

    private void refreshUIWithMessage() {
        runOnUiThread(new Runnable() {
            public void run() {
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
    //自己被邀请进入某群是调用
    @Subscribe (threadMode = ThreadMode.MAIN)
    public void invitationGroupEvent(InvitationGroupEvent event) {
        EMGroup group = event.getGroup();
        if (group != null){
            UIHelper.ToastMessage(getContext(),"你被 " +group.getGroupName() + " 群管理员邀请进入该群");
        }
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
