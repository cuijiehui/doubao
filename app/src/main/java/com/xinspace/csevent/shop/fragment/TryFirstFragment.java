package com.xinspace.csevent.shop.fragment;

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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.google.gson.Gson;
import com.xinspace.csevent.monitor.view.MyListView;
import com.xinspace.csevent.R;
import com.xinspace.csevent.adapter.AdvViewpagerAdapter;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.biz.GetDataBiz;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.shop.activity.TrialDetailAct;
import com.xinspace.csevent.shop.adapter.TryFristAdapter;
import com.xinspace.csevent.shop.adapter.TryHlAdapter;
import com.xinspace.csevent.shop.modle.BannerBean;
import com.xinspace.csevent.shop.modle.IsselectBean;
import com.xinspace.csevent.shop.modle.NoticeBean;
import com.xinspace.csevent.util.ImagerLoaderUtil;
import com.xinspace.csevent.util.LogUtil;

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
 * Created by Android on 2017/6/14.
 *
 * 试用首页
 */
public class TryFirstFragment extends Fragment{

    private View view;
    private TextSwitcher ts_trial;
    private RelativeLayout rel_notice;
    private MyListView lv_trial;
    private HorizontalListView hl_trial;
    private TryHlAdapter tryHlAdapter;
    private TryFristAdapter tryFristAdapter;

    private List<ImageView> adv_list;//广告fragment集合
    private static ViewPager vp_trial;
    private CircleIndicator indicator;//指示器
    //重新获取地址的请求码
    private int REQUEST_CODE_RE_GET_ADDRESS = 200;
    //广告滚动
    private static final int HANDLER_SCROLL_ADVERTISEMENT = 10;
    private boolean scroll_flag = true;//标记变量,用于判断是否自动滚动广告

    private List<NoticeBean> allNoticeList = new ArrayList<>();
    private List<IsselectBean> allIsselectList = new ArrayList<>();
    private List<IsselectBean> allIsHotList = new ArrayList<>();

