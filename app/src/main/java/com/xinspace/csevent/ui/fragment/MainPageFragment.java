package com.xinspace.csevent.ui.fragment;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xinspace.csevent.R;
import com.xinspace.csevent.adapter.AdvViewpagerAdapter;
import com.xinspace.csevent.adapter.GridViewActivityAdapter;
import com.xinspace.csevent.app.CoresunApp;
import com.xinspace.csevent.app.utils.ScreenUtils;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.customview.CustomQianDaoDialog;
import com.xinspace.csevent.data.biz.GetActivityListBiz;
import com.xinspace.csevent.data.biz.GetAdvertisementBiz;
import com.xinspace.csevent.data.biz.GetDataBiz;
import com.xinspace.csevent.data.biz.QianDaoBiz;
import com.xinspace.csevent.data.entity.ActivityListEntity;
import com.xinspace.csevent.data.entity.AdvBean2;
import com.xinspace.csevent.data.entity.QiandaoAwardEntity;
import com.xinspace.csevent.login.activity.LoginActivity;
import com.xinspace.csevent.monitor.activity.CommunityActivity;
import com.xinspace.csevent.monitor.activity.SubmitDataAct;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.myinterface.SearchAddressFinishListener;
import com.xinspace.csevent.shop.activity.GoodsDetailAct;
import com.xinspace.csevent.shop.activity.ShideShopActivity;
import com.xinspace.csevent.shop.activity.ShopCartAct2;
import com.xinspace.csevent.sweepstake.adapter.GoodsListAdapter3;
import com.xinspace.csevent.sweepstake.modle.AppFristGoodsBean;
import com.xinspace.csevent.sweepstake.modle.FirstEvent;
import com.xinspace.csevent.ui.activity.AwardPoolActivity;
import com.xinspace.csevent.ui.activity.RegetAddressActivity;
import com.xinspace.csevent.ui.activity.WebViewActivity;
import com.xinspace.csevent.util.BdMapUtil;
import com.xinspace.csevent.util.Const;
import com.xinspace.csevent.util.DialogUtil;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.NewcomersTutorialUtil;
import com.xinspace.csevent.util.SharedPreferencesUtil1;
import com.xinspace.csevent.util.TimeUtil;
import com.xinspace.csevent.util.ToastUtil;
import com.xinspace.csevent.util.UpdateVersionUtil;
import com.xinspace.csevent.util.parser.JsonPaser;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
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


public class MainPageFragment extends Fragment implements SearchAddressFinishListener,View.OnClickListener {
    private View view;
    private static ViewPager vp_adv;
    private CircleIndicator indicator;//指示器

    private PullToRefreshListView listView;
    private ImageView iv_shide_shop;//拾得商城
    private ImageView iv_smart_community; // 智慧社区
    private ImageView iv_video;

    private String province;//省
    private String city;
    private String district;

    private List<ImageView> adv_list;//广告fragment集合
    private boolean scroll_flag = true;//标记变量,用于判断是否自动滚动广告
    private MyReceiver receiver;
    private static final int length = 10;//默认加载10条活动数据
    private int start = 0;//默认从第0条数据起请求
    private boolean isLoadMore = false;//是否是加载更多

    private List<Object> act_list = new ArrayList<>();//最新活动列表
    private List<Object> corwd_List = new ArrayList<>();

    private GridViewActivityAdapter activityListAdapter;

    //重新获取地址的请求码
    private int REQUEST_CODE_RE_GET_ADDRESS = 200;
    //广告滚动
    private static final int HANDLER_SCROLL_ADVERTISEMENT = 10;

    private TextView tv_award_instant, tv_award_crowd;
    private LinearLayout ll_award_instant_line, ll_award_crowd_line;

    private TextView[] textviewArray = new TextView[2];
    private LinearLayout[] linearArray = new LinearLayout[2];

    private int clickPostion = 1;

