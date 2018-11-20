package com.medui.yixiu.bean;

import java.io.Serializable;

/**
 * Created by xkai on 2018/8/17.
 * 教程分类列表（教程收藏列表）
 */

public class CourseClassifyListBean implements Serializable{

    /**
     * id : 22
     * title : 中山一院基础外科学院成立宣传视频
     * uid : 820516
     * addtime : 1535962721
     * pic : http://gzyxg.img-cn-shenzhen.aliyuncs.com/Uploads/imgs/20180903/15359626922492.jpg
     * video : http://gzyxg.oss-cn-shenzhen.aliyuncs.com/video/dsyszsyp.mp4
     * view : 28
     * favour : 10
     * reviews : 10
     * like : 10
     * recommend : 1
     * cid : 22
     * uptime : 1536060616
     * cname : null
     * liked : 0
     * favoured : 0
     * memberpic : http://gzyxg.img-cn-shenzhen.aliyuncs.com/Uploads/tmp/5b88db556b95e.jpg
     * nickname : 医修哥
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
    private String uptime;
    private String cname;
    private String liked;
    private String favoured;
    private String memberpic;
    private String nickname;
    private int subscribe;

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

    public String getUptime() {
        return uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getLiked() {
        return liked;
    }

    public void setLiked(String liked) {
        this.liked = liked;
    }

    public String getFavoured() {
        return favoured;
    }

    public void setFavoured(String favoured) {
        this.favoured = favoured;
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
}
