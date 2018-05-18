package com.xinspace.csevent.login.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.util.DialogUtil;
import com.xinspace.csevent.util.StatusBarUtils;
import com.xinspace.csevent.util.ToastUtil;
import com.xinspace.csevent.ui.activity.BaseActivity;

/**
 * Created by Android on 2016/9/21.
 */
public class SettingAct extends BaseActivity {

    private RelativeLayout rel_faq , rel_version_message , rel_sweepstakes_rules , rel_clear_cache;

    private TextView tv_log_out;

    private LinearLayout ll_setting_back;

    private SDPreference preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(this , R.color.app_bottom_color);
        setContentView(R.layout.act_setting);

        preference = SDPreference.getInstance();

        initView();
    }

    private void initView() {

        tv_log_out = (TextView) findViewById(R.id.tv_log_out);
        tv_log_out.setOnClickListener(clickListener);

        ll_setting_back = (LinearLayout) findViewById(R.id.ll_setting_back);
        ll_setting_back.setOnClickListener(clickListener);

        rel_faq = (RelativeLayout) findViewById(R.id.rel_faq);
        rel_faq.setOnClickListener(clickListener);

        rel_version_message = (RelativeLayout) findViewById(R.id.rel_version_message);
        rel_version_message.setOnClickListener(clickListener);

        rel_sweepstakes_rules = (RelativeLayout) findViewById(R.id.rel_sweepstakes_rules);
        rel_sweepstakes_rules.setOnClickListener(clickListener);

        rel_clear_cache = (RelativeLayout) findViewById(R.id.rel_clear_cache);
        rel_clear_cache.setOnClickListener(clickListener);

    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_log_out: //退出登录
                    Logout();
                    break;
                case R.id.ll_setting_back:
                    finish();
                    break;
                case R.id.rel_faq:
                    Intent faqIntent = new Intent(SettingAct.this , FaqAct.class);
                    startActivity(faqIntent);
                    break;
                case R.id.rel_version_message:
                    Intent aboutIntent = new Intent(SettingAct.this , AboutUsActivity.class);
                    startActivity(aboutIntent);
                    break;
                case R.id.rel_sweepstakes_rules:
                    Intent ruleIntent = new Intent(SettingAct.this , SweepRulesAct.class);
                    startActivity(ruleIntent);
                    break;
                case R.id.rel_clear_cache:

                    ToastUtil.makeToast("已清除缓存");

                    break;
            }
        }
    };

    private void Logout() {
        //弹出退出提示对话框
        DialogUtil.showTipsDailog(this, "退出提示", "您确定要退出登录吗?", postitiveListener, negativeListener);
    }

    //确定
    private DialogInterface.OnClickListener postitiveListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (preference.getContent("userId").equals("0")) {
                ToastUtil.makeToast("未登录");
                return;
            }
            ToastUtil.makeToast("已退出");
            //CoresunApp.USER_ID = null;
            //将全局的个人信息置空
            //CoresunApp.username = "";
            //CoresunApp.userIcon = null;
            //CoresunApp.USER_TEL = "";
            //将偏好存储的个人信息清空
//            SharedPreferencesUtil1.saveString(SettingAct.this, "userId", null);
//            SharedPreferencesUtil1.saveString(SettingAct.this, "username", null);
//            SharedPreferencesUtil1.saveString(SettingAct.this, "username", null);

            preference.putContent("userId" ,"0");
            preference.putContent("tel" , "0");
            preference.putContent("nickName" , "0");
            preference.putContent("openid" , "0");
            preference.putContent("GetNeiborId" , "0");

            finish();
        }
    };

    //取消
    private DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            DialogUtil.close();
        }
    };

}
