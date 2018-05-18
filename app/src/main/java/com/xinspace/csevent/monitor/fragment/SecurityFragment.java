package com.xinspace.csevent.monitor.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
//import com.intelligoo.sdk.LibDevModel;
//import com.intelligoo.sdk.LibInterface;
//import com.intelligoo.sdk.ScanCallback;
import com.xinspace.csevent.monitor.bean.A;
import com.xinspace.csevent.monitor.bean.Data;
import com.xinspace.csevent.monitor.bean.DeviceBean;
import com.xinspace.csevent.monitor.utils.Request;
import com.xinspace.csevent.monitor.weiget.ConnectDialog;
import com.xinspace.csevent.monitor.weiget.VideoHintDialog;
import com.xinspace.csevent.R;
import com.xinspace.csevent.app.utils.ScreenUtils;
import com.xinspace.csevent.util.ImagerLoaderUtil;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Android on 2016/12/6.
 * <p/>
 * 安保门禁
 */
public class SecurityFragment extends Fragment {

    private View view;
    private ImageView iv_load_open_door;
    private ImageView iv_open_door;
    private AnimationDrawable animationDrawable;

    private String username, password;

    private static String client_id = null;

    private static final int LOGIN_SUCCESS = 0x00;

    private static final int LOGIN_FAILED = 0x01;

    private static final int CLIENT_ID_NULL = 0x02;

    private static final int DATA_NULL = 0x03;

    private static final int LIST_REFRESH = 0x04;

    private static ArrayList<DeviceBean> devList = new ArrayList<DeviceBean>();

    private boolean pressed = false;

   // private LibDevModel libDevModel;

    private ConnectDialog dialog;

    private Timer timer;

    private RelativeLayout rel_blue_content , rel_switch_open;

    private TextView tv_content;

    private boolean isBule = true; //是否为蓝牙开门

    private String dev_sn;

    private SoundPool soundPool;

    private ImageView iv_open_video;

    private int screenWidth;

    private String callAccount;

    private RelativeLayout rel_video;

    private VideoHintDialog videoHintDialog;

    public static Handler mhandler;

    private TimerTask task = new TimerTask() {
        public void run() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    iv_open_door.setImageResource(R.drawable.icon_close_door);
                    iv_load_open_door.setImageResource(R.drawable.animation_load_open);
                    animationDrawable = (AnimationDrawable) iv_load_open_door.getDrawable();
                    animationDrawable.start();
                    refreshScanList();
                }
            });
        }
    };

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOGIN_SUCCESS:
                    LogUtil.i("----------------------登录成功---------------------");
                    //Toast.makeText(getActivity(), "登录成功", Toast.LENGTH_SHORT).show();
//                    adapter.refreshList(devList);
//                    mDrawerLayout.closeDrawers();
                    if(isBule){
                        refreshScanList();
                    }else{

                    }
                    break;
                case CLIENT_ID_NULL:
                    //Toast.makeText(getActivity(), "client id 为空", Toast.LENGTH_SHORT).show();
                    showDialog("重新连接");
                    break;
                case LOGIN_FAILED:
                    int ret = (Integer) msg.obj;
                    showDialog("重新连接");
                    //Toast.makeText(getActivity(), "登录失败" + ret, Toast.LENGTH_SHORT).show();
                    break;
                case LIST_REFRESH:

                    break;
                case 11:
                    ToastUtil.makeToast("未搜索到需要开门门禁");
                    break;
                case 100 :
                    ToastUtil.makeToast("开门成功");
                    playMedia();
                    break;
                case 101:
                    ToastUtil.makeToast("开门失败");
                    break;
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_security, null);

        initView();

       // DMVPhoneModel.initDMVoipSDK(getActivity());

        return view;
    }


    private void initView() {
        screenWidth = ScreenUtils.getScreenWidth(getActivity());

        SharedPreferences sharedPre = getActivity().getSharedPreferences("PREFS_CONF",
                Context.MODE_PRIVATE);

        rel_video = (RelativeLayout) view.findViewById(R.id.rel_video);

        tv_content = (TextView) view.findViewById(R.id.tv_content);
        rel_blue_content = (RelativeLayout) view.findViewById(R.id.rel_blue_content);
        rel_switch_open = (RelativeLayout) view.findViewById(R.id.rel_switch_open);
        rel_switch_open.setOnClickListener(clickListener);

        iv_open_video = (ImageView) view.findViewById(R.id.iv_open_video);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv_open_video.getLayoutParams();
        params.width = (int) (screenWidth * 1.0f);
        params.height = (int) (screenWidth * 0.4f);
        iv_open_video.setLayoutParams(params);
        iv_open_video.setScaleType(ImageView.ScaleType.FIT_XY);
        ImagerLoaderUtil.displayImage("drawable://" + R.drawable.icon_video_bg, iv_open_video);

        iv_open_video.setOnClickListener(clickListener);

        if(isBule){
            tv_content.setText("蓝牙开门");
            rel_blue_content.setVisibility(View.VISIBLE);
            rel_video.setVisibility(View.GONE);
        }else{
            tv_content.setText("远程开门");
            rel_blue_content.setVisibility(View.INVISIBLE);
            rel_video.setVisibility(View.VISIBLE);
            iv_open_door.setVisibility(View.GONE);
        }

        iv_load_open_door = (ImageView) view.findViewById(R.id.iv_load_open_door);
        iv_load_open_door.setImageResource(R.drawable.animation_load_open);
        animationDrawable = (AnimationDrawable) iv_load_open_door.getDrawable();
        animationDrawable.start();

        iv_open_door = (ImageView) view.findViewById(R.id.iv_open_door);
        iv_open_door.setOnClickListener(clickListener);

        Thread login_th = new Thread(new Runnable() {
            public void run() {
                login();
            }
        });
        login_th.start();

        mhandler = new Handler();

        login2();
        mhandler.postDelayed(new Runnable() {
            public void run() {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null && getActivity().getCurrentFocus() != null) {
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                }
            }
        }, 100);

