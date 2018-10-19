package com.lanmei.yixiu.bean;

/**
 * Created by xkai on 2018/10/19.
 */

public class QuestionnaireManagementBean {

    public String getUid() {
        return uid;
    }

    public String getTitle() {
        return title;
    }

    public String getStarttime() {
        return starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public String getStatus() {
        return status;
    }

    public String getContent() {
        return content;
    }

    public String getQuest_num() {
        return quest_num;
    }

    public String getState() {
        return state;
    }

    public String getNumber() {
        return number;
    }

    public String getQuest() {
        return quest;
    }

    public String getSubmit_num() {
        return submit_num;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setQuest_num(String quest_num) {
        this.quest_num = quest_num;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setQuest(String quest) {
        this.quest = quest;
    }

    public void setSubmit_num(String submit_num) {
        this.submit_num = submit_num;
    }

    private String uid;//老师id
    private String title;//问卷标题
    private String starttime;//调查开始时间
    private String endtime;//调查结束时间
    private String status;//调查状态  （0正在进行|1已结束|2未开始）
    private String content;//内容
    private String quest_num;//题目数量
    private String state;//审核状态 （0未审核|1已审核）
    private String number;//调查人数
    private String quest;//题目
    private String submit_num;//已参与调查人数
}
