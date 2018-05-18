package sdk_sample.sdk.views;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.xinspace.csevent.R;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.biz.GetDataBiz;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.SharedPreferencesUtil1;

import org.json.JSONException;
import org.json.JSONObject;

import sdk_sample.sdk.HttpRespListener;
import sdk_sample.sdk.bean.LockRecordBean;
import sdk_sample.sdk.result.BaseResult;
import sdk_sample.sdk.utils.HttpUtils;
import sdk_sample.sdk.utils.SharedPreferencesUtil;
import sdk_sample.sdk.utils.ToolsUtil;

import static com.xinspace.csevent.app.AppConfig.COMMUNITY_AREA;

/**
 * Created by Android on 2017/3/23.
 */

public class OpenLockDialog extends Dialog {
    private Context context;
    private String doorName;
    private Button btn_open_video;
    private Button btn_get_password;
    private Button btn_back;
    private SlidingLinearLayout btn_open;

    @SuppressLint({"NewApi"})
    String domain_sn;
    String toSipNumber;
    String path;
    String token;
    String userName;

    private SDPreference preference;
    private String cUid;
    private String cToken;
    private String phone;
    private String area;

    public OpenLockDialog(Context context, String doorName) {
        super(context, R.style.dialog);
        this.doorName = doorName;
        this.context = context;
        preference = SDPreference.getInstance();
        cUid = preference.getContent("cUid");
        cToken = preference.getContent("cToken");
        phone = preference.getContent("mobile");
        area = SharedPreferencesUtil1.getString(context, COMMUNITY_AREA, "");
        setCustomDialog();
    }

    private void setCustomDialog() {
        // TODO Auto-generated method stub
        View view = LayoutInflater.from(getContext()).inflate(R.layout.sayee_dialog_open_lock, null);
//        WindowManager.LayoutParams p = this.getWindow().getAttributes();
//        WindowManager windowManager = (WindowManager)this.context.getSystemService(Context.WINDOW_SERVICE);
//        Display display = windowManager.getDefaultDisplay();
//        p.height = (int)((double)display.getHeight() * 0.4D);
//        p.width = (int)((double)display.getWidth() * 0.9D);
//        this.getWindow().setAttributes(p);

        btn_open_video = (Button)view.findViewById(R.id.btn_open_video);
        btn_get_password = (Button)view.findViewById(R.id.btn_get_password);
        btn_open = (SlidingLinearLayout)view.findViewById(R.id.btn_open);
        btn_back = (Button)view.findViewById(R.id.btn_back);
        this.setListener();

        super.setContentView(view);
    }



//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(1);
//
//        View view = LayoutInflater.from(getContext()).inflate(R.layout.sayee_dialog_open_lock, null);
//
//        //this.setContentView(R.layout.sayee_dialog_open_lock);
//        initView();
//        this.setListener();
//        WindowManager.LayoutParams p = this.getWindow().getAttributes();
//        WindowManager windowManager = (WindowManager)this.context.getSystemService(Context.WINDOW_SERVICE);
//        Display display = windowManager.getDefaultDisplay();
//        p.height = (int)((double)display.getHeight() * 0.4D);
//        p.width = (int)((double)display.getWidth() * 0.9D);
//        this.getWindow().setAttributes(p);
//    }

    public void setListener() {
//        if(this.openVideoListener != null && this.btn_open_video != null) {
//            this.btn_open_video.setOnClickListener((OnClickListener) openVideoListener);
//        }
//
//        if(this.openDoorForpasswordListener != null && this.btn_get_password != null) {
//            this.btn_get_password.setOnClickListener( openDoorForpasswordListener);
//        }

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenLockDialog.this.dismiss();
            }
        });
    }

//    public void setOpenVideoListener(View.OnClickListener listener) {
//        btn_open_video.setOnClickListener(listener);
//    }


//    public void setOneListener(View.OnClickListener listener) {
//        rel_one_door.setOnClickListener(listener);
//    }

    public void setOpenDoorForpasswordListener(View.OnClickListener listener) {
        this.btn_get_password.setOnClickListener(listener);
    }

    public void setOpenVideoListener(View.OnClickListener listener){
        this.btn_open_video.setOnClickListener(listener);
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setDomain_sn(String domain_sn) {
        this.domain_sn = domain_sn;
    }

    public void setToSipNumber(String toSipNumber) {
        this.toSipNumber = toSipNumber;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if(this.btn_open != null && this.btn_open.handleActivityEvent(event) && !TextUtils.isEmpty(this.domain_sn) && !TextUtils.isEmpty(this.toSipNumber)) {
            SharedPreferencesUtil.saveData(this.context, "sayee_user_name_key", this.userName);
            SharedPreferencesUtil.saveData(this.context, "sayee_domain_sn_key", this.domain_sn);
            SharedPreferencesUtil.saveData(this.context, "sayee_type_key", Integer.valueOf(0));

            HttpUtils.openDoorLock(this.context, this.path, this.token, this.userName, this.domain_sn, 0, this.toSipNumber, (String)null, new HttpRespListener() {
                public void onSuccess(int code, BaseResult result) {
                    LockRecordBean bean = new LockRecordBean();
                    bean.setUid(cUid);
                    bean.setToken(cToken);
                    bean.setPhone(phone);
                    bean.setEquip_sn(domain_sn);
                    bean.setType("1");
                    addEntranceRecord(bean);
                    ToolsUtil.toast(OpenLockDialog.this.context, "已发送开锁请求");
                }

                public void onFail(int code, String msg) {
                    if(code == 3) {
                        Intent intent = new Intent();
                        intent.setAction("com.sayee.sdk.action.token.fail");
                        intent.putExtra("sayee_callback_code", 1);
                        intent.putExtra("sayee_error_msg", "token重新获取失败");
                        OpenLockDialog.this.context.sendBroadcast(intent);
                    } else {
                        ToolsUtil.toast(OpenLockDialog.this.context, msg);
                    }
                }
            });
        }
        return super.onTouchEvent(event);
    }


    private void addEntranceRecord(LockRecordBean bean){

        GetDataBiz.ADD_LOCK_RECORD(area, bean, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                LogUtil.i("开门记录上传返回" + result);

                if (result == null || result.equals("")){
                    return;
                }
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getInt("code") == 200){
                    LogUtil.i("开门记录上传成功");
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }
}