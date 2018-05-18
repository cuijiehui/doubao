package com.xinspace.csevent.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.xinspace.csevent.R;
import com.xinspace.csevent.login.activity.FeedbackActivity;

/**
 * 会员中心页面
 */
public class MemberCenterActivity extends BaseActivity {

    private LinearLayout llBack;
    private RelativeLayout rlFeedback;
    private RelativeLayout rlQa;
    private RelativeLayout rlRule;
    private RelativeLayout rlEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_center);
        setViews();
        setListeners();
    }

    private void setListeners() {
        //点击返回
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //意见反馈
        rlFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MemberCenterActivity.this, FeedbackActivity.class);
                startActivity(intent);
            }
        });
        //Q&A
        rlQa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MemberCenterActivity.this, QAndAActivity.class);
                startActivity(intent);
            }
        });
        //抽奖规则
        rlRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MemberCenterActivity.this, RulesActivity.class);
                startActivity(intent);
            }
        });
        //邮箱
        rlEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MemberCenterActivity.this, EmailActivity.class);
                startActivity(intent);
            }
        });

    }

    private void setViews() {
        llBack = (LinearLayout) findViewById(R.id.ll_member_center_back);
        rlFeedback = (RelativeLayout) findViewById(R.id.rl_member_center_feedback);
        rlQa = (RelativeLayout) findViewById(R.id.rl_member_center_qa);
        rlRule = (RelativeLayout) findViewById(R.id.rl_member_center_rule);
        rlEmail = (RelativeLayout) findViewById(R.id.rl_member_center_email);
    }
}
