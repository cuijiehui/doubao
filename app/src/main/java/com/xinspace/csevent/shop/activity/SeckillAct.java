package com.xinspace.csevent.shop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
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
import com.xinspace.csevent.data.entity.TimeLeftEntity;
import com.xinspace.csevent.login.activity.LoginActivity;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.parser.JsonPaser;
import com.xinspace.csevent.shop.adapter.SeckillGoodsAdapter;
import com.xinspace.csevent.shop.adapter.SeckillTimeAdapter;
import com.xinspace.csevent.shop.modle.SeckillAdBean;
import com.xinspace.csevent.shop.modle.SeckillGoodsBean;
import com.xinspace.csevent.shop.modle.SeckillTimeBean;
import com.xinspace.csevent.util.ImagerLoaderUtil;
import com.xinspace.csevent.util.LogUtil;
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
import sdk_sample.sdk.views.HorizontalListView;

/**
 * 秒杀列表界面
 * <p>
 * Created by Android on 2017/6/7.
 */

public class SeckillAct extends BaseActivity {

    private LinearLayout ll_seckill_back;
    private ViewPager vp_seckill_jiu;
    private List<ImageView> adv_list;//广告fragment集合
    private CircleIndicator indicator;//指示器
    //重新获取地址的请求码
    private int REQUEST_CODE_RE_GET_ADDRESS = 200;
    //广告滚动
    private static final int HANDLER_SCROLL_ADVERTISEMENT = 10;
    private boolean scroll_flag = true;//标记变量,用于判断是否自动滚动广告
    private List<SeckillTimeBean> allTimeList = new ArrayList<>();
    private List<SeckillGoodsBean> allBeanList = new ArrayList<>();
    private MyListView lv_seckill;
    private String timeId;
    private String uniacid;
    private String taskid;
    private String status;

    private long startTime;
    private long endTime;
    private long nowTime;
    private long timeCha;
    private String userId;
    private RelativeLayout rel_data_load;

    private Timer timer1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //广告滚动
                case HANDLER_SCROLL_ADVERTISEMENT:
                    scrollAdv();
                    break;
                case 200:

                    if (msg.obj != null) {
                        allTimeList.addAll((Collection<? extends SeckillTimeBean>) msg.obj);
                        seckillTimeAdapter.notifyDataSetChanged();

                        timeId = allTimeList.get(0).getId();
                        uniacid = allTimeList.get(0).getUniacid();
                        taskid = allTimeList.get(0).getTaskid();
                        status = allTimeList.get(0).getStatus();

                        startTime = Long.valueOf(allTimeList.get(0).getStarttime());
                        endTime = Long.valueOf(allTimeList.get(0).getEndtime());

                        if (status.equals("0")){
                            timeCha = endTime - nowTime;
                            LogUtil.i("0timeCha" + timeCha);
                        }else if(status.equals("1")){
                            timeCha = startTime - nowTime;
                            LogUtil.i("1timeCha1" + timeCha);
                        }
                        startTask();
                        getSeckillData();
                    }
                    break;
                case 2000:
                    rel_data_load.setVisibility(View.GONE);
                    if (msg.obj != null) {
                        allBeanList.addAll((Collection<? extends SeckillGoodsBean>) msg.obj);
                        goodsAdapter.setStatus(status);
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
    private HorizontalListView lv_time;
    private SeckillTimeAdapter seckillTimeAdapter;
    private TextView tv_seckill_state;
    private TextView tv_seckill_time;
    private TextView tv_seckill_state1;

    private SeckillGoodsAdapter goodsAdapter;
    private SDPreference preference;
    private ImageView iv_main_cart;
    private TextView tv_shopcart_size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_seckill);
        StatusBarUtils.setWindowStatusBarColor(SeckillAct.this , R.color.app_bottom_color);
        preference = SDPreference.getInstance();
        userId = preference.getContent("userId");

