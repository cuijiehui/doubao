package com.xinspace.csevent.shop.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xinspace.csevent.monitor.view.MyListView;
import com.xinspace.csevent.R;
import com.xinspace.csevent.adapter.AdvViewpagerAdapter;
import com.xinspace.csevent.app.utils.ScreenUtils;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.biz.GetDataBiz;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.shop.adapter.TeamsAdapter;
import com.xinspace.csevent.shop.modle.GroupDetailBean;
import com.xinspace.csevent.shop.modle.TreamBean;
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
 * 拼团详情
 *
 * Created by Android on 2017/3/20.
 */

public class GroupGoodsDetailAct extends BaseActivity{

    private LinearLayout ll_shop_detail_back;
    private String goodsId;
    private Intent intent;
    private ViewPager vp_goods_detail;
    private CircleIndicator circleIndicator;
    private int screenWidth;
    private GroupDetailBean goodsDetail;

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
    private WebView web_img;

    private RelativeLayout rel_bottom;
    private List<String> urlList;

    private SDPreference preference;
    private String userId;
    private String openid;

    private LinearLayout lin_go_buy;
    private LinearLayout lin_go_group;
    private TextView tv_go_buy;
    private TextView tv_go_group;
    private MyListView lv_teams;
    private LinearLayout lin_teams;
    private List<TreamBean> allTreamList = new ArrayList<TreamBean>();
    private TeamsAdapter teamsAdapter;
    private TextView tv_team_num;
    private boolean isCanBuy;
    private ImageView iv_go_home;
    private RelativeLayout rel_data_load;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 100:
                    rel_data_load.setVisibility(View.GONE);
                    if (msg.obj != null){
                        goodsDetail = (GroupDetailBean) msg.obj;
                        tv_goods_title.setText("[两人成团]" + goodsDetail.getTitle());
                        tv_goods_price.setText("¥" + goodsDetail.getGroupsprice());
                        tv_goods_price2.setText( "原价" + goodsDetail.getPrice());
                        tv_goods_price2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                        tv_goods_buy_num.setText("销量" + goodsDetail.getSales());
                        tv_team_num.setText("已有" + goodsDetail.getTeamnum()+ "人成团");
                        tv_go_buy.setText("¥" + goodsDetail.getSingleprice());
                        tv_go_group.setText("¥" + goodsDetail.getGroupsprice());
                        scrollAd();
                    }
                    break;
                case 101:
                    rel_data_load.setVisibility(View.GONE);
                    LogUtil.i("数据获取失败");
                    break;
                case HANDLER_SCROLL_ADVERTISEMENT:
                    scrollAdv();
                    break;
                case 200:
                    if (msg.obj != null){
                        lin_teams.setVisibility(View.VISIBLE);
                        allTreamList.addAll((Collection<? extends TreamBean>) msg.obj);
                        teamsAdapter.setGoodsDetail(goodsDetail);
                        teamsAdapter.setTeamList(allTreamList);
                        teamsAdapter.setCanBuy(isCanBuy);
                        teamsAdapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(GroupGoodsDetailAct.this , R.color.app_bottom_color);

        setContentView(R.layout.act_group_detail_web);
        preference = SDPreference.getInstance();
        openid = preference.getContent("openid");
        userId = preference.getContent("userId");
        intent = getIntent();
        goodsId = intent.getStringExtra("goodId");
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView() {
        ll_shop_detail_back = (LinearLayout) findViewById(R.id.ll_shop_detail_back);
        ll_shop_detail_back.setOnClickListener(onclickListener);

        iv_go_home = (ImageView) findViewById(R.id.iv_go_home);
        iv_go_home.setOnClickListener(onclickListener);

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
        tv_team_num = (TextView) findViewById(R.id.tv_team_num);

        web_img = (WebView) findViewById(R.id.web_img);
        web_img.getSettings().setUseWideViewPort(true);
        web_img.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        web_img.getSettings().setLoadWithOverviewMode(true);
        web_img.setOverScrollMode(View.SCROLL_AXIS_NONE);

        rel_bottom = (RelativeLayout) findViewById(R.id.rel_bottom);

        lin_go_buy = (LinearLayout) findViewById(R.id.lin_go_buy);
        lin_go_group = (LinearLayout) findViewById(R.id.lin_go_group);

        lin_go_buy.setOnClickListener(onclickListener);
        lin_go_group.setOnClickListener(onclickListener);

        tv_go_buy = (TextView) findViewById(R.id.tv_go_buy);
        tv_go_group = (TextView) findViewById(R.id.tv_go_group);

        lin_teams = (LinearLayout) findViewById(R.id.lin_teams);
        lin_teams.setVisibility(View.GONE);
        lv_teams = (MyListView) findViewById(R.id.lv_teams);
        teamsAdapter = new TeamsAdapter(this);
        teamsAdapter.setTeamList(allTreamList);
        lv_teams.setAdapter(teamsAdapter);
    }


    View.OnClickListener onclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_shop_detail_back:
                    GroupGoodsDetailAct.this.finish();

                    break;
                case R.id.lin_go_buy:   //直接购买
                    if (isCanBuy){
                        Intent intent = new Intent(GroupGoodsDetailAct.this , BuyGroupAct.class);
                        intent.putExtra("bean" , goodsDetail);
                        intent.putExtra("type" , "single");
                        startActivity(intent);
                    }else{
                       ToastUtil.makeToast("您已经参与了该团，请等待拼团结束后再进行购买！");
                    }
                    break;
                case R.id.lin_go_group:  //去开团
                    if (isCanBuy){
                        Intent intent1 = new Intent(GroupGoodsDetailAct.this , BuyGroupAct.class);
                        intent1.putExtra("bean" , goodsDetail);
                        intent1.putExtra("type" , "groups");
                        intent1.putExtra("teamid" , "");
                        startActivity(intent1);
                    }else{
                        ToastUtil.makeToast("您已经参与了该团，请等待拼团结束后再进行购买！");
                    }
                    break;
                case R.id.iv_go_home:

                    GroupGoodsDetailAct.this.finish();

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


    private void scrollAd(){
        urlList = goodsDetail.getThumb_url();
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


    private void initData() {
        rel_data_load.setVisibility(View.VISIBLE);

        GetDataBiz.getGroupIsBuy(goodsId, openid, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                if(result == null || result.equals("")){
                    return;
                }

                Log.i("www", "获取商品是否可以购买" + result);
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("code").equals("200")){
                    isCanBuy = true;
                }else if (jsonObject.getString("code").equals("400")){
                    isCanBuy = false;
                }
                initGoodsData();
            }

            @Override
            public void onHttpRequestError(String error) {
                rel_data_load.setVisibility(View.GONE);
            }
        });
    }

    private void initGoodsData(){
        GetDataBiz.getGroupDetail(goodsId , openid, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                Log.i("www", "拼团详情数据" + result);
                if(result == null || result.equals("")){
                    return;
                }

                try {
                    if (result != null){
                        List<String> imgUrlList = new ArrayList<String>();

                        JSONObject jsonObject = new JSONObject(result);
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");

                        JSONObject goodsJsonobject = jsonObject1.getJSONObject("goods");
                        Gson gson=new Gson();
                        GroupDetailBean bean = gson.fromJson(goodsJsonobject.toString(), GroupDetailBean.class);
                        LogUtil.i("------------------------------");
                        JSONArray jsonArray = goodsJsonobject.getJSONArray("thumb_url");
                        for(int i = 0 ; i < jsonArray.length() ; i++){
                            imgUrlList.add(jsonArray.getString(i));
                        }
                        LogUtil.i("图片数组的长度" + imgUrlList.size());
                        bean.setThumb_url(imgUrlList);

                        List<TreamBean> teamList = new ArrayList<TreamBean>();
                        JSONArray treamsJsonArray = jsonObject1.getJSONArray("teams");
                        for (int i = 0 ; i < treamsJsonArray.length() ; i++){
                            TreamBean treamBean = gson.fromJson(treamsJsonArray.getJSONObject(i).toString() , TreamBean.class);
                            teamList.add(treamBean);
                        }
                        LogUtil.i("-----------teamList.size()----------------------" + teamList.size());

                        handler.obtainMessage(100 , bean).sendToTarget();
                        if (teamList.size() != 0){
                            handler.obtainMessage(200 , teamList).sendToTarget();
                        }
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
        urlList = null;
        allTreamList = null;
        web_img = null;
        ImagerLoaderUtil.clearImageMemory();
        handler.removeCallbacksAndMessages(null);
        teamsAdapter.onDestroy();
//        if (teamsAdapter != null){
//            teamsAdapter = null;
//        }
        this.setContentView(R.layout.empty_view);
        System.gc();
        super.onDestroy();
    }


    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        GroupGoodsDetailAct.this.finish();
    }
}
