package com.xinspace.csevent.ui.fragment;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
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
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListAdapter;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListItem;
import com.example.zhouwei.library.CustomPopWindow;
import com.google.gson.Gson;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.xinspace.csevent.R;
import com.xinspace.csevent.adapter.AdvViewpagerAdapter;
import com.xinspace.csevent.app.CoresunApp;
import com.xinspace.csevent.app.utils.ScreenUtils;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.biz.CommunityUnit;
import com.xinspace.csevent.data.biz.GetAdvertisementBiz;
import com.xinspace.csevent.data.biz.GetDataBiz;
import com.xinspace.csevent.data.entity.AdvBean2;
import com.xinspace.csevent.login.activity.LoginActivity;
import com.xinspace.csevent.monitor.activity.AfficheAct;
import com.xinspace.csevent.monitor.activity.ComAppAct;
import com.xinspace.csevent.monitor.activity.OpenDoorRecordAct;
import com.xinspace.csevent.monitor.activity.PassWordAct;
import com.xinspace.csevent.monitor.activity.SubmitDataAct;
import com.xinspace.csevent.monitor.bean.SipEvent;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.sweepstake.modle.FirstEvent;
import com.xinspace.csevent.ui.activity.MainActivity;
import com.xinspace.csevent.util.ImageUtil;
import com.xinspace.csevent.util.ImagerLoaderUtil;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.SharedPreferencesUtil1;
import com.xinspace.csevent.util.ToastUtil;
import com.xinspace.csevent.util.ToastUtils;
import com.xinspace.csevent.util.parser.JsonPaser;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.linphone.InCallActivity;
import org.linphone.LinphoneManager;
import org.linphone.LinphonePreferences;
import org.linphone.core.LinphoneAuthInfo;
import org.linphone.core.LinphoneCall;
import org.linphone.core.LinphoneChatMessage;
import org.linphone.core.LinphoneChatRoom;
import org.linphone.core.LinphoneCore;
import org.linphone.core.LinphoneCoreFactory;
import org.linphone.core.LinphoneCoreListenerBase;
import org.linphone.core.LinphoneProxyConfig;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import sdk_sample.sdk.HttpRespListener;
import sdk_sample.sdk.activity.LockListActivity;
import sdk_sample.sdk.bean.LockBean;
import sdk_sample.sdk.bean.LockRecordBean;
import sdk_sample.sdk.bean.OpenLockPasswordBean;
import sdk_sample.sdk.db.DatabaseHelper;
import sdk_sample.sdk.result.BaseResult;
import sdk_sample.sdk.result.LockListResult;
import sdk_sample.sdk.result.OpenLockPasswordResult;
import sdk_sample.sdk.result.TokenResult;
import sdk_sample.sdk.utils.HttpUtils;
import sdk_sample.sdk.utils.SharedPreferencesUtil;
import sdk_sample.sdk.utils.ToolsUtil;

import static com.xinspace.csevent.app.AppConfig.ACTION_INSTALL_SHORTCUT;
import static com.xinspace.csevent.app.AppConfig.COMMUNITY_AREA;

/**
 * 门禁开锁的Fragment
 * author by mqz
 * Created by Android on 2017/8/16.
 */
public class OpenDoorFragment extends Fragment implements View.OnClickListener,EasyPermissions.PermissionCallbacks{

    private static Boolean isIndexFrom;
    private Handler handler;
    private LinphonePreferences mPrefs;
    private SDPreference preference;
    private String cToken;
    private String cUid;
    private String mobile;
    private ViewPager vp_adv;
    private TextView tv_notice, tv_shop_title;
    private TextSwitcher ts_notice;
    private ImageView img_setting;
    private RelativeLayout rel_notice;
    private LinearLayout ll_open_monitoring;
    private LinearLayout ll_open2;
    private LinearLayout ll_open3;
    private LinearLayout ll_open4;
    private LinearLayout ll_open5;
    private ImageView ivOpenDoor;
    private SipEvent sipEvent;

