package com.xinspace.csevent.sweepstake.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xinspace.csevent.R;
import com.xinspace.csevent.app.utils.ScreenUtils;
import com.xinspace.csevent.shop.modle.FristGoodsBean;
import com.xinspace.csevent.util.ImagerLoaderUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android on 2017/3/19.
 */

public class FristGoodsAdapter2 extends BaseAdapter{

    private Context context;
    private List<FristGoodsBean> list = new ArrayList<>();
    private LayoutInflater inflater;
    private int screenWidth;

    public FristGoodsAdapter2(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        screenWidth = ScreenUtils.getScreenWidth(context);
    }

    public void setList(List<FristGoodsBean> list) {
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
            convertView = inflater.inflate(R.layout.item_wechat_goods2 , null);
            holder.lin_content = (LinearLayout) convertView.findViewById(R.id.lin_content);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.lin_content.getLayoutParams();
            params.width = (int) (screenWidth * 0.4f);

            holder.iv_goods_image= (ImageView) convertView.findViewById(R.id.iv_goods_image);
            holder.tv_goods_name= (TextView) convertView.findViewById(R.id.tv_goods_name);
            holder.tv_goods_price= (TextView) convertView.findViewById(R.id.tv_goods_price);
            holder.tv_goods_price2 = (TextView) convertView.findViewById(R.id.tv_goods_price2);
            holder.tv_goods_buy_num = (TextView) convertView.findViewById(R.id.tv_goods_buy_num);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }

        FristGoodsBean goodsEntity =  list.get(position);
        Glide.with(context)
                .load(goodsEntity.getThumb())
                .placeholder(R.drawable.goods_loading)
                .error(R.drawable.goods_loading)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.iv_goods_image);

        holder.tv_goods_name.setText(goodsEntity.getTitle());
        holder.tv_goods_price.setText("¥" + goodsEntity.getPrice());
        holder.tv_goods_price2.setText( "原价" + goodsEntity.getProductprice());
        holder.tv_goods_price2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        holder.tv_goods_buy_num.setVisibility(View.GONE);
        holder.tv_goods_buy_num.setText(goodsEntity.getTotal() + "人付款");

        return convertView;
    }

    static class ViewHolder{
        LinearLayout lin_content;
        ImageView iv_goods_image;
        TextView tv_goods_name;
        TextView tv_goods_price;
        TextView tv_goods_price2;
        TextView tv_goods_buy_num;
    }
}
