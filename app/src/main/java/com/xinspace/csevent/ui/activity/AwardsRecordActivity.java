package com.xinspace.csevent.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.adapter.AwardsAdapter;
import com.xinspace.csevent.data.biz.AddressManagerBiz;
import com.xinspace.csevent.data.biz.AwardRecordBiz;
import com.xinspace.csevent.data.entity.AwardsRecordEntity;
import com.xinspace.csevent.data.entity.GetAddressEntity;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.parser.JsonPaser2;
import com.xinspace.csevent.sweepstake.activity.PrizeDetailAct;
import com.xinspace.csevent.util.LogUtil;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * 即开抽奖中奖记录列表
 */
public class AwardsRecordActivity extends BaseActivity{

    private ListView listView;
    private LinearLayout llBack;
    private List<Object> list;//记录列表
    private static final int REQUEST_CODE_REFRESHING_RECORD = 100;
    private AwardsAdapter adapter;
    private Intent intent;
    private String addressState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_awards_record);

        intent = getIntent();
        if (intent != null){
            addressState = intent.getStringExtra("address");
        }
        setViews();
        setListeners();
        //获取记录列表数据
        getUserAddresses();
        getAwardRecord();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //刷新数据
        getAwardRecord();
        getUserAddresses();
    }

    //获取中奖纪录
    private void getAwardRecord() {
        AwardRecordBiz.getRecord(new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                showAwardRecord(result);
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }
    //显示中奖记录
    private void showAwardRecord(String result) {

        Log.i("www" , "即开抽奖中奖纪录列表" + result);

        list= JsonPaser2.parserAry(result, AwardsRecordEntity.class,"data");

        if(null==adapter){
            adapter = new AwardsAdapter(this,list);
            listView.setAdapter(adapter);
        }else{
            adapter.updateData(list);
        }
    }
    //设置监听
    private void setListeners() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AwardsRecordEntity enty=(AwardsRecordEntity)list.get(position);
                LogUtil.i("活动id:" + enty.getAid());
                String recordId = enty.getId();//记录id
                String type = enty.getType();//记录状态
                //intent.putExtra("id",recordId);
                //intent.putExtra("type",type);
                if (addressState != null && addressState.equals("1")){
                    Intent intent = new Intent(AwardsRecordActivity.this, PrizeDetailAct.class);
                    intent.putExtra("flag" , "list");
                    intent.putExtra("bean" , enty);
                    startActivityForResult(intent,REQUEST_CODE_REFRESHING_RECORD);
                }else if (addressState != null && addressState.equals("0")){
                    Intent intent = new Intent(AwardsRecordActivity.this, AddressManagerActivity.class);
                    startActivityForResult(intent,REQUEST_CODE_REFRESHING_RECORD);
                }
            }
        });

        //点击返回
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    //初始化组件
    private void setViews() {
        listView =(ListView)findViewById(R.id.listView);
        llBack=(LinearLayout)findViewById(R.id.ll_award_record_back);
    }


    //获取用户地址
    public void getUserAddresses() {
        AddressManagerBiz.getAddressList("" , new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                Log.i("www" , "查询地址的" + result);
                handleAddAddressResult(result);
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }

    private void handleAddAddressResult(String result) {
        List<Object> list= JsonPaser2.parserAry(result, GetAddressEntity.class, "addresslist");
        //地址列表
        if(list.size() !=0 && list.get(0) instanceof GetAddressEntity){
            List<GetAddressEntity> addresssList=new ArrayList<>();
            for (Object obj: list) {
                addresssList.add((GetAddressEntity)obj);
            }
            addressState = "1" ;
        }else {
            addressState = "0" ;
        }
    }



}
