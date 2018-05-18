package sdk_sample.sdk.activity;

/**
 * Created by Android on 2017/3/23.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.biz.GetDataBiz;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.SharedPreferencesUtil1;

import org.json.JSONException;
import org.json.JSONObject;
import org.linphone.CallManager;
import org.linphone.LinphoneManager;
import org.linphone.core.LinphoneCall;
import org.linphone.core.LinphoneCall.State;
import org.linphone.core.LinphoneCore;
import org.linphone.mediastream.Log;
import org.linphone.mediastream.video.AndroidVideoWindowImpl;
import org.linphone.mediastream.video.AndroidVideoWindowImpl.VideoWindowListener;
import org.linphone.mediastream.video.capture.hwconf.AndroidCameraConfiguration;

import sdk_sample.sdk.HttpRespListener;
import sdk_sample.sdk.bean.LockRecordBean;
import sdk_sample.sdk.db.DatabaseHelper;
import sdk_sample.sdk.result.BaseResult;
import sdk_sample.sdk.utils.HttpUtils;
import sdk_sample.sdk.utils.SharedPreferencesUtil;
import sdk_sample.sdk.utils.ToolsUtil;
import sdk_sample.sdk.views.SlidingLinearLayout;

import static com.xinspace.csevent.app.AppConfig.COMMUNITY_AREA;

public class VideoCallActivity extends BaseActivity  {
    private AndroidVideoWindowImpl androidVideoWindowImpl;
    private SurfaceView mCaptureView;
    private SurfaceView mVideoView;
    SlidingLinearLayout btn_open;
    TextView tv_top_left;
    TextView tv_top_right;
    TextView tv_address;
    LinearLayout ll_top_left;
    LinearLayout ll_top_right;
    boolean isVideo = true;
    boolean isInComing = false;
    Chronometer tv_time;
    String path;
    String token;
    String userName;
    String fromSipName;
    String toSipDomain;
    long deal_time;
    String doorName;
    boolean isCall = false;
    String toSipNumber;
    String domain_sn;
    boolean isSpeakerEnabled = false;
    private boolean hasOutgoingEarlyMedia = false;
    private boolean isMyHandup = false;
    private boolean isBusy = false;
    private WakeLock mWakeLock;
    private PowerManager powerManager;
    private DatabaseHelper databaseHelper = null;
    long firstTime = 0L;

    private SDPreference preference;
    private String cUid;
    private String cToken;
    private String phone;
    private String area;

    public VideoCallActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(1);
        this.getWindow().setFlags(1024, 1024);
        this.getWindow().addFlags(2621440);

        preference = SDPreference.getInstance();
        cUid = preference.getContent("cUid");
        cToken = preference.getContent("cToken");
        phone = preference.getContent("mobile");
        area = SharedPreferencesUtil1.getString(this, COMMUNITY_AREA, "");

        if(this.getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        try {
            this.setContentView(ToolsUtil.getIdByName(this.getApplication(), "layout", "sayee_activity_video_call"));
            this.mVideoView = (SurfaceView)this.findViewById(ToolsUtil.getIdByName(this.getApplication(), "id", "videoSurface"));
            this.mCaptureView = (SurfaceView)this.findViewById(ToolsUtil.getIdByName(this.getApplication(), "id", "videoCaptureSurface"));
            this.btn_open = (SlidingLinearLayout)this.findViewById(ToolsUtil.getIdByName(this.getApplication(), "id", "btn_open"));
            this.ll_top_left = (LinearLayout)this.findViewById(ToolsUtil.getIdByName(this.getApplication(), "id", "ll_top_left"));
            this.ll_top_right = (LinearLayout)this.findViewById(ToolsUtil.getIdByName(this.getApplication(), "id", "ll_top_right"));
            this.tv_top_left = (TextView)this.findViewById(ToolsUtil.getIdByName(this.getApplication(), "id", "tv_top_left"));
            this.tv_top_right = (TextView)this.findViewById(ToolsUtil.getIdByName(this.getApplication(), "id", "tv_top_right"));
            this.tv_time = (Chronometer)this.findViewById(ToolsUtil.getIdByName(this.getApplication(), "id", "tv_time"));
            this.tv_address = (TextView)this.findViewById(ToolsUtil.getIdByName(this.getApplication(), "id", "tv_address"));
        } catch (Exception var3) {
            var3.printStackTrace();
            return;
        }

        this.ll_top_right.setVisibility(View.VISIBLE);
        this.tv_top_left.setText("门口监控");
        this.tv_top_right.setText("听筒");
        if(this.getIntent() != null) {
            this.initData();
        } else if(savedInstanceState != null) {
            this.isInComing = savedInstanceState.getBoolean("sayee_call_incoming", false);
            this.isVideo = savedInstanceState.getBoolean("sayee_is_video");
            this.domain_sn = savedInstanceState.getString("sayee_domain_sn");
            this.toSipNumber = savedInstanceState.getString("sayee_from_sip_number");
            this.toSipDomain = savedInstanceState.getString("sayee_from_sip_domain");
            this.isSpeakerEnabled = savedInstanceState.getBoolean("sayee_speaker_enabled");
            this.path = savedInstanceState.getString("sayee_path_url");
            this.token = savedInstanceState.getString("sayee_token");
            this.userName = savedInstanceState.getString("sayee_username");
            this.deal_time = savedInstanceState.getLong("sayee_deal_time", 0L);
            this.doorName = savedInstanceState.getString("sayee_door_name");
            this.hasOutgoingEarlyMedia = savedInstanceState.getBoolean("sayee_early_media", false);
            this.isCall = savedInstanceState.getBoolean("sayee_is_call_key", false);
            if(this.isSpeakerEnabled) {
                this.tv_top_right.setText("听筒");
            } else {
                this.tv_top_right.setText("免提");
            }

            if(LinphoneManager.getLcIfManagerNotDestroyedOrNull() != null) {
                LinphoneManager.getInstance().routeAudioToSpeaker();
                LinphoneManager.getLc().enableSpeaker(this.isSpeakerEnabled);
            }
        } else {
            this.isSpeakerEnabled = true;
            if(LinphoneManager.getLcIfManagerNotDestroyedOrNull() != null) {
                LinphoneManager.getInstance().routeAudioToSpeaker();
                LinphoneManager.getLc().enableSpeaker(this.isSpeakerEnabled);
            }
        }

        this.tv_address.setText(this.doorName);
        if(!this.isVideo) {
            this.mVideoView.setVisibility(View.GONE);
            this.mCaptureView.setVisibility(View.GONE);
            this.tv_time.setTextColor(000000);
        }

        this.mCaptureView.getHolder().setType(3);
        this.fixZOrder(this.mVideoView, this.mCaptureView);
        this.androidVideoWindowImpl = new AndroidVideoWindowImpl(this.mVideoView, this.mCaptureView, new VideoWindowListener() {
            public void onVideoPreviewSurfaceDestroyed(AndroidVideoWindowImpl paramAnonymousAndroidVideoWindowImpl) {
                LinphoneCore localLinphoneCore = LinphoneManager.getLcIfManagerNotDestroyedOrNull();
                if(localLinphoneCore != null) {
                    localLinphoneCore.setVideoWindow((Object)null);
                }
            }

            public void onVideoPreviewSurfaceReady(AndroidVideoWindowImpl paramAnonymousAndroidVideoWindowImpl, SurfaceView paramAnonymousSurfaceView) {
                VideoCallActivity.this.mCaptureView = paramAnonymousSurfaceView;
                if(LinphoneManager.getLcIfManagerNotDestroyedOrNull() != null) {
                    LinphoneManager.getLc().setPreviewWindow(VideoCallActivity.this.mCaptureView);
                }
            }

            public void onVideoRenderingSurfaceDestroyed(AndroidVideoWindowImpl paramAnonymousAndroidVideoWindowImpl) {
                LinphoneCore localLinphoneCore = LinphoneManager.getLcIfManagerNotDestroyedOrNull();
                if(localLinphoneCore != null) {
                    localLinphoneCore.setVideoWindow((Object)null);
                }
            }

            public void onVideoRenderingSurfaceReady(AndroidVideoWindowImpl vw, SurfaceView paramAnonymousSurfaceView) {
                LinphoneCore localLinphoneCore = LinphoneManager.getLcIfManagerNotDestroyedOrNull();
                if(localLinphoneCore != null) {
                    localLinphoneCore.setVideoWindow(vw);
                }
                VideoCallActivity.this.mVideoView = paramAnonymousSurfaceView;
            }
        });
        this.findViewById(ToolsUtil.getIdByName(this.getApplication(), "id", "btn_hangup")).setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                VideoCallActivity.this.hangUp();
            }
        });
        this.tv_top_right.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                VideoCallActivity.this.toggleSpeaker();
            }
        });
        this.ll_top_left.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                try {
                    long e = System.currentTimeMillis();
                    if(e - VideoCallActivity.this.firstTime > 2000L) {
                        Toast.makeText(VideoCallActivity.this, "再按一次将挂断通话", Toast.LENGTH_SHORT).show();
                        VideoCallActivity.this.firstTime = e;
                    } else {
                        VideoCallActivity.this.hangUp();
                    }
                } catch (Exception var4) {
                    var4.printStackTrace();
                }
            }
        });
    }

    private void initData() {
        this.isInComing = this.getIntent().getBooleanExtra("sayee_call_incoming", false);
        this.doorName = this.getIntent().getStringExtra("sayee_door_name");
        this.isVideo = this.getIntent().getBooleanExtra("sayee_is_video", true);
        this.domain_sn = this.getIntent().getStringExtra("sayee_domain_sn");
        this.deal_time = this.getIntent().getLongExtra("sayee_deal_time", 0L);
        this.toSipNumber = this.getIntent().getStringExtra("sayee_from_sip_number");
        this.toSipDomain = this.getIntent().getStringExtra("sayee_from_sip_domain");
        this.token = this.getIntent().getStringExtra("sayee_token");
        this.userName = this.getIntent().getStringExtra("sayee_username");
        this.path = this.getIntent().getStringExtra("sayee_path_url");
        this.isCall = this.getIntent().getBooleanExtra("sayee_is_call_key", false);
        SharedPreferencesUtil.saveData(this.getApplicationContext(), "sayee_sip_is_call_key", Boolean.valueOf(this.isCall));
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.setIntent(intent);
        if(this.getIntent() != null) {
            this.initData();
        }

    }

    private void fixZOrder(SurfaceView paramSurfaceView1, SurfaceView paramSurfaceView2) {
        paramSurfaceView1.setZOrderOnTop(false);
        paramSurfaceView2.setZOrderOnTop(true);
        paramSurfaceView2.setZOrderMediaOverlay(true);
    }

    @SuppressLint({"Wakelock"})
    public void onDestroy() {
        this.mCaptureView = null;
        if(this.mVideoView != null) {
            this.mVideoView.setOnTouchListener((OnTouchListener)null);
            this.mVideoView = null;
        }

        if(this.androidVideoWindowImpl != null) {
            this.androidVideoWindowImpl.release();
            this.androidVideoWindowImpl = null;
        }

        if(this.mWakeLock != null && this.mWakeLock.isHeld()) {
            this.mWakeLock.release();
        }

       // LinphoneManager.removeListener(this);
        SharedPreferencesUtil.saveData(this.getApplicationContext(), "sayee_sip_is_call_key", Boolean.valueOf(true));
        super.onDestroy();
        System.gc();
    }

    public boolean onTouchEvent(MotionEvent event) {
        if(this.btn_open != null && this.btn_open.handleActivityEvent(event) && !TextUtils.isEmpty(this.toSipNumber)) {
            SharedPreferencesUtil.saveData(this, "sayee_user_name_key", this.userName);
            SharedPreferencesUtil.saveData(this, "sayee_domain_sn_key", this.domain_sn);
            SharedPreferencesUtil.saveData(this, "sayee_type_key", Integer.valueOf(1));
            HttpUtils.openDoorLock(this, this.path, this.token, this.userName, this.domain_sn, 1, this.toSipNumber, this.toSipDomain, new HttpRespListener() {
                public void onSuccess(int code, BaseResult result) {

                    LockRecordBean bean = new LockRecordBean();
                    bean.setUid(cUid);
                    bean.setToken(cToken);
                    bean.setPhone(phone);
                    bean.setEquip_sn(domain_sn);
                    bean.setType("1");
                    addEntranceRecord(bean);

                    ToolsUtil.toast(VideoCallActivity.this, "已发送开锁请求");
                }

                public void onFail(int code, String msg) {
                    if(code != 3) {
                        ToolsUtil.toast(VideoCallActivity.this, msg);
                    } else {
                        Intent intent = new Intent();
                        intent.setAction("com.sayee.sdk.action.token.fail");
                        intent.putExtra("sayee_callback_code", 1);
                        intent.putExtra("sayee_error_msg", "token重新获取失败");
                        VideoCallActivity.this.sendBroadcast(intent);
                    }
                }
            });
        }

        return super.onTouchEvent(event);
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

    public void onPause() {
        if(this.androidVideoWindowImpl != null) {
            AndroidVideoWindowImpl var1 = this.androidVideoWindowImpl;
            synchronized(this.androidVideoWindowImpl) {
                if(LinphoneManager.isInstanciated() && LinphoneManager.getLcIfManagerNotDestroyedOrNull() != null) {
                    LinphoneManager.getLcIfManagerNotDestroyedOrNull().setVideoWindow((Object)null);
                }
            }
        }

        if(this.mVideoView != null) {
            ((GLSurfaceView)this.mVideoView).onPause();
        }

        if(this.mWakeLock != null && this.mWakeLock.isHeld()) {
            this.mWakeLock.release();
        }

        super.onPause();
    }

    public void onResume() {
        super.onResume();
        if(LinphoneManager.isInstanciated() && LinphoneManager.getLcIfManagerNotDestroyedOrNull() != null && LinphoneManager.getLcIfManagerNotDestroyedOrNull().getCallsNb() == 0) {
            this.finish();
        } else {
//            LinphoneManager.removeListener(this);
//            LinphoneManager.addListener(this);
            if(this.mVideoView != null) {
                ((GLSurfaceView)this.mVideoView).onResume();
            }

            if(this.androidVideoWindowImpl != null) {
                AndroidVideoWindowImpl var1 = this.androidVideoWindowImpl;
                synchronized(this.androidVideoWindowImpl) {
                    if(LinphoneManager.getLcIfManagerNotDestroyedOrNull() != null) {
                        LinphoneManager.getLc().setVideoWindow(this.androidVideoWindowImpl);
                    }
                }
            }

            this.refreshTime();
            if(TextUtils.isEmpty(this.path)) {
                this.path = (String)SharedPreferencesUtil.getData(this, "sayee_path_url", "");
            }

            if(TextUtils.isEmpty(this.domain_sn)) {
                domain_sn = this.getDatabaseHelper().getDomainSn(this.toSipNumber);
            }
        }
    }

    private void refreshTime() {
        if(this.tv_time != null && LinphoneManager.getLcIfManagerNotDestroyedOrNull() != null) {
            LinphoneCall[] var4;
            int var3 = (var4 = LinphoneManager.getLc().getCalls()).length;

            for(int var2 = 0; var2 < var3; ++var2) {
                LinphoneCall call = var4[var2];
                if(call != null) {
                    int i = call.getDuration();
                    if(i == 0 && call.getState() != State.StreamsRunning) {
                        this.tv_time.stop();
                        return;
                    }

                    this.tv_time.setBase(SystemClock.elapsedRealtime() - (long)(i * 1000));
                    this.tv_time.start();
                }
            }
        }
    }

    public void switchCamera() {
        try {
            if(LinphoneManager.getLcIfManagerNotDestroyedOrNull() == null) {
                return;
            }

            int ae = LinphoneManager.getLc().getVideoDevice();
            ae = (ae + 1) % AndroidCameraConfiguration.retrieveCameras().length;
            LinphoneManager.getLc().setVideoDevice(ae);
            CallManager.getInstance().updateCall();
            if(this.mCaptureView != null) {
                LinphoneManager.getLc().setPreviewWindow(this.mCaptureView);
            }
        } catch (ArithmeticException var2) {
            var2.printStackTrace();
            Log.e("Cannot swtich camera : no camera");
        }
    }

    private void toggleSpeaker() {
        if(!this.isSpeakerEnabled) {
            if(LinphoneManager.isInstanciated()) {
                LinphoneManager.getInstance().routeAudioToSpeaker();
            }

            this.isSpeakerEnabled = true;
            this.tv_top_right.setText("听筒");
        } else {
            if(LinphoneManager.isInstanciated()) {
                LinphoneManager.getInstance().routeAudioToReceiver();
            }

            this.tv_top_right.setText("免提");
            this.isSpeakerEnabled = false;
        }

        if(LinphoneManager.getLcIfManagerNotDestroyedOrNull() != null) {
            LinphoneManager.getLc().enableSpeaker(this.isSpeakerEnabled);
        }
    }

    private void hangUp() {
        this.isMyHandup = true;
        LinphoneCore lc = LinphoneManager.getLcIfManagerNotDestroyedOrNull();
        if(lc != null) {
            LinphoneCall currentCall = lc.getCurrentCall();
            if(currentCall != null) {
                lc.terminateCall(currentCall);
            } else if(lc.isInConference()) {
                lc.terminateConference();
            } else {
                lc.terminateAllCalls();
            }
        }
        this.finish();
    }

    public void onCallStateChanged(LinphoneCall call, State state, String paramString) {
        if(state == State.OutgoingEarlyMedia) {
            this.hasOutgoingEarlyMedia = true;
        } else if(state == State.Error) {
            if(ToolsUtil.isRegistration() && !this.isInComing && !this.isBusy) {
                ToolsUtil.toast(this, "门口机正在通话中，请稍后再试！");
            }
            this.isBusy = true;
            this.hasOutgoingEarlyMedia = true;
        } else if(state == State.CallEnd) {
            if(!this.hasOutgoingEarlyMedia && !this.isInComing && !this.isMyHandup && call != null && call.getDuration() * 1000 < 3000) {
                ToolsUtil.toast(this, "门口机在不线，请稍后再试！");
            }
            this.hasOutgoingEarlyMedia = false;
        }

        if(LinphoneManager.getLcIfManagerNotDestroyedOrNull() != null && LinphoneManager.getLc().getCallsNb() != 0) {
            this.refreshTime();
        } else {
            this.finish();
            if(LinphoneManager.getLcIfManagerNotDestroyedOrNull() != null) {
                LinphoneManager.getLcIfManagerNotDestroyedOrNull().terminateAllCalls();
            }
        }
    }

    protected void onStart() {
        super.onStart();
        if(this.powerManager == null) {
            this.powerManager = (PowerManager)this.getSystemService(Context.POWER_SERVICE);
        }

        if(this.powerManager != null && this.mWakeLock == null) {
            this.mWakeLock = this.powerManager.newWakeLock(268435482, VideoCallActivity.class.getSimpleName());
            if(this.mWakeLock != null) {
                this.mWakeLock.acquire();
            }
        }
    }

    private DatabaseHelper getDatabaseHelper() {
        if(this.databaseHelper == null) {
            this.databaseHelper = new DatabaseHelper(this.getApplicationContext());
        }
        return this.databaseHelper;
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.isVideo = savedInstanceState.getBoolean("sayee_is_video");
        this.domain_sn = savedInstanceState.getString("sayee_domain_sn");
        this.deal_time = savedInstanceState.getLong("sayee_deal_time", 0L);
        this.toSipNumber = savedInstanceState.getString("sayee_from_sip_number");
        this.toSipDomain = savedInstanceState.getString("sayee_from_sip_domain");
        this.isSpeakerEnabled = savedInstanceState.getBoolean("sayee_speaker_enabled");
        this.path = savedInstanceState.getString("sayee_path_url");
        this.token = savedInstanceState.getString("sayee_token");
        this.userName = savedInstanceState.getString("sayee_username");
        this.isInComing = savedInstanceState.getBoolean("sayee_call_incoming", false);
        this.doorName = savedInstanceState.getString("sayee_door_name");
        this.hasOutgoingEarlyMedia = savedInstanceState.getBoolean("sayee_early_media", false);
        this.isCall = savedInstanceState.getBoolean("sayee_is_call_key", false);
    }

    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean("sayee_is_video", this.isVideo);
        savedInstanceState.putString("sayee_domain_sn", this.domain_sn);
        savedInstanceState.putLong("sayee_deal_time", this.deal_time);
        savedInstanceState.putString("sayee_from_sip_number", this.toSipNumber);
        savedInstanceState.putString("sayee_from_sip_domain", this.toSipDomain);
        savedInstanceState.putBoolean("sayee_speaker_enabled", this.isSpeakerEnabled);
        savedInstanceState.putString("sayee_path_url", this.path);
        savedInstanceState.putString("sayee_token", this.token);
        savedInstanceState.putString("sayee_username", this.userName);
        savedInstanceState.putBoolean("sayee_call_incoming", this.isInComing);
        savedInstanceState.putString("sayee_door_name", this.doorName);
        savedInstanceState.putBoolean("sayee_early_media", this.hasOutgoingEarlyMedia);
        savedInstanceState.putBoolean("sayee_is_call_key", this.isCall);
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

