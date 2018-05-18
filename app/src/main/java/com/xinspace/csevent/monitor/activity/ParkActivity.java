package com.xinspace.csevent.monitor.activity;


import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.xinspace.csevent.R;
import com.xinspace.csevent.util.StatusBarUtils;
import com.xinspace.csevent.ui.activity.BaseActivity;

/**
 * 停车界面
 *
 * Created by Android on 2017/3/17.
 */

public class ParkActivity extends BaseActivity{

    private LinearLayout ll_park_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_park);
        StatusBarUtils.setWindowStatusBarColor(this , R.color.app_bottom_color);

        initView();

    }

    private void initView() {
        ll_park_back = (LinearLayout) findViewById(R.id.ll_park_back);
        ll_park_back.setOnClickListener(onClickListener);

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_park_back:
                    ParkActivity.this.finish();
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        this.setContentView(R.layout.empty_view);
        onClickListener = null;
        System.gc();
        super.onDestroy();
    }

}
