package com.xinspace.csevent.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;

public class NewcomersTutorialUtil {
    /**判断是不是第一次使用程序,是则加载新手指导页面*/
    /**
     *
     * @param activity 新手指导页面出现在哪个activity
     * @param whichPage  哪个新手指导页面(用来判定此页面是否已经出现过指导页面)
     * @param whichLayout 加载哪个layout(主要是不同布局显示不同的背景)
     */
    public static void loadToNewcomersTutorial(Activity activity,String whichPage,int whichLayout) {
        SharedPreferences sp   = activity.getSharedPreferences("IsNewcomers", Context.MODE_PRIVATE);
        //判断是不是首次使用应用
        if (sp.getBoolean(whichPage, true)) {//sp.getBoolean("firststart", true)
            LogUtil.i("是否首次使用应用-新手指导:"+sp.getBoolean("firstUse", true));
            SharedPreferences.Editor editor = sp.edit();
            //将登录标志位设置为false，下次使用应用时不再显示指导页面
            editor.putBoolean(whichPage, false);
            editor.commit();
            Dialog dialogTutorial= DialogUtil.createLoadingTutorial(activity, whichLayout);
            dialogTutorial.show();
            /*finish();*/
        }
    }

}

