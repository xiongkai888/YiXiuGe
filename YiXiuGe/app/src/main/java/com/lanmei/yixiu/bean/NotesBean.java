package com.lanmei.yixiu.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xkai on 2018/9/
 * 笔记
 */

public class NotesBean implements Serializable{

    /**
     * id : 4
     * title : 哈哈
     * pic : ["http://gzyxg.oss-cn-shenzhen.aliyuncs.com/lanmei/yixiuge/img1/2018-09-07%2011%3A49%3A58-0.jpg"]
     * enclosure : [{"name":"1617721478.png","url":"/storage/emulated/0/.360gamecentersdk/com.tciplay.doa5if.qihoo/pushsdk/1617721478.png"}]
     * addtime : 1536292200
     * uid : 820515
     * content : 啊啊啊
     */

    private String id;
    private String title;
    private String addtime;
    private String uid;
    private String content;
    private List<String> pic;
    private List<EnclosureBean> enclosure;

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

    public List<EnclosureBean> getEnclosure() {
        return enclosure;
    }

    public void setEnclosure(List<EnclosureBean> enclosure) {
        this.enclosure = enclosure;
    }

    public static class EnclosureBean implements Serializable{
        /**
         * name : 1617721478.png
         * url : /storage/emulated/0/.360gamecentersdk/com.tciplay.doa5if.qihoo/pushsdk/1617721478.png
         */

        private String name;
        private String url;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
