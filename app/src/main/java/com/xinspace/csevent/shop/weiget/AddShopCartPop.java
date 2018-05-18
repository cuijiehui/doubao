package com.xinspace.csevent.shop.weiget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xinspace.csevent.R;
import com.xinspace.csevent.app.utils.ScreenUtils;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.biz.GetActivityListBiz;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.shop.adapter.SpecAdapter;
import com.xinspace.csevent.shop.modle.GoodsDetailBean;
import com.xinspace.csevent.shop.modle.OptionBean;
import com.xinspace.csevent.shop.modle.SpecBean;
import com.xinspace.csevent.util.ImagerLoaderUtil;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 加入购物车弹窗
 *
 * Created by Android on 2017/4/19.
 */

public class AddShopCartPop extends PopupWindow{

    private Context context;
    private View view;
    private ImageView img_goods;
    private TextView tv_goods_discount_price;
    private TextView tv_goods_price;
    private Button but_minus;
    private Button but_add;
    private TextView tv_num;
    private Button but_yes;
    private int num = 1;
    private ImageView img_close;
    private GoodsDetailBean goodsDetailBean;
    private SDPreference preference;
    private String openId;

    private GridView gv_spec;
    private List<SpecBean> allSpecList = new ArrayList<SpecBean>();
    private List<OptionBean> allOptionList = new ArrayList<OptionBean>();
    private SpecAdapter adapter;
    private String specId;
    private String goodsId;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
//                case 100:
//                    if (msg.obj != null){
//                        allSpecList.addAll((Collection<? extends SpecBean>) msg.obj);
//                        adapter.setSpecList(allSpecList);
//                        adapter.notifyDataSetChanged();
//                    }
//                    break;
                case 200:
                    if (msg.obj != null){
                        allOptionList.addAll((Collection<? extends OptionBean>) msg.obj);
                        adapter.setSpecList(allOptionList);
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case 300:
                    ToastUtil.makeToast("添加购物车成功");
                    AddShopCartPop.this.dismiss();
                    break;
                case 301:
                    ToastUtil.makeToast("添加购物车失败");
                    break;
            }
        }
    };


    public AddShopCartPop(Context context , GoodsDetailBean bean , String openid) {
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.pop_buy_goods, null);
        this.setContentView(view);
        this.goodsDetailBean = bean;
        this.openId = openid;
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        this.setHeight(ScreenUtils.getScreenHeight(context) - ScreenUtils.getStatusBarHeight(context));
        setBackgroundDrawable(new BitmapDrawable());
        setTouchable(true);
        preference = SDPreference.getInstance();
        initView();
        standardData();
    }

    private void initView() {
        openId = preference.getContent("openid");
        goodsId = goodsDetailBean.getId();
        num = 1;
        img_goods = (ImageView) view.findViewById(R.id.img_goods);
        tv_goods_discount_price = (TextView) view.findViewById(R.id.tv_goods_discount_price);
        tv_goods_price = (TextView) view.findViewById(R.id.tv_goods_price);

        img_close = (ImageView) view.findViewById(R.id.img_close);
        img_close.setOnClickListener(clickListener);

        but_minus = (Button) view.findViewById(R.id.but_minus);
        but_add = (Button) view.findViewById(R.id.but_add);
        tv_num = (TextView) view.findViewById(R.id.tv_num);
        but_yes = (Button) view.findViewById(R.id.but_yes);

        but_minus.setOnClickListener(clickListener);
        but_add.setOnClickListener(clickListener);
        but_yes.setOnClickListener(clickListener);

        ImagerLoaderUtil.displayImageWithLoadingIcon(goodsDetailBean.getThumb(), img_goods, R.drawable.icon_detail_load);
        tv_goods_discount_price.setText("¥" + goodsDetailBean.getMarketprice());
        tv_goods_price.setText("¥" + goodsDetailBean.getProductprice());
        tv_goods_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        gv_spec = (GridView) view.findViewById(R.id.gv_spec);
        adapter = new SpecAdapter(context);
        adapter.setSpecList(allOptionList);
        gv_spec.setAdapter(adapter);
        gv_spec.setOnItemClickListener(itemClickListener);
    }

    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        TextView oldTextView;
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (oldTextView != null) {
                oldTextView.setBackgroundResource(R.drawable.tv_spec_bg_shape);
                oldTextView.setTextColor(Color.parseColor("#000000"));
            }

            TextView textView = (TextView) view.findViewById(R.id.tv_spec);
            oldTextView = textView;
            textView.setBackgroundResource(R.drawable.tv_spec_bg_press_shape);
            textView.setTextColor(Color.parseColor("#ffffff"));

            tv_goods_discount_price.setText("¥" + allOptionList.get(position).getMarketprice());
            tv_goods_price.setText("¥" + allOptionList.get(position).getProductprice());
            specId = allOptionList.get(position).getId();
        }
    };

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.but_minus:
                    if (num == 1){
                        ToastUtil.makeToast("至少选择一件");
                    }else{
                        num--;
                        tv_num.setText(num + "");
                        //allPrice = count * prize;
                    }
                    break;
                case R.id.but_add:
                    num ++;
                    tv_num.setText(num + "");
                    //allPrice = count * prize;
                    break;
                case R.id.img_close:
                    dismiss();
                    break;
                case R.id.but_yes:

                    if (allOptionList.size() != 0){
                        if (specId != null){
                            addCartData();
                        }else {
                            ToastUtil.makeToast("请选择商品规格");
                        }
                    }else{
                        addCartData();
                    }
                    break;
            }
        }
    };

    /**
     * 获取商品规格数据
     *
     */
    private void standardData(){
        String goodId = goodsDetailBean.getId();
        final List<SpecBean> specList = new ArrayList<SpecBean>();
        final List<OptionBean> optionList = new ArrayList<OptionBean>();

        GetActivityListBiz.getGoodsStandard(goodId, openId, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {

                if (result == null || result.equals("")){
                    return;
                }

                JSONObject jsonObject = new JSONObject(result);
                Gson gson = new Gson();
                if (jsonObject.getString("code").equals("200")){
                    JSONObject dataObject = jsonObject.getJSONObject("data");

                    JSONArray jsonArray = dataObject.getJSONArray("specs");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    JSONArray jsonArray1 = jsonObject1.getJSONArray("items");
                    for (int i = 0 ; i < jsonArray1.length() ; i++){
                        JSONObject jsonObject2 = jsonArray1.getJSONObject(i);
                        SpecBean bean = gson.fromJson(jsonObject2.toString() ,  SpecBean.class);
                        specList.add(bean);
                    }
                    LogUtil.i("规格数组长度" + specList.size());

                    JSONArray optionsArray = dataObject.getJSONArray("options");
                    for (int i = 0 ; i < optionsArray.length() ; i++){
                        JSONObject optionObject = optionsArray.getJSONObject(i);
                        OptionBean bean = gson.fromJson(optionObject.toString() , OptionBean.class);
                        optionList.add(bean);
                    }
                    LogUtil.i("规格数组长度" + specList.size() + "规格数组长度" + optionList.size());
                    handler.obtainMessage(100 , specList).sendToTarget();
                    handler.obtainMessage(200 , optionList).sendToTarget();
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }

    private void addCartData(){
        GetActivityListBiz.addCartData(goodsId, openId, specId, String.valueOf(num), new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                LogUtil.i("加入购物车返回值" + result);
                if (result == null || result.equals("")){
                    return;
                }

                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("code").equals("200")){
                    handler.obtainMessage(300).sendToTarget();
                }else{
                    handler.obtainMessage(301).sendToTarget();
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }


    public void onDestory(){

        itemClickListener = null;
        clickListener = null;
        allOptionList = null;
        allSpecList = null;
        handler.removeCallbacksAndMessages(null);

    }

}
