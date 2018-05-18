package com.xinspace.csevent.shop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.data.entity.ActivityListEntity;
import com.xinspace.csevent.data.entity.TimeLeftEntity;
import com.xinspace.csevent.util.ImagerLoaderUtil;

import java.util.List;

public class AwardsTypeContentAdapter extends BaseAdapter{
    private Context context;
    private List<Object> list;
    private LayoutInflater inflater;


    public AwardsTypeContentAdapter(Context context, List<Object> list) {
        this.context = context;
        this.list = list;
        this.inflater=LayoutInflater.from(context);
    }

    //更新数据
    public void updateData(List<Object> list){
        this.list=list;
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
        ViewHolder holder=null;
        if(convertView==null){
            holder=new ViewHolder();
            convertView = inflater.inflate(R.layout.item_awards_type_content_list, null);

            holder.iv_hot= (ImageView) convertView.findViewById(R.id.iv_main_hot_icon);
            holder.iv_photo= (ImageView) convertView.findViewById(R.id.iv_main_act_icon);
            holder.tv_title= (TextView) convertView.findViewById(R.id.tv_act_name);
            holder.tv_award_left= (TextView) convertView.findViewById(R.id.main_award_left);
            holder.tv_award_count= (TextView) convertView.findViewById(R.id.main_award_count);
            holder.tv_time_left= (TextView) convertView.findViewById(R.id.main_time_left);
            //活动状态
            holder.tv_not_start= (TextView) convertView.findViewById(R.id.tv_state_not_start);
            holder.tv_starting= (TextView) convertView.findViewById(R.id.tv_state_starting);
            holder.tv_end= (TextView) convertView.findViewById(R.id.tv_state_end);
            holder.tv_no_award= (TextView) convertView.findViewById(R.id.tv_state_no_award);

            convertView.setTag(holder);
        }
        holder= (ViewHolder) convertView.getTag();
        //数据
        ActivityListEntity enty = (ActivityListEntity) list.get(position);
        String name=enty.getName();
        String url=enty.getImage();
        String award_left=enty.getSurplus_prize();
        String award_count=enty.getWinners();
        int state = enty.getState();
        int is_hot=enty.getIs_top();

        //为了重用时,状态不会冲突,首先将所有的状态设置成隐藏
        holder.tv_end.setVisibility(View.GONE);
        holder.tv_not_start.setVisibility(View.GONE);
        holder.tv_starting.setVisibility(View.GONE);
        holder.tv_no_award.setVisibility(View.GONE);

        holder.iv_hot.setVisibility(View.GONE);

        //设置热门标记
        if(is_hot==1){
            holder.iv_hot.setVisibility(View.VISIBLE);
        }

        //活动状态
        if(state==1){//已经结束
            holder.tv_end.setVisibility(View.VISIBLE);
        }else if(state==2){//未开始
            holder.tv_not_start.setVisibility(View.VISIBLE);
        }else if(state==3){//进行中
            holder.tv_starting.setVisibility(View.VISIBLE);
        }else if(state==4){//奖品已发完
            holder.tv_no_award.setVisibility(View.VISIBLE);
        }

        //剩余时间
        String time_left=enty.getTime();
        TimeLeftEntity timeEnty=new TimeLeftEntity(Integer.parseInt(time_left));
        String days=timeEnty.getDays();
        String hours=timeEnty.getHoursWithDay();
        String mins=timeEnty.getMinWithDay();

        if(days.length()==1){
            days="0"+days;
        }
        if(hours.length()==1){
            hours="0"+hours;
        }
        if(mins.length()==1){
            mins="0"+mins;
        }

        ImagerLoaderUtil.displayImage(url,holder.iv_photo);
        holder.tv_title.setText(name);
        holder.tv_award_left.setText(award_left);
        holder.tv_award_count.setText(award_count+"人");
        holder.tv_time_left.setText(days+"天"+hours+"时"+mins+"分");

        return convertView;
    }

    class ViewHolder{
        ImageView iv_photo;
        ImageView iv_hot;//热门标记
        TextView tv_title;
        TextView tv_award_left;
        TextView tv_award_count;
        TextView tv_time_left;
        TextView tv_starting;//进行中
        TextView tv_end;//已经结束
        TextView tv_not_start;//未开始
        TextView tv_no_award;//奖品已经发完
    }
}
