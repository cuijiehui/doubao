package com.xinspace.csevent.login.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xinspace.csevent.R;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.util.StatusBarUtils;
import com.xinspace.csevent.util.ToastUtil;
import com.xinspace.csevent.ui.activity.BaseActivity;

/**
 * Created by Android on 2016/11/17.
 * 系统通知界面
 *
 */
public class SystemNotificaAct extends BaseActivity{

    private LinearLayout ll_back;
    private ImageView but_notification1;
    private ImageView but_notification2;

    private SDPreference preference;

    private boolean isOpen1 ;
    private boolean isOpen2 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(this , R.color.app_bottom_color);
        setContentView(R.layout.act_system_notifica);
        preference = SDPreference.getInstance();
        initView();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        clickListener = null;
    }

    private void initView() {

        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(clickListener);

        but_notification1 = (ImageView) findViewById(R.id.but_notification1);
        but_notification2 = (ImageView) findViewById(R.id.but_notification2);

        but_notification1.setOnClickListener(clickListener);
        but_notification2.setOnClickListener(clickListener);

        isOpen1 = preference.getBooleanContent("isOpen1");
        isOpen2 = preference.getBooleanContent("isOpen2");

        if (isOpen1){
            but_notification1.setBackgroundResource(R.drawable.icon_notifica_open);
        }else{
            but_notification1.setBackgroundResource(R.drawable.icon_notifica_close);
        }

        if (isOpen2){
            but_notification2.setBackgroundResource(R.drawable.icon_notifica_open);
        }else{
            but_notification2.setBackgroundResource(R.drawable.icon_notifica_close);
        }

    }


    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_back:
                    SystemNotificaAct.this.finish();
                    break;
                case R.id.but_notification1:
                    isOpen1 = preference.getBooleanContent("isOpen1");
                    if (isOpen1){
                        preference.putContent("isOpen1" , false);
                        but_notification1.setBackgroundResource(R.drawable.icon_notifica_close);
                        ToastUtil.makeToast("揭晓通知已关闭");
                    }else{
                        preference.putContent("isOpen1" , true);
                        but_notification1.setBackgroundResource(R.drawable.icon_notifica_open);
                        ToastUtil.makeToast("揭晓通知已打开");
                    }
                    break;
                case R.id.but_notification2:
                    isOpen2 = preference.getBooleanContent("isOpen2");
                    if (isOpen2){
                        preference.putContent("isOpen2" , false);
                        but_notification2.setBackgroundResource(R.drawable.icon_notifica_close);
                        ToastUtil.makeToast("夜间免打扰已关闭");
                    }else{
                        preference.putContent("isOpen2" , true);
                        but_notification2.setBackgroundResource(R.drawable.icon_notifica_open);
                        ToastUtil.makeToast("夜间免打扰已打开");
                    }
                    break;
            }
        }
    };

}
