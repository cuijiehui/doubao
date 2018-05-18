package com.xinspace.csevent.monitor.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xinspace.csevent.monitor.bean.LeaseApplyBean;
import com.xinspace.csevent.R;
import com.xinspace.csevent.util.ImagerLoaderUtil;

import java.util.List;

/**
 * Created by Android on 2017/7/18.
 */

public class LeaseApplyAdapter extends RecyclerView.Adapter<LeaseApplyAdapter.ViewHodler>{

    private List<LeaseApplyBean> list;
    private Context context;
    private LayoutInflater inflater;

    public LeaseApplyAdapter(List<LeaseApplyBean> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        LeaseApplyAdapter.ViewHodler viewHodler = new LeaseApplyAdapter.ViewHodler(inflater.inflate(R.layout.item_lease_apply , null));
        return viewHodler;
    }

    @Override
    public void onBindViewHolder(ViewHodler holder, int position) {

        final LeaseApplyBean bean = list.get(position);

        ImagerLoaderUtil.displayImage(bean.getPic() , holder.iv_room_pic);
        holder.tv_room_name.setText(bean.getTitle());
        holder.tv_room_address.setText(bean.getAddress());
        holder.tv_room_attr.setText(bean.getHouse_type() + "-" + bean.getAcreage() + "㎡-" + bean.getOrientations());

        if (bean.getType() != null && bean.getType().equals("已预约")){
            holder.tv_apply_type.setTextColor(Color.parseColor("#db3e26"));
        }else if (bean.getType() != null && bean.getType().equals("已过期")){
            holder.tv_apply_type.setTextColor(Color.parseColor("#2699db"));
        }else if(bean.getType() != null && bean.getType().equals("已完成")){
            holder.tv_apply_type.setTextColor(Color.parseColor("#37bb2a"));
        }


        holder.tv_apply_type.setText( bean.getType() );

        if (!TextUtils.isEmpty(bean.getCharge_pay())){
            holder.tv_room_attr1.setText(bean.getCharge_pay());
        }

        if (!TextUtils.isEmpty(bean.getDecoration())){
            holder.tv_room_attr2.setText(bean.getDecoration());
        }

        holder.tv_apply_time.setText(bean.getAppoint());

//        holder.rel_content.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context , LeaseDetailAct.class);
//                intent.putExtra("bean" , bean);
//                context.startActivity(intent);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHodler extends RecyclerView.ViewHolder {

        ImageView iv_room_pic;
        TextView tv_room_name;
        TextView tv_room_attr;
        TextView tv_apply_type;
        TextView tv_room_address;
        TextView tv_room_attr1;
        TextView tv_room_attr2;
        TextView tv_apply_time;
        RelativeLayout rel_content;

        public ViewHodler(View itemView) {
            super(itemView);
            iv_room_pic = (ImageView) itemView.findViewById(R.id.iv_room_pic);
            tv_room_name = (TextView) itemView.findViewById(R.id.tv_room_name);
            tv_room_attr = (TextView) itemView.findViewById(R.id.tv_room_attr);
            tv_apply_type = (TextView) itemView.findViewById(R.id.tv_apply_type);
            tv_room_address = (TextView) itemView.findViewById(R.id.tv_room_address);

            tv_room_attr1 = (TextView) itemView.findViewById(R.id.tv_room_attr1);
            tv_room_attr2 = (TextView) itemView.findViewById(R.id.tv_room_attr2);
            tv_apply_time = (TextView) itemView.findViewById(R.id.tv_apply_time);

            rel_content = (RelativeLayout) itemView.findViewById(R.id.rel_content);
        }
    }
}
