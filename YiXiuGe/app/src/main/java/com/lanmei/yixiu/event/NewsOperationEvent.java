package com.lanmei.yixiu.event;

/**
 * Created by xkai on 2018/9/4.
 * 资讯评论、点击收藏时
 */

public class NewsOperationEvent {

    private String favoured;//是否收藏
    private String id;
    private String reviews;//评论数

    public String getReviews() {
        return reviews;
    }

    public String getFavoured() {
        return favoured;
    }


    public String getId() {
        return id;
    }


    public NewsOperationEvent(String id,String reviews, String favoured) {
        this.id = id;
        this.reviews = reviews;
        this.favoured = favoured;
    }
}