        initView();
        initData();
    }

    private void initData() {

        if (allTimeList.size() != 0){
            allTimeList.clear();
        }

        GetDataBiz.seckillTimeData(new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                LogUtil.i("秒杀时间返回" + result);
                if (result == null || result.equals("")){
                    return;
                }
                Gson gson = new Gson();
                JSONObject jsonObject = new JSONObject(result);
                List<SeckillTimeBean> timeList = new ArrayList<SeckillTimeBean>();
                if (jsonObject.getString("code").equals("200")) {
                    JSONObject dataJsonObject = jsonObject.getJSONObject("data");

                    nowTime = Long.valueOf(dataJsonObject.getString("now"));

                    JSONArray adJsonArray = dataJsonObject.getJSONArray("advs");
                    List<Object> advs = JsonPaser.parserAry(adJsonArray.toString(), SeckillAdBean.class);
                    adv_list = new ArrayList<>();
                    for (int i = 0; i < advs.size(); i++) {
                        final SeckillAdBean enty = (SeckillAdBean) advs.get(i);
                        String url = enty.getThumb();
                        //final String link = enty.getAdlink();
                        //显示广告图片
                        ImageView image = new ImageView(SeckillAct.this);
                        image.setScaleType(ImageView.ScaleType.FIT_XY);
                        ImagerLoaderUtil.displayImageWithLoadingIcon(url, image, R.drawable.advertisement_loading);
                        //设置监听器
                        adv_list.add(image);
                    }
                    //设置适配器
                    AdvViewpagerAdapter adapter = new AdvViewpagerAdapter(adv_list);
                    vp_seckill_jiu.setAdapter(adapter);

                    //设置指示器
                    if (isFirst) {
                        isFirst = false;
                        // indicator.setViewPager(vp_adv);
                        //滚动广告
                        timer.schedule(timeTask, 5000, 5000);
                    }

                    JSONArray timeJsonArray = dataJsonObject.getJSONArray("times");
                    for (int i = 0; i < timeJsonArray.length(); i++) {
                        JSONObject timeJsonObject = timeJsonArray.getJSONObject(i);
                        SeckillTimeBean bean = gson.fromJson(timeJsonObject.toString(), SeckillTimeBean.class);
                        timeList.add(bean);
                    }
                    handler.obtainMessage(200, timeList).sendToTarget();
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }

    private void initView() {

        ll_seckill_back = (LinearLayout) findViewById(R.id.ll_seckill_back);
        ll_seckill_back.setOnClickListener(clickListener);

        rel_data_load = (RelativeLayout) findViewById(R.id.rel_data_load);

        iv_main_cart = (ImageView) findViewById(R.id.iv_main_cart);
        iv_main_cart.setOnClickListener(clickListener);

        tv_shopcart_size = (TextView) findViewById(R.id.tv_shopcart_size);
        if (!userId.equals("0")){
            tv_shopcart_size.setText(preference.getContent("cartSize"));
        }else{
            tv_shopcart_size.setText("0");
        }

        vp_seckill_jiu = (ViewPager) findViewById(R.id.vp_seckill_jiu);

        lv_time = (HorizontalListView) findViewById(R.id.lv_time);
        seckillTimeAdapter = new SeckillTimeAdapter(SeckillAct.this);
        seckillTimeAdapter.setTimesList(allTimeList);
        lv_time.setAdapter(seckillTimeAdapter);
        lv_time.setFocusable(false);
        lv_time.setOnItemClickListener(onItemClickListener);

        tv_seckill_time = (TextView) findViewById(R.id.tv_seckill_time);
        tv_seckill_state = (TextView) findViewById(R.id.tv_seckill_state);
        tv_seckill_state1 = (TextView) findViewById(R.id.tv_seckill_state1);

        lv_seckill = (MyListView) findViewById(R.id.lv_seckill);
        goodsAdapter = new SeckillGoodsAdapter(SeckillAct.this);
        goodsAdapter.setList(allBeanList);
        lv_seckill.setAdapter(goodsAdapter);
        lv_seckill.setOnItemClickListener(onItemClickListener2);
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            seckillTimeAdapter.setSelectItem(position);
            seckillTimeAdapter.notifyDataSetChanged();

            timeId = allTimeList.get(position).getId();
            uniacid = allTimeList.get(position).getUniacid();
            taskid = allTimeList.get(position).getTaskid();
            status = allTimeList.get(position).getStatus();
            getSeckillData();
        }
    };

    AdapterView.OnItemClickListener onItemClickListener2 = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(SeckillAct.this , SeckillDetailAct.class);

            LogUtil.i("goodsId" +  allBeanList.get(position).getGoodsid() + "timeId" + timeId + "taskid" + taskid );

            intent.putExtra("goodsId" , allBeanList.get(position).getGoodsid());
            intent.putExtra("timeId" ,timeId);
            intent.putExtra("taskid" , taskid);
            startActivity(intent);
        }
    };

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_seckill_back:

                    finish();

                    break;
                case R.id.iv_main_cart:

                    userId = preference.getContent("userId");
                    if (userId.equals("0")){
                        Intent intent4 = new Intent(SeckillAct.this , LoginActivity.class);
                        startActivity(intent4);
                    }else{
                        Intent intent4 = new Intent(SeckillAct.this , ShopCartAct2.class);
                        startActivity(intent4);
                    }

                    break;

            }
        }
    };

    //设置广告页
    private void scrollAdv() {
        if (scroll_flag) {
            int currentIndex = vp_seckill_jiu.getCurrentItem();
            if (currentIndex == adv_list.size() - 1) {
                currentIndex = 0;
            } else {
                currentIndex += 1;
            }
            vp_seckill_jiu.setCurrentItem(currentIndex, false);
        }
    }

    private void getSeckillData() {

        rel_data_load.setVisibility(View.VISIBLE);

        if (allBeanList.size() != 0){
            allBeanList.clear();
        }

        GetDataBiz.seckillListData(timeId, taskid, timeId, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                LogUtil.i("秒杀商品列表" + result);
                if (result == null || result.equals("")){
                    return;
                }

                Gson gson = new Gson();
                JSONObject jsonObject = new JSONObject(result);
                List<SeckillGoodsBean> beanList = new ArrayList<SeckillGoodsBean>();
                if (jsonObject.getString("code").equals("200")) {
                    JSONArray dataJsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < dataJsonArray.length(); i++) {
                        SeckillGoodsBean bean = gson.fromJson(dataJsonArray.getJSONObject(i).toString(), SeckillGoodsBean.class);
                        beanList.add(bean);
                    }
                    handler.obtainMessage(2000, beanList).sendToTarget();
                } else {
                    rel_data_load.setVisibility(View.GONE);
                }
            }

            @Override
            public void onHttpRequestError(String error) {
                rel_data_load.setVisibility(View.GONE);
            }
        });
    }

    private void updateTime(long time ,String status){

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

        if (status.equals("0")){
            tv_seckill_state.setText("抢购中 先下单先得哦");
            tv_seckill_state1.setText("距本场结束还有");
        }else if(status.equals("1")){
            tv_seckill_state.setText("即将开始 先下单先得哦");
            tv_seckill_state1.setText("距下场开始还有");
        }
        tv_seckill_time.setText(hours +":" + mins + ":" + sec);

//        LogUtil.i("day:" + day);
//        LogUtil.i("hour:" + hours);
//        LogUtil.i("min:" + mins);
//        LogUtil.i("sec:" + sec);
    }


    //更新时间 timerTask
    private void startTask(){
        timer1 = new Timer();
        TimerTask timeTask1 = new TimerTask() {
            @Override
            public void run() {
                if (timeCha > 0) {
                    timeCha -= 1;
                    //主线程中更新UI
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateTime(timeCha , status);
                        }
                    });
                }else if (timeCha == 0){
                    initData();
                    timer1 = null;
                    return;
                }
            }
        };
        timer1.schedule(timeTask1, 0, 1000);
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
        this.setContentView(R.layout.empty_view);
        SeckillAct.this.finish();
        super.onDestroy();
    }
}
