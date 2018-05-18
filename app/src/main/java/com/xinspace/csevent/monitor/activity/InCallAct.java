package com.xinspace.csevent.monitor.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.ui.activity.BaseActivity;

/**
 * Created by Android on 2017/3/17.
 */

public class InCallAct extends BaseActivity {

    private ImageView img_call_refuse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_incall);

        initView();
    }

    private void initView() {

        img_call_refuse = (ImageView) findViewById(R.id.img_call_refuse);
        img_call_refuse.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.img_call_refuse:
                    InCallAct.this.finish();
                    break;
            }
        }
    };

}
