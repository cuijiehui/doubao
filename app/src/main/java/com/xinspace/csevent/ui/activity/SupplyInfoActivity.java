package com.xinspace.csevent.ui.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.data.biz.SupplyInfoBiz;
import com.xinspace.csevent.data.entity.SupplyInfoEntity;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.parser.JsonPaser;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 完善用户信息页面
 */
public class SupplyInfoActivity extends BaseActivity{

    private LinearLayout llBack;
    private TextView tvBirthday1;
    private EditText etName;
    private EditText etEmail;
    private String myGender ="1";
    private ImageButton ibMan;
    private ImageButton ibWoman;
    private Spinner spIncome;
    private Spinner spInterest;
    private String myInterest;
    private String myIncome;
    private String myName;
    private String myEmail;
    private String myBirthday;
    private Button btCommit;
    private LinearLayout llMyAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supply_info);
        setViews();
        setListeners();
        //读取用户资料并设置显示
        readInfo();
    }


    private void setListeners() {
        //点击返回
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //点击提交补充资料
        btCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取昵称
                myName=etName.getText().toString();
                //获取邮箱
                myEmail=etEmail.getText().toString();
                //写入用户完善的资料
                WriteInfo();
            }
        });

        //点击选择生日
        tvBirthday1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                DatePickerDialog d = new DatePickerDialog(
                        SupplyInfoActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override//注意月份是从0开始计算
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                LogUtil.i("生日:"+year+"."+monthOfYear+"."+dayOfMonth);
                                tvBirthday1.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
                                myBirthday = tvBirthday1.getText().toString();
                            }
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                d.show();
            }
        });

        //点击性别女
        ibWoman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ibWoman.setBackground(getResources().getDrawable(R.drawable.bt_register_gender_woman_shape_pressed));
                ibMan.setBackground(getResources().getDrawable(R.drawable.bt_register_gender_man_shape_unpressed));
                myGender = "0";
            }
        });
        //点击性别男
        ibMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ibWoman.setBackground(getResources().getDrawable(R.drawable.bt_register_gender_woman_shape_unpressed));
                ibMan.setBackground(getResources().getDrawable(R.drawable.bt_register_gender_man_shape_pressed));
                myGender = "1";
            }
        });

        //选择收入
        spIncome.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
             //   String [] income=getResources().getStringArray(R.array.income);
             //   myIncome = income[position];
                myIncome = String.valueOf(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //选择感兴趣产品
        spInterest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              //  String [] interest=getResources().getStringArray(R.array.interest);
              //  myInterest = interest[position];
                myInterest = String.valueOf(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //我的地址
        llMyAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SupplyInfoActivity.this, AddressManagerActivity.class));
            }
        });

    }
    //初始化组件
    private void setViews() {
        llBack = (LinearLayout) findViewById(R.id.ll_supply_info_back);
        llMyAddress = (LinearLayout) findViewById(R.id.ll_supply_info_my_address);

        etName = (EditText) findViewById(R.id.et_supply_info_name);
        etEmail = (EditText) findViewById(R.id.et_supply_info_email);

        tvBirthday1 = (TextView) findViewById(R.id.tv_supply_info_birthday1);

        ibMan = (ImageButton) findViewById(R.id.ib_supply_info_man);
        ibWoman = (ImageButton) findViewById(R.id.ib_supply_info_woman);

        spIncome=(Spinner)findViewById(R.id.sp_income);
        spInterest=(Spinner)findViewById(R.id.sp_interest);

        btCommit=(Button)findViewById(R.id.bt_supply_info_commit);
    }
    /**读取用户资料,并显示*/
    private void readInfo() {
        SupplyInfoBiz.getInfo(new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                LogUtil.i("读取用户资料接口的返回:"+result);
                JSONObject json = new JSONObject(result);
                if ("200".equals(json.getString("result"))){//显示资料
                    SupplyInfoEntity enty= (SupplyInfoEntity) JsonPaser.parserObj(result, SupplyInfoEntity.class);
                    etName.setText(enty.getNickname());
                    etName.setSelection(etName.getText().length());//把光标定位在最后面
                    tvBirthday1.setText(enty.getBirthday());
                    myBirthday=enty.getBirthday();
                    etEmail.setText(enty.getEmail());
                    etEmail.setSelection(etEmail.getText().length());//把光标定位在最后面
                    myGender = enty.getSex();
                    if ("0".equals(enty.getSex())){//女
                        ibWoman.setBackground(getResources().getDrawable(R.drawable.bt_register_gender_woman_shape_pressed));
                        ibMan.setBackground(getResources().getDrawable(R.drawable.bt_register_gender_man_shape_unpressed));
                    }else{//男
                        ibWoman.setBackground(getResources().getDrawable(R.drawable.bt_register_gender_woman_shape_unpressed));
                        ibMan.setBackground(getResources().getDrawable(R.drawable.bt_register_gender_man_shape_pressed));
                    }
                    spIncome.setSelection(Integer.parseInt(enty.getSalary()),true);
                    spInterest.setSelection(Integer.parseInt(enty.getInterest()),true);
                }else {
                    ToastUtil.makeToast(json.getString("msg"));
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }
    /**写入完善的资料*/
    private void WriteInfo() {
        //在将数据写入数据库之前,判断用户输入的邮箱格式是否正确
        String filter="^[0-9a-z][a-z0-9\\._-]{1,}@[a-z0-9-]{1,}[a-z0-9]\\.[a-z\\.]{1,}[a-z]$";
        Pattern p=Pattern.compile(filter);
        Matcher matcher = p.matcher(myEmail);
        if(!matcher.find()){
            ToastUtil.makeToast("邮箱格式错误,请重新输入");
            return;
        }
        SupplyInfoBiz.supplyInfo(myName, myGender, myBirthday, myIncome, myEmail, myInterest, new HttpRequestListener() {
                    @Override
                    public void onHttpRequestFinish(String result) throws JSONException {
                        LogUtil.i("写入资料接口的返回:"+result);
                        JSONObject json = new JSONObject(result);
                        if("200".equals(json.getString("result"))){
                            ToastUtil.makeToast("更新资料成功!");
                            finish();
                        }else{
                            ToastUtil.makeToast(json.getString("msg"));
                        }
                    }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }
}
