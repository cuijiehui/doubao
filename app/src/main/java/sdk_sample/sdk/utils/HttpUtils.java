package sdk_sample.sdk.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.xinspace.csevent.util.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.linphone.LinphoneManager;
import org.linphone.LinphonePreferences;
import org.linphone.core.LinphoneAddress;
import org.linphone.core.LinphoneAuthInfo;
import org.linphone.core.LinphoneChatMessage;
import org.linphone.core.LinphoneChatRoom;
import org.linphone.core.LinphoneCore;
import org.linphone.core.LinphoneCoreException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sdk_sample.sdk.HttpRespListener;
import sdk_sample.sdk.SayeeManager;
import sdk_sample.sdk.activity.LockListActivity;
import sdk_sample.sdk.bean.ComBean;
import sdk_sample.sdk.bean.LockBean;
import sdk_sample.sdk.bean.NeiborBean;
import sdk_sample.sdk.bean.OpenLockPasswordBean;
import sdk_sample.sdk.bean.UserBean;
import sdk_sample.sdk.result.BaseResult;
import sdk_sample.sdk.result.LockListResult;
import sdk_sample.sdk.result.LoginSipBean;
import sdk_sample.sdk.result.NeiborResult;
import sdk_sample.sdk.result.OpenLockPasswordResult;
import sdk_sample.sdk.result.TokenResult;

import static com.xinspace.csevent.app.CoresunApp.username;

/**
 * Created by Android on 2017/3/23.
 */

public class HttpUtils {
    public static final String baseUrl = "https://api.sayee.cn:28084";
    static final String GET_TOKEN_URL = "https://api.sayee.cn:28084/sdk/get_ylb_token.json";
    static final String GET_NEIBOR_ID_URL = "https://api.sayee.cn:28084/fir_platform/get_neibor_and_user_msg.json";
    private HttpUtils() {
    }

    public static void getToken(Context context, String userName, String oldToken, HttpRespListener listener) {
        getToken(context, (String)null, userName, (String)null, oldToken, listener);
    }

    public static void getToken(final Context context, String key, String userName, String app_id, String oldToken, final HttpRespListener listener) {
        HashMap params = new HashMap();
        if(TextUtils.isEmpty(oldToken)) {
            params.put("key", key);
            params.put("username", userName);
            params.put("app_id", app_id);
        } else {
            params.put("username", userName);
            params.put("token", oldToken);
        }

        try {//      https://api.sayee.cn:28084     https://gdsayee.cn:27072
            HttpURLConnectionUtil.doGet("https://api.sayee.cn:28084/sdk/get_ylb_token.json", null, params, new Handler() {
                public void dispatchMessage(Message msg) {
                    if(msg.what == 200) {
                        try {
                            if(msg.obj == null) {
                                return;
                            }

                            Log.i("www" , "Token" + msg.obj.toString());

                            JSONObject e = new JSONObject(((BaseResult)msg.obj).getMsg());
                            int code = e.getInt("code");
                            if(code == 0) {
                                JSONObject myMsg = e.getJSONObject("result");
                                String token = myMsg.getString("token");
                                long dead_time = myMsg.getLong("dead_time");
                                String myMsg1 = e.getString("msg");
                                if(listener != null) {
                                    TokenResult tokenResult = new TokenResult();
                                    tokenResult.setCode(code);
                                    tokenResult.setMsg(myMsg1);
                                    tokenResult.setToken(token);
                                    tokenResult.setDead_time(dead_time);
                                    listener.onSuccess(code, tokenResult);
                                    SharedPreferencesUtil.saveData(context, "sayee_user_token_key", token);
                                    SharedPreferencesUtil.saveData(context, "sayee_deal_time", Long.valueOf(dead_time));
                                }
                            }
                            else if(listener != null) {
                                String myMsg2 = e.getString("msg");
                                listener.onFail(code, myMsg2);
                            }
                        } catch (JSONException var10) {
                            var10.printStackTrace();
                        }
                    }
                    else if(listener != null && msg.obj != null) {
                        listener.onFail(msg.what, ((BaseResult)msg.obj).getMsg());
                    }

                }
            });
        } catch (Exception var8) {
            var8.printStackTrace();
        }

    }

