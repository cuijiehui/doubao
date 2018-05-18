package sdk_sample.sdk.activity;

/**
 * Created by Android on 2017/3/23.
 * 开锁列表 界面
 */

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xinspace.csevent.monitor.activity.ComAppAct;
import com.xinspace.csevent.monitor.activity.PassWordAct;
import com.xinspace.csevent.monitor.bean.SipEvent;
import com.xinspace.csevent.R;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.biz.GetDataBiz;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.SharedPreferencesUtil1;
import com.xinspace.csevent.util.StatusBarUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;
import org.linphone.*;
import org.linphone.core.LinphoneAddress;
import org.linphone.core.LinphoneAuthInfo;
import org.linphone.core.LinphoneCall;
import org.linphone.core.LinphoneCall.State;
import org.linphone.core.LinphoneChatMessage;
import org.linphone.core.LinphoneChatRoom;
import org.linphone.core.LinphoneCore;
import org.linphone.core.LinphoneCoreException;
import org.linphone.core.LinphoneCoreFactory;
import org.linphone.core.LinphoneCoreListenerBase;
import org.linphone.core.LinphoneProxyConfig;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import sdk_sample.sdk.HttpRespListener;
import sdk_sample.sdk.adapter.LockListAdapter;
import sdk_sample.sdk.adapter.LockListAdapter2;
import sdk_sample.sdk.adapter.LockListAdapter3;
import sdk_sample.sdk.bean.ComBean;
import sdk_sample.sdk.bean.LockBean;
import sdk_sample.sdk.bean.LockRecordBean;
import sdk_sample.sdk.bean.OpenLockPasswordBean;
import sdk_sample.sdk.db.DatabaseHelper;
import sdk_sample.sdk.result.BaseResult;
import sdk_sample.sdk.result.LockListResult;
import sdk_sample.sdk.result.OpenLockPasswordResult;
import sdk_sample.sdk.utils.HttpUtils;
import sdk_sample.sdk.utils.SharedPreferencesUtil;
import sdk_sample.sdk.utils.ToolsUtil;
import sdk_sample.sdk.views.OpenLockDialog;

import static com.xinspace.csevent.app.AppConfig.COMMUNITY_AREA;

public class LockListActivity extends BaseActivity {
    public static final String NEIGBIOR_NAME_KEY = "neigbiorName";
    public static final String PATH = "path_url";
    public static final String TOKEN = "token";
    public static final String USER_NAME = "username";
    public static final String NEIGBOR_ID = "neigbor_id";
    public static final String DEAL_TIME = "deal_time";
    private ListView ls_lock_list;
    private LockListAdapter adapter;
    private List<LockBean> list;
    private LinearLayout ll_list_back;
    private TextView tv_top_left, tv_top_left_arrow;
    private String path;
    private String neigbor_id;
    private DatabaseHelper databaseHelper = null;
    private boolean isBusy = false;
    private boolean isClick = false;
    private Button btn_cancel;

    private String sip_number;
    private String sip_password;
    private String sip_domin;
    private int sip_port;
    private boolean accountCreated = false;
    private LinphoneAddress address;
    private LinphonePreferences mPrefs;

    private OrientationEventListener mOrientationHelper;
    private LinphoneCoreListenerBase mListener;

    private static final int CALL_ACTIVITY = 19;

    private static LockListActivity instance;

    private String flag ;

    private SipEvent sipEvent;

    private String domain_sn;
    private ArrayList<String> sipNums;
    private String area;

    public static final boolean isInstanciated() {
        return instance != null;
    }

    public static final LockListActivity instance() {
        if (instance != null){
            return instance;
        }
        throw new RuntimeException("LockListActivity not instantiated yet");
        //return instance;
    }


    private ExpandableListView ex_lock_list;
    private LockListAdapter2 adapter2;
    private List<ComBean> comBeanList = new ArrayList<>();
    private ImageView img_setting;

    private List<LockBean> allLockList3 = new ArrayList<>();
    private RecyclerView rv_lock_list;
    private LockListAdapter3 adapter3;
    private GridLayoutManager gManager;

