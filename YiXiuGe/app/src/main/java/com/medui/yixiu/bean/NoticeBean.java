package com.medui.yixiu.bean;

import java.io.Serializable;

/**
 * Created by xkai on 2018/9/4.
 * 通知公告
 */

public class NoticeBean implements Serializable{

    /**
     * id : 75
     * identify : 7
     * mode : 1
     * tid :
     * title : 不知道为什么  就是想发条公告
     * intro :
     * content : <span>不知道为什么&nbsp; 就是想发条公告</span>
     * pic : null
     * status : 1
     * addtime : 1524793118
     * label :
     * recommend : 0
     */

    private String id;
    private String identify;
    private String mode;
    private String tid;
    private String title;
    private String intro;
    private String content;
    private String pic;
    private String status;
    private String addtime;
    private String label;
    private String recommend;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }
}