    public static void goToOpenDoor(final Context context, Map<String, String> headpParams, Map<String, String> params,
                                    final String flag , final HttpRespListener listener) {

        final String token = headpParams.get("token");
        final String userName = headpParams.get("username");
        final String dealTime = headpParams.get("dealtime");

        try {
            HttpURLConnectionUtil.doGet(GET_NEIBOR_ID_URL, headpParams, params, new Handler() {
                public void dispatchMessage(Message msg) {
                    if(msg.what == 200) {
                        try {
                            if(msg.obj == null) {
                                return;
                            }

                            JSONObject e = new JSONObject(((BaseResult)msg.obj).getMsg());
                            int code = e.getInt("code");

                            Log.i("www" , "------code------------" + code);

                            if(code == 0) {
                                try {

                                    Log.i("www" , "--------0-----------" + e.toString());

                                    Log.i("www" , "--------1-----------");

                                    if(listener != null) {
                                        Log.i("www" , "--------2-----------");

                                        listener.onSuccess(msg.what, (BaseResult)msg.obj);

                                        SayeeManager.getInstance().turnOnCall(context);
                                    }
                                } catch (Exception var16) {
                                    var16.printStackTrace();
                                }
                            }
                            else if(listener != null) {
                                String myMsg1 = e.getString("msg");
                                listener.onFail(code, myMsg1);
                            }
                        } catch (JSONException var17) {
                            var17.printStackTrace();
                        }
                    }
                    else if(listener != null && msg.obj != null) {
                        listener.onFail(msg.what, ((BaseResult)msg.obj).getMsg());
                    }

                }
            });
        } catch (Exception var8) {
            var8.printStackTrace();
        }



    }

    public static void goToOpenDoor2(final Context context, Map<String, String> headpParams, Map<String, String> params, final String flag , final HttpRespListener listener) {
        if(context == null) {
            Log.e( "www" ,"context cannot be null");
        } else if(headpParams == null) {
            Log.e( "www" , "headpParams cannot be null");
        } else if(params == null) {
            Log.e("www" , "params cannot be null");
        } else {
            final String token = (String)headpParams.get("token");
            final String userName = (String)headpParams.get("username");
            final String dealTime = (String)headpParams.get("dealtime");
            if(TextUtils.isEmpty(token)) {
                Log.e("www" , "token cannot be null");
            } else if(TextUtils.isEmpty(userName)) {
                Log.e("www" , "username cannot be null");
            } else if(TextUtils.isEmpty(dealTime)) {
                Log.e("www" , "dealtime cannot be null");
            } else {
                try {//https://api.sayee.cn:28084
                    HttpURLConnectionUtil.doGet("https://api.sayee.cn:28084/fir_platform/get_neibor_and_user_msg.json", headpParams, params, new Handler() {
                        public void dispatchMessage(Message msg) {
                            if(msg.what == 200) {
                                try {
                                    if(msg.obj == null) {
                                        return;
                                    }

                                    JSONObject e = new JSONObject(((BaseResult)msg.obj).getMsg());
                                    int code = e.getInt("code");

                                    Log.i("www" , "------code------------" + code);

                                    if(code == 0) {

                                        try {

                                            Log.i("www" , "--------0-----------" + e.toString());

                                            //NeiborResult myMsg = JsonParserUtil.parseJson2Object(NeiborResult.class, e);

                                            Log.i("www" , "--------1-----------");

                                            if(listener != null) {
                                                Log.i("www" , "--------2-----------");

                                                BaseResult baseResult = new BaseResult();

                                                listener.onSuccess(code, baseResult);

                                                JSONObject jsonObject = e.getJSONObject("result");


                                                long mdealTime = 0L;

                                                try {
                                                    mdealTime = Long.parseLong(dealTime);
                                                } catch (Exception var15) {
                                                    var15.printStackTrace();
                                                }

                                                Log.i("www" , "-------------------");

                                                JSONObject jsonObject1 = jsonObject.getJSONObject("user_msg");
//                                                final String sip_number = jsonObject1.getString("user_sip");
//                                                final String sip_password = userBean.getUser_password();
//                                                final String sip_domin = userBean.getFs_ip();
//                                                final int sip_port = userBean.getFs_port();

                                                String sip_number = jsonObject1.getString("user_sip");
                                                String sip_password = jsonObject1.getString("user_password");
                                                String sip_domin = jsonObject1.getString("fs_ip");
                                                int sip_port = jsonObject1.getInt("fs_port");

                                                String path = "https://" + jsonObject.getString("fip") + ":" + jsonObject.getInt("fport");
                                                Intent intent = new Intent(context, LockListActivity.class);
                                                intent.putExtra("path_url", path);
                                                intent.putExtra("token", token);
                                                intent.putExtra("username", userName);
                                                intent.putExtra("neigbor_id", jsonObject.getString("neibor_id"));
                                                intent.putExtra("deal_time", mdealTime);

                                                intent.putExtra("sip_number" , sip_number);
                                                intent.putExtra("sip_password" , sip_password);
                                                intent.putExtra("sip_domin" , sip_domin);
                                                intent.putExtra("sip_port" , sip_port);
                                                intent.putExtra("flag" , flag);

                                                intent.addFlags(268435456);
                                                //context.startActivity(intent);

                                                SharedPreferencesUtil.saveData(context, "sayee_tow_url_key", path);
                                                SharedPreferencesUtil.saveData(context, "sayee_user_sip_domain_key", sip_domin);
                                                SharedPreferencesUtil.saveData(context, "sayee_user_name_key", userName);
                                                SharedPreferencesUtil.saveData(context, "sayee_user_token_key", token);
                                                SharedPreferencesUtil.saveData(context, "sayee_deal_time", Long.valueOf(mdealTime));
                                                SayeeManager.getInstance().turnOnCall(context);
//                                                (new Thread() {
//                                                    public void run() {
//                                                        while(!LinphoneService.isReady()) {
//                                                            try {
//                                                                Thread.sleep(30L);
//                                                            } catch (InterruptedException var3) {
//                                                                var3.printStackTrace();
//                                                            }
//                                                        }
//
//                                                        String sipDomin = sip_domin;
//                                                        int sipPort = sip_port;
//                                                        if(TextUtils.isEmpty(sipDomin)) {
//                                                            sipDomin = neiborBean.getFfs_ip();
//                                                        }
//
//                                                        if(sipPort == 0) {
//                                                            sipPort = neiborBean.getFfs_port();
//                                                        }
//
//                                                        HttpUtils.sipRegister(neiborBean, sip_number, sip_password, sip_domin, sip_port);
//                                                    }
//                                                }).start();
                                            }
                                        } catch (Exception var16) {
                                            var16.printStackTrace();
                                        }
                                    } else if(listener != null) {
                                        String myMsg1 = e.getString("msg");
                                        listener.onFail(code, myMsg1);
                                    }
                                } catch (JSONException var17) {
                                    var17.printStackTrace();
                                }
                            } else if(listener != null && msg.obj != null) {
                                listener.onFail(msg.what, ((BaseResult)msg.obj).getMsg());
                            }

                        }
                    });
                } catch (Exception var8) {
                    var8.printStackTrace();
                }

            }
        }
    }

