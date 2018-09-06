package com.lanmei.yixiu.bean;

import java.util.List;

/**
 * Created by xkai on 2018/9/
 * 笔记
 */

public class NotesBean {

    /**
     * id : 1
     * title : 第一次上传笔记
     * pic : ["http://gzyxg.oss-cn-shenzhen.aliyuncs.com/lanmei/yixiuge/img1/2018-09-06%2017%3A40%3A31-0.jpg","http://gzyxg.oss-cn-shenzhen.aliyuncs.com/lanmei/yixiuge/img1/2018-09-06%2017%3A40%3A31-1.jpg","http://gzyxg.oss-cn-shenzhen.aliyuncs.com/lanmei/yixiuge/img1/2018-09-06%2017%3A40%3A31-2.jpg","http://gzyxg.oss-cn-shenzhen.aliyuncs.com/lanmei/yixiuge/img1/2018-09-06%2017%3A40%3A31-3.jpg","http://gzyxg.oss-cn-shenzhen.aliyuncs.com/lanmei/yixiuge/img1/2018-09-06%2017%3A40%3A31-4.jpg","http://gzyxg.oss-cn-shenzhen.aliyuncs.com/lanmei/yixiuge/img1/2018-09-06%2017%3A40%3A31-5.jpg","http://gzyxg.oss-cn-shenzhen.aliyuncs.com/lanmei/yixiuge/img1/2018-09-06%2017%3A40%3A31-6.jpg"]
     * enclosure : 附件是要传什么东东啦？
     * addtime : 1536226832
     * uid : 820515
     * content : 恭喜发财
     */

    private String id;
    private String title;
    private String enclosure;
    private String addtime;
    private String uid;
    private String content;
    private List<String> pic;

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

    public String getEnclosure() {
        return enclosure;
    }

    public void setEnclosure(String enclosure) {
        this.enclosure = enclosure;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getPic() {
        return pic;
    }

    public void setPic(List<String> pic) {
        this.pic = pic;
    }
}
