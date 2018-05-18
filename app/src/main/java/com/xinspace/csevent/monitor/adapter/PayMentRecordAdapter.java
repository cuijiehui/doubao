package com.xinspace.csevent.monitor.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinspace.csevent.monitor.bean.PayRecordBean;
import com.xinspace.csevent.R;

import java.util.List;

/**
 * 缴费记录
 *
 * Created by Android on 2017/5/26.
 */

public class PayMentRecordAdapter extends RecyclerView.Adapter<PayMentRecordAdapter.ViewHodler>{

    private Context content;
    private List<PayRecordBean> list;
    private LayoutInflater inflater;

    public PayMentRecordAdapter(Context content, List<PayRecordBean> list) {
        this.content = content;
        this.list = list;
        inflater = LayoutInflater.from(content);
    }


    @Override
    public ViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        PayMentRecordAdapter.ViewHodler holder = new PayMentRecordAdapter.ViewHodler(inflater.inflate(R.layout.item_pay_record_list, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHodler holder, int position) {

        PayRecordBean bean = list.get(position);

        holder.tv_payment_name.setText(bean.getInstruct());
        holder.tv_record_price.setText("¥" + bean.getSum());

        String status = bean.getPay_status();
        if (status.equals("1")){
            holder.tv_payment_status.setText("未缴费");
        }else if (status.equals("2")){
            holder.tv_payment_status.setText("已缴费");
        }

        String cate = bean.getCate();

        if (cate.equals("水费")) {
            holder.iv_payment_type.setImageResource(R.drawable.icon_pay_water_record);
        }else if (cate.equals("电费")) {
            holder.iv_payment_type.setImageResource(R.drawable.icon_energy_charge_record);
        }else if (cate.equals("煤气费")) {
            holder.iv_payment_type.setImageResource(R.drawable.icon_fuel_gas_record);
        }else if (cate.equals("物业费")) {
            holder.iv_payment_type.setImageResource(R.drawable.icon_property_fee_record);
        }else if (cate.equals("停车费")) {
            holder.iv_payment_type.setImageResource(R.drawable.icon_parking_charge_record);
        }else if (cate.equals("管理费")) {
            holder.iv_payment_type.setImageResource(R.drawable.icon_pay_other_record);
        }else{
            holder.iv_payment_type.setImageResource(R.drawable.icon_pay_other_record);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHodler extends RecyclerView.ViewHolder {
        ImageView iv_payment_type;
        TextView tv_payment_name;
        TextView tv_payment_status;
        TextView tv_record_price;

        public ViewHodler(View itemView) {
            super(itemView);

            iv_payment_type = (ImageView) itemView.findViewById(R.id.iv_payment_type);

            tv_payment_name = (TextView) itemView.findViewById(R.id.tv_payment_name);
            tv_payment_status = (TextView) itemView.findViewById(R.id.tv_payment_status);
            tv_record_price = (TextView) itemView.findViewById(R.id.tv_record_price);

        }
    }


}
