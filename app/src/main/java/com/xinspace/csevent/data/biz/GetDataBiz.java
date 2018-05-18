package com.xinspace.csevent.data.biz;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.xinspace.csevent.app.CoresunApp;
import com.xinspace.csevent.monitor.bean.AuditBean;
import com.xinspace.csevent.monitor.bean.LeaseScreenBean;
import com.xinspace.csevent.monitor.bean.SubmitRepairsBean;
import com.xinspace.csevent.app.utils.TimeHelper;
import com.xinspace.csevent.data.entity.GetAddressEntity;
import com.xinspace.csevent.data.entity.Params;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.shop.modle.GroupOrderBean;
import com.xinspace.csevent.shop.modle.PlaceOrder;
import com.xinspace.csevent.shop.modle.PlaceOrderBean;
import com.xinspace.csevent.shop.modle.RefundBean;
import com.xinspace.csevent.app.AppConfig;
import com.xinspace.csevent.util.HttpUtil;

import java.util.ArrayList;
import java.util.List;

import sdk_sample.sdk.bean.LockRecordBean;

/**
 * Created by Android on 2016/10/11.
 */
public class GetDataBiz {


    private static HttpUtil http;
    private static String url;

    /**
     *  金币全价购买
     */
    public static void getFullPriceBuy(Context context,String type,GetAddressEntity getAddressEntity ,String id,String match,HttpRequestListener listener){
        try {
            HttpUtil http=new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            String url= AppConfig.FULL_PRICE_BUY;
            List<Params> list=new ArrayList<>();
            list.add(new Params("activeid", id));
            list.add(new Params("uid", CoresunApp.USER_ID));
            list.add(new Params("match", match));
            list.add(new Params("buy_type", type));
            list.add(new Params("tel", getAddressEntity.getMobile()));
            list.add(new Params("name", getAddressEntity.getRealname()));
            list.add(new Params("address", getAddressEntity.getProvince() + getAddressEntity.getCity()
                    + getAddressEntity.getArea() + getAddressEntity.getAddress()));
            http.sendPost(url,list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     *
     * @param context
     * @param type
     * @param id
     * @param match
     * @param listener
     */
    public static void getCrowdBuy(Context context,String type,String id,String match,HttpRequestListener listener){

        String millisecond = TimeHelper.getDateString2(String.valueOf(System.currentTimeMillis()))+ "00";
        try {
            HttpUtil http=new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            String url= AppConfig.CROWD_PRICE_BUY;
            List<Params> list=new ArrayList<>();
            list.add(new Params("activeid", id));
            list.add(new Params("uid", CoresunApp.USER_ID));
            list.add(new Params("match", match));
            list.add(new Params("buy_type", type));
            list.add(new Params("millisecond", millisecond.replaceAll(":" , "")));
            list.add(new Params("shishicai_code" , "001"));
            http.sendPost(url,list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     *
     * 支付宝微信购买 获得统一下单单号
     *
     */
    public static void getFullPriceBuyWA(Context context,String cname ,String type,String amount,
                                         String active_id,String match,String code,String issue,String commodity_type,HttpRequestListener listener){

        String millisecond = TimeHelper.getDateString2(String.valueOf(System.currentTimeMillis()))+ "00";

        try {
            HttpUtil http=new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            String url= AppConfig.WA_ORDER_NUM;
            List<Params> list=new ArrayList<>();

            list.add(new Params("active_id", active_id));
            list.add(new Params("noactivity", issue));
            list.add(new Params("match", match));
            list.add(new Params("millisecond", millisecond.replaceAll(":" , "")));
            list.add(new Params("shishicai_code" , code));

            list.add(new Params("user_id", CoresunApp.USER_ID));
            list.add(new Params("cname", cname));
            list.add(new Params("kim", "1"));
            list.add(new Params("type", type));
            list.add(new Params("amount", amount));
            list.add(new Params("purchase_type", "1"));
            list.add(new Params("commodity_type", commodity_type));
            http.sendPost(url,list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void getFullPriceBuyWA2(Context context, String out_trade_no, HttpRequestListener listener){
        try {
            HttpUtil http=new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            String url= AppConfig.WA_ORDER_NUM2;
            List<Params> list=new ArrayList<>();
            list.add(new Params("user_id", CoresunApp.USER_ID));
            list.add(new Params("out_trade_no", out_trade_no));
            http.sendPost(url,list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     *   用户购买记录数据
     */

    public static void getUserBuyRecord(Context context, String type, int start , String length ,HttpRequestListener listener){
        try {
            HttpUtil http=new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            String url= AppConfig.BUY_RECORD;
            List<Params> list=new ArrayList<>();
            list.add(new Params("uid", CoresunApp.USER_ID));
            list.add(new Params("type", type));
            list.add(new Params("start", String.valueOf(start)));
            list.add(new Params("length", length));
            http.sendPost(url,list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getUserBuyRecord2( String openid, int page , String status ,HttpRequestListener listener){
        try {
            HttpUtil http=new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            String url= AppConfig.BUY_RECORD;
            List<Params> list=new ArrayList<>();
            list.add(new Params("openid", openid));
            list.add(new Params("page", String.valueOf(page)));
            list.add(new Params("status", status));
            http.sendPost(url,list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 积分兑换记录
     *
     * @param openid
     * @param page
     * @param status
     * @param listener
     */
    public static void getConvertRecord( String openid, int page , String status ,HttpRequestListener listener){
        try {
            HttpUtil http=new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            String url= AppConfig.CONVERT_RECORD;
            List<Params> list=new ArrayList<>();
            list.add(new Params("openid", openid));
            list.add(new Params("page", String.valueOf(page)));
            list.add(new Params("status", status));
            http.sendPost(url,list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    /**
     *   元宝兑换积分
     */
    public static void getGoldExIntegral(Context context, String goodNum ,HttpRequestListener listener){
        try {
            HttpUtil http=new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            String url= AppConfig.GoldExIntegral;
            List<Params> list=new ArrayList<>();
            list.add(new Params("uid", CoresunApp.USER_ID));
            list.add(new Params("gold", goodNum));
            http.sendPost(url,list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     *  众筹购买加入队列
     * @param context
     * @param actId
     * @param match
     * @param listener
     */
    public static void addCrowdQueue(Context context , String actId , String match ,HttpRequestListener listener){
        try {
            HttpUtil http=new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            String url= AppConfig.CrowdAddQueue;
            List<Params> list=new ArrayList<>();
            list.add(new Params("uid", CoresunApp.USER_ID));
            list.add(new Params("activeid", actId));
            list.add(new Params("match", match));
            http.sendPost(url,list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 众筹购买出队列
     * @param context
     * @param actId
     * @param match
     * @param listener
     */
    public static void crowdDeQueue(Context context, String type, String actId , String match , HttpRequestListener listener){
        try {
            HttpUtil http=new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            String url= AppConfig.CrowdDeQueue;
            List<Params> list=new ArrayList<>();
            list.add(new Params("uid", CoresunApp.USER_ID));
            list.add(new Params("activeid", actId));
            list.add(new Params("match", match));
            list.add(new Params("type" , type));
            http.sendPost(url,list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 众筹抽奖记录接口
     * @param context
     * @param start
     * @param length
     * @param listener
     */
    public static void crowdRecord(Context context, final int start , final String length , final HttpRequestListener listener){
        String url= AppConfig.CrowdRecord;
        Log.i("www" , "url   " + url);
        try {
            HttpUtil http=new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list=new ArrayList<>();
            list.add(new Params("uid", CoresunApp.USER_ID));
            list.add(new Params("is_Announced", "2"));
            list.add(new Params("start", String.valueOf(start)));
            list.add(new Params("length", length));
            http.sendPost2(url,list);
        } catch (Exception e) {
            e.printStackTrace();
        }

//
//        RequestQueue requestQueue = Volley.newRequestQueue(CoresunApp.context);
//        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            listener.onHttpRequestFinish(response.toString());
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                listener.onHttpRequestError(error.getMessage());
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() {
//                //在这里设置需要post的参数
//                Map<String, String> map = new HashMap<String, String>();
//                map.put("uid", CoresunApp.USER_ID);
//                map.put("is_Announced", "2");
//                map.put("start", String.valueOf(start));
//                map.put("length", length);
//
//                Log.i("www" , "uid" +  CoresunApp.USER_ID + "start" + start + "length" + length );
//
//                return map;
//            }
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//
//                HashMap<String,String> headers = new HashMap<String,String>();
//
//                headers.put("Accept","*/*");
//
//                headers.put("application/x-www-form-urlencoded; charset=%s", "utf-8");
//
//                return headers;
//            }
//        };
//        requestQueue.add(stringRequest);
    }

    /**
     * 众筹抽奖中奖纪录接口
     *
     * @param context
     * @param start
     * @param length
     * @param listener
     */
    public static void crowdWinRecord(Context context, int start , String length , final HttpRequestListener listener) {
        String url = AppConfig.CrowdWinRecord;
        Log.i("www", "url   " + url);
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
           // list.add(new Params("uid", CoresunApp.USER_ID));
            list.add(new Params("uid", "267"));
            list.add(new Params("type", "0"));
            list.add(new Params("start", String.valueOf(start)));
            list.add(new Params("length", length));
            http.sendPost(url, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void crowdPublish(Context context, String startTime, String endtime , final HttpRequestListener listener) {
        String url = AppConfig.CrowdPublish;
        Log.i("www", "url   " + url);
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            // list.add(new Params("uid", CoresunApp.USER_ID));
            list.add(new Params("start_time", startTime));
            list.add(new Params("end_time", endtime));
            http.sendPost2(url, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 众筹往期揭晓
     *
     * @param context
     * @param start
     * @param length
     * @param cid
     * @param listener
     */
    public static void crowdPublishActivities(Context context, int start, String length ,String cid ,final HttpRequestListener listener) {
        String url = AppConfig.CrowdActivitiesPublish;
        Log.i("www", "url   " + url);
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("cid", cid));
            list.add(new Params("start", String.valueOf(start)));
            list.add(new Params("length", length));
            http.sendPost(url, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     *
     * @param context
     * @param start
     * @param length
     * @param activeid
     * @param listener
     */
    public static void crowdPartRecord(Context context, int start, String length ,String activeid ,final HttpRequestListener listener) {
        String url = AppConfig.CrowdPartRecord;
        Log.i("www", "url   " + url);
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("activeid", activeid));
            list.add(new Params("start", String.valueOf(start)));
            list.add(new Params("length", length));
            http.sendPost(url, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 检测众筹有没有下期活动
     *
     * @param pid
     * @param listener
     */
    public static void crowdNextIssue( String pid ,final HttpRequestListener listener) {
        String url = AppConfig.CrowdNext;
        Log.i("www", "url   " + url);
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("pid", pid));
            http.sendPost(url, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断微信支付支付状态
     * @param aid
     * @param orderNum
     * @param listener
     */
    public static void checkCrowdPayState(String aid , String orderNum, final HttpRequestListener listener) {
        String url = AppConfig.checkCrowPayState;
        Log.i("www", "url   " + url);
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("aid", aid));
            list.add(new Params("uid", CoresunApp.USER_ID));
            list.add(new Params("number", orderNum));
            http.sendPost(url, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取晒单列表
     *
     * @param context
     * @param start
     * @param length
     * @param listener
     */
    public static void getBaskOrderList(Context context, String start, String length , final HttpRequestListener listener) {
        String url = AppConfig.baskOrderList;
        Log.i("www", "url   " + url);
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            // list.add(new Params("uid", CoresunApp.USER_ID));
            list.add(new Params("start", start));
            list.add(new Params("length", length));
            http.sendPost(url, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 我的获取晒单列表
     * @param context
     * @param start
     * @param length
     * @param listener
     */
    public static void getMyBaskOrderList(Context context, String start, String length , final HttpRequestListener listener) {
        String url = AppConfig.myBaskOrderList;
        Log.i("www", "url   " + url);
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("uid", CoresunApp.USER_ID));
            list.add(new Params("start", start));
            list.add(new Params("length", length));
            http.sendPost(url, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 发表晒单接口
     * @param productid
     * @param comment
     * @param title
     * @param type
     * @param cid
     * @param listener
     */
    public static void addOrderList(String productid, String comment ,String title, String type , String cid ,final HttpRequestListener listener) {
        String datetime = TimeHelper.getDateString(String.valueOf(System.currentTimeMillis()));
        String url = AppConfig.addTextContent;
        Log.i("www", "url   " + url);
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("uid", CoresunApp.USER_ID));
            list.add(new Params("productid", productid));
            list.add(new Params("comment", comment));
            list.add(new Params("title", title));
            list.add(new Params("type", type));
            list.add(new Params("cid", cid));
            list.add(new Params("datetime", datetime));
            http.sendPost(url, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 统计数据接口
     *
     * @param imei
     * @param time
     * @param startflag
     * @param duration
     * @param movie
     * @param movietime
     * @param air
     * @param smart
     * @param tagName
     * @param listener
     */
    public static void upstatisticsData(String imei , String time , String startflag, String duration ,
                                        String movie , String movietime , String air, String smart ,
                                        String tagName , final HttpRequestListener listener) {
        String url = AppConfig.statisticsApi;
        Log.i("www", "url   " + url);
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("imei", imei));
            list.add(new Params("time", time));
            list.add(new Params("startflag", startflag));
            list.add(new Params("duration", duration));
            list.add(new Params("movie", movie));
            list.add(new Params("movietime", movietime));
            list.add(new Params("air", air));
            list.add(new Params("smart", smart));
            list.add(new Params("tagName", tagName));
            http.sendPost(url, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 远程开门
     * @param listener
     * @param data
     */
    public static void longOpenDoor(String data ,  final HttpRequestListener listener) {
        String url = AppConfig.longOpenDoorAPI;
        Log.i("www", "url   " + url);
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("access_token", "b43d991e1ee06fbcf0dc1ed8b75e560d29a8530L59904e631568c6bd"));
            list.add(new Params("operation", "OPEN_DOOR"));
            list.add(new Params("data", data));
            http.sendPost(url, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void placeOrder(PlaceOrder placeOrder , final HttpRequestListener listener){
        String url = AppConfig.placeOrder;
        String userId = CoresunApp.USER_ID;
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("userid", userId));
            list.add(new Params("allprice", placeOrder.getAllprice()));
            list.add(new Params("provice", placeOrder.getProvice()));
            list.add(new Params("city", placeOrder.getCity()));
            list.add(new Params("area", placeOrder.getArea()));
            list.add(new Params("address", placeOrder.getAddress()));
            list.add(new Params("commodityid", placeOrder.getCommodityid()));
            list.add(new Params("branch", placeOrder.getBranch()));
            list.add(new Params("commodityname", placeOrder.getCommodityname()));
            http.sendPost4(url, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 商城下订单
     *
     *
     * @param listener
     */
    public static void placeOrder2(PlaceOrderBean placeOrder, HttpRequestListener listener){
        String url = AppConfig.placeOrder;
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);

            List<Params> list = new ArrayList<>();
            list.add(new Params("openid", placeOrder.getOpenid()));
            list.add(new Params("fromcart", placeOrder.getFromcart()));
            list.add(new Params("addressid", placeOrder.getAddressid()));
            list.add(new Params("goods", placeOrder.getGoods()));
            //String jsonStr = "{\"openid\":\"wap_user_1_18810199893\",\"fromcart\":\"0\",\"addressid\":\"140\",\"goods\":[{\\\"cates\\\":\\\"1175\\\",\\\"goodsid\\\":\\\"198\\\",\\\"marketprice\\\":\\\"9.90\\\",\\\"optionid\\\":\\\"395\\\",\\\"total\\\":\\\"1\\\"}]}";

            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 团购下单
     * @param bean
     * @param listener
     */
    public static void groupOrder(GroupOrderBean bean, HttpRequestListener listener){
        String url = AppConfig.groupOrder;
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("openid", bean.getOpenid()));
            list.add(new Params("id", bean.getId()));
            list.add(new Params("aid", bean.getAid()));
            list.add(new Params("type", bean.getType()));
            if (bean.getType().equals("groups")){
                if (bean.getTeamid() != null && !bean.getTeamid().equals("")){
                    list.add(new Params("teamid", bean.getTeamid()));
                }
            }
            list.add(new Params("realname", bean.getRealname()));
            list.add(new Params("mobile", bean.getMobile()));

            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 积分商城下单
     * @param bean
     * @param listener
     */
    public static void exChangeOrder(GroupOrderBean bean, HttpRequestListener listener){

        String url = AppConfig.exChangeOrder;
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("openid", bean.getOpenid()));
            list.add(new Params("id", bean.getId()));
            list.add(new Params("addressid", bean.getAid()));
            if (bean.getOptionid() != null && !bean.getOptionid().equals("")){
                list.add(new Params("optionid", bean.getOptionid()));
            }
            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     *
     *
     * @param orderId
     * @param openId
     * @param listener
     */
    public static void getOrderData(String orderId ,String openId, HttpRequestListener listener){
        String url = AppConfig.getOrderData;
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("id", orderId));
            list.add(new Params("openid", openId));
            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 订单详情
     *
     * @param orderId
     * @param openId
     */
    public static void getOrderDetailData(String orderId ,String openId , HttpRequestListener listener){
        String url = AppConfig.ORDER_DETAIL;
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("id", orderId));
            list.add(new Params("openid", openId));
            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消订单
     *
     * @param orderId
     * @param openId
     * @param listener
     */
    public static void cancleOrderDetailData(String orderId , String openId , HttpRequestListener listener){
        String url = AppConfig.CANCLE_ORDER;
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("id", orderId));
            list.add(new Params("openid", openId));
            list.add(new Params("remark", "就是不想买了"));
            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 删除订单
     *
     * @param orderId
     * @param openId
     * @param listener
     */
    public static void delOrderDetailData(String orderId , String openId , HttpRequestListener listener){
        String url = AppConfig.DEL_ORDER;
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("id", orderId));
            list.add(new Params("openid", openId));
            list.add(new Params("userdeleted", "2"));
            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 确认收货
     *
     */
    public static void confirmReceiptData(String orderId , String openId , HttpRequestListener listener){
        String url = AppConfig.CONFIRM_RECEIPT;
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("id", orderId));
            list.add(new Params("openid", openId));
            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 退款
     *
     * @param refundBean
     * @param listener
     */
    public static void afterSaleData(RefundBean refundBean , HttpRequestListener listener){
        String url = AppConfig.AFTER_SALE;
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();

            list.add(new Params("id", refundBean.getOrderId()));
            list.add(new Params("openid", refundBean.getOpenId()));
            list.add(new Params("rtype", refundBean.getRtype()));
            list.add(new Params("reason", refundBean.getReason()));
            list.add(new Params("price", refundBean.getPrice()));
            if (refundBean.getContent() != null  && !refundBean.getContent().equals("")){
                list.add(new Params("content", refundBean.getContent()));
            }
            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 取消退款
     * @param id
     * @param openid
     * @param listener
     */
    public static void cancleAfterSaleData(String id , String openid , HttpRequestListener listener){
        String url = AppConfig.CANCLE_AFTER_SALE;
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("id", id));
            list.add(new Params("openid", openid));
            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查看物流
     * @param id
     * @param openid
     * @param listener
     */
    public static void lookExpressData(String id , String openid , HttpRequestListener listener){

        String url = AppConfig.LOOK_EXPRESS;
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("id", id));
            list.add(new Params("openid", openid));
            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param id
     * @param openid
     * @param listener
     */
    public static void lookExpressData2(String id , String openid , HttpRequestListener listener){

        String url = AppConfig.LOOK_EXPRESS2;
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("id", id));
            list.add(new Params("openid", openid));
            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 积分商城的物流
     *
     * @param id
     * @param openid
     * @param listener
     */
    public static void lookExpressData3(String id , String openid , HttpRequestListener listener){

        String url = AppConfig.LOOK_EXPRESS3;
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("id", id));
            list.add(new Params("openid", openid));
            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 积分商城确认收货
     *
     * @param id
     * @param openid
     * @param listener
     */
    public static void JiFenShouHuo(String id , String openid , HttpRequestListener listener){

        String url = AppConfig.JIFENSHOUHUO;
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("orderid", id));
            list.add(new Params("openid", openid));
            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 报修
     * @param bean
     * @param listener
     */
    public static void submitRepairsData(String area, SubmitRepairsBean bean , HttpRequestListener listener){
        if (area.equals("gz")){
            url = AppConfig.GZ_COMMUNITY_BASE_URL + AppConfig.SUBMIT_REPAIRS;
        }else{
            url = AppConfig.COMMUNITY_BASE_URL + AppConfig.SUBMIT_REPAIRS;
        }

        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("uid", bean.getUid()));
            list.add(new Params("token", bean.getToken()));
            list.add(new Params("phone", bean.getPhone()));
            list.add(new Params("name", bean.getName()));
            list.add(new Params("describe", bean.getDescribe()));
            list.add(new Params("type", bean.getType()));
            list.add(new Params("pics", ""));
            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取报修记录
     *
     */
    public static void repairsListData(String area, String uid,String token,String page ,HttpRequestListener listener){
        if (area.equals("gz")){
            url = AppConfig.GZ_COMMUNITY_BASE_URL + AppConfig.REPAIRS_LIST;
        }else{
            url = AppConfig.COMMUNITY_BASE_URL + AppConfig.REPAIRS_LIST;
        }
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("uid", uid));
            list.add(new Params("token", token));
            list.add(new Params("page", page));
            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     *
     * 获取代缴费的种类
     *
     */
    public static void getPaymentData(String area, String uid,String token,String com_id,HttpRequestListener listener){
        if (area.equals("gz")){
            url = AppConfig.GZ_COMMUNITY_BASE_URL + AppConfig.PAYMENT;
        }else{
            url = AppConfig.COMMUNITY_BASE_URL + AppConfig.PAYMENT;
        }
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("uid", uid));
            list.add(new Params("token", token));
            list.add(new Params("com_id" , com_id));
            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 社区缴费项统一下单
     *
     * @param uid
     * @param token
     * @param id
     * @param listener
     */
    public static void getPayOrder(String area, String uid,String token,String id,HttpRequestListener listener){
        if (area.equals("gz")){
            url = AppConfig.GZ_COMMUNITY_BASE_URL + AppConfig.PAY_ORDER;
        }else{
            url = AppConfig.COMMUNITY_BASE_URL + AppConfig.PAY_ORDER;
        }
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("uid", uid));
            list.add(new Params("token", token));
            list.add(new Params("id" , id));
            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取最新公告数据
     *
     * @param uid
     * @param token
     * @param listener
     */
    public static void getNoticeData(String area, String uid , String token , HttpRequestListener listener){
        if (area.equals("gz")){
            url = AppConfig.GZ_COMMUNITY_BASE_URL + AppConfig.COMMUNITY_NOTICE;
        }else{
            url = AppConfig.COMMUNITY_BASE_URL + AppConfig.COMMUNITY_NOTICE;
        }
        try {
            http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("uid", uid));
            list.add(new Params("token", token));
            list.add(new Params("terminal" , "2"));
            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void cancelNoticeData(){
        http.cancelRequest();
    }


    /**
     * 获取公告列表
     * @param uid
     * @param token
     * @param page
     * @param listener
     */
    public static void getNoticeListData(String area, String uid , String token , String page ,HttpRequestListener listener){
        if (area.equals("gz")){
            url = AppConfig.GZ_COMMUNITY_BASE_URL + AppConfig.COMMUNITY_NOTICE_LIST;
        }else{
            url = AppConfig.COMMUNITY_BASE_URL + AppConfig.COMMUNITY_NOTICE_LIST;
        }

        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("uid", uid));
            list.add(new Params("token", token));
            list.add(new Params("terminal" , "2"));
            list.add(new Params("page" , page));
            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取缴费的列表
     *
     * @param uid
     * @param token
     * @param page
     * @param status
     * @param cate_id
     * @param listener
     */

    public static void getPaymentRecordData(String area, String uid , String token , String page , String status ,
                                            String cate_id, HttpRequestListener listener){

        if (area.equals("gz")){
            url = AppConfig.GZ_COMMUNITY_BASE_URL + AppConfig.COMMUNITY_PAYMENT_LIST;
        }else{
            url = AppConfig.COMMUNITY_BASE_URL + AppConfig.COMMUNITY_PAYMENT_LIST;
        }
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("uid", uid));
            list.add(new Params("token", token));
            list.add(new Params("page" , page));
            if (!status.equals("")){
                list.add(new Params("status" , status));
            }

            if (!cate_id.equals("")){
                list.add(new Params("cate_id" , cate_id));
            }

            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取社区缴费的种类
     *
     * @param uid
     * @param token
     * @param com_id
     * @param listener
     */
    public static void getPayTypeData(String area, String uid , String token , String com_id , HttpRequestListener listener){
        if (area.equals("gz")){
            url = AppConfig.GZ_COMMUNITY_BASE_URL + AppConfig.PAY_TYPE;
        }else{
            url = AppConfig.COMMUNITY_BASE_URL + AppConfig.PAY_TYPE;
        }
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("uid", uid));
            list.add(new Params("token", token));
            list.add(new Params("com_id" , com_id));
            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     *
     * 获取社区列表
     *
     * @param uid
     * @param token
     * @param listener
     */
    public static void getComListData(String area, String uid , String token ,HttpRequestListener listener){
        if (area.equals("gz")){
            url = AppConfig.GZ_COMMUNITY_BASE_URL + AppConfig.COM_LIST;
        }else{
            url = AppConfig.COMMUNITY_BASE_URL + AppConfig.COM_LIST;
        }
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("uid", uid));
            list.add(new Params("token", token));
            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取房间号列表
     *
     * @param uid
     * @param token
     * @param unit_id
     * @param listener
     */
    public static void getCodeListData(String area, String uid , String token , String unit_id,HttpRequestListener listener){
        if (area.equals("gz")){
            url = AppConfig.GZ_COMMUNITY_BASE_URL + AppConfig.CODE_LIST;
        }else{
            url = AppConfig.COMMUNITY_BASE_URL + AppConfig.CODE_LIST;
        }
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("uid", uid));
            list.add(new Params("token", token));
            list.add(new Params("unit_id" , unit_id));
            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 九块九
     *
     * @param listener
     */
    public static void getJiuListData(HttpRequestListener listener){
        String url = AppConfig.JIU_GOODS;
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 免费试用首页
     * @param openid
     * @param listener
     */
    public static void getFristTryData(String openid, HttpRequestListener listener){
        String url = AppConfig.FRIST_TRY;
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("openid", openid));
            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 申请免费试用
     *
     * @param openid
     * @param gid
     * @param listener
     */
    public static void applyTryData(String openid, String gid , HttpRequestListener listener){
        String url = AppConfig.APPLY_TRY;
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("openid", openid));
            list.add(new Params("gid", gid));
            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 我的试用列表
     * @param openid
     * @param success
     * @param listener
     */
    public static void myApplyListData(String openid, String success , HttpRequestListener listener){
        String url = AppConfig.MY_APPLY_LIST;
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("openid", openid));
            list.add(new Params("success", success));
            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 我的试用
     * @param openid
     * @param id
     * @param listener
     */
    public static void myApplyData(String openid , String id , HttpRequestListener listener){
        String url = AppConfig.MY_APPLY;
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("openid", openid));
            list.add(new Params("id", id));
            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 拼团
     *
     * @param listener
     */
    public static void getGroupListData(HttpRequestListener listener){
        String url = AppConfig.GROUP_LIST;
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param page
     * @param cateid
     * @param listener
     */
    public static void getGroupCateData( String page , String cateid , String search, HttpRequestListener listener){
        String url = AppConfig.GROUP_CATE_LIST;
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("page", String.valueOf(page)));

            if (!TextUtils.isEmpty(cateid)){
                list.add(new Params("cateid", cateid));
            }

            if (!TextUtils.isEmpty(search)){
                list.add(new Params("keyword", search));
            }
            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 提交社区审核信息
     *
     * @param listener
     */
    public static void submitCommunityData(String area, AuditBean auditBean , HttpRequestListener listener){
        if (area.equals("gz")){
            url = AppConfig.GZ_COMMUNITY_BASE_URL + AppConfig.COM_SUBMIT;
        }else{
            url = AppConfig.COMMUNITY_BASE_URL + AppConfig.COM_SUBMIT;
        }

        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();

            list.add(new Params("uid", auditBean.getUid()));
            list.add(new Params("token", auditBean.getToken()));
            list.add(new Params("name" , auditBean.getName()));
            list.add(new Params("type", auditBean.getType()));
            list.add(new Params("community_id", auditBean.getCommunity_id()));
            list.add(new Params("unit_id" , auditBean.getUnit_id()));
            list.add(new Params("property_id" , auditBean.getProperty_id()));

            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 秒杀时间
     *
     * @param listener
     */
    public static void seckillTimeData( HttpRequestListener listener){
        String url = AppConfig.SECKILL_TIME;
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param id
     * @param taskid
     * @param timeid
     * @param listener
     */
    public static void seckillListData(String id , String taskid , String timeid ,HttpRequestListener listener){

        String url = AppConfig.SECKILL_LIST;
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("id", id));
            list.add(new Params("taskid", taskid));
            list.add(new Params("timeid" , timeid));
            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取拼团商品详情
     * @param openid
     * @param id
     * @param listener
     */
    public static void getGroupDetail(String id ,String openid , HttpRequestListener listener){
        String url = AppConfig.GROUP_DETAIL;
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("id", id));
            list.add(new Params("openid", openid));
            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 判断商品是否可以购买 拼团 开团
     *
     * @param id
     * @param openid
     * @param listener
     */

    public static void getGroupIsBuy(String id ,String openid , HttpRequestListener listener){
        String url = AppConfig.GROUP_IS_BUY;
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("id", id));
            list.add(new Params("openid", openid));
            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     *
     * @param uid
     * @param token
     * @param listener
     */
    public static void getqueryAuditStatus(String area, String uid ,String token , HttpRequestListener listener){
        if (area.equals("gz")){
            url = AppConfig.GZ_COMMUNITY_BASE_URL + AppConfig.QUERY_AUDIT_STATUS;
        }else{
            url = AppConfig.COMMUNITY_BASE_URL + AppConfig.QUERY_AUDIT_STATUS;
        }
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("uid", uid));
            list.add(new Params("token", token));
            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取拼团中的数据
     *
     * @param openid
     * @param status
     * @param page
     * @param listener
     */
    public static void getGroupingData(String openid ,String status , String page,HttpRequestListener listener){
        String url = AppConfig.GROUPING_DATA;
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("openid", openid));
            list.add(new Params("status", status));
            list.add(new Params("page", page));
            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 试用的分类
     *
     * @param listener
     */
    public static void getTrialType(HttpRequestListener listener){
        String url = AppConfig.TRIAL_TYPE;
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取试用的列表
     *
     * @param listener
     */
    public static void getTrialList(int page , String cate , String openId , HttpRequestListener listener){
        String url = AppConfig.TRIAL_LIST;
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("page", String.valueOf(page)));
            list.add(new Params("cate", cate));
            list.add(new Params("openid", openId));
            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 积分兑换首页
     *
     * @param openId
     * @param listener
     */
    public static void getExChangetData(String openId, HttpRequestListener listener){
        String url = AppConfig.EXCHANGE_LIST;
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("openid", openId));
            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     *
     * @param openId
     * @param listener
     */
    public static void setSignData(String openId, HttpRequestListener listener){
        String url = AppConfig.SIGN_DATA;
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("openid", openId));
            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     *
     * @param keyword
     * @param cateid
     * @param page
     * @param listener
     */
    public static void getSearchData(String keyword , String cateid , String page , String openId ,HttpRequestListener listener){
        String url = AppConfig.SERACH_DATA;
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();

            if (keyword != null && !keyword.equals("")){
                list.add(new Params("keyword", keyword));
            }

            if (cateid != null && !cateid.equals("")){
                list.add(new Params("cateid", cateid));
            }

            list.add(new Params("page", page));
            list.add(new Params("openid", openId));

            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取门禁申请状态
     * @param area
     * @param uid
     * @param token
     * @param listener
     */
    public static void getAppCom(String area, String uid , String token ,HttpRequestListener listener){

        try {

            if (area.equals("gz")) {
                url = AppConfig.GZ_COMMUNITY_BASE_URL + AppConfig.APP_COM;
            }else{
                url = AppConfig.COMMUNITY_BASE_URL + AppConfig.APP_COM;
            }

            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();

            list.add(new Params("uid", uid));
            list.add(new Params("token", token));

            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 添加开门记录
     *
     * @param bean
     * @param listener
     */
    public static void ADD_LOCK_RECORD(String area, LockRecordBean bean , HttpRequestListener listener){

        if (area.equals("gz")) {
            url = AppConfig.GZ_COMMUNITY_BASE_URL + AppConfig.ADD_LOCK_RECORD;
        }else{
            url = AppConfig.COMMUNITY_BASE_URL + AppConfig.ADD_LOCK_RECORD;
        }
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("token", bean.getToken()));
            list.add(new Params("uid", bean.getUid()));
            list.add(new Params("phone", bean.getPhone()));
            list.add(new Params("equip_sn", bean.getEquip_sn()));
            list.add(new Params("type", bean.getType()));
            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取开门记录
     * @param bean
     * @param listener
     */
    public static void GET_LOCK_RECORD(String area, LockRecordBean bean , HttpRequestListener listener){
        if (area.equals("gz")) {
            url = AppConfig.GZ_COMMUNITY_BASE_URL + AppConfig.GET_LOCK_RECORD;
        }else{
            url = AppConfig.COMMUNITY_BASE_URL + AppConfig.GET_LOCK_RECORD;
        }
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("uid", bean.getUid()));
            list.add(new Params("token", bean.getToken()));
            if (!TextUtils.isEmpty(bean.getDataDay())){
                list.add(new Params("date", bean.getDataDay()));
            }
            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取商品分类
     *
     * @param listener
     */
    public static void GET_SHOP_TYPE( HttpRequestListener listener){
        String url = AppConfig.SHOP_TYPE;
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取视频通话界面的广告
     *
     */
    public static void GET_INCAll_VD_DATA(String area, HttpRequestListener listener){
        if (area.equals("gz")) {
            url = AppConfig.GZ_COMMUNITY_BASE_URL + AppConfig.GET_INCALL_VD_DATA;
        }else{
            url = AppConfig.COMMUNITY_BASE_URL + AppConfig.GET_INCALL_VD_DATA;
        }
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("terminal", "2"));
            list.add(new Params("display", "4"));
            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 房屋的列表数据
     *
     */
    public static void GET_LEASE_ROOM_DATA(String area, String uid , String token ,
                                           int page , LeaseScreenBean bean, HttpRequestListener listener){
        if (area.equals("gz")) {
            url = AppConfig.GZ_COMMUNITY_BASE_URL + AppConfig.GET_ROOM_DATA;
        }else{
            url = AppConfig.COMMUNITY_BASE_URL + AppConfig.GET_ROOM_DATA;
        }
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("uid", uid));
            list.add(new Params("token", token));
            list.add(new Params("page", String.valueOf(page)));
            list.add(new Params("city", bean.getCity()));

            if (!TextUtils.isEmpty(bean.getKey())){
                list.add(new Params("key", bean.getKey()));
            }

            if (!TextUtils.isEmpty(bean.getArea())){
                list.add(new Params("area", bean.getArea()));
            }

            if (!TextUtils.isEmpty(bean.getHouse_type())){
                list.add(new Params("house_type", bean.getHouse_type()));
            }

            if (!TextUtils.isEmpty(bean.getOrientations())){
                list.add(new Params("orientations", bean.getOrientations()));
            }

            if (!TextUtils.isEmpty(bean.getMinprice())){
                list.add(new Params("minprice", bean.getMinprice()));
            }

            if (!TextUtils.isEmpty(bean.getMaxprice())){
                list.add(new Params("maxprice", bean.getMaxprice()));
            }

            if (!TextUtils.isEmpty(bean.getRent_type())){
                list.add(new Params("rent_type", bean.getRent_type()));
            }

            if (!TextUtils.isEmpty(bean.getCharge_pay())){
                list.add(new Params("charge_pay", bean.getCharge_pay()));
            }

            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取城市数据
     *
     *
     */
    public static void GET_CITY_DATA(String area, String key ,HttpRequestListener listener){
        if (area.equals("gz")) {
            url = AppConfig.GZ_COMMUNITY_BASE_URL + AppConfig.GET_CITY_DATA;
        }else{
            url = AppConfig.COMMUNITY_BASE_URL + AppConfig.GET_CITY_DATA;
        }
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            if(!TextUtils.isEmpty(key)){
                list.add(new Params("key", key));
            }
            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void GET_CITY_AREA_DATA(String area, String key ,HttpRequestListener listener){
        if (area.equals("gz")) {
            url = AppConfig.GZ_COMMUNITY_BASE_URL + AppConfig.GET_CITY_AREA_DATA;
        }else{
            url = AppConfig.COMMUNITY_BASE_URL + AppConfig.GET_CITY_AREA_DATA;
        }
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            if(!TextUtils.isEmpty(key)){
                list.add(new Params("key", key));
            }
            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     *  提交申请的信息
     *
     */
    public static void SUBMIT_APPLY_DATA(String area, String uid , String token, String id , String mobile,
                                         String name, String appoint ,HttpRequestListener listener){
        if (area.equals("gz")) {
            url = AppConfig.GZ_COMMUNITY_BASE_URL + AppConfig.SUBMIY_APPLY_DATA;
        }else{
            url = AppConfig.COMMUNITY_BASE_URL + AppConfig.SUBMIY_APPLY_DATA;
        }
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            if(!TextUtils.isEmpty(uid)){
                list.add(new Params("uid", uid));
            }

            list.add(new Params("token", token));
            list.add(new Params("id", id));
            list.add(new Params("mobile", mobile));
            list.add(new Params("name", name));
            list.add(new Params("appoint", appoint));

            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void GET_APPLY_DATA(String area, String uid , String token, String type ,
                                      String page,HttpRequestListener listener){
        if (area.equals("gz")) {
            url = AppConfig.GZ_COMMUNITY_BASE_URL + AppConfig.GET_APPLY_DATA;
        }else{
            url = AppConfig.COMMUNITY_BASE_URL + AppConfig.GET_APPLY_DATA;
        }
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("uid", uid));
            list.add(new Params("token", token));
            list.add(new Params("type", type));
            list.add(new Params("page", page));
            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void GET_COMMUNITY_LIST(String area, String uid, String token, String unitId, HttpRequestListener listener){
        try {
            if (area.equals("gz")) {
                url = AppConfig.GZ_COMMUNITY_BASE_URL + AppConfig.GET_COMMUNITY_LIST;
            }else{
                url = AppConfig.COMMUNITY_BASE_URL + AppConfig.GET_COMMUNITY_LIST;
            }
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("uid", uid));
            list.add(new Params("token", token));
            list.add(new Params("unit_id", unitId));
            http.sendPost(url , list);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 普通订单获取支付宝订单方法
     * @param orderId
     * @param openId
     * @param listener
     */
    public static void getAlipayInfo(String orderId, String openId, HttpRequestListener listener){
        String url = AppConfig.SHOP_ALIPAY_INFO;
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("id", orderId));
            list.add(new Params("openid", openId));
            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 积分商城订单获取支付宝订单接口
     * @param orderId
     * @param openId
     * @param payType
     * @param listener
     */
    public static void getExChangePayInfo(String orderId, String openId, String payType, HttpRequestListener listener) {
        String url = AppConfig.SHOP_EXCHANGE_PAY_INFO;
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("id", orderId));
            list.add(new Params("openid", openId));
            list.add(new Params("paytype", payType));
            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getCommunityAlipayInfo(String area, String uid, String token, String id, HttpRequestListener listener) {
        if (area.equals("gz")) {
            url = AppConfig.GZ_COMMUNITY_BASE_URL + AppConfig.COMMUNITY_ALIPAY;
        }else{
            url = AppConfig.COMMUNITY_BASE_URL + AppConfig.COMMUNITY_ALIPAY;
        }
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("id", id));
            list.add(new Params("uid", uid));
            list.add(new Params("token", token));
            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getGroupAlipayOrderInfo(String teamId, String orderId, String openId, HttpRequestListener listener) {
        String url = AppConfig.GROUP_ALIPAY_INFO;
        try {
            HttpUtil http = new HttpUtil();
            http.setOnHttpRequestFinishListener(listener);
            List<Params> list = new ArrayList<>();
            list.add(new Params("teamid", teamId));
            list.add(new Params("orderid", orderId));
            list.add(new Params("openid", openId));
            http.sendPost(url , list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
