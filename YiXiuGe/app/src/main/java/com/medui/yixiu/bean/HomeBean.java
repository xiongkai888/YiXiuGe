package com.medui.yixiu.bean;

/**
 * @author xkai 首页新闻分类列表
 */
public class HomeBean {

    /**
     * id : 5
     * pid : 0
     * title : 新手教学帮助
     * sort : 0
     * status : 1
     * img : /Uploads/order/2018-06-25/index5.PNG
     * subtitle : 任务帮助/视频教程/平台规则/违规说明
     */

    private String id;
    private String pid;
    private String title;
    private String sort;
    private String status;
    private String img;
    private String subtitle;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

}