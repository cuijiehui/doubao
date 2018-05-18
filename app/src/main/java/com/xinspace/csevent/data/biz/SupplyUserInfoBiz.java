package com.xinspace.csevent.data.biz;

import android.content.Context;
import android.text.TextUtils;

import com.xinspace.csevent.app.CoresunApp;
import com.xinspace.csevent.data.entity.Params;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.app.AppConfig;
import com.xinspace.csevent.util.HttpUtil;
import com.xinspace.csevent.ui.activity.SupplyUserInfoActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 第三方登陆补充个人资料
 */
public class SupplyUserInfoBiz {
    public static void supplyInfo(Context context,String name,String sex){
        try{
            String url=AppConfig.SUPPLY_USER_INFO_URL;
            HttpUtil http=new HttpUtil();
            http.setOnHttpRequestFinishListener((SupplyUserInfoActivity) context);
            List<Params> list=new ArrayList<>();
            list.add(new Params("uid", CoresunApp.USER_ID));
            list.add(new Params("sex", sex));
            list.add(new Params("nickname", name));
            http.sendPost(url,list);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void supplyInfo2(Context context,String name,String tel , HttpRequestListener listener){
        try{
            String url=AppConfig.SUPPLY_USER_INFO_URL;
            HttpUtil http=new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list=new ArrayList<>();
            list.add(new Params("uid", CoresunApp.USER_ID));
            list.add(new Params("sex", "1"));
            list.add(new Params("nickname", name));
            list.add(new Params("birthday", "0000-00-00"));
            list.add(new Params("salary", "3000"));
            list.add(new Params("email", ""));
            list.add(new Params("interest", ""));
            list.add(new Params("tel", tel));
            http.sendPost(url,list);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 修改昵称 真实姓名 手机号
     *
     * @param listener
     */
    public static void supplyInfo3(String openid , String nickname , String realname , String avatar, HttpRequestListener listener){
        try{
            String url=AppConfig.Edit_Info;
            HttpUtil http=new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list=new ArrayList<>();
            list.add(new Params("openid", openid));

            if (!TextUtils.isEmpty(nickname)){
                list.add(new Params("nickname", nickname));
            }

            if (!TextUtils.isEmpty(nickname)){
                list.add(new Params("nickname", nickname));
            }

            if (!TextUtils.isEmpty(realname)){
                list.add(new Params("realname", realname));
            }

            if (!TextUtils.isEmpty(avatar)){
                list.add(new Params("avatar", avatar));
            }
            http.sendPost(url,list);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
