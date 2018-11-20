package com.medui.yixiu.bean;

/**
 * Created by xkai on 2018/9/13.
 * 考勤管理
 */

public class CheckingInManageBean {

    /**
     * uid : 820517
     * realname : b_1341615****
     * attend_time : 0
     * explain :
     * attend_type : 旷课
     */

    private String uid;
    private String realname;
    private String attend_time;
    private String explain;
    private String attend_type;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getAttend_time() {
        return attend_time;
    }

    public void setAttend_time(String attend_time) {
        this.attend_time = attend_time;
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
