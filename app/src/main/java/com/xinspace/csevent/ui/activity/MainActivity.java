package com.xinspace.csevent.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.xinspace.csevent.app.AppConfig;
import com.xinspace.csevent.app.CoresunApp;
import com.xinspace.csevent.monitor.activity.OpenDoorActivity;
import com.xinspace.csevent.monitor.activity.SubmitDataAct;
import com.xinspace.csevent.monitor.bean.SipEvent;
import com.xinspace.csevent.R;
import com.xinspace.csevent.app.weiget.NetWorkHelper;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.biz.GetDataBiz;

import com.xinspace.csevent.myinterface.BackHandleInterface;
import com.xinspace.csevent.myinterface.BaseFragment;
import com.xinspace.csevent.ui.fragment.AirFragment;
import com.xinspace.csevent.ui.fragment.CommunityUnitFragment;
import com.xinspace.csevent.ui.fragment.OpenDoorFragment;
import com.xinspace.csevent.ui.fragment.SearchFragment;
import com.xinspace.csevent.login.activity.LoginActivity;
import com.xinspace.csevent.login.activity.ViedoAct;
import com.xinspace.csevent.data.sharedprefs.AppSharedPrefs;
import com.xinspace.csevent.ui.widget.NoTouchViewPager;
import com.xinspace.csevent.shop.weiget.UIAlertView;
import com.xinspace.csevent.ui.fragment.MainPageFragment;
import com.xinspace.csevent.ui.fragment.MinePageFragment;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.sweepstake.modle.FirstEvent;
import com.xinspace.csevent.sweepstake.weiget.CheckNetDialog;
import com.xinspace.csevent.util.AndroidPermissionUtils;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.SharedPreferencesUtil1;
import com.xinspace.csevent.util.StatusBarUtils;
import com.xinspace.csevent.util.ToastUtil;
import com.xinspace.csevent.util.ToastUtils;
import com.yinglan.alphatabs.AlphaTabsIndicator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;
import org.linphone.InCallActivity;
import org.linphone.LinphoneManager;
import org.linphone.LinphonePreferences;
import org.linphone.LinphoneService;
import org.linphone.core.LinphoneAddress;
import org.linphone.core.LinphoneCall;
import org.linphone.core.LinphoneCore;
import org.linphone.core.LinphoneCoreException;
import org.linphone.core.LinphoneCoreFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import sdk_sample.sdk.HttpRespListener;
import sdk_sample.sdk.db.DatabaseHelper;
import sdk_sample.sdk.result.BaseResult;
import sdk_sample.sdk.result.LoginSipBean;
import sdk_sample.sdk.result.TokenResult;
import sdk_sample.sdk.utils.ToolsUtil;

import static android.content.Intent.ACTION_MAIN;
import static com.xinspace.csevent.app.AppConfig.COMMUNITY_AREA;

/**
 * 主页
 */
public class MainActivity extends FragmentActivity implements EasyPermissions.PermissionCallbacks, BackHandleInterface {

    private boolean exit = false;//是否退出应用
    private long mExitTime;
    private static final int location = 10;
    private boolean isHaveNet;
    private CheckNetDialog dialog;
    private SDPreference sdPreference;
    private FragmentTransaction transaction;

    private String getNeiborId;
    private MainActivity.ServiceWaitThread mThread;

    private String mobile;
    private String token;
    private long dealTime;

    private String sip_number;
    private String sip_password;
    private String sip_domin;
    private int sip_port;
    private String domain_sn;
    private LinphonePreferences mPrefs;
    private boolean accountCreated = false;
    private LinphoneAddress address;
    private DatabaseHelper databaseHelper = null;
    private SipEvent sipEvent;
    private static final int CALL_ACTIVITY = 19;
    private OrientationEventListener mOrientationHelper;
    private String userName;
    private String path;
    private static MainActivity instance;

