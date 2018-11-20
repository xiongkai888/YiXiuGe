package com.medui.yixiu.bean;

/**
 * Created by xkai on 2018/8/24.
 * 选择考试学生
 */

public class SelectStudentsBean {

    public String getName() {
        return name;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    private String name;
    private boolean isSelect;

    /**
     * id : 1
     * qid : 1
     * result : 111
     * is_del : null
     * uid : 1
     * submittime : null
     */

    private String id;
    private String qid;
    private String result;
    private String is_del;
    private String uid;
    private String submittime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getIs_del() {
        return is_del;
    }

    public void setIs_del(String is_del) {
        this.is_del = is_del;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSubmittime() {
        return submittime;
    }

    public void setSubmittime(String submittime) {
        this.submittime = submittime;
    }

}
