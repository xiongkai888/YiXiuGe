package com.othershe.calendarview.bean;

/**
 * Created by xkai on 2018/9/16.
 */

public class CalendarEvent {
    private int year;

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getMonthDays() {
        return monthDays;
    }

    private int month;
    private int monthDays;

    public int getPosition() {
        return position;
    }

    private int position;
    public CalendarEvent(int year,int month,int monthDays,int position){
        this.year = year;
        this.month = month;
        this.position = position;
        this.monthDays = monthDays;
    }
}
