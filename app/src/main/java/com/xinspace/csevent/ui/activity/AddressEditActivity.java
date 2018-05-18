package com.xinspace.csevent.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.biz.AddressEditBiz;
import com.xinspace.csevent.login.activity.ChooseAddressAct;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.BdMapUtil;
import com.xinspace.csevent.util.NetWorkStateUtil;
import com.xinspace.csevent.util.PhoneNumUtil;
import com.xinspace.csevent.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 此页面为地址编辑和地址添加页面
 **/
public class AddressEditActivity extends BaseActivity {

    private Button btAddAddress;
    private Button btEditAddress;
    private EditText etName;
    private EditText etphoneNum;
    private EditText etAddress;
    private String addressId;
    private String isdefault;
    private BdMapUtil map;
    private String valueName;
    private String valuePhoneNum;
    private String valueAddress;
    private String valuePcd;
    private LinearLayout ll_back;
    private CheckBox ch_address;
    private RelativeLayout relative_editAddress;
    private TextView tv_user_address;
    private boolean isCheck;
    private TextView tv_recharge_title;
    private String provice;
    private String city;
    private String district;
    private SDPreference preference;
    private String openid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_edit);

        preference = SDPreference.getInstance();
        openid = preference.getContent("openid");

        setViews();
        setListeners();

        //判断从哪个页面跳转过来,显示不同的按钮
        Intent intent = getIntent();
        String action = intent.getStringExtra("addAddress");

        if ("addAddress".equals(action)) {
            tv_recharge_title.setText("添加地址");
            //显示新增收货地址按钮
            btAddAddress.setVisibility(View.VISIBLE);
        } else {
            //显示编辑地址按钮
            btEditAddress.setVisibility(View.VISIBLE);
            tv_recharge_title.setText("编辑地址");
            //接收adapter传过来的参数
            addressId = intent.getStringExtra("id");
            isdefault = intent.getStringExtra("isdefault");
            String detailAddress = intent.getStringExtra("address");
            String name = intent.getStringExtra("name");
            String tel = intent.getStringExtra("tel");

            provice = intent.getStringExtra("province");
            city = intent.getStringExtra("city");
            district = intent.getStringExtra("area");

            String pcd = intent.getStringExtra("pcd");
            //在编辑地址页面显示原来数据
            etName.setText(name);
            etphoneNum.setText(tel);
            etAddress.setText(detailAddress);

            if (pcd != null) {
                tv_user_address.setTextColor(Color.parseColor("#4a4a4a"));
                tv_user_address.setText(pcd);
            } else {
                tv_user_address.setTextColor(Color.parseColor("#cccccc"));
            }
        }
    }

    //设置监听
    private void setListeners() {
        //点击返回
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /**点击add收货地址*/
        btAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btAddAddress.setClickable(false);
                if (verify()) {
                    AddressEditBiz.addAddress2(openid , valueName, valuePhoneNum, valuePcd, valueAddress, new HttpRequestListener() {
                        @Override
                        public void onHttpRequestFinish(String result) throws JSONException {
                            handleAddAddressResult(result);
                        }

                        @Override
                        public void onHttpRequestError(String error) {

                        }
                    });
                }
                //设置添加的按钮不可用
            }
        });

        /**点击确认修改按钮修改地址*/
        btEditAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进行输入格式验证
                if (verify()) {
                    //remark = tvRelative.getText().toString();
                    AddressEditBiz.editAddress(addressId, openid , valueName, valuePhoneNum, valuePcd, valueAddress, new HttpRequestListener() {
                                @Override
                                public void onHttpRequestFinish(String result) throws JSONException {
                                    try {
                                        JSONObject jsonObject = new JSONObject(result);
                                        if (jsonObject.has("code")) {
                                            if (jsonObject.getString("code").equals("200")) {
                                                //刷新地址列表
                                                sendBroadcast(new Intent(AddressManagerActivity.ACTION_REFRESH_ADDRESS));
                                                finish();
                                            } else {
                                                ToastUtil.makeToast("编辑地址失败");
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onHttpRequestError(String error) {

                                }
                            });
                }
            }
        });
    }

    //处理添加地址返回的结果
    private void handleAddAddressResult(String result) {
        Log.i("www", "添加地址返回的结果:" + result);
        btAddAddress.setClickable(true);
        try {
            JSONObject jsonObject = new JSONObject(result);
            if (jsonObject.has("code")) {
                if (jsonObject.getString("code").equals("200")) {
                    this.setResult(RESULT_OK);
                    finish();
                } else {
                    ToastUtil.makeToast("添加地址失败");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setViews() {

        tv_recharge_title = (TextView) findViewById(R.id.tv_recharge_title);

        btAddAddress = (Button) findViewById(R.id.bt_address_edit_add_add);
        btEditAddress = (Button) findViewById(R.id.bt_address_edit_edit);

        etName = (EditText) findViewById(R.id.et_address_edit_name);
        etAddress = (EditText) findViewById(R.id.et_address_edit_address);
        etphoneNum = (EditText) findViewById(R.id.et_address_edit_phone);

        ll_back = (LinearLayout) findViewById(R.id.ll_address_edit_back);

        relative_editAddress = (RelativeLayout) findViewById(R.id.relative_editAddress);
        relative_editAddress.setOnClickListener(clickListener);
        ch_address = (CheckBox) findViewById(R.id.ch_address);
        ch_address.setOnCheckedChangeListener(checkedChangeListener);

        tv_user_address = (TextView) findViewById(R.id.tv_user_address);
    }

    CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            isCheck = isChecked;
        }
    };

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.relative_editAddress:
                    Intent intent = new Intent(AddressEditActivity.this, ChooseAddressAct.class);
                    startActivityForResult(intent, 1000);
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            switch (resultCode) {
                case 1000:
                    Bundle bundle = data.getExtras();
                    provice = bundle.getString("provice");
                    city = bundle.getString("city");
                    district = bundle.getString("District");
                    String pcd = provice + "," + city + "," + district;
                    if (pcd != null) {
                        tv_user_address.setTextColor(Color.parseColor("#4a4a4a"));
                        tv_user_address.setText(pcd);
                    } else {
                        tv_user_address.setTextColor(Color.parseColor("#cccccc"));
                    }
                    break;
            }
        }
    }

    /**
     * 验证地址编辑输入格式是否正确
     */
    private boolean verify() {
        //验证网络是否可用
        if (!NetWorkStateUtil.isNetworkAvailable(this)) {
            ToastUtil.makeToast("当前网络不可用");
            return false;
        }

        valueName = etName.getText().toString();
        valuePhoneNum = etphoneNum.getText().toString();
        valueAddress = etAddress.getText().toString();
        valuePcd = tv_user_address.getText().toString();

        btAddAddress.setClickable(true);

        if (TextUtils.isEmpty(valueName)) {
            ToastUtil.makeToast("联系人不能为空");
            return false;
        }
        if (TextUtils.isEmpty(valueAddress)) {
            ToastUtil.makeToast("详细地址不能为空");
            return false;
        }
        if (TextUtils.isEmpty(valuePcd)) {
            ToastUtil.makeToast("区域不能为空");
            return false;
        }
        if (TextUtils.isEmpty(valuePhoneNum) || (!PhoneNumUtil.isPhoneNO(valuePhoneNum))) {
            ToastUtil.makeToast("请输入正确的电话号码");
            return false;
        }

        return true;
    }
}


