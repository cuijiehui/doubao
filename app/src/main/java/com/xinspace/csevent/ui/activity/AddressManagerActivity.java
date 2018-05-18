package com.xinspace.csevent.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.adapter.AddressAdapter;
import com.xinspace.csevent.data.biz.AddressManagerBiz;
import com.xinspace.csevent.data.entity.GetAddressEntity;
import com.xinspace.csevent.login.weiget.ClickListener;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.parser.JsonPaser2;

import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * 此页面为地址管理页面
 * */
public class AddressManagerActivity extends BaseActivity{

    ListView listView;
    private AddressAdapter adapter;
    private ImageView img_address_add ;
    List<GetAddressEntity> list;
    List<GetAddressEntity> addresses;
    private LinearLayout ll_back;
    private static final int REQUEST_CODE_ADD_ADDRESS = 100;
    public static final String ACTION_REFRESH_ADDRESS = "ACTION_REFRESH_ADDRESS";
    private AddressEditReceiver receiver;
    private Intent intent;
    private String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_manager);

        intent = getIntent();
        if (intent != null){
            flag = intent.getStringExtra("flag");
        }
        setViews();
        setListeners();
        //注册广播
        receiver=new AddressEditReceiver();
        IntentFilter intentFliter=new IntentFilter(ACTION_REFRESH_ADDRESS);
        registerReceiver(receiver,intentFliter);
    }

    /**在此处获取用户地址是因为增加新地址返回次页面会调用此方法*/
    @Override
    protected void onStart() {
        super.onStart();
        //链接服务器获取用户地址
        getAddresses();
        //删除完所有数据后listview会隐藏,需要判断状态
        if (listView.getVisibility()==View.INVISIBLE){
            listView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_ADDRESS){
            getAddresses();
        }
    }


    //获取用户地址
    public void getAddresses() {
        AddressManagerBiz.getAddressList("" , new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                handleAddAddressResult(result);
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }


    //处理返回的结果
    private void handleAddAddressResult(String result) {

        List<Object> list = JsonPaser2.parserAry(result, GetAddressEntity.class, "result");
        Log.i("www", "用户地址列表" + list.size());

        //显示地址
        if(list.size() !=0 && list.get(0) instanceof GetAddressEntity){
            List<GetAddressEntity> addresssList=new ArrayList<>();
            for (Object obj: list) {
                addresssList.add((GetAddressEntity)obj);
            }

            if (list.size() == addresssList.size()){
                addresses = addresssList;
                updateListView(addresses);
            }
        }else {//如果地址集合没东西,直接把listview隐藏  list.size() ==0 的情况
            listView.setVisibility(View.INVISIBLE);
        }
    }

    private void setListeners() {
        //点击返回
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //点击新增收货地址
        img_address_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//跳转到新增地址页面
                Intent intent = new Intent(AddressManagerActivity.this, AddressEditActivity.class);
                intent.putExtra("addAddress", "addAddress");
                startActivityForResult(intent,REQUEST_CODE_ADD_ADDRESS);
            }
        });
    }




    private void setViews() {
        listView= (ListView) findViewById(R.id.listView);
        img_address_add= (ImageView) findViewById(R.id.img_address_add);
        ll_back = (LinearLayout) findViewById(R.id.ll_address_manager_back);
    }


    /**刷新地址列表*/
    public void updateListView(List<GetAddressEntity> list){
        this.list = list;
        //创建Adapter
        listView.setVisibility(View.VISIBLE);
        if (adapter == null){
            adapter = new AddressAdapter(this, clickListener);
        }
        //设置Adapter
        adapter.setList(list);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    class AddressEditReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(ACTION_REFRESH_ADDRESS)){
                getAddresses();
            }
        }
    }

    ClickListener clickListener = new ClickListener() {
        @Override
        public void clickListenet(boolean isClick , int position) {
            Log.i("www" , "isClick" + isClick + "position" + position);
            if (flag.equals("home")){
                GetAddressEntity entity = list.get(position);
                Intent intent = new Intent(AddressManagerActivity.this, AddressEditActivity.class);
                //把信息传到编辑页面
                intent.putExtra("id", entity.getId());
                intent.putExtra("address", entity.getAddress());
                intent.putExtra("name", entity.getRealname());
                intent.putExtra("tel", entity.getMobile());
                intent.putExtra("province" , entity.getProvince());
                intent.putExtra("city" , entity.getCity());
                intent.putExtra("area" , entity.getArea());
                intent.putExtra("pcd" ,entity.getProvince() + entity.getCity() + entity.getArea());
                startActivity(intent);
            }else if (flag.equals("detail")){
                GetAddressEntity getAddressEntity = list.get(position);
                Intent intent = getIntent();
                intent.putExtra("getAddressEntity" , (Serializable) getAddressEntity);
                setResult(1000, intent);
                finish();
            }
        }
    };

}
