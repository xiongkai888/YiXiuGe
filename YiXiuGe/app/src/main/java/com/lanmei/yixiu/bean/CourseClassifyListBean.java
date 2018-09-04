package com.lanmei.yixiu.bean;

import java.io.Serializable;

/**
 * Created by xkai on 2018/8/17.
 * 教程分类列表
 */

public class CourseClassifyListBean implements Serializable{

    /**
     * id : 17
     * title : 视频4
     * uid : 820516
     * addtime : 1535698749
     * pic : null
     * video : http://zsqz.oss-cn-shenzhen.aliyuncs.com/lanmei/screen/imgvideo/1532075975706.mp4
     * view : 0
     * favour : 0
     * reviews : 0
     * like : 0
     * recommend : 0
     * cid : 495
     * cname : 教程3
     * memberpic : http://gzyxg.oss-cn-shenzhen.aliyuncs.com/lanmei/yixiuge/img1/head-1807198884.jpg.tmp
     * nickname : b_1889860****
     * subscribe : 10
     */

    private String id;
    private String title;
    private String uid;
    private String addtime;
    private String pic;
    private String video;
    private String view;
    private String favour;
    private String reviews;
    private String like;
    private String recommend;
    private String cid;
    private String cname;
    private String memberpic;
    private String nickname;
    private int subscribe;

    /**
     * uptime : 1535957641
     * liked : 0
     */

    private String uptime;
    private int liked;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getFavour() {
        return favour;
    }

    public void setFavour(String favour) {
        this.favour = favour;
    }

    public String getReviews() {
        return reviews;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getMemberpic() {
        return memberpic;
    }

    public void setMemberpic(String memberpic) {
        this.memberpic = memberpic;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(int subscribe) {
        this.subscribe = subscribe;
    }

    public String getUptime() {
        return uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    public int getLiked() {
        return liked;
    }

    public void setLiked(int liked) {
        this.liked = liked;
    }
}