    //Create by mjz  2017/8/16
    private NoTouchViewPager main_viewpager;
    private AlphaTabsIndicator alphaTabsIndicator;
    private List<Fragment> mainfragments = new ArrayList<>();
    private String flag;
    private static final int REQUEST_CAMERA_PERM = 101;
    private CommunityUnitFragment unitFragment;//扫二维码添加社区Fragment
    private BaseFragment mBackHandedFragment;
    private String area;

    public static final boolean isInstanciated() {
        return instance != null;
    }

    public static final MainActivity instance() {
        if (instance != null)
            return instance;
        throw new RuntimeException("MainActivity not instantiated yet");
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //广告滚动
                case 200:
                    if (msg.obj != null) {
                        String status = (String) msg.obj;
                        if (status.equals("0")) {
                            ToastUtil.makeToast("请完善社区资料");
                            Intent intent = new Intent(MainActivity.this, SubmitDataAct.class);
                            startActivity(intent);
                        } else if (status.equals("1")) {
                            ToastUtil.makeToast("审核中");
                        } else if (status.equals("2")) {
                            //EventBus.getDefault().post(new FirstEvent("1"));
                            Intent intent = new Intent(MainActivity.this, OpenDoorActivity.class);
                            startActivity(intent);
                        }
                    }
                    break;
                case 400:
                    ToastUtil.makeToast("请你联系客服");
                    break;
                case 1000:  //获取用户的sip账号

                    doGetNeiborId("1");

                    break;
                case 1001:
                    LogUtil.i("获取用户的sip账户成功");
                    if (msg.obj != null) {
                        LoginSipBean loginSipBean = (LoginSipBean) msg.obj;
                        sip_number = loginSipBean.getSip_number();
                        sip_password = loginSipBean.getSip_password();
                        sip_domin = loginSipBean.getSip_domin();
                        sip_port = loginSipBean.getSip_port();

                        token = loginSipBean.getToken();
                        userName = loginSipBean.getUserName();
                        path = loginSipBean.getPath_url();

                        sipEvent = new SipEvent();
                        sipEvent.setToken(token);
                        sipEvent.setUserName(userName);
                        sipEvent.setPath(path);

                        LogUtil.i("sip_number" + sip_number + "sip_password" + sip_password + "sip_domin" + sip_domin);
                        if (sip_number != null && sip_password != null && sip_domin != null) {
                            logIn(sip_number, sip_password, sip_domin, false);
                        }
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(MainActivity.this, R.color.app_bottom_color);
        setContentView(R.layout.activity_main);
        instance = this;

        ToastUtils.init(this);
        sdPreference = SDPreference.getInstance();
        mPrefs = LinphonePreferences.instance();
        EventBus.getDefault().register(this);
        area = SharedPreferencesUtil1.getString(this, COMMUNITY_AREA, "");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkAllPermission();
        }

        isHaveNet = NetWorkHelper.getNetState();
        if (isHaveNet == false) {
            dialogShow();
        }

        flag = getIntent().getStringExtra("flag");

        //初始化个推sdk
//        PushManager.getInstance().initialize(this.getApplicationContext());
//        String cid=PushManager.getInstance().getClientid(SaiYiAct.this);

        //初始化组件
        initViews();

        //第一次登陆后可以直接再次登陆(偏好存储中取出用户信息)
        getUserInfoInLocal();

    }

