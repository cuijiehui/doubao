package com.xinspace.csevent.shop.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xinspace.csevent.R;
import com.xinspace.csevent.adapter.AdvViewpagerAdapter;
import com.xinspace.csevent.app.utils.ScreenUtils;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.biz.GetActivityListBiz;
import com.xinspace.csevent.data.entity.TimeLeftEntity;
import com.xinspace.csevent.login.activity.LoginActivity;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.shop.modle.CouponBean;
import com.xinspace.csevent.shop.modle.GoodsDetailBean;
import com.xinspace.csevent.shop.modle.ParamsBean;
import com.xinspace.csevent.shop.modle.SeckillDetailBean;
import com.xinspace.csevent.shop.weiget.AddShopCartPop;
import com.xinspace.csevent.shop.weiget.BuyGoodsPop;
import com.xinspace.csevent.shop.weiget.GetCouponListener;
import com.xinspace.csevent.shop.weiget.GetCouponPop;
import com.xinspace.csevent.util.ImagerLoaderUtil;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.StatusBarUtils;
import com.xinspace.csevent.util.ToastUtil;
import com.xinspace.csevent.ui.activity.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

/**
 * 秒杀商品详情
 *
 * Created by Android on 2017/3/20.
 */

public class SeckillDetailAct extends BaseActivity{

    private LinearLayout ll_shop_detail_back;
    private String goodsId;
    private Intent intent;
    private ViewPager vp_goods_detail;
    private CircleIndicator circleIndicator;
    private int screenWidth;
    private SeckillDetailBean goodsDetail;

    private boolean scroll_flag = true;//标记变量,用于判断是否自动滚动广告
    private static final int HANDLER_SCROLL_ADVERTISEMENT = 10;
    private Timer timer=new Timer();
    private boolean isFirst=true;//第一次设置indicator

    private TimerTask timeTask = new TimerTask() {
        @Override
        public void run() {
            handler.sendEmptyMessage(HANDLER_SCROLL_ADVERTISEMENT);
        }
    };

    private TextView tv_goods_title;
    private TextView tv_goods_price;
    private TextView tv_goods_price2;
    private TextView tv_goods_buy_num;
    private LinearLayout lin_content;
    private ImageView img_detail;
    private WebView web_img;

    private TextView tv_add_shopCart;
    private TextView tv_buy_goods;

    private RelativeLayout rel_get_coupon;

    private GetCouponPop getCouponPop;
    private RelativeLayout rel_bottom;
    private ImageView iv_goods_collect;
    private List<String>  urlList;

    private BuyGoodsPop buyGoodsPop;
    private SDPreference preference;
    private String userId;
    private String openid;
    private AddShopCartPop addShopCartPop;
    private ImageView iv_goods_shopCart;

    private String timeId;
    private String taskid;

    private TextView tv_time_state;
    private TextView tv_seckill_time;
    private Timer timer1;
    private int isdiscountTime;
    private LinearLayout lin_goods_params;
    private GoodsDetailBean goodsBean;
    private int status;
    private RelativeLayout rel_data_load;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 100:

