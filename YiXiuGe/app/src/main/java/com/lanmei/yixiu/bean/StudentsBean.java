package com.lanmei.yixiu.bean;

import java.io.Serializable;

/**
 * Created by xkai on 2018/9/13.
 */

public class StudentsBean implements Serializable{

    /**
     * id : 820519
     * realname : 大Boss
     */

    private String id;
    private String realname;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }
}
