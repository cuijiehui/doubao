package com.xinspace.csevent.sweepstake.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.data.biz.AddressManagerBiz;
import com.xinspace.csevent.data.biz.AwardRecordDetailBiz;
import com.xinspace.csevent.data.biz.ModifyAwardAddressBiz;
import com.xinspace.csevent.data.entity.AwardsRecordDetailEntity;
import com.xinspace.csevent.data.entity.AwardsRecordEntity;
import com.xinspace.csevent.data.entity.GetAddressEntity;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.parser.JsonPaser;
import com.xinspace.csevent.util.parser.JsonPaser2;
import com.xinspace.csevent.sweepstake.weiget.ReceivSucDialog;
import com.xinspace.csevent.util.ImagerLoaderUtil;
import com.xinspace.csevent.util.ToastUtil;
import com.xinspace.csevent.ui.activity.AddressManagerActivity;
import com.xinspace.csevent.ui.activity.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android on 2016/9/28.
 */
public class PrizeDetailAct extends BaseActivity {

    private Intent intent;
    private AwardsRecordEntity enty;
    private ImageView iv_back, img_prize;
    private TextView tv_prize_title;
    private TextView tv_prize_issue;
    private TextView tv_prize_value;
    private TextView tv_num;
    private TextView tv_announce_time;

    private ImageView img_prize_state;
    private TextView tv_hasprize_time;

    private RelativeLayout rel_address_no;
    private RelativeLayout rel_address_yes;

    private ImageView img_prize_payout;
    private TextView tv_payout_state;
    private ImageView img_receiving;
    private TextView tv_receiving;
    private ImageView img_sing_in;
    private TextView tv_sing_in;
    private TextView tv_has_address;

    private RelativeLayout rel_prize_payout;
    private RelativeLayout rel_award_receiving;
    private RelativeLayout rel_sing_in_right;
    private RelativeLayout rel_has_address;

    private TextView tv_name;
    private TextView tv_phonenum;
    private TextView tv_address;
    private TextView tv_other;
    private Button tv_yes;

    private TextView tv_receiving_ok;

    private List<Object> list;
    private List<GetAddressEntity> addresssList;
    private GetAddressEntity getAddressEntity;
    private GetAddressEntity getAddressEntity1;

    private AwardsRecordDetailEntity awardsRecordDetailEntity;

    private String id;
    private String aid;

    private LinearLayout lin_address_msg;
    private LinearLayout lin_logistics_msg;

    private TextView tv_consignee_name;
    private TextView tv_consignee_address;

    private TextView tv_award_name;
    private TextView tv_logistics_name;
    private TextView tv_logistics_num;

    private TextView tv_receiving_time;

    private ReceivSucDialog receivSucDialog;

    private String match;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    getAddressEntity1 = (GetAddressEntity) msg.obj;
                    tv_name.setText(getAddressEntity1.getRealname());
                    tv_phonenum.setText(getAddressEntity1.getMobile());
                    tv_address.setText(getAddressEntity1.getProvince() + getAddressEntity1.getCity()
                            + getAddressEntity1.getArea() + getAddressEntity1.getAddress());
                    break;
                case 100: //确认收货地址返回
                    getAwardRecord();
                    break;
                case 101: //确认收货
                    getAwardRecord();
                    break;
                case 200:
                    AwardsRecordDetailEntity awardsRecordDetailEntity = (AwardsRecordDetailEntity) msg.obj;

                    ImagerLoaderUtil.displayImage(awardsRecordDetailEntity.getImage(), img_prize);
                    tv_prize_title.setText(awardsRecordDetailEntity.getPname());//设置获得名称
                    tv_announce_time.setText("揭晓时间："+awardsRecordDetailEntity.getStartdate());//设置揭晓时间
                    tv_num.setText("参与次数: " + match + "次");//设置参与次数
                    tv_prize_value.setText("价值：¥" + awardsRecordDetailEntity.getPrice());
                    tv_prize_issue.setText("期号: " + awardsRecordDetailEntity.getNoactivity());
                    tv_hasprize_time.setText("     获得奖品   " + awardsRecordDetailEntity.getWintime());