    private SDPreference preference;
    private String cUid;
    private String cToken;
    private String phone;
    //StickyGridHeadersGridView

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 200:
                    if (msg.obj != null){
//                        oneSize();
//                        expandAllGroup();
                    }
                    break;
                case 300 :
                    if (msg.obj != null){
                        allLockList3.addAll((Collection<? extends LockBean>) msg.obj);
                        adapter3.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(this , R.color.app_bottom_color);
        this.requestWindowFeature(1);
        EventBus.getDefault().register(this);

        flag = getIntent().getStringExtra("flag");
        if (flag!=null){

        }
        instance = this;
        preference = SDPreference.getInstance();
        area = SharedPreferencesUtil1.getString(this, COMMUNITY_AREA, "");
        cUid = preference.getContent("cUid");
        cToken = preference.getContent("cToken");
        phone = preference.getContent("mobile");

        mPrefs = LinphonePreferences.instance();

        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        setContentView(ToolsUtil.getIdByName(this.getApplication(), "layout", "sayee_activity_lock_list"));

        try {
            initViews();            //1.初始化页面
        } catch (Exception var3) {
            var3.printStackTrace();
            return;
        }

        getDataFromUpPage();//2.获取上页传递过来的参数

        if (TextUtils.isEmpty(this.token) || TextUtils.isEmpty(this.userName)) {
            finish();
        }

        initLinPhoneConfig();//3.初始化linPhone的配置

        isGetToken = true;
        getNewToken();
        if (savedInstanceState != null) {
            path = savedInstanceState.getString("path_url");
            token = savedInstanceState.getString("token");
            userName = savedInstanceState.getString("username");
            neigbor_id = savedInstanceState.getString("neigbor_id");
            deal_time = savedInstanceState.getLong("deal_time");
        }

        setTitle();//根据从不同业务入口使用标题

        list = new ArrayList();
        adapter = new LockListAdapter(this, list);
        ls_lock_list.setAdapter(this.adapter);
        loadData();

        sipEvent = new SipEvent();
        sipEvent.setToken(LockListActivity.this.token);
        sipEvent.setUserName(LockListActivity.this.userName);
        sipEvent.setPath(LockListActivity.this.path);

        //列表的点击事件
        adapter3.setOnItemClickLitener(new LockListAdapter3.OnItemClickLitener() {
            @Override
            public void OnItemClick(View view, int positon, int type) {

                //1.大门列表
                if (flag.equals("1")) {
                    getDoorList(positon);
                }

                //2.设备列表
                else if (flag.equals("2")){
                    getMachineList(positon);
                }

                //3.密码开门
                else if (flag.equals("3")) {
                    getPasswordList(positon);
                }
            }

                @Override
                public void OnItemLongClick(View view, int position) {
                    //Nothing to do
                }
            });

        ll_list_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 根据用户获取开门密码。
     */
    private void getPasswordList(int position) {
        final String sipNum = allLockList3.get(position).getSip_number();
        HttpUtils.getOpenLockPassword(path, token, userName, sipNum, new HttpRespListener() {
            public void onSuccess(int code, BaseResult result) {
                OpenLockPasswordBean bean = ((OpenLockPasswordResult) result).getResult();
                Log.i("www", "没有跳转么");
                Intent intent3 = new Intent(LockListActivity.this, PassWordAct.class);
                intent3.putExtra("sayee_random_password", bean.getRandom_pw());
                intent3.putExtra("path", path);
                intent3.putExtra("token", token);
                intent3.putExtra("userName", userName);
                intent3.putExtra("sip_number", sipNum);
                intent3.putExtra("dead_time", bean.getRandomkey_dead_time());
                startActivity(intent3);
            }

            public void onFail(int code, String msg) {
                ToolsUtil.toast(LockListActivity.this, msg);
                if (code != 3) {
                    ToolsUtil.toast(LockListActivity.this, msg);
                } else {
                    Intent intent = new Intent();
                    intent.setAction("com.sayee.sdk.action.token.fail");
                    intent.putExtra("sayee_callback_code", 2);
                    intent.putExtra("sayee_error_msg", "token重新获取失败");
                    LockListActivity.this.sendBroadcast(intent);
                }
            }
        });
    }

    /**
     * 获取设备列表
     * @param positon
     */
    private void getMachineList(int positon) {
        final LockBean lockListBean = allLockList3.get(positon);
        String sipNum = lockListBean.getSip_number();
        sipEvent.setToSipNumber(lockListBean.getSip_number());
        sipEvent.setDomain_sn(lockListBean.getDomain_sn());
        LinphoneManager.getInstance().newOutgoingCall(sipNum, sipNum);
    }

    /**
     * 获取大门列表
     * @param positon
     */
    private void getDoorList(int positon) {
        LogUtil.i("-------www--------" + positon );
        getNewToken();
        final LockBean lockListBean = allLockList3.get(positon);
        final String domain_sn = lockListBean.getDomain_sn();
        final String sip_number = lockListBean.getSip_number();
        SharedPreferencesUtil.saveData(LockListActivity.this, "sayee_to_sip_number_key", sip_number);
        openDoor(domain_sn, sip_number);
    }

    private void setTitle() {
        if (flag!= null && flag.equals("1")){    // 开门
            tv_top_left.setText("大门列表");
        }else if (flag!= null && flag.equals("2")){  //监控
            tv_top_left.setText("设备列表");
        }else if(flag.equals("3")){
            tv_top_left.setText("密码开门");
        }
    }

    private void initLinPhoneConfig() {
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
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

        //LinphoneManager.getLc().setDeviceRotation(rotation);
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
            public void callState(LinphoneCore lc, LinphoneCall call, State state, String message) {
                if (state == State.IncomingReceived) {
                    LogUtil.i("LockListActivity这里是电话打进来界面");
                    startActivity(new Intent(LockListActivity.this, org.linphone.IncomingCallActivity.class));
                } else if (state == State.OutgoingInit) {

                    if (call.getCurrentParamsCopy().getVideoEnabled()) {
                        startVideoActivity(call);
                    } else {
                        LogUtil.i( "LockListActivity这里是电话打进来界面" + 11111);
                        startIncallActivity(call);
                    }
                } else if (state == State.CallEnd || state == State.Error || state == State.CallReleased) {

                }
        }
    };

        LinphoneCore lc = LinphoneManager.getLcIfManagerNotDestroyedOrNull();
        if (lc != null) {
            org.linphone.mediastream.Log.i("www", "走到这里添加监听了没");
            lc.addListener(mListener);
        }
    }

