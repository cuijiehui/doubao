package com.xinspace.csevent.monitor.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.zhouwei.library.CustomPopWindow;
import com.xinspace.csevent.R;
import com.xinspace.csevent.adapter.AdvViewpagerAdapter;
import com.xinspace.csevent.app.utils.ScreenUtils;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.biz.GetAdvertisementBiz;
import com.xinspace.csevent.data.biz.GetDataBiz;
import com.xinspace.csevent.data.entity.AdvBean2;
import com.xinspace.csevent.login.activity.LoginActivity;
import com.xinspace.csevent.login.util.LoginStatusUtil;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.SharedPreferencesUtil1;
import com.xinspace.csevent.util.parser.JsonPaser;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.StatusBarUtils;
import com.xinspace.csevent.util.ToastUtil;
import com.xinspace.csevent.ui.activity.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.linphone.LinphonePreferences;
import org.linphone.core.LinphoneAddress;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;
import sdk_sample.sdk.HttpRespListener;
import sdk_sample.sdk.activity.LockListActivity;
import sdk_sample.sdk.bean.ComBean;
import sdk_sample.sdk.result.BaseResult;
import sdk_sample.sdk.result.TokenResult;
import sdk_sample.sdk.utils.HttpUtils;
import sdk_sample.sdk.utils.SharedPreferencesUtil;
import sdk_sample.sdk.utils.ToolsUtil;

import static com.xinspace.csevent.app.AppConfig.COMMUNITY_AREA;


public class OpenDoorActivity extends BaseActivity {

    private boolean accountCreated = false;
    private LinphoneAddress address;
    private LinphonePreferences mPrefs;
    private static OpenDoorActivity instance;
    private Handler handler;
    private List<ComBean> lockList;
    private TextView tv_shide_community;
    //private OpenDoorActivity.ServiceWaitThread mThread;

    public static final boolean isInstanciated() {
        return instance != null;
    }

    public static final OpenDoorActivity instance() {
        if (instance != null)
            return instance;
        throw new RuntimeException("OpenDoorActivity not instantiated yet");
    }

    private List<ImageView> adv_list;//广告fragment集合
    private static ViewPager vp_adv;
    private CircleIndicator indicator;//指示器
    //重新获取地址的请求码
    private int REQUEST_CODE_RE_GET_ADDRESS = 200;
    //广告滚动
    private static final int HANDLER_SCROLL_ADVERTISEMENT = 10;
    private boolean scroll_flag = true;//标记变量,用于判断是否自动滚动广告

    private TextView tv_notice;
    private LinearLayout ll_open_monitoring, ll_open2, ll_open3, ll_open4;
    private ImageView ivOpenDoor;
    private ImageView ivFinish;

