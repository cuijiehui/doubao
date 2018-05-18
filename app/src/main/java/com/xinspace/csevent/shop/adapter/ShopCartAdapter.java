package com.xinspace.csevent.shop.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.app.utils.ScreenUtils;
import com.xinspace.csevent.data.biz.GetActivityListBiz;
import com.xinspace.csevent.login.view.MyHorizontalScrollView;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.shop.modle.ShopCartBean;
import com.xinspace.csevent.util.ImagerLoaderUtil;
import com.xinspace.csevent.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Android on 2017/5/4.
 */

public class ShopCartAdapter extends BaseAdapter {

    private Context context;
    private List<ShopCartBean> list;
    private int mScreentWidth;
    private View view;
    private HashMap<ShopCartBean, Boolean> isSelected;
    private Handler handler;
    private String openid;

    public ShopCartAdapter(Context context , HashMap<ShopCartBean, Boolean> isSelected , Handler handler , String openid) {
        this.context = context;
        this.isSelected = isSelected;
        this.handler = handler;
        this.openid = openid;
    }

    public void setList(List<ShopCartBean> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHodler viewHodler;
        if (convertView == null){
            viewHodler = new ViewHodler();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_shop_cart , null);

            viewHodler.hSView = (MyHorizontalScrollView) convertView.findViewById(R.id.horizontalScrollView);
            viewHodler.rel_content = (RelativeLayout) convertView.findViewById(R.id.rel_content);
            mScreentWidth = ScreenUtils.getScreenWidth(context);
            ViewGroup.LayoutParams lp = viewHodler.rel_content.getLayoutParams();
            lp.width = mScreentWidth;

            viewHodler.ll_action = (LinearLayout) convertView.findViewById(R.id.ll_action);
            viewHodler.cb_shop_cart = (CheckBox) convertView.findViewById(R.id.cb_shop_cart);
            viewHodler.iv_cart_goods = (ImageView) convertView.findViewById(R.id.iv_cart_goods);
            viewHodler.tv_cart_goods_name = (TextView) convertView.findViewById(R.id.tv_cart_goods_name);
            viewHodler.tv_cart_goods_spec = (TextView) convertView.findViewById(R.id.tv_cart_goods_spec);
            viewHodler.tv_cart_goods_price = (TextView) convertView.findViewById(R.id.tv_cart_goods_price);
            viewHodler.tv_cart_goods_num = (TextView) convertView.findViewById(R.id.tv_cart_goods_num);
            viewHodler.but_delete = (Button) convertView.findViewById(R.id.but_delete);

            convertView.setTag(viewHodler);
        }
        viewHodler = (ViewHodler) convertView.getTag();

        convertView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (view != null) {
                            ShopCartAdapter.ViewHodler viewHolder1 = (ShopCartAdapter.ViewHodler) view.getTag();
                            viewHolder1.hSView.smoothScrollTo(0, 0);
                            //notifyDataSetChanged();
                        }
                    case MotionEvent.ACTION_UP:
                        // 获得ViewHolder
                        ShopCartAdapter.ViewHodler viewHolder = (ShopCartAdapter.ViewHodler) v.getTag();
                        view = v;
                        // 获得HorizontalScrollView滑动的水平方向值.
                        int scrollX = viewHolder.hSView.getScrollX();
                        // 获得操作区域的长度
                        int actionW = viewHolder.ll_action.getWidth();
                        if (scrollX <= actionW / 2) {
                            viewHolder.hSView.smoothScrollTo(0, 0);
                        } else {
                            viewHolder.hSView.smoothScrollTo(actionW, 0);
                        }
                        return true;
                }
                return false;
            }
        });
        // 这里防止删除一条item后,ListView处于操作状态,直接还原
        if (viewHodler.hSView.getScrollX() != 0) {
            viewHodler.hSView.scrollTo(0, 0);
        }

        final ShopCartBean shopCart = list.get(position);
        ImagerLoaderUtil.displayImageWithLoadingIcon(shopCart.getThumb() , viewHodler.iv_cart_goods , R.drawable.icon_detail_load);
        viewHodler.tv_cart_goods_name.setText(shopCart.getTitle());
        if (shopCart.getOptiontitle() != null){
            viewHodler.tv_cart_goods_spec.setText("规格:" + shopCart.getOptiontitle());
        }else{
            viewHodler.tv_cart_goods_spec.setText("规格: 无");
        }

        viewHodler.tv_cart_goods_price.setText("¥" +  shopCart.getMarketprice());
        viewHodler.tv_cart_goods_num.setText( "×" + shopCart.getTotal());

        boolean b = isSelected.get(shopCart) != null && isSelected.get(shopCart) ? true : false;
        viewHodler.cb_shop_cart.setChecked(b);

        viewHodler.cb_shop_cart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isSelected.put(shopCart, isChecked);
                } else if (isSelected.containsKey(shopCart) && !isChecked) {
                    isSelected.remove(shopCart);
                }
                handler.obtainMessage(1).sendToTarget();
                handler.obtainMessage(10).sendToTarget();
            }
        });

        viewHodler.but_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeShopCart( openid , shopCart.getId() , shopCart);
            }
        });
        return convertView;
    }


    final static class ViewHodler{
        MyHorizontalScrollView hSView;
        RelativeLayout rel_content;
        LinearLayout ll_action;
        CheckBox cb_shop_cart;
        ImageView iv_cart_goods;
        TextView tv_cart_goods_name;
        TextView tv_cart_goods_spec;
        TextView tv_cart_goods_price;
        TextView tv_cart_goods_num;
        Button but_delete;
    }

    private void removeShopCart(String openId , final String id , final ShopCartBean shopCart){
        GetActivityListBiz.removeShopCartData(openId, id, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("code").equals("200")){
                    isSelected.remove(shopCart);
                    list.remove(shopCart);
                    handler.obtainMessage(1).sendToTarget();
                    handler.obtainMessage(10).sendToTarget();
                    handler.obtainMessage(500).sendToTarget();
                }else{
                    ToastUtil.makeToast("移除购物车失败");
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }
}
