package com.xinspace.csevent.ui.activity;
/**
 * 开屏广告
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.data.biz.FullPageAdsBiz;
import com.xinspace.csevent.data.entity.FullPageAdsEntity;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.parser.JsonPaser;
import com.xinspace.csevent.util.Const;
import com.xinspace.csevent.util.ImagerLoaderUtil;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.NetWorkStateUtil;
import com.xinspace.csevent.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class FullPageAdsActivity extends BaseActivity {
    private ImageView ivAds;
    private TextView tvTime;
    private Button btPass;
    private RelativeLayout rlCountdown;
    private int count=5;//距离进入首页的秒数,默认5秒
    private List<Object> adv_list;//广告集合
    private int advIndex=0;//显示广告的下标
    private String url;//跳转的网页链接

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullpage_ads);

        setViews();
        setListeners();
        checkNetwork();
        //不允许侧滑返回上级页面
        getSwipeBackLayout().setEnableGesture(false);
    }
    //检查网络
    private void checkNetwork() {
        //有网络的情况下
        if (NetWorkStateUtil.isNetworkAvailable(this)){
            FullPageAdsBiz.getAds(this, new HttpRequestListener() {
                @Override
                public void onHttpRequestFinish(String result) throws JSONException {
                    LogUtil.i("开屏页广告:"+result);
                    handleAdvResult(result);
                }

                @Override
                public void onHttpRequestError(String error) {

                }
            });
        }else{
            //倒计时进入首页
            countDown();

            ToastUtil.makeToast("当前没有网络!!");
            //从sp中加载上一次缓存的图片链接
            SharedPreferences sp=getSharedPreferences("ImageSharePreference", MODE_PRIVATE);
            String image=sp.getString("imageUrl","");

            //TODO 当没有网络时,默认显示上次显示的图片
            showAdvsFromSDcard(image);
        }
    }
    //设置监听
    private void setListeners() {
        //点击进入相关广告网页
        ivAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FullPageAdsActivity.this, WebViewActivity.class);
                intent.putExtra("data", url);
                startActivity(intent);
                finish();
            }
        });
        //跳过广告
        btPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FullPageAdsActivity.this , MainActivity.class);
                startActivity(intent);
                finish();
                changeQianDao();
            }
        });
    }
    //
    private void changeQianDao() {
        //关闭页面之后,改变首页的按钮状态
        Intent intent=new Intent(Const.ACTION_TO_CHECK_THE_STATE_OF_QIAN_DAO);
        sendBroadcast(intent);
    }

    //初始化组件
    private void setViews() {
        ivAds=(ImageView)findViewById(R.id.iv_fullpage_ads);
        tvTime=(TextView)findViewById(R.id.tv_fullpage_txt2);
        rlCountdown=(RelativeLayout)findViewById(R.id.rl_fullpageAdv_countdown);
        btPass = (Button) findViewById(R.id.bt_fullpageads_pass);
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
                if (count==0) {
                    timer.cancel();

                    Intent intent = new Intent(FullPageAdsActivity.this , MainActivity.class);
                    startActivity(intent);
                    FullPageAdsActivity.this.finish();
                    changeQianDao();
                }
            }
        },0,1000);
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
                count=size*2+1;
            }
            //默认保存一张图片到本地,在没有网络的时候显示
            FullPageAdsEntity enty = (FullPageAdsEntity) adv_list.get(0);

            showAdvsFromNetwork();

            //将图片的链接保存到sp中
            SharedPreferences sp=getSharedPreferences("ImageSharePreference", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("imageUrl",enty.getAddress());
            editor.commit();
        }
    }
    //显示从网络获取的广告图片
    private void showAdvsFromNetwork() {
        ivAds.setVisibility(View.VISIBLE);
        rlCountdown.setVisibility(View.VISIBLE);

        if(adv_list.size()>0){
            final Timer timer=new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if(advIndex==adv_list.size()){
                        timer.cancel();
                        return;
                    }
                    final FullPageAdsEntity enty = (FullPageAdsEntity) adv_list.get(advIndex);
                    url=enty.getAdlink();
                    //显示图片,在主线程
                    runOnUiThread(new TimerTask() {
                        @Override
                        public void run() {
                            ImagerLoaderUtil.displayImage(enty.getAddress(),ivAds);
                        }
                    });
                    //广告下标+1
                    advIndex++;
                }
            },1000,2000);
        }
        countDown();
    }

    //从本地显示图片
    private void showAdvsFromSDcard(final String address){
        ivAds.setVisibility(View.VISIBLE);
        rlCountdown.setVisibility(View.VISIBLE);

        Handler handler=new Handler();
        handler.postDelayed(new TimerTask() {
            @Override
            public void run() {
              ImagerLoaderUtil.displayImage(address,ivAds);
            }
        },2000);
    }
}