    private int page = 1;

    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 20:
                    if (msg.obj != null){
                        preference.putContent("cartSize" , (String) msg.obj);
                        tv_shopcart_size.setText((String) msg.obj);
                    }
                    break;
                //广告滚动
                case HANDLER_SCROLL_ADVERTISEMENT:
                    scrollAdv();
                    break;
                case 100:
                    if (msg.obj != null){
                        goods_List.addAll((Collection<? extends AppFristGoodsBean>) msg.obj);
                        Log.i("www" , "8888888888888888888888" + goods_List.size());
                        goodsAdapter.setList(goods_List);
                        goodsAdapter.notifyDataSetChanged();
                    }
                    break;
                case 200:
                    if (msg.obj != null){
                        String status = (String) msg.obj;
                        if (status.equals("0")){
                            ToastUtil.makeToast("请完善社区资料");
                            Intent intent = new Intent(getActivity() , SubmitDataAct.class);
                            startActivity(intent);
                        }else if (status.equals("1")){
                            ToastUtil.makeToast("用户审核中");
                        }else if (status.equals("2")){
                            Intent intent = new Intent(getActivity(), CommunityActivity.class);
                            startActivity(intent);
                        }
                    }
                    break;
                case 400:
                    ToastUtil.makeToast("请你联系客服");
                    break;
            }
        }
    };
    private Timer timer = new Timer();
    private boolean isFirst = true;//第一次设置indicator
    private TimerTask timeTask = new TimerTask() {
        @Override
        public void run() {
            mhandler.sendEmptyMessage(HANDLER_SCROLL_ADVERTISEMENT);
        }
    };
    private List<PackageInfo> local_pkgInfoNoSys = new ArrayList<>();
    private ImageView iv_location;
    private BdMapUtil map;
    private Timer updateTimer = new Timer();

    private List<AppFristGoodsBean> goods_List = new ArrayList<>();

    private GoodsListAdapter3 goodsAdapter;

    private ImageView iv_main_video;

    private int videoWidth, videoHeight;

    private SDPreference preference;
    private String uidId;
    private String openId;

    private ImageView iv_main_cart;
    private TextView tv_shopcart_size;
    private RelativeLayout rel_data_load;
    private String area;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        view = inflater.inflate(R.layout.fragment_main_page, null);

        area = SharedPreferencesUtil1.getString(getActivity(), COMMUNITY_AREA, "");
        boolean isLogin = CoresunApp.isLogin;
        if (!isLogin){
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }

        preference = SDPreference.getInstance();
        uidId = preference.getContent("userId");

        //初始化组件
        setViews();
        //设置监听器
        setListener();
        //检查是否有新版本
        checkUpdate(this);
        //注册广播接收器
        registerReceiver();

        //配置上拉加载
        settingListView();
        return view;
    }

    //配置上拉加载
    private void settingListView() {
        //上拉加载不要图片,不要更新时间
        ILoadingLayout layout = listView.getLoadingLayoutProxy(false, true);
        layout.setLoadingDrawable(null);
        layout.setReleaseLabel("松开加载");
        //layout.setPullLabel("上拉加载");
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
    public void onResume() {
        super.onResume();
        if (!uidId.equals("0")){
            openId = preference.getContent("openid");
            initData();
        }else{
            tv_shopcart_size.setText("0");
            preference.putContent("cartSize" , "0");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    //判断是否已经签到
    public void isQianDao() {
        //用户未登陆
        if (CoresunApp.USER_ID == null) return;

        QianDaoBiz.isQianDao(new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                if (result == null && result.equals("")){
                    return;
                }
                JSONObject obj = new JSONObject(result);
                String res = obj.getString("result");
                if (res.equals("200")) {
                    //未签到
                    qianDao();
                    LogUtil.i("未签到");
                } else if (res.equals("101")) {
                    //已经签到
                    LogUtil.i("已经签过到了");
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }

    //判断是否显示新手教程
    private void isShowGuide() {
        NewcomersTutorialUtil.loadToNewcomersTutorial(getActivity(), "mainPage", R.layout.dialog_tutorial_for_main);
    }

    //注册广播接收器
    private void registerReceiver() {
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter(Const.ACTION_CHANGE_QIAN_DAO_STATE);
        filter.addAction(Const.ACTION_TO_CHECK_THE_STATE_OF_QIAN_DAO);
        filter.addAction(Const.ACTION_CONNECTIVITY_CHANGE);
        filter.addAction(Const.ACTION_CONNECTIVITY_CHANGE);//网络状态改变的广播
        getActivity().registerReceiver(receiver, filter);
    }

    //检查是否有新的版本
    public void checkUpdate(MainPageFragment fragment) {
        UpdateVersionUtil updateUtil = new UpdateVersionUtil(getActivity());
        updateUtil.isUpdate(fragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);//反注册EventBus
        //取消广播接收器的注册
        getActivity().unregisterReceiver(receiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //取消计时器
        cancelTimer();
    }

    @Subscribe
    public void onEventMainThread(FirstEvent event) {

    }

    //初始化组件
    private void setViews() {

        rel_data_load = (RelativeLayout) view.findViewById(R.id.rel_data_load); //加载器
        iv_location = (ImageView) view.findViewById(R.id.iv_location_icon);//地理位置icon
        listView = (PullToRefreshListView) view.findViewById(R.id.lv_main_listview);//下拉刷新ListView
        listView.setMode(PullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);

        iv_main_cart = (ImageView) view.findViewById(R.id.iv_main_cart);//购物车icon
        iv_main_cart.setOnClickListener(this);

        tv_shopcart_size = (TextView) view.findViewById(R.id.tv_shopcart_size);//购物车数量icon

        //添加头部view
        View headView = getActivity().getLayoutInflater().inflate(R.layout.item_main_page_head_view, null);
        ListView myListview = listView.getRefreshableView();
        myListview.addHeaderView(headView);

        vp_adv = (ViewPager) headView.findViewById(R.id.vp_viewPager);  //广告容器
        indicator = (CircleIndicator) headView.findViewById(R.id.indicator); //广告圆点

        //拾得影院宣传广告
        iv_main_video = (ImageView) headView.findViewById(R.id.iv_main_video);
        videoWidth = ScreenUtils.getScreenWidth(getActivity());
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv_main_video.getLayoutParams();
        params.width = (int) (videoWidth * 1.0f);
        params.height = (int) (videoWidth * 0.25f);
        iv_main_video.setLayoutParams(params);
        iv_main_video.setScaleType(ImageView.ScaleType.FIT_XY);
        iv_main_video.setImageResource(R.drawable.icon_main_video_pic);
        iv_main_video.setOnClickListener(this);

        //拾得商城
        iv_shide_shop = (ImageView) headView.findViewById(R.id.iv_shide_shop);
        iv_shide_shop.setOnClickListener(this);

        //智慧社区
        iv_smart_community = (ImageView) headView.findViewById(R.id.iv_smart_community);
        iv_smart_community.setOnClickListener(this);

        //拾得影院
        iv_video = (ImageView) headView.findViewById(R.id.iv_video);
        iv_video.setOnClickListener(this);

        goodsAdapter = new GoodsListAdapter3(getActivity());
        goodsAdapter.setList(goods_List);
        listView.setAdapter(goodsAdapter);
        getGoodsList(page);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity() , GoodsDetailAct.class);
                intent.putExtra("goodId" , goods_List.get(position - 2).getGid());
                startActivity(intent);
            }
        });
    }

    private void initData() {
        GetActivityListBiz.getShopCartData(openId, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                LogUtil.i("获取购物车列表数据" + result);
                if (result == null || result.equals("")){
                    return;
                }
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("code").equals("200")){
                    JSONObject dataJson = jsonObject.getJSONObject("data");
                    String total = dataJson.getString("total");
                    mhandler.obtainMessage(20 , total).sendToTarget();
                }else{

                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_smart_community:       //智慧社区
                preference.putContent("smart", "1");
                String uid = preference.getContent("userId");
                if (uid.equals("0")){
                    Intent intent = new Intent(getActivity() , LoginActivity.class);
                    startActivity(intent);
                }else{
                    queryAuditStatus();
                }
                break;

            case R.id.iv_main_cart:     //购物车
                String uidId = preference.getContent("userId");
                if (uidId.equals("0")){
                    Intent intent = new Intent(getActivity() , LoginActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getActivity() , ShopCartAct2.class);
                    startActivity(intent);
                }
                break;

            case R.id.iv_shide_shop:      //拾得商城
                Intent intent = new Intent(getActivity() , ShideShopActivity.class);
                startActivity(intent);
                break;

            case R.id.iv_video:     //拾得影院

                ToastUtil.makeToast("正在建设中，敬请期待");
//                showShare(getActivity() , "Wechat" , true);

                break;
            case R.id.iv_main_video:
//                    Intent intent2 = new Intent(getActivity(), ViedoAct.class);
//                    startActivity(intent2);

               ToastUtil.makeToast("此功能调整中");

                break;
        }
    }


    /**
     * 演示调用ShareSDK执行分享
     *
     * @param context
     * @param platformToShare  指定直接分享平台名称（一旦设置了平台名称，则九宫格将不会显示）
     * @param showContentEdit  是否显示编辑页
     */
    public static void showShare(Context context, String platformToShare, boolean showContentEdit) {

    }

    private void queryAuditStatus(){

        String cuid = preference.getContent("cUid");
        String cToken = preference.getContent("cToken");

        GetDataBiz.getqueryAuditStatus(area, cuid, cToken, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                LogUtil.i("获取审核状态列表" + result);
                if (result == null || result.equals("")){
                    return;
                }

                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("code").equals("200")){
                    JSONObject dataJsonObject = jsonObject.getJSONObject("data");
                    String status = dataJsonObject.getString("status");
                    mhandler.obtainMessage(200 , status).sendToTarget();
                }else{
                    mhandler.obtainMessage(400 ).sendToTarget();
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }

    private void changeBackground(int currentIndex) {
        if (currentIndex == 0) {
            //改变下划线背景
            for (LinearLayout ll : linearArray) {
                ll.setBackgroundColor(getResources().getColor(R.color.color_font));
            }
            ll_award_instant_line.setBackgroundColor(getResources().getColor(R.color.color_text_base));
            //改变文字背景
            for (TextView tv : textviewArray) {
                tv.setTextColor(getResources().getColor(R.color.color_text_base));
            }
            tv_award_instant.setTextColor(getResources().getColor(R.color.color_text_base));
        }
        if (currentIndex == 1) {
            //改变下划线背景
            for (LinearLayout ll : linearArray) {
                ll.setBackgroundColor(getResources().getColor(R.color.color_font));
            }
            ll_award_crowd_line.setBackgroundColor(getResources().getColor(R.color.color_text_base));
            //改变文字背景
            for (TextView tv : textviewArray) {
                tv.setTextColor(getResources().getColor(R.color.color_text_base));
            }
            tv_award_crowd.setTextColor(getResources().getColor(R.color.color_text_base));
        }
    }

    private void changeDataList(int currentIndex) {
        Log.i("www", "currentIndex" + currentIndex);
        if (currentIndex == 0) {
            corwd_List.clear();
            isLoadMore = false;
            start = 0;
            getActivityList();
        }
        if (currentIndex == 1) {
            act_list.clear();
            //activityListAdapter.notifyDataSetChanged();
            isLoadMore = false;
            activityListAdapter = null;
            start = 0;
            //getCrowdList();
        }
    }

    //设置监听器3
    private void setListener() {
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                LogUtil.i("下拉刷新");
                page = 1;
                goods_List.clear();
                //调用获取活动列表的方法
                getGoodsList(page);
                getAdvertisement();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                LogUtil.i("加载更多");
                listView.onRefreshComplete();
                ToastUtil.makeToast("没有更多商品");
//                page++;
//                getGoodsList(page);
            }
        });

        //广告设置当手指在广告上滚动时,则不自动滚动,否则自动滚动
        vp_adv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    LogUtil.i("手动");
                    scroll_flag = false;
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    LogUtil.i("松手");
                    scroll_flag = true;
                }
                return false;
            }
        });

        //当前位置地址
        iv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RegetAddressActivity.class);
                startActivityForResult(intent, REQUEST_CODE_RE_GET_ADDRESS);
            }
        });
    }

    //启动第三方app(空港云)
    private void startOtherApp() {
        final PackageManager packageManager = getActivity().getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pinfo.size(); i++) {
            PackageInfo packageInfo = pinfo.get(i);
            // 获取 非系统的应用
            if ((packageInfo.applicationInfo.flags & packageInfo.applicationInfo.FLAG_SYSTEM) <= 0) {
                local_pkgInfoNoSys.add(packageInfo);
            }
            // 本来是系统程序，被用户手动更新后，该系统程序也成为第三方应用程序了
            else if ((packageInfo.applicationInfo.flags & packageInfo.applicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                local_pkgInfoNoSys.add(packageInfo);
            }
        }
        //有安装应用则打开
        for (int i = 0; i < local_pkgInfoNoSys.size(); i++) {
            PackageInfo enty = local_pkgInfoNoSys.get(i);
            if (enty.packageName.equals("com.dongdao")) {
                Intent mainIntent = getActivity().getPackageManager().getLaunchIntentForPackage("com.dongdao");
                startActivity(mainIntent);
                return;
            }
        }
        //否则跳转到改应用的下载页面
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(Const.THE_THIRD_APP_URL);
        intent.setData(content_url);
        startActivity(intent);
    }

    //回调地址页面的新地址
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_RE_GET_ADDRESS && resultCode == -1) {
            province = data.getStringExtra("theMoveProvince");
            city = data.getStringExtra("theMoveCity");
            district = data.getStringExtra("theMoveDistrict");
            //将位置信息保存到全局
            CoresunApp.province = province;
            CoresunApp.city = city;
            CoresunApp.area = district;

            String street = data.getStringExtra("theMoveStreet");
            String streetNumber = data.getStringExtra("theMovestreetNumber");
            // getActivityList();//获取到新地址后更新活动
        }
    }

    //获取即时抽奖活动列表
    private void getActivityList() {
        GetActivityListBiz.getActivityByIndex(String.valueOf(start), String.valueOf(length), "广东省", "广州市", "天河区", new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                try {

                    //刷新完成
                    listView.onRefreshComplete();
                    listView.getLoadingLayoutProxy(true, false).setLastUpdatedLabel("上次更新:" + TimeUtil.getTime());
                    Log.i("www", "获取活动列表" + result);
                    if (result == null && result.equals("")){
                        return;
                    }

                    showActivityList(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });

    }

//    private void getCrowdList() {
//        GetActivityListBiz.getCrowdDataList(String.valueOf(start), String.valueOf(length), new HttpRequestListener() {
//            @Override
//            public void onHttpRequestFinish(String result) throws JSONException {
//                try {
//                    //刷新完成
//                    listView.onRefreshComplete();
//                    listView.getLoadingLayoutProxy(true, false).setLastUpdatedLabel("上次更新:" + TimeUtil.getTime());
//                    Log.i("www", "众筹活动列表" + result);
//                    showCrowdList(result);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onHttpRequestError(String error) {
//                if (null == activityListAdapter) {
//                    activityListAdapter = new GridViewActivityAdapter(getActivity(), act_list);
//                    listView.setAdapter(activityListAdapter);
//                }
//                listView.onRefreshComplete();
//            }
//        });
//    }


    private void getGoodsList(int pageNum) {
        GetActivityListBiz.getFristGoodsList(String.valueOf(pageNum), new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                try {
                    //刷新完成
                    listView.onRefreshComplete();
                    listView.getLoadingLayoutProxy(true, false).setLastUpdatedLabel("上次更新:" + TimeUtil.getTime());
                    Log.i("www", "微信商城" + result.toString());
                    if (result == null || result.equals("")){
                        return;
                    }

                    showGoodsList(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onHttpRequestError(String error) {

                rel_data_load.setVisibility(View.GONE);

            }
        });
    }


    //显示活动列表
    private void showActivityList(String result) throws Exception {
        JSONObject obj = new JSONObject(result);
        String res = obj.getString("result");
        if (res.equals("200")) {
            JSONArray act_ary = obj.getJSONArray("data");
            List<Object> actList = JsonPaser.parserAry(act_ary.toString(), ActivityListEntity.class);
            Log.i("www", "解析list长度" + actList.size());
            //如果是加载更多
            if (isLoadMore) {
                Log.i("www", "进来刷新适配");
                isLoadMore = false;
                act_list.addAll(actList);
                activityListAdapter.updateData(act_list);
            } else {
                act_list.clear();
                act_list.addAll(actList);
                if (null == activityListAdapter) {
                    activityListAdapter = new GridViewActivityAdapter(getActivity(), act_list);
                    listView.setAdapter(activityListAdapter);
                } else {
                    activityListAdapter.updateData(act_list);
                }
            }
        } else if (res.equals("201")) {
            ToastUtil.makeToast("没有更多活动");
            isLoadMore = false;
        }
    }


    private void showGoodsList(String result) throws Exception {

        listView.onRefreshComplete();
        rel_data_load.setVisibility(View.GONE);

        JSONObject obj = new JSONObject(result);
        String type = obj.getString("code");
        if (type.equals("200")) {
            //JSONObject jsonObject = obj.getJSONObject("data");
            JSONArray act_ary = obj.getJSONArray("data");
            List<AppFristGoodsBean> goodsEntities = new ArrayList<>();
            for (int i = 0; i < act_ary.length(); i++) {
                JSONObject j = act_ary.getJSONObject(i);
                Gson gson=new Gson();
                AppFristGoodsBean goodsModle = gson.fromJson(j.toString(), AppFristGoodsBean.class);
                goodsEntities.add(goodsModle);
            }
            LogUtil.i("商品分类列表" + goodsEntities.size());
            if (goodsEntities.size() != 0){
                mhandler.obtainMessage(100 , goodsEntities).sendToTarget();
            }else{
                ToastUtil.makeToast("没有更多商品");
            }
        } else {
            ToastUtil.makeToast("数据出错");
        }
    }

    //获取当前位置
    private void getLocation() {
        map = new BdMapUtil();
        map.setOnAddressFinishListener(this);
        map.getLocation();
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
                ImageView image = new ImageView(getActivity());
                image.setScaleType(ImageView.ScaleType.FIT_XY);
//                ImagerLoaderUtil.displayImageWithLoadingIcon(url, image, R.drawable.advertisement_loading);
                Glide.with(this)
                        .load(url)
                        .crossFade()
                        .placeholder(R.drawable.advertisement_loading)
                        .error(R.drawable.advertisement_loading)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(image);
                //设置监听器
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (enty.getUrl() != null) {

                            String url = enty.getUrl();
                            if (url.contains("http")){
                                clickIntoAdvertisement(enty.getUrl(), "0");
                            }else{
                                clickIntoAdvertisement(enty.getUrl(), "1");
                            }
                        }
                    }
                });
                adv_list.add(image);
            }
            //设置适配器
            AdvViewpagerAdapter adapter = new AdvViewpagerAdapter(adv_list);
            vp_adv.setAdapter(adapter);

            //设置指示器
            if (isFirst) {
                isFirst = false;
                indicator.setViewPager(vp_adv);
                //滚动广告
                timer.schedule(timeTask, 5000, 5000);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //点击广告进入相应的页面或者活动
    private void clickIntoAdvertisement(String link, String type) {
        if (type.equals("0")) {
            //进入一个html静态页面
            Intent intent = new Intent(getActivity(), WebViewActivity.class);
            intent.putExtra("data", link);
            startActivity(intent);
        } else if (type.equals("1")) {

            Intent intent = new Intent(getActivity() , GoodsDetailAct.class);
            intent.putExtra("goodId", link);
            startActivity(intent);

        } else if (type.equals("4")) {
            //进入抽奖池抽奖页面
            int index = link.lastIndexOf("_");
            String act_id = link.substring(index + 1);
            ActivityListEntity enty = new ActivityListEntity();
            enty.setId(Integer.parseInt(act_id));

            Intent intent = new Intent(getActivity(), AwardPoolActivity.class);
            intent.putExtra("data", enty);
            startActivity(intent);
            LogUtil.i("获取到的活动的id:" + act_id);
        }
    }

    //获取广告
    private void getAdvertisement() {
        GetAdvertisementBiz.getAdvs2(getActivity(), new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                Log.i("www", "广告的数据" + result);
                if(result != null && result.length() != 0){
                    showAdvertisement(result);
                }else{
                    ToastUtil.makeToast("网络出错");
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }

    //签到
    private void qianDao() {
        //没登录
        if (CoresunApp.USER_ID == null) {
            ToastUtil.makeToast("请先登录");
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            return;
        }
        QianDaoBiz.qiandao(new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                if (result == null && result.equals("")){
                    return;
                }
                handleQiandao(result);
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }

    //处理签到
    private void handleQiandao(String result) {
        try {
            JSONObject obj = new JSONObject(result);
            String res = obj.getString("result");
            LogUtil.i("handleQiandao" + result);
            if (res.equals("101")) {
                LogUtil.i("已经签到");
            } else if (res.equals("200")) {
                //签到成功后拿到数据
                String data = obj.getString("data");
                final QiandaoAwardEntity qiandaoEnty = (QiandaoAwardEntity) JsonPaser.parserObj(data, QiandaoAwardEntity.class);
                float radius = 0f;
                switch (qiandaoEnty.getRadius()) {
                    case "0":
                        radius = 90f; //金币
                        break;
                    case "1":
                        radius = 45f; //积分
                        break;
                    case "2":
                        radius = 135f; //抽奖次数
                        break;
                    case "3":
                        radius = 0f; //商品
                        break;
                }
                final Dialog dialog = DialogUtil.createQianDaoDialog(getActivity(), radius);
                dialog.show();//显示签到转盘
                final Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        dialog.cancel();
                        getActivity().runOnUiThread(new TimerTask() {
                            @Override
                            public void run() {
                                timer.cancel();
                                CustomQianDaoDialog dialog1 = CustomQianDaoDialog.getInstance(getActivity());
                                dialog1.show();
                                dialog1.setText(qiandaoEnty.getNum() + "个" + qiandaoEnty.getName());
                            }
                        });
                    }
                }, 3000);//转盘转4秒钟后显示签到转盘抽中的物品
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //查询经纬度和地址回调
    @Override
    public void onSearchFinish(LatLng latLng, String address, String province, String city, String district) {
        LogUtil.i("获取到了位置信息:" + address + ";" + province + ";" + city + "," + district);

        if (null == province) {
            map.getLocation();
            return;
        }
        this.province = province;
        this.city = city;
        this.district = district;
        //将位置信息保存到全局
        CoresunApp.province = province;
        CoresunApp.city = city;
        CoresunApp.area = district;

        //调用获取活动列表的方法
    }

    //监听登录与退出,改变签到的状态,登录后检查登录状态
    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Const.ACTION_TO_CHECK_THE_STATE_OF_QIAN_DAO)) {
               // isQianDao();
            } else if (action.equals(Const.ACTION_CONNECTIVITY_CHANGE)) {
                //网络改变
                LogUtil.i("网络状态改变");
                getLocation();
                getAdvertisement();
            }
        }
    }

    //取消timer
    private void cancelTimer() {
        if (null != timer) {
            timer.cancel();
            timer = null;
        }
        if (null != updateTimer) {
            updateTimer.cancel();
            updateTimer = null;
        }
    }

}
