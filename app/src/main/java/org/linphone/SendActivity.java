package org.linphone;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.xinspace.csevent.R;

import org.linphone.core.LinphoneAuthInfo;
import org.linphone.core.LinphoneCall;
import org.linphone.core.LinphoneChatMessage;
import org.linphone.core.LinphoneChatRoom;
import org.linphone.core.LinphoneCore;
import org.linphone.core.LinphoneCoreListenerBase;
import org.linphone.core.LinphoneProxyConfig;
import org.linphone.mediastream.Log;
import org.linphone.ui.AddressText;
import org.linphone.ui.CallButton;

/**
 * Created by Android on 2017/2/23.
 *
 * 拨打电话
 */

public class SendActivity extends Activity{

    private OrientationEventListener mOrientationHelper;
    private LinphoneCoreListenerBase mListener;
    private static SendActivity sendActivity;
    private static boolean isCallTransferOngoing = false;
    public boolean mVisible;
    private AddressText mAddress;
    private CallButton mCall;
    private ImageView mAddContact;
    private View.OnClickListener addContactListener, cancelListener, transferListener;
    private boolean shouldEmptyAddressField = true;
    private boolean isInCall, isAttached = false;

    private TextView statusText;
    private ImageView statusLed;
    private static final int CALL_ACTIVITY = 19;

    private static SendActivity instance;

    static final boolean isInstanciated() {
        return instance != null;
    }

    public static final SendActivity instance() {
        if (instance != null)
            return instance;
        throw new RuntimeException("LinphoneActivity not instantiated yet");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_send);
        instance = this;
        initView();
    }

    private void initView() {

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

        statusText = (TextView) findViewById(R.id.statusText);
        statusLed = (ImageView) findViewById(R.id.statusLed);

        mAddress = (AddressText) findViewById(R.id.Adress);
        mAddress.setDialerFragment(this);

        mCall = (CallButton) findViewById(R.id.Call);
        mCall.setAddressWidget(mAddress);

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
            public void callState(LinphoneCore lc, LinphoneCall call, LinphoneCall.State state, String message) {
                if (state == LinphoneCall.State.IncomingReceived) {
                    Log.i("www" , "这里是电话打进来界面");
                    startActivity(new Intent(SendActivity.this, IncomingCallActivity.class));
                } else if (state == LinphoneCall.State.OutgoingInit) {
                    if (call.getCurrentParamsCopy().getVideoEnabled()) {
                        startVideoActivity(call);
                    } else {
                        Log.i("www" , "是不是这里" + 11111);
                        startIncallActivity(call);
                    }
                } else if (state == LinphoneCall.State.CallEnd || state == LinphoneCall.State.Error || state == LinphoneCall.State.CallReleased){
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
            Log.i("www" , "走到这里添加监听了没");
            lc.addListener(mListener);
        }
    }

    public void startIncallActivity(LinphoneCall currentCall) {
        Log.i("www" , "走到这里了2222222222222");
        Intent intent = new Intent(this, InCallActivity.class);
        intent.putExtra("VideoEnabled", false);
        startOrientationSensor();
        startActivityForResult(intent, CALL_ACTIVITY);
    }


    public void startVideoActivity(LinphoneCall currentCall) {
        Log.i("www" , "走到这里了11111111111111");
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

            Log.d("Phone orientation changed to ", degrees);
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
