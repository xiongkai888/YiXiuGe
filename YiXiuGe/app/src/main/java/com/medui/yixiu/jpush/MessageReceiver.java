package com.medui.yixiu.jpush;

import android.content.Context;

import com.xson.common.utils.L;

import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.service.JPushMessageReceiver;

/**
 * Created by xkai on 2018/10/12.
 */

public class MessageReceiver extends JPushMessageReceiver {

    @Override
    public void onTagOperatorResult(Context var1, JPushMessage var2) {
        L.d(JiGuangReceiver.TAG, "MessageReceiver.onTagOperatorResult : " + var2.toString());
    }

    @Override
    public void onCheckTagOperatorResult(Context var1, JPushMessage var2) {
        L.d(JiGuangReceiver.TAG, "MessageReceiver.onCheckTagOperatorResult : " + var2.toString());
    }

    @Override
    public void onAliasOperatorResult(Context var1, JPushMessage var2) {
        L.d(JiGuangReceiver.TAG, "MessageReceiver.onAliasOperatorResult : " + var2.toString());
    }

    @Override
    public void onMobileNumberOperatorResult(Context var1, JPushMessage var2) {
        L.d(JiGuangReceiver.TAG, "MessageReceiver.onMobileNumberOperatorResult : " + var2.toString());
    }
}
