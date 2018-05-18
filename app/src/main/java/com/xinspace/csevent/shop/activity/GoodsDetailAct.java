package com.xinspace.csevent.shop.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
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
import com.xinspace.csevent.login.activity.LoginActivity;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.shop.modle.CouponBean;
import com.xinspace.csevent.shop.modle.GoodsDetailBean;
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
 * 商城商品详情
 *
 * Created by Android on 2017/3/20.
 */

public class GoodsDetailAct extends BaseActivity{

    private LinearLayout ll_shop_detail_back;
    private String goodsId;
    private Intent intent;
    private ViewPager vp_goods_detail;
    private CircleIndicator circleIndicator;
    private int screenWidth;
    private GoodsDetailBean goodsDetail;

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

    private RelativeLayout rel_data_load;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 100:

                    rel_data_load.setVisibility(View.GONE);
                    goodsDetail = (GoodsDetailBean) msg.obj;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StatusBarUtils.setWindowStatusBarColor(GoodsDetailAct.this , R.color.app_bottom_color);

        setContentView(R.layout.act_goods_detail_web);
        preference = SDPreference.getInstance();
        intent = getIntent();
        if (intent != null){
            goodsId = intent.getStringExtra("goodId");
        }
        initData();
        initView();
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
        tv_goods_buy_num = (TextView) findViewById(R.id.tv_goods_buy_num);

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
    }

    private void scrollAd(){
        tv_goods_title.setText(goodsDetail.getTitle());
        tv_goods_price.setText("¥" + goodsDetail.getMarketprice());
        tv_goods_price2.setText( "原价" + goodsDetail.getProductprice());
        tv_goods_price2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tv_goods_buy_num.setText(goodsDetail.getSales() + "人付款");
//
        urlList = goodsDetail.getStringList();
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

        List<String> contentList = (List<String>) goodsDetail.getContent();
        if (contentList != null){
            for (int i = 0 ; i < contentList.size() ; i++){
                WebView webview = new WebView(this);
                lin_content.addView(webview);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) webview.getLayoutParams();
               // params.height = (int) (screenWidth * 1.0f);
                params.width = (int) (screenWidth * 1.0f);
                webview.setLayoutParams(params);
                webview.getSettings().setUseWideViewPort(true);
                webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
                webview.getSettings().setLoadWithOverviewMode(true);
                webview.setOverScrollMode(View.SCROLL_AXIS_NONE);
                //imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                //ImagerLoaderUtil.displayImage(contentList.get(i) , imageView);
                LogUtil.e("web img url\n" + contentList.get(i));
                webview.loadUrl(contentList.get(i));
            }
        }
//        web_img.loadUrl("http://shop.coresun.net/attachment/" + goodsDetail.getThumb());
    }

    View.OnClickListener onclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_shop_detail_back:
                    GoodsDetailAct.this.finish();
                    break;
                case R.id.tv_buy_goods:  //购买
                    if (userId != null && userId.equals("0")) {
                        ToastUtil.makeToast("请先登录");
                        Intent intent = new Intent(GoodsDetailAct.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        openid = preference.getContent("openid");
                        showBuyGoodsPop();
                    }
                    break;
                case R.id.rel_get_coupon:
                    showgetCouponPop();
                    break;
                case R.id.iv_goods_collect: //收藏



                    break;
                case R.id.tv_add_shopCart:    // 加入购物车
                    if (userId != null && userId.equals("0")) {
                        ToastUtil.makeToast("请先登录");
                        Intent intent = new Intent(GoodsDetailAct.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        openid = preference.getContent("openid");
                        showgoodsCartPop();
                    }
                    break;
                case R.id.iv_goods_shopCart:
                    if (userId != null && userId.equals("0")) {
                        ToastUtil.makeToast("请先登录");
                        Intent intent = new Intent(GoodsDetailAct.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(GoodsDetailAct.this, ShopCartAct2.class);
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
        GetActivityListBiz.getGoodsDetail( goodsId , new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                try {
                    if (result != null && !result.equals("")){
                        List<String> imgUrlList = new ArrayList<String>();
                        JSONObject jsonObject = new JSONObject(result);
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        Gson gson=new Gson();
                        GoodsDetailBean bean = gson.fromJson(jsonObject1.toString(), GoodsDetailBean.class);

                        JSONArray jsonArray = jsonObject1.getJSONArray("thumb_url");
                        for(int i = 0 ; i < jsonArray.length() ; i++){
                            imgUrlList.add(jsonArray.getString(i));
                        }
                        LogUtil.i("图片数组的长度" + imgUrlList.size());
                        bean.setStringList(imgUrlList);

                        List<String> contentList = new ArrayList<String>();
                        LogUtil.i("www什么东西" +jsonObject1.getString("content"));

                        if (!jsonObject1.getString("content").equals("null")){
                            JSONArray contentJsonArray = jsonObject1.getJSONArray("content");
                            for(int i = 0 ; i < contentJsonArray.length() ; i++){
                                contentList.add(contentJsonArray.getString(i));
                            }
                            bean.setContent(contentList);
                        }
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

                Log.i("www", "微信商城详情error" + error.toString());
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
            getCouponPop = new GetCouponPop(GoodsDetailAct.this , getCouponListener);
        }

        if (!getCouponPop.isShowing()) {
            getCouponPop.showAtLocation(rel_bottom, Gravity.BOTTOM, 0, 0);
            getCouponPop.isShowing();
        } else {
            getCouponPop.dismiss();
        }
    }


    /**
     * 买东西选择规格
     *
     */
    private void showBuyGoodsPop(){

        if (buyGoodsPop == null) {
            buyGoodsPop = new BuyGoodsPop(GoodsDetailAct.this , goodsDetail , openid , "");
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
            addShopCartPop = new AddShopCartPop(GoodsDetailAct.this , goodsDetail , openid);
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

    @Override
    protected void onDestroy() {
        if(timer != null){
            timer.cancel();
        }
        if(timeTask != null){
            timeTask.cancel();
        }
        timer = null;
        timeTask = null;
        onclickListener = null;

        if (getCouponPop != null){
            getCouponPop.onDestory();
        }
        getCouponPop = null;

        if (addShopCartPop != null){
            addShopCartPop.onDestory();
        }
        addShopCartPop = null;

        if (buyGoodsPop != null){
            buyGoodsPop.onDestory();
        }
        buyGoodsPop = null;

        handler.removeCallbacksAndMessages(null);
        ImagerLoaderUtil.clearImageMemory();
        this.setContentView(R.layout.empty_view);
        System.gc();
        super.onDestroy();
    }


}
