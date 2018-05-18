package com.xinspace.csevent.shop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.shop.modle.CouponBean;

import java.util.List;

/**
 * Created by Android on 2017/4/15.
 */

public class CouponAdapter extends BaseAdapter{

    private Context context;
    private List<CouponBean> list;

    public CouponAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<CouponBean> list) {
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
        ViewHodler viewHodler = null;
        if (convertView == null){
            viewHodler = new ViewHodler();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_coupon , null);
            viewHodler.tv_coupon_price = (TextView) convertView.findViewById(R.id.tv_coupon_price);
            viewHodler.tv_get_coupon = (TextView) convertView.findViewById(R.id.tv_get_coupon);
            viewHodler.tv_coupon_type = (TextView) convertView.findViewById(R.id.tv_coupon_type);
            viewHodler.tv_coupon_reduce_price = (TextView) convertView.findViewById(R.id.tv_coupon_reduce_price);
            viewHodler.tv_coupon_time = (TextView) convertView.findViewById(R.id.tv_coupon_time);
            convertView.setTag(viewHodler);
        }else{
            viewHodler = (ViewHodler) convertView.getTag();
        }
//        viewHodler.tv_coupon_price.setText("");
//        viewHodler.tv_get_coupon.setText("");
//        viewHodler.tv_coupon_type.setText("");
//        viewHodler.tv_coupon_reduce_price.setText("");
//        viewHodler.tv_coupon_time.setText("");
        return convertView;
    }

    static class ViewHodler{
        TextView tv_coupon_price;
        TextView tv_get_coupon;
        TextView tv_coupon_type;
        TextView tv_coupon_reduce_price;
        TextView tv_coupon_time;
    }

}
