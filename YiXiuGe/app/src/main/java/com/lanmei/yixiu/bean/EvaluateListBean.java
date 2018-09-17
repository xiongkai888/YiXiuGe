package com.lanmei.yixiu.bean;

/**
 * Created by xkai on 2018/9/17.
 * 评价列表
 */

public class EvaluateListBean {

    public void setId(String id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getGrade() {
        return grade;
    }

    public String getPic() {
        return pic;
    }

    public String getRealname() {
        return realname;
    }

    private String id;//数据id
    private String content;//评价内容
    private String grade;//星级
    private String pic;//头像
    private String realname;//姓名
}