    public static final int NEWS_MESSAGE_TEXTVIEW = 2000;//通知公告信息
    private int index = 0;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //广告滚动
                case HANDLER_SCROLL_ADVERTISEMENT:
                    scrollAdv();
                    break;
                case 200:
                    //公告
                    if (msg.obj != null){
                        allNoticeList.addAll((Collection<? extends NoticeBean>) msg.obj);
                        notice(allNoticeList);
                    }
                    break;
                case 300:
                    // 精选试用
                    if (msg.obj != null){
                        allIsselectList.addAll((Collection<? extends IsselectBean>) msg.obj);
                        tryFristAdapter.setList(allIsselectList);
                        tryFristAdapter.notifyDataSetChanged();
                    }
                    break;
                case 400:
                    //热门试用
                    if (msg.obj != null){
                        allIsHotList.addAll((Collection<? extends IsselectBean>) msg.obj);
                        tryHlAdapter.setList(allIsHotList);
                        tryHlAdapter.notifyDataSetChanged();
                    }
                    break;
                case NEWS_MESSAGE_TEXTVIEW:
                    ts_trial.setText(allNoticeList.get(index).getTitle());
                    index++;
                    if (index == allNoticeList.size()) {
                        index = 0;
                    }
                    break;
            }
        }
    };
    private Timer timer = new Timer();
    private boolean isFirst = true; //第一次设置indicator
    private TimerTask timeTask = new TimerTask() {
        @Override
        public void run() {
            handler.sendEmptyMessage(HANDLER_SCROLL_ADVERTISEMENT);
        }
    };
    private SDPreference preference;
    private String openId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_try_first , null);

        preference = SDPreference.getInstance();
        openId = preference.getContent("openid");

        initView();
        initData();

        return view;
    }

    private void initData() {
        GetDataBiz.getFristTryData(openId , new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                LogUtil.i("免费试用首页" + result);
                if (result == null || result.equals("")){
                    return;
                }

                Gson gson = new Gson();
                JSONObject obj = new JSONObject(result);
                List<BannerBean> bannerList = null;
                List<NoticeBean> noticeList = null;
                List<IsselectBean> isselectList = null;
                List<IsselectBean> isHotList = null;

                if (obj.getInt("code") == 200) {
                    JSONObject jsonObject = obj.getJSONObject("data");
                    bannerList = new ArrayList<BannerBean>();
                    noticeList = new ArrayList<NoticeBean>();
                    isselectList = new ArrayList<IsselectBean>();
                    isHotList = new ArrayList<IsselectBean>();

                    JSONArray bannerJsonArray = jsonObject.getJSONArray("adv");
                    for (int i = 0; i < bannerJsonArray.length(); i++) {
                        JSONObject bannerObject = bannerJsonArray.getJSONObject(i);
                        BannerBean bannerBean =  gson.fromJson(bannerObject.toString(), BannerBean.class);
                        bannerList.add(bannerBean);
                    }

                    JSONArray noticeJsonArray = jsonObject.getJSONArray("notice");
                    for (int i = 0; i < noticeJsonArray.length(); i++) {
                        JSONObject noticeObject = noticeJsonArray.getJSONObject(i);
                        NoticeBean noticeBean =  gson.fromJson(noticeObject.toString(), NoticeBean.class);
                        noticeList.add(noticeBean);
                    }

                    JSONArray isselectArray = jsonObject.getJSONArray("isselect");
                    for (int i = 0; i < isselectArray.length(); i++) {
                        JSONObject isselectObject = isselectArray.getJSONObject(i);
                        IsselectBean isselectBean =  gson.fromJson(isselectObject.toString(), IsselectBean.class);
                        isselectList.add(isselectBean);
                    }

                    JSONArray isHotArray = jsonObject.getJSONArray("ishot");
                    for (int i = 0; i < isHotArray.length(); i++) {
                        JSONObject isHotObject = isHotArray.getJSONObject(i);
                        IsselectBean isselectBean =  gson.fromJson(isHotObject.toString(), IsselectBean.class);
                        isHotList.add(isselectBean);
                    }

                    handler.obtainMessage(200 , noticeList).sendToTarget();
                    handler.obtainMessage(300 , isselectList).sendToTarget();
                    handler.obtainMessage(400 , isHotList).sendToTarget();
                    LogUtil.i("-------------------------------------------------------");

                    adv_list = new ArrayList<ImageView>();
                    for (int i = 0; i < bannerList.size(); i++) {
                        final BannerBean enty = (BannerBean) bannerList.get(i);
                        String url = enty.getImgurl();
                        // final String link = enty.getAdlink();
                        //显示广告图片
                        ImageView image = new ImageView(getActivity());
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
                    vp_trial.setAdapter(adapter);

                    //设置指示器
                    if (isFirst) {
                        isFirst = false;
                        // indicator.setViewPager(vp_adv);
                        //滚动广告
                        timer.schedule(timeTask, 5000, 5000);
                    }
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }

    private void initView() {

        vp_trial = (ViewPager) view.findViewById(R.id.vp_trial);
        vp_trial.setFocusable(true);  //焦点在ViewPager上

        rel_notice = (RelativeLayout) view.findViewById(R.id.rel_notice);
        rel_notice.setOnClickListener(onClickListener);

        ts_trial = (TextSwitcher) view.findViewById(R.id.ts_first_shop);
        ts_trial.setFactory(new ViewSwitcher.ViewFactory() {
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

        hl_trial = (HorizontalListView) view.findViewById(R.id.hl_trial);
        hl_trial.setFocusable(false);
        tryHlAdapter = new TryHlAdapter(getActivity());
        tryHlAdapter.setList(allIsHotList);
        hl_trial.setAdapter(tryHlAdapter);
        hl_trial.setOnItemClickListener(onItemClickListener);

        lv_trial = (MyListView) view.findViewById(R.id.lv_trial);
        lv_trial.setFocusable(false);
        tryFristAdapter = new TryFristAdapter(getActivity());
        tryFristAdapter.setList(allIsselectList);
        lv_trial.setAdapter(tryFristAdapter);
    }

    //设置广告页
    private void scrollAdv() {
        if (scroll_flag) {
            int currentIndex = vp_trial.getCurrentItem();
            if (currentIndex == adv_list.size() - 1) {
                currentIndex = 0;
            } else {
                currentIndex += 1;
            }
            vp_trial.setCurrentItem(currentIndex, false);
        }
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            String success = allIsHotList.get(position).getSuccess();
            String goodId =  allIsHotList.get(position).getId();
            String url = allIsHotList.get(position).getUrl();
            Intent intent = new Intent(getActivity() , TrialDetailAct.class);
            intent.putExtra("id" , goodId);
            intent.putExtra("success" , success);
            intent.putExtra("url" , url);
            startActivity(intent);
        }
    };

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.rel_notice:  //点击公告跳转
                    Intent intent = new Intent(getActivity() , TrialDetailAct.class);
                    intent.putExtra("id" , allNoticeList.get(index).getGid());
                    intent.putExtra("success" , allNoticeList.get(index).getStatus());
                    intent.putExtra("url" , allNoticeList.get(index).getUrl());
                    startActivity(intent);
                    break;
            }
        }
    };

    private void notice(final List<NoticeBean> list){
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
    }

}
