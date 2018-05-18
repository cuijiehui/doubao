package com.xinspace.csevent.login.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.util.StatusBarUtils;
import com.xinspace.csevent.ui.activity.BaseActivity;

/**
 * 关于我们页面
 */
public class AboutUsActivity extends BaseActivity {
    private LinearLayout ll_back;
    private TextView tvVersion;
    private RelativeLayout rel_system_notification, rel_user_agreement;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StatusBarUtils.setWindowStatusBarColor(AboutUsActivity.this , R.color.app_bottom_color);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        setView();
        setListener();
        getVersionCode();
    }

    //设置监听器
    private void setListener() {
        //返回
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //初始化组件
    private void setView() {
        ll_back = (LinearLayout) findViewById(R.id.ll_about_us_back);
        tvVersion = (TextView) findViewById(R.id.tv_about_us_version);

        rel_system_notification = (RelativeLayout) findViewById(R.id.rel_system_notification);
        rel_user_agreement = (RelativeLayout) findViewById(R.id.rel_user_agreement);

        rel_system_notification.setOnClickListener(clickListener);
        rel_user_agreement.setOnClickListener(clickListener);
    }

    //获取版本号
    private void getVersionCode() {
        PackageManager manager = this.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            tvVersion.setText("版本号 ver " + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.rel_system_notification:
                    intent = new Intent(AboutUsActivity.this, SystemNotificaAct.class);
                    break;
                case R.id.rel_user_agreement:
                    intent = new Intent(AboutUsActivity.this, SweepRulesAct.class);
                    break;
            }
            startActivity(intent);
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        clickListener = null;
        this.setContentView(R.layout.empty_view);
    }


}
