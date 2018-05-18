package com.xinspace.csevent.data.biz;

import android.content.Context;

import com.xinspace.csevent.app.CoresunApp;
import com.xinspace.csevent.data.entity.Params;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.login.service.LoginIntentService;
import com.xinspace.csevent.app.AppConfig;
import com.xinspace.csevent.util.HttpUtil;
import com.xinspace.csevent.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizhihong on 2015/12/8.
 * 登录业务
 */
public class LoginBiz {

    //本地账号系统登录
    public static void localLogin(Context context,String phoneNum,String password){

        LogUtil.i("pppppppppppppppppppppppppppppppppp");

        String url = AppConfig.LOGIN_URL3;
        try {
            HttpUtil httpUtil = new HttpUtil();
            httpUtil.setOnHttpRequestFinishListener((LoginIntentService)context);
            List<Params> list = new ArrayList<>();
            list.add(new Params("mobile", phoneNum));
            list.add(new Params("pwd", password));
            httpUtil.sendPost(url, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void localLogin2(String phoneNum,String password , HttpRequestListener listener){

        LogUtil.i("pppppppppp222222222222222");

        String url = AppConfig.LOGIN_URL3;
        try {
            HttpUtil httpUtil = new HttpUtil();
            httpUtil.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("mobile", phoneNum));
            list.add(new Params("pwd", password));
            httpUtil.sendPost(url, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**第三方登录*/
    public static void thirdLogin(Context context,String openid,String state){

        String url = AppConfig.THIRD_LOGIN_URL;

        try {
            HttpUtil httpUtil = new HttpUtil();
            httpUtil.setOnHttpRequestFinishListener((LoginIntentService)context);
            List<Params> list = new ArrayList<>();
            list.add(new Params("openid", openid));
            list.add(new Params("state", state));
            httpUtil.sendPost(url, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**发送小奖品记录给服务器*/
    public static void sendPrizeRecord(String id, HttpRequestListener listener){
        String url = AppConfig.SEND_RECORD_URL;
        try {
            HttpUtil http=new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list=new ArrayList<>();
            list.add(new Params("uid", CoresunApp.USER_ID));
            list.add(new Params("id", id));
            http.sendPost(url,list);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