    /**
     * 在首页登录sip账号
     *
     * @param context
     * @param headpParams
     * @param params
     * @param flag
     * @param listener
     */
    public static void goToOpenDoor3(final Context context, Map<String, String> headpParams, Map<String, String> params, final String flag , final HttpRespListener listener) {
        if(context == null) {
            Log.e( "www" ,"context cannot be null");
        } else if(headpParams == null) {
            Log.e( "www" , "headpParams cannot be null");
        } else if(params == null) {
            Log.e("www" , "params cannot be null");
        } else {
            final String token = (String)headpParams.get("token");
            final String userName = (String)headpParams.get("username");
            final String dealTime = (String)headpParams.get("dealtime");
            if(TextUtils.isEmpty(token)) {
                Log.e("www" , "token cannot be null");
            } else if(TextUtils.isEmpty(userName)) {
                Log.e("www" , "username cannot be null");
            } else if(TextUtils.isEmpty(dealTime)) {
                Log.e("www" , "dealtime cannot be null");
            } else {
                try {//https://api.sayee.cn:28084
                    HttpURLConnectionUtil.doGet("https://api.sayee.cn:28084/fir_platform/get_neibor_and_user_msg.json", headpParams, params, new Handler() {
                        public void dispatchMessage(Message msg) {
                            if(msg.what == 200) {
                                try {
                                    if(msg.obj == null) {
                                        return;
                                    }

                                    JSONObject e = new JSONObject(((BaseResult)msg.obj).getMsg());
                                    int code = e.getInt("code");

                                    Log.i("www" , "------code------------" + code);

                                    if(code == 0) {

                                        try {

                                            Log.i("www" , "--------0-----------" + e.toString());

                                            //NeiborResult myMsg = JsonParserUtil.parseJson2Object(NeiborResult.class, e);

                                            Log.i("www" , "--------1-----------");

                                            if(listener != null) {
                                                Log.i("www" , "--------2-----------");

                                                BaseResult baseResult = new BaseResult();


//                                                if(myMsg == null) {
//                                                    return;
//                                                }

                                                JSONObject jsonObject = e.getJSONObject("result");

                                                final NeiborBean neiborBean = new NeiborBean();

//                                                if(neiborBean == null) {
//                                                    return;
//                                                }

                                                long mdealTime = 0L;

                                                try {
                                                    mdealTime = Long.parseLong(dealTime);
                                                } catch (Exception var15) {
                                                    var15.printStackTrace();
                                                }

                                                Log.i("www" , "-------------------");
//                                                UserBean userBean = neiborBean.getUser_msg();
//                                                if(userBean == null) {
//                                                    return;
//                                                }

                                                JSONObject jsonObject1 = jsonObject.getJSONObject("user_msg");
//                                                final String sip_number = jsonObject1.getString("user_sip");
//                                                final String sip_password = userBean.getUser_password();
//                                                final String sip_domin = userBean.getFs_ip();
//                                                final int sip_port = userBean.getFs_port();

                                                String sip_number = jsonObject1.getString("user_sip");
                                                String sip_password = jsonObject1.getString("user_password");
                                                String sip_domin = jsonObject1.getString("fs_ip");
                                                int sip_port = jsonObject1.getInt("fs_port");

                                                String path = "https://" + jsonObject.getString("fip") + ":" + jsonObject.getInt("fport");
                                                Intent intent = new Intent(context, LockListActivity.class);
                                                intent.putExtra("path_url", path);
                                                intent.putExtra("token", token);
                                                intent.putExtra("username", userName);
                                                intent.putExtra("neigbor_id", jsonObject.getString("neibor_id"));
                                                intent.putExtra("deal_time", mdealTime);

                                                intent.putExtra("sip_number" , sip_number);
                                                intent.putExtra("sip_password" , sip_password);
                                                intent.putExtra("sip_domin" , sip_domin);
                                                intent.putExtra("sip_port" , sip_port);
                                                intent.putExtra("flag" , flag);

                                                //intent.addFlags(268435456);
                                                //context.startActivity(intent);

                                                LoginSipBean loginSipBean = new LoginSipBean();
                                                loginSipBean.setDeal_time(mdealTime);
                                                loginSipBean.setToken(token);
                                                loginSipBean.setNeigbor_id(jsonObject.getString("neibor_id"));
                                                loginSipBean.setPath_url(path);
                                                loginSipBean.setSip_domin(sip_domin);
                                                loginSipBean.setSip_number(sip_number);
                                                loginSipBean.setUserName(userName);
                                                loginSipBean.setSip_port(sip_port);
                                                loginSipBean.setSip_password(sip_password);


                                                SharedPreferencesUtil.saveData(context, "sayee_tow_url_key", path);
                                                SharedPreferencesUtil.saveData(context, "sayee_user_sip_domain_key", sip_domin);
                                                SharedPreferencesUtil.saveData(context, "sayee_user_name_key", userName);
                                                SharedPreferencesUtil.saveData(context, "sayee_user_token_key", token);
                                                SharedPreferencesUtil.saveData(context, "sayee_deal_time", Long.valueOf(mdealTime));
                                                SayeeManager.getInstance().turnOnCall(context);

                                                baseResult.setLoginSipBean(loginSipBean);
                                                listener.onSuccess(code, baseResult);

//                                                (new Thread() {
//                                                    public void run() {
//                                                        while(!LinphoneService.isReady()) {
//                                                            try {
//                                                                Thread.sleep(30L);
//                                                            } catch (InterruptedException var3) {
//                                                                var3.printStackTrace();
//                                                            }
//                                                        }
//
//                                                        String sipDomin = sip_domin;
//                                                        int sipPort = sip_port;
//                                                        if(TextUtils.isEmpty(sipDomin)) {
//                                                            sipDomin = neiborBean.getFfs_ip();
//                                                        }
//
//                                                        if(sipPort == 0) {
//                                                            sipPort = neiborBean.getFfs_port();
//                                                        }
//
//                                                        HttpUtils.sipRegister(neiborBean, sip_number, sip_password, sip_domin, sip_port);
//                                                    }
//                                                }).start();
                                            }
                                        } catch (Exception var16) {
                                            var16.printStackTrace();
                                        }
                                    } else if(listener != null) {
                                        String myMsg1 = e.getString("msg");
                                        listener.onFail(code, myMsg1);
                                    }
                                } catch (JSONException var17) {
                                    var17.printStackTrace();
                                }
                            } else if(listener != null && msg.obj != null) {
                                listener.onFail(msg.what, ((BaseResult)msg.obj).getMsg());
                            }

                        }
                    });
                } catch (Exception var8) {
                    var8.printStackTrace();
                }

            }
        }
    }



