package com.xinspace.csevent.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xinspace.csevent.R;
import com.xinspace.csevent.adapter.DuobaoRecordAdapter;
import com.xinspace.csevent.data.biz.GetDuobaoRecordBiz;
import com.xinspace.csevent.data.entity.ActivityListEntity;
import com.xinspace.csevent.data.entity.DuobaoRecordEntity;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.parser.JsonPaser;
import com.xinspace.csevent.sweepstake.activity.ActDetailActivity2;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.TimeUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/***
 * 夺宝记录页面
 */
public class  DuobaoRecordActivity extends SwipeBackActivity {
    private LinearLayout ll_back;
    private PullToRefreshListView listView;
    private List<Object> entyList=new ArrayList<>();
    private DuobaoRecordAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duobao_record);
        setView();
        setListener();
        getRecords();
    }


    //处理夺宝结果
    private void handleDuobaoRecord(String result) throws JSONException {

        Log.i("www" , "夺宝记录result" + result);

        JSONObject obj=new JSONObject(result);
        String res=obj.getString("result");
        if(res.equals("200")){
            JSONArray ary = obj.getJSONArray("data");
            entyList = JsonPaser.parserAry(ary.toString(), DuobaoRecordEntity.class);

            if(null==adapter){
                adapter=new DuobaoRecordAdapter(this,entyList);
                listView.setAdapter(adapter);
            }else{
                adapter.updateData(entyList);
            }
        }else if(res.equals("201")){
            //没有更多活动了
        }
    }
    //获取夺宝记录列表
    private void getRecords() {
        GetDuobaoRecordBiz.getRecordByAll(new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                listView.onRefreshComplete();
                listView.getLoadingLayoutProxy(true,false).setLastUpdatedLabel("上次更新:"+ TimeUtil.getTime());
                //处理返回结果
                handleDuobaoRecord(result);
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }
    //设置监听
    private void setListener() {
        //返回
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //刷新
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                LogUtil.i("下拉刷新");
                getRecords();
            }
        });
        //点击记录跳转到活动抽奖页面
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DuobaoRecordEntity enty = (DuobaoRecordEntity) entyList.get(position-1);
                ActivityListEntity actEnty = new ActivityListEntity();
                actEnty.setId(Integer.parseInt(enty.getAid()));
                Intent intent=null;
                if(enty.getType().equals("1")){
                    //普通抽奖
                    intent=new Intent(DuobaoRecordActivity.this, ActDetailActivity2.class);
                }
//                else if(enty.getType().equals("4")){
//                    //抽奖池
//                    intent=new Intent(DuobaoRecordActivity.this, AwardPoolActivity.class);
//                }
                intent.putExtra("data",actEnty);
                startActivity(intent);
            }
        });
    }
    //初始化组件
    private void setView() {
        ll_back= (LinearLayout) findViewById(R.id.ll_duobao_record_back);
        listView= (PullToRefreshListView)findViewById(R.id.lv_duobao_record_listview);
    }
}
