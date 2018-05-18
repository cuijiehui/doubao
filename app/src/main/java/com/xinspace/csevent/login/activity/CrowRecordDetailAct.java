package com.xinspace.csevent.login.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.login.adapter.LuckNumAdapter;
import com.xinspace.csevent.login.model.CrowdRecord;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.StatusBarUtils;
import com.xinspace.csevent.ui.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Android on 2016/10/17.
 *
 * 抽奖记录详情
 *
 */
public class CrowRecordDetailAct extends BaseActivity {

    private LinearLayout ll_back;

    private Intent intent;

    private CrowdRecord crowdRecord;

    private String winNum;

    private String luckNum;

    private GridView gv_luck_num;

    private LuckNumAdapter adapter;

    private ImageView iv_open_close;

    private boolean isOpen = false;

    private TextView tv_duobao_record_name;

    private TextView tv_noactivity;

    private TextView tv_duobao_record_attend_num;

    private TextView tv_crowd_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(this , R.color.app_bottom_color);
        setContentView(R.layout.act_crowd_record_detail);

        intent = getIntent();
        if (intent != null){
            crowdRecord = (CrowdRecord) intent.getSerializableExtra("crowdRecord");
        }
        initView();

    }

    private void initView() {

        ll_back = (LinearLayout) findViewById(R.id.ll_award_detail_back);
        ll_back.setOnClickListener(onClickListener);

        winNum = crowdRecord.getLucky_number();
        luckNum = winNum.substring(1 , winNum.length());
        List<String> list = getStringList(luckNum);
        LogUtil.i("期号集合长度" + list.size());

        iv_open_close = (ImageView) findViewById(R.id.iv_open_close);
        iv_open_close.setImageResource(R.drawable.icon_open);
        iv_open_close.setOnClickListener(onClickListener);

        tv_duobao_record_name = (TextView) findViewById(R.id.tv_duobao_record_name);
        tv_noactivity = (TextView) findViewById(R.id.tv_noactivity);
        tv_duobao_record_attend_num = (TextView) findViewById(R.id.tv_duobao_record_attend_num);
        tv_crowd_time = (TextView) findViewById(R.id.tv_crowd_time);

        LogUtil.i("name" + crowdRecord.getCommodity_name() );

        tv_duobao_record_name.setText(crowdRecord.getCommodity_name());
        tv_duobao_record_attend_num.setText(crowdRecord.getMatch());
        tv_noactivity.setText("期号：" + crowdRecord.getNoactivity());
        tv_crowd_time.setText(crowdRecord.getParticipate());

        gv_luck_num = (GridView) findViewById(R.id.gv_luck_num);

        adapter = new LuckNumAdapter(this);
        adapter.setLuckNum(list);
        gv_luck_num.setAdapter(adapter);

    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_award_detail_back:
                    finish();
                    break;
                case R.id.iv_open_close:
                    if (isOpen){
                        isOpen = false;
                        iv_open_close.setImageResource(R.drawable.icon_close_g);
                        gv_luck_num.setVisibility(View.GONE);
                    }else{
                        isOpen = true;
                        iv_open_close.setImageResource(R.drawable.icon_open);
                        gv_luck_num.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }
    };

    /**
     *
     * @param str
     * @return
     */
    private List<String> getStringList(String str){
        List<String> list=new ArrayList<String>();
        StringTokenizer st=new StringTokenizer(str,",");
        while(st.hasMoreTokens()){
            list.add(st.nextToken());
        }
//        String[] arr = str.split(",");
//        List<String> list1 = java.util.Arrays.aslist(str);
        return list;
    }

}
