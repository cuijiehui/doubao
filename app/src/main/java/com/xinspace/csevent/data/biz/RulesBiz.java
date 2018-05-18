package com.xinspace.csevent.data.biz;

import android.content.Context;

import com.xinspace.csevent.app.AppConfig;
import com.xinspace.csevent.util.HttpUtil;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.ui.activity.RulesActivity;

/**
 * 会员中心的抽奖规则页面接口
 */
public class RulesBiz {
    public static void getRules(Context context){
        try{
            String url=AppConfig.GET_RULES_URL;
            HttpUtil http=new HttpUtil();
            http.setOnHttpRequestFinishListener((RulesActivity)context);
            http.sendGet(url);
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.i("抽奖规则页面接口数据异常:"+e.getMessage());
        }
    }
}
