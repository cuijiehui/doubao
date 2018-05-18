package com.xinspace.csevent.shop.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.shop.activity.OrderDetailAct;
import com.xinspace.csevent.shop.modle.OrderMiddleBean;
import com.xinspace.csevent.shop.weiget.OrderContent;
import com.xinspace.csevent.util.ImagerLoaderUtil;
import com.xinspace.csevent.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单布局-in
 * 作者：fly on 2016/8/24 0024 23:45
 * 邮箱：cugb_feiyang@163.com
 */
public class ItemOrderIn implements OrderContent {

    private List<OrderMiddleBean> list;
    private OrderMiddleBean orderMiddleBean;

    public ItemOrderIn(OrderMiddleBean orderMiddleBean ) {
        this.orderMiddleBean = orderMiddleBean;
        list = new ArrayList<>();
        list.add(orderMiddleBean);
    }

    @Override
    public int getLayout() {
        return R.layout.item_order_in;
    }

    @Override
    public boolean isClickAble() {
        return true;
    }

    @Override
    public View getView(final Context mContext, View convertView, LayoutInflater inflater) {
        inflater = LayoutInflater.from(mContext);
        convertView =  inflater.inflate(getLayout(),null);

        convertView.setBackgroundColor(Color.parseColor("#ffffff"));
        //TODO 数据展示-订单内容
        TextView nameTv = (TextView) convertView.findViewById(R.id.tv_goods_name);
        nameTv.setText(orderMiddleBean.getTitle());
        TextView snTv = (TextView) convertView.findViewById(R.id.tv_goods_spec);

        //LogUtil.i("规格是什么" + orderMiddleBean.getOptiontitle());

        if (!orderMiddleBean.getOptiontitle().equals("")){
            snTv.setText(orderMiddleBean.getOptiontitle());
        }else{
            snTv.setText("无");
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_goods_image);
        ImagerLoaderUtil.displayImageWithLoadingIcon(orderMiddleBean.getThumb() , imageView , R.drawable.icon_detail_load);


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext , OrderDetailAct.class);
                intent.putExtra("orderId" , orderMiddleBean.getOrderId());
                mContext.startActivity(intent);
            }
        });


        return convertView;
    }
}
