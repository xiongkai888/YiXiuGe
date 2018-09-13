package com.lanmei.yixiu.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xkai on 2018/9/13.
 * 选择班级
 */

public class ClassSelectBean implements Serializable{

    /**
     * id : 2
     * name : 2018级
     * parent_id : 0
     * xiaji : [{"id":"36","name":"一班","parent_id":"2"}]
     */

    private String id;
    private String name;
    private String parent_id;
    private List<XiajiBean> xiaji;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public List<XiajiBean> getXiaji() {
        return xiaji;
    }

    public void setXiaji(List<XiajiBean> xiaji) {
        this.xiaji = xiaji;
    }

    public static class XiajiBean implements Serializable{
        /**
         * id : 36
         * name : 一班
         * parent_id : 2
         */

        private String id;
        private String name;
        private String parent_id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getParent_id() {
            return parent_id;
        }

        public void setParent_id(String parent_id) {
            this.parent_id = parent_id;
        }
    }
}
