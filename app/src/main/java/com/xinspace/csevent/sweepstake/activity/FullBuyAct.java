package com.xinspace.csevent.sweepstake.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.data.biz.AddressManagerBiz;
import com.xinspace.csevent.data.entity.CrowdActEntity;
import com.xinspace.csevent.data.entity.GetAddressEntity;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.parser.JsonPaser2;
import com.xinspace.csevent.util.ImagerLoaderUtil;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.ToastUtil;
import com.xinspace.csevent.ui.activity.BaseActivity;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android on 2016/10/9.
 *
 * 全价购买
 */
public class FullBuyAct extends BaseActivity{

    private LinearLayout ll_back;
    private TextView tv_go_order;
    private TextView tv_all_prize;
    private Intent intent;
    private CrowdActEntity crowdActEntity;

    private EditText et_detail_count;
    private int count = 1;
    private TextView tv_detail_reduce ;
    private TextView tv_detail_add;

    private Float prize;
    private String imgUrl;

    private ImageView iv_goods_image;

    private TextView tv_goods_name;
    private TextView tv_goods_prize;

    private Float allPrice;

    private RelativeLayout rel_no_address;

    private RelativeLayout rel_has_address;

    private List<Object> list;
    private List<GetAddressEntity> addresssList;
    private GetAddressEntity getAddressEntity;
    private GetAddressEntity getAddressEntity1;

    private TextView tv_user_name;
    private TextView tv_user_phone;
    private TextView tv_user_address;

    private ImageView iv_img_go;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String detailAddaress;
            switch (msg.what){
                case 1: //直接有地址
                    getAddressEntity1 = (GetAddressEntity) msg.obj;
                    tv_user_name.setText(getAddressEntity1.getRealname());
                    tv_user_phone.setText(getAddressEntity1.getMobile());
                    detailAddaress =  getAddressEntity1.getProvince() + getAddressEntity1.getCity()
                            + getAddressEntity1.getArea() + getAddressEntity1.getAddress();
                    tv_user_address.setText(detailAddaress);
                    break;
                case 2:  // 添加地址
                    getAddressEntity1 = (GetAddressEntity) msg.obj;
                    tv_user_name.setText(getAddressEntity1.getRealname());
                    tv_user_phone.setText(getAddressEntity1.getMobile());
                    detailAddaress =  getAddressEntity1.getProvince() + getAddressEntity1.getCity()
                            + getAddressEntity1.getArea() + getAddressEntity1.getAddress();
                    tv_user_address.setText(detailAddaress);
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_fullbuy);

