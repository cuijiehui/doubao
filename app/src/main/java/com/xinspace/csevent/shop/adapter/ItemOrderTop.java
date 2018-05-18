package com.xinspace.csevent.shop.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.shop.activity.OrderDetailAct;
import com.xinspace.csevent.shop.modle.AllOrderBean;
import com.xinspace.csevent.shop.weiget.OrderContent;

/**
 * 订单布局-top
 * 作者：fly on 2016/8/24 0024 23:45
 * 邮箱：cugb_feiyang@163.com
 */
public class ItemOrderTop implements OrderContent {

    private AllOrderBean allOrderBean;

    public ItemOrderTop(AllOrderBean allOrderBean) {
        this.allOrderBean = allOrderBean;
    }

    @Override
    public int getLayout() {
        return R.layout.item_order_top;
    }

    @Override
    public boolean isClickAble() {
        return true;
    }

    @Override
    public View getView(final Context mContext, View convertView, LayoutInflater inflater) {
        inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(getLayout(), null);

        TextView tv_store_name = (TextView) convertView.findViewById(R.id.tv_store_name);
        TextView tv_order_state = (TextView) convertView.findViewById(R.id.tv_order_state);

        tv_store_name.setText("拾得商城");

        String status = allOrderBean.getStatus();
        String paytype = allOrderBean.getPaytype();
        int sendtype = Integer.valueOf(allOrderBean.getSendtype());
        int refundstate = Integer.valueOf(allOrderBean.getRefundstate());

//
//        if (state.equals("0")){
//            tv_order_state.setText("待付款");
//        }else if(state.equals("1")){
//            tv_order_state.setText("待发货");
//        }else if (state.equals("2")){
//            tv_order_state.setText("待收货");
//        }else if (state.equals("3")){
//            tv_order_state.setText("已完成");
//        }else if(state.equals("-1")){
//            tv_order_state.setText("订单已取消");
//        }


        if (status.equals("0")){
            if (!paytype.equals("") && paytype.equals("3")) {
                //货到付款，待发货
                tv_order_state.setText("货到付款,待发货");
            } else {
                //等待付款
                tv_order_state.setText("待付款");
            }
        }

        if (status.equals("1")){
            if (sendtype > 0){
                //部分商品已发货
                tv_order_state.setText("部分商品已发货");
            }else{
                //买家已付款
                tv_order_state.setText("待发货");
            }
        }

        if (status.equals("2")){
            //卖家已发货
            tv_order_state.setText("待收货");
        }

        if (status.equals("3")){
            // 交易已完成
            tv_order_state.setText("已完成");
        }

        if (status.equals("-1")){
            // 交易取消
            tv_order_state.setText("订单已取消");
        }

        if(refundstate > 0){
            if (status.equals("1")){
                //申请退款
                tv_order_state.setText("申请退款");
            }else{
                // 申请售后
                tv_order_state.setText("申请售后");
            }
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext , OrderDetailAct.class);
                intent.putExtra("orderId" , allOrderBean.getId());
                mContext.startActivity(intent);
            }
        });
        //TODO 数据展示
        return convertView;
    }
}
