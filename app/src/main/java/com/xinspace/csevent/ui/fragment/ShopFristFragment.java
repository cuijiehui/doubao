package com.xinspace.csevent.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.xinspace.csevent.monitor.view.MyListView;
import com.xinspace.csevent.R;
import com.xinspace.csevent.adapter.AdvViewpagerAdapter;
import com.xinspace.csevent.app.utils.ScreenUtils;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.biz.GetAdvertisementBiz;
import com.xinspace.csevent.login.activity.ExChangeAct;
import com.xinspace.csevent.login.activity.LoginActivity;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.shop.activity.FreeTrialAct;
import com.xinspace.csevent.shop.activity.GoodsDetailAct;
import com.xinspace.csevent.shop.activity.GroupGoodsAct;
import com.xinspace.csevent.shop.activity.JiuGoodsAct;
import com.xinspace.csevent.shop.activity.SearchAllShopAct;
import com.xinspace.csevent.shop.activity.SeckillAct;
import com.xinspace.csevent.shop.modle.BannerBean;
import com.xinspace.csevent.shop.modle.FristGoodsBean;
import com.xinspace.csevent.shop.modle.NoticeBean;
import com.xinspace.csevent.sweepstake.adapter.FristGoodsAdapter;
import com.xinspace.csevent.sweepstake.adapter.FristGoodsAdapter2;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.ui.activity.WebViewActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import sdk_sample.sdk.views.HorizontalListView;

/**
 * Created by Android on 2017/3/19.
 */

public class ShopFristFragment extends Fragment {

    private ViewPager vp_shop_first;
    private View view;
    private List<ImageView> adv_list;//广告fragment集合
    //重新获取地址的请求码
    private int REQUEST_CODE_RE_GET_ADDRESS = 200;
    //广告滚动
    private static final int HANDLER_SCROLL_ADVERTISEMENT = 10;
    private boolean scroll_flag = true;//标记变量,用于判断是否自动滚动广告
    private FristGoodsAdapter goodsAdapter;
    private FristGoodsAdapter2 goodsAdapter2;
    private FristGoodsAdapter goodsAdapter3;

    private List<FristGoodsBean> goods_List = new ArrayList<>();
    private List<FristGoodsBean> goods_List2 = new ArrayList<>();
    private List<FristGoodsBean> goods_List3 = new ArrayList<>();
    private List<FristGoodsBean> goods_List4 = new ArrayList<>();

    private MyListView lv_shop_one;
    private MyListView lv_shop_two;
    private ImageView iv_ad1;
    private WebView iv_ad2;
    private WebView iv_ad3;
    private HorizontalListView gv_shop;
    private int screenWidth;

    private RelativeLayout rel_moods_top, rel_sale_top;
    private TextView tv_sale_top, tv_sale_line;
    private TextView tv_moods_top, tv_moods_line;

    private LinearLayout lin_jiu;
    private LinearLayout lin_seckill;
    private LinearLayout lin_group;
    private LinearLayout lin_try_free;
    private LinearLayout lin_exChange;

    private SDPreference preference;
    private String userId;
    private ImageView iv_go_top;
    private ScrollView sl_shop_first;