    public static void goToOpenDoor(final Context context, final String token, final long dealTime, final String userName, String neibor_flag, final HttpRespListener listener) {
        if(context == null) {
            Log.e( "www" , "context cannot be null");
        } else {
            HashMap headpParams = new HashMap();
            headpParams.put("token", token);
            headpParams.put("username", userName);
            HashMap params = new HashMap();
            params.put("username", userName);
            params.put("neibor_flag", neibor_flag);

            try {
                HttpURLConnectionUtil.doGet("https://api.sayee.cn:28084/fir_platform/get_neibor_and_user_msg.json", headpParams, params, new Handler() {
                    public void dispatchMessage(Message msg) {
                        if(msg.what == 200) {
                            try {
                                if(msg.obj == null) {
                                    return;
                                }

                                JSONObject e = new JSONObject(((BaseResult)msg.obj).getMsg());
                                int code = e.getInt("code");
                                if(code == 0) {
                                    try {
                                        NeiborResult myMsg = (NeiborResult)JsonParserUtil.parseJson2Object(NeiborResult.class, e);
                                        if(listener != null) {
                                            listener.onSuccess(code, myMsg);
                                            if(myMsg == null) {
                                                return;
                                            }

                                            final NeiborBean neiborBean = myMsg.getResult();
                                            if(neiborBean == null) {
                                                return;
                                            }

                                            String path = "https://" + neiborBean.getFip() + ":" + neiborBean.getFport();
                                            Intent intent = new Intent(context, LockListActivity.class);
                                            intent.putExtra("path_url", path);
                                            intent.putExtra("token", token);
                                            intent.putExtra("username", userName);
                                            intent.putExtra("neigbor_id", neiborBean.getNeibor_id());
                                            intent.putExtra("deal_time", dealTime);
                                            intent.addFlags(268435456);
                                            context.startActivity(intent);
                                            UserBean userBean = neiborBean.getUser_msg();
                                            if(userBean == null) {
                                                return;
                                            }

                                            final String sip_number = userBean.getUser_sip();
                                            final String sip_password = userBean.getUser_password();
                                            final String sip_domin = neiborBean.getFfs_ip();
                                            final int sip_port = neiborBean.getFfs_port();
                                            SharedPreferencesUtil.saveData(context, "sayee_path_url", path);
                                            SharedPreferencesUtil.saveData(context, "sayee_user_sip_domain_key", sip_domin);
                                            SharedPreferencesUtil.saveData(context, "sayee_user_name_key", userName);
                                            SharedPreferencesUtil.saveData(context, "sayee_user_token_key", token);
                                            SharedPreferencesUtil.saveData(context, "sayee_deal_time", Long.valueOf(dealTime));
                                            SayeeManager.getInstance().turnOnCall(context);
                                            (new Thread() {
                                                public void run() {
//                                                    while(!LinphoneService.isReady()) {
//                                                        try {
//                                                            Thread.sleep(30L);
//                                                        } catch (InterruptedException var3) {
//                                                            var3.printStackTrace();
//                                                        }
//                                                    }

                                                    String sipDomin = sip_domin;
                                                    int sipPort = sip_port;
                                                    if(TextUtils.isEmpty(sipDomin)) {
                                                        sipDomin = neiborBean.getFfs_ip();
                                                    }

                                                    if(sipPort == 0) {
                                                        sipPort = neiborBean.getFfs_port();
                                                    }

                                                    HttpUtils.sipRegister(neiborBean, sip_number, sip_password, sip_domin, sip_port);
                                                }
                                            }).start();
                                        }
                                    } catch (Exception var13) {
                                        var13.printStackTrace();
                                        if(listener != null) {
                                            listener.onFail(code, "数据解析错误");
                                        }
                                    }
                                } else if(listener != null) {
                                    String myMsg1 = e.getString("msg");
                                    listener.onFail(code, myMsg1);
                                }
                            } catch (JSONException var14) {
                                var14.printStackTrace();
                            }
                        } else if(listener != null && msg.obj != null) {
                            listener.onFail(msg.what, ((BaseResult)msg.obj).getMsg());
                        }

                    }
                });
            } catch (Exception var10) {
                var10.printStackTrace();
            }

        }
    }

