package com.medui.yixiu.event;

/**
 * Created by xkai on 2018/9/4.
 * 教程相关操作（点赞、评论）
 */

public class CourseOperationEvent {

    private String id;
    private String view;//观看数
    private String reviews;//评论数
    private String like;//点赞数
    private String favour;//收藏数量
    private String favoured;//是否收藏

    public String getLike() {
        return like;
    }

    public String getReviews() {
        return reviews;
    }

    public String getFavoured() {
        return favoured;
    }

    public String getFavour() {
        return favour;
    }

    public String getView() {
        return view;
    }

    public String getViewNum() {
        return view;
    }

    public String getId() {
        return id;
    }

    private String liked;

    public String getLiked() {
        return liked;
    }

    public CourseOperationEvent(String id, String liked,String like,String view,String reviews,String favoured,String favour){
        this.id = id;
        this.liked = liked;
        this.view = view;
        this.reviews = reviews;
        this.favoured = favoured;
        this.favour = favour;
        this.like = like;
    }

}
