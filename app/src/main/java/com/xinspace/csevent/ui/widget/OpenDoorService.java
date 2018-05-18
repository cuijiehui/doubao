package com.xinspace.csevent.ui.widget;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.xinspace.csevent.monitor.activity.PassWordAct;
import com.xinspace.csevent.monitor.activity.SubmitDataAct;
import com.xinspace.csevent.monitor.bean.SipEvent;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.biz.GetDataBiz;
import com.xinspace.csevent.login.activity.LoginActivity;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.SharedPreferencesUtil1;
import com.xinspace.csevent.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.linphone.InCallActivity;
import org.linphone.LinphoneManager;
import org.linphone.core.LinphoneAuthInfo;
import org.linphone.core.LinphoneCall;
import org.linphone.core.LinphoneChatMessage;
import org.linphone.core.LinphoneChatRoom;
import org.linphone.core.LinphoneCore;
import org.linphone.core.LinphoneCoreListenerBase;
import org.linphone.core.LinphoneProxyConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import static com.xinspace.csevent.app.AppConfig.COMMUNITY_AREA;


/**
 * 快速打开门禁的Service
 * 1.检查用户是否已经通过审核
 * 2.检查用户是否已经拥有Token
 * 3.获取用户的门禁列表
 * 4.开锁
 * Created by Android on 2017/8/23.
 */

public class OpenDoorService extends Service{

    private String token;
    private SDPreference prefs;
    private String dealTime;
    private String mobile;
    private String appflag;
    private List<LockBean> lockList;
    private String domain_sn;
    private SipEvent sipEvent;
    private String cUid;
    private LinphoneCoreListenerBase mListener;
    private DatabaseHelper databaseHelper;
    private static final int CALL_ACTIVITY = 19;
    private String cToken;
    private String NEIBOR_ID;
    private String area;

