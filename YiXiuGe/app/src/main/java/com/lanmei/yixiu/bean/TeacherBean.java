package com.lanmei.yixiu.bean;

/**
 * Created by xkai on 2018/9/18.
 * 老师列表
 */

public class TeacherBean {

    /**
     * id : 820515
     * pic : http://gzyxg.oss-cn-shenzhen.aliyuncs.com/lanmei/yixiuge/img1/head4219045509521850070.jpg
     * realname : 小四轮
     * age : 20
     * teachingage : 5
     * sex : 1
     * cityname : 广州
     * grade : 5
     */

    private String id;
    private String pic;
    private String realname;
    private int age;
    private int teachingage;
    private String sex;
    private String cityname;
    private String grade;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getTeachingage() {
        return teachingage;
    }

    public void setTeachingage(int teachingage) {
        this.teachingage = teachingage;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
