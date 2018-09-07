package com.lanmei.yixiu.bean;

/**
 * Created by xkai on 2018/8/17.
 *  教程分类
 */

public class CourseClassifyBean {

    /**
     * id : 491
     * name : 教程2
     * pic : http://gzyxg.img-cn-shenzhen.aliyuncs.com/Uploads/tmp/5b88db556b95e.jpg
     * intro : 3333
     * recommend : 0
     * update_time : 0
     * cid : 19
     */

    private String id;
    private String name;
    private String pic;
    private String intro;
    private String recommend;
    private String update_time;
    private String cid;
    private boolean isChoose;//发布教程的分类列表（需要）

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

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }
}
