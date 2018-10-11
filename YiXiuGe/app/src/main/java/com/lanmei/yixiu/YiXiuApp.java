package com.lanmei.yixiu;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.baidu.mapapi.SDKInitializer;
import com.hyphenate.chatuidemo.DemoHelper;
import com.lanmei.yixiu.update.UpdateAppConfig;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.xson.common.app.BaseApp;
import com.yzq.zxinglibrary.common.Constant;

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
//        LeakCanary.install(this);//LeakCanary内存泄漏监控
        DemoHelper.getInstance().init(this);
        UpdateAppConfig.initUpdateApp(applicationContext);//app版本更新
        //友盟初始化设置
        initUM();
    }

    public void initUM() {
//        微信
        PlatformConfig.setWeixin(Constant.WEIXIN_APP_ID,Constant.WEIXIN_APP_SECRET);
//        新浪微博
//        PlatformConfig.setSinaWeibo(Constant.SINA_APP_ID,Constant.SINA_APP_SECRET,Constant.SINA_NOTIFY_URL);
//        qq
//        PlatformConfig.setQQZone(Constant.QQ_APP_ID, Constant.QQ_APP_SECRET);
//        友盟分享
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "");
    }

    @Override
    public void onCreate() {
//        MultiDex.install(this);
        SDKInitializer.initialize(this);//百度地图
        super.onCreate();
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
