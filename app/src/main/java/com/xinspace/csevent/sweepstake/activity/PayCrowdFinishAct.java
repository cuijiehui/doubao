package com.xinspace.csevent.sweepstake.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.app.utils.TimeHelper;
import com.xinspace.csevent.data.biz.GetDataBiz;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.ui.activity.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Android on 2016/10/11.
 *
 * 众筹 购买支付完成界面
 */
public class PayCrowdFinishAct extends BaseActivity{

    private TextView tv_continue_buy;

    private TextView tv_buy_record;

    private TextView tv_buy_count;

    private TextView tv_goods_name;

    private TextView tv_pay_time;

    private TextView tv_goods_price;

    private ImageView iv_pay_finish;

    private Intent intent;

    private String buyCount;
    private String totlaPrice;
    private String goodsName;

    private LinearLayout ll_back;

    private String flag;

    private TextView tv_pay_finish;

    private TextView tv_state1;

    private TextView tv_state2;

    private LinearLayout lin_award_state_success;

    private TextView tv_award_state_fail;

    private String tag;

    private String actId;
    private String order_number1;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 12:

                    tv_state1.setVisibility(View.VISIBLE);
                    tv_state2.setVisibility(View.VISIBLE);
                    lin_award_state_success.setVisibility(View.GONE);
                    tv_award_state_fail.setVisibility(View.VISIBLE);

                    break;
                case 13:
                    iv_pay_finish.setImageResource(R.drawable.icon_pay_fail);
                    tv_pay_finish.setText("参与抽奖失败");
                    tv_state1.setVisibility(View.VISIBLE);
                    tv_state2.setVisibility(View.VISIBLE);
                    tv_state2.setText("我们会在1-5个工作日退回到您的原账户上");
                    lin_award_state_success.setVisibility(View.GONE);
                    tv_award_state_fail.setVisibility(View.VISIBLE);

                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_paycrowdfinish);
        intent = getIntent();
        if (intent != null){
            goodsName = intent.getStringExtra("goodsName");
            totlaPrice = intent.getStringExtra("totlaPrice");
            buyCount = intent.getStringExtra("buyCount");
            flag = intent.getStringExtra("flag");
            tag = intent.getStringExtra("tag");

            actId = intent.getStringExtra("actId");
            order_number1 = intent.getStringExtra("order_number1");

        }
        initView();
    }

    private void initView() {
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(clickListener);

        iv_pay_finish = (ImageView) findViewById(R.id.iv_pay_finish);
        tv_pay_finish = (TextView) findViewById(R.id.tv_pay_finish);

        lin_award_state_success = (LinearLayout) findViewById(R.id.lin_award_state_success);
        tv_award_state_fail = (TextView) findViewById(R.id.tv_award_state_fail);

        tv_state1 = (TextView) findViewById(R.id.tv_state1);
        tv_state2 = (TextView) findViewById(R.id.tv_state2);

        if (flag.equals("success")){
            iv_pay_finish.setImageResource(R.drawable.icon_pay_finish);
            tv_pay_finish.setText("参与抽奖成功");
        }else if (flag.equals("fail")){
            iv_pay_finish.setImageResource(R.drawable.icon_pay_fail);
            tv_pay_finish.setText("参与抽奖失败");
            tv_state1.setVisibility(View.VISIBLE);
            lin_award_state_success.setVisibility(View.GONE);
            tv_award_state_fail.setVisibility(View.VISIBLE);
        }else if (flag.equals("fail1")){ // 支付宝购买检测
            if (actId != null && order_number1 != null){
                getCheckPayState(actId , order_number1);
            }
        }

        tv_continue_buy = (TextView) findViewById(R.id.tv_continue_buy);
        tv_buy_record = (TextView) findViewById(R.id.tv_buy_record);
        tv_continue_buy.setOnClickListener(clickListener);
        tv_buy_record.setOnClickListener(clickListener);

        tv_buy_count = (TextView) findViewById(R.id.tv_buy_count);
        tv_goods_name = (TextView) findViewById(R.id.tv_goods_name);
        tv_pay_time = (TextView) findViewById(R.id.tv_pay_time);
        tv_goods_price = (TextView) findViewById(R.id.tv_goods_price);

        tv_buy_count.setText(buyCount);
        tv_goods_name.setText(goodsName);
        tv_goods_price.setText("商品价值： ¥" + Integer.valueOf(totlaPrice) / Integer.valueOf(buyCount));

        long time = System.currentTimeMillis();
        tv_pay_time.setText("购买时间： " + TimeHelper.getDateString(String.valueOf(time)));

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (flag.equals("fail1")){
            if (actId != null && order_number1 != null){
                getCheckPayState(actId , order_number1);
            }
        }
    }

    //检测微信支付宝是否购买成功
    public void getCheckPayState(String aid, String orderNum) {
        GetDataBiz.checkCrowdPayState( aid , orderNum, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                Log.i("www", "检测支付购买成功状态" + result);
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.has("state")) {
                    if (jsonObject.getString("state").equals("1")) {
                        //购买成功 跳转
                        handler.obtainMessage(12).sendToTarget();
                    }else{
                        handler.obtainMessage(13).sendToTarget();
                    }
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
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