    private void initViews() {
        ll_list_back = (LinearLayout) findViewById(R.id.ll_list_back);
        tv_top_left = (TextView) this.findViewById(ToolsUtil.getIdByName(this.getApplication(), "id", "tv_top_left"));
        ls_lock_list = (ListView) this.findViewById(R.id.ls_lock_list);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);

        ex_lock_list = (ExpandableListView) findViewById(R.id.ex_lock_list);
        comBeanList = new ArrayList<>();

        ex_lock_list.setGroupIndicator(null);
        adapter2 = new LockListAdapter2(LockListActivity.this);

        rv_lock_list = (RecyclerView) findViewById(R.id.rv_lock_list);
        adapter3 = new LockListAdapter3(allLockList3 , LockListActivity.this);
        gManager = new GridLayoutManager(LockListActivity.this , 2);
        gManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch(adapter3.getItemViewType(position)){
                    case 2:
                        return 1;
                    case 1:
                        return 2;
                    default:
                        return 4;
                }
            }
        });
        rv_lock_list.setLayoutManager(gManager);
        rv_lock_list.setAdapter(adapter3);

        img_setting = (ImageView) findViewById(R.id.img_setting);
        img_setting.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LockListActivity.this, ComAppAct.class);
                startActivity(intent);
            }
        });
    }

    private void getDataFromUpPage() {
        if (this.getIntent() != null) {
            Intent intent = getIntent();
            path = intent.getStringExtra("path_url");
            token = intent.getStringExtra("token");
            userName = intent.getStringExtra("username");
            neigbor_id = intent.getStringExtra("neigbor_id");
            deal_time = intent.getLongExtra("deal_time", 0L);

            sip_number = intent.getStringExtra("sip_number");
            sip_password = intent.getStringExtra("sip_password");
            sip_domin = intent.getStringExtra("sip_domin");
            sip_port = intent.getIntExtra("sip_port", 0);
            sipNums = intent.getStringArrayListExtra("sipNums");

            flag = intent.getStringExtra("flag");

            LogUtil.i("sip_number" + sip_number + "sip_password" + sip_password + "sip_domin" + sip_domin);

        } else {
            finish();
        }
    }

    private void getOpenPassword(String sip_number, final OpenLockDialog dialog) {
        HttpUtils.getOpenLockPassword(LockListActivity.this.path, LockListActivity.this.token, LockListActivity.this.userName, sip_number, new HttpRespListener() {
            public void onSuccess(int code, BaseResult result) {
                OpenLockPasswordBean bean = ((OpenLockPasswordResult) result).getResult();
                Log.i("www", "没有跳转么");
                Intent intent = new Intent();
                intent.setAction("com.sayee.sdk.action.random.code");
                intent.putExtra("sayee_random_password", bean.getRandom_pw());
                intent.putExtra("sayee_random_password_deadline", bean.getRandomkey_dead_time());
                LockListActivity.this.sendBroadcast(intent);
                dialog.dismiss();
            }

            public void onFail(int code, String msg) {
                ToolsUtil.toast(LockListActivity.this, msg);
                if (code != 3) {
                    ToolsUtil.toast(LockListActivity.this, msg);
                } else {
                    Intent intent = new Intent();
                    intent.setAction("com.sayee.sdk.action.token.fail");
                    intent.putExtra("sayee_callback_code", 2);
                    intent.putExtra("sayee_error_msg", "token重新获取失败");
                    LockListActivity.this.sendBroadcast(intent);
                }
            }
        });
    }

    private void openDoor(final String domain_sn, String sip_number) {
        HttpUtils.openDoorLock(this, path, token, userName, domain_sn, 0, sip_number, (String)null, new HttpRespListener() {
            public void onSuccess(int code, BaseResult result) {
                LockRecordBean bean = new LockRecordBean();
                bean.setUid(cUid);
                bean.setToken(cToken);
                bean.setPhone(phone);
                bean.setEquip_sn(domain_sn);
                bean.setType("1");
                addEntranceRecord(bean);
                ToolsUtil.toast(LockListActivity.this , "已发送开锁请求");
            }

            public void onFail(int code, String msg) {
                if(code == 3) {
                    Intent intent = new Intent();
                    intent.setAction("com.sayee.sdk.action.token.fail");
                    intent.putExtra("sayee_callback_code", 1);
                    intent.putExtra("sayee_error_msg", "token重新获取失败");
                    LockListActivity.this.sendBroadcast(intent);
                } else {
                    ToolsUtil.toast(LockListActivity.this, msg);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe
    public void onEventMainThread(SipEvent event) {
        LogUtil.i("-----------www---------------- msg");
    }


    private void addEntranceRecord(LockRecordBean bean){

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


    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        path = savedInstanceState.getString("path_url");
        token = savedInstanceState.getString("token");
        userName = savedInstanceState.getString("username");
        neigbor_id = savedInstanceState.getString("neigbor_id");
        deal_time = savedInstanceState.getLong("deal_time");
    }

    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("path_url", this.path);
        savedInstanceState.putString("token", this.token);
        savedInstanceState.putString("username", this.userName);
        savedInstanceState.putString("neigbor_id", this.neigbor_id);
        savedInstanceState.putLong("deal_time", this.deal_time);
    }

    private void loadData() {
        final String url = this.path + this.userName + this.neigbor_id;
        if (!TextUtils.isEmpty(url)) {
            List lockList = this.getDatabaseHelper().selectLockList(url);
            if (lockList != null && lockList.size() > 0 && this.list != null) {
                this.list.clear();
                this.list.addAll(lockList);
                if (this.adapter != null) {
                    this.adapter.notifyDataSetChanged();
                }
            }
        }

        HttpUtils.getLockList(path, token, userName, neigbor_id, new HttpRespListener() {
            public void onSuccess(int code, BaseResult result) {
                LockListResult listResult = (LockListResult) result;
                if (listResult != null) {
                    List lockList = listResult.getLockList();
                    List<LockBean> lockList3 = listResult.getLockList3();
                    handler.obtainMessage(300 , lockList3).sendToTarget();
                    List<ComBean> comList = listResult.getComList();
                    comBeanList.addAll(comList);
                    adapter2.setList(comBeanList);
                    ex_lock_list.setAdapter(adapter2);
                    //LogUtil.i("------comBeanList----------" + comBeanList.size());
                    handler.obtainMessage(200 , comBeanList).sendToTarget();

                    if (lockList != null) {
                        if (LockListActivity.this.list != null) {
                            LockListActivity.this.list.clear();
                            LockListActivity.this.list.addAll(lockList);

                            if (LockListActivity.this.adapter != null) {
                                LockListActivity.this.adapter.notifyDataSetChanged();
                                adapter.notifyDataSetChanged();
                            }
                        }

                        if (!TextUtils.isEmpty(url)) {
                            getDatabaseHelper().saveLockList(lockList, url);
                        }
                    }
                }
            }

            public void onFail(int code, String msg) {
                if (LockListActivity.this.list != null && LockListActivity.this.list.size() == 0) {
                    ToolsUtil.toast(LockListActivity.this, msg);
                }
            }
        });
    }

    private DatabaseHelper getDatabaseHelper() {
        if (this.databaseHelper == null) {
            this.databaseHelper = new DatabaseHelper(this.getApplicationContext());
        }
        return this.databaseHelper;
    }

    /**
     * 展开所有组
     */
    private void expandAllGroup() {

        //LogUtil.i("--------------展开所有组-------------");

        for (int i = 0; i < comBeanList.size(); i++) {
            ex_lock_list.expandGroup(i);
        }
    }

    protected void onResume() {
        super.onResume();
        //LinphoneManager.addListener(this);
    }

    protected void onPause() {
        super.onPause();
        //LinphoneManager.removeListener(this);
        this.isGetToken = false;
    }

    public void onCallStateChanged(LinphoneCall paramLinphoneCall, State state, String paramString) {
        if (state == State.Error) {
            if (ToolsUtil.isRegistration() && this.isClick && !this.isBusy) {
                ToolsUtil.toast(this, "门口机正在通话中，请稍后再试！");
            }
            this.isBusy = true;
            this.isClick = false;
        }
    }

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


    //创建登录一个sip账号
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

        Log.i("www", "identity" + identity + "   isMainAccountLinphoneDotOrg   " + isMainAccountLinphoneDotOrg + "  useLinphoneDotOrgCustomPorts  " + useLinphoneDotOrgCustomPorts);

        LinphonePreferences.AccountBuilder builder = new LinphonePreferences.AccountBuilder(LinphoneManager.getLc())
                .setUsername(username)
                .setDomain(domain)
                .setPassword(password);

        if (isMainAccountLinphoneDotOrg && useLinphoneDotOrgCustomPorts) {
            if (getResources().getBoolean(R.bool.disable_all_security_features_for_markets)) {
                // builder.setProxy(domain + ":5228").setTransport(TransportType.LinphoneTransportTcp);
                builder.setProxy(domain + ":35162").setTransport(LinphoneAddress.TransportType.LinphoneTransportTcp);
            } else {
                // 登录的账号
                //builder.setProxy(domain + ":5223").setTransport(TransportType.LinphoneTransportTcp);
                builder.setProxy(domain + ":35162").setTransport(LinphoneAddress.TransportType.LinphoneTransportTcp);
            }

//            builder.setExpires("604800")
//                    .setOutboundProxyEnabled(true)
//                    .setAvpfEnabled(true)
//                    .setAvpfRRInterval(3)
//                    .setQualityReportingCollector("sip:voip-metrics@sip.linphone.org")
//                    .setQualityReportingEnabled(true)
//                    .setQualityReportingInterval(180)
//                    .setRealm(domain);
//
//            mPrefs.setStunServer(getString(R.string.default_stun));
//            mPrefs.setIceEnabled(true);
        } else {
            String forcedProxy = getResources().getString(R.string.setup_forced_proxy);
            if (!TextUtils.isEmpty(forcedProxy)) {
                builder.setProxy(forcedProxy)
                        .setOutboundProxyEnabled(true)
                        .setAvpfRRInterval(5);
            }
        }

//        if (getResources().getBoolean(R.bool.enable_push_id)) {
//            String regId = mPrefs.getPushNotificationRegistrationID();
//            String appId = getString(R.string.push_sender_id);
//            if (regId != null && mPrefs.isPushNotificationEnabled()) {
//                String contactInfos = "app-id=" + appId + ";pn-type=google;pn-tok=" + regId;
//                builder.setContactParameters(contactInfos);
//            }
//        }

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
        Log.i("www", "我要登录成功了");
        mPrefs.firstLaunchSuccessful();
        //setResult(Activity.RESULT_OK);
//        Intent intent = new Intent(FristActivity.this , SendActivity.class);
//        startActivity(intent);
//        finish();
    }

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


    public void startVideoActivity(LinphoneCall currentCall) {
        Log.i("www", "走到这里了11111111111111");
        Intent intent = new Intent(this, InCallActivity.class);
        intent.putExtra("VideoEnabled", true);
        intent.putExtra("sipEvent", sipEvent);
        startOrientationSensor();
        startActivityForResult(intent, CALL_ACTIVITY);
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


    private int getStatusIconResource(LinphoneCore.RegistrationState state, boolean isDefaultAccount) {
        try {
            LinphoneCore lc = LinphoneManager.getLcIfManagerNotDestroyedOrNull();
            boolean defaultAccountConnected = (isDefaultAccount && lc != null && lc.getDefaultProxyConfig() != null && lc.getDefaultProxyConfig().isRegistered()) || !isDefaultAccount;
            if (state == LinphoneCore.RegistrationState.RegistrationOk && defaultAccountConnected) {
                return R.drawable.led_connected;
            } else if (state == LinphoneCore.RegistrationState.RegistrationProgress) {
                return R.drawable.led_inprogress;
            } else if (state == LinphoneCore.RegistrationState.RegistrationFailed) {
                return R.drawable.led_error;
            } else {
                return R.drawable.led_disconnected;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.drawable.led_disconnected;
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
}

