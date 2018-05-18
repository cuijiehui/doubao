package com.xinspace.csevent.login.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xinspace.csevent.monitor.view.MyListView;
import com.xinspace.csevent.R;
import com.xinspace.csevent.adapter.AdvViewpagerAdapter;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.biz.GetDataBiz;
import com.xinspace.csevent.data.biz.GetProfileBiz;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.shop.activity.ConvertRecordAct;
import com.xinspace.csevent.shop.activity.ExChangeDetailAct;
import com.xinspace.csevent.shop.activity.SearchMoreAct;
import com.xinspace.csevent.shop.activity.ShopCartAct2;
import com.xinspace.csevent.shop.adapter.ExGoodsAdapter;
import com.xinspace.csevent.shop.modle.BannerBean2;
import com.xinspace.csevent.shop.modle.ExGoodsBean;
import com.xinspace.csevent.shop.weiget.SignInPop;
import com.xinspace.csevent.util.ImagerLoaderUtil;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.StatusBarUtils;
import com.xinspace.csevent.util.ToastUtil;
import com.xinspace.csevent.ui.activity.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by Android on 2016/10/13.
 * 积分兑换
 */
public class ExChangeAct extends BaseActivity{

    private LinearLayout ll_back;
    private TextView tv_my_integral;
    private LinearLayout lin_sign_in_record;
    private LinearLayout lin_sign_in;

    private RelativeLayout rel_zero_more;
    private RelativeLayout rel_optimize_more;

    private SDPreference preference;
    private String openId;
    private ViewPager vp_exchange;

    private List<ImageView> adv_list;//广告fragment集合
    private CircleIndicator indicator;//指示器
    //重新获取地址的请求码
    private int REQUEST_CODE_RE_GET_ADDRESS = 200;
    //广告滚动
    private static final int HANDLER_SCROLL_ADVERTISEMENT = 10;
    private boolean scroll_flag = true;//标记变量,用于判断是否自动滚动广告
    private Timer timer = new Timer();
    private boolean isFirst = true; //第一次设置indicator
    private TimerTask timeTask = new TimerTask() {
        @Override
        public void run() {
            handler.sendEmptyMessage(HANDLER_SCROLL_ADVERTISEMENT);
        }
    };
    private MyListView lv_zero;
    private MyListView lv_goods;
    private ExGoodsAdapter adapter;
    private ExGoodsAdapter adapter2;

    private List<ExGoodsBean>  allGoodList = new ArrayList<ExGoodsBean>();
    private List<ExGoodsBean>  allGoodList2 = new ArrayList<ExGoodsBean>();

    private RelativeLayout rel_search;
    private int integral;

    private SignInPop signInPop;
    private ImageView iv_main_cart;
    private String userId;
    private TextView tv_shopcart_size;
    private RelativeLayout rel_data_load;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    ToastUtil.makeToast((String) msg.obj);
                    finish();
                    break;
                case HANDLER_SCROLL_ADVERTISEMENT:
                    scrollAdv();
                    break;
                case 20:  //签到成功加积分

                    //ToastUtil.makeToast((String) msg.obj);
                    tv_my_integral.setText("我的积分:" + (int) msg.obj);
                    showPop("签到成功获取10积分");
                    //integral = integral + 10;

                    break;
                case 40:

                    //ToastUtil.makeToast((String) msg.obj);
                    showPop((String) msg.obj);

                    break;
                case 200:

