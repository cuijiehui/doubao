package com.xinspace.csevent.monitor.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xinspace.csevent.monitor.bean.AuditBean;
import com.xinspace.csevent.monitor.bean.ChildCommunityBean;
import com.xinspace.csevent.monitor.bean.CommunityBean;
import com.xinspace.csevent.monitor.weiget.ChooseCodePop;
import com.xinspace.csevent.monitor.weiget.ChooseComPop;
import com.xinspace.csevent.monitor.weiget.ChooseIdentityPop;
import com.xinspace.csevent.monitor.weiget.CodeResultListener;
import com.xinspace.csevent.monitor.weiget.ComResultListener;
import com.xinspace.csevent.monitor.weiget.IdentityResultListener;
import com.xinspace.csevent.R;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.biz.GetDataBiz;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.SharedPreferencesUtil1;
import com.xinspace.csevent.util.StatusBarUtils;
import com.xinspace.csevent.util.ToastUtil;
import com.xinspace.csevent.ui.activity.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import static com.xinspace.csevent.app.AppConfig.COMMUNITY_AREA;

/**
 * 审核提交信息界面
 *
 * Created by Android on 2017/6/2.
 */

public class SubmitDataAct extends BaseActivity {

    private EditText ed_name;
    private TextView ed_identity;
    private TextView tv_community_name;
    private TextView tv_community_code;
    private RelativeLayout rel_community_code, rel_community_name;
    private ChooseComPop chooseComPop;
    private LinearLayout lin_bg;
    private TextView tv_submit;

    private CommunityBean groupBean = null;
    private ChildCommunityBean childBean = null;

    private String communityName;
    private String communityName2;

    private String userName;
    private String identity;

    private ChooseCodePop codePop;
    private String unit_id;
    private String codeId;

    private ChooseIdentityPop identityPop;
    private SDPreference preference;

    private String cUid;
    private String token;
    private String community_id;
    private LinearLayout ll_back;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 200:

                    ToastUtil.makeToast("信息提交成功 , 审核中");
                    finish();

