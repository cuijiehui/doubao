package com.xinspace.csevent.shop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xinspace.csevent.R;
import com.xinspace.csevent.adapter.AdvViewpagerAdapter;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.biz.GetDataBiz;
import com.xinspace.csevent.data.entity.AdvBanner;
import com.xinspace.csevent.login.activity.LoginActivity;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.parser.JsonPaser;
import com.xinspace.csevent.shop.adapter.JiuGoodsAdapter;
import com.xinspace.csevent.shop.modle.JiuGoodsBean;
import com.xinspace.csevent.util.ImagerLoaderUtil;
import com.xinspace.csevent.util.StatusBarUtils;
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
 * 九块九商品列表
 *
 * Created by Android on 2017/6/3.
 */

public class JiuGoodsAct extends BaseActivity{

    private LinearLayout ll_jiu_back;
    private ViewPager vp_shop_jiu;
    private List<ImageView> adv_list;//广告fragment集合
    private CircleIndicator indicator;//指示器
    //重新获取地址的请求码
    private int REQUEST_CODE_RE_GET_ADDRESS = 200;
    //广告滚动
    private static final int HANDLER_SCROLL_ADVERTISEMENT = 10;
    private boolean scroll_flag = true;//标记变量,用于判断是否自动滚动广告

    private ListView lv_jiu_goods;
    private List<JiuGoodsBean> allList;
    private JiuGoodsAdapter goodsAdapter;
    private ImageView iv_main_cart;
    private TextView tv_shopcart_size;

    private SDPreference preference;
    private String userId;
    private RelativeLayout rel_data_load;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //广告滚动
                case HANDLER_SCROLL_ADVERTISEMENT:
                    scrollAdv();
                    break;

                case 200:
                    rel_data_load.setVisibility(View.GONE);
                    if (msg.obj != null){
                        allList.addAll((Collection<? extends JiuGoodsBean>) msg.obj);
                        Log.i("www" , "传过来数组长度" + allList.size());
                        goodsAdapter.setList(allList);
                        goodsAdapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };
    private Timer timer = new Timer();
    private boolean isFirst = true;//第一次设置indicator
    private TimerTask timeTask = new TimerTask() {
        @Override
        public void run() {
            handler.sendEmptyMessage(HANDLER_SCROLL_ADVERTISEMENT);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(JiuGoodsAct.this , R.color.app_bottom_color);

        setContentView(R.layout.act_jiu_goods);
        preference = SDPreference.getInstance();
        userId = preference.getContent("userId");
        allList = new ArrayList<>();
        initView();
        getAdvertisement();
    }


    private void initView() {

        ll_jiu_back = (LinearLayout) findViewById(R.id.ll_jiu_back);
        ll_jiu_back.setOnClickListener(clickListener);

        rel_data_load = (RelativeLayout) findViewById(R.id.rel_data_load);

        iv_main_cart = (ImageView) findViewById(R.id.iv_main_cart);
        iv_main_cart.setOnClickListener(clickListener);

        tv_shopcart_size = (TextView) findViewById(R.id.tv_shopcart_size);
        if (!userId.equals("0")){
            tv_shopcart_size.setText(preference.getContent("cartSize"));
        }else{
            tv_shopcart_size.setText("0");
        }

        vp_shop_jiu = (ViewPager) findViewById(R.id.vp_shop_jiu);

        lv_jiu_goods = (ListView) findViewById(R.id.lv_jiu_goods);
        goodsAdapter = new JiuGoodsAdapter(JiuGoodsAct.this);
        goodsAdapter.setList(allList);
        lv_jiu_goods.setAdapter(goodsAdapter);

        lv_jiu_goods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(JiuGoodsAct.this , GoodsDetailAct.class);

                intent.putExtra("goodId" , allList.get(position).getGid());

                startActivity(intent);

            }
        });
    }


    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_jiu_back:
                    finish();
                    break;
                case R.id.iv_main_cart:
                    userId = preference.getContent("userId");
                    if (userId.equals("0")){
                        Intent intent = new Intent(JiuGoodsAct.this , LoginActivity.class);

                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(JiuGoodsAct.this , ShopCartAct2.class);

                        startActivity(intent);
                    }
                    break;
            }
        }
    };

    //获取广告
    private void getAdvertisement() {
        GetDataBiz.getJiuListData( new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                Log.i("www", "广告的数据" + result);
                if (result == null || result.equals("")){
                    return;
                }
                showAdvertisement(result);
            }

            @Override
            public void onHttpRequestError(String error) {
                rel_data_load.setVisibility(View.GONE);
            }
        });
    }

    //设置滚动广告栏的adapter
    private void showAdvertisement(String result) {
        try {
            JSONObject obj = new JSONObject(result);
            JSONObject dataJsonObject = obj.getJSONObject("data");

            JSONArray ary = dataJsonObject.getJSONArray("banner");
            List<Object> advs = JsonPaser.parserAry(ary.toString(), AdvBanner.class);
            adv_list = new ArrayList<>();
            for (int i = 0; i < advs.size(); i++) {
                final AdvBanner enty = (AdvBanner) advs.get(i);
                String url = enty.getImgurl();
                //final String link = enty.getAdlink();
                //显示广告图片
                ImageView image = new ImageView(JiuGoodsAct.this);
                image.setScaleType(ImageView.ScaleType.FIT_XY);
                ImagerLoaderUtil.displayImageWithLoadingIcon(url, image, R.drawable.advertisement_loading);
                //设置监听器
//                image.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (enty.getType() != null) {
//                            //clickIntoAdvertisement(link, enty.getType());
//                        }
//                    }
//                });
                adv_list.add(image);
            }
            //设置适配器
            AdvViewpagerAdapter adapter = new AdvViewpagerAdapter(adv_list);
            vp_shop_jiu.setAdapter(adapter);

            //设置指示器
            if (isFirst) {
                isFirst = false;
                // indicator.setViewPager(vp_adv);
                //滚动广告
                timer.schedule(timeTask, 5000, 5000);
            }


            Gson gson = new Gson();
            List<JiuGoodsBean> goodsList = new ArrayList<JiuGoodsBean>();
            JSONArray goodsJsonArray = dataJsonObject.getJSONArray("list");
            for (int i = 0 ; i < goodsJsonArray.length() ; i++){
                JSONObject goodsJSONObject = goodsJsonArray.getJSONObject(i);
                JiuGoodsBean bean = gson.fromJson(goodsJSONObject.toString() , JiuGoodsBean.class);
                goodsList.add(bean);
            }
            handler.obtainMessage(200 , goodsList).sendToTarget();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //设置广告页
    private void scrollAdv() {
        if (scroll_flag) {
            int currentIndex = vp_shop_jiu.getCurrentItem();
            if (currentIndex == adv_list.size() - 1) {
                currentIndex = 0;
            } else {
                currentIndex += 1;
            }
            vp_shop_jiu.setCurrentItem(currentIndex, false);
        }
    }


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
        allList = null;
        clickListener = null;
        handler.removeCallbacksAndMessages(null);
        ImagerLoaderUtil.clearImageMemory();
        this.setContentView(R.layout.empty_view);
        System.gc();
        super.onDestroy();

    }
}
