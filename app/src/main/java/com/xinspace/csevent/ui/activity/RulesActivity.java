package com.xinspace.csevent.ui.activity;
/**
 * 会员中心抽奖规则页面
 */

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.data.biz.RulesBiz;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class RulesActivity extends BaseActivity implements HttpRequestListener{

    private LinearLayout ll_back;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);
        RulesBiz.getRules(this);
        setListeners();
    }

    private void setListeners() {
        ll_back= (LinearLayout) findViewById(R.id.ll_rules_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void setViews(String rules) {
        tv=(TextView)findViewById(R.id.tv_rules_txt);
        tv.setText(rules);
    }

    @Override
    public void onHttpRequestFinish(String result) throws JSONException {
        JSONObject json = new JSONObject(result);
        switch (json.getString("result")){
            case "200":
                String rules=json.getString("remark");
                setViews(rules);
                break;
            case "202":
                ToastUtil.makeToast(json.getString("msg"));
                break;
        }

    }

    @Override
    public void onHttpRequestError(String error) {

    }
}
