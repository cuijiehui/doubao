package com.xinspace.csevent.app;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Vibrator;
import android.support.multidex.MultiDex;
import android.widget.Toast;

import com.mob.MobApplication;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.xinspace.csevent.data.biz.DownloadManger;
import com.xinspace.csevent.data.entity.DownloadStateSubject;
import com.xinspace.csevent.data.sharedprefs.AppSharedPrefs;
import com.xinspace.csevent.exception.CrashHandler;
import com.xinspace.csevent.login.util.LoginStatusUtil;
import com.xinspace.csevent.ui.activity.WebViewActivity;
import com.xinspace.csevent.util.LogUtil;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by Yangtuhua on 2015/11/22.
 * 此类集成Application,用于应用在运行时执行一些初始化操作
 */
public class CoresunApp extends MobApplication {
    //全局实例
    public static CoresunApp instance;

    //app 渠道
    public static String channel = null;

    //用于测试阶段
    public static boolean isTest = true;
    //用于保存用户id
    public static String USER_ID = null;

    //用于保存用户Tel
    public static String USER_TEL;
    //第三方登录个人数据
    public static String username;
    public static String userIcon;

    public static DownloadStateSubject downloadSubject = new DownloadStateSubject();//观察者

    //此map的键为对应软件的下载链接
    public static HashMap<String, DownloadManger> downloadManagers = new HashMap<>();//下载器的集合
    public static String province;//省
    public static String city;//市
    public static String area;//区
    public static Context context;
    public Vibrator mVibrator;
    private static Boolean isSetDoorDefault;
    public static boolean isLogin;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this.getApplicationContext();
        instance = this;
        channel = getChannel(context);

        isSetDoorDefault = new AppSharedPrefs(this).getDefault();

        isLogin = LoginStatusUtil.checkLoginStatus();

        ZXingLibrary.initDisplayOpinion(this);

        //监控内存泄漏
        //LeakCanary.install(this);

        //初始化百度地图
        //SDKInitializer.initialize(getApplicationContext());

        //initX5Environment();
//        UMConfigure.init(this,UMConfigure.DEVICE_TYPE_PHONE,null);
        //初始化ImageLoader
        initImageLoader();
        //这是收集异常信息的单例类，具体代码请看下文
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());//初始化
//        TelephonyManager TelephonyMgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
//        String szImei = TelephonyMgr.getDeviceId();
        //智果门禁初始化
        //DMVPhoneModel.initDMVoipSDK(context , "智果");

        //SayeeManager.getInstance().initalize(getApplicationContext());
        //注册友盟消息推送
        UMConfigure.init(this, "56df8aa7e0f55ac9ee0021a6", "shide", UMConfigure.DEVICE_TYPE_PHONE,
                "b30970ed02349e6e851b6d8d28e5b4af");
        initUpush();
    }
    private void initUpush(){
        LogUtil.i("消息推送测试" );

        PushAgent mPushAgent = PushAgent.getInstance(this);
//        handler = new Handler(getMainLooper());

        //sdk开启通知声音
        mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE);
        // sdk关闭通知声音
        // mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE);
        // 通知声音由服务端控制
        // mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SERVER);

        // mPushAgent.setNotificationPlayLights(MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE);
        // mPushAgent.setNotificationPlayVibrate(MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE);

        UmengMessageHandler messageHandler = new UmengMessageHandler() {

            /**
             * 通知的回调方法（通知送达时会回调）
             */
            @Override
            public void dealWithNotificationMessage(Context context, UMessage msg) {
                //调用super，会展示通知，不调用super，则不展示通知。
                LogUtil.i("dealWithNotificationMessage 消息推送测试" );

                super.dealWithNotificationMessage(context, msg);
            }

            /**
             * 自定义消息的回调方法
             */
            @Override
            public void dealWithCustomMessage(final Context context, final UMessage msg) {
                LogUtil.i("dealWithCustomMessage 消息推送测试" );

//                handler.post(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        // TODO Auto-generated method stub
//                        // 对自定义消息的处理方式，点击或者忽略
//                        boolean isClickOrDismissed = true;
//                        if (isClickOrDismissed) {
//                            //自定义消息的点击统计
//                            UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
//                        } else {
//                            //自定义消息的忽略统计
//                            UTrack.getInstance(getApplicationContext()).trackMsgDismissed(msg);
//                        }
//                        Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
//                    }
//                });
            }

            /**
             * 自定义通知栏样式的回调方法
             */
            @Override
            public Notification getNotification(Context context, UMessage msg) {
                LogUtil.i("getNotification 消息推送测试" );

//                switch (msg.builder_id) {
//                    case 1:
//                        Notification.Builder builder = new Notification.Builder(context);
//                        RemoteViews myNotificationView = new RemoteViews(context.getPackageName(),
//                                R.layout.notification_view);
//                        myNotificationView.setTextViewText(R.id.notification_title, msg.title);
//                        myNotificationView.setTextViewText(R.id.notification_text, msg.text);
//                        myNotificationView.setImageViewBitmap(R.id.notification_large_icon, getLargeIcon(context, msg));
//                        myNotificationView.setImageViewResource(R.id.notification_small_icon,
//                                getSmallIconId(context, msg));
//                        builder.setContent(myNotificationView)
//                                .setSmallIcon(getSmallIconId(context, msg))
//                                .setTicker(msg.ticker)
//                                .setAutoCancel(true);
//
//                        return builder.getNotification();
//                    default:
                //默认为0，若填写的builder_id并不存在，也使用默认。
                return super.getNotification(context, msg);
//                }
            }
        };
        mPushAgent.setMessageHandler(messageHandler);

        /**
         * 自定义行为的回调处理，参考文档：高级功能-通知的展示及提醒-自定义通知打开动作
         * UmengNotificationClickHandler是在BroadcastReceiver中被调用，故
         * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
         * */
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {

            @Override
            public void launchApp(Context context, UMessage msg) {

                if(msg.extra!=null){
                    if(msg.extra.get("url")!=null){
                       AppConfig.WEB_URL=msg.extra.get("url");
                    }
                }
                super.launchApp(context, msg);
            }

            @Override
            public void openUrl(Context context, UMessage msg) {
                LogUtil.i("openUrl 消息推送测试" );

                super.openUrl(context, msg);
            }

            @Override
            public void openActivity(Context context, UMessage msg) {
                LogUtil.i("openActivity 消息推送测试" );

                super.openActivity(context, msg);
            }

            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                LogUtil.i("dealWithCustomAction 消息推送测试" );

                Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
            }
        };
        //使用自定义的NotificationHandler
        mPushAgent.setNotificationClickHandler(notificationClickHandler);

        //注册推送服务 每次调用register都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                LogUtil.i(" 消息推送测试 device token:" + deviceToken);

