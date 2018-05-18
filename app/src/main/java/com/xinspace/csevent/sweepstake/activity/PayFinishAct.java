package com.xinspace.csevent.sweepstake.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.app.utils.TimeHelper;
import com.xinspace.csevent.ui.activity.BaseActivity;

/**
 * Created by Android on 2016/10/11.
 *
 * 购买支付完成界面
 */
public class PayFinishAct extends BaseActivity{

    private TextView tv_continue_buy;

    private TextView tv_buy_record;

    private TextView tv_buy_count;

    private TextView tv_goods_name;

    private TextView tv_pay_time;

    private TextView tv_goods_price;

    private ImageView iv_pay_finish;

    private TextView tv_pay_finish;

    private Intent intent;

    private String buyCount;
    private String totlaPrice;
    private String goodsName;

    private LinearLayout ll_back;

    private String flag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_payfinish);
        intent = getIntent();
        if (intent != null){
            goodsName = intent.getStringExtra("goodsName");
            totlaPrice = intent.getStringExtra("totlaPrice");
            buyCount = intent.getStringExtra("buyCount");
            flag = intent.getStringExtra("flag");
        }
        initView();
    }

    private void initView() {

        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(clickListener);

        tv_continue_buy = (TextView) findViewById(R.id.tv_continue_buy);
        tv_buy_record = (TextView) findViewById(R.id.tv_buy_record);
        tv_continue_buy.setOnClickListener(clickListener);
        tv_buy_record.setOnClickListener(clickListener);


        iv_pay_finish = (ImageView) findViewById(R.id.iv_pay_finish);
        tv_pay_finish = (TextView) findViewById(R.id.tv_pay_finish);

        tv_buy_count = (TextView) findViewById(R.id.tv_buy_count);
        tv_goods_name = (TextView) findViewById(R.id.tv_goods_name);
        tv_pay_time = (TextView) findViewById(R.id.tv_pay_time);
        tv_goods_price = (TextView) findViewById(R.id.tv_goods_price);

        if (flag.equals("success")){
            tv_buy_count.setText(buyCount);
            tv_goods_name.setText(goodsName);
            tv_goods_price.setText("商品价值： " + totlaPrice);
            tv_pay_finish.setText("购买成功");
            iv_pay_finish.setBackgroundResource(R.drawable.icon_pay_finish);
        }else if (flag.equals("fail")){
            iv_pay_finish.setBackgroundResource(R.drawable.icon_pay_fail);
            tv_pay_finish.setText("购买失败");
            tv_buy_count.setText(buyCount);
            tv_goods_name.setText(goodsName);
            tv_goods_price.setText("商品价值： " + totlaPrice);
        }


        long time = System.currentTimeMillis();
        tv_pay_time.setText("购买时间： " + TimeHelper.getDateString(String.valueOf(time)));

    }


    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_continue_buy: // 继续购买
                    finish();
                    break;
                case R.id.tv_buy_record: //  购买记录

                    break;
                case R.id.ll_back:
                    finish();
                    break;
            }
        }
    };

}
