package com.medui.yixiu.event;

/**
 * Created by xkai on 2018/11/9.
 */

public class TestTimeEvent {

    private int second;

    public int getSecond() {
        return second;
    }

    private String time;

    public TestTimeEvent(String time,int second) {
        this.time = time;
        this.second = second;
    }

    public String getTime() {
        return time;
    }
}
