package com.xinspace.csevent.monitor.activity;


import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.xinspace.csevent.R;
import com.xinspace.csevent.util.StatusBarUtils;
import com.xinspace.csevent.ui.activity.BaseActivity;

/**
 * 周边商家
 *
 * Created by Android on 2017/3/17.
 */

public class RimShopActivity extends BaseActivity{

    private LinearLayout ll_rim_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_rim_shop);
        StatusBarUtils.setWindowStatusBarColor(this , R.color.app_bottom_color);

        initView();

    }

    private void initView() {
        ll_rim_back = (LinearLayout) findViewById(R.id.ll_rim_back);
        ll_rim_back.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_rim_back:
                    RimShopActivity.this.finish();
                    break;
            }
        }
    };

}
