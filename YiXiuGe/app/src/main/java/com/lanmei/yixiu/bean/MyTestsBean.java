package com.lanmei.yixiu.bean;

import java.io.Serializable;

/**
 * Created by xkai on 2018/11/12.
 *
 */

public class MyTestsBean implements Serializable{

    /**
     * id : 9
     * title : 线下评估测试（只是测试）
     * starttime : 1541811600
     * endtime : 1542016800
     * intro : 线下评估测试（只是测试）
     * grade : null
     * comment : null
     * result : null
     * status : 2
     */

    private String id;
    private String title;
    private String starttime;
    private String endtime;
    private String intro;
    private String grade;
    private String comment;
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

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
