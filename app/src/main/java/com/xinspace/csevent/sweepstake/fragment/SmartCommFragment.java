//package com.xinspace.csevent.sweepstake.fragment;
//
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.support.annotation.Nullable;
//import android.support.design.widget.TabLayout;
//import android.support.v4.app.Fragment;
//import android.support.v4.view.ViewPager;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//
//import com.xinspace.csevent.monitor.fragment.SecurityFragment;
//import com.xinspace.csevent.R;
//import com.xinspace.csevent.adapter.AdvViewpagerAdapter;
//import com.xinspace.csevent.data.biz.GetAdvertisementBiz;
//import com.xinspace.csevent.data.entity.ActivityListEntity;
//import com.xinspace.csevent.data.entity.AdvEntity;
//import com.xinspace.csevent.myinterface.HttpRequestListener;
//import com.xinspace.csevent.util.parser.JsonPaser;
//import com.xinspace.csevent.sweepstake.adapter.FragmentViewPagerAdapter;
//import com.xinspace.csevent.util.ImagerLoaderUtil;
//import com.xinspace.csevent.util.LogUtil;
//import com.xinspace.csevent.ui.activity.ActDetailActivity;
//import com.xinspace.csevent.ui.activity.AwardPoolActivity;
//import com.xinspace.csevent.ui.activity.WebViewActivity;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Timer;
//import java.util.TimerTask;
//import java.util.Vector;
//
//import me.relex.circleindicator.CircleIndicator;
//
///**
// * Created by Android on 2016/12/8.
// */
//public class SmartCommFragment extends Fragment{
//
//    private View view;
//    private ViewPager vp_viewPager;
//    private ViewPager vp_content;
//    private SecurityFragment securityFragment;
//    private ManageFragment manageFragment;
//    private CommunityFragment communityFragment;
//    private List<Fragment> fragments = new Vector<>();
//    private CircleIndicator indicator;
//    private List<ImageView> adv_list;//广告fragment集合
//    private boolean scroll_flag = true;
//    private Timer timer = new Timer();
//    private boolean isFirst = true;//第一次设置indicator
//    private TabLayout mTabLayout;
//
//    private int REQUEST_CODE_RE_GET_ADDRESS = 200;
//    //广告滚动
//    private static final int HANDLER_SCROLL_ADVERTISEMENT = 10;
//    private Timer updateTimer = new Timer();
//
//    private TimerTask timeTask = new TimerTask() {
//        @Override
//        public void run() {
//            handler.sendEmptyMessage(HANDLER_SCROLL_ADVERTISEMENT);
//        }
//    };
//
//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                //广告滚动
//                case HANDLER_SCROLL_ADVERTISEMENT:
//                    scrollAdv();
//                    break;
//            }
//        }
//    };
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.fragment_smartcomm , null);
//        initView();
//        getAdvertisement();
//        return view;
//    }
//
//    private void initView() {
//
//        vp_viewPager = (ViewPager) view.findViewById(R.id.vp_viewPager);
//        indicator = (CircleIndicator) view.findViewById(R.id.indicator);
//        vp_content = (ViewPager) view.findViewById(R.id.vp_content);
//        mTabLayout = (TabLayout) view.findViewById(R.id.tabs);
//
//        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
//        mTabLayout.addTab(mTabLayout.newTab().setText("安保门禁"));
//        //mTabLayout.addTab(mTabLayout.newTab().setText("智慧社区"));
//        mTabLayout.addTab(mTabLayout.newTab().setText("智慧物管"));
//
//        securityFragment = new SecurityFragment();
//        fragments.add(securityFragment);
////        communityFragment = new CommunityFragment();
////        fragments.add(communityFragment);
//        manageFragment = new ManageFragment();
//        fragments.add(manageFragment);
//
//
//        vp_content.setAdapter(new FragmentViewPagerAdapter(getActivity().getSupportFragmentManager(), fragments));
//        vp_content.setCurrentItem(0);
//
//        TabLayout.TabLayoutOnPageChangeListener listener = new TabLayout.TabLayoutOnPageChangeListener(mTabLayout);
//        vp_content.addOnPageChangeListener(listener);
//        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                vp_content.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//
//
//    }
//
//
//    //获取广告
//    private void getAdvertisement() {
//
//        GetAdvertisementBiz.getAdvs(getActivity(), new HttpRequestListener() {
//            @Override
//            public void onHttpRequestFinish(String result) throws JSONException {
//                Log.i("www", "广告的数据" + result);
//                if (result == null && result.equals("")){
//                    return;
//                }
//
//                showAdvertisement(result);
//            }
//
//            @Override
//            public void onHttpRequestError(String error) {
//
//            }
//        });
//    }
//
//    //设置滚动广告栏的adapter
//    private void showAdvertisement(String result) {
//        try {
//            JSONObject obj = new JSONObject(result);
//            JSONArray ary = obj.getJSONArray("data");
//            List<Object> advs = JsonPaser.parserAry(ary.toString(), AdvEntity.class);
//            adv_list = new ArrayList<>();
//            for (int i = 0; i < advs.size(); i++) {
//                final AdvEntity enty = (AdvEntity) advs.get(i);
//                String url = enty.getAddress();
//                final String link = enty.getAdlink();
//                //显示广告图片
//                ImageView image = new ImageView(getActivity());
//                image.setScaleType(ImageView.ScaleType.FIT_XY);
//                ImagerLoaderUtil.displayImageWithLoadingIcon(url, image, R.drawable.advertisement_loading);
//                //设置监听器
//                image.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (enty.getType() != null){
//                            clickIntoAdvertisement(link, enty.getType());
//                        }
//                    }
//                });
//                adv_list.add(image);
//            }
//            //设置适配器
//            AdvViewpagerAdapter adapter = new AdvViewpagerAdapter(adv_list);
//            vp_viewPager.setAdapter(adapter);
//
//            //设置指示器
//            if (isFirst) {
//                isFirst = false;
//                indicator.setViewPager(vp_viewPager);
//                //滚动广告
//                timer.schedule(timeTask, 5000, 5000);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    //设置广告页
//    private void scrollAdv() {
//        if (scroll_flag) {
//            int currentIndex = vp_viewPager.getCurrentItem();
//            if (currentIndex == adv_list.size() - 1) {
//                currentIndex = 0;
//            } else {
//                currentIndex += 1;
//            }
//            vp_viewPager.setCurrentItem(currentIndex, false);
//        }
//    }
//
//    //点击广告进入相应的页面或者活动
//    private void clickIntoAdvertisement(String link, String type) {
//        if (type.equals("0")) {
//            //进入一个html静态页面
//            Intent intent = new Intent(getActivity(), WebViewActivity.class);
//            intent.putExtra("data", link);
//            startActivity(intent);
//        } else if (type.equals("1")) {
//            //进入普通抽奖页面
//            //随机得到的活动id
//            int index = link.lastIndexOf("_");
//            String act_id = link.substring(index + 1);
//            ActivityListEntity enty = new ActivityListEntity();
//            enty.setId(Integer.parseInt(act_id));
//
//            Intent intent = new Intent(getActivity(), ActDetailActivity.class);
//            intent.putExtra("data", enty);
//            startActivity(intent);
//            LogUtil.i("获取到的活动的id:" + act_id);
//        } else if (type.equals("4")) {
//            //进入抽奖池抽奖页面
//            int index = link.lastIndexOf("_");
//            String act_id = link.substring(index + 1);
//            ActivityListEntity enty = new ActivityListEntity();
//            enty.setId(Integer.parseInt(act_id));
//
//            Intent intent = new Intent(getActivity(), AwardPoolActivity.class);
//            intent.putExtra("data", enty);
//            startActivity(intent);
//            LogUtil.i("获取到的活动的id:" + act_id);
//        }
//    }
//
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        //取消计时器
//        cancelTimer();
//    }
//
//    //取消timer
//    private void cancelTimer() {
//        if (null != timer) {
//            timer.cancel();
//            timer = null;
//        }
//        if (null != updateTimer) {
//            updateTimer.cancel();
//            updateTimer = null;
//        }
//    }
//
//}