//        timer = new Timer();
//        timer.schedule(task , 5000 , 10000);

        /***********************************操作设备***********************************/
//        final LibInterface.ManagerCallback callBack = new LibInterface.ManagerCallback() {
//            @Override
//            public void setResult(final int result, Bundle bundle) {
//                getActivity().runOnUiThread(new Runnable() {
//                    public void run() {
//                        pressed = false; //二次点击处理
//                        LogUtil.i("www-------------" + result);
//                        if (result == 0x00) {
//                            Toast.makeText(getActivity(), "开门成功", Toast.LENGTH_SHORT).show();
//                            playMedia();
//                        } else {
//                            Toast.makeText(getActivity(), "开门失败" + result, Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//            }
//        };


        iv_open_door.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isBule){
                    if (!pressed) {
                        //Toast.makeText(getActivity(), "正在操作中", Toast.LENGTH_SHORT).show();
                    }
                    try {
                        /*************************OPEN OPERATION*******************************/
                        //int ret = LibDevModel.controlDevice(getActivity(), 0x00, libDevModel, null, callBack);
//                        LogUtil.i("www   ---" + ret);
//                        if (ret == 0x00) {
//                            pressed = true;
//                        } else {
//                            //Toast.makeText(getActivity(), "open" + ret, Toast.LENGTH_SHORT).show();
//                        }
                    } catch (NumberFormatException e) {
                        Log.e("111111111111", e.getMessage());
                    }
                }else{
                    longRangeOpen();
                }
            }
        });
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rel_switch_open:
                    if(isBule){
                        isBule = false;
                        tv_content.setText("远程开门");
                        iv_open_door.setImageResource(R.drawable.icon_open_door);
                        rel_blue_content.setVisibility(View.INVISIBLE);
                        rel_video.setVisibility(View.VISIBLE);
                        iv_open_door.setVisibility(View.GONE);
                    }else{
                        isBule = true;
                        tv_content.setText("蓝牙开门");
                        rel_blue_content.setVisibility(View.VISIBLE);
                        rel_video.setVisibility(View.GONE);
                        iv_open_door.setVisibility(View.VISIBLE);
                        iv_open_door.setImageResource(R.drawable.icon_close_door);
                        animationDrawable.start();
                        refreshScanList();
                    }
                    break;
                case R.id.iv_open_video:

                    showHintDialog();

                    break;
            }
        }
    };

    private void longRangeOpen(){

        //String dev_sn = devList.get(0).getDevSn();

        LogUtil.i("远程开开门www" + dev_sn);
        if (dev_sn != null){

        }

        Data data = new Data();
        data.setDev_sn(dev_sn);
        data.setEnable_keepopen_ts(0);
        final String dataString = JSON.toJSONString(data, true).toString();

        A a = new A();
        a.setAccess_token("b43d991e1ee06fbcf0dc1ed8b75e560d29a8530L59904e631568c6bd");
        a.setOperation("OPEN_DOOR");
        a.setData(data);

        final String json = JSON.toJSONString(a, true);

        LogUtil.i("远程开门请求json"+ json);

        Thread open_th = new Thread(new Runnable() {
            public void run() {

                openDoor(dataString);

            }
        });
        open_th.start();


//        GetDataBiz.longOpenDoor(dataString, new HttpRequestListener() {
//            @Override
//            public void onHttpRequestFinish(String result) throws JSONException {
//                LogUtil.i("远程开门返回值"+ result);
//            }
//
//            @Override
//            public void onHttpRequestError(String error) {
//                LogUtil.i("--------------请求失败-------------" + error);
//            }
//        });

    }


    private void openDoor(String dataString){

//        JSONObject resultJson = DemoHttps.connectGet(AppConfig.longOpenDoorAPI ,  dataString);
//        LogUtil.i("远程开门请求返回json"+ resultJson.toString());

        try {
            JSONObject resultJson = Request.longOpenDoor(dataString);
            if (resultJson != null){
                LogUtil.i("远程开门请求返回json"+ resultJson.toString());
                if (resultJson.getString("msg").equals("ok")){
                    if (resultJson.getString("ret").equals("0")){
                        mHandler.obtainMessage(100).sendToTarget();
                    }else{
                        mHandler.obtainMessage(101).sendToTarget();
                    }
                }else{
                    mHandler.obtainMessage(101).sendToTarget();
                }
            }else{
                mHandler.obtainMessage(101).sendToTarget();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    // 登录
    private void login() {
        username = "13560115020";
        password = "cui602602";
        try {
            JSONObject login_ret = Request.login(username, password);
            if (login_ret == null || login_ret.isNull("ret")) {
                LogUtil.i("doormaster" + "login_ret error");
                return;
            }
            LogUtil.i("doomasterlogin"  + login_ret.toString());
            int ret = login_ret.getInt("ret");
            Message msg = new Message();
            if (ret == 0) {
                if (login_ret.isNull("data")) {
                    mHandler.sendEmptyMessage(DATA_NULL);
                    return;
                }
                JSONObject data = login_ret.getJSONObject("data");
                JSONObject jsonObject = data.getJSONObject("community_info");
                JSONArray jsonArray = jsonObject.getJSONArray("dev_list");
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                dev_sn = jsonObject1.getString("dev_sn");
                LogUtil.i("dev_sn"  + dev_sn);

                if (!data.isNull("client_id")) {
                    client_id = data.getString("client_id");
                    devList = Request.reqDeviceList(client_id);
                    if (devList == null) {
                        LogUtil.i("doormaster" + "devList is null");
                        devList = new ArrayList<DeviceBean>();
                    }
                    mHandler.sendEmptyMessage(LOGIN_SUCCESS);
                } else {
                    msg.what = CLIENT_ID_NULL;
                    mHandler.sendMessage(msg);
                }
            } else {
                msg.what = LOGIN_FAILED;
                msg.obj = ret;
                mHandler.sendMessage(msg);
            }
        } catch (JSONException e) {
            Log.e("doormaster ", e.getMessage());
        }
    }

    private void login2() {
//        account = et_account.getText().toString().trim();
//        password = et_password.getText().toString().trim();

        //String account = "4488".toString().trim();
        String account = "13560115020";
        String passwordVideo = "13560115020";

        if (TextUtils.isEmpty(account)) {
            Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(passwordVideo)) {
            Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
            return;
        }

        String token = "6ebb6cc8d7d5d20e53da9a18a337dfdf4669fc0b";
        //参数：呼叫号码、sdk-token、呼叫类型、回调callBack
        LogUtil.i("wwww"+"account="+account+",token="+token);

//        DMVPhoneModel.loginVoipServer(account, token , 1, new DMModelCallBack.DMCallback() {
//            @Override
//            public void setResult(int errorCode, DMException e) {
//                if (e == null) {
//                   // Toast.makeText(getActivity(),"登录成功",Toast.LENGTH_SHORT).show();
//                } else {
//
//                    LogUtil.i("www" + errorCode + "errorCode+"+e.toString());
////
////                    Toast.makeText(getActivity(),"登录失败，errorCode="+errorCode+",e="+e.toString()
////                            ,Toast.LENGTH_SHORT).show();
//                }
//            }
//        });


//        DMVoipModel.loginVoipServer(account, passwordVideo, new DMModelCallBack.DMCallback() {
//            @Override
//            public void setResult(int i, Bundle bundle) {
//                if (i == 0) {
//                    //Toast.makeText(getActivity(),"登录失败",Toast.LENGTH_SHORT).show();
//                } else {
//
//                    LogUtil.i("视频登录成功了");
//
//                    //Toast.makeText(getActivity(),"登录成功",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }


    /***********************************
     * 扫描设备
     **************************************/
    // 扫描到设备，刷新列表
    private void refreshScanList() {
//        ScanCallback callback = new ScanCallback() {
//
//            @Override
//            public void onScanResult(ArrayList<String> deviceList,
//                                     ArrayList<Integer> rssi) {
//                LogUtil.i("" + deviceList.toString() + "" + rssi.toString());
//            }
//
//            @Override
//            public void onScanResultAtOnce(final String devSn, int rssi) {
//                LogUtil.i(devSn + " :[" + rssi + "]");
//                getActivity().runOnUiThread(new Runnable() {
//                    public void run() {
//                        if (devList == null || devList.isEmpty()) {
//                            return;
//                        }
//                        if (dev_sn == null){
//                            mHandler.obtainMessage(11).sendToTarget();
//                        }
//                        for (DeviceBean device : devList) {
//                            if (device.getDevSn().equalsIgnoreCase(devSn)) {
//                                device.setDevSn(devSn);
//                                iv_open_door.setImageResource(R.drawable.icon_open_door);
//                                iv_load_open_door.setImageResource(R.drawable.icon_open_four);
//                                //adapter.sortList(device);
//                                libDevModel = getLibDev(device);
//                                pressed = true;
//                            }
//                        }
//                    }
//                });
//            }
//        };
//        int ret = LibDevModel.scanDevice(getActivity(), true, 2, callback);
//        LogUtil.i("ret" + "ret" + ret);
//        if (ret != 0x00) {
//            //Toast.makeText(getActivity(), "" + ret, Toast.LENGTH_SHORT).show();
//            showDialog("未打开蓝牙");
//        }else if (ret == 0){
//            showDialog("重新连接");
//        }
    }

    /***********************************
     * 扫描设备
     **************************************/

//    public static LibDevModel getLibDev(DeviceBean dev) {
//        LibDevModel device = new LibDevModel();
//        device.devSn = dev.getDevSn();
//        device.devMac = dev.getDevMac();
//        device.devType = dev.getDevType();
//        device.eKey = dev.geteKey();
//        //device.encrytion = dev.getEncrytion();
//        device.endDate = dev.getEndDate();
//        device.openType = dev.getOpenType();
//        device.privilege = dev.getPrivilege();
//        device.startDate = dev.getStartDate();
//        device.useCount = dev.getUseCount();
//        device.verified = dev.getVerified();
//        return device;
//    }

    private void showDialog(String res) {
        dialog = new ConnectDialog(getActivity());
        dialog.setCanceledOnTouchOutside(false);
        TextView textView1 = (TextView) dialog.getTextView1();
        ImageView imageView = (ImageView) dialog.getImageView();
        textView1.setText(res);
        imageView.setImageResource(R.drawable.icon_reconnect);
        //确定
        dialog.setOnPositiveListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                Thread login_th = new Thread(new Runnable() {
//                    public void run() {
//                        login();
//                    }
//                });
//                login_th.start();
                refreshScanList();
                dialog.dismiss();
            }
        });

        dialog.setCloseDialog(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }




    private void showHintDialog() {
        videoHintDialog = new VideoHintDialog(getActivity());
        videoHintDialog.setCanceledOnTouchOutside(false);
        //确定
        videoHintDialog.setOnPositiveListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                call();
                SharedPreferences settings = getActivity().getSharedPreferences("PREFS_CONF",
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor edtor=settings.edit();
                edtor.putString("callAccount", callAccount);
                edtor.apply();
                videoHintDialog.dismiss();
            }
        });

        videoHintDialog.setOnNegativeListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoHintDialog.dismiss();
            }
        });

        videoHintDialog.show();
    }


    private void playMedia(){
        soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
        soundPool.load(getActivity(), R.raw.open_door_sound, 1);
        soundPool.play(1, 1, 1, 0, 0, 1);
    }

    public void call(){
        callAccount = "442c056a1d28";
        if (TextUtils.isEmpty(callAccount)) {
            Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
            return;
        }
        //DMVPhoneModel.callAccount(callAccount);

        //DMVPhoneModel.callAccount(callAccount , 2);
    }

}