    private void initViews() {
        main_viewpager = (NoTouchViewPager) findViewById(R.id.main_view_pager);
        alphaTabsIndicator = (AlphaTabsIndicator) findViewById(R.id.main_alphatabs_indicator);//底部导航容器
        MainAdapter mainAdapter = new MainAdapter(getSupportFragmentManager());
        main_viewpager.setAdapter(mainAdapter);
        main_viewpager.addOnPageChangeListener(mainAdapter);
        alphaTabsIndicator.setViewPager(main_viewpager);
        boolean isCommunityDefault = new AppSharedPrefs(this).getDefault();
        if (isCommunityDefault || flag != null) {
            alphaTabsIndicator.setTabCurrenItem(2);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        ToastUtils.showToast("已同意" + perms + "权限");
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        LogUtil.e("已拒绝" + perms + "权限");
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            for (int i = 0; i < perms.size() - 1; i++) {
                if (perms.get(i) == Manifest.permission.CAMERA) {
                    showAppPermissionDialog(R.string.permission_camera);
                }
                if (perms.get(i) == Manifest.permission.ACCESS_COARSE_LOCATION) {
                    showAppPermissionDialog(R.string.permission_location);
                }
                if (perms.get(i) == Manifest.permission.WRITE_EXTERNAL_STORAGE) {
                    showAppPermissionDialog(R.string.permission_sdcard);
                }
            }
        }
    }

