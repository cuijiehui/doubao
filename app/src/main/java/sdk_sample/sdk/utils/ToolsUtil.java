package sdk_sample.sdk.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;


import com.xinspace.csevent.util.LogUtil;

import org.linphone.LinphoneManager;
import org.linphone.core.LinphoneCall;
import org.linphone.core.LinphoneCore;
import org.linphone.core.LinphoneCoreException;
import org.linphone.core.LinphoneProxyConfig;

import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import sdk_sample.sdk.HttpRespListener;
import sdk_sample.sdk.LinphoneService;
import sdk_sample.sdk.SayeeManager;
import sdk_sample.sdk.bean.OpenLockPasswordBean;
import sdk_sample.sdk.result.BaseResult;
import sdk_sample.sdk.result.OpenLockPasswordResult;

/**
 * Created by Android on 2017/3/23.
 */

public class ToolsUtil {
    private static Timer timer = null;

    private ToolsUtil() {
    }

    public static final String MD5(String s) {
        char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        try {
            byte[] e = s.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(e);
            byte[] md = mdInst.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;

            for(int i = 0; i < j; ++i) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 15];
                str[k++] = hexDigits[byte0 & 15];
            }

            return new String(str);
        } catch (Exception var10) {
            var10.printStackTrace();
            return null;
        }
    }

    public static final String getUuid(Context context) {
        return context == null?null: Settings.Secure.getString(context.getContentResolver(), "android_id");
    }

    public static void getToken(Context context, String key, String userName, String app_id, String oldToken, HttpRespListener listener) {

        Log.i("www" ,"------走到这个方法里来了");

        HttpUtils.getToken(context, key, userName, app_id, oldToken, listener);
    }

    public static void goToOpenDoor(Context context, Map<String, String> headpParams, Map<String, String> params, String  flag ,HttpRespListener listener) {

        LogUtil.i("goToOpenDoor走到这个方法里来了");
        HttpUtils.goToOpenDoor(context, headpParams, params, flag ,listener);
    }

    public static void goToOpenDoor2(Context context, Map<String, String> headpParams, Map<String, String> params, String  flag ,HttpRespListener listener) {

        LogUtil.i("goToOpenDoor2走到这个方法里来了");
        HttpUtils.goToOpenDoor2(context, headpParams, params, flag ,listener);
    }


    public static void goToOpenDoor3(Context context, Map<String, String> headpParams, Map<String, String> params, String  flag ,HttpRespListener listener) {

        LogUtil.i("goToOpenDoor2走到这个方法里来了");
        HttpUtils.goToOpenDoor3(context, headpParams, params, flag ,listener);
    }




    public static void setAllowRinging(Context context, boolean isAllowRinging) {
        SharedPreferencesUtil.setAllowRinging(context, isAllowRinging);
    }

    public static void setAllowVibrate(Context context, boolean isAllowVibrate) {
        SharedPreferencesUtil.setAllowVibrate(context, isAllowVibrate);
    }

    public static void outgoingCall(Context context, String toSip, Intent intent) {
        if(TextUtils.isEmpty(toSip)) {
           // Log.e("toSip is null");
        } else {
            LinphoneCore lc = LinphoneManager.getLcIfManagerNotDestroyedOrNull();
            if(lc != null) {
                try {
                    if(!LinphoneManager.getInstance().acceptCallIfIncomingPending()) {
                        LinphoneManager.getInstance().newOutgoingCall(toSip, "");
                        if(context != null && intent != null) {
                            intent.addFlags(268435456);
                            context.startActivity(intent);
                        }
                    }
                } catch (LinphoneCoreException var5) {
                    var5.printStackTrace();
                }
            }

        }
    }

    public static void hangUp() {
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

    }

    public static Bitmap getBitmap4Assets(Context context, String fileName) {
        InputStream inStream = context.getClass().getClassLoader().getResourceAsStream("assets/" + fileName);
        return BitmapFactory.decodeStream(inStream);
    }

    public static Drawable getDrawable4Assets(Context context, String fileName) {
        InputStream inStream = context.getClass().getClassLoader().getResourceAsStream("assets/" + fileName);
        return new BitmapDrawable(BitmapFactory.decodeStream(inStream));
    }

    public static int getIdByName(Context context, String className, String name) {
        String packageName = context.getPackageName();
        Class r = null;
        int id = 0;

        try {
            r = Class.forName(packageName + ".R");
            Class[] e = r.getClasses();
            Class desireClass = null;

            for(int i = 0; i < e.length; ++i) {
                if(e[i].getName().split("\\$")[1].equals(className)) {
                    desireClass = e[i];
                    break;
                }
            }

            if(desireClass != null) {
                id = desireClass.getField(name).getInt(desireClass);
            }
        } catch (ClassNotFoundException var9) {
            var9.printStackTrace();
        } catch (IllegalArgumentException var10) {
            var10.printStackTrace();
        } catch (SecurityException var11) {
            var11.printStackTrace();
        } catch (IllegalAccessException var12) {
            var12.printStackTrace();
        } catch (NoSuchFieldException var13) {
            var13.printStackTrace();
        }
        return id;
    }

    public static void toast(Context context, String msg) {
        if(context != null) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isNetworkConnected(Context context) {
        if(context == null) {
            return false;
        } else {
            try {
                ConnectivityManager e = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo ni = e.getActiveNetworkInfo();
                return ni != null && ni.isConnectedOrConnecting();
            } catch (Exception var3) {
                var3.printStackTrace();
                return false;
            }
        }
    }

    public static void refreshRegisters(final Context context) {
        if(context != null) {
            if(isNetworkConnected(context)) {
                if(LinphoneManager.getLcIfManagerNotDestroyedOrNull() != null) {
                    //Log.i("sip刷新註冊");
                    LinphoneManager.getLc().refreshRegisters();
                } else {
                    System.gc();
//                    if(LinphoneService.isReady()) {
//                        //Log.i("关闭，重新注册");
//                        SayeeManager.getInstance().turnOffCall(context);
//                        if(timer == null) {
//                            timer = new Timer();
//                        } else {
//                            timer.cancel();
//                            timer = new Timer();
//                        }
//
//                        timer.schedule(new TimerTask() {
//                            public void run() {
//                                if(!ToolsUtil.isRegistration()) {
//                                    SayeeManager.getInstance().turnOnCall(context);
//                                    //Log.i("重新注册---- on");
//                                    int i = 0;
//
//                                    while(!LinphoneService.isReady()) {
//                                        try {
//                                            Thread.sleep(30L);
//                                            ++i;
//                                            if(i == 200) {
//                                               // Log.i("重新注册----break");
//                                                break;
//                                            }
//                                            //Log.i("重新注册---- sleep");
//                                        } catch (InterruptedException var3) {
//                                            var3.printStackTrace();
//                                        }
//                                    }
//
//                                   // Log.i("重新注册---- end");
//                                    if(LinphoneManager.getLcIfManagerNotDestroyedOrNull() != null) {
//                                        LinphoneManager.getLc().refreshRegisters();
//                                    }
//                                }
//                            }
//                        }, 1500L);
//                    } else {
//                        //Log.i("重新注册");
//                        SayeeManager.getInstance().turnOnCall(context);
//                    }
                }
            }
        }
    }

    public static boolean isRegistration() {
        LinphoneCore lc = LinphoneManager.getLcIfManagerNotDestroyedOrNull();
        if(lc != null) {
            LinphoneProxyConfig config = lc.getDefaultProxyConfig();
            if(config != null) {
                return config.isRegistered();
            }
        }
        return false;
    }

    public static void callback(final Context context, int code, String newToken, long time) {
        if(!TextUtils.isEmpty(newToken)) {
            SharedPreferencesUtil.saveData(context, "sayee_user_token_key", newToken);
        }

        if(time != 0L) {
            SharedPreferencesUtil.saveData(context, "sayee_deal_time", Long.valueOf(time));
        }

        switch(code) {
            case 1:
                String userName = SharedPreferencesUtil.getUserName(context);
                String domain_sn = SharedPreferencesUtil.getDomainSn(context);
                int type = SharedPreferencesUtil.getType(context);
                String path = SharedPreferencesUtil.getTwoUrl(context);
                String toSipUri = SharedPreferencesUtil.getToSipNumber(context);
                if(TextUtils.isEmpty(path)) {
                    return;
                }

                HttpUtils.openDoorLock(context, path, newToken, userName, domain_sn, type, toSipUri, (String)null, new HttpRespListener() {
                    public void onSuccess(int code, BaseResult result) {
                        ToolsUtil.toast(context, "已开锁请求");
                    }

                    public void onFail(int code, String msg) {
                        ToolsUtil.toast(context, msg);
                    }
                });
                break;
            case 2:
                String path1 = SharedPreferencesUtil.getTwoUrl(context);
                String userName1 = SharedPreferencesUtil.getUserName(context);
                String sip_number = SharedPreferencesUtil.getToSipNumber(context);
                if(TextUtils.isEmpty(path1)) {
                    return;
                }

                HttpUtils.getOpenLockPassword(path1, newToken, userName1, sip_number, new HttpRespListener() {
                    public void onSuccess(int code, BaseResult result) {
                        OpenLockPasswordBean bean = ((OpenLockPasswordResult)result).getResult();
                        Intent intent = new Intent();
                        intent.setAction("com.sayee.sdk.action.random.code");
                        intent.putExtra("sayee_random_password", bean.getRandom_pw());
                        intent.putExtra("sayee_random_password_deadline", bean.getRandomkey_dead_time());
                        context.sendBroadcast(intent);
                    }

                    public void onFail(int code, String msg) {
                        ToolsUtil.toast(context, msg);
                    }
                });
        }

    }
}

