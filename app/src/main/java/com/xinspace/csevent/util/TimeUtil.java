package com.xinspace.csevent.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 时间工具类
 */
public class TimeUtil {
    /**
     * 获取当前时间
     * @return 当前时间
     */
    public static String getTime(){
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }
}
