package com.xinspace.csevent.sweepstake.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.biz.AddressManagerBiz;
import com.xinspace.csevent.data.entity.GetAddressEntity;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.parser.JsonPaser2;
import com.xinspace.csevent.shop.activity.FreeTrialAct;
import com.xinspace.csevent.sweepstake.adapter.ChangeAddressAdapter;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.StatusBarUtils;
import com.xinspace.csevent.util.ToastUtil;
import com.xinspace.csevent.ui.activity.AddressEditActivity;
import com.xinspace.csevent.ui.activity.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 此页面为地址管理页面
 * */
public class ChooseAddressAct2 extends BaseActivity{

    ListView listView;
    private ChangeAddressAdapter adapter;
    private ImageView img_address_add;
    List<GetAddressEntity> list;
    List<GetAddressEntity> addresses;
    private LinearLayout ll_back;
    private static final int REQUEST_CODE_ADD_ADDRESS = 100;
    public static final String ACTION_REFRESH_ADDRESS = "ACTION_REFRESH_ADDRESS";
    private AddressEditReceiver receiver;
    private TextView tv_recharge_title;
    private SDPreference preference;
    private String openid;
    private String goodsId;
    private Intent intent;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 200:

                    finish();
                    ToastUtil.makeToast("添加地址成功");
                    Intent intent = new Intent(ChooseAddressAct2.this , FreeTrialAct.class);
                    intent.putExtra("flag" , "address");
                    startActivity(intent);

                    break;
                case 400:

                    ToastUtil.makeToast("添加地址失败,请重新添加");

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(ChooseAddressAct2.this , R.color.app_bottom_color);
        setContentView(R.layout.activity_address_manager2);

        preference = SDPreference.getInstance();
        openid = preference.getContent("openid");

        intent = getIntent();
        if (intent != null){
            goodsId =  intent.getStringExtra("id");
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
        if (listView.getVisibility() == View.INVISIBLE){
            listView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getAddresses();
    }


    //获取用户地址
    public void getAddresses() {
        AddressManagerBiz.getAddressList(openid ,new HttpRequestListener() {
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
        List<Object> list = JsonPaser2.parserAry(result, GetAddressEntity.class, "data");

        LogUtil.i( "用户地址列表" + list.size());

        //显示地址
        if(list.size() !=0 && list.get(0) instanceof GetAddressEntity){
            listView.setVisibility(View.VISIBLE);
            List<GetAddressEntity> addresssList=new ArrayList<>();
            for (Object obj: list) {
                addresssList.add((GetAddressEntity)obj);
            }
            addresses = addresssList;
            LogUtil.i("收货地址:" + addresses.toString());
            // ToastUtil.makeToast(addresses.toString());
            updateListView(addresses);
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
    }

    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            LogUtil.i("www点击 listview了");
            GetAddressEntity getAddressEntity = addresses.get(position);
//            Intent intent = getIntent();
//            intent.putExtra("getAddressEntity" , (Serializable) getAddressEntity);
//            setResult(1000, intent);

            addAddressData(openid , goodsId , addresses.get(position).getId() ,
                    addresses.get(position).getRealname() ,  addresses.get(position).getMobile());

            //finish();
        }
    };

    private void setViews() {
        listView= (ListView) findViewById(R.id.listView);
        listView.setFocusable(true);
        listView.setFocusableInTouchMode(true);
        listView.setOnItemClickListener(itemClickListener);

        img_address_add= (ImageView) findViewById(R.id.img_address_add);
        img_address_add.setOnClickListener(onClickListener);

        ll_back = (LinearLayout) findViewById(R.id.ll_address_manager_back);

        tv_recharge_title = (TextView) findViewById(R.id.tv_recharge_title);
        tv_recharge_title.setText("选择地址");
    }

    /**刷新地址列表*/
    public void updateListView(List<GetAddressEntity> list){
        this.list = list;
        //创建Adapter
        if (adapter == null){
            adapter = new ChangeAddressAdapter(this);
        }
        adapter.setList(list);
        //设置Adapter
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

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.img_address_add:
                    Intent intent = new Intent(ChooseAddressAct2.this, AddressEditActivity.class);
                    intent.putExtra("addAddress", "addAddress");
                    startActivityForResult(intent,REQUEST_CODE_ADD_ADDRESS);
                    break;
            }
        }
    };


    private void addAddressData(String openid , String id , String aid , final String realname , String mobile){
        AddressManagerBiz.addAddressList2(openid, id, aid, realname, mobile, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                if(result == null || result.equals("")){
                    return;
                }
                JSONObject jsonObject = new JSONObject(result);
                LogUtil.i("试用添加地址" + result);
                if (jsonObject.getInt("code") == 200){
                    handler.obtainMessage(200).sendToTarget();
                }else{
                    handler.obtainMessage(400).sendToTarget();
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        handler.removeCallbacksAndMessages(null);
        itemClickListener = null;
        onClickListener = null;

        if (intent != null){
            intent = null;
        }
        list = null;
        addresses = null;
    }

}