//                sendBroadcast(new Intent(UPDATE_STATUS_ACTION));
            }

            @Override
            public void onFailure(String s, String s1) {
                LogUtil.i(" 消息推送测试 register failed:" + s + " " + s1);

//                sendBroadcast(new Intent(UPDATE_STATUS_ACTION));
            }
        });

        //使用完全自定义处理
        //mPushAgent.setPushIntentServiceClass(UmengNotificationService.class);

        //小米通道
        //MiPushRegistar.register(this, XIAOMI_ID, XIAOMI_KEY);
        //华为通道
        //HuaWeiRegister.register(this);
        //魅族通道
        //MeizuRegister.register(this, MEIZU_APPID, MEIZU_APPKEY);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        // 程序终止的时候执行
        LogUtil.i("------------------程序终止了------------");
    }


    @Override
    public void onTrimMemory(int level) {
        // 程序在内存清理的时候执行
        LogUtil.i("onTrimMemory" + level);
        super.onTrimMemory(level);
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     *
     *
     */
    private void initX5Environment() {
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        //TbsDownloader.needDownload(getApplicationContext(), false);
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                LogUtil.i("apptbs" + " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub

            }
        };
        QbSdk.setTbsListener(new TbsListener() {
            @Override
            public void onDownloadFinish(int i) {
                LogUtil.i("apptbs" + "onDownloadFinish");
            }

            @Override
            public void onInstallFinish(int i) {
                LogUtil.i("apptbs" + "onInstallFinish");
            }

            @Override
            public void onDownloadProgress(int i) {
                LogUtil.i("apptbs" + "onDownloadProgress:" + i);
            }
        });
        QbSdk.initX5Environment(getApplicationContext(), cb);
    }


    //退出应用
    public void exit() {
        try {
            android.os.Process.killProcess(android.os.Process.myPid());
            // 结束进程
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * 初始化ImageLoader
     */
    private void initImageLoader() {
        File cacheDir = StorageUtils.getOwnCacheDirectory(this, "imageloader/Cache");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(4 * 1024 * 1024))
                .memoryCacheSizePercentage(13) // default
                .diskCacheSize(50 * 1024 * 1024)
                .writeDebugLogs()
                //.discCache(new UnlimitedDiscCache(cacheDir))//自定义缓存路径
                .build();

        //Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }


    /**
     * 获取包的渠道
     * @param context
     * @return
     */
    public static String getChannel(Context context) {
        ApplicationInfo appinfo = context.getApplicationInfo();
        String sourceDir = appinfo.sourceDir;
        String ret = "";
        ZipFile zipfile = null;
        try {
            zipfile = new ZipFile(sourceDir);
            Enumeration<?> entries = zipfile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = ((ZipEntry) entries.nextElement());
                String entryName = entry.getName();
                if (entryName.startsWith("channel")) {
                    ret = entryName;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zipfile != null) {
                try {
                    zipfile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        String[] split = ret.split("_");
        if (split != null && split.length >= 2) {

            LogUtil.i("渠道为" + ret.substring(split[0].length() + 1));

            return ret.substring(split[0].length() + 1);
        } else {
            return "shide";
        }
    }

    public boolean getShopOrDoor(){
        return isSetDoorDefault;
    }

}
