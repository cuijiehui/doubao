package sdk_sample.sdk.activity;

/**
 * Created by Android on 2017/3/23.
 */

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.util.LogUtil;

import org.linphone.InCallActivity;
import org.linphone.LinphoneManager;
import org.linphone.LinphonePreferences;
import org.linphone.SendActivity;
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
import org.linphone.ui.AddressText;
import org.linphone.ui.CallButton;

import java.util.ArrayList;
import java.util.List;

import sdk_sample.sdk.HttpRespListener;
import sdk_sample.sdk.adapter.LockListAdapter;
import sdk_sample.sdk.bean.LockBean;
import sdk_sample.sdk.db.DatabaseHelper;
import sdk_sample.sdk.result.BaseResult;
import sdk_sample.sdk.result.LockListResult;
import sdk_sample.sdk.utils.HttpUtils;
import sdk_sample.sdk.utils.ToolsUtil;
import sdk_sample.sdk.views.OpenLockDialog;

public class LockListActivity2 extends BaseActivity  {
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
    private TextView tv_top_left , tv_top_left_arrow;
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
    private static SendActivity sendActivity;
    private static boolean isCallTransferOngoing = false;
    public boolean mVisible;
    private AddressText mAddress;
    private CallButton mCall;
    private ImageView mAddContact;
    private OnClickListener addContactListener, cancelListener, transferListener;
    private boolean shouldEmptyAddressField = true;
    private boolean isInCall, isAttached = false;

    private TextView statusText;
    private ImageView statusLed;
    private static final int CALL_ACTIVITY = 19;

    private static LockListActivity2 instance;

    public static final boolean isInstanciated() {
        return instance != null;
    }

    public static final LockListActivity2 instance() {
        if (instance != null)
            return instance;
        throw new RuntimeException("LinphoneActivity not instantiated yet");
    }

