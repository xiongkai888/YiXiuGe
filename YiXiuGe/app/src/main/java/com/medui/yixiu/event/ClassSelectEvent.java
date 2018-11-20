package com.medui.yixiu.event;

import com.medui.yixiu.bean.ClassSelectBean;

import java.util.List;

/**
 * Created by xkai on 2018/9/13.
 * 班级选择
 */

public class ClassSelectEvent {

    private List<ClassSelectBean.XiajiBean> list;

    public List<ClassSelectBean.XiajiBean> getList() {
        return list;
    }

    public ClassSelectEvent(List<ClassSelectBean.XiajiBean> list) {
        this.list = list;
    }


}
