package com.xinspace.csevent.shop.weiget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xinspace.csevent.monitor.view.MyGridView;
import com.xinspace.csevent.R;
import com.xinspace.csevent.app.utils.ScreenUtils;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.biz.GetActivityListBiz;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.shop.activity.BuyExChangeAct;
import com.xinspace.csevent.shop.adapter.SpecAdapter;
import com.xinspace.csevent.shop.modle.ExGoodsBean;
import com.xinspace.csevent.shop.modle.GoodsDetailBean;
import com.xinspace.csevent.shop.modle.OptionBean;
import com.xinspace.csevent.shop.modle.OrderGoodsBean;
import com.xinspace.csevent.shop.modle.SpecBean;
import com.xinspace.csevent.util.ImagerLoaderUtil;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 积分商城商品买东西下单弹窗
 * <p>
 * Created by Android on 2017/4/19.
 */

public class StandardPop extends PopupWindow {

    private Context context;
    private View view;
    private ImageView img_goods;
    private TextView tv_goods_integral;
    private TextView tv_goods_price;
    private Button but_minus;
    private Button but_add;
    private TextView tv_num;
    private Button but_yes;
    private int num = 1;
    private ImageView img_close;
    private ExGoodsBean goodsDetailBean;
    private SDPreference preference;
    private String openId;

    private MyGridView gv_spec;
    private List<SpecBean> allSpecList = new ArrayList<SpecBean>();
    private List<OptionBean> allOptionList = new ArrayList<OptionBean>();
    private SpecAdapter adapter;
    private List<GoodsDetailBean> goodsList;
    private List<OrderGoodsBean> orderGoodsList;
    private String specId = "";
    private String marketprice;
    private Float allPrice;
    private TextView tv_goods_title;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
//                case 100:
//                    if (msg.obj != null){
//                        allSpecList.addAll((Collection<? extends SpecBean>) msg.obj);
//                        adapter.setSpecList(allSpecList);
//                        adapter.notifyDataSetChanged();
//                    }
//                    break;
                case 200:
                    if (msg.obj != null) {
                        allOptionList.addAll((Collection<? extends OptionBean>) msg.obj);
                        adapter.setSpecList(allOptionList);
                        adapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };

    public StandardPop(Context context, ExGoodsBean bean, String openid) {
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.pop_good_standard, null);
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

        goodsList = new ArrayList<GoodsDetailBean>();
        orderGoodsList = new ArrayList<OrderGoodsBean>();

        openId = preference.getContent("openid");
        num = 1;
        img_goods = (ImageView) view.findViewById(R.id.img_goods);
        tv_goods_integral = (TextView) view.findViewById(R.id.tv_goods_integral);
        tv_goods_price = (TextView) view.findViewById(R.id.tv_goods_price);

        tv_goods_title = (TextView) view.findViewById(R.id.tv_goods_title);
        tv_goods_title.setText(goodsDetailBean.getTitle());

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
        marketprice = goodsDetailBean.getMoney();
        BigDecimal b1 = new BigDecimal(Float.toString(num));
        BigDecimal b2 = new BigDecimal(Float.valueOf(marketprice));
        allPrice = b1.multiply(b2).floatValue();

        tv_goods_integral.setText("积分" + goodsDetailBean.getCredit() );
        tv_goods_price.setText("+¥" + goodsDetailBean.getMoney());
//        tv_goods_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        gv_spec = (MyGridView) view.findViewById(R.id.gv_spec);
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
                oldTextView.setTextColor(Color.parseColor("#ffffff"));
            }
            TextView textView = (TextView) view.findViewById(R.id.tv_spec);
            oldTextView = textView;
            textView.setBackgroundResource(R.drawable.tv_spec_bg_press_shape);
            textView.setTextColor(Color.parseColor("#ffffff"));

            specId = allOptionList.get(position).getId();
            marketprice = allOptionList.get(position).getMarketprice();