    @Override
    public void onCreate() {
        super.onCreate();
        prefs = SDPreference.getInstance();
        area = SharedPreferencesUtil1.getString(this, COMMUNITY_AREA, "");
        if (area.equals("")){
            area = "st";
        }
        token = prefs.getContent("coresun_token");
        dealTime = prefs.getContent("coresun_dealtime");
        mobile = prefs.getContent("mobile");

        cUid = prefs.getContent("cUid");
        cToken = prefs.getContent("cToken");
        sipEvent = new SipEvent();
        initLinPhoneConfig();
        setDefaultCommunity();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        appflag = intent.getStringExtra("theflag");

        if (mobile.equals("0")){
            Intent login = new Intent(this, LoginActivity.class);
            login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(login);
            stopSelf();
        }
        if (token.equals("0") && dealTime.equals("0")){
            verifyPermission();
        }
        else{
            openDoor();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 设置默认的社区
     * create by MQZ at 2017/11/1
     */
    private void setDefaultCommunity() {
        if (area.equals("gz")){
            NEIBOR_ID = "100046";
        }else{
            NEIBOR_ID = "100032";  //汕头
        }
    }

    /**
     * 1.验证用户权限
     */
    private void verifyPermission() {
        LogUtil.e("UID:" + cUid);
        GetDataBiz.getqueryAuditStatus(area, cUid, cToken, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                LogUtil.e("verifyPermission/OpenDoorService CurrentThread：" + Thread.currentThread().getName());
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("code").equals("200")){
                    JSONObject dataJsonObject = jsonObject.getJSONObject("data");
                    String status = dataJsonObject.getString("status");
                    if (status.equals("0")){
                        ToastUtil.makeToast("请完善社区资料");
                        Intent intent = new Intent(OpenDoorService.this , SubmitDataAct.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    else if (status.equals("1")){
                        ToastUtil.makeToast("当前用户正在审核中");
                    }
                    else{
                        getToken();
                    }
                }else{
                    ToastUtil.makeToast("当前用户无使用权限");
                }
            }

            @Override
            public void onHttpRequestError(String error) {
                ToastUtil.makeToast("网络错误");
            }
        });
    }

    /**
     * 2.获取Token
     */
    private void getToken() {
        HttpUtils.getToken(this, "2", mobile, "2", null, new HttpRespListener() {
            @Override
            public void onSuccess(int code, BaseResult result) {
                TokenResult tokeResult = (TokenResult) result;
                token = tokeResult.getToken();
                dealTime = String.valueOf(tokeResult.getDead_time());

                LogUtil.e("getToken/OpenDoorService CurrentThread：" + Thread.currentThread().getName());
                prefs.putContent("coresun_token", token);
                prefs.putContent("coresun_dealtime", dealTime+"");
                openDoor();

            }

            @Override
            public void onFail(int var1, String var2) {

            }
        });
    }

    /**
     * 根据用户操作进行门禁开锁
     */
    private void openDoor() {
        final Map<String, String> headpParams = new HashMap<String, String>();
        headpParams.put("token", token);
        headpParams.put("username", mobile);
        headpParams.put("dealtime", dealTime + "");
        final Map<String, String> params = new HashMap<String, String>();
        params.put("username", mobile);
        params.put("neibor_flag", NEIBOR_ID);

        HttpUtils.goToOpenDoor(OpenDoorService.this, headpParams, params, appflag, new HttpRespListener() {
            @Override
            public void onSuccess(int code, BaseResult result) {
                //请求成功回调
                Log.i("www", "code   " + code + "result   " + result.toString());

                try {

                    JSONObject e = new JSONObject(result.getMsg());
                    JSONObject jsonObject = e.getJSONObject("result");

                    JSONObject jsonObject1 = jsonObject.getJSONObject("user_msg");
                    final String neiborId = jsonObject.getString("neibor_id");
                    final String sip_number = jsonObject1.getString("user_sip");
                    final String sip_password = jsonObject1.getString("user_password");
                    final String sip_domin = jsonObject1.getString("fs_ip");
                    final int sip_port = jsonObject1.getInt("fs_port");

                    final String path = "https://" + jsonObject.getString("fip") + ":" + jsonObject.getInt("fport");

                    HttpUtils.getLockList(path, token, mobile, neiborId, new HttpRespListener() {
                        @Override
                        public void onSuccess(int var1, BaseResult baseResult) {
                            LogUtil.i("当前线程：" + Thread.currentThread().getName());
                            LockListResult result = (LockListResult) baseResult;
                            lockList = result.getLockList();
                            openLock(neiborId, sip_number, sip_password, sip_domin, sip_port, path);
                        }

                        @Override
                        public void onFail(int var1, String var2) {
                            ToastUtil.makeToast(var2);
                        }
                    });



                } catch (Exception var15) {
                    var15.printStackTrace();
                }
            }

            @Override
            public void onFail(int code, String msg) {

                //请求失败回调
                Log.i("www", "失败code   " + code + "msg   " + msg.toString());

                ToolsUtil.toast(OpenDoorService.this, "身份验证失败");

                stopSelf();

            }
        });
    }

    /**
     * 4.开锁
     * @param neiborId
     * @param sip_number
     * @param sip_password
     * @param sip_domin
     * @param sip_port
     * @param path
     */
    private void openLock(String neiborId, final String sip_number, String sip_password, String sip_domin, int sip_port, final String path) {
        if (lockList.size() == 1){
            final LockBean lockbean = lockList.get(0);
                if (appflag.equals("1")){
                    String sipNumber = lockbean.getSip_number();
                    domain_sn = lockbean.getDomain_sn();
                    SharedPreferencesUtil.saveData(OpenDoorService.this,  "sayee_to_sip_number_key", sip_number);
                    openDoorByRequest(sipNumber, path, domain_sn, mobile);   //请求开锁
                    stopSelf();
                }
                else if (appflag.equals("2")){
                    LockBean lockListBean = lockList.get(0);
                    String sipNum = lockListBean.getSip_number();
                    sipEvent.setToken(token);
                    sipEvent.setUserName(mobile);
                    sipEvent.setPath(path);
                    sipEvent.setToSipNumber(lockListBean.getSip_number());
                    sipEvent.setDomain_sn(lockListBean.getDomain_sn());
                    LinphoneManager.getInstance().newOutgoingCall(sipNum, sipNum);
                    stopSelf();
                }
                else if (appflag.equals("3")){
                    HttpUtils.getOpenLockPassword(path, token, mobile, sip_number, new HttpRespListener() {
                        public void onSuccess(int code, BaseResult result) {
                            OpenLockPasswordBean bean = ((OpenLockPasswordResult) result).getResult();
                            Log.i("www", "没有跳转么");
                            Intent intent3 = new Intent(OpenDoorService.this, PassWordAct.class);
                            intent3.putExtra("sayee_random_password", bean.getRandom_pw());
                            intent3.putExtra("path", path);
                            intent3.putExtra("token", token);
                            intent3.putExtra("userName", mobile);
                            intent3.putExtra("sip_number", lockbean.getSip_number());
                            intent3.putExtra("dead_time", bean.getRandomkey_dead_time());
                            intent3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent3);
                            stopSelf();
                        }

                        public void onFail(int code, String msg) {
                            ToolsUtil.toast(OpenDoorService.this, msg);
                            if (code != 3) {
                                ToolsUtil.toast(OpenDoorService.this, msg);
                            } else {
                                Intent intent = new Intent();
                                intent.setAction("com.sayee.sdk.action.token.fail");
                                intent.putExtra("sayee_callback_code", 2);
                                intent.putExtra("sayee_error_msg", "token重新获取失败");
                                sendBroadcast(intent);
                            }
                        }
                    });
            }
        }
        else{
            Intent intent = new Intent(OpenDoorService.this, LockListActivity.class);
            intent.putExtra("path_url", path);
            intent.putExtra("token", token);
            intent.putExtra("username", mobile);
            intent.putExtra("neigbor_id", neiborId);
            intent.putExtra("deal_time", dealTime);

            intent.putExtra("sip_number" , sip_number);
            intent.putExtra("sip_password" , sip_password);
            intent.putExtra("sip_domin" , sip_domin);
            intent.putExtra("sip_port" , sip_port);
            intent.putExtra("flag" , appflag);

            intent.addFlags(268435456);
            startActivity(intent);
            stopSelf();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.e("OpenDoorService destory");
    }

    private void openDoorByRequest(String sip_number, String path, final String doMain_sn, String userName) {
        HttpUtils.openDoorLock(OpenDoorService.this, path, token, userName,
                doMain_sn, 0, sip_number, null, new HttpRespListener() {
                    @Override
                    public void onSuccess(int code, BaseResult var2) {
                        LockRecordBean bean = new LockRecordBean();
                        bean.setUid(cUid);
                        bean.setToken(token);
                        bean.setPhone(mobile);
                        bean.setEquip_sn(doMain_sn);
                        bean.setType("1");
                        addEntranceRecord(bean);
                        ToolsUtil.toast(OpenDoorService.this , "已发送开锁请求");
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if(code == 3) {
                            Intent intent = new Intent();
                            intent.setAction("com.sayee.sdk.action.token.fail");
                            intent.putExtra("sayee_callback_code", 1);
                            intent.putExtra("sayee_error_msg", "token重新获取失败");
                            sendBroadcast(intent);
                        } else {
                            ToolsUtil.toast(OpenDoorService.this, msg);
                        }
                    }
                });
    }

    /**
     * 上传开门记录
     * @param bean
     */
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

    private void initLinPhoneConfig() {

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
                    startActivity(new Intent(OpenDoorService.this, org.linphone.IncomingCallActivity.class));
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

        LinphoneCore lc = LinphoneManager.getLcIfManagerNotDestroyedOrNull();
        if (lc != null) {
            org.linphone.mediastream.Log.i("www", "走到这里添加监听了没");
            lc.addListener(mListener);
        }
    }

    public void startVideoActivity(LinphoneCall currentCall) {
        Log.i("www", "走到这里了11111111111111");
        Intent intent = new Intent(this, InCallActivity.class);
        intent.putExtra("VideoEnabled", true);
        intent.putExtra("sipEvent", sipEvent);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void startIncallActivity(LinphoneCall currentCall) {

        if (TextUtils.isEmpty(domain_sn)) {
            domain_sn = getDatabaseHelper().getDomainSn(currentCall.getRemoteAddress().getUserName());
        }
        Log.i("www", "走到这里了2222222222222 对方打进来的" + currentCall.getRemoteAddress().getUserName() + "domain_sn" + domain_sn);
        Intent intent = new Intent(this, InCallActivity.class);
        sipEvent.setDomain_sn(domain_sn);
        sipEvent.setToSipNumber(currentCall.getRemoteAddress().getUserName());
        intent.putExtra("VideoEnabled", true);
        intent.putExtra("sipEvent", sipEvent);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private DatabaseHelper getDatabaseHelper() {
        if (this.databaseHelper == null) {
            this.databaseHelper = new DatabaseHelper(this.getApplicationContext());
        }
        return this.databaseHelper;
    }



}
