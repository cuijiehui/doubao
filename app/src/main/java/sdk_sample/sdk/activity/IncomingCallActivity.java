package sdk_sample.sdk.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import org.linphone.LinphoneManager;
import org.linphone.LinphoneUtils;
import org.linphone.core.LinphoneAddress;
import org.linphone.core.LinphoneCall;
import org.linphone.core.LinphoneCallParams;
import org.linphone.core.LinphoneCore;
import org.linphone.core.LinphoneCoreException;
import org.linphone.mediastream.Log;

import java.util.Iterator;
import java.util.List;

import sdk_sample.sdk.db.DatabaseHelper;
import sdk_sample.sdk.utils.ToolsUtil;
import sdk_sample.sdk.views.AlertDialog;

/**
 * Created by Android on 2017/3/23.
 */

public class IncomingCallActivity extends Activity  {
    static IncomingCallActivity instance;
    private Button btn_anwser;
    private Button btn_hangup;
    boolean isAnswer;
    private TextView tv_address;
    private String domain;
    private String fromSipName;
    private String displayName = "";
    private LinphoneCall mCall;
    private PowerManager.WakeLock mWakeLock;
    private PowerManager powerManager;
    private DatabaseHelper databaseHelper = null;
    long firstTime = 0L;

    public IncomingCallActivity() {
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        this.requestWindowFeature(1);
        this.getWindow().setFlags(1024, 1024);
        this.getWindow().addFlags(2621440);
        if(this.getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        instance = this;

        try {
            this.setContentView(ToolsUtil.getIdByName(this.getApplication(), "layout", "sayee_activtiy_incoming_call"));
            this.tv_address = (TextView)this.findViewById(ToolsUtil.getIdByName(this.getApplication(), "id", "tv_address"));
            this.btn_anwser = (Button)this.findViewById(ToolsUtil.getIdByName(this.getApplication(), "id", "btn_answer"));
            this.btn_hangup = (Button)this.findViewById(ToolsUtil.getIdByName(this.getApplication(), "id", "btn_hangup"));
        } catch (Exception var3) {
            var3.printStackTrace();
            return;
        }
        this.setListener();
    }

    private void setListener() {
        this.btn_anwser.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if(LinphoneManager.getLcIfManagerNotDestroyedOrNull() != null && LinphoneManager.getLc().isIncall()) {
                    LinphoneCall[] ad = LinphoneManager.getLc().getCalls();
                    if(ad != null) {
                        LinphoneCall[] var6 = ad;
                        int var5 = ad.length;

                        for(int var4 = 0; var4 < var5; ++var4) {
                            LinphoneCall call = var6[var4];
                            if(call != IncomingCallActivity.this.mCall && call != null) {
                                LinphoneManager.getLc().terminateCall(call);
                            }
                        }
                    }
                }

//                if(!LinphoneUtils.isHasPermission(IncomingCallActivity.this.getApplicationContext())) {
//                    final AlertDialog var7 = new AlertDialog(IncomingCallActivity.this);
//                    var7.setPositiveButton(new View.OnClickListener() {
//                        public void onClick(View v) {
//                            String name = Build.MANUFACTURER;
//                            if("HUAWEI".equals(name)) {
//                                LinphoneUtils.goHuaWeiMainager(IncomingCallActivity.this);
//                            } else if("vivo".equals(name)) {
//                                LinphoneUtils.goVivoMainager(IncomingCallActivity.this);
//                            } else if("OPPO".equals(name)) {
//                                LinphoneUtils.goOppoMainager(IncomingCallActivity.this);
//                            } else if("Coolpad".equals(name)) {
//                                LinphoneUtils.goCoolpadMainager(IncomingCallActivity.this);
//                            } else if("Meizu".equals(name)) {
//                                LinphoneUtils.goMeizuMainager(IncomingCallActivity.this);
//                            } else if("Xiaomi".equals(name)) {
//                                LinphoneUtils.goXiaoMiMainager(IncomingCallActivity.this);
//                            } else if("samsung".equals(name)) {
//                                LinphoneUtils.goSangXinMainager(IncomingCallActivity.this);
//                            } else {
//                                LinphoneUtils.goIntentSetting(IncomingCallActivity.this);
//                            }
//
//                            var7.dismiss();
//                        }
//                    });
//                    var7.setNegativeButton(new View.OnClickListener() {
//                        public void onClick(View v) {
//                            var7.dismiss();
//                        }
//                    });
//                } else {
//                    IncomingCallActivity.this.answer();
//                }
            }
        });
        this.btn_hangup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                IncomingCallActivity.this.hangUp();
            }
        });
    }

    private void hangUp() {
        LinphoneCore lc = LinphoneManager.getLcIfManagerNotDestroyedOrNull();
        if(lc != null) {
            if(this.mCall != null) {
                lc.terminateCall(this.mCall);
            } else if(lc.isInConference()) {
                lc.terminateConference();
            } else {
                lc.terminateAllCalls();
            }
        }

        this.finish();
    }

    public static IncomingCallActivity instance() {
        return instance;
    }

    public static boolean isInstanciated() {
        return instance != null;
    }

    private void answer() {
        LinphoneCallParams call = LinphoneManager.getLc().createDefaultCallParameters();
//        if(!LinphoneUtils.isHightBandwidthConnection(this)) {
//            call.enableLowBandwidth(true);
//        }

        if(this.mCall != null && this.mCall.getState() != LinphoneCall.State.CallReleased && this.mCall.getState() != LinphoneCall.State.Error) {
            if(!LinphoneManager.getInstance().acceptCallWithParams(this.mCall, call)) {
                Toast.makeText(this, "不能接通电话", Toast.LENGTH_SHORT).show();
            } else {
                LinphoneCallParams param = this.mCall.getRemoteParams();
                Intent intent;
                if(param != null && param.getVideoEnabled()) {
                    this.acceptCallUpdate(true);
                    intent = new Intent(this, VideoCallActivity.class);
                    intent.putExtra("sayee_is_call_key", false);
                    intent.putExtra("sayee_call_incoming", true);
                    intent.putExtra("sayee_from_sip_domain", this.domain);
                    intent.putExtra("sayee_from_sip_number", this.fromSipName);
                    intent.putExtra("sayee_door_name", this.displayName);
                    this.startActivity(intent);
                    this.finish();
                } else {
                    this.acceptCallUpdate(false);
                    intent = new Intent(this, VideoCallActivity.class);
                    intent.putExtra("sayee_is_call_key", false);
                    intent.putExtra("sayee_call_incoming", true);
                    intent.putExtra("sayee_is_video", false);
                    intent.putExtra("sayee_from_sip_domain", this.domain);
                    intent.putExtra("sayee_from_sip_number", this.fromSipName);
                    intent.putExtra("sayee_door_name", this.displayName);
                    this.startActivity(intent);
                    this.finish();
                }
            }

        } else {
            this.finish();
        }
    }

    private void acceptCallUpdate(boolean paramBoolean) {
        if(this.mCall != null) {
            LinphoneCallParams params = this.mCall.getCurrentParamsCopy();
            if(paramBoolean) {
                params.setVideoEnabled(true);
                LinphoneManager.getLc().enableVideo(true, true);
            }

            try {
                LinphoneManager.getLc().acceptCallUpdate(this.mCall, params);
            } catch (LinphoneCoreException var4) {
                var4.printStackTrace();
            }

        }
    }

    public void onCallStateChanged(LinphoneCall call, LinphoneCall.State state, String paramString) {
        if(call != this.mCall || LinphoneCall.State.CallEnd != state && LinphoneCall.State.CallReleased != state) {
            if(state == LinphoneCall.State.StreamsRunning) {
                LinphoneManager.getLc().enableSpeaker(LinphoneManager.getLc().isSpeakerEnabled());
            }
        } else {
            this.finish();
        }

    }

    protected void onResume() {
        super.onResume();
       // LinphoneManager.addListener(this);
        if(LinphoneManager.getLcIfManagerNotDestroyedOrNull() != null) {
            List localLinphoneAddress = LinphoneUtils.getLinphoneCalls(LinphoneManager.getLc());
            Iterator var3 = localLinphoneAddress.iterator();

            while(var3.hasNext()) {
                LinphoneCall call = (LinphoneCall)var3.next();
                if(LinphoneCall.State.IncomingReceived == call.getState()) {
                    this.mCall = call;
                    break;
                }
            }
        }

        if(this.mCall == null) {
            Log.e("Couldn\'t find incoming call");
            this.finish();
        } else if(this.mCall.getState() != LinphoneCall.State.CallReleased && this.mCall.getState() != LinphoneCall.State.Error) {
            LinphoneAddress localLinphoneAddress1 = this.mCall.getRemoteAddress();
            if(localLinphoneAddress1 != null) {
                this.domain = localLinphoneAddress1.getDomain();
                this.fromSipName = localLinphoneAddress1.getUserName();
                this.displayName = localLinphoneAddress1.getDisplayName();
                if(!TextUtils.isEmpty(this.displayName)) {
                    if(localLinphoneAddress1.getDisplayName().contains(",")) {
                       // this.displayName = LinphoneUtils.codePoint2String(this.displayName);
                        this.tv_address.setText(this.displayName);
                    } else {
                        this.displayName = this.getDatabaseHelper().getDisplayName(this.displayName);
                        this.tv_address.setText(this.displayName);
                    }
                }
            }

        } else {
            this.finish();
        }
    }

    protected void onStart() {
        super.onStart();
        if(this.powerManager == null) {
            this.powerManager = (PowerManager)this.getSystemService(Context.POWER_SERVICE);
        }

        if(this.powerManager != null && this.mWakeLock == null) {
            this.mWakeLock = this.powerManager.newWakeLock(268435482, IncomingCallActivity.class.getSimpleName());
            if(this.mWakeLock != null) {
                this.mWakeLock.acquire();
            }
        }

    }

    protected void onPause() {
        super.onPause();
       // LinphoneManager.removeListener(this);
        if(this.mWakeLock != null && this.mWakeLock.isHeld()) {
            this.mWakeLock.release();
        }

    }

    @SuppressLint({"Wakelock"})
    protected void onDestroy() {
        super.onDestroy();
        if(this.mWakeLock != null && this.mWakeLock.isHeld()) {
            this.mWakeLock.release();
        }
    }

    private DatabaseHelper getDatabaseHelper() {
        if(this.databaseHelper == null) {
            this.databaseHelper = new DatabaseHelper(this.getApplicationContext());
        }

        return this.databaseHelper;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch(keyCode) {
            case 4:
                try {
                    long e = System.currentTimeMillis();
                    if(e - this.firstTime > 2000L) {
                        Toast.makeText(this, "再按一次将挂断通话", Toast.LENGTH_SHORT).show();
                        this.firstTime = e;
                        return true;
                    } else {
                        this.hangUp();
                    }
                } catch (Exception var5) {
                    var5.printStackTrace();
                }
            default:
                return super.onKeyUp(keyCode, event);
        }
    }
}
