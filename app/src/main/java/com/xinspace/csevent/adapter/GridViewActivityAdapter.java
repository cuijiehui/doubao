package com.xinspace.csevent.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.data.entity.ActivityListEntity;
import com.xinspace.csevent.data.entity.TimeLeftEntity;
import com.xinspace.csevent.sweepstake.activity.ActDetailActivity2;
import com.xinspace.csevent.util.ImagerLoaderUtil;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.ui.activity.AwardPoolActivity;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 首页活动adapter
 */
public class GridViewActivityAdapter extends BaseAdapter{

    private Context context;
    private List<Object> list;

    private static final Timer timer=new Timer();
    private static final int HANDLER_UPDATE_DATA = 100;
    private final Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            notifyDataSetChanged();
        }
    };
    public GridViewActivityAdapter(Context context, List<Object> list) {
        this.context = context;
        this.list = list;
        //创建计时器
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                refreshTime();
            }
        },0,1000);
    }
    //倒计时
    private void refreshTime() {
        for (Object enty:list){
            String time = ((ActivityListEntity) enty).getTime();
            int lasttime = Integer.parseInt(time);
            if(lasttime>0){
                ((ActivityListEntity) enty).setTime(String.valueOf(lasttime- 1));
            }
        }
        handler.sendEmptyMessage(HANDLER_UPDATE_DATA);
    }

    public void updateData(List<Object> list){
        this.list=list;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        if (list.size() % 2 == 0 ){
            return list.size()/2;
        }else{
            return (list.size() + 1) / 2;
        }
       //return list.size()/2;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            holder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.item_main_page_activity_list,null);
            holder.iv_hot= (ImageView) convertView.findViewById(R.id.iv_hot_icon);
            holder.iv_logo= (ImageView) convertView.findViewById(R.id.iv_main_activity_logo);
            holder.tv_name= (TextView) convertView.findViewById(R.id.tv_main_activity_name);
            holder.tv_count= (TextView) convertView.findViewById(R.id.tv_main_award_count);
            holder.tv_time= (TextView) convertView.findViewById(R.id.tv_main_time_left);
            holder.tv_state= (TextView) convertView.findViewById(R.id.tv_main_activity_state);

            holder.iv_hot1= (ImageView) convertView.findViewById(R.id.iv_hot_icon1);
            holder.iv_logo1= (ImageView) convertView.findViewById(R.id.iv_main_activity_logo1);
            holder.tv_name1= (TextView) convertView.findViewById(R.id.tv_main_activity_name1);
            holder.tv_count1= (TextView) convertView.findViewById(R.id.tv_main_award_count1);
            holder.tv_time1= (TextView) convertView.findViewById(R.id.tv_main_time_left1);
            holder.tv_state1= (TextView) convertView.findViewById(R.id.tv_main_activity_state1);

            holder.item= (LinearLayout) convertView.findViewById(R.id.ll_main_item);
            holder.item1= (LinearLayout) convertView.findViewById(R.id.ll_main_item1);

            holder.rel_time_left = (RelativeLayout) convertView.findViewById(R.id.rel_time_left);
            holder.rel_time_right = (RelativeLayout) convertView.findViewById(R.id.rel_time_right);

            convertView.setTag(holder);
        }
        holder= (ViewHolder) convertView.getTag();
        //数据
        //position=0  0 1
        //position=1  2 3
        //position=2  4 5
        //position=3  6 7
        //position=4  8 9


        final ActivityListEntity enty = (ActivityListEntity) list.get(position + position);
        //活动的状态
        int state = enty.getState();
        if(state == 1){
            //已经结束
            holder.tv_state.setText("活动已经结束");
            holder.tv_state.setVisibility(View.VISIBLE);
            holder.rel_time_left.setVisibility(View.GONE);
            holder.tv_state.setTextColor(context.getResources().getColor(R.color.color_text_base));
        }else if(state==2){
            //未开始
            holder.tv_state.setText("未开始");
            holder.tv_state.setVisibility(View.VISIBLE);
            holder.rel_time_left.setVisibility(View.GONE);
            holder.tv_state.setTextColor(context.getResources().getColor(R.color.color_text_base));
        }else if(state==3){
            //进行中
//            holder.tv_state.setText("进行中");
//            holder.tv_state.setTextColor(context.getResources().getColor(R.color.color_text_base));

            holder.tv_state1.setVisibility(View.GONE);
            holder.rel_time_right.setVisibility(View.VISIBLE);
        }else if(state==4){
            //奖品已发完
            holder.tv_state.setVisibility(View.VISIBLE);
            holder.rel_time_left.setVisibility(View.GONE);
            holder.tv_state.setText("奖品已发完");
            holder.tv_state.setTextColor(context.getResources().getColor(R.color.color_text_base));
        }


        ImagerLoaderUtil.displayImage(enty.getImage(),holder.iv_logo);
        holder.tv_name.setText(enty.getName());
        holder.tv_count.setText(enty.getWinners());

        //为了重用view,在使用之前重置一下
        holder.iv_hot.setVisibility(View.GONE);

        int is_hot = enty.getIs_top();

        if(is_hot==1){//热门
            holder.iv_hot.setVisibility(View.GONE);
        }

        //剩余时间
        String time_left = enty.getTime();

        TimeLeftEntity timeEnty=new TimeLeftEntity(Integer.parseInt(time_left));
        String days=timeEnty.getDays();
        String hours=timeEnty.getHoursWithDay();
        String mins=timeEnty.getMinWithDay();
        String second=timeEnty.getSecondsWithDay();

        if(days.length()==1){
            days="0"+days;
        }
        if(hours.length()==1){
            hours="0"+hours;
        }
        if(mins.length()==1){
            mins="0"+mins;
        }
        if(second.length()==1){
            second="0"+second;
        }
        holder.tv_time.setText(days+"天 "+hours+": "+mins+": "+second);
        //设置监听
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type=enty.getType();
                LogUtil.i("活动抽奖类型:"+type);
                Intent intent=null;
                if(type.equals("1")){
                    //普通抽奖
                    intent=new Intent(context,ActDetailActivity2.class);

                }else if(type.equals("4")){
                    //游戏抽奖
                    intent=new Intent(context, AwardPoolActivity.class);
                }
                intent.putExtra("data",enty);
                context.startActivity(intent);
            }
        });

        if (list.size() % 2 == 0){
            holder.item1.setVisibility(View.VISIBLE);
            final ActivityListEntity enty1 = (ActivityListEntity) list.get(position + position + 1);
            int state1 = enty1.getState();
            if(state1==1){
                //已经结束
                holder.tv_state1.setText("活动已经结束");
                holder.tv_state1.setVisibility(View.VISIBLE);
                holder.rel_time_right.setVisibility(View.GONE);
                holder.tv_state1.setTextColor(context.getResources().getColor(R.color.color_text_base));
            }else if(state1==2){
                //未开始
                holder.tv_state1.setText("未开始");
                holder.tv_state1.setVisibility(View.VISIBLE);
                holder.rel_time_right.setVisibility(View.GONE);
                holder.tv_state1.setTextColor(context.getResources().getColor(R.color.color_text_base));
            }else if(state1==3){
                //进行中
//                holder.tv_state1.setText("进行中");
//                holder.tv_state1.setTextColor(context.getResources().getColor(R.color.color_text_base));

                holder.tv_state1.setVisibility(View.GONE);
                holder.rel_time_right.setVisibility(View.VISIBLE);

            }else if(state1==4){
                //奖品已发完
                holder.tv_state1.setText("奖品已发完");
                holder.tv_state1.setVisibility(View.VISIBLE);
                holder.rel_time_right.setVisibility(View.GONE);
                holder.tv_state1.setTextColor(context.getResources().getColor(R.color.color_text_base));
            }
            ImagerLoaderUtil.displayImage(enty1.getImage(),holder.iv_logo1);
            holder.tv_name1.setText(enty1.getName());
            holder.tv_count1.setText(enty1.getWinners());
            holder.iv_hot1.setVisibility(View.GONE);

            int is_hot1 = enty1.getIs_top();
            if(is_hot1==1){
                holder.iv_hot1.setVisibility(View.GONE);
            }
            String time_left1=enty1.getTime();
            TimeLeftEntity timeEnty1=new TimeLeftEntity(Integer.parseInt(time_left1));
            String days1=timeEnty1.getDays();
            String hours1=timeEnty1.getHoursWithDay();
            String mins1=timeEnty1.getMinWithDay();
            String second1=timeEnty1.getSecondsWithDay();
            if(days1.length()==1){
                days1="0"+days1;
            }
            if(hours1.length()==1){
                hours1="0"+hours1;
            }
            if(mins1.length()==1){
                mins1="0"+mins1;
            }
            if(second1.length()==1){
                second1="0"+second1;
            }
            holder.tv_time1.setText(days1+"天 "+hours1+": "+mins1+": "+second1);

            holder.item1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String type=enty1.getType();
                    LogUtil.i("活动抽奖类型:"+type);
                    Intent intent=null;
                    if(type.equals("1")){
                        //普通抽奖
                        intent=new Intent(context,ActDetailActivity2.class);

                    }else if(type.equals("4")){
                        //游戏抽奖
                        intent=new Intent(context, AwardPoolActivity.class);
                    }
                    intent.putExtra("data",enty1);
                    context.startActivity(intent);
                }
            });
        }else{

            if ((list.size() -1) / 2 != position ) {
                holder.item1.setVisibility(View.VISIBLE);
                final ActivityListEntity enty1 = (ActivityListEntity) list.get(position + position + 1);
                int state1 = enty1.getState();
                if (state1 == 1) {
                    //已经结束
                    holder.tv_state1.setText("活动已经结束");
                    holder.tv_state1.setVisibility(View.VISIBLE);
                    holder.rel_time_right.setVisibility(View.GONE);
                    holder.tv_state1.setTextColor(context.getResources().getColor(R.color.color_text_base));
                } else if (state1 == 2) {
                    //未开始
                    holder.tv_state1.setText("未开始");
                    holder.tv_state1.setVisibility(View.VISIBLE);
                    holder.rel_time_right.setVisibility(View.GONE);
                    holder.tv_state1.setTextColor(context.getResources().getColor(R.color.color_text_base));
                } else if (state1 == 3) {
                    //进行中
//                    holder.tv_state1.setText("进行中");
//                    holder.tv_state1.setTextColor(context.getResources().getColor(R.color.color_text_base));
                    holder.tv_state1.setVisibility(View.GONE);
                    holder.rel_time_right.setVisibility(View.VISIBLE);

                } else if (state1 == 4) {
                    //奖品已发完
                    holder.tv_state1.setText("奖品已发完");
                    holder.tv_state1.setVisibility(View.VISIBLE);
                    holder.rel_time_right.setVisibility(View.GONE);
                    holder.tv_state1.setTextColor(context.getResources().getColor(R.color.color_text_base));
                }
                ImagerLoaderUtil.displayImage(enty1.getImage(), holder.iv_logo1);
                holder.tv_name1.setText(enty1.getName());
                holder.tv_count1.setText(enty1.getWinners());
                holder.iv_hot1.setVisibility(View.GONE);

                int is_hot1 = enty1.getIs_top();
                if (is_hot1 == 1) {
                    holder.iv_hot1.setVisibility(View.GONE);
                }
                String time_left1 = enty1.getTime();
                TimeLeftEntity timeEnty1 = new TimeLeftEntity(Integer.parseInt(time_left1));
                String days1 = timeEnty1.getDays();
                String hours1 = timeEnty1.getHoursWithDay();
                String mins1 = timeEnty1.getMinWithDay();
                String second1 = timeEnty1.getSecondsWithDay();
                if (days1.length() == 1) {
                    days1 = "0" + days1;
                }
                if (hours1.length() == 1) {
                    hours1 = "0" + hours1;
                }
                if (mins1.length() == 1) {
                    mins1 = "0" + mins1;
                }
                if (second1.length() == 1) {
                    second1 = "0" + second1;
                }
                holder.tv_time1.setText(days1 + "天 " + hours1 + ": " + mins1 + ": " + second1);

                holder.item1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String type = enty1.getType();
                        LogUtil.i("活动抽奖类型:" + type);
                        Intent intent = null;
                        if (type.equals("1")) {
                            //普通抽奖
                            intent = new Intent(context, ActDetailActivity2.class);

                        } else if (type.equals("4")) {
                            //游戏抽奖
                            intent = new Intent(context, AwardPoolActivity.class);
                        }
                        intent.putExtra("data", enty1);
                        context.startActivity(intent);
                    }
                });
            }
        }

        return convertView;
    }

    class ViewHolder{
        ImageView iv_hot;//热门
        ImageView iv_logo;//活动logo
        TextView tv_name;//活动名称
        TextView tv_count;//中奖人数
        TextView tv_time;//剩余时间
        TextView tv_state;//活动状态

        ImageView iv_hot1;//热门
        ImageView iv_logo1;//活动logo
        TextView tv_name1;//活动名称
        TextView tv_count1;//中奖人数
        TextView tv_time1;//剩余时间
        TextView tv_state1;//活动状态

        LinearLayout item;//第一个item
        LinearLayout item1;//第二个item

        RelativeLayout rel_time_left;
        RelativeLayout rel_time_right;

    }
}
