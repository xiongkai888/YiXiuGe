package com.medui.yixiu.bean;

/**
 * Created by xkai on 2018/8/21.
 * 考试答案
 */

public class StudentTestAnswerBean {

    /**
     * score : 0
     * text : 2
     * type : 1
     * id : 1
     */

    private String score;//分数 (type == 1时：score=0是对，1是错)(type != 1时，表示分数)
    private String text;//备注
    private String type;//类型（1|2=>判断题|打分题）
    private String id;//

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
