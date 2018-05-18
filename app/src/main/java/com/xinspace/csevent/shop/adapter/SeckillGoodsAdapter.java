package com.xinspace.csevent.shop.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.shop.modle.JiuGoodsBean;
import com.xinspace.csevent.shop.modle.SeckillGoodsBean;
import com.xinspace.csevent.sweepstake.view.MyProgress;
import com.xinspace.csevent.util.ImagerLoaderUtil;
import com.xinspace.csevent.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android on 2017/3/19.
 */

public class SeckillGoodsAdapter extends BaseAdapter{

    private Context context;
    private List<SeckillGoodsBean> list = new ArrayList<>();
    private LayoutInflater inflater;
    private String status;

    public void setStatus(String status) {
        this.status = status;
    }

    public SeckillGoodsAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setList(List<SeckillGoodsBean> list) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_seckill_goods , null);
            holder.iv_goods_image= (ImageView) convertView.findViewById(R.id.iv_goods_image);
            holder.tv_goods_name= (TextView) convertView.findViewById(R.id.tv_goods_name);
            holder.tv_goods_price= (TextView) convertView.findViewById(R.id.tv_goods_price);
            holder.tv_goods_price2 = (TextView) convertView.findViewById(R.id.tv_goods_price2);
            holder.tv_goods_buy_num = (TextView) convertView.findViewById(R.id.tv_goods_buy_num);
            holder.progressBar1 = (MyProgress) convertView.findViewById(R.id.progressBar1);
            holder.tv_state = (TextView) convertView.findViewById(R.id.tv_state);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }

        SeckillGoodsBean seckillGoodsBean =  list.get(position);

        ImagerLoaderUtil.displayImageWithLoadingIcon(seckillGoodsBean.getThumb() , holder.iv_goods_image , R.drawable.goods_loading);
        holder.tv_goods_name.setText(seckillGoodsBean.getTitle());
        holder.tv_goods_price.setText("¥" + seckillGoodsBean.getPrice());
        holder.tv_goods_price2.setText( "¥" + seckillGoodsBean.getMarketprice());
        holder.tv_goods_price2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.tv_goods_buy_num.setText("已售" +  seckillGoodsBean.getPercen() + "%");
        holder.progressBar1.setProgress(Integer.valueOf(seckillGoodsBean.getPercen()));


        if (status.equals("0")){
            holder.tv_state.setText("抢购中");
            holder.tv_goods_buy_num.setVisibility(View.VISIBLE);
            holder.progressBar1.setVisibility(View.VISIBLE);
            holder.tv_state.setBackgroundResource(R.drawable.bt_pressed_shape);
        }else if (status.equals("1")){
            holder.tv_state.setText("等待抢购");
            holder.tv_state.setBackgroundResource(R.drawable.bt_pressed_green_shape);
            holder.tv_goods_buy_num.setVisibility(View.INVISIBLE);
            holder.progressBar1.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    static class ViewHolder{
        ImageView iv_goods_image;
        TextView tv_goods_name;
        TextView tv_goods_price;
        TextView tv_goods_price2;
        TextView tv_goods_buy_num;
        MyProgress progressBar1;
        TextView tv_state;
    }
}
