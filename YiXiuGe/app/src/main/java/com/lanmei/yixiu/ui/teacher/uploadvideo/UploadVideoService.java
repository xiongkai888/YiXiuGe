package com.lanmei.yixiu.ui.teacher.uploadvideo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.lanmei.yixiu.YiXiuApp;

import oss.ResuambleUploadSamples;

/**
 * Created by xkai on 2018/8/3.
 */

public class UploadVideoService extends Service {

    private ResuambleUploadSamples resuambleUploadSamples;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        resuambleUploadSamples = new ResuambleUploadSamples(YiXiuApp.applicationContext);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    public void startNext(){
//        List<UploadVideoBean> listData = getUploadVideos();
//        if (getUploadItemCallback()!=null) {
//            getUploadItemCallback().onListDataRefresh(listData);
//
//        }
//        if (listData.size()>0)
//            addUploadFile(0,listData.get(0));
    }

}
