package com.xinspace.csevent.monitor.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.xinspace.csevent.R;
import com.xinspace.csevent.adapter.AdvViewpagerAdapter;
import com.xinspace.csevent.app.CoresunApp;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.biz.GetAdvertisementBiz;
import com.xinspace.csevent.data.biz.GetDataBiz;
import com.xinspace.csevent.data.entity.AdvBean2;
import com.xinspace.csevent.login.activity.FeedbackActivity;
import com.xinspace.csevent.login.activity.LoginActivity;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.ui.activity.BaseActivity;
import com.xinspace.csevent.util.ImagerLoaderUtil;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.SharedPreferencesUtil1;
import com.xinspace.csevent.util.StatusBarUtils;
import com.xinspace.csevent.util.ToastUtil;
import com.xinspace.csevent.util.parser.JsonPaser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

import static com.xinspace.csevent.app.AppConfig.COMMUNITY_AREA;

/**
 * Created by Android on 2017/9/30.
 */

public class CommunityActivity extends BaseActivity {

    private List<ImageView> adv_list;//广告fragment集合
    private static ViewPager vp_adv;
    private CircleIndicator indicator;//指示器
    //重新获取地址的请求码
    private int REQUEST_CODE_RE_GET_ADDRESS = 200;
    //广告滚动
    private static final int HANDLER_SCROLL_ADVERTISEMENT = 10;
    private boolean scroll_flag = true;//标记变量,用于判断是否自动滚动广告
    private TextView tv_notice;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //广告滚动
                case HANDLER_SCROLL_ADVERTISEMENT:
                    scrollAdv();
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

    private LinearLayout ll_security;
    private LinearLayout ll_repairs; //报修
    private LinearLayout ll_payment; //社区缴费
    private LinearLayout ll_lease; // 租赁
    private LinearLayout ll_park; // 停车

    private LinearLayout ll_rim_shop; //周边商家
    private LinearLayout ll_feedback; // 意见反馈
    private LinearLayout ll_more; // 更多

    private SDPreference preference;
    private String uid;
    private TextSwitcher ts_notice;

    private RelativeLayout rel_notice;

    private int index = 0;
    public static final int NEWS_MESSAGE_TEXTVIEW = 200;//通知公告信息

    private String cToken , cUid;

    // 要显示的文本
    private List<String> newsList = new ArrayList<>();

