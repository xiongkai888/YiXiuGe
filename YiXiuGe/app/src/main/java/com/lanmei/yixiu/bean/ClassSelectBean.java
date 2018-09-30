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
     * xiaji : [{"id":"48","name":"A组","parent_id":"2","parent_name":"2018级"},{"id":"49","name":"B组","parent_id":"2","parent_name":"2018级"}]
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
         * id : 48
         * name : A组
         * parent_id : 2
         * parent_name : 2018级
         */

        private String id;
        private String name;
        private String parent_id;
        private String parent_name;
        private boolean isChoose;

        public void setChoose(boolean choose) {
            isChoose = choose;
        }

        public boolean isChoose() {
            return isChoose;
        }

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

        public String getParent_name() {
            return parent_name;
        }

        public void setParent_name(String parent_name) {
            this.parent_name = parent_name;
        }
    }
}