    public static void getLockList(final String path, String token, String userName, String neigbor_id, final HttpRespListener listener) {
        HashMap headParams = new HashMap();
        headParams.put("username", userName);
        headParams.put("token", token);
        HashMap params = new HashMap();
        params.put("username", userName);
        params.put("neigbor_id", neigbor_id);

        try {
            HttpURLConnectionUtil.doGet(path + "/config/my_lock_list.json", headParams, params, new Handler() {
                public void dispatchMessage(Message msg) {
                    if(msg.what == 200) {
                        try {

                            LogUtil.i("my_lock_list" + msg.obj.toString());

                            JSONObject e = new JSONObject(((BaseResult)msg.obj).getMsg());
                            int code = e.getInt("code");
                            if(code == 0 && listener != null) {
                                JSONObject resultJson = e.getJSONObject("result");
                                //LockListResult result = (LockListResult)JsonParserUtil.parseJson2Object(LockListResult.class, resultJson);

                                LockListResult result= new LockListResult();

                                JSONObject jsonObject = e.getJSONObject("result");
                                JSONArray jsonArray = jsonObject.getJSONArray("lockList");

                                List<LockBean> list = new ArrayList<LockBean>();
                                for (int i = 0 ; i <jsonArray.length() ; i++){
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    LockBean lockBean = new LockBean();
                                    lockBean.setDomain_sn(jsonObject1.getString("domain_sn"));
                                    lockBean.setLock_name(jsonObject1.getString("lock_name"));
                                    lockBean.setSip_number(jsonObject1.getString("sip_number"));
                                    lockBean.setLock_parent_name(jsonObject1.getString("lock_parent_name"));
                                    lockBean.setLayer(jsonObject1.getString("layer"));
                                    list.add(lockBean);
                                }

                                List<String> parentList = new ArrayList<String>();
                                for(int i = 0 ; i < list.size() ; i++){
                                    String lock_parent_name = list.get(i).getLock_parent_name();
                                    int one = lock_parent_name.indexOf("-") + 1;
                                    int two = lock_parent_name.lastIndexOf("-");
                                    String parent = lock_parent_name.substring(one , two);
                                    //LogUtil.i("-------------parent-----------------" + parent);
                                    parentList.add(parent);
                                }


                                /**
                                 * 去重
                                 *
                                 */
                                List<String> parentList2 = new ArrayList<String>();
                                for(String i:parentList){
                                    if(!parentList2.contains(i)){
                                        parentList2.add(i);
                                    }
                                }

                                List<ComBean> comList = new ArrayList<ComBean>();
                                for (int i = 0 ;  i < parentList2.size() ; i++){
                                    ComBean comBean = new ComBean();
                                    comBean.setComTitle(parentList2.get(i));
                                    String parent = parentList2.get(i);
                                    List<LockBean> lockList = new ArrayList<LockBean>();
                                    for (int j  = 0 ; j < list.size() ; j++){
                                        String lock_parent_name = list.get(j).getLock_parent_name();
                                        if (lock_parent_name.contains(parent)){
                                            lockList.add(list.get(j));
                                        }
                                    }
                                    comBean.setList(lockList);
                                    comList.add(comBean);
                                }


                                //  这是第一种数据格式
                                List<LockBean> list2 = new ArrayList<LockBean>();
                                for (int i = 0 ; i <parentList2.size() ; i++){
                                    String parent = parentList2.get(i);
                                    for (int j  = 0 ; j < list.size() ; j++){
                                        String lock_parent_name = list.get(j).getLock_parent_name();
                                        if (lock_parent_name.contains(parent)){
                                            list.get(j).setSection(i);
                                            LogUtil.i("----" + list.get(j).toString());
                                            list2.add(list.get(j));
                                        }
                                    }
                                }
                                LogUtil.i("----------list2---------------" + list2.size());
                                //LogUtil.i("----------comList---------------" + comList.size());

                                List<LockBean> list3 = new ArrayList<LockBean>();
                                for (int i = 0 ; i <parentList2.size() ; i++){
                                    String parent = parentList2.get(i);
                                    LockBean lockBean = new LockBean();
                                    lockBean.setType(1);
                                    lockBean.setLock_parent_name(parent);
                                    list3.add(lockBean);
                                    for (int j  = 0 ; j < list.size() ; j++){
                                        String lock_parent_name = list.get(j).getLock_parent_name();
                                        if (lock_parent_name.contains(parent)){
                                            //list.get(j).setSection(i);
                                            list.get(j).setType(2);
                                            LogUtil.i("----" + list.get(j).toString());
                                            list3.add(list.get(j));
                                        }
                                    }
                                }
                                LogUtil.i("----------list3---------------" + list3.size());

                                result.setLockList(list);
                                result.setLockList2(list2);
                                result.setLockList3(list3);
                                result.setComList(comList);
                                result.setMsg(e.getString("msg"));
                                listener.onSuccess(code, result);
                            }
                        } catch (Exception var6) {
                            var6.printStackTrace();
                        }
                    } else if(listener != null) {
                        listener.onFail(msg.what, ((BaseResult)msg.obj).getMsg());
                    }
                }
            });
        } catch (Exception var8) {
            var8.printStackTrace();
        }

    }

