package com.xinspace.csevent.sweepstake.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.ui.activity.BaseActivity;

/**
 * Created by Android on 2016/9/29.
 */
public class SmartCommAct extends BaseActivity {

    private WebView web_smart;
    private ImageView iv_address_edit_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_smart_comm);

        iv_address_edit_back = (ImageView) findViewById(R.id.iv_address_edit_back);
        iv_address_edit_back.setOnClickListener(clickListener);

        web_smart = (WebView) findViewById(R.id.web_smart);
        WebSettings wSet = web_smart.getSettings();
        wSet.setJavaScriptEnabled(true);
        web_smart.loadUrl("file:///android_asset/smart.html");

    }


    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_address_edit_back:
                    finish();
                    break;
            }
        }
    };

}
