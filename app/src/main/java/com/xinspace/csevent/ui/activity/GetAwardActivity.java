package com.xinspace.csevent.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.adapter.AddressForGetAwardAdapter;
import com.xinspace.csevent.data.biz.AddressManagerBiz;
import com.xinspace.csevent.data.biz.GetAwardBiz;
import com.xinspace.csevent.data.entity.GetAddressEntity;
import com.xinspace.csevent.data.entity.WinningEnty;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.parser.JsonPaser2;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.NetWorkStateUtil;
import com.xinspace.csevent.util.ToastUtil;

import org.json.JSONException;

import java.util.List;

/**
 * 领取奖品页面
 */
public class GetAwardActivity extends BaseActivity implements HttpRequestListener{
    private LinearLayout ll_back;
    private ListView listView;
    private List<Object> list;
    private AddressForGetAwardAdapter adapter;
    private TextView tvPname;
    private String pname;
    private StringBuilder id=new StringBuilder();
    private String type;
    private String name,rtel,address;
    private Button btCommit;
    private LinearLayout llAddNewAddress;
    private String rid;//收货地址id
    private List<Object> winningList;
    private static final int REQUEST_CODE_ADD_ADDRESS = 100;//使用新的地址

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_award);
        getData();
        setView();
        setListener();
        //显示获奖信息
        showAwardInfo();
        //获取地址列表
        getAddressList();
    }
    //获取地址列表
    private void getAddressList() {
        AddressManagerBiz.getAddressList("" , new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                showAddressList(result);
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //检查网络状态
        boolean is_available = NetWorkStateUtil.isNetworkAvailable(this);
        if(!is_available){
            ToastUtil.makeToast("当前网络不可用");
            return;
        }
        //刷新数据
        getAddressList();
    }

    //显示地址列表
    private void showAddressList(String result) {
        list = JsonPaser2.parserAry(result, GetAddressEntity.class, "addresslist");
        //显示地址
        if (list.size() != 0 ) {
            if(null==adapter){
                //创建Adapter
                adapter = new AddressForGetAwardAdapter(this, list);
                //设置Adapter
                listView.setAdapter(adapter);
            }else{
                adapter.updateData(list);
            }
        } else {//如果地址集合没东西,直接把listview隐藏  list.size() ==0 的情况
            listView.setVisibility(View.INVISIBLE);
        }
    }
    //显示中奖奖品信息
    private void showAwardInfo() {
        StringBuilder sb=new StringBuilder();
        for (int i=0;i<winningList.size();i++){
            WinningEnty enty = (WinningEnty) winningList.get(i);
            String cname=enty.getCname();
            sb.append(cname+" ");
            id.append(enty.getRegistration_id());
            if(i!=winningList.size()-1){
                id.append(",");
            }
        }
        LogUtil.i("拼接的奖品id:"+id.toString());
        tvPname.setText(sb.toString());
    }
    //获取上个应用传递过来的数据
    private void getData() {
        Intent intent=getIntent();
        //从抽奖页面跳转过来
        winningList = (List<Object>) intent.getSerializableExtra("data");
    }
    //设置监听器
    private void setListener() {
        //点击选择收货地址
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.changeSelected(position);//通知更新,选择后对号变绿色,其他未选择变灰色
                GetAddressEntity enty = (GetAddressEntity) list.get(position);
                rid=enty.getId();
                name = enty.getRealname();
                rtel = enty.getMobile();
                address = enty.getAddress();
                //ToastUtil.makeToast("姓名:"+name+"电话:"+rtel+"地址:"+address);
            }
        });
        //点击提交收货地址
        btCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.i("提交按钮："+rtel);
                //没有收货地址的奖品添加收货地址后会记录到有收货地址的奖品表,并且id也会相应改变
                if(rtel!=null){
                    GetAwardBiz.commitAddress(GetAwardActivity.this, id.toString(), name, "0", rtel, address);
                    ToastUtil.makeToast("收货地址添加成功!");
                }else{
                    ToastUtil.makeToast("请选择收货地址");
                }
            }
        });
        //点击添加新地址
        //没有收货地址的奖品添加收货地址后会记录到有收货地址的奖品表,并且id也会相应改变
        llAddNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GetAwardActivity.this, AddAddressForAwardActivity.class);
                intent.putExtra("id", id.toString());
                startActivityForResult(intent,REQUEST_CODE_ADD_ADDRESS);
            }
        });
        //点击返回
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    //初始化组件
    private void setView() {
        btCommit=(Button)findViewById(R.id.bt_get_award_again);
        listView = (ListView) findViewById(R.id.listView);
        ll_back = (LinearLayout) findViewById(R.id.ll_get_award_back);
        llAddNewAddress = (LinearLayout) findViewById(R.id.ll_use_new_address);
        tvPname = (TextView) findViewById(R.id.tv_get_award_pname);
    }
    @Override
    public void onHttpRequestFinish(String result) {
        LogUtil.i("提交收货地址的回调数据" + result);
        if (result.contains("200")) {
            finish();
        }
    }

    @Override
    public void onHttpRequestError(String error) {

    }
}
