package com.medui.yixiu.bean;

/**
 * Created by xkai on 2018/11/1.
 * 我的试卷
 */

public class TestPaperBean {

    /**
     * id : 14
     * title : 111
     * type : 2
     * starttime : 1539084000
     * endtime : 1539176400
     * status : 3
     */

    private String id;
    private String title;
    private String type;
    private String starttime;
    private String endtime;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
