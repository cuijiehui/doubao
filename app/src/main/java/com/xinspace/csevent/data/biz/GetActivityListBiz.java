package com.xinspace.csevent.data.biz;

import android.text.TextUtils;
import android.util.Log;

import com.xinspace.csevent.data.entity.Params;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.shop.modle.ScreenGoodsBean;
import com.xinspace.csevent.app.AppConfig;
import com.xinspace.csevent.util.HttpUtil;
import com.xinspace.csevent.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取活动列表业务
 */
public class GetActivityListBiz {
    /**
     * @param start 起始位置
     * @param length 请求条数
     * @param pro 省
     * @param city 市
     * @param district 区
     */
    public static void getActivityByIndex(String start, String length, String pro, String city, String district, HttpRequestListener listener){
        try {
            HttpUtil http= new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            String url= AppConfig.ACTIVITY_LIST_URL;
            List<Params> list=new ArrayList<>();
            list.add(new Params("province",pro));
            list.add(new Params("city",city));
            list.add(new Params("area",district));
            list.add(new Params("start",start));
            list.add(new Params("length",length));
            http.sendPost(url,list);
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.i("获取活动列表异常"+e.getMessage());
        }
    }


    /**
     * @param start 起始位置
     * @param length 请求数据条数
     * @param pro 省
     * @param city 市
     * @param district 区
     */
    public static void getActivityByType(String start, String length, String pro, String city, String district, HttpRequestListener listener){
        try {
            HttpUtil http= new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            String url= AppConfig.ACTIVITY_LIST_URL;
            List<Params> list=new ArrayList<>();
            list.add(new Params("province",pro));
            list.add(new Params("city",city));
            list.add(new Params("area",district));
            list.add(new Params("start",start));
            list.add(new Params("length",length));
            list.add(new Params("type","4"));//抽奖池活动列表

            http.sendPost(url,list);
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.i("获取活动列表异常"+e.getMessage());
        }
    }

    public static void getCrowdDataList(String start, String length, HttpRequestListener listener){
        try {
            HttpUtil http= new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            String url= AppConfig.CROWD_LIST_URL;
            List<Params> list=new ArrayList<>();
            list.add(new Params("start",start));
            list.add(new Params("length",length));
            http.sendPost(url,list);
        }catch (Exception e){
            e.printStackTrace();
            Log.i("www" , "获取众筹活动列表异常"+e.getMessage());
        }
    }

    /**
     * 微信商城
     * @param page
     * @param listener
     */
    public static void getGoodsList(String page , HttpRequestListener listener){
        try {
            HttpUtil http= new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            String url= AppConfig.WECHAT_GOODS;
            List<Params> list=new ArrayList<>();
            list.add(new Params("page",page));
            list.add(new Params("type","mobile"));
            list.add(new Params("frommyshop","0"));
            http.sendPost(url , list);
        }catch (Exception e){
            e.printStackTrace();
            Log.i("www" , "首页数据异常"+e.getMessage());
        }
    }


    /**
     * app首页商品
     *
     * @param page
     * @param listener
     */
    public static void getFristGoodsList(String page , HttpRequestListener listener){
        try {
            HttpUtil http= new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            String url= AppConfig.FRIST_GOODS;
            List<Params> list=new ArrayList<>();
            http.sendPost(url , list);
        }catch (Exception e){
            e.printStackTrace();
            Log.i("www" , "首页数据异常"+e.getMessage());
        }
    }


    /**
     * 微信商城 分类获取商品数据
     * @param page
     * @param listener
     */
    public static void getGoodsList2(String page,String cateId ,HttpRequestListener listener){
        try {
            HttpUtil http= new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            String url= AppConfig.WECHAT_GOODS;
            List<Params> list=new ArrayList<>();
            list.add(new Params("page",page));
            list.add(new Params("type","mobile"));
            list.add(new Params("frommyshop","0"));
            list.add(new Params("cate" , cateId));
            http.sendPost(url , list);
        }catch (Exception e){
            e.printStackTrace();
            Log.i("www" , "首页数据异常"+e.getMessage());
        }
    }


    /**
     * 搜索界面的商品查询
     *
     *
     */
    public static void getGoodsList3(String page, ScreenGoodsBean bean, HttpRequestListener listener){
        try {
            HttpUtil http= new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            String url= AppConfig.WECHAT_GOODS;
            List<Params> list=new ArrayList<>();
            list.add(new Params("type","mobile"));
            list.add(new Params("frommyshop","0"));
            list.add(new Params("page",page));


            String cateId = bean.getCate();
            if (!TextUtils.isEmpty(cateId)){
                list.add(new Params("cate" , cateId));
            }

            String searchKey = bean.getKeywords();
            if (!TextUtils.isEmpty(searchKey)){
                list.add(new Params("keywords" , searchKey));
            }

            if (!TextUtils.isEmpty(bean.getIsnew())){
                list.add(new Params("isnew" , bean.getIsnew()));
            }

            if (!TextUtils.isEmpty(bean.getIshot())){
                list.add(new Params("ishot" , bean.getIshot()));
            }

            if (!TextUtils.isEmpty(bean.getIsrecommand())){
                list.add(new Params("isrecommand" , bean.getIsrecommand()));
            }

            if (!TextUtils.isEmpty(bean.getIstime())){
                list.add(new Params("istime" , bean.getIstime()));
            }

            if (!TextUtils.isEmpty(bean.getIssendfree())){
                list.add(new Params("issendfree" ,bean.getIssendfree()));
            }

            if (!TextUtils.isEmpty(bean.getOrder())){
                list.add(new Params("order" , bean.getOrder()));
            }

            if (!TextUtils.isEmpty(bean.getBy())){
                list.add(new Params("by" , bean.getBy()));
            }

            http.sendPost(url , list);
        }catch (Exception e){
            e.printStackTrace();
            Log.i("www" , "首页数据异常"+e.getMessage());
        }
    }

