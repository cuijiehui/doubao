package com.xinspace.csevent.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.data.entity.AwardsRecordEntity;
import com.xinspace.csevent.baskorder.activity.AddBaskOrderAct;
import com.xinspace.csevent.util.ImagerLoaderUtil;

import java.util.List;

/**
 * Created by Lizhihong on 2015/12/24.
 * 中奖记录列表适配器
 */
public class AwardsAdapter extends BaseAdapter{

    private List<Object> list;
    private Context context;
    public AwardsAdapter (Context context,List<Object> list){
        this.list=list;
        this.context=context;
    }
    public void updateData(List<Object> list){
        this.list=list;
        notifyDataSetChanged();
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
        ViewHolder holder=null;
        if (convertView==null){
            convertView=View.inflate(context, R.layout.item_awards_record_list,null);
            holder=new ViewHolder();
            holder.ivImage=(ImageView)convertView.findViewById(R.id.iv_awards_record_image);
            holder.tvName=(TextView)convertView.findViewById(R.id.tv_awards_record_name);
            holder.tvTime=(TextView)convertView.findViewById(R.id.tv_awards_record_time);
            holder.tvAttendNum=(TextView)convertView.findViewById(R.id.tv_awards_record_attend_num);
            holder.tvStart =(TextView)convertView.findViewById(R.id.tv_awards_record_state);
            holder.tvPrice= (TextView) convertView.findViewById(R.id.tv_awards_record_price);
            holder.tv_awards_noactivity = (TextView) convertView.findViewById(R.id.tv_awards_noactivity);
            holder.tv_share_order = (TextView) convertView.findViewById(R.id.tv_share_order);
            convertView.setTag(holder);
        }
        holder=(ViewHolder)convertView.getTag();
        //给holder中的控件赋值
        final AwardsRecordEntity enty = (AwardsRecordEntity) list.get(position);
        ImagerLoaderUtil.displayImage(enty.getImage(), holder.ivImage);
        holder.tvName.setText(enty.getPname());//设置获得名称
        holder.tvTime.setText(enty.getStartdate());//设置揭晓时间
        holder.tvAttendNum.setText(enty.getMatch());//设置参与次数
        holder.tvPrice.setText("¥" +enty.getPrice());
        holder.tv_awards_noactivity.setText( "期号" + enty.getNoactivity());

        String start = enty.getStart();
        String userConfirm=enty.getUser_confirm();
        switch (start){
            case "0"://未发货
                holder.tvStart.setText("等待商品派发");
                break;
            case "1"://已经发货
                if(enty.getConfirm().equals("0")){//用户未确认派送
                    holder.tvStart.setText("商品派发中");
                }else if (enty.getConfirm().equals("1")){//用户已经确认派送
                    holder.tvStart.setText("已签收");
                    holder.tv_share_order.setVisibility(View.VISIBLE);
                }
                break;
        }

        holder.tv_share_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context , AddBaskOrderAct.class);
                intent.putExtra("data" , enty);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    class ViewHolder{
        ImageView ivImage;
        TextView tvName;
        TextView tvTime;
        TextView tvAttendNum;
        TextView tvStart;
        TextView tvPrice;
        TextView tv_awards_noactivity;
        TextView tv_share_order;
    }
}
