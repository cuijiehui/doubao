package com.xinspace.csevent.shop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.shop.modle.ExGoodsBean;
import com.xinspace.csevent.shop.modle.GoodsBean;
import com.xinspace.csevent.util.ImagerLoaderUtil;

import java.util.List;

/**
 * Created by Android on 2016/11/17.
 */
public class ExGoodsAdapter extends BaseAdapter {

    private Context context;
    private List<ExGoodsBean> list;
    private LayoutInflater inflater;

    public ExGoodsAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setList(List<ExGoodsBean> list) {
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
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_good_ex, null);

            holder.iv_photo = (ImageView) convertView.findViewById(R.id.iv_main_act_icon);
            holder.tv_goods_name = (TextView) convertView.findViewById(R.id.tv_goods_name);
            holder.lin_2 = (LinearLayout) convertView.findViewById(R.id.lin_2);

            holder.tv_goods_jifen = (TextView) convertView.findViewById(R.id.tv_goods_jifen);
            holder.tv_goods_price = (TextView) convertView.findViewById(R.id.tv_goods_price);
            holder.tv_exchange = (TextView) convertView.findViewById(R.id.tv_exchange);

            //活动状态
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        ExGoodsBean goodsBean = list.get(position);

        ImagerLoaderUtil.displayImage(goodsBean.getThumb(),holder.iv_photo);
        holder.tv_goods_name.setText(goodsBean.getTitle());
        holder.tv_goods_jifen.setText(goodsBean.getCredit());
        holder.tv_goods_price.setText(goodsBean.getMoney());
        if (goodsBean.getMoney().equals("0.00")){
            holder.lin_2.setVisibility(View.INVISIBLE);
        }else if(!goodsBean.getMoney().equals("0.00")){
            holder.lin_2.setVisibility(View.VISIBLE);
        }

        return convertView;
    }


    class ViewHolder{
        LinearLayout lin_2;
        ImageView iv_photo;
        TextView tv_goods_name;
        TextView tv_goods_jifen;
        TextView tv_goods_price;
        TextView tv_exchange;
    }

}
