package com.xinspace.csevent.data.biz;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.xinspace.csevent.data.entity.Params;
import com.xinspace.csevent.ui.fragment.MainPageFragment;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.app.AppConfig;
import com.xinspace.csevent.util.HttpUtil;
import com.xinspace.csevent.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 版本更新业务,获取最新版本号等信息
 */
public class UpdateVersionBiz {
    public static void getVersion(MainPageFragment fragment, HttpRequestListener listener){
        try{
            String url=AppConfig.UPDATE_SOFO_URL;
            //获得当前版本号
            Context context=fragment.getContext();
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String versionName = info.versionName;

            HttpUtil http=new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            //list.add(new Params("cv", versionName));//当前版本号
            list.add(new Params("type", "1"));
            http.sendPost(url,list);
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.i("版本更新接口出错:"+e.getMessage());
        }
    }
}
