//package com.xinspace.csevent.sweepstake.activity;
//
//import android.content.Intent;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.support.design.widget.CollapsingToolbarLayout;
//import android.support.design.widget.CoordinatorLayout;
//import android.support.design.widget.TabLayout;
//import android.support.v4.app.Fragment;
//import android.support.v4.view.ViewPager;
//import android.support.v7.widget.Toolbar;
//import android.util.Log;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.xinspace.csevent.R;
//import com.xinspace.csevent.adapter.AdvViewpagerAdapter;
//import com.xinspace.csevent.app.CoresunApp;
//import com.xinspace.csevent.app.utils.ScreenUtils;
//import com.xinspace.csevent.data.biz.GetAdvertisementBiz;
//import com.xinspace.csevent.data.entity.ActivityListEntity;
//import com.xinspace.csevent.data.entity.AdvEntity;
//import com.xinspace.csevent.myinterface.HttpRequestListener;
//import com.xinspace.csevent.util.parser.JsonPaser;
//import com.xinspace.csevent.sweepstake.adapter.FragmentViewPagerAdapter;
//import com.xinspace.csevent.sweepstake.fragment.CommunityFragment;
//import com.xinspace.csevent.sweepstake.fragment.ManageFragment;
//import com.xinspace.csevent.monitor.fragment.SecurityFragment;
//import com.xinspace.csevent.sweepstake.modle.FirstEvent;
//import com.xinspace.csevent.util.ImagerLoaderUtil;
//import com.xinspace.csevent.util.LogUtil;
//import com.xinspace.csevent.ui.activity.ActDetailActivity;
//import com.xinspace.csevent.ui.activity.AwardPoolActivity;
//import com.xinspace.csevent.ui.activity.BaseActivity;
//import com.xinspace.csevent.ui.activity.WebViewActivity;
//
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
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
// * Created by Android on 2016/9/29.
// */
//public class SmartCommAct2 extends BaseActivity {
//
//
//    private LinearLayout lin_content;
//    private LinearLayout ll_main_home;
//    private LinearLayout ll_main_video;
//    private LinearLayout ll_main_activity;
//    private LinearLayout ll_main_mine;
//    private Toolbar toolBar;
//    private RelativeLayout rel_top;
//    private CoordinatorLayout coordinatorLayout;
//    private CollapsingToolbarLayout collapsingToolbar;
//    private TabLayout mTabLayout;
//    private ViewPager vp_viewPager;
//    private ViewPager communityViewpager;
//    private SecurityFragment securityFragment;
//    private ManageFragment manageFragment;
//    private CommunityFragment communityFragment;
//    private List<Fragment> fragments = new Vector<>();
//    private CircleIndicator indicator;
//    private List<ImageView> adv_list;//广告fragment集合
//    private boolean scroll_flag = true;
//    private Timer timer = new Timer();
//    private boolean isFirst = true;//第一次设置indicator
//
//    private FrameLayout frameLayout;
//
//    private TextView tv_bottom;
//
//    //重新获取地址的请求码
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
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.act_smart_comm2);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }
//        EventBus.getDefault().register(this);
//        initView();
//        getAdvertisement();
//    }
//
//
//
//    //获取广告
//    private void getAdvertisement() {
//        GetAdvertisementBiz.getAdvs(SmartCommAct2.this, new HttpRequestListener() {
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
//                ImageView image = new ImageView(SmartCommAct2.this);
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
//            Intent intent = new Intent(SmartCommAct2.this, WebViewActivity.class);
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
//            Intent intent = new Intent(SmartCommAct2.this, ActDetailActivity.class);
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
//            Intent intent = new Intent(SmartCommAct2.this, AwardPoolActivity.class);
//            intent.putExtra("data", enty);
//            startActivity(intent);
//            LogUtil.i("获取到的活动的id:" + act_id);
//        }
//    }
//
//    private void initView() {
//
//        toolBar = (Toolbar) findViewById(R.id.id_tool_bar);
//        toolBar.setContentInsetsRelative(0, 0);
//        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content);
//        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
//        mTabLayout = (TabLayout) findViewById(R.id.tabs);
//        vp_viewPager = (ViewPager) findViewById(R.id.vp_viewPager);
//        indicator = (CircleIndicator) findViewById(R.id.indicator);
//
//        tv_bottom = (TextView) findViewById(R.id.tv_bottom);
//        tv_bottom.setHeight(ScreenUtils.dpToPx(CoresunApp.context, 44));
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            int barHight = ScreenUtils.getStatusBarHeight(CoresunApp.context) + ScreenUtils.dpToPx(CoresunApp.context, 44);
//            tv_bottom.setHeight(barHight);
//            RelativeLayout.LayoutParams layoutToolBar = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, barHight);
//            toolBar.setLayoutParams(layoutToolBar);
//            RelativeLayout.LayoutParams layoutPager = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
//            layoutPager.topMargin = barHight;
//            coordinatorLayout.setLayoutParams(layoutPager);
//            RelativeLayout relContent = (RelativeLayout) findViewById(R.id.rel_content);
//            Toolbar.LayoutParams params = (Toolbar.LayoutParams) relContent.getLayoutParams();
//            params.topMargin = ScreenUtils.getStatusBarHeight(CoresunApp.context);
//            lin_content = (LinearLayout) findViewById(R.id.lin_content);
//            CollapsingToolbarLayout.LayoutParams params1 = (CollapsingToolbarLayout.LayoutParams) lin_content.getLayoutParams();
//            params1.setMargins(0, - ScreenUtils.getStatusBarHeight(CoresunApp.context), 0, 0);
//            lin_content.setLayoutParams(params1);
//        }
//        //setSupportActionBar(toolBar);
//
//        communityViewpager = (ViewPager) findViewById(R.id.community_viewpager);
//        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
//        mTabLayout.addTab(mTabLayout.newTab().setText("安保门禁"));
//        mTabLayout.addTab(mTabLayout.newTab().setText("智慧社区"));
//        mTabLayout.addTab(mTabLayout.newTab().setText("智慧物管"));
//
//        securityFragment = new SecurityFragment();
//        fragments.add(securityFragment);
//        communityFragment = new CommunityFragment();
//        fragments.add(communityFragment);
//        manageFragment = new ManageFragment();
//        fragments.add(manageFragment);
//
//        communityViewpager.setAdapter(new FragmentViewPagerAdapter(getSupportFragmentManager(), fragments));
//        communityViewpager.setCurrentItem(0);
//
//        TabLayout.TabLayoutOnPageChangeListener listener = new TabLayout.TabLayoutOnPageChangeListener(mTabLayout);
//        communityViewpager.addOnPageChangeListener(listener);
//        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                communityViewpager.setCurrentItem(tab.getPosition());
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
//        ll_main_home = (LinearLayout) findViewById(R.id.ll_main_home);
//        ll_main_video = (LinearLayout) findViewById(R.id.ll_main_video);
//        ll_main_activity = (LinearLayout) findViewById(R.id.ll_main_activity);
//        ll_main_mine = (LinearLayout) findViewById(R.id.ll_main_mine);
//
//        ll_main_home.setOnClickListener(clickListener);
//        ll_main_video.setOnClickListener(clickListener);
//        ll_main_activity.setOnClickListener(clickListener);
//        ll_main_mine.setOnClickListener(clickListener);
//
//
//        vp_viewPager.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_MOVE) {
//                    LogUtil.i("手动");
//                    scroll_flag = false;
//                }
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    LogUtil.i("松手");
//                    scroll_flag = true;
//                }
//                return false;
//            }
//        });
//
//
//    }
//
//
//    View.OnClickListener clickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.ll_main_home: //首页
//                    EventBus.getDefault().post(new FirstEvent("1"));
//                    break;
//                case R.id.ll_main_video: //视频
//                    EventBus.getDefault().post(new FirstEvent("2"));
//                    break;
//                case R.id.ll_main_activity: // 揭晓
//                    EventBus.getDefault().post(new FirstEvent("3"));
//                    break;
//                case R.id.ll_main_mine: // 我的
//                    EventBus.getDefault().post(new FirstEvent("4"));
//                    break;
//            }
//            SmartCommAct2.this.finish();
//        }
//    };
//
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        //取消计时器
//        cancelTimer();
//        EventBus.getDefault().unregister(this);//反注册EventBus
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
//    @Subscribe
//    public void onEventMainThread(FirstEvent event) {
//
//    }
//}
