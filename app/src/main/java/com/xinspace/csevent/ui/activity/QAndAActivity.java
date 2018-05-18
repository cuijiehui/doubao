package com.xinspace.csevent.ui.activity;
/**
 * 会员中心Q&A页面
 */

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.xinspace.csevent.R;
import com.xinspace.csevent.data.biz.QAndABiz;
import com.xinspace.csevent.data.entity.QAndAEntity;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.parser.JsonPaser2;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.ToastUtil;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QAndAActivity extends BaseActivity implements HttpRequestListener {
    private List<Object> qaList;
    private LinearLayout ll_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_and_a);
        QAndABiz.getQA(this);
        setListeners();
    }

    private void setListeners() {
        ll_back= (LinearLayout) findViewById(R.id.ll_q_and_a_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void setViews() {

        SimpleAdapter adapter = new SimpleAdapter(this,getData(),R.layout.item_q_and_a,
                new String[]{"question","answer"},
                new int[]{R.id.item_q_txt1,R.id.item_a_txt1});
        ListView lv= (ListView) findViewById(R.id.lvQa);
        lv.setAdapter(adapter);
    }

    private List<Map<String,String>> getData() {

        List<Map<String, String>> list = new ArrayList<>();
        for (Object qa : qaList) {
            Map<String, String> map =new HashMap<String, String>();
            QAndAEntity enty=(QAndAEntity) qa;
            map.put("question", enty.getQuestions());
            map.put("answer", enty.getAnswers());
            list.add(map);
        }
        return list;
    }

    @Override
    public void onHttpRequestFinish(String result) throws JSONException {
        LogUtil.i("Q&A数据返回:"+result);
        JSONObject json = new JSONObject(result);
        if ("200".equals(json.getString("result"))){//返回正确
            qaList = JsonPaser2.parserAry(result, QAndAEntity.class,"data");
            setViews();
        }else{
            ToastUtil.makeToast(json.getString("msg"));
        }
    }

    @Override
    public void onHttpRequestError(String error) {

    }
}
