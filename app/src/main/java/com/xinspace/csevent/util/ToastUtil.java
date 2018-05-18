package com.xinspace.csevent.util;

import android.widget.Toast;

import com.xinspace.csevent.app.CoresunApp;

/**
 * Created by Yangtuhua on 2015/11/23.
 */
public class ToastUtil {
    /**
     * 吐丝信息
     *
     * @param msg 要显示的提示信息
     */
    public static void makeToast(String msg) {
        Toast.makeText(CoresunApp.instance, msg, Toast.LENGTH_SHORT).show();
    }
}
