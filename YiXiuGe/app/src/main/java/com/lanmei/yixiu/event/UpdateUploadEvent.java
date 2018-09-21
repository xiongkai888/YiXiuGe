package com.lanmei.yixiu.event;

import com.lanmei.yixiu.ui.teacher.uploadvideo.UploadVideoBean;

/**
 * Created by xkai on 2018/9/21.
 * 上传视频事件
 */

public class UpdateUploadEvent {

    private int status;//上传的状态
    private UploadVideoBean bean;

    public void setBean(UploadVideoBean bean) {
        this.bean = bean;
    }

    public UploadVideoBean getBean() {
        return bean;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
