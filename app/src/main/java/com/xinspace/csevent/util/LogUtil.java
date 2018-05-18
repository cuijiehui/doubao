package com.xinspace.csevent.util;

import android.util.Log;

import com.xinspace.csevent.app.CoresunApp;

public class LogUtil {
    /**
     * 打印信息
     * @param msg 要打印的信息
     */
    public static void i(String msg) {
        if (CoresunApp.isTest) {
            Log.i("www", msg);
        }
    }

    public static void v(String msg) {
        if (CoresunApp.isTest) {
            Log.v("www", msg);
        }
    }

    public static void e(String msg) {
        if (CoresunApp.isTest) {
            Log.e("www", msg);
        }
    }

    public static void d(String msg) {
        if (CoresunApp.isTest) {
            Log.d("www", msg);
        }
    }
}
