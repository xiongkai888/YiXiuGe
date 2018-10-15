package com.lanmei.yixiu.bean;

/**
 * Created by xkai on 2018/10/15.
 * 教学设备
 */

public class TeachingAttachmentsBean {

    /**
     * id : 1
     * name : 设备1
     * icon : http://gzyxg.oss-cn-shenzhen.aliyuncs.com/820533/1537521012820533.png
     * status : 2
     * addtime : null
     */

    private String id;
    private String name;
    private String icon;
    private String status;
    private String addtime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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
}
