package com.xinspace.csevent.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.data.biz.AwardRecordDetailBiz;
import com.xinspace.csevent.data.entity.AwardsRecordDetailEntity;
import com.xinspace.csevent.login.activity.FeedbackActivity;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.parser.JsonPaser;
import com.xinspace.csevent.util.Const;
import com.xinspace.csevent.util.ImagerLoaderUtil;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

/***
 * 中奖记录详情页面
 */
public class AwardRecordDetailActivity extends BaseActivity{

    private LinearLayout llBack;
    private Button btModify;
    private String id;
    private String type;
    private TextView tvAddAddress;
    private LinearLayout llAddress;
    private ImageView ivImage;
    private TextView tvPname;
    private TextView tvNumber;
    private TextView tvTime;
    private TextView tvAttendNum;
    private TextView tvUname;
    private TextView tvUtel;
    private TextView tvUaddress;
    private TextView tvState;
    private DetailBroadcastReceiver receiver;
    private AwardsRecordDetailEntity enty;
    private Button btSend;
    private Button btFeedback;
    private static final int REQUEST_CODE_EDIT_ADDRESS = 100;//修改地址

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_award_record_detail);

        getData();
        setViews();
        setListeners();
        getAwardRecord();
        //注册广播
        receiver=new DetailBroadcastReceiver();
        registerReceiver(receiver, new IntentFilter(Const.ACTION_CHANGE_TYPE_AND_ID));
    }
    //获取上个页面传递过来的数据
    private void getData() {
        Intent intent=getIntent();
        id=intent.getStringExtra("id");//纪录表中的id
        type=intent.getStringExtra("type");
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //刷新数据
        getAwardRecord();
    }
    //获取中奖记录详细信息
    private void getAwardRecord() {
        AwardRecordDetailBiz.getRecordDetail(id, type, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                showRecordDetail(result);
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }
    //显示中奖记录信息
    private void showRecordDetail(String result) {
        try {
            JSONObject json = new JSONObject(result);
            switch (json.getString("result")){
                case "200":
                    String data=json.getString("data");
                    enty=(AwardsRecordDetailEntity)JsonPaser.parserObj(data, AwardsRecordDetailEntity.class);
                    ImagerLoaderUtil.displayImage(enty.getImage(), ivImage);
                    tvPname.setText(enty.getPname());
                    tvNumber.setText(enty.getPrice()+"元");
                    tvTime.setText(enty.getStartdate());
                    tvAttendNum.setText(enty.getNum());
                    tvUname.setText(enty.getUname());
                    tvUtel.setText(enty.getUtel());
                    tvUaddress.setText(enty.getUaddress());
                    showViews();
                    //当没有地址时,隐藏确定派送按钮
                    if("registration_id".equals(enty.getType())){
                        btSend.setVisibility(View.INVISIBLE);
                    }else{
                        btSend.setVisibility(View.VISIBLE);
                    }
                    //除商品外都不可更改发货地址，积分和金币自动发货
                    if (!"product".equals(enty.getCtype())){//非商品时(即金币或积分),需要隐藏按钮及显示发货状态
                        btSend.setVisibility(View.INVISIBLE);
                        btModify.setVisibility(View.INVISIBLE);
                        tvState.setTextColor(getResources().getColor(R.color.color_login_background));
                        tvState.setText("奖品已派送");
                    }
                    //用户确认送单的奖品纪录也需要隐藏修改地址及确认派送按钮
                    if ("1".equals(enty.getUser_confirm())){//1,已经确认,隐藏
                        btSend.setVisibility(View.INVISIBLE);
                        btModify.setVisibility(View.INVISIBLE);
                    }else{//0, 未确认,显示
                        btSend.setVisibility(View.VISIBLE);
                        btModify.setVisibility(View.VISIBLE);
                    }
                    if ("0".equals(enty.getStart())){
                        if(enty.getUser_confirm().equals("1")){
                            //用户已经确认
                            tvState.setTextColor(getResources().getColor(R.color.color_deep_red));
                            tvState.setText("等待派送中");
                        }else{
                            //用户未确认
                            tvState.setTextColor(getResources().getColor(R.color.color_deep_red));
                            tvState.setText("奖品未派送");
                        }
                    }else{
                        tvState.setTextColor(getResources().getColor(R.color.color_login_background));
                        tvState.setText("奖品已派送");
                    }
                    break;
                case "202":
                    ToastUtil.makeToast(json.getString("msg"));
                    break;
                case "102":
                    ToastUtil.makeToast("没有中奖纪录!");
                    break;
                case "300":
                    ToastUtil.makeToast("已经确认送单,请耐心等待!");
                    btSend.setVisibility(View.INVISIBLE);
                    btModify.setVisibility(View.INVISIBLE);
                    break;
                case "302":
                    ToastUtil.makeToast(json.getString("msg"));
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void setListeners() {
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //问题反馈
        btFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AwardRecordDetailActivity.this, FeedbackActivity.class);
                startActivity(intent);
            }
        });
        //修改地址
        btModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AwardRecordDetailActivity.this, ModifyAwardAddressActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("type", type);
                intent.putExtra("pname", tvPname.getText().toString());
                startActivityForResult(intent,REQUEST_CODE_EDIT_ADDRESS);
            }
        });
        //点击确认派送
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tvAddAddress.getVisibility()==View.VISIBLE){
                    ToastUtil.makeToast("请先添加收货地址");
                    return;
                }
                AwardRecordDetailBiz.sureDelivery(id, enty.getType(), new HttpRequestListener() {
                    @Override
                    public void onHttpRequestFinish(String result) throws JSONException {
                        LogUtil.i("确认派送.............."+result);
                        showRecordDetail(result);
                        getAwardRecord();
                    }

                    @Override
                    public void onHttpRequestError(String error) {

                    }
                });
            }
        });
    }
    //初始化组件
    private void setViews() {
        llBack=(LinearLayout)findViewById(R.id.ll_award_detail_back);
        tvAddAddress=(TextView)findViewById(R.id.tv_add_address);
        llAddress=(LinearLayout)findViewById(R.id.ll_award_record_detail_address);
        btModify=(Button)findViewById(R.id.bt_award_record_modify_address);
        btSend=(Button)findViewById(R.id.bt_award_record_detail_sure_send);
        btFeedback=(Button)findViewById(R.id.bt_award_record_detail_feedback);

        ivImage=(ImageView)findViewById(R.id.iv_awards_record_detail_image);
        tvPname=(TextView)findViewById(R.id.tv_awards_record_detail_pname);
        tvNumber=(TextView)findViewById(R.id.tv_awards_record_detail_number);
        tvTime=(TextView)findViewById(R.id.tv_awards_record_detail_time);
        tvAttendNum=(TextView)findViewById(R.id.tv_awards_record_detail_attend_num);
        tvUname=(TextView)findViewById(R.id.tv_awards_record_detail_uname);
        tvUtel=(TextView)findViewById(R.id.tv_awards_record_detail_utel);
        tvUaddress=(TextView)findViewById(R.id.tv_awards_record_detail_uaddress);
        tvState=(TextView)findViewById(R.id.tv_awards_record_detail_state);

        showViews();
    }
    /**控制控件显示*/
    private void showViews() {
        switch (type){
            case "delivery_order_id"://已有地址的用户
                tvAddAddress.setVisibility(View.GONE);
                llAddress.setVisibility(View.VISIBLE);
                btModify.setText("修改收货地址");
                break;
            case "registration_id"://没有地址的用户
                tvAddAddress.setVisibility(View.VISIBLE);
                llAddress.setVisibility(View.GONE);
                btModify.setVisibility(View.VISIBLE);
                btModify.setText("添加地址");
                break;
            case "prizes_order_id"://小奖品修改地址
                LogUtil.i("小奖品地址:" + tvUaddress.getText().toString());
                if (tvUaddress.getText().toString().equals("")){//小奖品没有收货地址
                    tvAddAddress.setVisibility(View.VISIBLE);
                    llAddress.setVisibility(View.GONE);
                    btModify.setText("添加地址");
                }else {//小奖品有收货地址
                    tvAddAddress.setVisibility(View.GONE);
                    llAddress.setVisibility(View.VISIBLE);
                    btModify.setText("修改地址");
                }
                break;
        }
    }
    /**广播接收类
     * 接收添加地址后的奖品的新type,id
     * */
    class DetailBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            type=intent.getStringExtra("type");
            id = intent.getStringExtra("id");
            showViews();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
