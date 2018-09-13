package com.lanmei.yixiu.event;

/**
 * Created by xkai on 2018/9/13.
 * 班级选择
 */

public class ClassSelectEvent {

    private int fatherPosition;
    private int childPosition;

    public ClassSelectEvent(int fatherPosition, int childPosition) {
        this.fatherPosition = fatherPosition;
        this.childPosition = childPosition;
    }

    public int getFatherPosition() {
        return fatherPosition;
    }

    public int getChildPosition() {
        return childPosition;
    }

}
