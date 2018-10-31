package com.lanmei.yixiu.event;

/**
 * Created by xkai on 2018/10/30.
 * 环信请求获取用户信息事件
 */

public class UserBeanEvent {

    private String userName;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public UserBeanEvent(){

    }
    public UserBeanEvent(String userName){
        this.userName = userName;
    }
}
