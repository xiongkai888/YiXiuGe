package com.lanmei.yixiu.ui.teacher.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.bean.StudentsBean;
import com.lanmei.yixiu.event.TestFinishEvent;
import com.lanmei.yixiu.event.TestTimeEvent;
import com.lanmei.yixiu.event.TestUidEvent;
import com.lanmei.yixiu.utils.CommonUtils;
import com.xson.common.utils.L;
import com.xson.common.utils.UIHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by xkai on 2018/11/9.
 */

public class TestService extends Service {


    private int testTime;
    private final long HEART_BEAT_RATE = 1000;//一秒更新位置信息
    private boolean isRunning;
    private StudentsBean bean;//学生信息
    public static String uid;

    @Override
    public void onCreate() {
        super.onCreate();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!isRunning) {
            bean = (StudentsBean) intent.getSerializableExtra("bean");
            uid = bean.getUid();
            testTime = Integer.valueOf(intent.getStringExtra("testTime")) * 60;
            handler.removeCallbacks(heartBeatRunnable);
            handler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);// 发送心跳包
            isRunning = true;
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        L.d(L.TAG, "onDestroy");
        handler.removeCallbacks(heartBeatRunnable);
        handler = null;
        uid = "";
        EventBus.getDefault().unregister(this);
    }

    //提交后停止计时
    @Subscribe
    public void testFinishEvent(TestFinishEvent event) {
        stopSelf();
    }

    //
    @Subscribe
    public void testUidEvent(TestUidEvent event) {
        UIHelper.ToastMessage(this, "考生" + bean.getRealname() + "还未评估完，无法评估其他考生");
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1://
                    testTime = testTime - 1;
                    L.d(L.TAG, testTime + "");
                    if (testTime <= 0) {
                        L.d(L.TAG, "考试已超时");
                        EventBus.getDefault().post(new TestTimeEvent(getString(R.string.overtime),testTime));
                    } else {
                        EventBus.getDefault().post(new TestTimeEvent(CommonUtils.secToTime(testTime),testTime));
                    }
                    break;
            }
        }
    };


    private Runnable heartBeatRunnable = new Runnable() {//心跳包请求位置信息
        @Override
        public void run() {
            handler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);
            Message msg = handler.obtainMessage();
            msg.what = 1;
            handler.sendMessage(msg);
        }
    };


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
