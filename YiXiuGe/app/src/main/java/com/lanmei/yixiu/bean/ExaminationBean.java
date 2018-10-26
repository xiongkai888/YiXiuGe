package com.lanmei.yixiu.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by xkai on 2018/10/25.
 * 学生考试列表
 */

public class ExaminationBean {

    /**
     * id : 13
     * title : 1111
     * score : 0.0
     * passscore : 0.0
     * exam_type : a:5:{i:1;a:3:{i:1;s:1:"3";i:2;s:1:"1";i:3;s:1:"1";}i:2;a:3:{i:1;s:0:"";i:2;s:0:"";i:3;s:0:"";}i:3;a:3:{i:1;s:0:"";i:2;s:0:"";i:3;s:0:"";}i:4;a:3:{i:1;s:0:"";i:2;s:0:"";i:3;s:0:"";}i:6;a:3:{i:1;s:0:"";i:2;s:0:"";i:3;s:0:"";}}
     * num : 5
     * exam : [{"id":"8","title":"检验高压灭菌效果最可靠的方法","answer":"2","model":"1","options":{"1":" 作细菌培养","2":"包内和包外各一条灭菌指示纸带","3":"置入包内的升华硫磺是否融化","4":"观察手术切口有无感染","5":"置入包内的明矾粉是否液化"}},{"id":"2","title":"问题","answer":"1","model":"1","options":{"1":" 答案1","2":" 答案2","3":" 答案3"}},{"id":"7","title":"11111","answer":"5","model":"1","options":{"1":" 1111234","2":"12342","3":"434","4":"124","5":"2134"}},{"id":"9","title":"手术区皮肤消毒范围应包括切口周围","answer":"3","model":"1","options":{"1":" 5cm","2":"10cm","3":"15cm","4":"20cm","5":"30cm"}},{"id":"1","title":"问题","answer":"1","model":"1","options":{"1":" 答案1","2":" 答案2","3":" 答案3","4":" 答案4"}}]
     */

    private String id;
    private String title;
    private String score;
    private String passscore;
    private String exam_type;
    private String num;
    private String sub_time;//提交时间
    private String result;//考试结果
    private List<ExaminationAnswerBean> beanList;//答案列表

    public void setBeanList(List<ExaminationAnswerBean> beanList) {
        this.beanList = beanList;
    }

    public List<ExaminationAnswerBean> getBeanList() {
        return beanList;
    }

    private List<ExamBean> exam;

    public void setSub_time(String sub_time) {
        this.sub_time = sub_time;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getSub_time() {
        return sub_time;
    }

    public String getResult() {
        return result;
    }

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

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getPassscore() {
        return passscore;
    }

    public void setPassscore(String passscore) {
        this.passscore = passscore;
    }

    public String getExam_type() {
        return exam_type;
    }

    public void setExam_type(String exam_type) {
        this.exam_type = exam_type;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public List<ExamBean> getExam() {
        return exam;
    }

    public void setExam(List<ExamBean> exam) {
        this.exam = exam;
    }

    public static class ExamBean {
        /**
         * id : 8
         * title : 检验高压灭菌效果最可靠的方法
         * answer : 2
         * model : 1
         * options : {"1":" 作细菌培养","2":"包内和包外各一条灭菌指示纸带","3":"置入包内的升华硫磺是否融化","4":"观察手术切口有无感染","5":"置入包内的明矾粉是否液化"}
         */

        private String id;
        private String title;
        private String answer;
        private String model;
        private String options;
        private List<ExaminationTopicBean> list;//自己加

        public List<ExaminationTopicBean> getList() {
            return list;
        }

        public void setMap(Map<String, String> map) {
            list = new ArrayList<>();
            for (String key : map.keySet()) {
                ExaminationTopicBean topicBean = new ExaminationTopicBean();
                topicBean.setTopic(map.get(key));
                list.add(topicBean);
            }
        }

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

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String getOptions() {
            return options;
        }

        public void setOptions(String options) {
            this.options = options;
        }
    }
}