//            tv_goods_discount_price.setText("¥" + allOptionList.get(position).getMarketprice());
//            tv_goods_price.setText("¥" + allOptionList.get(position).getProductprice());
        }
    };

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.but_minus:
                    if (num == 1) {
                        ToastUtil.makeToast("至少选择一件");
                    } else {
                        num--;
                        tv_num.setText(num + "");
                        BigDecimal b1 = new BigDecimal(Float.toString(num));
                        BigDecimal b2 = new BigDecimal(Float.valueOf(marketprice));
                        allPrice = b1.multiply(b2).floatValue();
                    }
                    break;
                case R.id.but_add:
                    num++;
                    tv_num.setText(num + "");
                    BigDecimal b1 = new BigDecimal(Float.toString(num));
                    BigDecimal b2 = new BigDecimal(Float.valueOf(marketprice));
                    allPrice = b1.multiply(b2).floatValue();
                    break;
                case R.id.img_close:
                    dismiss();
                    break;
                case R.id.but_yes:

//                    goodsDetailBean.setGoodsNum(String.valueOf(num));
//                    goodsList.add(goodsDetailBean);

//                    OrderGoodsBean orderGoodsBean = new OrderGoodsBean();
//                    orderGoodsBean.setTotal(String.valueOf(num));
//                    orderGoodsBean.setOptionid(specId);
//                    orderGoodsBean.setMarketprice(marketprice);
//                    //orderGoodsBean.setCates(goodsDetailBean.getCates());
//                    orderGoodsBean.setGoodsid(goodsDetailBean.getId());
//                    orderGoodsList.add(orderGoodsBean);

//                    Intent intent = new Intent(context, BuyGoodsAct.class);
//                    intent.putExtra("data", (Serializable) goodsList);
//                    intent.putExtra("dataOrder", (Serializable) orderGoodsList);
//                    intent.putExtra("allPrice", String.valueOf(allPrice));
//                    intent.putExtra("flag" , "0");
//                    context.startActivity(intent);


                    if (specId != null && !specId.equals("")){

                        Intent intent = new Intent(context , BuyExChangeAct.class);
                        goodsDetailBean.setSpecId(specId);
                        intent.putExtra("bean" , goodsDetailBean);
                        context.startActivity(intent);
                        dismiss();

                    }else{

                        ToastUtil.makeToast("您未选择商品规格");
                        return;
                    }
                    break;
            }
        }
    };

    /**
     * 获取商品规格数据
     */
    private void standardData() {
        String goodId = goodsDetailBean.getId();
        final List<SpecBean> specList = new ArrayList<SpecBean>();
        final List<OptionBean> optionList = new ArrayList<OptionBean>();

        GetActivityListBiz.getIntegralStandard(goodId , openId , new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                if (result == null || result.equals("")){
                    return;
                }
                JSONObject jsonObject = new JSONObject(result);
                Gson gson = new Gson();
                if (jsonObject.getString("code").equals("200")) {
                    JSONObject dataObject = jsonObject.getJSONObject("message");

                    JSONArray jsonArray = dataObject.getJSONArray("specs");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    JSONArray jsonArray1 = jsonObject1.getJSONArray("items");
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        JSONObject jsonObject2 = jsonArray1.getJSONObject(i);
                        SpecBean bean = gson.fromJson(jsonObject2.toString(), SpecBean.class);
                        specList.add(bean);
                    }
                    LogUtil.i("规格数组长度" + specList.size());

                    JSONArray optionsArray = dataObject.getJSONArray("options");
                    for (int i = 0; i < optionsArray.length(); i++) {
                        JSONObject optionObject = optionsArray.getJSONObject(i);
                        OptionBean bean = gson.fromJson(optionObject.toString(), OptionBean.class);
                        optionList.add(bean);
                    }
                    LogUtil.i("规格数组长度" + specList.size() + "规格数组长度" + optionList.size());
                    handler.obtainMessage(100, specList).sendToTarget();
                    handler.obtainMessage(200, optionList).sendToTarget();
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }


    public void onDestory(){
        handler.removeCallbacksAndMessages(null);
        itemClickListener = null;
        clickListener = null;
        allOptionList = null;
        allSpecList = null;
    }

}
