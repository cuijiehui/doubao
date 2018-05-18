package com.xinspace.csevent.publish.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.app.utils.TimeHelper;
import com.xinspace.csevent.publish.model.PublishBean;
import com.xinspace.csevent.ui.activity.BaseActivity;

/**
 * Created by Android on 2016/10/20.
 */
public class CrowdRuleAct extends BaseActivity{

    private LinearLayout ll_back;
    private boolean isOpen = true;
    private ImageView iv_open_close;
    private LinearLayout lin_record_content;

    private TextView tv_win_time_min;
    private TextView tv_num_min;
    private TextView tv_user_min;

    private TextView tv_win_time_max;
    private TextView tv_num_max;
    private TextView tv_user_max;

    private TextView tv_a_num;
    private TextView tv_b_win_num;

    private TextView tv_time_issue;

    private TextView tv_luck_num;

    private Intent intent;

    private PublishBean publishBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_crowd_rule);

        intent = getIntent();
        if (intent != null){
            publishBean = (PublishBean) intent.getSerializableExtra("publishBean");
        }
        initView();
    }

    private void initView() {

        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(clickListener);

        iv_open_close = (ImageView) findViewById(R.id.iv_open_close);
        iv_open_close.setOnClickListener(clickListener);

        lin_record_content = (LinearLayout) findViewById(R.id.lin_record_content);

        tv_win_time_min = (TextView) findViewById(R.id.tv_win_time_min);
        tv_num_min = (TextView) findViewById(R.id.tv_num_min);
        tv_user_min = (TextView) findViewById(R.id.tv_user_min);

        tv_win_time_max = (TextView) findViewById(R.id.tv_win_time_max);
        tv_num_max = (TextView) findViewById(R.id.tv_num_max);
        tv_user_max = (TextView) findViewById(R.id.tv_user_max);

        tv_a_num = (TextView) findViewById(R.id.tv_a_num);
        tv_b_win_num = (TextView) findViewById(R.id.tv_b_win_num);

        tv_time_issue = (TextView) findViewById(R.id.tv_time_issue);
        tv_luck_num = (TextView) findViewById(R.id.tv_luck_num);

        tv_win_time_min.setText(publishBean.getNumber_min());
        int lengthA = publishBean.getNumber_min().length();
        long a = Long.valueOf(publishBean.getNumber_min().substring(lengthA - 8 , lengthA));
        tv_num_min.setText( "——>" + a);


        int lengthB = publishBean.getNumber_max().length();
        long b = Long.valueOf(publishBean.getNumber_max().substring(lengthB - 8 , lengthB));
        tv_win_time_max.setText(publishBean.getNumber_max());
        tv_num_max.setText(" ——>" + b);

        tv_a_num.setText(a+b+ "");

        tv_b_win_num.setText(publishBean.getLottery_num());
        tv_luck_num.setText(publishBean.getLottery_num());

        long winTime = TimeHelper.getStringToDate(publishBean.getWintime());
        String issue = TimeHelper.getDateString2(String.valueOf(winTime)).substring(0 , 9);

        tv_time_issue.setText("第("+issue + ")期");

    }


    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_back:
                    finish();
                    break;
                case R.id.iv_open_close:
                    if (isOpen){
                        isOpen = false;
                        lin_record_content.setVisibility(View.GONE);
                    }else{
                        isOpen = true;
                        lin_record_content.setVisibility(View.VISIBLE);
                    }
                    break;

            }
        }
    };

}
