package com.medui.yixiu.bean;

import java.util.List;

/**
 * Created by xkai on 2018/9/18.
 */

public class TeacherDetailsBean {

    /**
     * id : 820516
     * pic : http://gzyxg.img-cn-shenzhen.aliyuncs.com/Uploads/tmp/5b88db556b95e.jpg
     * realname : 医修哥
     * age : 20
     * teachingage : 5
     * sex : 1
     * intro : 个人简介
     * learned : 10|11|12|13
     * cityname : 广州
     * videonum : 16
     * video : [{"pic":"http://gzyxg.img-cn-shenzhen.aliyuncs.com/Uploads/imgs/20180911/15366391979554.png","id":"40"},{"pic":"http://gzyxg.img-cn-shenzhen.aliyuncs.com/Uploads/imgs/20180911/1536639098895.png","id":"39"},{"pic":"http://gzyxg.img-cn-shenzhen.aliyuncs.com/Uploads/imgs/20180911/15366389962030.png","id":"38"},{"pic":"http://gzyxg.img-cn-shenzhen.aliyuncs.com/Uploads/imgs/20180911/1536638825965.png","id":"36"}]
     * kec : 普外科|神经外科|胸心外科|泌尿外科
     */

    private String id;
    private String pic;
    private String realname;
    private int age;
    private int teachingage;
    private String sex;
    private String intro;
    private String learned;
    private String cityname;
    private String videonum;
    private String kec;
    private List<VideoBean> video;

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

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getLearned() {
        return learned;
    }

    public void setLearned(String learned) {
        this.learned = learned;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getVideonum() {
        return videonum;
    }

    public void setVideonum(String videonum) {
        this.videonum = videonum;
    }

    public String getKec() {
        return kec;
    }

    public void setKec(String kec) {
        this.kec = kec;
    }

    public List<VideoBean> getVideo() {
        return video;
    }

    public void setVideo(List<VideoBean> video) {
        this.video = video;
    }

    public static class VideoBean {
        /**
         * pic : http://gzyxg.img-cn-shenzhen.aliyuncs.com/Uploads/imgs/20180911/15366391979554.png
         * id : 40
         */

        private String pic;
        private String id;

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