    /**
     * 提示用户是否赋予权限
     *
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
    private void cameraTask() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)) {
            //拥有相机权限了，你可以为所以为了。
            ToastUtils.showToast("拥有相机权限了，你可以为所以为了");
        } else {
            EasyPermissions.requestPermissions(this, "当前操作需要相机权限喔~", REQUEST_CAMERA_PERM, Manifest.permission.CAMERA);
        }
    }

    @Override
    public void setSelectedFragment(BaseFragment selectedFragment) {
        this.mBackHandedFragment = selectedFragment;
    }

    private class ServiceWaitThread extends Thread {
        public void run() {
            while (!LinphoneService.isReady()) {
                try {
                    sleep(30);
                } catch (InterruptedException e) {
                    throw new RuntimeException("waiting thread sleep() has been interrupted");
                }
            }

            handler.post(new Runnable() {
                @Override
                public void run() {
                    //initView();
                    doGetToken();
                }
            });
            mThread = null;
        }
    }

    //进入广告引导页面
    private void goFullAdvPage() {
        startActivity(new Intent(this, FullPageAdsActivity.class));
    }

    //进入应用时,读取默认登录的用户信息
    private void getUserInfoInLocal() {
        String userid = SharedPreferencesUtil1.getString(this, "userId");
        CoresunApp.username = SharedPreferencesUtil1.getString(this, "username");
        CoresunApp.userIcon = SharedPreferencesUtil1.getString(this, "userIcon");
        if (userid.equals("")) {
            CoresunApp.USER_ID = null;
        }
    }

    //启动友盟统计分析的sdk
    @Override
    protected void onResume() {
        super.onResume();
        //判断用户是否登录过
        isLogin();
        if (!AppConfig.WEB_URL.equals("NO")) {
            Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
            intent.putExtra("data", AppConfig.WEB_URL);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            AppConfig.WEB_URL = "NO";
            startActivity(intent);
        }
    }


    //判断用户是否登录过
    private void isLogin() {
        String userId = sdPreference.getContent("cUid");
        LogUtil.i("上次登录的社区id:" + userId);
        if ("0".equals(userId)) {
            //没有用户登录过
            CoresunApp.USER_ID = null;
        } else {
            CoresunApp.USER_ID = userId;
            mobile = sdPreference.getContent("mobile");

            if (LinphoneService.isReady()) {
                LogUtil.i("linphone的服务启动");
            } else {
                // start linphone as background
                LogUtil.i("linphone的服务未启动");
                startService(new Intent(ACTION_MAIN).setClass(this, LinphoneService.class));
                mThread = new MainActivity.ServiceWaitThread();
                mThread.start();
            }
        }
    }

    private void checkAllPermission() {
        String[] unCheckPermissions = AndroidPermissionUtils.checkPermission(this);
        if (unCheckPermissions.length != 0) {
            ActivityCompat.requestPermissions(this, unCheckPermissions, 100);
        }
    }

    //停止友盟统计分析sdk
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        clickListener = null;
        handler.removeCallbacksAndMessages(null);
        setContentView(R.layout.empty_view);
        LogUtil.e("MainActivity excuted onDestory() method");
        System.gc();
    }

    @Subscribe
    public void onEventMainThread(FirstEvent event) {
        String msg = event.getEventMsg();
        LogUtil.i("-----------www----------------" + msg);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (msg.equals("11")) {
            if (unitFragment == null) {
                unitFragment = CommunityUnitFragment.newInstance();
            }
            Bundle bundle = new Bundle();
            bundle.putParcelable("data", event.getCommunityUnit());
            bundle.putString("flag", "1");
            unitFragment.setArguments(bundle);
            ft.add(R.id.community_unit_container, unitFragment, "commuintyunit");
            ft.addToBackStack(null);
        }
        ft.commit();
    }

    @Override
    public void onBackPressed() {

        if (mBackHandedFragment == null || !mBackHandedFragment.onBackPressed()) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
//                super.onBackPressed();
                if (!exit) {
                    exit = true;
                    ToastUtil.makeToast("再按一次退出程序");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            exit = false;
                        }
                    }, 2000);
                } else {
                    //退出程序
                    finish();
                }
            } else {
                getSupportFragmentManager().popBackStack();
            }
        }
    }

    private void showExitDialog() {
        final UIAlertView delDialog = new UIAlertView(MainActivity.this, "温馨提示", "请不要在后台杀死应用程序,否则您将无法使用门禁设备",
                "取消", "确定");
        delDialog.show();

        delDialog.setClicklistener(
                new UIAlertView.ClickListenerInterface() {
                    @Override
                    public void doLeft() {
                        delDialog.dismiss();
                    }

                    @Override
                    public void doRight() {
                        finish();
                        //stopService(new Intent(Intent.ACTION_MAIN).setClass(MainActivity.this, LinphoneService.class));
                        delDialog.dismiss();
                    }
                }
        );
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_main_video:
                    Intent intent1 = new Intent(MainActivity.this, ViedoAct.class);
                    startActivity(intent1);
                    break;
                case R.id.main_coresun:
                    openDoor();
                    break;
            }
        }
    };

    public void openDoor() {
        String uid = sdPreference.getContent("userId");
        if (uid.equals("0")) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        } else {
            queryAuditStatus();
        }
    }

    private void queryAuditStatus() {
        String cuid = sdPreference.getContent("cUid");
        String cToken = sdPreference.getContent("cToken");

        GetDataBiz.getqueryAuditStatus(area, cuid, cToken, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                LogUtil.i("获取审核状态列表" + result);
                if (result == null || result.equals("")) {
                    return;
                }

                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("code").equals("200")) {
                    JSONObject dataJsonObject = jsonObject.getJSONObject("data");
                    String status = dataJsonObject.getString("status");
                    handler.obtainMessage(200, status).sendToTarget();
                } else {
                    handler.obtainMessage(400).sendToTarget();
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }

    /*
     * 提示没网dialog
     */
    private void dialogShow() {
        dialog = new CheckNetDialog(MainActivity.this);
        TextView textView = (TextView) dialog.getTextView();
        textView.setText("当前没有网络，请去连接网络");
        dialog.setOnPositiveListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT > 10) {
                    // 3.0以上打开设置界面，也可以直接用ACTION_WIRELESS_SETTINGS打开到wifi界面
                    startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                } else {
                    startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                }
                dialog.dismiss();
            }
        });
        dialog.setOnNegativeListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

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
                //ToolsUtil.toast(getApplicationContext(), "成功: "+tokeResult.getToken());
                token = tokeResult.getToken();
                dealTime = tokeResult.getDead_time();
                handler.obtainMessage(1000).sendToTarget();

                Log.i("www", "dealtime" + dealTime);
                Log.i("www", "token" + token);
            }

            @Override
            public void onFail(int code, String msg) {
                //ToolsUtil.toast(getApplicationContext(), "您无使用权限");
            }
        });
    }


    /**
     * 获取的社区
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

        String neibor_flag = "100032";
        if (TextUtils.isEmpty(neibor_flag)) {
            ToolsUtil.toast(this, "neibor_flag不能为空");
            return;
        }

        final Map<String, String> headpParams = new HashMap<String, String>();
        headpParams.put("token", token);
        headpParams.put("username", userName);
        headpParams.put("dealtime", dealTime + "");
        final Map<String, String> params = new HashMap<String, String>();
        params.put("username", userName);
        params.put("neibor_flag", neibor_flag);

        Log.i("www", "username" + userName);
        Log.i("www", "neibor_flag" + neibor_flag);

        ToolsUtil.goToOpenDoor3(MainActivity.this, headpParams, params, falg, new HttpRespListener() {
            @Override
            public void onSuccess(int code, BaseResult result) {
                //请求成功回调
                Log.i("www", "code   " + code + "result   " + result.toString());
                LoginSipBean loginSipBean = result.getLoginSipBean();

                handler.obtainMessage(1001, loginSipBean).sendToTarget();
            }

            @Override
            public void onFail(int code, String msg) {

                //请求失败回调
                Log.i("www", "失败code   " + code + "msg   " + msg.toString());

            }
        });
    }

    /**
     * 登录sip账户
     *
     * @param username
     * @param password
     * @param domain
     * @param sendEcCalibrationResult
     */
    private void logIn(String username, String password, String domain, boolean sendEcCalibrationResult) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        saveCreatedAccount(username, password, domain);

        if (LinphoneManager.getLc().getDefaultProxyConfig() != null) {
            Log.i("www", "来走这里了");
            launchEchoCancellerCalibration(sendEcCalibrationResult);
        }
    }


    /**
     * 创建登录一个sip账号
     */
    public void saveCreatedAccount(String username, String password, String domain) {
        if (accountCreated)
            return;

        String identity = "sip:" + username + "@" + domain;
        try {
            address = LinphoneCoreFactory.instance().createLinphoneAddress(identity);
        } catch (LinphoneCoreException e) {
            e.printStackTrace();
        }
        boolean isMainAccountLinphoneDotOrg = domain.equals(getString(R.string.default_domain));     //true
        //boolean isMainAccountLinphoneDotOrg = true;
        boolean useLinphoneDotOrgCustomPorts = getResources().getBoolean(R.bool.use_linphone_server_ports);   //true

        Log.i("www", "identity" + identity + " isMainAccountLinphoneDotOrg   " + isMainAccountLinphoneDotOrg + "  useLinphoneDotOrgCustomPorts  " + useLinphoneDotOrgCustomPorts);

        LinphonePreferences.AccountBuilder builder = new LinphonePreferences.AccountBuilder(LinphoneManager.getLc())
                .setUsername(username)
                .setDomain(domain)
                .setPassword(password);

        if (isMainAccountLinphoneDotOrg && useLinphoneDotOrgCustomPorts) {
            String port = getString(R.string.default_port);
            if (getResources().getBoolean(R.bool.disable_all_security_features_for_markets)) {
                // builder.setProxy(domain + ":5228").setTransport(TransportType.LinphoneTransportTcp);
                builder.setProxy(domain + ":" + port).setTransport(LinphoneAddress.TransportType.LinphoneTransportTcp);
            } else {
                // 登录的账号
                //builder.setProxy(domain + ":5223").setTransport(TransportType.LinphoneTransportTcp);
                builder.setProxy(domain + ":" + port).setTransport(LinphoneAddress.TransportType.LinphoneTransportTcp);
            }

        } else {
            String forcedProxy = getResources().getString(R.string.setup_forced_proxy);
            if (!TextUtils.isEmpty(forcedProxy)) {
                builder.setProxy(forcedProxy)
                        .setOutboundProxyEnabled(true)
                        .setAvpfRRInterval(5);
            }
        }


        try {
            builder.saveNewAccount();
            accountCreated = true;
        } catch (LinphoneCoreException e) {
            e.printStackTrace();
        }
    }

    private void launchEchoCancellerCalibration(boolean sendEcCalibrationResult) {   //false
        boolean needsEchoCalibration = LinphoneManager.getLc().needsEchoCalibration();

        Log.i("www", "needsEchoCalibration  " + needsEchoCalibration + " mPrefs.isFirstLaunch()  " + mPrefs.isFirstLaunch());

        if (needsEchoCalibration && mPrefs.isFirstLaunch()) {
//            mPrefs.setAccountEnabled(mPrefs.getAccountCount() - 1, false); //We'll enable it after the echo calibration
//            EchoCancellerCalibrationFragment fragment = new EchoCancellerCalibrationFragment();
//            fragment.enableEcCalibrationResultSending(sendEcCalibrationResult);
//            changeFragment(fragment);
//            currentFragment = SetupFragmentsEnum.ECHO_CANCELLER_CALIBRATION;
//            back.setVisibility(View.VISIBLE);
//            next.setVisibility(View.GONE);
//            next.setEnabled(false);
//            cancel.setEnabled(false);
        } else {
            if (mPrefs.isFirstLaunch()) {
                mPrefs.setEchoCancellation(LinphoneManager.getLc().needsEchoCanceler());
            }
            success();
        }
    }


    public void success() {
        LogUtil.i("linphone sip账号登录成功了");
        mPrefs.firstLaunchSuccessful();
    }


    /**
     * 梯口机拨打手机
     *
     * @param currentCall
     */
    public void startIncallActivity(LinphoneCall currentCall) {

        if (TextUtils.isEmpty(this.domain_sn)) {
            domain_sn = this.getDatabaseHelper().getDomainSn(currentCall.getRemoteAddress().getUserName());
        }
        Log.i("www", "走到这里了2222222222222 对方打进来的" + currentCall.getRemoteAddress().getUserName() + "domain_sn" + domain_sn);

        Intent intent = new Intent(this, InCallActivity.class);
        sipEvent.setDomain_sn(domain_sn);
        sipEvent.setToSipNumber(currentCall.getRemoteAddress().getUserName());
        intent.putExtra("VideoEnabled", true);
        intent.putExtra("sipEvent", sipEvent);
        startOrientationSensor();
        startActivityForResult(intent, CALL_ACTIVITY);
    }


    /**
     * 手机拨打梯口机
     *
     * @param currentCall
     */
    public void startVideoActivity(LinphoneCall currentCall) {
        Log.i("www", "走到这里了11111111111111");
        Intent intent = new Intent(this, InCallActivity.class);
        intent.putExtra("VideoEnabled", true);
        intent.putExtra("sipEvent", sipEvent);
        startOrientationSensor();
        startActivityForResult(intent, CALL_ACTIVITY);
    }

    private DatabaseHelper getDatabaseHelper() {
        if (this.databaseHelper == null) {
            this.databaseHelper = new DatabaseHelper(this.getApplicationContext());
        }
        return this.databaseHelper;
    }


    /**
     * Register a sensor to track phoneOrientation changes
     */
    private synchronized void startOrientationSensor() {
        if (mOrientationHelper == null) {
            mOrientationHelper = new LocalOrientationEventListener(this);
        }
        mOrientationHelper.enable();
    }


    private int mAlwaysChangingPhoneAngle = -1;

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

    class MainAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {

        public MainAdapter(FragmentManager fm) {
            super(fm);
            mainfragments.add(new MainPageFragment());
            mainfragments.add(new AirFragment());
            mainfragments.add(OpenDoorFragment.newInstance(true));
            mainfragments.add(new SearchFragment());
            mainfragments.add(new MinePageFragment());
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position != 0) {
                EventBus.getDefault().post(new FirstEvent("22"));
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

        @Override
        public Fragment getItem(int position) {
            return mainfragments.get(position);
        }

        @Override
        public int getCount() {
            return mainfragments.size();
        }
    }

}
