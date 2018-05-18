package com.xinspace.csevent.util;

import android.os.Environment;

import com.xinspace.csevent.app.CoresunApp;

/**
 * 此类用来定义定义常量
 */
public class Const {

    //阿里云oss初始化所需参数
    public static final String END_POINT="http://pic.coresun.net";
    public static final String BUCKET= "shidedata";

    public static final String END_POINT_Test="http://pictest.coresun.net";
    public static final String BUCKETTEST = "shidetest";
    public static final String OBJECT_NAME="user_avatar/"+ CoresunApp.USER_ID+"/user_avatar.png";


    //第三方app应用"空港云"的下载地址
    public static final String THE_THIRD_APP_URL="http://app.mi.com/detail/85389";

    //发现页面Apk下载保存路径
    public static final String APK_STEORY_PATH= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();

    //登录界面广播Action
    public static final String ACTION_LOGIN="ACTION_LOGIN";

    //改变签到状态的广播
    public static final String ACTION_CHANGE_QIAN_DAO_STATE="ACTION_CHANGE_QIAN_DAO_STATE";

    //登录成功之后判断签到状态
    public static final String ACTION_TO_CHECK_THE_STATE_OF_QIAN_DAO="ACTION_TO_CHECK_THE_STATE_OF_QIAN_DAO";

    //网络状态发生变化的广播
    public static final String ACTION_CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";

    //改变未有收货地址的奖品的type和id
    public static final String ACTION_CHANGE_TYPE_AND_ID="ACTION_CHANGE_TYPE_AND_ID";

    //下载的几种状态
    public static final int DOWNLOAD_STATE_READY=1;//未开始
    public static final int DOWNLOAD_STATE_DOWNLOADING=2;//进行中
    public static final int DOWNLOAD_STATE_PAUSE=3;//暂停
    public static final int DOWNLOAD_STATE_STOP=4;//停止
    public static final int DOWNLOAD_STATE_FINISH=5;//已经完成

}
