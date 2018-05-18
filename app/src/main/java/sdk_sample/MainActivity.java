package sdk_sample;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.xinspace.csevent.R;

import java.util.HashMap;
import java.util.Map;

import sdk_sample.sdk.HttpRespListener;
import sdk_sample.sdk.result.BaseResult;
import sdk_sample.sdk.result.TokenResult;
import sdk_sample.sdk.utils.ToolsUtil;

/**
 * 赛义门禁设备
 *
 *
 */


public class MainActivity extends Activity implements OnClickListener {

	private EditText et_key,et_user_name,et_app_id,et_neibor_flag;
	private Button btn_get_token,btn_start,btn_open_call;
	private String token;
	private long dealTime;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//		btn_start=(Button) findViewById(R.id.btn_start);
//		btn_open_call=(Button) findViewById(R.id.btn_open_call);
//		btn_get_token=(Button) findViewById(R.id.btn_get_token);
//		et_key=(EditText) findViewById(R.id.et_key);
//		et_user_name=(EditText) findViewById(R.id.et_user_name);
//		et_app_id=(EditText) findViewById(R.id.et_app_id);
//		et_neibor_flag=(EditText) findViewById(R.id.et_neibor_flag);

		btn_start.setOnClickListener(this);
		btn_get_token.setOnClickListener(this);
		btn_open_call.setOnClickListener(this);
	}
	
	
	protected void onDestroy() {
		super.onDestroy();
		System.gc();
	}
	
	@Override
	public void onClick(View view) {
//		switch (view.getId()) {
//		case R.id.btn_get_token:
//			doGetToken();
//			break;
//		case R.id.btn_start:
//			doGetNeiborId();
//			break;
//		case R.id.btn_open_call:
//			if(SayeeManager.getInstance().isEnableCall()){
//				SayeeManager.getInstance().turnOffCall(this);
//				btn_open_call.setText("开启来电模式");
//			}else{
//				SayeeManager.getInstance().turnOnCall(this);
//				btn_open_call.setText("关闭来电模式");
//			}
//			break;
//		}
		
	}


	/**
	 * 获取token
	 */
	private void doGetToken() {
		String key=et_key.getText().toString();
		String userName=et_user_name.getText().toString();
		String app_id=et_app_id.getText().toString();
		if(TextUtils.isEmpty(key)){
			ToolsUtil.toast(this, "key不能为空！");
			return;
		}
		if(TextUtils.isEmpty(userName)){
			ToolsUtil.toast(this, "userName不能为空！");
			return;
		}
		if(TextUtils.isEmpty(app_id)){
			ToolsUtil.toast(this, "appId不能为空！");
			return;
		}
		
		ToolsUtil.getToken(this,key,userName,app_id,null,new HttpRespListener() {
			@Override
			public void onSuccess(int code, BaseResult result) {
				TokenResult tokeResult=(TokenResult)result;
				ToolsUtil.toast(getApplicationContext(), "成功: "+tokeResult.getToken());
				token=tokeResult.getToken();
				dealTime=tokeResult.getDead_time();
			}
			
			@Override
			public void onFail(int code, String msg) {
				ToolsUtil.toast(getApplicationContext(), msg);
			}
		});
	}

	/**
	 * 获取的社区
	 */
	private void doGetNeiborId(){

		Log.i("www" , "------------社区列表请求成功---------------");

		final String userName=et_user_name.getText().toString();
		if(TextUtils.isEmpty(token)){
			ToolsUtil.toast(this, "token不能为空！");
			return;
		}
		if(TextUtils.isEmpty(userName)){
			ToolsUtil.toast(this, "userName不能为空！");
			return;
		}
		
		String neibor_flag=et_neibor_flag.getText().toString();
		
		if(TextUtils.isEmpty(neibor_flag)){
			ToolsUtil.toast(this, "neibor_flag不能为空");
			return;
		}
		
		final Map<String, String> headpParams = new HashMap<String, String>();
		headpParams.put("token",token);
		headpParams.put("username", userName);
		headpParams.put("dealtime", dealTime+"");
		final Map<String, String> params = new HashMap<String, String>();
		params.put("username", userName);
		params.put("neibor_flag", neibor_flag);

		Log.i("www" , "username" + userName);
		Log.i("www" , "neibor_flag" + neibor_flag);


		ToolsUtil.goToOpenDoor(MainActivity.this , headpParams , params,"1" ,new HttpRespListener() {
			@Override
			public void onSuccess(int code, BaseResult result) {
				//请求成功回调

				Log.i("www"  , "code   " + code + "result   " + result.toString());

			}
			
			@Override
			public void onFail(int code, String msg) {
				//请求失败回调

				Log.i("www"  , "失败code   " + code + "msg   " + msg.toString());

				ToolsUtil.toast(getApplicationContext(), msg);
			}
		});
	}

}
