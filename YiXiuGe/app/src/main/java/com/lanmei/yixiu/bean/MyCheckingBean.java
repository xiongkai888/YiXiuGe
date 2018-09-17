package com.lanmei.yixiu.bean;

/**
 * Created by xkai on 2018/9/17.
 * 我的考勤（学生）
 */

public class MyCheckingBean {

    /**
     * id : 7
     * uid : 820619
     * cid : 37
     * attend_time : 1537168657
     * addtime : 1537168657
     * explain : null
     * attend_type : 正常
     */

    private String id;
    private String uid;
    private String cid;
    private String attend_time;
    private String addtime;
    private String explain;
    private String attend_type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getAttend_time() {
        return attend_time;
    }

    public void setAttend_time(String attend_time) {
        this.attend_time = attend_time;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public String getAttend_type() {
        return attend_type;
    }

    public void setAttend_type(String attend_type) {
        this.attend_type = attend_type;
    }
}
