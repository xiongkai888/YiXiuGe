package com.medui.yixiu.bean;

import java.io.Serializable;

/**
 * Created by xkai on 2018/10/25.
 * 学生考试列表
 */

public class ExaminationListBean implements Serializable{

    /**
     * id : 1
     * title : 111
     * type : 2
     * starttime : 1540898100
     * endtime : 1540904400
     * explain : 1111
     * result : null
     * status : 1
     */

    private String id;
    private String title;
    private String type;
    private String starttime;
    private String endtime;
    private String explain;
    private String result;
    private int status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
