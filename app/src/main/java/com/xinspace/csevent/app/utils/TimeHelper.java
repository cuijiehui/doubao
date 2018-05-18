package com.xinspace.csevent.app.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by MartinGrice on 2015/8/21 0021.
 */

public class TimeHelper {
    /**
     * get time stamp for php
     *
     * @return timeStamp
     */
    public static long getCurrentTimeMillis() {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy年MM月dd日 HH:mm:ss ");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String timeStampStr = String.valueOf(curDate.getTime());
        long timeStampL = Long.parseLong((String) timeStampStr.subSequence(0,
                timeStampStr.length() - 3));
        return timeStampL;
    }

    private static SimpleDateFormat sf = null;

//    public static String getCurrentDate() {
//        Date d = new Date();
//        sf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
//        return sf.format(d);
//    }

    public static long dateDifCalculate(long lastime) {

        long diff = getCurrentTimeMillis() - lastime;// 这样得到的差值是微秒级别
        long days = diff / (60 * 60 * 24);
        long hours = (diff - days * (60 * 60 * 24)) / (60 * 60);
        long minutes = (diff - days * (60 * 60 * 24) - hours * (60 * 60)) / (60);
        return minutes;
    }

    /* 时间戳转换成字符窜 */
    public static String getDateString(String string) {

        Date d = new Date(Long.parseLong(string));
        sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sf.format(d);
    }

    public static String getDateString2(String string) {

        Date d = new Date(Long.parseLong(string));
        sf = new SimpleDateFormat("yyyyMMddHH:mm:ss");
        return sf.format(d);
    }


    /* 时间戳转换成字符窜 */
    public static String getDate(String string) {

        Date d = new Date(Long.parseLong(string));
        sf = new SimpleDateFormat("yyyy-MM-dd");
        return sf.format(d);
    }
    
    
    /* 将字符串转为时间戳 */
    public static long getStringToDate(String time) {
        sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try {
            date = sf.parse(time);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return date.getTime();
    }


}