    private String token;
    private long dealTime;
    private RelativeLayout rel_notice;
    private String flag;
    private String mobile;
    private SDPreference preference;
    private TextSwitcher ts_notice;
    private String cUid;
    private String cToken;
    private ImageView img_setting;
    private CustomPopWindow popWindow;
    private TextView gz, st, tv_shop_title;
    private RelativeLayout relativeTopTitle;
    private ImageView imgForwardRight;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //广告滚动
                case HANDLER_SCROLL_ADVERTISEMENT:
                    scrollAdv();
                    break;
                case 1000:   //获取token成功
                    doGetNeiborId(flag);
                    break;
                case 2000:  //公告数据
                    if (msg.obj != null ){
                        newsList.addAll((Collection<? extends String>) msg.obj);
                        notice(newsList);
                    }
                    break;
            }
        }
    };


    private int index = 0;//textview上下滚动下标
    public static final int NEWS_MESSAGE_TEXTVIEW = 300;//通知公告信息
    // 要显示的文本
    private List<String> newsList = new ArrayList<>();

    Handler noticeHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case NEWS_MESSAGE_TEXTVIEW:

                    //LogUtil.i("-------------------------------" + newsList.get(index));

                    ts_notice.setText(newsList.get(index));
                    index++;
                    if (index == newsList.size()) {
                        index = 0;
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private Timer timer = new Timer();
    private boolean isFirst = true;//第一次设置indicator
    private TimerTask timeTask = new TimerTask() {
        @Override
        public void run() {
            mHandler.sendEmptyMessage(HANDLER_SCROLL_ADVERTISEMENT);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opendoor);
        StatusBarUtils.setWindowStatusBarColor(this , R.color.app_bottom_color);
        area = SharedPreferencesUtil1.getString(this, COMMUNITY_AREA, "");
        boolean isLogin = LoginStatusUtil.checkLoginStatus();
        if (!isLogin){
            startActivity(new Intent(this, LoginActivity.class));
        }

        handler = new Handler();
        mPrefs = LinphonePreferences.instance();

        preference = SDPreference.getInstance();
        cToken = preference.getContent("cToken");
        cUid = preference.getContent("cUid");
        mobile = preference.getContent("mobile");

        initView();
        setDefaultCommunity();
    }

    private void initView() {

        relativeTopTitle = (RelativeLayout)findViewById(R.id.relative_shop_title);
        imgForwardRight = (ImageView)findViewById(R.id.img_forward_right);
        tv_shop_title = (TextView)findViewById(R.id.tv_shop_title);
        tv_shop_title.setOnClickListener(onclickListener);

        vp_adv = (ViewPager) findViewById(R.id.vp_ad);
        tv_notice = (TextView) findViewById(R.id.tv_notice);
        tv_notice.setSelected(true);

        ts_notice = (TextSwitcher) findViewById(R.id.ts_notice);
        img_setting = (ImageView) findViewById(R.id.img_setting);
        img_setting.setOnClickListener(onclickListener);

        ts_notice.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView textView = new TextView(OpenDoorActivity.this);
                textView.setSingleLine();
                textView.setTextSize(16);//字号
                textView.setTextColor(Color.parseColor("#ffffff"));
                textView.setEllipsize(TextUtils.TruncateAt.END);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.gravity = Gravity.CENTER_VERTICAL;
                textView.setLayoutParams(params);
                return textView;
            }
        });

        rel_notice = (RelativeLayout) findViewById(R.id.rel_notice);
        rel_notice.setOnClickListener(onclickListener);

        ll_open_monitoring = (LinearLayout) findViewById(R.id.ll_open_monitoring);
        ll_open2 = (LinearLayout) findViewById(R.id.ll_open2);
        ll_open3 = (LinearLayout) findViewById(R.id.ll_open3);
        ll_open4 = (LinearLayout) findViewById(R.id.ll_open4);

        ll_open_monitoring.setOnClickListener(onclickListener);
        ll_open2.setOnClickListener(onclickListener);
        ll_open3.setOnClickListener(onclickListener);
        ll_open4.setOnClickListener(onclickListener);

        ivOpenDoor = (ImageView) findViewById(R.id.iv_open_door);
        ivOpenDoor.setOnClickListener(onclickListener);

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
        getNoticeData();
        getAdvertisement();
    }

    /**
     * 检查用户当前状态
     * status:  1  用户处于审核状态
     *          2  用户处于通过状态
     *          0  用户尚未注册社区
     */
    private void verifyPermission() {
        GetDataBiz.getqueryAuditStatus(area, cUid, cToken, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                LogUtil.e("当前线程：" + Thread.currentThread().getName());
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("code").equals("200")){
                    JSONObject dataJsonObject = jsonObject.getJSONObject("data");
                    String status = dataJsonObject.getString("status");
                    if (status.equals("0")){
                        ToastUtil.makeToast("请完善社区资料");
                        Intent intent = new Intent(OpenDoorActivity.this , SubmitDataAct.class);
                        startActivity(intent);
                    }
                    else if (status.equals("1")){
                        ToastUtil.makeToast("当前用户正在审核中");
                    }
                    else{
                        doGetToken();
                    }
                }else{
                    ToastUtil.makeToast("当前用户无使用权限");
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


    private void getNoticeData() {
        GetDataBiz.getNoticeData(area, cUid, cToken, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                LogUtil.i("获取最新公告数据的result" + result);
                if (result == null || result.equals("")){
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
                    mHandler.obtainMessage(2000,noticeList).sendToTarget();
                } else if (jsonObject.getString("code").equals("400")) {
                    LogUtil.i("没有公告数据");
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }

    private void showPopBottom() {
        popWindow = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(R.layout.title_pop_layout)
                .setFocusable(true)
                .setOutsideTouchable(true)
                .setOnDissmissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        imgForwardRight.setImageResource(R.drawable.icon_group_right);
                    }
                })
                .create();
        int x_start = ScreenUtils.getScreenWidth(this)/2 - 250;
        popWindow.showAsDropDown(relativeTopTitle, x_start, 10);
        gz = (TextView)popWindow.getPopupWindow().getContentView().findViewById(R.id.tv_community_gz);
        st = (TextView)popWindow.getPopupWindow().getContentView().findViewById(R.id.tv_community_st);
        gz.setOnClickListener(onclickListener);
        st.setOnClickListener(onclickListener);
    }

    /**
     * 设置默认的社区
     * create by MQZ at 2017/11/1
     */
    private void setDefaultCommunity() {
        if (area.equals("gz")){
            tv_shop_title.setText(R.string.community_gz);
            NEIBOR_ID = "100046";
        }else{
            tv_shop_title.setText(R.string.community_st);
            NEIBOR_ID = "100032";  //汕头
        }
    }


    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    private String area;
    private String NEIBOR_ID;
    View.OnClickListener onclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.tv_shop_title:
                    showPopBottom();
                    imgForwardRight.setImageResource(R.drawable.icon_group_down);
                    break;

                case R.id.tv_community_gz:
                    tv_shop_title.setText(gz.getText().toString());
                    tv_shop_title.setTextColor(Color.GREEN);
                    NEIBOR_ID = "100046";   //广州
                    SharedPreferencesUtil1.saveString(OpenDoorActivity.this, COMMUNITY_AREA, "gz");
                    popWindow.dissmiss();
                    tv_shop_title.setTextColor(Color.WHITE);
                    imgForwardRight.setImageResource(R.drawable.icon_group_right);
                    break;

                case R.id.tv_community_st:
                    tv_shop_title.setText(st.getText().toString());
                    tv_shop_title.setTextColor(Color.GREEN);
                    NEIBOR_ID = "100032";  //汕头
                    SharedPreferencesUtil1.saveString(OpenDoorActivity.this, COMMUNITY_AREA, "st");
                    popWindow.dissmiss();
                    tv_shop_title.setTextColor(Color.WHITE);
                    imgForwardRight.setImageResource(R.drawable.icon_group_right);
                    break;

                case R.id.iv_open_door:  // 开门
                    flag = "1";
                    verifyPermission();
                    break;
                case R.id.ll_open_monitoring:  // 门禁监控InCallAct
                    flag = "2";
                    verifyPermission();
                    break;
                case R.id.ll_open2: // 密码开门
                    flag = "3";
                    verifyPermission();

                    break;
                case R.id.ll_open3: // 授权开门
                    ToastUtil.makeToast("此功能待完善");
                    break;
                case R.id.ll_open4: // 开门记录
                    Intent intent4 = new Intent(OpenDoorActivity.this, OpenDoorRecordAct.class);
                    startActivity(intent4);
                    break;
                case R.id.rel_notice: // 公告
                    Intent intent5 = new Intent(OpenDoorActivity.this, AfficheAct.class);
                    startActivity(intent5);
                    break;
                case R.id.img_setting:
                    Intent intent = new Intent(OpenDoorActivity.this, ComAppAct.class);
                    startActivity(intent);
                    break;

            }
        }
    };

    //获取广告
    private void getAdvertisement() {
        GetAdvertisementBiz.getAdvs3(this, new HttpRequestListener() {
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

            }
        });
    }

    //设置滚动广告栏的adapter
    private void showAdvertisement(String result) {
        if (result == null || result.equals("")){
            return;
        }
        try {
            JSONObject obj = new JSONObject(result);
            JSONArray ary = obj.getJSONArray("data");
            List<Object> advs = JsonPaser.parserAry(ary.toString(), AdvBean2.class);
            adv_list = new ArrayList<>();
            for (int i = 0; i < advs.size(); i++) {
                final AdvBean2 enty = (AdvBean2) advs.get(i);
                String url = enty.getPic();
                //显示广告图片
                ImageView image = new ImageView(OpenDoorActivity.this);
                image.setScaleType(ImageView.ScaleType.FIT_XY);
                Glide.with(this)
                        .load(url)
                        .crossFade()
                        .placeholder(R.drawable.advertisement_loading)
                        .error(R.drawable.advertisement_loading)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(image);
//                ImagerLoaderUtil.displayImageWithLoadingIcon(url, image, R.drawable.advertisement_loading);
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

    /**
     * 1.首先获取Token
     */
    private void doGetToken() {
        String key = "2";
        String userName = mobile;
        String app_id = "2";
        if (TextUtils.isEmpty(key)) {
            ToolsUtil.toast(this, "key不能为空！");
            return;
        }
        if (TextUtils.isEmpty(userName)) {
            ToolsUtil.toast(this, "userName不能为空！");
            return;
        }
        if (TextUtils.isEmpty(app_id)) {
            ToolsUtil.toast(this, "appId不能为空！");
            return;
        }

        ToolsUtil.getToken(this, key, userName, app_id, null, new HttpRespListener() {
            @Override
            public void onSuccess(int code, BaseResult result) {
                TokenResult tokeResult = (TokenResult) result;
                token = tokeResult.getToken();
                dealTime = tokeResult.getDead_time();
                mHandler.obtainMessage(1000).sendToTarget();

                Log.i("www", "dealtime" + dealTime);
                Log.i("www", "token" + token);
            }

            @Override
            public void onFail(int code, String msg) {
                ToolsUtil.toast(getApplicationContext(), "您无使用权限");
            }
        });
    }

    /**
     * 2.获取的社区
     */
    private void doGetNeiborId(String falg) {

        Log.i("www", "------------社区列表请求成功---------------");

        final String userName = mobile;
        if (TextUtils.isEmpty(token)) {
            ToolsUtil.toast(this, "token不能为空！");
            return;
        }
        if (TextUtils.isEmpty(userName)) {
            ToolsUtil.toast(this, "userName不能为空！");
            return;
        }

        if (TextUtils.isEmpty(NEIBOR_ID)) {
            ToolsUtil.toast(this, "neibor_flag不能为空");
            return;
        }

        final Map<String, String> headpParams = new HashMap<String, String>();
        headpParams.put("token", token);
        headpParams.put("username", userName);
        headpParams.put("dealtime", dealTime + "");
        final Map<String, String> params = new HashMap<String, String>();
        params.put("username", userName);
        params.put("neibor_flag", NEIBOR_ID);

        HttpUtils.goToOpenDoor(this, headpParams, params, flag, new HttpRespListener() {
            //回调方法运行在主线程
            @Override
            public void onSuccess(int code, BaseResult result) {
                //请求成功回调
                LogUtil.i("----------code---------" + code + "\n---------result-----------" + result.toString());

                try {
                    JSONObject e = new JSONObject(result.getMsg());
                    JSONObject jsonObject = e.getJSONObject("result");

                    JSONObject jsonObject1 = jsonObject.getJSONObject("user_msg");
                    String neiborId = jsonObject.getString("neibor_id");
                    String sip_number = jsonObject1.getString("user_sip");
                    String sip_password = jsonObject1.getString("user_password");
                    String sip_domin = jsonObject1.getString("fs_ip");
                    int sip_port = jsonObject1.getInt("fs_port");

                    String path = "https://" + jsonObject.getString("fip") + ":" + jsonObject.getInt("fport");

                    Intent intent = new Intent(OpenDoorActivity.this, LockListActivity.class);
                    intent.putExtra("path_url", path);
                    intent.putExtra("token", token);
                    intent.putExtra("username", userName);
                    intent.putExtra("neigbor_id", neiborId);
                    intent.putExtra("deal_time", dealTime);

                    intent.putExtra("sip_number" , sip_number);
                    intent.putExtra("sip_password" , sip_password);
                    intent.putExtra("sip_domin" , sip_domin);
                    intent.putExtra("sip_port" , sip_port);
                    intent.putExtra("flag" , flag);

                    intent.addFlags(268435456);
                    startActivity(intent);

                    SharedPreferencesUtil.saveData(OpenDoorActivity.this, "sayee_tow_url_key", path);
                    SharedPreferencesUtil.saveData(OpenDoorActivity.this, "sayee_user_sip_domain_key", sip_domin);
                    SharedPreferencesUtil.saveData(OpenDoorActivity.this, "sayee_user_name_key", userName);
                    SharedPreferencesUtil.saveData(OpenDoorActivity.this, "sayee_user_token_key", token);
                    SharedPreferencesUtil.saveData(OpenDoorActivity.this, "sayee_deal_time", Long.valueOf(dealTime));
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void onFail(int code, String msg) {

                //请求失败回调
                Log.i("www", "失败code   " + code + "msg   " + msg.toString());

                ToolsUtil.toast(getApplicationContext(), msg);

            }
        });
    }

    public void displayCustomToast(final String message, final int duration) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast, (ViewGroup) findViewById(R.id.toastRoot));

        TextView toastText = (TextView) layout.findViewById(R.id.toastMessage);
        toastText.setText(message);

        final Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(duration);
        toast.setView(layout);
        toast.show();
    }


    @Override
    public void onDestroy() {
        timer.cancel();
        timer = null;
        timeTask.cancel();
        timeTask = null;
        mHandler.removeCallbacksAndMessages(null);
        noticeHandler.removeCallbacksAndMessages(null);

        if (adv_list != null && adv_list.size() != 0){
            adv_list.clear();
            adv_list = null;
        }

        if (newsList != null && newsList.size() != 0){
            newsList.clear();
            newsList = null;
        }

        this.setContentView(R.layout.empty_view);
        System.gc();
        super.onDestroy();
        GetDataBiz.cancelNoticeData();
    }

}




