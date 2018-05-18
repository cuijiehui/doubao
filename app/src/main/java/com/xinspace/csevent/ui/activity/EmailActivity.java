package com.xinspace.csevent.ui.activity;
/**
 * 会员中心邮箱页面
 */
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.adapter.FeedfackMessageAdapter;
import com.xinspace.csevent.data.biz.EmailBiz;
import com.xinspace.csevent.data.entity.EmailForSysMsgEntity;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.parser.JsonPaser2;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class EmailActivity extends BaseActivity{

    private LinearLayout ll_back;
    private ListView lv;
    private List<Object> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        setViews();
        setListeners();
        getMessage();
    }
    //获取系统推送消息
    private void getMessage() {
        EmailBiz.getSysMsg(new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                showMessage(result);
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }
    //设置监听
    private void setListeners() {
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(EmailActivity.this, EmailSysPutActivity.class);
                EmailForSysMsgEntity enty = (EmailForSysMsgEntity) list.get(position);
                intent.putExtra("data",enty);
                startActivity(intent);
            }
        });
    }
    //初始化组件
    private void setViews() {
        ll_back= (LinearLayout) findViewById(R.id.ll_email_back);
        lv= (ListView) findViewById(R.id.lv_sys_msg);
    }
    //显示系统推送的消息
    private void showMessage(String result) throws JSONException {
        LogUtil.i("系统消息推送返回:"+result);
        JSONObject json = new JSONObject(result);
        if("200".equals(json.getString("result"))){//成功获取数据
            list= JsonPaser2.parserAry(result, EmailForSysMsgEntity.class, "data");
            FeedfackMessageAdapter adapter=new FeedfackMessageAdapter(this,list);
            lv.setAdapter(adapter);
        }else{//错误 202
            ToastUtil.makeToast(json.getString("msg"));
        }
    }
}
