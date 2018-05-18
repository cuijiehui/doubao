package com.xinspace.csevent.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.baidu.mapapi.model.LatLng;
import com.xinspace.csevent.R;
import com.xinspace.csevent.data.entity.ResultForNoAddressAwardEntity;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.myinterface.SearchAddressFinishListener;
import com.xinspace.csevent.util.parser.JsonPaser;
import com.xinspace.csevent.util.BdMapUtil;
import com.xinspace.csevent.util.Const;
import com.xinspace.csevent.util.NetWorkStateUtil;
import com.xinspace.csevent.util.PhoneNumUtil;
import com.xinspace.csevent.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 此页面为中奖后添加新地址的页面
 * */
public class AddAddressForAwardActivity extends BaseActivity implements SearchAddressFinishListener,HttpRequestListener{

    private Button btAddAddress;
    private EditText etName;
    private EditText etphoneNum;
    private EditText etAddress;
    private BdMapUtil map;
    private String valueName;
    private String valuePhoneNum;
    private String valueAddress;
    private LinearLayout ll_back;
    private LinearLayout current_address;
    private String id;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address_for_award);

        setViews();
        setListeners();

        getData();
    }
    //获取上一个页面传递过来的数据
    private void getData() {
        Intent intent= getIntent();
        id=intent.getStringExtra("id");
        type=intent.getStringExtra("type");
    }

    private void setListeners() {
        //点击返回
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //点击添加新收货地址
        btAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (verify()) {
//                    if ("prizes_order_id".equals(type)){//小奖品添加新地址
//                        AddAddressForAwardBiz.addAddressForSmallPrize(AddAddressForAwardActivity.this,id,valueName,valuePhoneNum,valueAddress);
//                    }else {//普通奖品添加新地址
//                        AddAddressForAwardBiz.commitAddress(AddAddressForAwardActivity.this, id, valueName, "", valuePhoneNum, valueAddress);
//                    }
//                }
                if(verify()){
                    //检查网络状态
                    boolean is_available = NetWorkStateUtil.isNetworkAvailable(AddAddressForAwardActivity.this);
                    if(!is_available){
                        ToastUtil.makeToast("当前网络不可用");
                        return;
                    }
//                    AddressEditBiz.addAddress(valueAddress, valueName, valuePhoneNum, new HttpRequestListener() {
//                        @Override
//                        public void onHttpRequestFinish(String result) throws JSONException {
//                            handleAddAddressResult(result);
//                        }
//                    });
                }
            }
        });

        /**点击获取地址*/
        current_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取当前位置
                getLocation();
            }
        });

    }
    //处理增加地址的返回结果
    private void handleAddAddressResult(String result) {
        try {
            JSONObject object=new JSONObject(result);
            if(object.getString("result").equals("200")){
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //初始化组件
    private void setViews() {
        btAddAddress = (Button) findViewById(R.id.bt_address_edit_add_add);

        etName = (EditText) findViewById(R.id.et_address_edit_name);
        etphoneNum = (EditText) findViewById(R.id.et_address_edit_phone);
        etAddress = (EditText) findViewById(R.id.et_address_edit_address);

        ll_back= (LinearLayout) findViewById(R.id.ll_address_edit_back);
        current_address= (LinearLayout) findViewById(R.id.ll_get_current_address);
    }
    //获取当前位置
    private void getLocation() {
        map=new BdMapUtil();
        map.setOnAddressFinishListener(this);
        map.getLocation();
    }
    /**验证地址编辑输入格式是否正确*/
    private boolean verify () {
        valueName = etName.getText().toString();
        valuePhoneNum = etphoneNum.getText().toString();
        valueAddress = etAddress.getText().toString();

        if (TextUtils.isEmpty(valueName)) {
            ToastUtil.makeToast("联系人不能为空");
            return false;
        }
        if (TextUtils.isEmpty(valueAddress)) {
            ToastUtil.makeToast("地址不能为空");
            return false;
        }
        if (TextUtils.isEmpty(valuePhoneNum)||(!PhoneNumUtil.isPhoneNO(valuePhoneNum))) {
            ToastUtil.makeToast("请输入正确的联系电话");
            return false;
        }
        return true;
    }

    @Override
    public void onSearchFinish(LatLng latLng, String address, String province, String city, String district) {
        if(null==address){
            etAddress.setText("定位中,请稍后...");
        }else {
            if(address.contains("中国")){
                address=address.replace("中国","");
            }
            etAddress.setText(address);
        }
    }
    /**联网回调接口,返回json字符串*/
    @Override
    public void onHttpRequestFinish(String result) {
        if (result.contains("200")) {
            if (result.contains("id")) {
                ResultForNoAddressAwardEntity enty= (ResultForNoAddressAwardEntity) JsonPaser.parserObj(result, ResultForNoAddressAwardEntity.class);
                sendBroadToChangeTypeAndId(enty.getId());//把id发回给中奖记录详情
            }
            finish();
        }
    }

    @Override
    public void onHttpRequestError(String error) {

    }

    /**
     * 没有地址的奖品添加收货地址后发广播给中奖详情页改变此奖品插入新表的表名和新表的id
     * @param id 新记录表的id
     */
    private void sendBroadToChangeTypeAndId(String id) {
        Intent intent = new Intent(Const.ACTION_CHANGE_TYPE_AND_ID);
        intent.putExtra("type", "delivery_order_id");//type变为已经添加收货地址类型
        intent.putExtra("id",id);
        sendBroadcast(intent);
    }
}