                    break;
                case 400:
                    ToastUtil.makeToast((String) msg.obj);
                    break;
            }
        }
    };
    private String area;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_submit_data);
        StatusBarUtils.setWindowStatusBarColor(this , R.color.app_bottom_color);

        area = SharedPreferencesUtil1.getString(this, COMMUNITY_AREA, "");
        preference = SDPreference.getInstance();
        cUid = preference.getContent("cUid");
        token = preference.getContent("cToken");

        initView();
        initData();
    }

    private void initView() {

        lin_bg = (LinearLayout) findViewById(R.id.lin_bg);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(clickListener);

        ed_name = (EditText) findViewById(R.id.ed_name);

        ed_identity = (TextView) findViewById(R.id.ed_identity);
        ed_identity.setOnClickListener(clickListener);

        rel_community_code = (RelativeLayout) findViewById(R.id.rel_community_code);
        rel_community_name = (RelativeLayout) findViewById(R.id.rel_community_name);

        rel_community_name.setOnClickListener(clickListener);
        rel_community_code.setOnClickListener(clickListener);

        tv_community_name = (TextView) findViewById(R.id.tv_community_name);
        tv_community_code = (TextView) findViewById(R.id.tv_community_code);

        tv_submit = (TextView) findViewById(R.id.tv_submit);
        tv_submit.setOnClickListener(clickListener);
    }

    private void initData() {

    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_back:

                    finish();

                    break;
                case R.id.rel_community_name:

                    showPop();

                    break;
                case R.id.rel_community_code:
                    if (unit_id != null) {
                        showPop2(unit_id);
                    }
                    break;
                case R.id.ed_identity:

                    showIdentityPop();

                    break;
                case R.id.tv_submit:  //点击提交信息

                    userName = ed_name.getText().toString().trim();

                    if (TextUtils.isEmpty(userName)) {
                        ToastUtil.makeToast("名字不能为空");
                        return;
                    }

                    if (TextUtils.isEmpty(identity)) {
                        ToastUtil.makeToast("身份不能为空");
                        return;
                    }

                    if (TextUtils.isEmpty(communityName)) {
                        ToastUtil.makeToast("社区不能为空");
                        return;
                    }

                    if (TextUtils.isEmpty(codeId)) {
                        ToastUtil.makeToast("单元号不能为空");
                        return;
                    }

                    AuditBean auditBean = new AuditBean();
                    auditBean.setUid(cUid);
                    auditBean.setToken(token);

                    if (identity.equals("业主")){
                        auditBean.setType("1");
                    }else if(identity.equals("家人")){
                        auditBean.setType("2");
                    }else if (identity.equals("物业员工")){
                        auditBean.setType("3");
                    }else if (identity.equals("物业高管")){
                        auditBean.setType("4");
                    }else if (identity.equals("其他")){
                        auditBean.setType("5");
                    }

                    auditBean.setName(userName);
                    auditBean.setCommunity_id(community_id);
                    auditBean.setUnit_id(unit_id);
                    auditBean.setProperty_id(codeId);

                    auditData(auditBean);

                    break;
            }
        }
    };


    private void auditData(AuditBean auditBean){

        LogUtil.i("-----------------------------------------------");

        GetDataBiz.submitCommunityData(area, auditBean, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                LogUtil.i("提交审核信息返回" + result);
                if (result == null || result.equals("")){
                    return;
                }

                Gson gson = new Gson();
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("code").equals("200")){
                    handler.obtainMessage(200).sendToTarget();
                }else {
                    handler.obtainMessage(400 , jsonObject.getString("message")).sendToTarget();
                }
            }

           @Override
            public void onHttpRequestError(String error) {

            }
        });
    }

    private void showPop() {

        if (chooseComPop == null) {
            chooseComPop = new ChooseComPop(SubmitDataAct.this, comResultListener);
            chooseComPop.setOnDismissListener(dismissListener);
        }

        if (!chooseComPop.isShowing()) {
            chooseComPop.showAtLocation(lin_bg, Gravity.BOTTOM, 0, 0);
            chooseComPop.isShowing();
        } else {
            chooseComPop.dismiss();
        }
    }

    private void showPop2(String id) {

        if (codePop == null) {
            codePop = new ChooseCodePop(SubmitDataAct.this, id, codeResultListener);
            codePop.setOnDismissListener(dismissListener);
        }

        if (!codePop.isShowing()) {
            codePop.showAtLocation(lin_bg, Gravity.BOTTOM, 0, 0);
            codePop.isShowing();
        } else {
            codePop.dismiss();
        }

    }

    private void showIdentityPop() {
        if (identityPop == null) {
            identityPop = new ChooseIdentityPop(SubmitDataAct.this, identityResultListener);
            identityPop.setOnDismissListener(dismissListener);
        }

        if (!identityPop.isShowing()) {
            identityPop.showAtLocation(lin_bg, Gravity.BOTTOM, 0, 0);
            identityPop.isShowing();
        } else {
            identityPop.dismiss();
        }
    }


    PopupWindow.OnDismissListener dismissListener = new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
            chooseComPop = null;
            codePop = null;
            identityPop = null;
        }
    };


    ComResultListener comResultListener = new ComResultListener() {
        @Override
        public void resultBean(CommunityBean groupBean, ChildCommunityBean childBean) {
            communityName = groupBean.getName() + "," + childBean.getName();
            community_id = groupBean.getId();
            unit_id = childBean.getId();
            tv_community_name.setText(communityName);

            codeId = "";
            tv_community_code.setText("");

            LogUtil.i("选择地社区" + communityName);
        }
    };

    CodeResultListener codeResultListener = new CodeResultListener() {
        @Override
        public void codeBean(ChildCommunityBean bean) {

            tv_community_code.setText(bean.getName());
            codeId = bean.getId();
        }
    };


    IdentityResultListener identityResultListener = new IdentityResultListener() {
        @Override
        public void identityResult(String result) {

            ed_identity.setText(result);
            identity = result;

        }
    };


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (chooseComPop != null){
            chooseComPop.dismiss();
        }else if (identityPop != null){
            identityPop.dismiss();
        }else if (codePop != null){
            codePop.dismiss();
        }else{
            SubmitDataAct.this.finish();
        }
        return true;
    }


    @Override
    protected void onDestroy() {

        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        this.setContentView(R.layout.empty_view);
        clickListener = null;
        chooseComPop = null;
        identityResultListener = null;
        codeResultListener = null;
        System.gc();
    }
}
