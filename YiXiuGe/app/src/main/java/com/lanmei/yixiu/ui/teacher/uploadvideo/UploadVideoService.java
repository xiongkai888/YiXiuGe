package com.lanmei.yixiu.ui.teacher.uploadvideo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.model.CompleteMultipartUploadResult;
import com.alibaba.sdk.android.oss.model.ResumableUploadRequest;
import com.data.volley.Response;
import com.data.volley.error.VolleyError;
import com.lanmei.yixiu.R;
import com.lanmei.yixiu.YiXiuApp;
import com.lanmei.yixiu.api.YiXiuGeApi;
import com.lanmei.yixiu.event.AddCourseEvent;
import com.lanmei.yixiu.event.UpdateUploadEvent;
import com.xson.common.bean.BaseBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import oss.ResuambleUploadSamples;
import oss.SimUploadCallback;

/**
 * Created by xkai on 2018/8/3.
 */

public class UploadVideoService extends Service {

    private ResuambleUploadSamples resuambleUploadSamples;
    private DBUploadViewHelper dbUploadViewHelper;
    private List<UploadVideoBean> videoBeanList;
    private UpdateUploadEvent event = new UpdateUploadEvent();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        resuambleUploadSamples = new ResuambleUploadSamples(YiXiuApp.applicationContext);
        dbUploadViewHelper = new DBUploadViewHelper(this);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (dbUploadViewHelper == null) {
            dbUploadViewHelper = new DBUploadViewHelper(this);
        }
        videoBeanList = dbUploadViewHelper.getUploadVideoList();
        startNext();
        return START_STICKY;
    }


    public void startNext() {
        List<UploadVideoBean> listData = dbUploadViewHelper.getUploadVideoList();
        if (!StringUtils.isEmpty(listData)) {
            UploadVideoBean bean = listData.get(0);
            if (!StringUtils.isSame(getString(R.string.uploading), bean.getStatus())) {
                addUploadFile(bean);
                L.d("AyncListObjects", "UploadVideoService：正在上传");
            }
        } else {
            stopSelf();
            L.d("AyncListObjects", "UploadVideoService：停止服务");
        }
    }

    public void addUploadFile(UploadVideoBean videoUpload) {

        SimUploadCallback simUploadCallback = new SimUploadCallback(videoUpload) {
            @Override
            public void onPerpare(String name, String uploadId) {
//                boolean isCanceled = resuambleUploadSamples.getResumableTask().isCanceled();
//                if (isCanceled) {
//                    return;
//                }
                L.d("AyncListObjects", "UploadVideoService：name：" + name);
                videoUpload.setStatus(getString(R.string.waiting));
//                dbUploadViewHelper.updateUploadVideoBean(videoUpload.getPath(), videoUpload.getProgress(), videoUpload.getStatus());

                event.setStatus(0);
                event.setBean(videoUpload);
                EventBus.getDefault().post(event);
            }

            @Override
            public void onProgress(ResumableUploadRequest resumableUploadRequest, long currentSize, long totalSize) {
//                boolean isCanceled = resuambleUploadSamples.getResumableTask().isCanceled();
//                if (isCanceled) {
//                    return;
//                }
                L.d("AyncListObjects", "UploadVideoService：currentSize：" + currentSize + ",totalSize:" + totalSize+", progress:"+(int) ((currentSize*100 / totalSize)));
                videoUpload.setStatus(getString(R.string.uploading));
                videoUpload.setProgress((int) ((currentSize*100 / totalSize)));

                event.setStatus(1);
                event.setBean(videoUpload);
                EventBus.getDefault().post(event);

            }

            @Override
            public void onSuccess(ResumableUploadRequest resumableUploadRequest, CompleteMultipartUploadResult completeMultipartUploadResult) {
                boolean isCanceled = resuambleUploadSamples.getResumableTask().isCanceled();
                if (isCanceled) {
                    return;
                }
                L.d("AyncListObjects", "UploadVideoService：completeMultipartUploadResult.getLocation()：" + completeMultipartUploadResult.getLocation());
                videoUpload.setStatus(getString(R.string.uploading_succeed));
                videoUpload.setProgress(100);
                dbUploadViewHelper.updateUploadVideoBean(videoUpload.getPath(), videoUpload.getProgress(), videoUpload.getStatus());

                event.setStatus(2);
                event.setBean(videoUpload);
                EventBus.getDefault().post(event);

                loadUploadVideo(videoUpload, completeMultipartUploadResult.getLocation());

            }

            @Override
            public void onFailure(ResumableUploadRequest resumableUploadRequest, ClientException clientException, ServiceException serviceException) {
                boolean isCanceled = resuambleUploadSamples.getResumableTask().isCanceled();
                if (isCanceled) {
                    return;
                }
                L.d("AyncListObjects", "UploadVideoService：onFailure：" + getString(R.string.uploading_failure));
                videoUpload.setStatus(getString(R.string.uploading_failure));
                dbUploadViewHelper.updateUploadVideoBean(videoUpload.getPath(), videoUpload.getProgress(), videoUpload.getStatus());

                event.setStatus(3);
                event.setBean(videoUpload);
                EventBus.getDefault().post(event);

//                startNext();
            }
        };
        resuambleUploadSamples.resumableUploadWithRecordPathSetting(videoUpload.getPath(), simUploadCallback);

    }

    private void loadUploadVideo(final UploadVideoBean videoUpload, String location) {

        L.d("AyncListObjects", "上传视频到阿里云返回的地址location：" + location);

        if (StringUtils.isEmpty(location)) {
            return;
        }

        YiXiuGeApi api = new YiXiuGeApi("app/video_add");
        api.addParams("title", videoUpload.getTitle());
        api.addParams("pic", videoUpload.getPic());
        api.addParams("video", location);
        api.addParams("cid", videoUpload.getCid());
        api.addParams("uid", api.getUserId(this));
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (event == null) {
                    return;
                }

                dbUploadViewHelper.deleteUploadVideoBean(videoUpload);
                EventBus.getDefault().post(new AddCourseEvent(videoUpload.getCid()));//刷新cid == classifyBean.getCid()的教程列表

                event.setStatus(4);
                videoUpload.setProgress(100);
                videoUpload.setStatus(getString(R.string.uploading_succeed));
                EventBus.getDefault().post(event);

                startNext();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (dbUploadViewHelper == null) {
                    return;
                }
                event.setStatus(5);
                videoUpload.setProgress(100);
                videoUpload.setStatus("提交上传视频信息失败...");
                event.setBean(videoUpload);
                EventBus.getDefault().post(event);
            }
        });
    }


}
