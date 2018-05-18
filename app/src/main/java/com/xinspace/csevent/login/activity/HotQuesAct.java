package com.xinspace.csevent.login.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.login.model.HotQuestionBean;
import com.xinspace.csevent.util.StatusBarUtils;
import com.xinspace.csevent.ui.activity.BaseActivity;

/**
 * Created by Android on 2016/11/10.
 */
public class HotQuesAct extends BaseActivity{


    private LinearLayout ll_back;
    private TextView tv_hot_question;
    private TextView tv_hot_answers;
    private Intent intent;
    private HotQuestionBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(this , R.color.app_bottom_color);
        setContentView(R.layout.act_hotques);

        intent = getIntent();
        if (intent != null ){
            bean = (HotQuestionBean) intent.getSerializableExtra("data");
        }
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clickListener = null;
    }

    private void initView() {
        ll_back  = (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(clickListener);

        tv_hot_question  = (TextView) findViewById(R.id.tv_hot_question);
        tv_hot_answers  = (TextView) findViewById(R.id.tv_hot_answers);

        tv_hot_question.setText("1. " + bean.getQuestions() );
        tv_hot_answers.setText(bean.getAnswers());
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_back:
                    HotQuesAct.this.finish();
                    break;
            }
        }
    };

}
