package com.xinspace.csevent.data.entity;

import android.util.Log;

/**
 * 此实体用于转换一个时间,
 * 传递一个秒数过来,则转成一个xx小时xx分xx秒xx毫秒
 *
 */
public class TimeLeftEntity2 {

    private long time;
    String strMinute;  //分钟
    String strSecond;  //秒
    String strMilliSecond; //毫秒

    public TimeLeftEntity2(long time) {
        this.time = time;
        parserTimeWithDay();
    }
    //转换时间,含天
    private void parserTimeWithDay() {
        int ss = 1000;
        int mi = ss * 60;
        long minute = time/ mi;//计算分钟
        long second = (time- minute * mi) / ss;//总时间-分钟的毫秒数得到秒数
        long milliSecond = time  - minute * mi - second * ss;//获得毫秒数
        strMinute = minute < 10 ? "0" + minute : "" + minute;//分钟
        strSecond = second < 10 ? "0" + second : "" + second;//秒
        strMilliSecond = milliSecond < 10 ? "0" + milliSecond : "" + milliSecond;//毫秒不满十前面补0
        strMilliSecond = milliSecond >=100 ? strMilliSecond.substring(0,strMilliSecond.length()-1) : "" + strMilliSecond;////毫秒超过100显示前2位
        //显示分秒毫秒的倒计时
        //tv_status.setText(strMinute + " 分 "+strSecond+"秒"+strMilliSecond);
        Log.i("www" , strMinute + " 分 "+strSecond+"秒"+strMilliSecond);
    }




    public String getStrMin(){
        return strMinute;
    }
    public String getstrSecond(){
        return strSecond;
    }
    public String getstrMilliSecond(){
        return strMilliSecond;
    }
}
