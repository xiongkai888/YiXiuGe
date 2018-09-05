package com.lanmei.yixiu;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.hyphenate.chatuidemo.DemoHelper;
import com.squareup.leakcanary.LeakCanary;
import com.xson.common.app.BaseApp;

/**
 * Created by xkai on 2018/4/13.
 */

public class YiXiuApp extends BaseApp {

    public static Context applicationContext;
    private static YiXiuApp instance;

    @Override
    protected void installMonitor() {
        applicationContext = this;
        instance = this;
        LeakCanary.install(this);//LeakCanary内存泄漏监控
        DemoHelper.getInstance().init(this);
    }

    @Override
    public void watch(Object object) {

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    public static YiXiuApp getInstance() {
        return instance;
    }

}
