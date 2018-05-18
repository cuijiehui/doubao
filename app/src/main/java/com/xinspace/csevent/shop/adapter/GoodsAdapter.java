package com.xinspace.csevent.shop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.shop.modle.GoodsBean;
import com.xinspace.csevent.util.ImagerLoaderUtil;

import java.util.List;

/**
 * Created by Android on 2016/11/17.
 */
public class GoodsAdapter extends BaseAdapter {

    private Context context;
    private List<Object> list;
    private LayoutInflater inflater;

    public GoodsAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setList(List<Object> list) {
        this.list = list;
    }

    //更新数据
    public void updateData(List<Object> list) {
        this.list = list;
        this.notifyDataSetChanged();
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
            convertView = inflater.inflate(R.layout.item_good, null);

            holder.iv_photo = (ImageView) convertView.findViewById(R.id.iv_main_act_icon);
            holder.tv_goods_remark = (TextView) convertView.findViewById(R.id.tv_goods_remark);
            holder.tv_goods_name = (TextView) convertView.findViewById(R.id.tv_goods_name);
            holder.tv_buy_num = (TextView) convertView.findViewById(R.id.tv_buy_num);
            holder.tv_goods_detail = (TextView) convertView.findViewById(R.id.tv_goods_detail);
            //活动状态
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        GoodsBean goodsBean = (GoodsBean) list.get(position);

        holder.tv_goods_detail.setVisibility(View.GONE);
        ImagerLoaderUtil.displayImage(goodsBean.getImg_url(),holder.iv_photo);
        holder.tv_goods_name.setText(goodsBean.getName());
        holder.tv_goods_remark.setText(goodsBean.getRemark());
        holder.tv_buy_num.setText(goodsBean.getMatch());

        return convertView;
    }


    class ViewHolder{
        ImageView iv_photo;
        TextView tv_goods_remark;
        TextView tv_goods_name;
        TextView tv_buy_num;
        TextView tv_goods_detail;
    }

}
