package com.medui.yixiu;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.alibaba.sdk.android.oss.common.OSSLog;
import com.baidu.mapapi.SDKInitializer;
import com.hyphenate.chatuidemo.DemoHelper;
import com.medui.yixiu.bean.ExaminationBean;
import com.medui.yixiu.bean.StudentTestAnswerBean;
import com.medui.yixiu.jpush.JiGuangReceiver;
import com.medui.yixiu.update.UpdateAppConfig;
import com.medui.yixiu.utils.CommonUtils;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.xson.common.app.BaseApp;
import com.xson.common.utils.L;
import com.xson.common.utils.UserHelper;
import com.yzq.zxinglibrary.common.Constant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by xkai on 2018/4/13.
 */

public class YiXiuApp extends BaseApp {

    public static Context applicationContext;
    private static YiXiuApp instance;
    private Map<String, ExaminationBean> map;
    private Map<String, List<StudentTestAnswerBean>> answerBeanMap;

    @Override
    protected void installMonitor() {
        applicationContext = this;
        instance = this;
        L.debug = OSSLog.enableLog = false;//true,false
        if (L.debug) {
//            LeakCanary.install(this);//LeakCanary内存泄漏监控
        }
        //友盟初始化设置
        initUM();
        initJiGuang();
        DemoHelper.getInstance().init(this);
    }

    //存评估题目(暂时这样,可以考虑保存在TestService中)
    public void saveTestAnswerBean(String id, List<StudentTestAnswerBean> bean) {
        if (answerBeanMap == null) {
            answerBeanMap = new HashMap<>();
        }
        answerBeanMap.put(id, bean);
    }

    public void removeTestAnswerBean(String id) {
        if (map == null) {
            map = new HashMap<>();
        }
        answerBeanMap.remove(id);
    }

    public List<StudentTestAnswerBean> getTestAnswerBean(String id) {
        if (answerBeanMap == null) {
            answerBeanMap = new HashMap<>();
        }
        return answerBeanMap.get(id);
    }



    //存考试题目(暂时这样)
    public void saveExamination(String id, ExaminationBean bean) {
        if (map == null) {
            map = new HashMap<>();
        }
        map.put(id, bean);
    }

    public void removeExamination(String id) {
        if (map == null) {
            map = new HashMap<>();
        }
        map.remove(id);
    }

    public ExaminationBean getExaminationBean(String id) {
        if (map == null) {
            map = new HashMap<>();
        }
        return map.get(id);
    }

    public void initJiGuang() {
        if (!UserHelper.getInstance(this).hasLogin()) {
            JPushInterface.stopPush(this);//停止接收极光的推送
            return;
        }
        JPushInterface.setDebugMode(L.debug);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);    // 初始化 JPush
        if (JPushInterface.isPushStopped(this)) {
            String uid = CommonUtils.getUserId(this);
            JPushInterface.resumePush(this);
            JPushInterface.setAlias(this, Integer.valueOf(uid), uid);
            L.d(JiGuangReceiver.TAG, "极光推送设置别名:" + uid);
        }
    }

    public void initUM() {

//        微信
        PlatformConfig.setWeixin(Constant.WEIXIN_APP_ID, Constant.WEIXIN_APP_SECRET);
//        新浪微博
//        PlatformConfig.setSinaWeibo(Constant.SINA_APP_ID,Constant.SINA_APP_SECRET,Constant.SINA_NOTIFY_URL);
//        qq
//        PlatformConfig.setQQZone(Constant.QQ_APP_ID, Constant.QQ_APP_SECRET);
        UMConfigure.setLogEnabled(L.debug);//如果查看初始化过程中的LOG，一定要在调用初始化方法前将LOG开关打开。
//        友盟分享
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "");
    }

    @Override
    public void onCreate() {
//        MultiDex.install(this);
        SDKInitializer.initialize(this);//百度地图
        super.onCreate();
        UpdateAppConfig.initUpdateApp(this);//app版本更新
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