        intent = getIntent();
        if (intent != null){
            crowdActEntity = (CrowdActEntity) intent.getSerializableExtra("data");
            String onePrize = String.valueOf((Float.valueOf(crowdActEntity.getDiscount()) * Float.valueOf(crowdActEntity.getPrice())) /10) ;
            int one = onePrize.indexOf(".");
            String twoPrize;

            LogUtil.i("价格" + one);

            if (onePrize.length() > (one + 2)){
                twoPrize = onePrize.substring(0 , one + 3);
            }else{
                twoPrize = onePrize.substring(0 , one + 2) + 0;
            }

            LogUtil.i("twoPrize" + twoPrize);

            prize = Float.valueOf(twoPrize);
            imgUrl = crowdActEntity.getImg();
        }
        initView();
        getUserAddresses();
    }

    private void initView() {
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(onClickListener);

        tv_go_order = (TextView) findViewById(R.id.tv_go_order);
        tv_go_order.setOnClickListener(onClickListener);
        tv_all_prize = (TextView) findViewById(R.id.tv_all_prize);

        tv_detail_reduce = (TextView) findViewById(R.id.tv_detail_reduce);
        tv_detail_reduce.setOnClickListener(onClickListener);
        tv_detail_add = (TextView) findViewById(R.id.tv_detail_add);
        tv_detail_add.setOnClickListener(onClickListener);
        et_detail_count = (EditText) findViewById(R.id.et_detail_count);

        iv_goods_image = (ImageView) findViewById(R.id.iv_goods_image);
        ImagerLoaderUtil.displayImage(imgUrl , iv_goods_image);

        tv_goods_name = (TextView) findViewById(R.id.tv_goods_name);
        tv_goods_name.setText(crowdActEntity.getName());

        tv_goods_prize = (TextView) findViewById(R.id.tv_goods_prize);
        //tv_goods_prize.setText("¥" + prize);
        tv_goods_prize.setText("¥" + prize);

        allPrice = count * (prize);
        tv_all_prize.setText("¥" + allPrice);

        rel_no_address = (RelativeLayout) findViewById(R.id.rel_no_address);
        rel_no_address.setOnClickListener(onClickListener);

        rel_has_address = (RelativeLayout) findViewById(R.id.rel_has_address);

        tv_user_name = (TextView) findViewById(R.id.tv_user_name);
        tv_user_phone = (TextView) findViewById(R.id.tv_user_phone);
        tv_user_address = (TextView) findViewById(R.id.tv_user_address);

        iv_img_go = (ImageView) findViewById(R.id.iv_img_go);
        iv_img_go.setOnClickListener(onClickListener);
    }


    public void getUserAddresses() {
        AddressManagerBiz.getAddressList(  "" ,new HttpRequestListener() {
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
            rel_no_address.setVisibility(View.GONE);
            rel_has_address.setVisibility(View.VISIBLE);
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
        }else{
            rel_no_address.setVisibility(View.VISIBLE);
        }
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_back:
                    finish();
                    break;
                case R.id.tv_go_order:

                    LogUtil.i("111111111111111111111111");
                    if (rel_has_address.getVisibility() == View.VISIBLE){
                        if (tv_user_name.getText() != null && !tv_user_name.getText().equals("")){
                            Intent intent = new Intent(FullBuyAct.this , PayOrderAct.class);
                            intent.putExtra("flag" , "full");
                            intent.putExtra("address" , getAddressEntity1);
                            intent.putExtra("goodsName" , crowdActEntity.getName());
                            intent.putExtra("allPrice" ,  allPrice + "");
                            intent.putExtra("actId" , crowdActEntity.getId());
                            intent.putExtra("buyCount" , count + "");
                            intent.putExtra("issue" , crowdActEntity.getNoactivity());
                            startActivity(intent);
                        }else{
                            ToastUtil.makeToast("收货地址不能为空");
                        }
                    }else{
                        ToastUtil.makeToast("收货地址不能为空");
                    }
                    break;
                case R.id.tv_detail_reduce:
                    if (count == 1){
                        ToastUtil.makeToast("至少选择一件");
                        allPrice = count * prize;
                        tv_all_prize.setText("¥" + allPrice);
                    }else{
                        count--;
                        et_detail_count.setText(count + "");
                        allPrice = count * prize;
                        tv_all_prize.setText("¥" + allPrice);
                    }
                    break;
                case R.id.tv_detail_add:
                    count ++;
                    et_detail_count.setText(count + "");
                    allPrice = count * prize;
                    tv_all_prize.setText("¥" + allPrice);
                    break;
                case R.id.rel_no_address:
                    Intent intent = new Intent(FullBuyAct.this , ChangeAddressAct.class);
                    startActivityForResult(intent , 100);
                    break;
                case R.id.iv_img_go:
                    Intent intent2 = new Intent(FullBuyAct.this , ChangeAddressAct.class);
                    startActivityForResult(intent2 , 100);
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100){
            switch (resultCode){
                case 1000:
                    rel_no_address.setVisibility(View.GONE);
                    rel_has_address.setVisibility(View.VISIBLE);
                    GetAddressEntity getAddressEntity = (GetAddressEntity) data.getSerializableExtra("getAddressEntity");
                    if (getAddressEntity != null){
                        handler.obtainMessage(2, getAddressEntity).sendToTarget();
                    }
                    break;
            }
        }
    }
}