                    if (msg.obj != null) {
                        allGoodList.addAll((Collection<? extends ExGoodsBean>) msg.obj);
                        adapter.setList(allGoodList);
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case 300:
                    if (msg.obj != null) {
                        allGoodList2.addAll((Collection<? extends ExGoodsBean>) msg.obj);
                        LogUtil.i("------------------" + allGoodList2.size());
                        adapter2.setList(allGoodList2);
                        adapter2.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(this , R.color.app_bottom_color);
        setContentView(R.layout.act_exchange);

        preference = SDPreference.getInstance();
        openId = preference.getContent("openid");
        userId = preference.getContent("userId");
        initView();
        initData();
    }


    @Override
    protected void onResume() {
        super.onResume();
        //getProfile();
    }

    private void initView() {

        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(onClickListener);

        rel_data_load = (RelativeLayout) findViewById(R.id.rel_data_load);

        iv_main_cart = (ImageView) findViewById(R.id.iv_main_cart);
        iv_main_cart.setOnClickListener(onClickListener);

        tv_shopcart_size = (TextView) findViewById(R.id.tv_shopcart_size);
        if (!userId.equals("0")){
            tv_shopcart_size.setText(preference.getContent("cartSize"));
        }else{
            tv_shopcart_size.setText("0");
        }
        tv_my_integral = (TextView) findViewById(R.id.tv_my_integral);

        lin_sign_in_record = (LinearLayout) findViewById(R.id.lin_sign_in_record);
        lin_sign_in = (LinearLayout) findViewById(R.id.lin_sign_in);
        lin_sign_in_record.setOnClickListener(onClickListener);
        lin_sign_in.setOnClickListener(onClickListener);

        rel_zero_more = (RelativeLayout) findViewById(R.id.rel_zero_more);
        rel_optimize_more = (RelativeLayout) findViewById(R.id.rel_optimize_more);
        rel_zero_more.setOnClickListener(onClickListener);
        rel_optimize_more.setOnClickListener(onClickListener);

        vp_exchange = (ViewPager) findViewById(R.id.vp_exchange);
        vp_exchange.setFocusable(true);

        lv_zero = (MyListView) findViewById(R.id.lv_zero);
        lv_zero.setFocusable(false);
        adapter = new ExGoodsAdapter(this);
        adapter.setList(allGoodList);
        lv_zero.setAdapter(adapter);
        lv_zero.setOnItemClickListener(itemClickListener);

        lv_goods = (MyListView) findViewById(R.id.lv_goods);
        lv_goods.setFocusable(false);
        adapter2 = new ExGoodsAdapter(this);
        adapter2.setList(allGoodList2);
        lv_goods.setAdapter(adapter2);
        lv_goods.setOnItemClickListener(itemClickListener2);

        rel_search = (RelativeLayout) findViewById(R.id.rel_search);
        rel_search.setOnClickListener(onClickListener);
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_back :
                    finish();
                    break;
                case R.id.lin_sign_in_record:  //兑换记录

                    Intent intent1 = new Intent(ExChangeAct.this, ConvertRecordAct.class);
                    startActivity(intent1);

                    break;
                case R.id.lin_sign_in: // 签到
                    signInData();
                    break;
                case R.id.rel_zero_more: // 0元兑换  cateid

                    Intent intent = new Intent(ExChangeAct.this , SearchMoreAct.class);
                    intent.putExtra("cateid" , "1");
                    intent.putExtra("integral", integral);
                    startActivity(intent);

                    break;
                case R.id.rel_optimize_more: //精品优选兑换

                    Intent intent2 = new Intent(ExChangeAct.this , SearchMoreAct.class);
                    intent2.putExtra("cateid" , "2");
                    intent2.putExtra("integral", integral);
                    startActivity(intent2);

                    break;
                case R.id.rel_search:
                    // 搜索
                    Intent intent3 = new Intent(ExChangeAct.this , SearchMoreAct.class);
                    intent3.putExtra("integral", integral);
                    startActivity(intent3);
                    break;
                case R.id.iv_main_cart:

                    userId = preference.getContent("userId");
                    if (userId.equals("0")){
                        Intent intent4 = new Intent(ExChangeAct.this , LoginActivity.class);
                        startActivity(intent4);
                    }else{
                        Intent intent4 = new Intent(ExChangeAct.this , ShopCartAct2.class);
                        startActivity(intent4);
                    }
                    break;
            }
        }
    };


    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Intent intent = new Intent(ExChangeAct.this , ExChangeDetailAct.class);
            intent.putExtra("id" , allGoodList.get(position).getId());
            intent.putExtra("bean" , allGoodList.get(position));
            intent.putExtra("integral" , integral);
            startActivity(intent);
        }
    };

    AdapterView.OnItemClickListener itemClickListener2 = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Intent intent = new Intent(ExChangeAct.this , ExChangeDetailAct.class);
            intent.putExtra("id" , allGoodList2.get(position).getId());
            intent.putExtra("bean" , allGoodList2.get(position));
            intent.putExtra("integral" , integral);
            startActivity(intent);
        }
    };

    private void initData() {

        rel_data_load.setVisibility(View.VISIBLE);

        GetDataBiz.getExChangetData(openId, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                if (result == null || result.equals("")){
                    return;
                }
                rel_data_load.setVisibility(View.GONE);
                LogUtil.i("积分兑换的首页" + result);
                Gson gson = new Gson();
                JSONObject obj = new JSONObject(result);
                JSONObject dataObject = obj.getJSONObject("data");

                String credit = dataObject.getString("credit");
                integral = Integer.parseInt(credit);
                LogUtil.i("-------ss-------" + integral);
                tv_my_integral.setText("我的积分:" + integral);

                List<BannerBean2>  bannerList = new ArrayList<BannerBean2>();
                List<ExGoodsBean>  goodList = new ArrayList<ExGoodsBean>();
                List<ExGoodsBean>  goodList2 = new ArrayList<ExGoodsBean>();

                JSONArray bannerJsonArray = dataObject.getJSONArray("advs");
                for (int i = 0; i < bannerJsonArray.length(); i++) {
                    JSONObject bannerObject = bannerJsonArray.getJSONObject(i);
                    BannerBean2 bannerBean =  gson.fromJson(bannerObject.toString(), BannerBean2.class);
                    bannerList.add(bannerBean);
                }

                JSONArray goodsArray = dataObject.getJSONArray("goods_no");
                for (int i = 0; i < goodsArray.length(); i++) {
                    JSONObject goodsObject = goodsArray.getJSONObject(i);
                    ExGoodsBean exGoodsBean =  gson.fromJson(goodsObject.toString(), ExGoodsBean.class);
                    goodList.add(exGoodsBean);
                }

                JSONArray goodsArray2 = dataObject.getJSONArray("goods");
                for (int i = 0; i < goodsArray2.length(); i++) {
                    JSONObject goodsObject = goodsArray2.getJSONObject(i);
                    ExGoodsBean exGoodsBean =  gson.fromJson(goodsObject.toString(), ExGoodsBean.class);
                    goodList2.add(exGoodsBean);
                }

                handler.obtainMessage(200 , goodList).sendToTarget();
                handler.obtainMessage(300 , goodList2).sendToTarget();

                adv_list = new ArrayList<ImageView>();
                for (int i = 0; i < bannerList.size(); i++) {
                    final BannerBean2 enty = (BannerBean2) bannerList.get(i);
                    String url = enty.getThumb();
                    // final String link = enty.getAdlink();
                    //显示广告图片
                    ImageView image = new ImageView(ExChangeAct.this);
                    image.setScaleType(ImageView.ScaleType.FIT_XY);
                    ImagerLoaderUtil.displayImageWithLoadingIcon(url, image, R.drawable.advertisement_loading);
                    //设置监听器
                    image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                        if (enty.getType() != null) {
//                            //clickIntoAdvertisement(link, enty.getType());
//                        }
                        }
                    });
                    adv_list.add(image);
                }
                //设置适配器
                AdvViewpagerAdapter adapter = new AdvViewpagerAdapter(adv_list);
                vp_exchange.setAdapter(adapter);

                //设置指示器
                if (isFirst) {
                    isFirst = false;
                    // indicator.setViewPager(vp_adv);
                    //滚动广告
                    timer.schedule(timeTask, 5000, 5000);
                }
            }

            @Override
            public void onHttpRequestError(String error) {
                rel_data_load.setVisibility(View.GONE);
            }
        });
    }

    //设置广告页
    private void scrollAdv() {
        if (scroll_flag) {
            int currentIndex = vp_exchange.getCurrentItem();
            if (currentIndex == adv_list.size() - 1) {
                currentIndex = 0;
            } else {
                currentIndex += 1;
            }
            vp_exchange.setCurrentItem(currentIndex, false);
        }
    }

    /**
     * 点击签到
     *
     */
    private void signInData(){
        GetDataBiz.setSignData(openId, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                LogUtil.i("签到接口" + result);
                if (result == null || result.equals("")){
                    return;
                }
                JSONObject obj = new JSONObject(result);
                if (obj.getString("code").equals("200")){
                    String message = obj.getJSONObject("message").getString("message");
                    int jifen = obj.getJSONObject("message").getInt("credit");
                    handler.obtainMessage(20 , jifen).sendToTarget();
                }else if (obj.getString("code").equals("400")){
                    String message = obj.getString("message");
                    handler.obtainMessage(40 , message).sendToTarget();
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }

    public void getProfile() {
        GetProfileBiz.getProfile2(openId , new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                if (result == null && result.equals("")){
                    return;
                }

                JSONObject jsonObject = new JSONObject(result);
                LogUtil.i("www" + jsonObject.getString("message"));

                if (jsonObject.getString("code").equals("200")){
                    JSONObject dataJsonObject = jsonObject.getJSONObject("data");

                    LogUtil.i("-------integral-------" + dataJsonObject.getString("integral"));

                    String ss = dataJsonObject.getString("integral");
                    integral = Integer.parseInt(ss);

                    LogUtil.i("-------ss-------" + integral);

                    tv_my_integral.setText("我的积分:" + integral);
                }
            }

            @Override
            public void onHttpRequestError(String error) {
                rel_data_load.setVisibility(View.GONE);
            }
        });
    }


    private void showPop(String text){

        if (signInPop == null) {
            signInPop = new SignInPop(ExChangeAct.this);
        }

        if (!signInPop.isShowing()) {
            signInPop.showAtLocation(ll_back, Gravity.BOTTOM, 0, 0);
            signInPop.isShowing();
        } else {
            signInPop.dismiss();
            signInPop = null;
        }
        signInPop.setTextContent(text);
    }
}