                    if (awardsRecordDetailEntity.getUser_confirm().equals("0")) {
                        //用户没有确认地址
                        rel_address_no.setVisibility(View.VISIBLE);
                        rel_address_yes.setVisibility(View.GONE);
                    } else if (awardsRecordDetailEntity.getUser_confirm().equals("1")) {
                        //用户确认地址
                        rel_address_no.setVisibility(View.GONE);
                        rel_address_yes.setVisibility(View.VISIBLE);
                        lin_address_msg.setVisibility(View.VISIBLE);
                        tv_has_address.setText(awardsRecordDetailEntity.getConfirmation_time());
                        rel_prize_payout.setBackgroundResource(R.drawable.icon_has_ok);
                        tv_consignee_name.setText(awardsRecordDetailEntity.getUname());
                        tv_consignee_address.setText(awardsRecordDetailEntity.getUaddress());

                        if (awardsRecordDetailEntity.getStart().equals("0")) {
                            //没发货
                            img_prize_payout.setImageResource(R.drawable.icon_payout_no);
                            tv_payout_state.setVisibility(View.VISIBLE);
                            tv_payout_state.setText("待派发....");
                            tv_payout_state.setTextColor(Color.parseColor("#de6350"));
                        } else if (awardsRecordDetailEntity.getStart().equals("1")) {
                            //发货
                            tv_payout_state.setText("已派发");
                            img_prize_payout.setImageResource(R.drawable.icon_payout_yes);
                            rel_award_receiving.setBackgroundResource(R.drawable.icon_has_ok);

                            lin_logistics_msg.setVisibility(View.VISIBLE);
                            tv_award_name.setText(awardsRecordDetailEntity.getPname());
                            tv_logistics_name.setText(awardsRecordDetailEntity.getDname());
                            tv_logistics_num.setText(awardsRecordDetailEntity.getNumber());
                            if (awardsRecordDetailEntity.getConfirm().equals("0")) {
                                //没确认收货
                                img_receiving.setImageResource(R.drawable.icon_receiving_no);
                                tv_receiving_ok.setVisibility(View.VISIBLE);
                            } else if (awardsRecordDetailEntity.getConfirm().equals("1")) {
                                //确认收货
                                tv_receiving_ok.setVisibility(View.GONE);
                                tv_receiving_time.setVisibility(View.VISIBLE);
                                tv_receiving_time.setText(awardsRecordDetailEntity.getReceipt_time());
                                img_receiving.setImageResource(R.drawable.icon_receiving_yes);
                                img_sing_in.setImageResource(R.drawable.icon_sign_in_no);
                                rel_sing_in_right.setBackgroundResource(R.drawable.icon_has_ok);
                            }
                        }
                    }
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_prizedetail);

        intent = getIntent();
        if (intent != null) {
            String flag = intent.getStringExtra("flag") ;
            if (flag != null && flag.equals("list")){
                enty = (AwardsRecordEntity) intent.getSerializableExtra("bean");
                aid = enty.getAid();
                id = enty.getId();
                match = enty.getMatch();
            }else if (flag != null && flag.equals("detail")){
                aid = intent.getStringExtra("id");
                id = intent.getStringExtra("deliveryid");
                match = intent.getStringExtra("match");
            }
        }
        initData();
        initView();
    }

    private void initData() {
        getAwardRecord();
        getUserAddresses();
    }

    private void initView() {

        iv_back = (ImageView) findViewById(R.id.iv_back);
        img_prize = (ImageView) findViewById(R.id.img_prize);
        iv_back.setOnClickListener(clickListener);

        tv_prize_title = (TextView) findViewById(R.id.tv_prize_title);
        tv_prize_issue = (TextView) findViewById(R.id.tv_prize_issue);
        tv_prize_value = (TextView) findViewById(R.id.tv_prize_value);
        tv_num = (TextView) findViewById(R.id.tv_num);
        tv_announce_time = (TextView) findViewById(R.id.tv_announce_time);

        img_prize_state = (ImageView) findViewById(R.id.img_prize_state);
        tv_hasprize_time = (TextView) findViewById(R.id.tv_hasprize_time);

        img_prize_payout = (ImageView) findViewById(R.id.img_prize_payout);
        tv_payout_state = (TextView) findViewById(R.id.tv_payout_state);

        img_receiving = (ImageView) findViewById(R.id.img_receiving);
        tv_receiving = (TextView) findViewById(R.id.tv_receiving);

        img_sing_in = (ImageView) findViewById(R.id.img_sing_in);
        tv_sing_in = (TextView) findViewById(R.id.tv_sing_in);

        tv_has_address = (TextView) findViewById(R.id.tv_has_address);

        tv_name = (TextView) findViewById(R.id.tv_name); // 收货人
        tv_phonenum = (TextView) findViewById(R.id.tv_phonenum); // 收货人号码
        tv_address = (TextView) findViewById(R.id.tv_address);   // 收货人地址

        tv_other = (TextView) findViewById(R.id.tv_other);
        tv_yes = (Button) findViewById(R.id.tv_yes_yes);

        tv_other.setOnClickListener(clickListener);
        tv_yes.setOnClickListener(clickListener);

        rel_prize_payout = (RelativeLayout) findViewById(R.id.rel_prize_payout);
        rel_award_receiving = (RelativeLayout) findViewById(R.id.rel_award_receiving);
        rel_sing_in_right = (RelativeLayout) findViewById(R.id.rel_sing_in_right);
        rel_has_address = (RelativeLayout) findViewById(R.id.rel_has_address);

        rel_address_yes = (RelativeLayout) findViewById(R.id.rel_address_yes);
        rel_address_no = (RelativeLayout) findViewById(R.id.rel_address_no);

        tv_receiving_ok = (TextView) findViewById(R.id.tv_receiving_ok);
        tv_receiving_ok.setOnClickListener(clickListener);

        lin_address_msg = (LinearLayout) findViewById(R.id.lin_address_msg);
        lin_logistics_msg = (LinearLayout) findViewById(R.id.lin_logistics_msg);

        tv_consignee_name = (TextView) findViewById(R.id.tv_consignee_name);
        tv_consignee_address = (TextView) findViewById(R.id.tv_consignee_address);

        tv_award_name = (TextView) findViewById(R.id.tv_award_name);
        tv_logistics_name = (TextView) findViewById(R.id.tv_logistics_name);
        tv_logistics_num = (TextView) findViewById(R.id.tv_logistics_num);

        tv_receiving_time = (TextView) findViewById(R.id.tv_receiving_time);


    }


    public void getUserAddresses() {
        AddressManagerBiz.getAddressList("" , new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                handleAddAddressResult(result);
                Log.i("www", "查询地址的" + result);
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }


    //处理用户地址返回的结果
    private void handleAddAddressResult(String result) {
        list = JsonPaser2.parserAry(result, GetAddressEntity.class, "addresslist");
        //显示地址
        if (list.size() != 0 && list.get(0) instanceof GetAddressEntity) {
            addresssList = new ArrayList<>();
            for (Object obj : list) {
                addresssList.add((GetAddressEntity) obj);
            }

            if (list.size() == addresssList.size()) {
                for (int i = 0; i < addresssList.size(); i++) {
                    if (addresssList.get(i).getIsdefault().equals("1")) {
                        getAddressEntity = addresssList.get(i);
                        handler.obtainMessage(1, getAddressEntity).sendToTarget();
                        return;
                    } else {
                        getAddressEntity = addresssList.get(0);
                        handler.obtainMessage(1, getAddressEntity).sendToTarget();
                    }
                }
            }
        }
    }


    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_back:
                    finish();
                    break;
                case R.id.tv_other:  //切换其他地址
                    Intent intent = new Intent(PrizeDetailAct.this, AddressManagerActivity.class);
                    intent.putExtra("flag" , "detail");
                    startActivityForResult(intent, 1000);
                    break;
                case R.id.tv_yes_yes:  //地址确认
                    getAddressstate();
                    break;
                case R.id.tv_receiving_ok: //确认收货
                    receivingOk();
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            switch (resultCode) {
                case 1000:   // 点击地址列表 回传地址
                    handler.obtainMessage(1, data.getSerializableExtra("getAddressEntity")).sendToTarget();
                    break;
            }
        }
    }

    //获取中奖记录详细信息
    private void getAwardRecord() {
        AwardRecordDetailBiz.getRecordDetail2(id, aid, "delivery_order_id", new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                Log.i("www", "获取中奖记录详细信息" + result);
                showRecordDetail(result);
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }


    private void showRecordDetail(String result) {
        try {
            JSONObject json = new JSONObject(result);
            switch (json.getString("result")) {
                case "200":
                    String data = json.getString("data");
                    awardsRecordDetailEntity = (AwardsRecordDetailEntity) JsonPaser.parserObj(data, AwardsRecordDetailEntity.class);
                    handler.obtainMessage(200, awardsRecordDetailEntity).sendToTarget();
                    break;
                case "202":
                    ToastUtil.makeToast(json.getString("msg"));
                    break;
                case "102":
                    ToastUtil.makeToast("没有中奖纪录!");
                    break;
                case "300":
                    ToastUtil.makeToast("已经确认送单,请耐心等待!");
                    break;
                case "302":
                    ToastUtil.makeToast(json.getString("msg"));
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void getAddressstate() {
        ModifyAwardAddressBiz.affirmAddress(PrizeDetailAct.this, id, getAddressEntity1, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                Log.i("www", "点击确认地址的返回" + result);
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.has("result")) {
                    if (jsonObject.getString("result").equals("200")) {
                        handler.obtainMessage(100).sendToTarget();
                    } else {

                    }
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }

    private void receivingOk() {
        AwardRecordDetailBiz.sureDelivery2(id, "delivery_order_id", new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.has("result")) {
                    if (jsonObject.getString("result").equals("300")) {
                        showDialog();
                        handler.obtainMessage(101).sendToTarget();
                    } else {

                    }
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }

    //确认收货成功
    private void showDialog(){
        receivSucDialog = new ReceivSucDialog(this);
        receivSucDialog.setCanceledOnTouchOutside(false);
        //确定
        receivSucDialog.setOnPositiveListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                receivSucDialog.dismiss();
            }
        });
        receivSucDialog.show();
    }

}
