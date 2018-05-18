package sdk_sample;



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import sdk_sample.sdk.Consts;
import sdk_sample.sdk.HttpRespListener;
import sdk_sample.sdk.result.BaseResult;
import sdk_sample.sdk.result.TokenResult;
import sdk_sample.sdk.utils.SharedPreferencesUtil;
import sdk_sample.sdk.utils.ToolsUtil;


/**
 * 1.密码开锁的时，获取密码和过期时间
 * 2.token失效回调处理
 *
 */
public class RamdonPasswordReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(final Context context, Intent intent) {
		
		if(Consts.SAYEE_RANDOM_CODE_ACTION.equals(intent.getAction())){
			String random_password=intent.getStringExtra(Consts.SAYEE_RANDOM_PASSWORD);
			long random_password_deadline=intent.getLongExtra(Consts.SAYEE_RANDOM_PASSWORD_DEADLINE,0);
			Intent intent2=new Intent(context,RamdonPasswordActivity.class);
			intent2.putExtra(Consts.SAYEE_RANDOM_PASSWORD, random_password);
			intent2.putExtra(Consts.SAYEE_RANDOM_PASSWORD_DEADLINE, random_password_deadline);
			intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent2);
		}else if(Consts.SAYEE_TOKEN_FAIL_ACTION.equals(intent.getAction())){
			final int callBackCode=intent.getIntExtra(Consts.SAYEE_CALLBACK_CODE, 0);
			String msg=intent.getStringExtra(Consts.SAYEE_ERROR_MSG);
			if(Consts.AGAIN_GET_TOKEN_ERROR_MSG.equals(msg)){
				//第三方开发，此处调用自己的服务器获取 赛翼的token后，调用方法
				//ToolsUtil.callback(context, callBackCode, "获取到的token", "token失效的时间");

				//例如demo中实现
				String userName= SharedPreferencesUtil.getUserName(context);
				ToolsUtil.getToken(context,"2",userName,"2",null,new HttpRespListener() {
					@Override
					public void onSuccess(int code, BaseResult result) {
						TokenResult tokenResult=(TokenResult) result;
						ToolsUtil.callback(context, callBackCode, tokenResult.getToken(), tokenResult.getDead_time());
					}
					
					@Override
					public void onFail(int code, String msg) {
						
					}
				});
			}
		}
	}

}
