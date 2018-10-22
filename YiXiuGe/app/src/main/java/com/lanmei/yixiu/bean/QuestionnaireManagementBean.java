package com.lanmei.yixiu.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xkai on 2018/10/19.
 */

public class QuestionnaireManagementBean implements Serializable{


    /**
     * id : 22
     * uid : 820515
     * title : 刘灵玲
     * addtime : 1540199414
     * number : 1
     * status : null
     * gid : null
     * content : 哦坡头
     * quest_num : null
     * state : 0
     * cid : 44
     * starttime : 1540199400
     * endtime : 1540200060
     * quest : [{"select":[{"id":1,"text":"八路"},{"id":2,"text":"土语"}],"title":"枯叶","type":"1"}]
     * is_del : null
     * submit_num : 0
     */

    private String id;
    private String uid;
    private String title;
    private String addtime;
    private String number;
    private String status;
    private String gid;
    private String content;
    private String quest_num;
    private String state;
    private String cid;
    private String starttime;
    private String endtime;
    private String is_del;
    private String submit_num;
    private List<QuestBean> quest;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getQuest_num() {
        return quest_num;
    }

    public void setQuest_num(String quest_num) {
        this.quest_num = quest_num;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getIs_del() {
        return is_del;
    }

    public void setIs_del(String is_del) {
        this.is_del = is_del;
    }

    public String getSubmit_num() {
        return submit_num;
    }

    public void setSubmit_num(String submit_num) {
        this.submit_num = submit_num;
    }

    public List<QuestBean> getQuest() {
        return quest;
    }

    public void setQuest(List<QuestBean> quest) {
        this.quest = quest;
    }

    public static class QuestBean implements Serializable{
        /**
         * select : [{"id":1,"text":"八路"},{"id":2,"text":"土语"}]
         * title : 枯叶
         * type : 1
         */

        private String title;
        private String type;
        private List<SelectBean> select;
        private String answer;

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public String getAnswer() {
            return answer;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<SelectBean> getSelect() {
            return select;
        }

        public void setSelect(List<SelectBean> select) {
            this.select = select;
        }

        public static class SelectBean implements Serializable{
            /**
             * id : 1
             * text : 八路
             */

            private int id;
            private String text;
            private boolean isSelect;

            public void setSelect(boolean select) {
                isSelect = select;
            }

            public boolean isSelect() {
                return isSelect;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }
        }
    }
}