    /**
     * 获取商品详情
     *
     * @param id
     * @param listener
     */
    public static void getGoodsDetail(String id, HttpRequestListener listener){
        try {
            HttpUtil http= new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            String url= AppConfig.goodsDetail;
            List<Params> list=new ArrayList<>();
            list.add(new Params("id",id));
            http.sendPost(url,list);
        }catch (Exception e){
            e.printStackTrace();
            Log.i("www" , "商品详情"+e.getMessage());
        }
    }


    public static void getGoodClass(String type, HttpRequestListener listener){
        try {
            HttpUtil http= new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            String url= "http://shop.coresun.net/user/Commodityapi/class_list?cls=" + type;
            List<Params> list = new ArrayList<>();
            http.sendGet2(url);
        }catch (Exception e){
            e.printStackTrace();
            Log.i("www" , "首页数据异常"+e.getMessage());
        }
    }

    /**
     * 获取商品规格
     *
     * @param id
     * @param openid
     * @param listener
     */
    public static void getGoodsStandard(String id, String openid, HttpRequestListener listener){
        try {
            HttpUtil http= new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            String url= AppConfig.goodsSpec;
            List<Params> list=new ArrayList<>();
            list.add(new Params("id",id));
            list.add(new Params("openid",openid));
            http.sendPost(url,list);
        }catch (Exception e){
            e.printStackTrace();
            Log.i("www" , "商品详情"+e.getMessage());
        }
    }

    /**
     * 积分商城商品规格
     *
     * @param id
     * @param openid
     * @param listener
     */
    public static void getIntegralStandard(String id, String openid, HttpRequestListener listener){
        try {
            HttpUtil http= new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            String url= AppConfig.IntegralStandard;
            List<Params> list=new ArrayList<>();
            list.add(new Params("id",id));
            //list.add(new Params("openid",openid));
            http.sendPost(url,list);
        }catch (Exception e){
            e.printStackTrace();
            Log.i("www" , "商品详情"+e.getMessage());
        }
    }




    /**
     * 获取商品的大分类
     *
     * @param listener
     */
    public static void getGoodsClass(HttpRequestListener listener){
        try {
            HttpUtil http= new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            String url= AppConfig.goodsClass;
            List<Params> list=new ArrayList<>();
            http.sendPost(url,list);
        }catch (Exception e){
            e.printStackTrace();
            Log.i("www" , "商品分类"+e.getMessage());
        }
    }


    /**
     *
     * @param listener
     * @param id            商品id
     * @param openid    	用户openid
     * @param optionid      规格id
     * @param total         数量
     */
    public static void addCartData(String id , String openid , String optionid , String total , HttpRequestListener listener){
        try {
            HttpUtil http= new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            String url= AppConfig.addCart;
            List<Params> list=new ArrayList<>();
            list.add(new Params("id",id));
            list.add(new Params("openid",openid));
            list.add(new Params("optionid",optionid));
            list.add(new Params("total",total));
            http.sendPost(url,list);
        }catch (Exception e){
            e.printStackTrace();
            Log.i("www" , "加入购物车"+e.getMessage());
        }
    }


    /**
     * 获取购物车列表
     *
     * @param openid
     * @param listener
     */
    public static void getShopCartData(String openid, HttpRequestListener listener){
        try {
            HttpUtil http= new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            String url= AppConfig.getShopCart;
            List<Params> list=new ArrayList<>();
            list.add(new Params("openid",openid));
            http.sendPost(url,list);
        }catch (Exception e){
            e.printStackTrace();
            Log.i("www" , "获取购物车列表" + e.getMessage());
        }
    }

    /**
     * 移除购物车
     * @param openid
     * @param id
     * @param listener
     */
    public static void removeShopCartData(String openid , String id , HttpRequestListener listener){
        try {
            HttpUtil http= new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            String url= AppConfig.REMOVE_SHOPCART_URL;
            List<Params> list=new ArrayList<>();
            list.add(new Params("openid" , openid));
            list.add(new Params("ids" , id));
            http.sendPost(url , list);
        }catch (Exception e){
            e.printStackTrace();
            Log.i("www" , "获取购物车列表" + e.getMessage());
        }
    }

    /**
     * 获取秒杀商品详情
     *
     * @param id
     * @param listener
     */
    public static void getSeckillDetail(String id,String taskid, String timeid , HttpRequestListener listener){
        try {
            HttpUtil http= new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            String url= AppConfig.SECKILL_DETAIL;
            List<Params> list=new ArrayList<>();
            list.add(new Params("id",id));
            list.add(new Params("taskid",taskid));
            list.add(new Params("timeid",timeid));
            http.sendPost(url,list);
        }catch (Exception e){
            e.printStackTrace();
            Log.i("www" , "商品详情"+e.getMessage());
        }
    }


}
