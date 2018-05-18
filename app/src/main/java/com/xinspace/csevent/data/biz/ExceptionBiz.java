package com.xinspace.csevent.data.biz;

import android.content.Context;

import com.xinspace.csevent.data.entity.Params;
import com.xinspace.csevent.exception.CrashHandler;
import com.xinspace.csevent.app.AppConfig;
import com.xinspace.csevent.util.HttpUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/10.
 */
public class ExceptionBiz {

    public static void sendException(Context context,String app_version,String modeltype,String sys_version,String errlog,Thread.UncaughtExceptionHandler handler){

            String url= AppConfig.ERR_LOG_URL;

            try {
                HttpUtil httpUtil= new HttpUtil();
                httpUtil.setOnHttpRequestFinishListener((CrashHandler)handler);
                List<Params> list=new ArrayList<>();
                list.add(new Params("app_version", app_version));
                list.add(new Params("platform", "0"));
                list.add(new Params("modeltype", modeltype));
                list.add(new Params("sys_version", sys_version));
                list.add(new Params("errlog", errlog));
                httpUtil.sendPost(url,list);
            } catch (Exception e) {
                e.printStackTrace();
            }



    }
}