    Handler noticeHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case NEWS_MESSAGE_TEXTVIEW:
                    ts_notice.setText(newsList.get(index));
                    index++;
                    if (index == newsList.size()) {
                        index = 0;
                    }
                    break;
                case 2000:
                    if (msg.obj != null ){
                        newsList.addAll((Collection<? extends String>) msg.obj);
                        notice(newsList);
                    }
                    break;
                default:
                    break;
            }
        }
    };
    private String area;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(this , R.color.app_bottom_color);
        setContentView(R.layout.community_activity);
        boolean isLogin = CoresunApp.isLogin;
        if (!isLogin){
            startActivity(new Intent(this, LoginActivity.class));
        }
        area = SharedPreferencesUtil1.getString(this, COMMUNITY_AREA, "");

        preference = SDPreference.getInstance();
        uid = preference.getContent("userId");

        cToken = preference.getContent("cToken");
        cUid = preference.getContent("cUid");

        getAdvertisement();
        initView();
        getNoticeData();
    }

    private void initView() {
        vp_adv = (ViewPager) findViewById(R.id.vp_ad);
        tv_notice = (TextView) findViewById(R.id.tv_notice);
        tv_notice.setSelected(true);

        rel_notice = (RelativeLayout) findViewById(R.id.rel_notice);
        rel_notice.setOnClickListener(onClickListener);

        ll_security = (LinearLayout) findViewById(R.id.ll_security);
        ll_security.setOnClickListener(onClickListener);

        ll_repairs = (LinearLayout) findViewById(R.id.ll_repairs);
        ll_payment = (LinearLayout) findViewById(R.id.ll_payment);
        ll_lease = (LinearLayout) findViewById(R.id.ll_lease);
        ll_park = (LinearLayout) findViewById(R.id.ll_park);
        ll_rim_shop = (LinearLayout) findViewById(R.id.ll_rim_shop);
        ll_feedback = (LinearLayout) findViewById(R.id.ll_feedback);
        ll_more = (LinearLayout) findViewById(R.id.ll_more);

        ll_repairs.setOnClickListener(onClickListener);
        ll_payment.setOnClickListener(onClickListener);
        ll_lease.setOnClickListener(onClickListener);
        ll_park.setOnClickListener(onClickListener);
        ll_rim_shop.setOnClickListener(onClickListener);
        ll_feedback.setOnClickListener(onClickListener);
        ll_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.makeToast("更多精彩内容，请期待");
            }
        });

        ts_notice = (TextSwitcher) findViewById(R.id.ts_notice);
        ts_notice.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView textView = new TextView(CommunityActivity.this);
                textView.setSingleLine();
                textView.setTextSize(16);//字号
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

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            uid = preference.getContent("userId");
            switch (v.getId()){
                case R.id.ll_security: // 安保门禁
                    if (!uid.equals("0")){
                        intent.setClass(CommunityActivity.this , OpenDoorActivity.class);
                    }else{
                        intent.setClass(CommunityActivity.this, LoginActivity.class);
                    }
                    break;
                case R.id.ll_repairs: // 报修

                    if (!uid.equals("0")){
                        intent.setClass(CommunityActivity.this, RepairsActivity.class);
                    }else{
                        intent.setClass(CommunityActivity.this , LoginActivity.class);
                    }

                    break;
                case R.id.ll_payment: //社区缴费

                    if (!uid.equals("0")){
                        intent.setClass(CommunityActivity.this, PaymentActivity.class);
                    }else{
                        intent.setClass(CommunityActivity.this, LoginActivity.class);
                    }

                    break;
                case R.id.ll_lease: // 租赁

                    if (!uid.equals("0")){
                        intent.setClass(CommunityActivity.this , LeaseActivity.class);
                    }else{
                        intent.setClass(CommunityActivity.this , LoginActivity.class);
                    }

                    break;
                case R.id.ll_park: //停车

                    if (!uid.equals("0")){
                        intent.setClass(CommunityActivity.this , ParkActivity.class);
                    }else{
                        intent.setClass(CommunityActivity.this , LoginActivity.class);
                    }

                    break;
                case R.id.ll_rim_shop: //周边商家

                    if (!uid.equals("0")){
                        intent.setClass(CommunityActivity.this, RimShopActivity.class);
                    }else{
                        intent.setClass(CommunityActivity.this, LoginActivity.class);
                    }

                    break;
                case R.id.ll_feedback: // 意见反馈

                    if (!uid.equals("0")){
                        intent.setClass(CommunityActivity.this, FeedbackActivity.class);
                    }else {
                        intent.setClass(CommunityActivity.this , LoginActivity.class);
                    }
                    break;
                case R.id.rel_notice: // 公告
                    if (!uid.equals("0")){
                        intent.setClass(CommunityActivity.this, AfficheAct.class);
                    }else {
                        intent.setClass(CommunityActivity.this , LoginActivity.class);
                    }
                    break;
            }
            startActivity(intent);
        }
    };

    //获取广告
    private void getAdvertisement() {
        GetAdvertisementBiz.getAdvs3(CommunityActivity.this, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                Log.i("www", "广告的数据" + result);
                if (result == null && result.equals("")){
                    return;
                }
                showAdvertisement(result);
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }

    //设置滚动广告栏的adapter
    private void showAdvertisement(String result) {
        try {
            JSONObject obj = new JSONObject(result);
            JSONArray ary = obj.getJSONArray("data");
            List<Object> advs = JsonPaser.parserAry(ary.toString(), AdvBean2.class);
            adv_list = new ArrayList<>();
            for (int i = 0; i < advs.size(); i++) {
                final AdvBean2 enty = (AdvBean2) advs.get(i);
                String url = enty.getPic();
                //final String link = enty.getAdlink();
                //显示广告图片
                ImageView image = new ImageView(CommunityActivity.this);
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
            vp_adv.setAdapter(adapter);

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
            int currentIndex = vp_adv.getCurrentItem();
            if (currentIndex == adv_list.size() - 1) {
                currentIndex = 0;
            } else {
                currentIndex += 1;
            }
            vp_adv.setCurrentItem(currentIndex, false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        timeTask.cancel();
        handler.removeCallbacksAndMessages(null);
    }


    private void getNoticeData() {
        GetDataBiz.getNoticeData(area, cUid, cToken, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                LogUtil.i("获取最新公告数据的result" + result);
                if (result == null && result.equals("")){
                    return;
                }

                JSONObject jsonObject = new JSONObject(result);
                List<String> noticeList = new ArrayList<String>();
                if (jsonObject.getString("code").equals("200")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        noticeList.add(jsonArray.getJSONObject(i).getString("title"));
                    }

                    LogUtil.i("noticeListsize" + noticeList.size());
                    noticeHandler.obtainMessage(2000,noticeList).sendToTarget();
                } else if (jsonObject.getString("code").equals("400")) {
                    LogUtil.i("没有公告数据");
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }


    private void notice(final List<String> list){
        new Thread(){
            @Override
            public void run() {
                //LogUtil.i("-------------------------------" + newsList.size());
                while (index < list.size()){
                    synchronized (this) {
                        noticeHandler.sendEmptyMessage(NEWS_MESSAGE_TEXTVIEW);
                        SystemClock.sleep(5000);//每隔4秒滚动一次
                    }
                }
            }
        }.start();
    }
}