                    rel_data_load.setVisibility(View.GONE);
                    if (msg.obj != null){
                        goodsDetail = (SeckillDetailBean) msg.obj;
                        goodsBean.setId(goodsDetail.getGoodsid());
                        goodsBean.setMarketprice(goodsDetail.getPrice());
                        goodsBean.setProductprice(goodsDetail.getMarketprice());
                        goodsBean.setTitle(goodsDetail.getTitle());
                        goodsBean.setThumb(goodsDetail.getThumb());
                        goodsBean.setCcate(goodsDetail.getCates());
                    }
                    scrollAd();
                    break;
                case 101:
                    LogUtil.i("数据获取失败");
                    rel_data_load.setVisibility(View.GONE);
                    break;
                case HANDLER_SCROLL_ADVERTISEMENT:
                    scrollAdv();
                    break;
            }
        }
    };
    // http://shop.coresun.net/weixin/arc_pr.html?pid=722

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(SeckillDetailAct.this , R.color.app_bottom_color);
        setContentView(R.layout.act_seckill_detail);
        preference = SDPreference.getInstance();

        intent = getIntent();

        if (intent != null){
            goodsId = intent.getStringExtra("goodsId");
            timeId = intent.getStringExtra("timeId");
            taskid = intent.getStringExtra("taskid");
        }
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        userId = preference.getContent("userId");
    }

    private void initView() {
        ll_shop_detail_back = (LinearLayout) findViewById(R.id.ll_shop_detail_back);
        ll_shop_detail_back.setOnClickListener(onclickListener);

        rel_data_load = (RelativeLayout) findViewById(R.id.rel_data_load);

        goodsBean = new GoodsDetailBean();
        screenWidth = ScreenUtils.getScreenWidth(this);

        vp_goods_detail = (ViewPager) findViewById(R.id.vp_goods_detail);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) vp_goods_detail.getLayoutParams();
        params.height = (int) (screenWidth * 1.0f);
        params.width = (int) (screenWidth * 1.0f);
        vp_goods_detail.setLayoutParams(params);

        circleIndicator = (CircleIndicator) findViewById(R.id.indicator_goods_detail);

        tv_goods_title = (TextView) findViewById(R.id.tv_goods_title);
        tv_goods_price = (TextView) findViewById(R.id.tv_goods_price);
        tv_goods_price2 = (TextView) findViewById(R.id.tv_goods_price2);

        lin_content = (LinearLayout) findViewById(R.id.lin_content);
        img_detail = (ImageView) findViewById(R.id.img_detail);
        web_img = (WebView) findViewById(R.id.web_img);

        web_img.getSettings().setUseWideViewPort(true);
        web_img.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        web_img.getSettings().setLoadWithOverviewMode(true);
        web_img.setOverScrollMode(View.SCROLL_AXIS_NONE);

        tv_add_shopCart = (TextView) findViewById(R.id.tv_add_shopCart); //加入购物车
        tv_add_shopCart.setOnClickListener(onclickListener);
        tv_buy_goods = (TextView) findViewById(R.id.tv_buy_goods);   //购买
        tv_buy_goods.setOnClickListener(onclickListener);

        rel_get_coupon = (RelativeLayout) findViewById(R.id.rel_get_coupon);
        rel_get_coupon.setOnClickListener(onclickListener);

        rel_bottom = (RelativeLayout) findViewById(R.id.rel_bottom);
        iv_goods_collect = (ImageView) findViewById(R.id.iv_goods_collect);
        iv_goods_collect.setOnClickListener(onclickListener);

        iv_goods_shopCart = (ImageView) findViewById(R.id.iv_goods_shopCart);
        iv_goods_shopCart.setOnClickListener(onclickListener);

        tv_time_state = (TextView) findViewById(R.id.tv_time_state);
        tv_seckill_time = (TextView) findViewById(R.id.tv_seckill_time);

        lin_goods_params = (LinearLayout) findViewById(R.id.lin_goods_params);
    }

    private void scrollAd(){
        tv_goods_title.setText(goodsDetail.getTitle());
        tv_goods_price.setText("秒杀价¥" + goodsDetail.getPrice());
        tv_goods_price2.setText( "原价" + goodsDetail.getMarketprice());
        tv_goods_price2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        status = goodsDetail.getStatus();
        LogUtil.i("status" + status);

        if (status == 1){
            tv_time_state.setText("距开始还有");
            isdiscountTime = Integer.valueOf(goodsDetail.getResttime());
            tv_buy_goods.setBackgroundResource(R.drawable.trial_tv_shape);

        }else if(status == 0){
            tv_time_state.setText("距结束还有");
            isdiscountTime = Integer.valueOf(goodsDetail.getResttime());
            //isdiscountTime = 5;
            tv_buy_goods.setBackgroundResource(R.drawable.selector_bt_state_change);
        }

        if (goodsDetail.getParamsBeanList().size() != 0){
            List<ParamsBean> listBean = goodsDetail.getParamsBeanList();
            for (int i = 0 ; i < listBean.size() ; i++){
                View view = LayoutInflater.from(this).inflate(R.layout.item_params ,null);
                TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
                TextView tv_value = (TextView) view.findViewById(R.id.tv_value);
                tv_title.setText(listBean.get(i).getTitle());
                tv_value.setText(listBean.get(i).getValue());
                lin_goods_params.addView(view);
            }
        }

        startTask(status);
//
        urlList = goodsDetail.getImgUrlList();
        List<ImageView> list = new ArrayList<>();
        for (int i = 0; i < urlList.size(); i++) {
            String url = urlList.get(i);
            //显示广告图片
            final ImageView image = new ImageView(this);
            image.setScaleType(ImageView.ScaleType.FIT_XY);
            ImagerLoaderUtil.displayImageWithLoadingIcon(url, image , R.drawable.icon_detail_load);
            list.add(image);
        }
        AdvViewpagerAdapter adapter = new AdvViewpagerAdapter(list);
        vp_goods_detail.setAdapter(adapter);
        if(isFirst){
            isFirst=false;
            circleIndicator.setViewPager(vp_goods_detail);
            //滚动广告
            timer.schedule(timeTask,3000,3000);
        }

        String html = goodsDetail.getContent();
        if (html.contains("src=\"/p")) {
            html = html.replace("src=\"/p", "src=\"http://car.zhuji.net/p");
        }
        web_img.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
    }


    View.OnClickListener onclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_shop_detail_back:
                    SeckillDetailAct.this.finish();
                    break;
                case R.id.tv_buy_goods:  //购买
                    if (userId != null && userId.equals("0")) {
                        ToastUtil.makeToast("请先登录");
                        Intent intent = new Intent(SeckillDetailAct.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        if (status == 1){
                            ToastUtil.makeToast("抢购已结束");
                        }else if(status == 0){
                            openid = preference.getContent("openid");
                            showBuyGoodsPop();
                        }else if (status == -1){
                            ToastUtil.makeToast("未开始");
                        }
                    }
                    break;
                case R.id.rel_get_coupon:
                    showgetCouponPop();
                    break;
                case R.id.iv_goods_collect: //收藏


                    break;
                case R.id.tv_add_shopCart:
                    if (userId != null && userId.equals("0")) {
                        ToastUtil.makeToast("请先登录");
                        Intent intent = new Intent(SeckillDetailAct.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        openid = preference.getContent("openid");
                        showgoodsCartPop();
                    }
                    break;
                case R.id.iv_goods_shopCart:
                    if (userId != null && userId.equals("0")) {
                        ToastUtil.makeToast("请先登录");
                        Intent intent = new Intent(SeckillDetailAct.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(SeckillDetailAct.this, ShopCartAct.class);
                        startActivity(intent);
                    }
                    break;
            }
        }
    };

    //设置广告页
    private void scrollAdv() {
        if(scroll_flag){
            int currentIndex = vp_goods_detail.getCurrentItem();
            if(currentIndex == urlList.size() - 1){
                currentIndex=0;
            }else{
                currentIndex+=1;
            }
            vp_goods_detail.setCurrentItem(currentIndex,false);
        }
    }

    private void initData() {

        rel_data_load.setVisibility(View.VISIBLE);

        GetActivityListBiz.getSeckillDetail( goodsId , taskid , timeId , new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                try {
                    if (result != null && !result.equals("")){
                        LogUtil.i("秒杀详情数据" + result);
                        List<String> imgUrlList = new ArrayList<String>();

                        JSONObject jsonObject = new JSONObject(result);
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        JSONObject goodsObject = jsonObject1.getJSONObject("goods");

                        Gson gson=new Gson();
                        SeckillDetailBean bean = gson.fromJson(goodsObject.toString(), SeckillDetailBean.class);

                        bean.setStatus(jsonObject1.getInt("status"));
                        bean.setStarttime(jsonObject1.getInt("starttime"));
                        bean.setEndtime(jsonObject1.getInt("endtime"));
                        bean.setResttime(jsonObject1.getInt("resttime"));

                        //轮播图
                        JSONArray jsonArray = jsonObject1.getJSONArray("advs");
                        for(int i = 0 ; i < jsonArray.length() ; i++){
                            imgUrlList.add(jsonArray.getString(i));
                        }
                        LogUtil.i("图片数组的长度" + imgUrlList.size());
                        bean.setImgUrlList(imgUrlList);

                        //参数
                        JSONArray paramsJsonArray = jsonObject1.getJSONArray("params");
                        List<ParamsBean> paramsBeanList = new ArrayList<ParamsBean>();
                        for(int j = 0 ; j < paramsJsonArray.length() ; j++){
                            ParamsBean paramsBean = gson.fromJson(paramsJsonArray.getJSONObject(j).toString() , ParamsBean.class);
                            paramsBeanList.add(paramsBean);
                        }
                        bean.setParamsBeanList(paramsBeanList);
                        handler.obtainMessage(100 , bean).sendToTarget();
                    }else{
                        handler.obtainMessage(101).sendToTarget();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onHttpRequestError(String error) {

                //Log.i("www", "微信商城详情error" + error.toString());
                handler.obtainMessage(101).sendToTarget();

            }
        });
    }

    /**
     * 获取优惠卷弹窗
     *
     */
    private void showgetCouponPop(){
        if (getCouponPop == null) {
            getCouponPop = new GetCouponPop(SeckillDetailAct.this , getCouponListener);
        }

        if (!getCouponPop.isShowing()) {
            getCouponPop.showAtLocation(rel_bottom, Gravity.BOTTOM, 0, 0);
            getCouponPop.isShowing();
        } else {
            getCouponPop.dismiss();
        }
    }


    /**
     * 买东西选择规格pop
     *
     */
    private void showBuyGoodsPop(){

        if (buyGoodsPop == null) {
            buyGoodsPop = new BuyGoodsPop(SeckillDetailAct.this , goodsBean , openid , "seckill");
            buyGoodsPop.setOnDismissListener(dismissListener);
        }

        if (!buyGoodsPop.isShowing()) {
            buyGoodsPop.showAtLocation(rel_bottom, Gravity.BOTTOM, 0, 0);
            buyGoodsPop.isShowing();
        } else {
            buyGoodsPop.dismiss();
        }
    }


    PopupWindow.OnDismissListener dismissListener = new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
            buyGoodsPop = null;
        }
    };

    /**
     * 加入购物车
     *
     */
    private void showgoodsCartPop(){

        if (addShopCartPop == null) {
            addShopCartPop = new AddShopCartPop(SeckillDetailAct.this , goodsBean , openid);
        }

        if (!addShopCartPop.isShowing()) {
            addShopCartPop.showAtLocation(rel_bottom, Gravity.BOTTOM, 0, 0);
            addShopCartPop.isShowing();
        } else {
            addShopCartPop.dismiss();
            buyGoodsPop = null;
        }
    }

    GetCouponListener getCouponListener = new GetCouponListener() {
        @Override
        public void sendCoupon(CouponBean bean) {
           // LogUtil.i("点击选择优惠卷了");
        }
    };


    //更新时间 timerTask
    private void startTask(final int state){

        timer1 = new Timer();
        TimerTask timeTask1 = new TimerTask() {
            @Override
            public void run() {
                if (isdiscountTime > 0) {
                    isdiscountTime -= 1;
                    //主线程中更新UI
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateTime(isdiscountTime , state);
                        }
                    });
                }else if(isdiscountTime == 0){
                    timer1.cancel();
                }
            }
        };
        timer1.schedule(timeTask1, 0, 1000);
    }


    private void updateTime(long time , int state){

        TimeLeftEntity timeEnty = new TimeLeftEntity((int) time);
        String day = timeEnty.getDays();
        String hours = timeEnty.getHoursWithDay();
        String mins = timeEnty.getMinWithDay();
        String sec = timeEnty.getSecondsWithDay();

        if (hours.length() == 1 ){
            hours = "0" + hours;
        }

        if (mins.length() == 1 ){
            mins = "0" + mins;
        }

        if (sec.length() == 1 ){
            sec = "0" + sec;
        }

        tv_seckill_time.setText(hours +":" + mins + ":" + sec);
        if (time == 0){
            if (state == 0){
                status = 1;
                tv_seckill_time.setText("00:00:00");
                tv_time_state.setText("本场已结束");
                tv_buy_goods.setBackgroundResource(R.drawable.trial_tv_shape);
            }else if (state == 1){
                tv_buy_goods.setBackgroundResource(R.drawable.selector_bt_state_change);
                status = 0;
            }else if (state == -1){
                tv_buy_goods.setBackgroundResource(R.drawable.selector_bt_state_change);
                status = 0;
                tv_time_state.setText("本场已开始");
            }
        }
//        LogUtil.i("day:" + day);
//        LogUtil.i("hour:" + hours);
//        LogUtil.i("min:" + mins);
//        LogUtil.i("sec:" + sec);
    }


    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);

        if (timer1 != null){
            timer1.cancel();
        }
        if (timer != null){
            timer.cancel();
        }
        if (timeTask != null){
            timeTask.cancel();
        }

        timer1 = null;
        timer = null;
        timeTask = null;
        ImagerLoaderUtil.clearImageMemory();
        SeckillDetailAct.this.finish();
        this.setContentView(R.layout.empty_view);
        System.gc();
        super.onDestroy();
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        timer = null;
//        handler.removeCallbacksAndMessages(null);
//    }



}
