package com.xinspace.csevent.shop.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.login.activity.LoginActivity;
import com.xinspace.csevent.shop.activity.GroupGoodsDetailAct;
import com.xinspace.csevent.shop.modle.GroupGoodsBean;
import com.xinspace.csevent.util.ImagerLoaderUtil;

import java.util.List;

/**
 *
 * Created by Android on 2017/6/12.
 */

public class GroupCateAdapter extends RecyclerView.Adapter<GroupCateAdapter.MyViewHolder>{

    private Context mContext;
    private LayoutInflater mIflater;
    private List<GroupGoodsBean> list;
    private SDPreference preference;

    public GroupCateAdapter(Context mContext, List<GroupGoodsBean> list) {
        this.mContext = mContext;
        this.list = list;
        mIflater = LayoutInflater.from(mContext);
        preference = SDPreference.getInstance();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        GroupCateAdapter.MyViewHolder holder = new GroupCateAdapter.MyViewHolder(mIflater.inflate(R.layout.item_group_goods, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final GroupGoodsBean goodsBean = list.get(position);
        ImagerLoaderUtil.displayImageWithLoadingIcon(goodsBean.getThumb() , holder.iv_goods_image , R.drawable.goods_loading);
        holder.tv_goods_name.setText(goodsBean.getTitle());
        holder.tv_goods_price.setText("¥" + goodsBean.getGroupsprice());
        holder.tv_goods_price2.setText( "原价" + goodsBean.getPrice() + "元");
        holder.tv_goods_price2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.tv_goods_buy_num.setText(goodsBean.getSales() + "人付款");

//
        holder.lin_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(mContext , MyCroupDetailAct.class);
//                intent.putExtra("bean" , bean);
//                mContext.startActivity(intent);

                String uid = preference.getContent("userId");

                if (!uid.equals("0")){
                    Intent intent = new Intent(mContext , GroupGoodsDetailAct.class);
                    intent.putExtra("goodId" , list.get(position).getId());
                    mContext.startActivity(intent);
                } else {
                    Intent intent = new Intent(mContext ,LoginActivity.class);
                    mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_goods_image;
        TextView tv_goods_name;
        TextView tv_goods_price;
        TextView tv_goods_price2;
        TextView tv_goods_buy_num;
        TextView tv_goto_group;
        LinearLayout lin_content;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv_goods_image= (ImageView) itemView.findViewById(R.id.iv_goods_image);
            tv_goods_name= (TextView) itemView.findViewById(R.id.tv_goods_name);
            tv_goods_price= (TextView) itemView.findViewById(R.id.tv_goods_price);
            tv_goods_price2 = (TextView) itemView.findViewById(R.id.tv_goods_price2);
            tv_goods_buy_num = (TextView) itemView.findViewById(R.id.tv_goods_buy_num);
            tv_goto_group = (TextView) itemView.findViewById(R.id.tv_goto_group);
            lin_content = (LinearLayout) itemView.findViewById(R.id.lin_content);

        }
    }
}
