package com.othershe.calendarview.bean;

import java.io.Serializable;
import java.util.Arrays;

public class DateBean implements Serializable{
    private int[] solar;//阳历年、月、日
    private String[] lunar;//农历月、日
    private String solarHoliday;//阳历节假日
    private String lunarHoliday;//阳历节假日
    private int type;//0:上月，1:当月，2:下月
    private String term;//节气
    private int screen = 0;//在日历上显示  筛选0|1|2|3=>全部|上课|评价|已评

    public void setScreen(int screen) {
        this.screen = screen;
    }

    public int getScreen() {
        return screen;
    }

    public int[] getSolar() {
        return solar;
    }

    public void setSolar(int year, int month, int day) {
        this.solar = new int[]{year, month, day};
    }

    public String[] getLunar() {
        return lunar;
    }

    public void setLunar(String[] lunar) {
        this.lunar = lunar;
    }

    public String getSolarHoliday() {
        return solarHoliday;
    }

    public void setSolarHoliday(String solarHoliday) {
        this.solarHoliday = solarHoliday;
    }

    public String getLunarHoliday() {
        return lunarHoliday;
    }

    public void setLunarHoliday(String lunarHoliday) {
        this.lunarHoliday = lunarHoliday;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    @Override
    public String toString() {
        return "DateBean{" +
                "solar=" + Arrays.toString(solar) +
                ", lunar=" + Arrays.toString(lunar) +
                ", solarHoliday='" + solarHoliday + '\'' +
                ", lunarHoliday='" + lunarHoliday + '\'' +
                ", type=" + type +
                ", term='" + term + '\'' +
                ", screen=" + screen +
                '}';
    }
}
