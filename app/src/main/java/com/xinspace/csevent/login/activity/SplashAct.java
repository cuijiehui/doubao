package com.xinspace.csevent.login.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.app.utils.TimeHelper;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.biz.FullPageAdsBiz;
import com.xinspace.csevent.data.entity.FullPageAdsEntity;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.parser.JsonPaser;
import com.xinspace.csevent.util.Const;
import com.xinspace.csevent.util.ImagerLoaderUtil;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.NetWorkStateUtil;
import com.xinspace.csevent.util.StatusBarUtils;
import com.xinspace.csevent.util.ToastUtil;
import com.xinspace.csevent.ui.activity.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Android on 2016/9/19.
 *
 * app 启动页
 */
public class SplashAct extends Activity{

    private ImageView imgSplash;
    private SDPreference preference;
    private List<Object> adv_list;//广告集合
    private int count=3;//距离进入首页的秒数,默认5秒
    private int advIndex=0;//显示广告的下标
    private TextView tvTime;
    private Button btPass;
    private RelativeLayout rlCountdown;
    private String url;//跳转的网页链接
    private static final int location = 10;
    private long time;
    private String szImei;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StatusBarUtils.setWindowStatusBarColor(SplashAct.this , R.color.app_bottom_color);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        preference = SDPreference.getInstance();
        setContentView(R.layout.act_splash);
        initView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkAllPermission();
        }else{
            //szImei = ((TelephonyManager) this.getSystemService(TELEPHONY_SERVICE)).getDeviceId();
        }

        if (szImei != null){
            preference.putContent("szImei" , szImei);
        }
        time = System.currentTimeMillis();
        preference.putContent("appStart" , time);
        String imei = preference.getContent("szImei");

        LogUtil.i("imei" + imei);

        if (imei != null){
            String currentTime = TimeHelper.getDateString(String.valueOf(System.currentTimeMillis())) ;
            run();
        }
    }


    private void initView() {
        imgSplash = (ImageView) findViewById(R.id.img_splash);
        imgSplash.setImageResource(R.drawable.splash_bg);

        //MyAnimationDrawable.animateRawManuallyFromXML(R.drawable.animation_start_app,imgSplash,null,null);

        tvTime=(TextView)findViewById(R.id.tv_fullpage_txt2);
        rlCountdown=(RelativeLayout)findViewById(R.id.rl_fullpageAdv_countdown);
        btPass = (Button) findViewById(R.id.bt_fullpageads_pass);
        btPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashAct.this , MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void checkAllPermission() {
        //百度地图定位权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //ACCESS_COARSE_LOCATION
            ActivityCompat.requestPermissions(SplashAct.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                    }, SplashAct.location);
        }
    }

    //获取定位权限的返回值
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        LogUtil.i("权限的返回值" + requestCode);
        switch (requestCode) {
            case location:
                szImei = ((TelephonyManager) this.getSystemService(TELEPHONY_SERVICE)).getDeviceId();
                LogUtil.i("wwwwwwwww" + szImei);
                if (szImei != null){
                    preference.putContent("szImei" , szImei);
                }
                time = System.currentTimeMillis();
                preference.putContent("appStart" , time);
                run();
                break;
        }
    }

    private void run() {
        // TODO Auto-generated method stub
        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessageDelayed(1, 1500);
            }
        }).start();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent;
            String appVersion = getVersion();
            switch (msg.what){
                case 1:
//                if (preference.getContent(appVersion).equals("0")) {
//                    preference.putContent(appVersion, "ok");
//                    intent = new Intent(SplashAct.this, GuideAct.class);
//                    startActivity(intent);
//                    finish();
//                } else {
//
//                }
                   // checkNetwork();

                    Intent intent1 = new Intent(SplashAct.this , MainActivity.class); //直接跳转到首页
                    startActivity(intent1);
                    SplashAct.this.finish();

                break;
            }
        }
    };

    //检查网络
    private void checkNetwork() {
        //有网络的情况下

        LogUtil.i("----------www-------------");

        btPass.setVisibility(View.VISIBLE);
        if (NetWorkStateUtil.isNetworkAvailable(this)){
            FullPageAdsBiz.getAds(this, new HttpRequestListener() {
                @Override
                public void onHttpRequestFinish(String result) throws JSONException {
                    LogUtil.i("开屏页广告:"+result);
                    handleAdvResult(result);
                }

                @Override
                public void onHttpRequestError(String error) {
//                    Intent intent = new Intent(SplashAct.this , OpenDoorActivity.class);
//                    startActivity(intent);
//                    SplashAct.this.finish();
                    LogUtil.i("------------------------------------------");
                }
            });
        }else{
            //倒计时进入首页
            countDown();
            ToastUtil.makeToast("当前没有网络!!");
            //从sp中加载上一次缓存的图片链接
            SharedPreferences sp=getSharedPreferences("ImageSharePreference", Context.MODE_PRIVATE);
            String image=sp.getString("imageUrl","");

            //TODO 当没有网络时,默认显示上次显示的图片
            showAdvsFromSDcard(image);
        }
    }

    //处理请求返回数据
    private void handleAdvResult(String result) throws JSONException {
        LogUtil.i("开屏广告的数据:"+result);
        JSONObject json = new JSONObject(result);
        if ("200".equals(json.getString("result"))){
            JSONArray ary=json.getJSONArray("data");
            adv_list = JsonPaser.parserAry(ary.toString(), FullPageAdsEntity.class);

            //设置剩余秒数进入首页
            int size = adv_list.size();
            if(size!=0){
                count = size * 2 + 1;
            }
            //默认保存一张图片到本地,在没有网络的时候显示
            FullPageAdsEntity enty = (FullPageAdsEntity) adv_list.get(0);

            showAdvsFromNetwork();

            //将图片的链接保存到sp中
            SharedPreferences sp=getSharedPreferences("ImageSharePreference", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("imageUrl",enty.getAddress());
            editor.commit();
        }
    }

    //计数器
    private void countDown() {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new TimerTask() {
                    @Override
                    public void run() {
                        tvTime.setText(count+"秒进入首页..");
                    }
                });
                count--;
                if (count == 1) {
                    timer.cancel();
                    Intent intent = new Intent(SplashAct.this , MainActivity.class);
                    startActivity(intent);
                    SplashAct.this.finish();
                    changeQianDao();
                }
            }
        },0,1000);
    }

    private void changeQianDao() {
        //关闭页面之后,改变首页的按钮状态
        Intent intent=new Intent(Const.ACTION_TO_CHECK_THE_STATE_OF_QIAN_DAO);
        sendBroadcast(intent);
    }

    //显示从网络获取的广告图片
    private void showAdvsFromNetwork() {
        imgSplash.setVisibility(View.VISIBLE);
        rlCountdown.setVisibility(View.VISIBLE);

        if(adv_list.size() > 0){
            final Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if(advIndex == adv_list.size()){
                        timer.cancel();
                        return;
                    }
                    final FullPageAdsEntity enty = (FullPageAdsEntity) adv_list.get(advIndex);
                    url=enty.getAdlink();
                    //显示图片,在主线程
                    runOnUiThread(new TimerTask() {
                        @Override
                        public void run() {
                            ImagerLoaderUtil.displayImage(enty.getAddress(),imgSplash);
                        }
                    });
                    //广告下标+1
                    advIndex++;
                }
            },1000,1000);
        }
        countDown();
    }

    //从本地显示图片
    private void showAdvsFromSDcard(final String address){
        imgSplash.setVisibility(View.VISIBLE);
        rlCountdown.setVisibility(View.VISIBLE);

        Handler handler = new Handler();
        handler.postDelayed(new TimerTask() {
            @Override
            public void run() {
                ImagerLoaderUtil.displayImage(address,imgSplash);
            }
        },1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    private String getVersion() {
        try {
            PackageManager packageManager = getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "版本号未知";
        }
    }
}
