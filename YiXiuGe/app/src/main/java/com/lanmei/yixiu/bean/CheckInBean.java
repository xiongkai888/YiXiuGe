package com.lanmei.yixiu.bean;

/**
 * Created by xkai on 2018/8/20.
 * 考勤
 *
 */

public class CheckInBean {

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getRemark() {
        return remark;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    private String date;
    private String time;
    private String remark;
}
