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
import com.xinspace.csevent.data.biz.ModifyAwardAddressBiz;
import com.xinspace.csevent.data.entity.GetAddressEntity;
import com.xinspace.csevent.data.entity.ResultForNoAddressAwardEntity;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.parser.JsonPaser;
import com.xinspace.csevent.util.parser.JsonPaser2;
import com.xinspace.csevent.util.Const;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.ToastUtil;

import org.json.JSONException;

import java.util.List;

/***
 * 修改收货地址页面
 */
public class ModifyAwardAddressActivity extends BaseActivity implements HttpRequestListener{
    private LinearLayout ll_back;
    private ListView listView;
    private List<Object> list;
    private AddressForGetAwardAdapter adapter;
    private TextView tvPname;
    private String pname;
    private String id;
    private String type;
    private String name,rtel,address;
    private Button btCommit;
    private LinearLayout llAddNewAddress;
    private String rid;//收货地址id
    private static final int REQUEST_CODE_ADD_NEW_ADDRESS = 100;//添加新地址

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_award_address);
        getData();
        setView();
        setListener();
        getAddressList();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //刷新数据
        getAddressList();
    }
    //获取地址列表
    private void getAddressList() {
        //获取地址列表
        AddressManagerBiz.getAddressList("" ,new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                showAddressList(result);
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }
    //显示地址列表
    private void showAddressList(String result) {
        list = JsonPaser2.parserAry(result, GetAddressEntity.class, "addresslist");
        //显示地址
        if (list.size() != 0) {
            //设置适配器
            if(null==adapter){
                adapter = new AddressForGetAwardAdapter(this, list);
                listView.setAdapter(adapter);
            }else{
                adapter.updateData(list);
            }
        } else {//如果地址集合没东西,直接把listview隐藏  list.size() ==0 的情况
            listView.setVisibility(View.INVISIBLE);
        }
    }
    //获取上个页面传递过来的数据
    private void getData() {
        Intent intent=getIntent();
        id=intent.getStringExtra("id");//地址记录id
        type=intent.getStringExtra("type");//地址记录在哪个表
        pname=intent.getStringExtra("pname");//地址记录id
    }
    //设置监听
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
            }
        });
        //点击提交收货地址
        btCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals("delivery_order_id")) {//已经有收货地址,修改地址
                    if(rid!=null) {
                        ModifyAwardAddressBiz.modifyAddress(ModifyAwardAddressActivity.this, id, rid, "");
                    }else{//没有选择列表中的地址
                        ToastUtil.makeToast("请先选择地址!");
                    }
                }
                if (type.equals("prizes_order_id")) {//小奖品添加地址
                    ModifyAwardAddressBiz.addAddressForSmallPrize(ModifyAwardAddressActivity.this, id, name, rtel, address);
                }
                //没有收货地址的奖品添加收货地址后会记录到有收货地址的奖品表,并且id也会相应改变
                if(type.equals("registration_id")) {//没有收货地址
                    ModifyAwardAddressBiz.commitAddress(ModifyAwardAddressActivity.this, id, name, "0", rtel, address);
                }
            }
        });
        //点击添加新地址
        //没有收货地址的奖品添加收货地址后会记录到有收货地址的奖品表,并且id也会相应改变
        llAddNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ModifyAwardAddressActivity.this, AddAddressForAwardActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("type", type);
                startActivityForResult(intent,REQUEST_CODE_ADD_NEW_ADDRESS);
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
    }

    @Override
    public void onHttpRequestFinish(String result) {
        LogUtil.i("提交收货地址的回调数据" + result);
        if (result.contains("200")) {
            if (result.contains("id")) {
                ResultForNoAddressAwardEntity enty= (ResultForNoAddressAwardEntity) JsonPaser.parserObj(result, ResultForNoAddressAwardEntity.class);
                sendBroadToChangeTypeAndId(enty.getId());//把id发回给中奖记录详情
            }
            ToastUtil.makeToast("收货地址添加成功!");
            finish();
        }
    }

    @Override
    public void onHttpRequestError(String error) {

    }

    /**没有地址的奖品添加收货地址后发广播给中奖详情页改变此奖品插入新表的表名和新表的id*/
    private void sendBroadToChangeTypeAndId(String id) {
        Intent intent = new Intent(Const.ACTION_CHANGE_TYPE_AND_ID);
        intent.putExtra("type", "delivery_order_id");//type变为已经添加收货地址类型
        intent.putExtra("id",id);//新记录表的id
        sendBroadcast(intent);
    }
}
