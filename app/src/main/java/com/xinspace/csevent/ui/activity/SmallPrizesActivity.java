package com.xinspace.csevent.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.xinspace.csevent.R;
import com.xinspace.csevent.data.entity.SmallPrizeEntity;
import com.xinspace.csevent.util.SharedPreferencesUtil1;

/**
 * 第一次安装应用时抽取小奖品页面
 */
public class SmallPrizesActivity extends BaseActivity{

    private LinearLayout ll_back;
    private TextView tvIntegral;
    private ImageView ivGet;
    private SmallPrizeEntity enty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_small_prizes);
        setViews();
        setListeners();

        getData();
    }
    //获取上个页面返回的数据
    private void getData() {
        Intent intent=getIntent();
        enty= (SmallPrizeEntity) intent.getSerializableExtra("data");

        tvIntegral.setText(enty.getPname());
    }
    //设置监听器
    private void setListeners() {
        //退出
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SmallPrizesActivity.this,FullPageAdsActivity.class));
                finish();
            }
        });
        //获取积分
        ivGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIntegral();
            }
        });
    }
    //获取积分
    private void getIntegral() {
        //将小奖品的信息保存至本地
        SharedPreferencesUtil1.saveString(this,"smallPrizeId",enty.getId());
        startActivity(new Intent(this,FullPageAdsActivity.class));
        finish();
    }
    //初始化组件
    private void setViews() {
        ll_back= (LinearLayout) findViewById(R.id.ll_small_prizes_back);

        tvIntegral= (TextView) findViewById(R.id.tv_small_prizes_integral);
        ivGet= (ImageView) findViewById(R.id.iv_small_prize_get_integral);
    }
}

