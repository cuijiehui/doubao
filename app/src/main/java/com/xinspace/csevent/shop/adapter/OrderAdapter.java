package com.xinspace.csevent.shop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.shop.modle.GoodsDetailBean;
import com.xinspace.csevent.app.AppConfig;
import com.xinspace.csevent.util.ImagerLoaderUtil;

import java.util.List;

/**
 * Created by Android on 2017/5/10.
 */

public class OrderAdapter extends BaseAdapter {

    private Context context;
    private List<GoodsDetailBean> goodsList;

    public OrderAdapter(Context context) {
        this.context = context;
    }

    public void setGoodsList(List<GoodsDetailBean> goodsList) {
        this.goodsList = goodsList;
    }

    @Override
    public int getCount() {
        return goodsList.size();
    }

    @Override
    public Object getItem(int position) {
        return goodsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler viewHodler = null;
        if (convertView == null){
            viewHodler = new ViewHodler();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_order , null);
            viewHodler.iv_order_image = (ImageView) convertView.findViewById(R.id.iv_order_image);
            viewHodler.tv_order_goods_name = (TextView) convertView.findViewById(R.id.tv_order_goods_name);
            viewHodler.tv_order_goods_prize = (TextView) convertView.findViewById(R.id.tv_order_goods_prize);
            viewHodler.text_order_standard = (TextView) convertView.findViewById(R.id.text_order_standard);
            viewHodler.tv_order_goods_num = (TextView) convertView.findViewById(R.id.tv_order_goods_num);
            convertView.setTag(viewHodler);
        }else{
            viewHodler = (ViewHodler) convertView.getTag();
        }

        GoodsDetailBean goodsDetailBean = goodsList.get(position);
        if (!goodsDetailBean.getThumb().contains(AppConfig.BaseNewUrl)){
            ImagerLoaderUtil.displayImage(AppConfig.BaseNewUrl + goodsDetailBean.getThumb() , viewHodler.iv_order_image);
        }else{
            ImagerLoaderUtil.displayImage(goodsDetailBean.getThumb() , viewHodler.iv_order_image);
        }

        viewHodler.tv_order_goods_num.setText("×" + goodsDetailBean.getGoodsNum());
        Float prize = Float.valueOf(goodsDetailBean.getMarketprice());
        viewHodler.tv_order_goods_name.setText(goodsDetailBean.getTitle());
        viewHodler.tv_order_goods_prize.setText("¥" + prize + "");

        String specTitle = goodsDetailBean.getSpecTitle();

        if (specTitle != null && !specTitle.equals("")){
            viewHodler.text_order_standard.setText("规格：" + goodsDetailBean.getSpecTitle());
        }else{
            viewHodler.text_order_standard.setText("规格：无");
        }
        return convertView;
    }


    final static class ViewHodler{

        ImageView iv_order_image;
        TextView tv_order_goods_name;
        TextView tv_order_goods_prize;
        TextView text_order_standard;
        TextView tv_order_goods_num;

    }

}
