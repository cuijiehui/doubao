package com.xinspace.csevent.sweepstake.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.sweepstake.modle.BuyRecordBean;
import com.xinspace.csevent.util.ImagerLoaderUtil;

import java.util.List;

/**
 * Created by Android on 2016/10/12.
 */
public class CrowdNoRecevingAdapter extends BaseAdapter{

    private Context context;
    private List<BuyRecordBean> recordList;

    public CrowdNoRecevingAdapter(Context context) {
        this.context = context;
    }

    public void setRecordList(List<BuyRecordBean> recordList) {
        this.recordList = recordList;
    }

    @Override
    public int getCount() {
        return recordList.size();
    }

    @Override
    public Object getItem(int position) {
        return recordList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_buy_all , null);
            holder.iv_goods_image = (ImageView) convertView.findViewById(R.id.iv_goods_image);
            holder.tv_buy_time = (TextView) convertView.findViewById(R.id.tv_buy_time);
            holder.tv_time_state = (TextView) convertView.findViewById(R.id.tv_time_state);
            holder.tv_goods_name = (TextView) convertView.findViewById(R.id.tv_goods_name);
            holder.tv_goods_price = (TextView) convertView.findViewById(R.id.tv_goods_price);
            holder.tv_goods_num = (TextView) convertView.findViewById(R.id.tv_goods_num);
            holder.tv_goods_all_price = (TextView) convertView.findViewById(R.id.tv_goods_all_price);
            holder.tv_pay_state = (TextView) convertView.findViewById(R.id.tv_pay_state);

            holder.tv_express_name = (TextView) convertView.findViewById(R.id.tv_express_name);
            holder.tv_express_num = (TextView) convertView.findViewById(R.id.tv_express_num);

            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }

        ImagerLoaderUtil.displayImage(recordList.get(position).getShowimg() , holder.iv_goods_image);
        holder.tv_buy_time.setText(recordList.get(position).getWintime());
        holder.tv_goods_name.setText(recordList.get(position).getName());
        holder.tv_goods_price.setText("¥ " + recordList.get(position).getPrice());
        holder.tv_goods_num.setText(recordList.get(position).getMatch() + "件");
        int allPrice = Integer.valueOf(recordList.get(position).getPrice()) * Integer.valueOf(recordList.get(position).getMatch());
        holder.tv_goods_all_price.setText( "¥ " + allPrice+ "");

        if (recordList.get(position).getDname() == null){
            holder.tv_express_name.setVisibility(View.GONE);
        }else{
            holder.tv_express_name.setVisibility(View.VISIBLE);
            holder.tv_express_name.setText("物流公司 ：" +recordList.get(position).getDname());
        }

        if (recordList.get(position).getNumber() == null){
            holder.tv_express_num.setVisibility(View.GONE);
        }else{
            holder.tv_express_num.setVisibility(View.VISIBLE);
            holder.tv_express_num.setText("快递单号 ：" + recordList.get(position).getNumber());
        }

        if (recordList.get(position).getStart().equals("0")){
            holder.tv_pay_state.setVisibility(View.INVISIBLE);
        }else{
            holder.tv_pay_state.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    class ViewHolder{
        ImageView iv_goods_image;
        TextView tv_buy_time;
        TextView tv_time_state;
        TextView tv_goods_name;
        TextView tv_goods_price;
        TextView tv_goods_num;
        TextView tv_goods_all_price;
        TextView tv_pay_state;
        TextView tv_express_name;
        TextView tv_express_num;

    }

}
