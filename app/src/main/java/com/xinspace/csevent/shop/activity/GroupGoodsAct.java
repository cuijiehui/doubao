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
import com.xinspace.csevent.monitor.view.MyGridView;
import com.xinspace.csevent.R;
import com.xinspace.csevent.adapter.AdvViewpagerAdapter;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.biz.GetDataBiz;
import com.xinspace.csevent.login.activity.LoginActivity;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.parser.JsonPaser;
import com.xinspace.csevent.shop.adapter.CateAdapter;
import com.xinspace.csevent.shop.adapter.GroupGoodsAdapter;
import com.xinspace.csevent.shop.modle.CategoryBean;
import com.xinspace.csevent.shop.modle.GroupAdsBean;
import com.xinspace.csevent.shop.modle.GroupGoodsBean;
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
 * 拼团列表界面
 *
 * Created by Android on 2017/6/8.
 */

public class GroupGoodsAct extends BaseActivity{

    private LinearLayout ll_group_back;
    private ViewPager vp_shop_group;
    private List<ImageView> adv_list;//广告fragment集合
    private CircleIndicator indicator;//指示器
    //重新获取地址的请求码
    private int REQUEST_CODE_RE_GET_ADDRESS = 200;
    //广告滚动
    private static final int HANDLER_SCROLL_ADVERTISEMENT = 10;
    private boolean scroll_flag = true;//标记变量,用于判断是否自动滚动广告

    private ListView lv_group_goods;
    private List<GroupGoodsBean> allList;
    private GroupGoodsAdapter goodsAdapter;
    private List<CategoryBean> allCataList = new ArrayList<>();
    private MyGridView gv_group_cate;
    private SDPreference preference;
    private ImageView iv_main_cart;
    private TextView tv_shopcart_size;
    private String userId;
    private ImageView iv_group_search;
    private CateAdapter cateAdapter;
    private RelativeLayout rel_data_load;

    private Timer timer = new Timer();
    private boolean isFirst = true;//第一次设置indicator
    private TimerTask timeTask = new TimerTask() {
        @Override
        public void run() {
            handler.sendEmptyMessage(HANDLER_SCROLL_ADVERTISEMENT);
        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //广告滚动
                case HANDLER_SCROLL_ADVERTISEMENT:
                    scrollAdv();
                    break;
                case 200:
                    if (msg.obj != null){
                        allList.addAll((Collection<? extends GroupGoodsBean>) msg.obj);
                        Log.i("www" , "传过来数组长度" + allList.size());
                        goodsAdapter.setList(allList);
                        goodsAdapter.notifyDataSetChanged();
                    }
                    break;
                case 201:
                    if (msg.obj != null){
                        allCataList.addAll((Collection<? extends CategoryBean>) msg.obj);
                        Log.i("www" , "传过来分类数组长度" + allList.size());
                        cateAdapter.setList(allCataList);
                        cateAdapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(GroupGoodsAct.this , R.color.app_bottom_color);

        setContentView(R.layout.act_group_goods);
        allList = new ArrayList<>();
        preference = SDPreference.getInstance();
        userId = preference.getContent("userId");
        initView();
        getAdvertisement();
    }

    private void initView() {
        ll_group_back = (LinearLayout) findViewById(R.id.ll_group_back);
        ll_group_back.setOnClickListener(clickListener);

        rel_data_load = (RelativeLayout) findViewById(R.id.rel_data_load);

        iv_main_cart = (ImageView) findViewById(R.id.iv_main_cart);
        iv_main_cart.setOnClickListener(clickListener);

        iv_group_search = (ImageView) findViewById(R.id.iv_group_search);
        iv_group_search.setOnClickListener(clickListener);

        tv_shopcart_size = (TextView) findViewById(R.id.tv_shopcart_size);
        if (!userId.equals("0")){
            tv_shopcart_size.setText(preference.getContent("cartSize"));
        }else{
            tv_shopcart_size.setText("0");
        }

        vp_shop_group = (ViewPager) findViewById(R.id.vp_shop_group);
        vp_shop_group.setFocusable(true);

        gv_group_cate = (MyGridView) findViewById(R.id.gv_group_cate);
        cateAdapter = new CateAdapter(GroupGoodsAct.this);
        cateAdapter.setList(allCataList);
        gv_group_cate.setAdapter(cateAdapter);
        gv_group_cate.setOnItemClickListener(onItemClickListener);

        lv_group_goods = (ListView) findViewById(R.id.lv_group_goods);
        goodsAdapter = new GroupGoodsAdapter(GroupGoodsAct.this);
        goodsAdapter.setList(allList);
        lv_group_goods.setAdapter(goodsAdapter);

        lv_group_goods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String uid = preference.getContent("userId");
                if (!uid.equals("0")){
                    Intent intent = new Intent(GroupGoodsAct.this , GroupGoodsDetailAct.class);
                    intent.putExtra("goodId" , allList.get(position).getId());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(GroupGoodsAct.this ,LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(GroupGoodsAct.this , GroupCateAct.class);

            intent.putExtra("cateId" , allCataList.get(position).getId());

            startActivity(intent);
        }
    };

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_group_back:
                    GroupGoodsAct.this.finish();
                    break;
                case R.id.iv_main_cart:
                    userId = preference.getContent("userId");
                    if (userId.equals("0")){
                        Intent intent4 = new Intent(GroupGoodsAct.this , LoginActivity.class);
                        startActivity(intent4);
                    }else{
                        Intent intent4 = new Intent(GroupGoodsAct.this , ShopCartAct2.class);
                        startActivity(intent4);
                    }
                    break;
                case R.id.iv_group_search:  //搜索
                    Intent intent = new Intent(GroupGoodsAct.this , GroupCateAct.class);
                    intent.putExtra("cateId" , "");
                    startActivity(intent);
                    break;
            }
        }
    };