    public static void openDoorLock(Context context,
                                    String path,
                                    String token,
                                    String userName,
                                    String domain_sn,
                                    int type,
                                    String toSipUri,
                                    String toSipDomin,
                                    final HttpRespListener listener) {

        if(TextUtils.isEmpty(toSipDomin)) {
            toSipDomin = SharedPreferencesUtil.getUserSipDomin(context);
        }

        HashMap headParams = new HashMap();
        headParams.put("username", userName);
        headParams.put("token", token);
        long time = System.currentTimeMillis();
        HashMap params = new HashMap();
        params.put("username", userName);
        params.put("domain_sn", domain_sn);
        params.put("time", String.valueOf(time));
        params.put("type", String.valueOf(type));

        Log.i("www" , "开锁username - " + token);
        Log.i("www" , "username - " + userName);
        Log.i("www" , "domain_sn - " + domain_sn);
        Log.i("www" , "time - " + time);
        Log.i("www" , "type - " + type);
        Log.i("www" , "开锁path - " + path);

        String sipUri = "sip:" + toSipUri + "@" + toSipDomin;
        String mess = "{\"ver\":\"1.0\",\"typ\":\"req\",\"cmd\":\"0610\",\"tgt\":\"" + domain_sn + "\",\"cnt\":{\"username\":\"" + userName + "\",\"type\":\"" + type + "\",\"time\":\"" + time + "\"}}";
        if(!TextUtils.isEmpty(toSipDomin)) {
            sendTextMessage(sipUri, mess);
        }

        if(!TextUtils.isEmpty(token) && !TextUtils.isEmpty(userName) && !TextUtils.isEmpty(domain_sn)) {
            try {
                HttpURLConnectionUtil.doPost(context, path + "/device/remote_unlock.json", headParams, params, new Handler() {
                    public void dispatchMessage(Message msg) {
                        if(msg.what == 200) {
                            try {
                                JSONObject e = new JSONObject(((BaseResult)msg.obj).getMsg());

                                Log.i("www" , "开锁" + msg.obj.toString());

                                int code = e.getInt("code");
                                if(code == 0) {
                                    if(listener != null) {
                                        BaseResult result = new BaseResult();
                                        result.setCode(code);
                                        result.setMsg(e.getString("msg"));
                                        listener.onSuccess(code, result);
                                    }
                                } else if(listener != null) {
                                    listener.onFail(code, e.getString("msg"));
                                }
                            } catch (Exception var5) {
                                var5.printStackTrace();
                            }
                        } else if(listener != null) {
                            listener.onFail(msg.what, ((BaseResult)msg.obj).getMsg());
                        }
                    }
                }, true);
            } catch (Exception var16) {
                var16.printStackTrace();
            }
        }
    }

