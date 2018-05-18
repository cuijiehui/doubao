package com.xinspace.csevent.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.ToastUtil;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	private IWXAPI api;
	public static int codeResult;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
		LogUtil.i("微信支付回调onReq");
	}

	@Override
	public void onResp(BaseResp resp) {
		finish();
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			int code = resp.errCode;
			LogUtil.i("微信支付回调onResp返回码:"+code);
			if(code == 0){//成功
				codeResult = 1;
				ToastUtil.makeToast("支付成功");
			}else if(code==-1){
				//错误
				codeResult = -1;
				ToastUtil.makeToast("支付错误,请稍后重试");
			}else if(code==-2){
				//用户取消
				codeResult = -2;
				ToastUtil.makeToast("取消微信支付");
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i("www" , "----销毁了----");
	}
}