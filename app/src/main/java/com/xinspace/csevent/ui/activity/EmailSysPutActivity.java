package com.xinspace.csevent.ui.activity;
/**
 * 会员中心邮箱的系统推送页面
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.data.entity.EmailForSysMsgEntity;

public class EmailSysPutActivity extends BaseActivity {

    private LinearLayout ll_back;
    private EmailForSysMsgEntity enty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_sys_put);

        getData();

        setViews();
        setListeners();
    }
    //获取上个页面传递过来的数据
    private void getData() {
        Intent intent = getIntent();
        enty=(EmailForSysMsgEntity)intent.getSerializableExtra("data");

    }

    private void setListeners() {
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setViews() {
        TextView tvFeedback = (TextView) findViewById(R.id.tv_email_sys_put_txt);
        ll_back= (LinearLayout) findViewById(R.id.ll_email_sys_put_back);
        tvFeedback.setText(enty.getReply());
    }


}