    public static void getOpenLockPassword(String path, String token, String userName, String sip_number, final HttpRespListener listener) {
        HashMap headParams = new HashMap();
        headParams.put("username", userName);
        headParams.put("token", token);
        HashMap params = new HashMap();
        params.put("username", userName);
        params.put("sip_number", sip_number);

        LogUtil.i("username" + username);
        LogUtil.i("token" + token);
        LogUtil.i("sip_number" + sip_number);
        LogUtil.i("path" + path);

        try {
            HttpURLConnectionUtil.doGet(path + "/users/random_password.json", headParams, params, new Handler() {
                public void dispatchMessage(Message msg) {
                    if(msg.what == 200) {
                        try {
                            String e = ((BaseResult)msg.obj).getMsg();

                            Log.i("www" , "密码开锁的" + e.toString());

                            JSONObject object = new JSONObject(e);
                            int code = object.getInt("code");
                            Log.i("www" , "code" + code);

                            BaseResult result;
                            OpenLockPasswordBean bean = new OpenLockPasswordBean();
                            bean.setRandom_pw(object.getJSONObject("result").getString("random_pw"));
                            bean.setRandomkey_dead_time(object.getJSONObject("result").getLong("randomkey_dead_time"));

                            OpenLockPasswordResult  passwordResult = new OpenLockPasswordResult();
                            passwordResult.setCode(object.getInt("code"));
                            passwordResult.setMsg(object.getString("msg"));
                            passwordResult.setResult(bean);
                            if(code == 0) {
                                if(listener != null){
                                    //result = (BaseResult)JsonParserUtil.parseJson2Object(OpenLockPasswordResult.class, e);
                                    result = passwordResult;
                                    result.setCode(code);
                                    listener.onSuccess(code, result);
                                }
                            } else if(listener != null) {
                                result = (BaseResult)JsonParserUtil.parseJson2Object(OpenLockPasswordResult.class, e);
                                listener.onFail(code, result.getMsg());
                            }
                        } catch (Exception var6) {
                            var6.printStackTrace();
                        }
                    } else if(listener != null) {
                        listener.onFail(msg.what, ((BaseResult)msg.obj).getMsg());
                    }
                }
            });
        } catch (Exception var8) {
            var8.printStackTrace();
        }
    }