    public LockListActivity2() {

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(1);

        instance = this;
        mPrefs = LinphonePreferences.instance();

        if(getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        try {
            setContentView(ToolsUtil.getIdByName(this.getApplication(), "layout", "sayee_activity_lock_list"));
//            this.ll_top_left = (LinearLayout)this.findViewById(ToolsUtil.getIdByName(this.getApplication(), "id", "ll_top_left"));

            ll_list_back = (LinearLayout) findViewById(R.id.ll_list_back);
            tv_top_left = (TextView)this.findViewById(ToolsUtil.getIdByName(this.getApplication(), "id", "tv_top_left"));
            ls_lock_list = (ListView)this.findViewById(R.id.ls_lock_list);
            btn_cancel = (Button) findViewById(R.id.btn_cancel);
        } catch (Exception var3) {
            var3.printStackTrace();
            return;
        }

        if(this.getIntent() != null) {
            Intent intent = getIntent();
            path = intent.getStringExtra("path_url");
            token = intent.getStringExtra("token");
            userName = intent.getStringExtra("username");
            neigbor_id = intent.getStringExtra("neigbor_id");
            deal_time = intent.getLongExtra("deal_time", 0L);

            sip_number = intent.getStringExtra("sip_number");
            sip_password = intent.getStringExtra("sip_password");
            sip_domin = intent.getStringExtra("sip_domin");
            sip_port = intent.getIntExtra("sip_port" , 0);

            LogUtil.i("sip_number" + sip_number + "sip_password" + sip_password + "sip_domin" + sip_domin);
            if (sip_number != null && sip_password != null && sip_domin!= null){
                logIn(sip_number ,sip_password , sip_domin , false);
            }
        } else {
            finish();
        }

        if(TextUtils.isEmpty(this.token) || TextUtils.isEmpty(this.userName)) {
            finish();
        }

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

        LinphoneManager.getLc().setDeviceRotation(rotation);
        mAlwaysChangingPhoneAngle = rotation;

        //监听回调
        mListener = new LinphoneCoreListenerBase(){
            @Override
            public void messageReceived(LinphoneCore lc, LinphoneChatRoom cr, LinphoneChatMessage message) {
//                displayMissedChats(getChatStorage().getUnreadMessageCount());
//                if (messageListFragment != null && messageListFragment.isVisible()) {
//                    ((ChatListFragment) messageListFragment).refresh();
//                }
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
                    org.linphone.mediastream.Log.i("www" , "这里是电话打进来界面");
                    startActivity(new Intent(LockListActivity2.this, org.linphone.IncomingCallActivity.class));
                } else if (state == State.OutgoingInit) {
                    if (call.getCurrentParamsCopy().getVideoEnabled()) {
                        startVideoActivity(call);
                    } else {
                        org.linphone.mediastream.Log.i("www" , "是不是这里" + 11111);
                        startIncallActivity(call);
                    }
                } else if (state == State.CallEnd || state == State.Error || state == State.CallReleased){
                    // Convert LinphoneCore message for internalization
//                    if (message != null && message.equals("Call declined.")) {
//                        displayCustomToast(getString(R.string.error_call_declined), Toast.LENGTH_LONG);
//                    } else if (message != null && message.equals("Not Found")) {
//                        displayCustomToast(getString(R.string.error_user_not_found), Toast.LENGTH_LONG);
//                    } else if (message != null && message.equals("Unsupported media type")) {
//                        displayCustomToast(getString(R.string.error_incompatible_media), Toast.LENGTH_LONG);
//                    } else if (message != null && state == LinphoneCall.State.Error) {
//                        displayCustomToast(getString(R.string.error_unknown) + " - " + message, Toast.LENGTH_LONG);
//                    }
//                    resetClassicMenuLayoutAndGoBackToCallIfStillRunning();
                }

//                int missedCalls = LinphoneManager.getLc().getMissedCallsCount();
//                displayMissedCalls(missedCalls);
            }
        };

        LinphoneCore lc = LinphoneManager.getLcIfManagerNotDestroyedOrNull();
        if (lc != null) {
            org.linphone.mediastream.Log.i("www" , "走到这里添加监听了没");
            lc.addListener(mListener);
        }

        isGetToken = true;
        getNewToken();
        if(savedInstanceState != null) {
            path = savedInstanceState.getString("path_url");
            token = savedInstanceState.getString("token");
            userName = savedInstanceState.getString("username");
            neigbor_id = savedInstanceState.getString("neigbor_id");
            deal_time = savedInstanceState.getLong("deal_time");
        }

        tv_top_left.setText("设备列表");
        list = new ArrayList();
        adapter = new LockListAdapter(this, list);
        ls_lock_list.setAdapter(this.adapter);
        loadData();

        ls_lock_list.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {
                LockListActivity2.this.getNewToken();
                final LockBean lockListBean = (LockBean)LockListActivity2.this.list.get(position);
                final OpenLockDialog dialog = new OpenLockDialog(LockListActivity2.this, lockListBean.getLock_name());
                final String domain_sn = lockListBean.getDomain_sn();
                final String sip_number = lockListBean.getSip_number();

                String sipNum = list.get(position).getSip_number();
                LogUtil.i("sipNum" + sipNum);
                LinphoneManager.getInstance().newOutgoingCall(sipNum , sipNum);

//                SharedPreferencesUtil1.saveData(LockListActivity2.this, "sayee_to_sip_number_key", sip_number);
//                dialog.setPath(LockListActivity2.this.path);
//                dialog.setToken(LockListActivity2.this.token);
//                dialog.setUserName(LockListActivity2.this.userName);
//                dialog.setDomain_sn(domain_sn);
//                dialog.setToSipNumber(sip_number);

//                dialog.setOpenDoorForpasswordListener(new OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        HttpUtils.getOpenLockPassword(LockListActivity2.this.path, LockListActivity2.this.token, LockListActivity2.this.userName, sip_number, new HttpRespListener() {
//                            public void onSuccess(int code, BaseResult result) {
//                                OpenLockPasswordBean bean = ((OpenLockPasswordResult)result).getResult();
//                                Log.i("www" , "没有跳转么");
//                                Intent intent = new Intent();
//                                intent.setAction("com.sayee.sdk.action.random.code");
//                                intent.putExtra("sayee_random_password", bean.getRandom_pw());
//                                intent.putExtra("sayee_random_password_deadline", bean.getRandomkey_dead_time());
//                                LockListActivity2.this.sendBroadcast(intent);
//                                dialog.dismiss();
//                            }
//
//                            public void onFail(int code, String msg) {
//                                ToolsUtil.toast(LockListActivity2.this, msg);
//                                if(code != 3) {
//                                    ToolsUtil.toast(LockListActivity2.this, msg);
//                                } else {
//                                    Intent intent = new Intent();
//                                    intent.setAction("com.sayee.sdk.action.token.fail");
//                                    intent.putExtra("sayee_callback_code", 2);
//                                    intent.putExtra("sayee_error_msg", "token重新获取失败");
//                                    LockListActivity2.this.sendBroadcast(intent);
//                                }
//                            }
//                        });
//                    }
//                });

//                dialog.setOpenVideoListener(new OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        String sipNum = list.get(position).getSip_number();
//                        LogUtil.i("sipNum" + sipNum);
//                        LinphoneManager.getInstance().newOutgoingCall(sipNum , sipNum);
//                        dialog.dismiss();
//                    }
//                });
//                dialog.show();
            }
        });

        ll_list_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LockListActivity2.this.finish();
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
        if(!TextUtils.isEmpty(url)) {
            List lockList = this.getDatabaseHelper().selectLockList(url);
            if(lockList != null && lockList.size() > 0 && this.list != null) {
                this.list.clear();
                this.list.addAll(lockList);
                if(this.adapter != null) {
                    this.adapter.notifyDataSetChanged();
                }
            }
        }

        HttpUtils.getLockList(this.path, this.token, this.userName, this.neigbor_id, new HttpRespListener() {
            public void onSuccess(int code, BaseResult result) {
                LockListResult listResult = (LockListResult)result;
                if(listResult != null) {
                    List lockList = listResult.getLockList();
                    if(lockList != null) {
                        if(LockListActivity2.this.list != null) {
                            LockListActivity2.this.list.clear();
                            LockListActivity2.this.list.addAll(lockList);
                            if(LockListActivity2.this.adapter != null) {
                                LockListActivity2.this.adapter.notifyDataSetChanged();
                            }
                        }

                        if(!TextUtils.isEmpty(url)) {
                            LockListActivity2.this.getDatabaseHelper().saveLockList(lockList, url);
                        }
                    }
                }
            }

            public void onFail(int code, String msg) {
                if(LockListActivity2.this.list != null && LockListActivity2.this.list.size() == 0) {
                    ToolsUtil.toast(LockListActivity2.this, msg);
                }
            }
        });
    }

    private DatabaseHelper getDatabaseHelper() {
        if(this.databaseHelper == null) {
            this.databaseHelper = new DatabaseHelper(this.getApplicationContext());
        }
        return this.databaseHelper;
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
        if(state == State.Error) {
            if(ToolsUtil.isRegistration() && this.isClick && !this.isBusy) {
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
            Log.i("www" ,"来走这里了");
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

        Log.i("www" , "identity" + identity +  "   isMainAccountLinphoneDotOrg   " + isMainAccountLinphoneDotOrg + "  useLinphoneDotOrgCustomPorts  " + useLinphoneDotOrgCustomPorts);

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

        Log.i("www" , "needsEchoCalibration  " + needsEchoCalibration + " mPrefs.isFirstLaunch()  " + mPrefs.isFirstLaunch());

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
        Log.i("www" , "我要登录成功了");
        mPrefs.firstLaunchSuccessful();
        //setResult(Activity.RESULT_OK);
//        Intent intent = new Intent(FristActivity.this , SendActivity.class);
//        startActivity(intent);
//        finish();
    }

    public void startIncallActivity(LinphoneCall currentCall) {
        org.linphone.mediastream.Log.i("www" , "走到这里了2222222222222");
        Intent intent = new Intent(this, InCallActivity.class);
        intent.putExtra("VideoEnabled", false);
        startOrientationSensor();
        startActivityForResult(intent, CALL_ACTIVITY);
    }


    public void startVideoActivity(LinphoneCall currentCall) {
        org.linphone.mediastream.Log.i("www" , "走到这里了11111111111111");
        Intent intent = new Intent(this, InCallActivity.class);
        intent.putExtra("VideoEnabled", true);
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

