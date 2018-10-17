package com.lanmei.yixiu.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.ProgressHUD;

import java.util.ArrayList;
import java.util.List;

import oss.ManageOssUpload;

/**
 * Created by xkai on 2018/7/20.
 * 没有压缩，单单上传文件
 */

public class UpdateFileTask extends AsyncTask<String, Integer, List<String>> {

    private ProgressHUD mProgressHUD;
    private String type;
    private Context context;
    private UploadingFileCallBack callBack;
    private String uploadingText = "文件上传中...";

    public void setUploadingText(String uploadingText) {
        this.uploadingText = uploadingText;
    }

    public UpdateFileTask(Context context,String type){
        this.context = context;
        this.type = type;
    }

    public void setUploadingFileCallBack(UploadingFileCallBack callBack){
        this.callBack = callBack;
    }

    /**
     * 运行在UI线程中，在调用doInBackground()之前执行
     */
    @Override
    protected void onPreExecute() {
        mProgressHUD = ProgressHUD.show(context, uploadingText, true, false, null);
    }

    /**
     * 后台运行的方法，可以运行非UI线程，可以执行耗时的方法
     */
    @Override
    protected List<String> doInBackground(String... params) {
        List<String> successPath = new ArrayList<>();
        ManageOssUpload manageOssUpload = new ManageOssUpload(context);//图片上传类
        manageOssUpload.setTimeStamp(true);
        for (String picPath:params) {
            String urlPic = manageOssUpload.uploadFile_img(picPath, type);
            if (StringUtils.isEmpty(urlPic)) {
                //写上传失败逻辑
                Message msg = mHandler.obtainMessage();
                msg.what = 1;
                msg.obj = picPath;
                mHandler.sendMessage(msg);
            } else {
                successPath.add(urlPic);
                L.d("CompressPhotoUtils", "上传成功返回的图片地址:" + urlPic);
            }
        }
        return successPath;
    }

    /**
     * 运行在ui线程中，在doInBackground()执行完毕后执行
     */
    @Override
    protected void onPostExecute(List<String> result) {
        //            mProgressDialog.cancel();
        mProgressHUD.cancel();
        mProgressHUD = null;
        if (StringUtils.isEmpty(result)) {
            L.d("CompressPhotoUtils", "isEmpty");
            return;
        }
        if (callBack != null){
            callBack.success(result);
        }
    }

    /**
     * 在publishProgress()被调用以后执行，publishProgress()用于更新进度
     */
    @Override
    protected void onProgressUpdate(Integer... values) {

    }



    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1://
                    UIHelper.ToastMessage(context, "上传文件失败");
                    break;
            }
        }
    };

     public interface UploadingFileCallBack {
        void success(List<String> paths);
    }

}