    private static void sendTextMessage(String sipUri, String content) {
        LinphoneCore lc = LinphoneManager.getLcIfManagerNotDestroyedOrNull();
        LinphoneChatRoom chatRoom = null;
        if(lc != null) {
            chatRoom = lc.getOrCreateChatRoom(sipUri);
        }

        boolean isNetworkReachable = lc == null?false:lc.isNetworkReachable();
        if(chatRoom != null && content != null && content.length() > 0 && isNetworkReachable) {
            LinphoneChatMessage localLinphoneChatMessage = chatRoom.createLinphoneChatMessage(content);
            chatRoom.sendMessage(localLinphoneChatMessage, new LinphoneChatMessage.StateListener() {
                public void onLinphoneChatMessageStateChanged(LinphoneChatMessage chatMessage, LinphoneChatMessage.State status) {

                }
            });
            Log.i("www" ,"Sent message current status: " + localLinphoneChatMessage.getStatus());
        } else if(!isNetworkReachable) {
            Log.e("www" , "没有网络，请稍后再试");
        }

    }

    private static void sipRegister(NeiborBean neiborBean, String sip_number, String sip_password, String sip_domin, int sip_port) {
        LinphoneCore linphoneCore = LinphoneManager.getLcIfManagerNotDestroyedOrNull();
        if(linphoneCore != null) {
            LinphoneAuthInfo[] authInfosList = linphoneCore.getAuthInfosList();
            if(linphoneCore != null && authInfosList != null && authInfosList.length > 0) {
                linphoneCore.clearProxyConfigs();
                linphoneCore.clearAuthInfos();
            }

            Log.i("www" , "-----创建----");
            LinphonePreferences.AccountBuilder builder = new LinphonePreferences.AccountBuilder(linphoneCore);
            builder.setUsername(sip_number);
            builder.setPassword(sip_password);
            if("tcp".equalsIgnoreCase(neiborBean.getTransport())) {
                builder.setTransport(LinphoneAddress.TransportType.LinphoneTransportTcp);
            } else if("udp".equalsIgnoreCase(neiborBean.getTransport())) {
                builder.setTransport(LinphoneAddress.TransportType.LinphoneTransportUdp);
            } else if("tls".equalsIgnoreCase(neiborBean.getTransport())) {
                builder.setTransport(LinphoneAddress.TransportType.LinphoneTransportTls);
            } else {
                builder.setTransport(LinphoneAddress.TransportType.LinphoneTransportTcp);
            }

            builder.setDomain(sip_domin);
            builder.setProxy(sip_domin + ":" + sip_port);

            try {
                builder.saveNewAccount();
            } catch (LinphoneCoreException var9) {
                var9.printStackTrace();
            }
        }

    }
}

