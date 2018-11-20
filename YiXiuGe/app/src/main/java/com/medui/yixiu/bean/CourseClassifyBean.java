package com.medui.yixiu.bean;

/**
 * Created by xkai on 2018/8/17.
 *  教程分类
 */

public class CourseClassifyBean {


    
    private boolean isChoose;//发布教程的分类列表（需要）
    /**
     * id : 31
     * name : 骨科
     * parent_id : 0
     * sort : 2
     * visibility : 1
     * model_id : 0
     * keywords : null
     * descript : null
     * title : null
     * pic : 
     * update_time : 0
     */

    private String id;
    private String name;
    private String parent_id;
    private String sort;
    private String visibility;
    private String model_id;
    private String keywords;
    private String descript;
    private String title;
    private String pic;
    private String update_time;

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

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getModel_id() {
        return model_id;
    }

    public void setModel_id(String model_id) {
        this.model_id = model_id;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }
}