    //广告滚动
    private static final int HANDLER_SCROLL_ADVERTISEMENT = 10;
    private boolean scroll_flag = true;//标记变量,用于判断是否自动滚动广告
    private List<ImageView> adv_list;
    private Timer timer = new Timer();
    private boolean isFirst = true;//第一次设置indicator
    // 要显示的文本
    private List<String> newsList = new ArrayList<>();

    private String token;
    private long dealTime;

    private String flag;
    private int index = 0;//textview上下滚动下标
    public static final int NEWS_MESSAGE_TEXTVIEW = 300;//通知公告信息
    private List<LockBean> lockList;
    private static final int CALL_ACTIVITY = 19;

    /**
     * 扫描跳转Activity RequestCode
     */
    public static final int REQUEST_CODE = 111;
    /**
     * 选择系统图片Request Code
     */
    public static final int REQUEST_IMAGE = 112;

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
                default:
                    break;
            }
        }
    };

    private TimerTask timeTask = new TimerTask() {
        @Override
        public void run() {
            mHandler.sendEmptyMessage(HANDLER_SCROLL_ADVERTISEMENT);
        }
    };
    private String userId;
    private int mAlwaysChangingPhoneAngle;
    private LinphoneCoreListenerBase mListener;
    private String domain_sn;
    private DatabaseHelper databaseHelper;
    private LocalOrientationEventListener mOrientationHelper;
    private static final int REQUEST_CAMERA_PERM = 101;
    private Gson gson = new Gson();
    private FrameLayout container;
    private static int isRegist;
    private TextView tv_community;

    private String NEIBOR_ID = "100032";    //汕头ID
    private String area;
    private RelativeLayout relativeTopTitle;
    private CustomPopWindow popWindow;
    private ImageView imgForwardRight;
    private TextView gz, st;


    public static OpenDoorFragment newInstance(Boolean flag){
        OpenDoorFragment fragment = new OpenDoorFragment();
        isIndexFrom = flag;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_opendoor, container, false);
        handler = new Handler();
        area = SharedPreferencesUtil1.getString(getActivity(), COMMUNITY_AREA, "");
        mPrefs = LinphonePreferences.instance();
        preference = SDPreference.getInstance();
        sipEvent = new SipEvent();


        boolean isLogin = CoresunApp.isLogin;
        if (!isLogin){
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
        ToastUtils.init(getActivity());
        initLinPhoneConfig();
        cToken = preference.getContent("cToken");
        cUid = preference.getContent("cUid");
        mobile = preference.getContent("mobile");
        initView(view);
        verifyRegisterStatus();

        setDefaultCommunity();  //设置广州社区为默认社区

        return view;
    }

    private void initView(View view) {

        relativeTopTitle = (RelativeLayout)view.findViewById(R.id.relative_shop_title);
        tv_shop_title = (TextView)view.findViewById(R.id.tv_shop_title);
        tv_shop_title.setOnClickListener(this);
        imgForwardRight = (ImageView) view.findViewById(R.id.img_forward_right);

        container = (FrameLayout)view.findViewById(R.id.community_unit_container);
        vp_adv = (ViewPager) view.findViewById(R.id.vp_ad);
        tv_notice = (TextView) view.findViewById(R.id.tv_notice);
        tv_notice.setSelected(true);

        ts_notice = (TextSwitcher) view.findViewById(R.id.ts_notice);
        img_setting = (ImageView) view.findViewById(R.id.img_setting);
        img_setting.setOnClickListener(this);


        ts_notice.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView textView = new TextView(getActivity());
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

        rel_notice = (RelativeLayout) view.findViewById(R.id.rel_notice);
        rel_notice.setOnClickListener(this);

        ll_open_monitoring = (LinearLayout) view.findViewById(R.id.ll_open_monitoring);
        ll_open2 = (LinearLayout) view.findViewById(R.id.ll_open2);
        ll_open3 = (LinearLayout) view.findViewById(R.id.ll_open3);
        ll_open4 = (LinearLayout) view.findViewById(R.id.ll_open4);
        ll_open5 = (LinearLayout) view.findViewById(R.id.ll_open5);

        ll_open_monitoring.setOnClickListener(this);
        ll_open2.setOnClickListener(this);
        ll_open3.setOnClickListener(this);
        ll_open4.setOnClickListener(this);
        ll_open5.setOnClickListener(this);

        ivOpenDoor = (ImageView) view.findViewById(R.id.iv_open_door);
        ivOpenDoor.setOnClickListener(this);

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
                SharedPreferencesUtil1.saveString(getActivity(), COMMUNITY_AREA, "gz");
                popWindow.dissmiss();
                tv_shop_title.setTextColor(Color.WHITE);
                imgForwardRight.setImageResource(R.drawable.icon_group_right);
                break;

            case R.id.tv_community_st:
                tv_shop_title.setText(st.getText().toString());
                tv_shop_title.setTextColor(Color.GREEN);
                NEIBOR_ID = "100032";  //汕头
                SharedPreferencesUtil1.saveString(getActivity(), COMMUNITY_AREA, "st");
                popWindow.dissmiss();
                tv_shop_title.setTextColor(Color.WHITE);
                imgForwardRight.setImageResource(R.drawable.icon_group_right);
                break;

            case R.id.iv_open_door:  // 开门
                flag = "1";
                if (isRegist==2){
                    doGetToken();
                }
                else if (isRegist==1){
                    ToastUtils.showToast("用户正在审核中");
                }
                else{
                    ToastUtils.showToast("尚未注册社区");
                    popVerifyDialog();
                }

                break;
            case R.id.ll_open_monitoring:  // 门禁监控InCallAct
                flag = "2";
                if (isRegist==2){
                    doGetToken();
                }
                else if (isRegist==1){
                    ToastUtils.showToast("用户正在审核中");
                }
                else{
                    ToastUtils.showToast("尚未注册社区");
                    popVerifyDialog();
                }
                break;
            case R.id.ll_open2: // 密码开门
                flag = "3";
                if (isRegist==2){
                    doGetToken();
                }
                else if (isRegist==1){
                    ToastUtils.showToast("用户正在审核中");
                }
                else{
                    ToastUtils.showToast("尚未注册社区");
                    popVerifyDialog();
                }

                break;
            case R.id.ll_open3: // 授权开门
                ToastUtil.makeToast("此功能待完善");
                break;
            case R.id.ll_open4: // 开门记录
                Intent intent4 = new Intent(getActivity(), OpenDoorRecordAct.class);
                startActivity(intent4);
                break;

            case R.id.ll_open5: // 添加桌面
                addShotcut();
                break;


            case R.id.rel_notice: // 公告
                Intent intent5 = new Intent(getActivity(), AfficheAct.class);
                startActivity(intent5);
                break;
            case R.id.img_setting:
                Intent intent = new Intent(getActivity(), ComAppAct.class);
                startActivity(intent);
                break;

        }
    }

    private void addShotcut() {
        Intent addShortcutIntent = new Intent(ACTION_INSTALL_SHORTCUT);

        // 不允许重复创建，不是根据快捷方式的名字判断重复的
        addShortcutIntent.putExtra("duplicate", false);
        addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "电子门卡");

        //图标
        addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(getActivity(), R.drawable.shide_shotcut));

        // 设置关联程序
        Intent launcherIntent = new Intent();
        launcherIntent.putExtra("launch_mode", "shotcut");
        launcherIntent.setClass(getActivity(), MainActivity.class);
        addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launcherIntent);

        // 发送广播
        getActivity().sendBroadcast(addShortcutIntent);
    }

    private void showPopBottom() {
        popWindow = new CustomPopWindow.PopupWindowBuilder(getActivity())
                .setView(R.layout.title_pop_layout)
                .setFocusable(true)
                .setOutsideTouchable(false)
                .setOnDissmissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        imgForwardRight.setImageResource(R.drawable.icon_group_right);
                    }
                })
                .create();

        int x_start = ScreenUtils.getScreenWidth(getActivity())/2 - 250;
        popWindow.showAsDropDown(relativeTopTitle, x_start, 10);
        gz = (TextView)popWindow.getPopupWindow().getContentView().findViewById(R.id.tv_community_gz);
        st = (TextView)popWindow.getPopupWindow().getContentView().findViewById(R.id.tv_community_st);
        gz.setOnClickListener(this);
        st.setOnClickListener(this);
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

    /**
     * 检查用户当前状态
     * status:  1  用户处于审核状态
     *          2  用户处于通过状态
     *          0  用户尚未注册社区
     */
    private void verifyRegisterStatus() {
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
                        popVerifyDialog();

                    }
                    else if (status.equals("1")){
                        ToastUtil.makeToast("当前用户正在审核中");
                    }
                    else{
                        isRegist = 2;
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

    /**
     * 弹窗让用户添加社区
     * author by Mqz
     */
    private void popVerifyDialog() {

        MaterialSimpleListAdapter listAdapter = new MaterialSimpleListAdapter(new MaterialSimpleListAdapter.Callback() {
            @Override
            public void onMaterialListItemSelected(MaterialDialog dialog, int index, MaterialSimpleListItem item) {
                switch (index){
                    case 0:
                        cameraTask();
                        dialog.dismiss();
                        break;
                    case 1:
                        Intent intent = new Intent(getActivity() , SubmitDataAct.class);
                        startActivity(intent);
                        dialog.dismiss();
                        break;
                }
            }
        });

        listAdapter.add(
                new MaterialSimpleListItem.Builder(getActivity())
                .content("扫一扫")
                .icon(R.drawable.qtcode)
                .backgroundColor(Color.WHITE)
                .build()
        );

        listAdapter.add(
                new MaterialSimpleListItem.Builder(getActivity())
                .content("添加社区")
                .icon(R.drawable.icon_userhead_no_login)
                .backgroundColor(Color.WHITE)
                .build()
        );

        new MaterialDialog.Builder(getActivity())
                .title(R.string.unit_choose_notice)
                .items(R.array.unit_add_items)
                .adapter(listAdapter, null)
                .show();
    }

    private void doGetToken() {
        String key = "2";
        String userName = mobile;
        String app_id = "2";
        if (TextUtils.isEmpty(key)) {
            ToolsUtil.toast(getActivity(), "key不能为空！");
            return;
        }
        if (TextUtils.isEmpty(userName)) {
            ToolsUtil.toast(getActivity(), "userName不能为空！");
            return;
        }
        if (TextUtils.isEmpty(app_id)) {
            ToolsUtil.toast(getActivity(), "appId不能为空！");
            return;
        }

        ToolsUtil.getToken(getActivity(), key, userName, app_id, null, new HttpRespListener() {
            @Override
            public void onSuccess(int code, BaseResult result) {
                TokenResult tokeResult = (TokenResult) result;
                token = tokeResult.getToken();
                dealTime = tokeResult.getDead_time();
                mHandler.obtainMessage(1000).sendToTarget();

                preference.putContent("coresun_token", token);
                preference.putContent("coresun_dealtime", dealTime+"");
                Log.i("www", "dealtime" + dealTime);
                Log.i("www", "token" + token);
            }

            @Override
            public void onFail(int code, String msg) {
                if (code == 202){
                    ToolsUtil.toast(getActivity(), "亲！请检查你的网络是否正常哦~");
                }
            }
        });
    }


    /**
     * 获取的社区
     */
    private void doGetNeiborId(String flag) {

        Log.i("www", "------------社区列表请求成功---------------");

        final String userName = mobile;
        if (TextUtils.isEmpty(token)) {
            ToolsUtil.toast(getActivity(), "token不能为空！");
            return;
        }
        if (TextUtils.isEmpty(userName)) {
            ToolsUtil.toast(getActivity(), "userName不能为空！");
            return;
        }

        if (TextUtils.isEmpty(NEIBOR_ID)) {
            ToolsUtil.toast(getActivity(), "neibor_flag不能为空");
            return;
        }

        final Map<String, String> headpParams = new HashMap<String, String>();
        headpParams.put("token", token);
        headpParams.put("username", userName);
        headpParams.put("dealtime", dealTime + "");
        final Map<String, String> params = new HashMap<String, String>();
        params.put("username", userName);
        params.put("neibor_flag", NEIBOR_ID);

        Log.i("www", "username" + userName);
        Log.i("www", "neibor_flag" + NEIBOR_ID);

        ToolsUtil.goToOpenDoor(getActivity(), headpParams, params, flag, new HttpRespListener() {
            @Override
            public void onSuccess(int code, BaseResult result) {
                //请求成功回调
                Log.i("www", "code   " + code + "result   " + result.toString());

                try {

                    toOpenDoor(result, userName);

                } catch (Exception var15) {
                    var15.printStackTrace();
                }
            }

            @Override
            public void onFail(int code, String msg) {

                //请求失败回调
                Log.i("www", "失败code   " + code + "msg   " + msg.toString());

                ToolsUtil.toast(getActivity(), msg);

            }
        });
    }

    /**
     * 处理开门事务，
     *      如果只有单项，直接实现业务。
     *      否则，进入业务列表
     * @param result
     * @param userName
     * @throws JSONException
     * modify by Mqz
     */
    private void toOpenDoor(BaseResult result, final String userName) throws JSONException {
        JSONObject e = new JSONObject(result.getMsg());
        JSONObject jsonObject = e.getJSONObject("result");

        JSONObject jsonObject1 = jsonObject.getJSONObject("user_msg");
        final String neiborId = jsonObject.getString("neibor_id");
        final String user_sip = jsonObject1.getString("user_sip");
        final String sip_password = jsonObject1.getString("user_password");
        final String sip_domin = jsonObject1.getString("fs_ip");
        final int sip_port = jsonObject1.getInt("fs_port");

        final String path = "https://" + jsonObject.getString("fip") + ":" + jsonObject.getInt("fport");

        HttpUtils.getLockList(path, token, userName, neiborId, new HttpRespListener() {
            @Override
            public void onSuccess(int var1, BaseResult baseResult) {
                LockListResult result = (LockListResult) baseResult;
                lockList = result.getLockList();
                OpenLock(neiborId, user_sip, sip_password, sip_domin, sip_port, path, userName);
            }

            @Override
            public void onFail(int var1, String var2) {

            }
        });

        SharedPreferencesUtil.saveData(getActivity(), "sayee_tow_url_key", path);
        SharedPreferencesUtil.saveData(getActivity(), "sayee_user_sip_domain_key", sip_domin);
        SharedPreferencesUtil.saveData(getActivity(), "sayee_user_name_key", userName);
        SharedPreferencesUtil.saveData(getActivity(), "sayee_user_token_key", token);
        SharedPreferencesUtil.saveData(getActivity(), "sayee_deal_time", Long.valueOf(dealTime));
    }

    private void OpenLock(String neiborId, final String sip_number, String sip_password, String sip_domin, int sip_port, final String path, final String userName) {
        if (lockList.size() == 1){
            LogUtil.e("xx1 --> " + Thread.currentThread().getName());
            LockBean lockbean = lockList.get(0);
            if (flag.equals("1")){
                domain_sn = lockbean.getDomain_sn();
                SharedPreferencesUtil.saveData(getActivity(),  "sayee_to_sip_number_key", sip_number);
                openDoorByRequest(sip_number, path, domain_sn, userName);   //请求开锁
            }
            else if (flag.equals("2")){
                LockBean lockListBean = lockList.get(0);
                String sipNum = lockListBean.getSip_number();
                sipEvent.setToken(token);
                sipEvent.setUserName(userName);
                sipEvent.setPath(path);
                sipEvent.setToSipNumber(lockListBean.getSip_number());
                sipEvent.setDomain_sn(lockListBean.getDomain_sn());
                LogUtil.e("sipNum--> " + lockListBean.getSip_number() + "\nDomainNum --> " + lockListBean.getDomain_sn());
                LinphoneManager.getInstance().newOutgoingCall(sipNum, sipNum);
            }
            else if (flag.equals("3")){
                final String sipNumber = lockbean.getSip_number();
                HttpUtils.getOpenLockPassword(path, token, userName, sipNumber, new HttpRespListener() {
                    public void onSuccess(int code, BaseResult result) {
                        OpenLockPasswordBean bean = ((OpenLockPasswordResult) result).getResult();
                        Log.i("www", "没有跳转么");
                        Intent intent3 = new Intent(getActivity(), PassWordAct.class);
                        intent3.putExtra("sayee_random_password", bean.getRandom_pw());
                        intent3.putExtra("path", path);
                        intent3.putExtra("token", token);
                        intent3.putExtra("userName", userName);
                        intent3.putExtra("sip_number", sipNumber);
                        intent3.putExtra("dead_time", bean.getRandomkey_dead_time());
                        startActivity(intent3);
                    }

                    public void onFail(int code, String msg) {
                        ToolsUtil.toast(getActivity(), msg);
                        if (code != 3) {
                            ToolsUtil.toast(getActivity(), msg);
                        } else {
                            Intent intent = new Intent();
                            intent.setAction("com.sayee.sdk.action.token.fail");
                            intent.putExtra("sayee_callback_code", 2);
                            intent.putExtra("sayee_error_msg", "token重新获取失败");
                            getActivity().sendBroadcast(intent);
                        }
                    }
                });
            }

        }
        else{
            ArrayList<String> sipNums = new ArrayList<>();
            for (LockBean bean : lockList) {
                sipNums.add(bean.getSip_number());
            }
            
            Intent intent = new Intent(getActivity(), LockListActivity.class);
            intent.putStringArrayListExtra("sipNums", sipNums);
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

        }
    }

    private void openDoorByRequest(String sip_number, String path, final String doMain_sn, String userName) {
        HttpUtils.openDoorLock(getActivity(), path, token, userName,
                doMain_sn, 0, sip_number, null, new HttpRespListener() {
            @Override
            public void onSuccess(int code, BaseResult var2) {
                LockRecordBean bean = new LockRecordBean();
                bean.setUid(cUid);
                bean.setToken(cToken);
                bean.setPhone(mobile);
                bean.setEquip_sn(doMain_sn);
                bean.setType("1");
                addEntranceRecord(bean);
                ToolsUtil.toast(getActivity() , "已发送开锁请求");
            }

            @Override
            public void onFail(int code, String msg) {
                if(code == 3) {
                    Intent intent = new Intent();
                    intent.setAction("com.sayee.sdk.action.token.fail");
                    intent.putExtra("sayee_callback_code", 1);
                    intent.putExtra("sayee_error_msg", "token重新获取失败");
                    getActivity().sendBroadcast(intent);
                } else {
                    ToolsUtil.toast(getActivity(), msg);
                }
            }
        });
    }

    private void addEntranceRecord(LockRecordBean bean) {
        GetDataBiz.ADD_LOCK_RECORD(area, bean, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                LogUtil.i("开门记录上传返回" + result);

                if (result == null || result.equals("")){
                    return;
                }
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getInt("code") == 200){
                    LogUtil.i("开门记录上传成功");
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

    //获取广告
    private void getAdvertisement() {
        GetAdvertisementBiz.getAdvs3(getActivity(), new HttpRequestListener() {
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
                ImageView image = new ImageView(getActivity());
                image.setScaleType(ImageView.ScaleType.FIT_XY);
                ImagerLoaderUtil.displayImageWithLoadingIcon(url, image, R.drawable.advertisement_loading);
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

    private void initLinPhoneConfig() {
        LinphoneCoreFactory.instance().setDebugMode(true, "CoreSun-MQZ");//打印debug
        int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
        switch (rotation) {
            case Surface.ROTATION_0:
                rotation = 0;
                break;
            case Surface.ROTATION_90:
                rotation = 90;
                break;
            case Surface.ROTATION_180:
                rotation = 180;
                break;
            case Surface.ROTATION_270:
                rotation = 270;
                break;
        }

//        LinphoneManager.getLc().setDeviceRotation(rotation);
        mAlwaysChangingPhoneAngle = rotation;

        //监听回调
        mListener = new LinphoneCoreListenerBase() {
            @Override
            public void messageReceived(LinphoneCore lc, LinphoneChatRoom cr, LinphoneChatMessage message) {

            }

            @Override
            public void registrationState(LinphoneCore lc, LinphoneProxyConfig proxy, LinphoneCore.RegistrationState state, String smessage) {
                if (state.equals(LinphoneCore.RegistrationState.RegistrationCleared)) {
                    if (lc != null) {
                        LinphoneAuthInfo authInfo = lc.findAuthInfo(proxy.getIdentity(), proxy.getRealm(), proxy.getDomain());
                        if (authInfo != null)
                            lc.removeAuthInfo(authInfo);
                    }
                }
            }

            @Override
            public void callState(LinphoneCore lc, LinphoneCall call, LinphoneCall.State state, String message) {
                if (state == LinphoneCall.State.IncomingReceived) {
                    LogUtil.i("LockListActivity这里是电话打进来界面");
                    startActivity(new Intent(getActivity(), org.linphone.IncomingCallActivity.class));
                } else if (state == LinphoneCall.State.OutgoingInit) {

                    if (call.getCurrentParamsCopy().getVideoEnabled()) {
                        startVideoActivity(call);
                    } else {
                        LogUtil.i( "LockListActivity这里是电话打进来界面" + 11111);
                        startIncallActivity(call);
                    }
                } else if (state == LinphoneCall.State.CallEnd || state == LinphoneCall.State.Error || state == LinphoneCall.State.CallReleased) {

                }
            }
        };

        LinphoneCore lc = LinphoneManager.getLcIfManagerNotDestroyedOrNull();//实例化LinPhoneCore, LinphoneManger的465行代码初始化实例。
        if (lc != null) {
            org.linphone.mediastream.Log.i("www", "走到这里添加监听了没");
            lc.addListener(mListener);
        }
    }

    public void startVideoActivity(LinphoneCall currentCall) {
        Log.i("www", "走到这里了11111111111111");
        Intent intent = new Intent(getActivity(), InCallActivity.class);
        intent.putExtra("VideoEnabled", true);
        intent.putExtra("sipEvent", sipEvent);
        startOrientationSensor();
        startActivityForResult(intent, CALL_ACTIVITY);
    }

    public void startIncallActivity(LinphoneCall currentCall) {

        if (TextUtils.isEmpty(domain_sn)) {
            domain_sn = getDatabaseHelper().getDomainSn(currentCall.getRemoteAddress().getUserName());
        }
        Log.i("www", "走到这里了2222222222222 对方打进来的" + currentCall.getRemoteAddress().getUserName() + "domain_sn" + domain_sn);
        Intent intent = new Intent(getActivity(), InCallActivity.class);
        sipEvent.setDomain_sn(domain_sn);
        sipEvent.setToSipNumber(currentCall.getRemoteAddress().getUserName());
        intent.putExtra("VideoEnabled", true);
        intent.putExtra("sipEvent", sipEvent);
        startOrientationSensor();
        startActivityForResult(intent, CALL_ACTIVITY);
    }

    private DatabaseHelper getDatabaseHelper() {
        if (this.databaseHelper == null) {
            this.databaseHelper = new DatabaseHelper(getActivity().getApplicationContext());
        }
        return this.databaseHelper;
    }

    /**
     * Register a sensor to track phoneOrientation changes
     */
    private synchronized void startOrientationSensor() {
        if (mOrientationHelper == null) {
            mOrientationHelper = new LocalOrientationEventListener(getActivity());
        }
        mOrientationHelper.enable();
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        LogUtil.e("已具备以下权限\n" + perms.toString());
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        LogUtil.e("已拒绝" + perms + "权限");
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)){
            for (int i = 0; i < perms.size() - 1; i++) {
                if (perms.get(i) == Manifest.permission.CAMERA){
                    showAppPermissionDialog(R.string.permission_camera);
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * 提示用户是否赋予权限
     * @param content
     */
    private void showAppPermissionDialog(int content) {
        new AppSettingsDialog.Builder(this)
                .setTitle("权限申请")
                .setRationale(content)
                .setNegativeButton("拒绝")
                .setPositiveButton("同意")
                .setRequestCode(REQUEST_CAMERA_PERM)
                .build()
                .show();
    }

    /**
     * 调用相机时验证权限
     * 例如，扫二维码，照相，录制视频等功能时需要验证此权限。
     */
    @AfterPermissionGranted(REQUEST_CAMERA_PERM)
    private void cameraTask(){
        if (EasyPermissions.hasPermissions(getActivity(), Manifest.permission.CAMERA)){
            //正常的二维码扫描
            Intent intent = new Intent(CoresunApp.context, CaptureActivity.class);
            this.startActivityForResult(intent, REQUEST_CODE);
        }
        else {
            EasyPermissions.requestPermissions(this, "当前操作需要相机权限喔~", REQUEST_CAMERA_PERM, Manifest.permission.CAMERA);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    if (!result.contains("|")){
                        ToastUtils.showToast("无法识别此二维码");
                        return;
                    }
                    String[] ids = result.split("\\|");
                    String communityId = ids[0];
                    String unitId = ids[1];
                    preference.putContent("community_id", communityId);
                    preference.putContent("unit_id", unitId);
                    GetDataBiz.GET_COMMUNITY_LIST(area, cUid, cToken, unitId, new HttpRequestListener() {
                        @Override
                        public void onHttpRequestFinish(String result) throws JSONException {
                            CommunityUnit bean = gson.fromJson(result, CommunityUnit.class);
                            if (bean.getCode() == 200){
                                EventBus.getDefault().post(new FirstEvent("11", bean));
                            }
                            else{
                                ToastUtils.showToast("获取列表失败");
                            }
                        }

                        @Override
                        public void onHttpRequestError(String error) {
                            LogUtil.e(error);
                        }
                    });
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    ToastUtils.showToast("解析二维码失败");
                }
            }
        }

        /**
         * 选择系统图片并解析
         */
        else if (requestCode == REQUEST_IMAGE) {
            if (data != null) {
                Uri uri = data.getData();
                try {
                    CodeUtils.analyzeBitmap(ImageUtil.getImageAbsolutePath(getActivity(), uri), new CodeUtils.AnalyzeCallback() {
                        @Override
                        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                            ToastUtils.showToast("解析结果:" + result);
                        }

                        @Override
                        public void onAnalyzeFailed() {
                            ToastUtils.showToast("解析二维码失败");
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        else if (requestCode == REQUEST_CAMERA_PERM) {
            ToastUtils.showToast("从设置页面返回");
        }
    }


    private class LocalOrientationEventListener extends OrientationEventListener {
        public LocalOrientationEventListener(Context context) {
            super(context);
        }

        @Override
        public void onOrientationChanged(final int o) {
            if (o == OrientationEventListener.ORIENTATION_UNKNOWN) {
                return;
            }

            int degrees = 270;
            if (o < 45 || o > 315)
                degrees = 0;
            else if (o < 135)
                degrees = 90;
            else if (o < 225)
                degrees = 180;

            if (mAlwaysChangingPhoneAngle == degrees) {
                return;
            }
            mAlwaysChangingPhoneAngle = degrees;

            org.linphone.mediastream.Log.d("Phone orientation changed to ", degrees);
            int rotation = (360 - degrees) % 360;
            LinphoneCore lc = LinphoneManager.getLcIfManagerNotDestroyedOrNull();
            if (lc != null) {
                lc.setDeviceRotation(rotation);
                LinphoneCall currentCall = lc.getCurrentCall();
                if (currentCall != null && currentCall.cameraEnabled() && currentCall.getCurrentParamsCopy().getVideoEnabled()) {
                    lc.updateCall(currentCall, null);
                }
            }
        }
    }

}