    //获取广告
    private void getAdvertisement() {

        rel_data_load.setVisibility(View.VISIBLE);

        GetDataBiz.getGroupListData(new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                Log.i("www", "广告的数据" + result);
                if (result == null || result.equals("")){
                    return;
                }
                rel_data_load.setVisibility(View.GONE);
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

            Gson gson = new Gson();
            List<CategoryBean> categoryList = new ArrayList<>();
            JSONArray categoryJsonArray = dataJsonObject.getJSONArray("category");
            for (int m = 0 ; m < categoryJsonArray.length() ; m++){
                CategoryBean categoryBean = gson.fromJson(categoryJsonArray.getJSONObject(m).toString(),CategoryBean.class);
                categoryList.add(categoryBean);
            }

            List<GroupGoodsBean> goodsList = new ArrayList<GroupGoodsBean>();
            JSONArray goodsJsonArray = dataJsonObject.getJSONArray("goods");
            for (int i = 0 ; i < goodsJsonArray.length() ; i++){
                JSONObject goodsJSONObject = goodsJsonArray.getJSONObject(i);
                GroupGoodsBean bean = gson.fromJson(goodsJSONObject.toString() , GroupGoodsBean.class);
                goodsList.add(bean);
            }

            if (categoryList.size() != 0){
                handler.obtainMessage(201, categoryList).sendToTarget();
            }

            if (goodsList.size() != 0){
                handler.obtainMessage(200 , goodsList).sendToTarget();
            }

            JSONArray ary = dataJsonObject.getJSONArray("advs");
            List<Object> advs = JsonPaser.parserAry(ary.toString(), GroupAdsBean.class);
            adv_list = new ArrayList<>();
            for (int i = 0; i < advs.size(); i++) {
                final GroupAdsBean enty = (GroupAdsBean) advs.get(i);
                String url = enty.getThumb();
                //final String link = enty.getAdlink();
                //显示广告图片
                ImageView image = new ImageView(GroupGoodsAct.this);
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
            vp_shop_group.setAdapter(adapter);
            //设置指示器
            if (isFirst) {
                isFirst = false;
                // indicator.setViewPager(vp_adv);
                //滚动广告
                timer.schedule(timeTask, 5000, 5000);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //设置广告页
    private void scrollAdv() {
        if (scroll_flag) {
            int currentIndex = vp_shop_group.getCurrentItem();
            if (currentIndex == adv_list.size() - 1) {
                currentIndex = 0;
            } else {
                currentIndex += 1;
            }
            vp_shop_group.setCurrentItem(currentIndex, false);
        }
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        if(timer != null){
            timer.cancel();
        }
        if(timeTask != null){
            timeTask.cancel();
        }

        timer = null;
        timeTask = null;
        clickListener = null;
        allCataList.clear();
        allList.clear();
        allCataList = null;
        allList = null;
        ImagerLoaderUtil.clearImageMemory();
        this.setContentView(R.layout.empty_view);
        System.gc();
        super.onDestroy();
    }
}
