package com.xinspace.csevent.monitor.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.monitor.bean.Data;
import com.xinspace.csevent.util.StatusBarUtils;
import com.xinspace.csevent.ui.activity.BaseActivity;
import com.xinspace.csevent.util.ToastUtil;
import com.xinspace.csevent.util.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import sdk_sample.sdk.HttpRespListener;
import sdk_sample.sdk.bean.OpenLockPasswordBean;
import sdk_sample.sdk.result.BaseResult;
import sdk_sample.sdk.result.OpenLockPasswordResult;
import sdk_sample.sdk.utils.HttpUtils;

/**
 * 密码开门
 *
 * Created by Android on 2017/3/17.
 */

public class PassWordAct extends BaseActivity{

    private LinearLayout ll_pass_word;
    private TextView tv_pass_word;
    private String psw;
    private String path;
    private String token;
    private String userName;
    private String neigbor_id;
    private Long dead_time;
    private String sip_number;
    private TextView tv_propat;
    private String dealTimeString;
    private ProgressBar progress;
    private static final String MOB_SHARE_KEY = "ce3ba281952a";
    private static final String MOB_SHARE_SCRECT = "e38fe84a117dce307438796385211e9a";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_pass_word);
        StatusBarUtils.setWindowStatusBarColor(this , R.color.app_bottom_color);
        ShareSDK.initSDK(this, MOB_SHARE_KEY);
        getDataFromPrePage();
        initView();
    }

    private void getDataFromPrePage() {
        psw = getIntent().getStringExtra("sayee_random_password");
        path = getIntent().getStringExtra("path");
        token = getIntent().getStringExtra("token");
        userName = getIntent().getStringExtra("userName");
        neigbor_id = getIntent().getStringExtra("neigbor_id");
        dead_time = getIntent().getLongExtra("dead_time", 0L);
        sip_number = getIntent().getStringExtra("sip_number");
        dealTimeString = FormatDate(dead_time);
    }

    public String FormatDate(Long time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String longt = sdf.format(Long.parseLong(time+"000"));
        return longt;
    }

    private void initView() {
        ll_pass_word = (LinearLayout) findViewById(R.id.ll_pass_word);
        ll_pass_word.setOnClickListener(onClickListener);
        tv_pass_word = (TextView)findViewById(R.id.open_door_psw);
        tv_pass_word.setText(psw);
        tv_propat = (TextView) findViewById(R.id.tv_propat);
        tv_propat.setText(dealTimeString);
        progress = (ProgressBar) findViewById(R.id.psw_progressbar);
        progress.setVisibility(View.GONE);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_pass_word:
                    PassWordAct.this.finish();
                    break;
            }
        }
    };

    public void handlePassword(View view){
        switch (view.getId()){
            case R.id.btn_get_psw_again:
                progress.setVisibility(View.VISIBLE);
                HttpUtils.getOpenLockPassword(path, token, userName, sip_number, new HttpRespListener() {
                    @Override
                    public void onSuccess(int var1, BaseResult result) {
                        OpenLockPasswordBean bean = ((OpenLockPasswordResult) result).getResult();
                        psw = bean.getRandom_pw();
                        tv_pass_word.setText(psw);
                        tv_propat.setText(FormatDate(bean.getRandomkey_dead_time()));
                        progress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFail(int var1, String var2) {
                        ToastUtil.makeToast("加载密码失败");
                        progress.setVisibility(View.GONE);
                    }
                });
                break;

            case R.id.btn_psw_share:
                Wechat.ShareParams shareParams = new Wechat.ShareParams();
                shareParams.setShareType(Platform.SHARE_TEXT);
                shareParams.setText(psw);
                Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                wechat.setPlatformActionListener(new PlatformActionListener() {
                    @Override
                    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                        ToastUtil.makeToast("分享成功！");
                    }

                    @Override
                    public void onError(Platform platform, int i, Throwable throwable) {
                        ToastUtil.makeToast("分享失败！");
                    }

                    @Override
                    public void onCancel(Platform platform, int i) {
                        ToastUtil.makeToast("取消分享！");
                    }
                });
                wechat.share(shareParams);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        this.setContentView(R.layout.empty_view);
        onClickListener = null;
        System.gc();
        super.onDestroy();
    }

}
