package com.lanmei.yixiu.bean;

/**
 * Created by xkai on 2018/8/21.
 * 考试答案
 */

public class StudentTestAnswerBean {

    private String type;
    private String answer;
    private boolean isRight;//选择的对错
    private boolean isClick;//是否点击选择

    public void setClick(boolean click) {
        isClick = click;
    }

    public boolean isClick() {
        return isClick;
    }

    public String getType() {
        return type;
    }

    public String getAnswer() {
        return answer;
    }

    public boolean isRight() {
        return isRight;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setRight(boolean right) {
        isRight = right;
    }

}
