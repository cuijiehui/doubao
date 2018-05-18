package com.xinspace.csevent.shop.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.shop.activity.MyCroupDetailAct;
import com.xinspace.csevent.shop.modle.GroupFailBean;
import com.xinspace.csevent.util.ImagerLoaderUtil;

import java.util.List;

/**
 * 拼团失败适配器
 *
 * Created by Android on 2017/6/12.
 */

public class GroupFailAdapter extends RecyclerView.Adapter<GroupFailAdapter.MyViewHolder>{

    private Context mContext;
    private LayoutInflater mIflater;
    private List<GroupFailBean> list;
    private String flag;

    public GroupFailAdapter(Context mContext, List<GroupFailBean> list , String flag) {
        this.mContext = mContext;
        this.list = list;
        this.flag = flag;
        mIflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        GroupFailAdapter.MyViewHolder holder = new GroupFailAdapter.MyViewHolder(mIflater.inflate(R.layout.item_group_fail, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final GroupFailBean bean = list.get(position);
        holder.tv_order_id.setText("订单号：" + bean.getOrderno());

        String status = bean.getStatus();
        if (flag.equals("0")){
            holder.tv_order_state.setText("拼团中");
        }else if (flag.equals("1")){
            holder.tv_order_state.setText("拼团完成");
        }else if (flag.equals("2")){
            holder.tv_order_state.setText("拼团失败");
        }

        holder.tv_look_detail.setText("查看详情");
        holder.tv_goods_name.setText(bean.getTitle());

        holder.tv_num.setText(":1(¥" + bean.getGroupsprice() + ")");
        holder.tv_goods_repertory.setText("库存：" + bean.getStock() + bean.getUnits());
        ImagerLoaderUtil.displayImageWithLoadingIcon(bean.getThumb() , holder.iv_goods_image , R.drawable.icon_detail_load);

        holder.tv_group_content.setText("运费： ¥0.0 , 共一个商品总额：" + bean.getGroupsprice());
        holder.tv_look_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext , MyCroupDetailAct.class);
                intent.putExtra("bean" , bean);
                mContext.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_look_detail;
        TextView tv_num;
        TextView tv_goods_repertory;
        TextView tv_goods_name;
        ImageView iv_goods_image;
        TextView tv_order_id;
        TextView tv_order_state;
        TextView tv_group_content;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_look_detail = (TextView) itemView.findViewById(R.id.tv_look_detail);
            tv_num = (TextView) itemView.findViewById(R.id.tv_num);
            tv_goods_repertory = (TextView) itemView.findViewById(R.id.tv_goods_spec);
            tv_goods_name = (TextView) itemView.findViewById(R.id.tv_goods_name);
            iv_goods_image = (ImageView) itemView.findViewById(R.id.iv_goods_image);

            tv_order_id = (TextView) itemView.findViewById(R.id.tv_order_id);
            tv_order_state = (TextView) itemView.findViewById(R.id.tv_order_state);

            tv_group_content = (TextView) itemView.findViewById(R.id.tv_group_content);
        }
    }
}
