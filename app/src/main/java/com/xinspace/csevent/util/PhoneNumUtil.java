package com.xinspace.csevent.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lizhihong on 2015/12/4.
 */
public class PhoneNumUtil {
    /**
     * 验证手机座机等号码
     */
    public static boolean isPhoneNO(String mobiles){

        Pattern p = Pattern.compile("((\\d{11})|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))$)");

        Matcher m = p.matcher(mobiles);

        return m.matches();

    }
    /**
     * 只验证手机号码
     */
    public static boolean isMobileNO(String mobiles){

        Pattern p = Pattern.compile("(\\d{11})$");

        Matcher m = p.matcher(mobiles);

        return m.matches();

    }
}