    private TextView tv_new_more;
    private RelativeLayout rel_data_load;
    private List<NoticeBean> allNoticeList = new ArrayList<>();
    public static final int NEWS_MESSAGE_TEXTVIEW = 2000;//通知公告信息
    private int index = 0;
    private RelativeLayout rel_notice;
    private TextSwitcher ts_shop_first;   //商城首页的公告

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //广告滚动
                case HANDLER_SCROLL_ADVERTISEMENT:
                    scrollAdv();
                    break;
                case 100:
                    if (msg.obj != null) {
                        goods_List.addAll((Collection<? extends FristGoodsBean>) msg.obj);
                        Log.i("www", "商品长度" + goods_List.size());
                        goodsAdapter.setList(goods_List);
                        goodsAdapter.notifyDataSetChanged();
                    }
                    break;
                case 200:
                    if (msg.obj != null) {
                        goods_List2.addAll((Collection<? extends FristGoodsBean>) msg.obj);
                        Log.i("www", "第二商品长度" + goods_List2.size());
                        goodsAdapter2.setList(goods_List2);
                        goodsAdapter2.notifyDataSetChanged();
                    }
                    break;
                case 300:
                    if (msg.obj != null) {
                        goods_List3.addAll((Collection<? extends FristGoodsBean>) msg.obj);
                        Log.i("www", "第二商品长度" + goods_List3.size());
                        goodsAdapter3.setList(goods_List3);
                        goodsAdapter3.notifyDataSetChanged();
                    }
                    break;
                case 400:
                    if (msg.obj != null) {
                        goods_List4.addAll((Collection<? extends FristGoodsBean>) msg.obj);
                    }
                    break;
                case 500:
                    if (msg.obj != null) {
                        allNoticeList.addAll((Collection<? extends NoticeBean>) msg.obj);
                        notice(allNoticeList);
                    }
                    break;
                case NEWS_MESSAGE_TEXTVIEW:
                    ts_shop_first.setText(allNoticeList.get(index).getTitle());
                    index++;
                    if (index == allNoticeList.size()) {
                        index = 0;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_shop_frist, null);
        preference = SDPreference.getInstance();
        userId = preference.getContent("userId");
        initView();
        getShopFristData();
        return view;
    }

    private void initView() {

        rel_data_load = (RelativeLayout) view.findViewById(R.id.rel_data_load);

        screenWidth = ScreenUtils.getScreenWidth(getActivity());
        vp_shop_first = (ViewPager) view.findViewById(R.id.vp_shop_first);
        vp_shop_first.setFocusable(true);  //焦点在ViewPager上

        iv_go_top = (ImageView) view.findViewById(R.id.iv_go_top);
        iv_go_top.setOnClickListener(onclickListener);
        sl_shop_first = (ScrollView) view.findViewById(R.id.sl_shop_first);

        iv_ad1 = (ImageView) view.findViewById(R.id.iv_ad1);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) iv_ad1.getLayoutParams();
        // params.height = (int) (screenWidth * 1.0f);
        params.width = (int) (screenWidth * 1.0f);
        params.height = (int) (screenWidth * 0.266f);
        iv_ad1.setLayoutParams(params);

        iv_ad2 = (WebView) view.findViewById(R.id.iv_ad2);
        iv_ad3 = (WebView) view.findViewById(R.id.iv_ad3);

        rel_moods_top = (RelativeLayout) view.findViewById(R.id.rel_moods_top);
        rel_moods_top.setOnClickListener(onclickListener);

        rel_sale_top = (RelativeLayout) view.findViewById(R.id.rel_sale_top);
        rel_sale_top.setOnClickListener(onclickListener);

        tv_sale_top = (TextView) view.findViewById(R.id.tv_sale_top);
        tv_sale_line = (TextView) view.findViewById(R.id.tv_sale_line);
        tv_moods_top = (TextView) view.findViewById(R.id.tv_moods_top);
        tv_moods_line = (TextView) view.findViewById(R.id.tv_moods_line);

        lv_shop_one = (MyListView) view.findViewById(R.id.lv_shop_one);
        lv_shop_one.setFocusable(false);

        lv_shop_two = (MyListView) view.findViewById(R.id.lv_shop_two);
        lv_shop_two.setFocusable(false);

        goodsAdapter = new FristGoodsAdapter(getActivity(), "1");
        goodsAdapter.setList(goods_List);
        lv_shop_one.setAdapter(goodsAdapter);

        goodsAdapter3 = new FristGoodsAdapter(getActivity(), "2");
        goodsAdapter3.setList(goods_List3);
        lv_shop_two.setAdapter(goodsAdapter3);

        gv_shop = (HorizontalListView) view.findViewById(R.id.gv_shop);
        gv_shop.setFocusable(false);
        goodsAdapter2 = new FristGoodsAdapter2(getActivity());
        goodsAdapter2.setList(goods_List2);
        gv_shop.setAdapter(goodsAdapter2);

        lv_shop_one.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), GoodsDetailAct.class);
                intent.putExtra("goodId", goods_List.get(position).getGid());
                startActivity(intent);
            }
        });

        lv_shop_two.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), GoodsDetailAct.class);
                intent.putExtra("goodId", goods_List3.get(position).getGid());
                startActivity(intent);
            }
        });

        gv_shop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), GoodsDetailAct.class);
                intent.putExtra("goodId", goods_List2.get(position).getGid());
                startActivity(intent);
            }
        });

        lin_jiu = (LinearLayout) view.findViewById(R.id.lin_jiu);
        lin_jiu.setOnClickListener(onclickListener);

        lin_seckill = (LinearLayout) view.findViewById(R.id.lin_seckill);
        lin_seckill.setOnClickListener(onclickListener);

        lin_group = (LinearLayout) view.findViewById(R.id.lin_group);
        lin_group.setOnClickListener(onclickListener);

        lin_try_free = (LinearLayout) view.findViewById(R.id.lin_try_free);
        lin_try_free.setOnClickListener(onclickListener);

        lin_exChange = (LinearLayout) view.findViewById(R.id.lin_exChange);
        lin_exChange.setOnClickListener(onclickListener);

        tv_new_more = (TextView) view.findViewById(R.id.tv_new_more);
        tv_new_more.setOnClickListener(onclickListener);

        rel_notice = (RelativeLayout) view.findViewById(R.id.rel_notice);
        rel_notice.setOnClickListener(onclickListener);

        ts_shop_first = (TextSwitcher) view.findViewById(R.id.ts_shop_first);
        ts_shop_first.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView textView = new TextView(getActivity());
                textView.setSingleLine();
                textView.setTextSize(14);//字号
                textView.setTextColor(Color.parseColor("#ff3333"));
                textView.setEllipsize(TextUtils.TruncateAt.END);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.gravity = Gravity.CENTER_VERTICAL;
                textView.setLayoutParams(params);
                return textView;
            }
        });

    }

    View.OnClickListener onclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rel_moods_top:
                    tv_sale_top.setTextColor(Color.parseColor("#4a4a4a"));
                    tv_sale_line.setBackgroundColor(Color.parseColor("#ffffff"));
                    tv_moods_top.setTextColor(Color.parseColor("#07a9e6"));
                    tv_moods_line.setBackgroundColor(Color.parseColor("#07a9e6"));
                    goodsAdapter2.setList(goods_List2);
                    goodsAdapter2.notifyDataSetChanged();
                    break;
                case R.id.rel_sale_top:
                    tv_sale_top.setTextColor(Color.parseColor("#07a9e6"));
                    tv_sale_line.setBackgroundColor(Color.parseColor("#07a9e6"));
                    tv_moods_top.setTextColor(Color.parseColor("#4a4a4a"));
                    tv_moods_line.setBackgroundColor(Color.parseColor("#ffffff"));
                    goodsAdapter2.setList(goods_List4);
                    goodsAdapter2.notifyDataSetChanged();
                    break;
                case R.id.lin_jiu:
                    if (rel_data_load.isShown()) return;
                    Intent intent = new Intent(getActivity(), JiuGoodsAct.class);
                    startActivity(intent);

                    break;
                case R.id.lin_seckill:
                    if (rel_data_load.isShown()) return;
                    Intent intent1 = new Intent(getActivity(), SeckillAct.class);
                    startActivity(intent1);

                    break;
                case R.id.lin_group:
                    if (rel_data_load.isShown()) return;
                    Intent intent2 = new Intent(getActivity(), GroupGoodsAct.class);
                    startActivity(intent2);
                    break;
                case R.id.lin_try_free: // 免费试用
                    if (rel_data_load.isShown()) return;
                    userId = preference.getContent("userId");
                    if (!userId.equals("0")) {
                        Intent intent3 = new Intent(getActivity(), FreeTrialAct.class);
                        startActivity(intent3);
                    } else {
                        Intent intent3 = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent3);
                    }
                    break;
                case R.id.lin_exChange: //积分兑换
                    if (rel_data_load.isShown()) return;
                    userId = preference.getContent("userId");
                    if (!userId.equals("0")) {
                        Intent intent4 = new Intent(getActivity(), ExChangeAct.class);
                        startActivity(intent4);
                    } else {
                        Intent intent4 = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent4);
                    }
                    break;
                case R.id.iv_go_top:
                    sl_shop_first.scrollTo(0, 0);
                    break;
                case R.id.tv_new_more:  //新品推荐更多
                    Intent intent5 = new Intent(getActivity(), SearchAllShopAct.class);
                    intent5.putExtra("type", "1");
                    intent5.putExtra("cate", "");
                    startActivity(intent5);
                    break;
                case R.id.rel_notice:
                    Intent intent6 = new Intent(getActivity(), WebViewActivity.class);
                    intent6.putExtra("data", allNoticeList.get(index).getLinkurl());
                    startActivity(intent6);
                    break;
            }
        }
    };

    private void getShopFristData() {

        LogUtil.i("------数据又加载了----------");
        GetAdvertisementBiz.getShopFristData(new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                LogUtil.i("商城首页的数据" + result);
                if (TextUtils.isEmpty(result)) {
                    return;
                }
                showAdvertisement(result);
                rel_data_load.setVisibility(View.GONE);
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }

    //获取广告
//    private void getAdvertisement() {
//        GetAdvertisementBiz.getAdvs2(getActivity(), new HttpRequestListener() {
//            @Override
//            public void onHttpRequestFinish(String result) throws JSONException {
//                Log.i("www", "广告的数据" + result);
//                showAdvertisement(result);
//            }
//
//            @Override
//            public void onHttpRequestError(String error) {
//
//            }
//        });
//    }

    //设置滚动广告栏的adapter
    private void showAdvertisement(String result) {
        try {
//            JSONObject obj = new JSONObject(result);
//            JSONArray ary = obj.getJSONArray("result");
//            List<Object> advs = JsonPaser.parserAry(ary.toString(), AdvBean.class);
            Gson gson = new Gson();
            JSONObject obj = new JSONObject(result);
            List<BannerBean> bannerList = null;

            List<FristGoodsBean> goodList = null;
            List<FristGoodsBean> goodList2 = null;
            List<FristGoodsBean> goodList3 = null;
            List<FristGoodsBean> goodList4 = null;

            List<BannerBean> adList1 = null;
            List<BannerBean> adList2 = null;
            List<BannerBean> adList3 = null;

            List<NoticeBean> noticeList = null;

            if (obj.getInt("code") == 200) {
                JSONObject jsonObject = obj.getJSONObject("data");
                JSONArray bannerJsonArray = jsonObject.getJSONArray("banner");
                bannerList = new ArrayList<BannerBean>();
                for (int i = 0; i < bannerJsonArray.length(); i++) {
                    JSONObject bannerObject = bannerJsonArray.getJSONObject(i);
                    BannerBean bannerBean = gson.fromJson(bannerObject.toString(), BannerBean.class);
                    bannerList.add(bannerBean);
                }
                //LogUtil.i("11111111111111111111");

                JSONArray adJsonArray1 = jsonObject.getJSONArray("ad1");
                adList1 = new ArrayList<>();
                for (int i = 0; i < adJsonArray1.length(); i++) {
                    JSONObject adJsonObject1 = adJsonArray1.getJSONObject(i);
                    BannerBean bannerBean = gson.fromJson(adJsonObject1.toString(), BannerBean.class);
                    adList1.add(bannerBean);
                }
                //LogUtil.i("22222222222222222222");

                JSONArray adJsonArray2 = jsonObject.getJSONArray("ad2");
                adList2 = new ArrayList<>();
                for (int i = 0; i < adJsonArray2.length(); i++) {
                    JSONObject adJsonObject2 = adJsonArray2.getJSONObject(i);
                    BannerBean bannerBean = gson.fromJson(adJsonObject2.toString(), BannerBean.class);
                    adList2.add(bannerBean);
                }

                //LogUtil.i("33333333333333333");

                JSONArray adJsonArray3 = jsonObject.getJSONArray("ad3");
                adList3 = new ArrayList<>();
                for (int i = 0; i < adJsonArray3.length(); i++) {
                    JSONObject adJsonObject3 = adJsonArray3.getJSONObject(i);
                    BannerBean bannerBean = gson.fromJson(adJsonObject3.toString(), BannerBean.class);
                    adList3.add(bannerBean);
                }

                //LogUtil.i("4444444444444444444444");
                JSONArray goodJsonArray = jsonObject.getJSONArray("list1");
                goodList = new ArrayList<FristGoodsBean>();
                for (int i = 0; i < goodJsonArray.length(); i++) {
                    JSONObject goodsObject = goodJsonArray.getJSONObject(i);
                    FristGoodsBean bean = gson.fromJson(goodsObject.toString(), FristGoodsBean.class);
                    goodList.add(bean);
                }

                //LogUtil.i("55555555555555555555555");
                JSONObject jsonObject2 = jsonObject.getJSONObject("list2");
                JSONArray goodJsonArray2 = jsonObject2.getJSONArray("1");
                goodList2 = new ArrayList<FristGoodsBean>();
                for (int i = 0; i < goodJsonArray2.length(); i++) {
                    JSONObject goodsObject2 = goodJsonArray2.getJSONObject(i);
                    FristGoodsBean bean = gson.fromJson(goodsObject2.toString(), FristGoodsBean.class);
                    goodList2.add(bean);
                }

                //LogUtil.i("666666666666666666666666");
                JSONArray goodJsonArray4 = jsonObject2.getJSONArray("2");
                goodList4 = new ArrayList<FristGoodsBean>();
                for (int i = 0; i < goodJsonArray4.length(); i++) {
                    JSONObject goodsObject4 = goodJsonArray4.getJSONObject(i);
                    FristGoodsBean bean = gson.fromJson(goodsObject4.toString(), FristGoodsBean.class);
                    goodList4.add(bean);
                }

                //LogUtil.i("7777777777777777777");
                JSONArray goodJsonArray3 = jsonObject.getJSONArray("list3");
                goodList3 = new ArrayList<FristGoodsBean>();
                for (int i = 0; i < goodJsonArray3.length(); i++) {
                    JSONObject goodsObject3 = goodJsonArray3.getJSONObject(i);
                    FristGoodsBean bean = gson.fromJson(goodsObject3.toString(), FristGoodsBean.class);
                    goodList3.add(bean);
                }
                //LogUtil.i("88888888888888888888");

                noticeList = new ArrayList<NoticeBean>();
                JSONArray noticeJsonArray = jsonObject.getJSONArray("notice");
                for (int i = 0; i < noticeJsonArray.length(); i++) {
                    JSONObject noticeJsonObject = noticeJsonArray.getJSONObject(i);
                    NoticeBean bean = gson.fromJson(noticeJsonObject.toString(), NoticeBean.class);
                    noticeList.add(bean);
                }
                LogUtil.i("999999999999999999");

                handler.obtainMessage(100, goodList).sendToTarget();
                handler.obtainMessage(200, goodList2).sendToTarget();
                handler.obtainMessage(300, goodList3).sendToTarget();
                handler.obtainMessage(400, goodList4).sendToTarget();
                handler.obtainMessage(500, noticeList).sendToTarget();

            }

//            ImagerLoaderUtil.displayImage(adList1.get(0).getImgurl() , iv_ad1);
//            ImagerLoaderUtil.displayImage(adList2.get(0).getImgurl() , iv_ad2);
//            ImagerLoaderUtil.displayImage(adList3.get(0).getImgurl() , iv_ad3);

//            ImagerLoaderUtil.displayImage(adList1.get(0).getImgurl(), iv_ad1);
            Glide.with(getActivity())
                    .load(adList1.get(0).getImgurl())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .crossFade()
                    .into(iv_ad1);

            iv_ad2.getSettings().setUseWideViewPort(true);
            iv_ad2.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
            iv_ad2.getSettings().setLoadWithOverviewMode(true);
            iv_ad2.setOverScrollMode(View.SCROLL_AXIS_NONE);
            iv_ad2.loadUrl(adList2.get(0).getImgurl());

            iv_ad3.getSettings().setUseWideViewPort(true);
            iv_ad3.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
            iv_ad3.getSettings().setLoadWithOverviewMode(true);
            iv_ad3.setOverScrollMode(View.SCROLL_AXIS_NONE);
            iv_ad3.loadUrl(adList3.get(0).getImgurl());

            adv_list = new ArrayList<ImageView>();
            for (int i = 0; i < bannerList.size(); i++) {
                final BannerBean enty = (BannerBean) bannerList.get(i);
                String url = enty.getImgurl();
                // final String link = enty.getAdlink();
                //显示广告图片
                ImageView image = new ImageView(getActivity());
                image.setScaleType(ImageView.ScaleType.FIT_XY);
                Glide.with(getActivity())
                        .load(url)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.drawable.advertisement_loading)
                        .into(image);
//                ImagerLoaderUtil.displayImageWithLoadingIcon(url, image, R.drawable.advertisement_loading);
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
            vp_shop_first.setAdapter(adapter);

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
            int currentIndex = vp_shop_first.getCurrentItem();
            if (currentIndex == adv_list.size() - 1) {
                currentIndex = 0;
            } else {
                currentIndex += 1;
            }
            vp_shop_first.setCurrentItem(currentIndex, false);
        }
    }


    private void notice(final List<NoticeBean> list) {
        new Thread(){
            @Override
            public void run() {
                while (index < list.size()){
                    synchronized (this) {
                        handler.sendEmptyMessage(NEWS_MESSAGE_TEXTVIEW);
                        SystemClock.sleep(5000);//每隔4秒滚动一次
                    }
                }
            }
        }.start();

        //new Thread(new MyRunnable()).start();
    }


    class MyRunnable implements Runnable {
        @Override
        public void run() {
            while (index < allNoticeList.size()) {
                synchronized (this) {
                    handler.sendEmptyMessage(NEWS_MESSAGE_TEXTVIEW);
                    SystemClock.sleep(5000);//每隔4秒滚动一次
                }
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (timer != null){
            timer.cancel();
        }
        if (timeTask != null){
            timeTask.cancel();
        }
        timer = null;
        timeTask = null;


        goods_List = null;
        goods_List2 = null;
        goods_List3 = null;
        goods_List4 = null;
        handler.removeCallbacksAndMessages(null);
    }
}
