package com.lanmei.yixiu.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xkai on 2018/10/19.
 */

public class SelectQuestionStudentsBean implements Serializable{

    /**
     * id : 48
     * name : A组
     * parent_id : 2
     * parent_name : 2018级
     * student : [{"id":"820517","realname":"b_1341615****"},{"id":"820518","realname":"严同学"},{"id":"820534","realname":"汪睿"},{"id":"820551","realname":"邱欧文"},{"id":"820553","realname":"刘泳君"},{"id":"820563","realname":"苏伟杰"},{"id":"820564","realname":"赵天娇"},{"id":"820569","realname":"李怡"},{"id":"820574","realname":"李瑞曦"},{"id":"820583","realname":"李晶晶"},{"id":"820592","realname":"李婷"},{"id":"820593","realname":"黄志鹏"},{"id":"820616","realname":"好学生"},{"id":"820624","realname":"曾慧岚"},{"id":"820626","realname":"王翰宇"},{"id":"820628","realname":"林俊杰"},{"id":"820629","realname":"段晓莹"},{"id":"820631","realname":"饶冰玉"},{"id":"820632","realname":"于龙"},{"id":"820636","realname":"张国锋"},{"id":"820637","realname":"韩兵"},{"id":"820639","realname":"魏鑫"},{"id":"820645","realname":"徐晶辉"},{"id":"820647","realname":"吴衍章"},{"id":"820661","realname":"任晖"},{"id":"820663","realname":"尹含潇"},{"id":"820665","realname":"王小群"},{"id":"820666","realname":"李华明"},{"id":"820674","realname":"刘轶"},{"id":"820721","realname":"方佳峰"},{"id":"820723","realname":"喻洋"},{"id":"820726","realname":"李子康"},{"id":"820727","realname":"陈金平"},{"id":"820728","realname":"李卓远"},{"id":"820729","realname":"黄庆豪"},{"id":"820730","realname":"姜佳佳"},{"id":"820731","realname":"杨毅兵"},{"id":"820735","realname":"岳媛"},{"id":"820739","realname":"冯景浩"}]
     */

    private String id;
    private String name;
    private String parent_id;
    private String parent_name;
    private boolean isAll;//全选
    private boolean isUnfold;//展开

    public void setUnfold(boolean unfold) {
        isUnfold = unfold;
    }

    public boolean isUnfold() {
        return isUnfold;
    }

    public void setAll(boolean all) {
        isAll = all;
    }

    public boolean isAll() {
        return isAll;
    }

    private List<StudentBean> student;

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

    public List<StudentBean> getStudent() {
        return student;
    }

    public void setStudent(List<StudentBean> student) {
        this.student = student;
    }

    public static class StudentBean implements Serializable{
        /**
         * id : 820517
         * realname : b_1341615****
         */

        private String id;
        private String parent_id;//自己是手动加的
        private String realname;
        private boolean isSelect;

        public String getParent_id() {
            return parent_id;
        }

        public void setParent_id(String parent_id) {
            this.parent_id = parent_id;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        public boolean isSelect() {
            return isSelect;
        }

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
}
