package com.xinspace.csevent.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.xinspace.csevent.R;
import com.xinspace.csevent.data.biz.SupplyUserInfoBiz;
import com.xinspace.csevent.data.entity.ThirdLoginEntity;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.Const;
import com.xinspace.csevent.util.LogUtil;

/**
 *补充个人信息页面
 * */
public class SupplyUserInfoActivity extends BaseActivity implements HttpRequestListener{


    private ImageButton ibMan;
    private ImageButton ibWoman;
    private String gender="1";
    private EditText etName;
    private Button btRegister;
    private ThirdLoginEntity enty;
    private LinearLayout ll_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supply_user_info);
        enty= (ThirdLoginEntity) getIntent().getSerializableExtra("enty");
        setViews();
        setListeners();
    }

    private void setListeners() {
        //退出
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //点击提交补充信息
        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                supplyInfo(name, gender);
                finish();
            }
        });

        //点击性别女
        ibWoman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ibWoman.setBackground(getResources().getDrawable(R.drawable.bt_register_gender_woman_shape_pressed));
                ibMan.setBackground(getResources().getDrawable(R.drawable.bt_register_gender_man_shape_unpressed));
                gender = "0";
            }
        });
        //点击性别男
        ibMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ibWoman.setBackground(getResources().getDrawable(R.drawable.bt_register_gender_woman_shape_unpressed));
                ibMan.setBackground(getResources().getDrawable(R.drawable.bt_register_gender_man_shape_pressed));
                gender = "1";
            }
        });

    }
    /**向服务器提交信息*/
    private void supplyInfo(String name, String gender) {
        SupplyUserInfoBiz.supplyInfo(this, name, gender);
    }

    private void setViews() {
        ibMan = (ImageButton) findViewById(R.id.ib_supply_man);
        ibWoman = (ImageButton) findViewById(R.id.ib_supply_woman);
        etName = (EditText) findViewById(R.id.et_supply_name);
        btRegister = (Button) findViewById(R.id.bt_supply_register);
        ll_back= (LinearLayout) findViewById(R.id.ll_complete_back);
    }

    @Override
    public void onHttpRequestFinish(String result) {
        LogUtil.i("补充资料接口回调:" + result);
        if (result.equals("200")) {
            sendBroadcastForThirdLogin(enty);
        }
    }

    @Override
    public void onHttpRequestError(String error) {

    }

    //发广播给登录的Activity(第三方登录)
    private void sendBroadcastForThirdLogin(ThirdLoginEntity enty) {
        LogUtil.i("消息:" + enty.getResult());
        LogUtil.i("消息:" + enty.getMsg());
        LogUtil.i("消息:" + enty.getUser_id());
        Intent intent2Log=new Intent(Const.ACTION_LOGIN);
        intent2Log.putExtra("msg",enty.getMsg());
        intent2Log.putExtra("result",enty.getResult());
        intent2Log.putExtra("user_id",enty.getUser_id());
        sendBroadcast(intent2Log);
    }
}
