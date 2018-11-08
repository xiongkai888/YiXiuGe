package com.lanmei.yixiu.bean;

import java.io.Serializable;

/**
 * Created by xkai on 2018/11/8.
 * 我的评估
 */

public class TestListBean implements Serializable{


    /**
     * id : 6
     * title : 131131
     * starttime : 1541646060
     * endtime : 1541732460
     * state : 1
     * e_time : 212
     * status : 1
     * quest_num : 7
     * submit_num : 0
     * number : 3
     */

    private String id;
    private String title;
    private String starttime;
    private String endtime;
    private String state;
    private String e_time;
    private int status;
    private String quest_num;
    private int submit_num;
    private int number;

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getE_time() {
        return e_time;
    }

    public void setE_time(String e_time) {
        this.e_time = e_time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getQuest_num() {
        return quest_num;
    }

    public void setQuest_num(String quest_num) {
        this.quest_num = quest_num;
    }

    public int getSubmit_num() {
        return submit_num;
    }

    public void setSubmit_num(int submit_num) {
        this.submit_num = submit_num;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
