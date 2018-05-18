package com.xinspace.csevent.data.entity;

/**
 * 此实体用于转换一个时间,
 * 传递一个秒数过来,则转成一个xx小时xx分xx秒
 */
public class TimeLeftEntity {
    private int time;

    private int days;
    private int seconds;
    private int hours;
    private int min;

    public TimeLeftEntity(int time) {
        this.time = time;
        parserTimeWithDay();
    }
    //转换时间,含天
    private void parserTimeWithDay() {
        //分
        min =time/60;
        //秒
        seconds =time%60;

        //时
        hours = min /60;
        //分
        min %=60;

        //天
        days= hours /24;

        hours %=24;

        //秒
        seconds = time - days * 60 * 24 * 60 - hours*3600 - min*60;
    }

    public String getDays(){
        return String.valueOf(days);
    }
    public String getHoursWithDay(){
        return String.valueOf(hours);
    }
    public String getMinWithDay(){
        return String.valueOf(min);
    }
    public String getSecondsWithDay(){
        return String.valueOf(seconds);
    }
}
