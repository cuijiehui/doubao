package sdk_sample.sdk.activity;

/**
 * Created by Android on 2017/3/23.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;


import org.linphone.mediastream.Log;

import sdk_sample.sdk.HttpRespListener;
import sdk_sample.sdk.result.BaseResult;
import sdk_sample.sdk.result.TokenResult;
import sdk_sample.sdk.utils.HttpUtils;
import sdk_sample.sdk.utils.SharedPreferencesUtil;

public abstract class BaseActivity extends Activity {
    protected boolean isGetToken = false;
    protected long deal_time;
    protected String userName;
    protected String token;

    public BaseActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(1);
    }

    protected void onResume() {
        super.onResume();
        Log.i(" -----------  " + this.userName + " ---" + this.token + " ---- " + this.isGetToken + " --- " + System.currentTimeMillis() + " --- " + this.deal_time);
        if(TextUtils.isEmpty(this.userName) || TextUtils.isEmpty(this.token) || this.deal_time == 0L) {
            this.userName = (String) SharedPreferencesUtil.getData(this, "sayee_user_name_key", "");
            this.token = (String)SharedPreferencesUtil.getData(this, "sayee_user_token_key", "");
            this.deal_time = SharedPreferencesUtil.getDealTime(this);
        }

        if(!this.isGetToken && !TextUtils.isEmpty(this.userName) && !TextUtils.isEmpty(this.token)) {
            this.getNewToken();
        }

    }

    protected void getNewToken() {
        Log.i("------------重新获取token-----------");
        if(System.currentTimeMillis() - 300000L > this.deal_time) {
            HttpUtils.getToken(this, this.userName, this.token, new HttpRespListener() {
                public void onSuccess(int code, BaseResult result) {
                    Log.i("------------重新获取成功-----------");
                    if(result instanceof TokenResult) {
                        TokenResult tokenResult = (TokenResult)result;
                        BaseActivity.this.token = tokenResult.getToken();
                        BaseActivity.this.deal_time = tokenResult.getDead_time();
                    }

                }

                public void onFail(int code, String msg) {
                    Log.i("------------重新获取失败-----------" + code);
                    if(code == 3) {
                        Intent intent = new Intent();
                        intent.setAction("com.sayee.sdk.action.token.fail");
                        intent.putExtra("sayee_callback_code", 0);
                        intent.putExtra("sayee_error_msg", "token重新获取失败");
                        BaseActivity.this.sendBroadcast(intent);
                    }

                }
            });
        }

    }
}

