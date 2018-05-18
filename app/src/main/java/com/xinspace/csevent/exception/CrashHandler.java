package com.xinspace.csevent.exception;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Looper;
import android.widget.Toast;

import com.xinspace.csevent.app.CoresunApp;
import com.xinspace.csevent.app.utils.TimeHelper;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.biz.ExceptionBiz;
import com.xinspace.csevent.data.biz.GetDataBiz;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.ToastUtil;

import org.json.JSONException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Administrator on 2015/12/9.
 *
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 * 实现UncaughtExceptionHandler接口
 * */

public class CrashHandler implements Thread.UncaughtExceptionHandler,HttpRequestListener{
    public static final String TAG = "bug";

    //系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    //CrashHandler实例
    private static CrashHandler INSTANCE = new CrashHandler();
    //程序的Context对象
    private Context mContext;
    //用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<String, String>();
    //用于格式化日期,作为日志文件名的一部分
    private DateFormat  df= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String appVersion;
    private String modelType;
    private String sysVersion;
    private String fingerprint;
    private String exceptionTime;
    private String stacktrace;
    private SDPreference sdPreference;

    /**
     * 保证只有一个CrashHandler实例
     */
    private CrashHandler() {
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        mContext = context;
        sdPreference = SDPreference.getInstance();
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                //Thread.sleep(3000);
                LogUtil.i("使用了自定义处理异常");
            } catch (Exception e) {
                LogUtil.i("全局异常处理--error : " + e);
            }
            //调用系统默认处理异常业务(结果打印在logcat)
            mDefaultHandler.uncaughtException(thread, ex);

            long time = System.currentTimeMillis();
            sdPreference.putContent("appEnd" , time);
            String imei = sdPreference.getContent("szImei");
            String duration = String.valueOf((time - sdPreference.getLongContent("appStart")) / 1000);
            String movie = sdPreference.getContent("video");
            String movietime =  String.valueOf((sdPreference.getLongContent("videoEndTime") - sdPreference.getLongContent("videoStartTime")) / 1000);
            String air = sdPreference.getContent("air");
            String smart = sdPreference.getContent("smart");
            upstatisticsData(imei , TimeHelper.getDateString(String.valueOf(time)) , "1" , duration , movie , movietime , air , smart , CoresunApp.channel);

            //退出程序
            CoresunApp.instance.exit();
            //           android.os.Process.killProcess(android.os.Process.myPid());
            //            System.exit(1);
        }
    }


    private void upstatisticsData(String imei , String time , String startflag, String duration ,
                                  String movie , String movietime , String air, String smart ,
                                  String tagName ) {

        GetDataBiz.upstatisticsData(imei, time, startflag, duration, movie, movietime, air, smart,
                tagName, new HttpRequestListener() {
                    @Override
                    public void onHttpRequestFinish(String result) throws JSONException {

                    }

                    @Override
                    public void onHttpRequestError(String error) {

                    }
                });

    }


    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        //使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();

                Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_LONG).show();

                Looper.loop();
            }
        }.start();


        //收集设备参数信息
        PackageManager pm = mContext.getPackageManager();
        ApplicationInfo ai = mContext.getApplicationInfo();
        PackageInfo pi = null;
        try {
            pi = pm.getPackageInfo(ai.packageName, 0);
            appVersion=pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
         //获取跟踪的栈信息，除了系统栈信息，还把手机型号、系统版本、编译版本的唯一标示
        StackTraceElement[] trace = ex.getStackTrace();
        StackTraceElement[] trace2 = new StackTraceElement[trace.length+4];
        System.arraycopy(trace, 0, trace2, 0, trace.length);//从trace的0位置开始复制到trace2的0位置,复制长度为trace.length
        modelType=android.os.Build.MODEL;
        sysVersion= android.os.Build.VERSION.RELEASE;
        fingerprint=android.os.Build.FINGERPRINT;
        exceptionTime=df.format(new Date());
        trace2[trace.length+0] = new StackTraceElement("Android", "MODEL", modelType, -1);
        trace2[trace.length+1] = new StackTraceElement("Android", "VERSION", sysVersion, -1);
        trace2[trace.length+2] = new StackTraceElement("Android", "FINGERPRINT", fingerprint, -1);
        trace2[trace.length+3] = new StackTraceElement("Android", "EXCEPTION_TIME", exceptionTime, -1);
        //追加信息，因为后面会回调默认的处理方法
        ex.setStackTrace(trace2);
        ex.printStackTrace(printWriter);
        //把上面获取的堆栈信息转为字符串，打印出来
        stacktrace = result.toString();
        LogUtil.i("跟踪信息:" + stacktrace);
        printWriter.close();
        LogUtil.i("自定义异常信息:" + ex.toString() + "appVersion:" + appVersion);

        //发送错误信息到服务器
        ExceptionBiz.sendException(mContext, appVersion, modelType, sysVersion, stacktrace, CrashHandler.this);

        return true;
    }

    //发送错误日志后的回调方法
    @Override
    public void onHttpRequestFinish(String result) {
        LogUtil.i("日志提交结果"+result);
        ToastUtil.makeToast(result);
    }

    @Override
    public void onHttpRequestError(String error) {

    }
}