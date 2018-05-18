package sdk_sample.sdk;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;


import org.linphone.LinphoneManager;
import org.linphone.LinphonePreferences;
import org.linphone.LinphoneUtils;
import org.linphone.core.LinphoneCall;
import org.linphone.core.LinphoneCore;
import org.linphone.core.LinphoneCoreException;
import org.linphone.core.LinphoneProxyConfig;

import sdk_sample.sdk.activity.IncomingCallActivity;
import sdk_sample.sdk.utils.SharedPreferencesUtil;
import sdk_sample.sdk.utils.ToolsUtil;

/**
 * Created by Android on 2017/3/23.
 */

public class LinphoneService extends Service  {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

//    @Override
//    public void onCallEncryptionChanged(LinphoneCall var1, boolean var2, String var3) {
//
//    }
//
//    @Override
//    public void onCallStateChanged(LinphoneCall var1, LinphoneCall.State var2, String var3) {
//
//    }
//
//    @Override
//    public void onGlobalStateChanged(LinphoneCore.GlobalState var1, String var2) {
//
//    }
//
//    @Override
//    public void onDisplayStatus(String var1) {
//
//    }
//
//    @Override
//    public void onRegistrationStateChanged(LinphoneProxyConfig var1, LinphoneCore.RegistrationState var2, String var3) {
//
//    }
//
//    @Override
//    public void tryingNewOutgoingCallButAlreadyInCall() {
//
//    }
//
//    @Override
//    public void tryingNewOutgoingCallButCannotGetCallParameters() {
//
//    }
//
//    @Override
//    public void tryingNewOutgoingCallButWrongDestinationAddress() {
//
//    }
//    public static final int IC_LEVEL_OFFLINE = 3;
//    public static final int IC_LEVEL_ORANGE = 0;
//    private static LinphoneService instance;
//    private static PendingIntent mkeepAlivePendingIntent;
//
//    public LinphoneService() {
//    }
//
//    public static LinphoneService instance() {
//        return isReady()?instance:null;
//    }
//
//    public static boolean isReady() {
//        return instance != null;
//    }
//
//    public IBinder onBind(Intent paramIntent) {
//        return null;
//    }
//
//    public void onCallStateChanged(LinphoneCall call, LinphoneCall.State paramState, String paramString) {
//        if(instance == null) {
//            this.startService((new Intent("android.intent.action.MAIN")).setClass(this, LinphoneService.class));
//        } else if(paramState == LinphoneCall.State.IncomingReceived) {
//            if(!LinphoneUtils.isHasPermission(this.getApplicationContext()) && LinphoneManager.getLcIfManagerNotDestroyedOrNull() != null && !LinphoneManager.getLc().isIncall()) {
//                try {
//                    ToolsUtil.toast(this, "由于没有录音权限，已自动帮您挂断来电，请打开手机设置的" + LinphoneUtils.getAppName(this.getApplicationContext()) + "的录音权限");
//                } catch (Exception var8) {
//                    var8.printStackTrace();
//                }
//            }
//
//            if(SharedPreferencesUtil1.getSipIsCall(this.getApplicationContext())) {
//                Intent remoteVideo1 = new Intent(this, IncomingCallActivity.class);
//                remoteVideo1.setFlags(268435456);
//                this.startActivity(remoteVideo1);
//            } else if(LinphoneManager.getLcIfManagerNotDestroyedOrNull() != null) {
//                LinphoneManager.getLc().terminateCall(call);
//            }
//
//        } else {
//            if(paramState == LinphoneCall.State.CallUpdatedByRemote) {
//                boolean remoteVideo = call.getRemoteParams().getVideoEnabled();
//                boolean localVideo = call.getCurrentParamsCopy().getVideoEnabled();
//                boolean autoAcceptCameraPolicy = LinphonePreferences.instance().shouldAutomaticallyAcceptVideoRequests();
//                if(remoteVideo && !localVideo && !autoAcceptCameraPolicy && !LinphoneManager.getLc().isInConference()) {
//                    try {
//                        LinphoneManager.getLc().deferCallUpdate(call);
//                    } catch (LinphoneCoreException var9) {
//                        var9.printStackTrace();
//                    }
//                }
//            }
//
//        }
//    }
//
//    public void onCreate() {
//        super.onCreate();
//        instance = this;
//        LinphoneManager.createAndStart(this, this);
//        Intent localIntent2 = new Intent(this, KeepAliveHandler.class);
//        PendingIntent mkeepAlivePendingIntent = PendingIntent.getBroadcast(this, 0, localIntent2, 1073741824);
//        ((AlarmManager)this.getSystemService(Context.ALARM_SERVICE)).setRepeating(2, 1000000L + SystemClock.elapsedRealtime(), 1000000L, mkeepAlivePendingIntent);
//    }
//
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        return Service.START_STICKY;
//    }
//
//    public void onDestroy() {
//        instance = null;
//        LinphoneManager.destroy();
//        if(mkeepAlivePendingIntent != null) {
//            ((AlarmManager)this.getSystemService(Context.ALARM_SERVICE)).cancel(mkeepAlivePendingIntent);
//        }
//
//        super.onDestroy();
//    }
//
//    public void onDisplayStatus(String paramString) {
//    }
//
//    public void onGlobalStateChanged(LinphoneCore.GlobalState paramGlobalState, String paramString) {
//    }
//
//    public void onRegistrationStateChanged(LinphoneProxyConfig config, LinphoneCore.RegistrationState state, String paramString) {
//    }
//
//    public void tryingNewOutgoingCallButAlreadyInCall() {
//    }
//
//    public void tryingNewOutgoingCallButCannotGetCallParameters() {
//    }
//
//    public void tryingNewOutgoingCallButWrongDestinationAddress() {
//    }
//
//    public void onCallEncryptionChanged(LinphoneCall paramLinphoneCall, boolean paramBoolean, String paramString) {
//    }
}

