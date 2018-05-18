package com.xinspace.csevent.shop.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.shop.modle.JiuGoodsBean;
import com.xinspace.csevent.util.ImagerLoaderUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android on 2017/3/19.
 */

public class JiuGoodsAdapter extends BaseAdapter{

    private Context context;
    private List<JiuGoodsBean> list = new ArrayList<>();
    private LayoutInflater inflater;

    public JiuGoodsAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setList(List<JiuGoodsBean> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        //Log.i("www" , "11111111111list" + list.size());
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_jiu_goods , null);
            holder.iv_goods_image= (ImageView) convertView.findViewById(R.id.iv_goods_image);
            holder.tv_goods_name= (TextView) convertView.findViewById(R.id.tv_goods_name);
            holder.tv_goods_price= (TextView) convertView.findViewById(R.id.tv_goods_price);
            holder.tv_goods_price2 = (TextView) convertView.findViewById(R.id.tv_goods_price2);
            holder.tv_goods_buy_num = (TextView) convertView.findViewById(R.id.tv_goods_buy_num);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }

        JiuGoodsBean jiuGoodsBean =  list.get(position);

        ImagerLoaderUtil.displayImageWithLoadingIcon(jiuGoodsBean.getThumb() , holder.iv_goods_image , R.drawable.goods_loading);
        holder.tv_goods_name.setText(jiuGoodsBean.getTitle());
        holder.tv_goods_price.setText("¥" + jiuGoodsBean.getPrice());
        holder.tv_goods_price2.setText( "原价" + jiuGoodsBean.getProductprice() + "元");
        holder.tv_goods_price2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.tv_goods_buy_num.setText("销量" + jiuGoodsBean.getSales());

        return convertView;
    }

    static class ViewHolder{
        ImageView iv_goods_image;
        TextView tv_goods_name;
        TextView tv_goods_price;
        TextView tv_goods_price2;
        TextView tv_goods_buy_num;
    }
}
