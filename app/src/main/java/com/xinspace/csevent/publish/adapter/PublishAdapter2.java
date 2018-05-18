package com.xinspace.csevent.publish.adapter;

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
import com.xinspace.csevent.data.entity.TimeLeftEntity2;
import com.xinspace.csevent.publish.activity.PublishDetailAct;
import com.xinspace.csevent.publish.model.PublishBean;
import com.xinspace.csevent.util.ImagerLoaderUtil;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.ToastUtil;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 揭晓活动adapter
 */
public class PublishAdapter2 extends BaseAdapter{

    private Context context;

    private List<PublishBean> publishList;

    public void setPublishList(List<PublishBean> publishList) {
        this.publishList = publishList;
    }

    public PublishAdapter2(Context context) {
        this.context = context;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                refreshTime();
            }
        },0,100);
    }

    private static final int HANDLER_UPDATE_DATA = 100;

    private static final Timer timer=new Timer();

    private final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            notifyDataSetChanged();
        }
    };


    private void refreshTime() {
        if (publishList != null){
            for (PublishBean enty:publishList){
                long lasttime = enty.getChaTime();
                long lasttime2 = enty.getDownTime();
                if(lasttime > 0 && lasttime < 10 * 60 * 1000 && lasttime2 > 0){
                    enty.setDownTime(lasttime2 - 100);
                    handler.sendEmptyMessage(HANDLER_UPDATE_DATA);
                }else{
                    enty.setDownTime(0);
                }
            }
        }
    }

    @Override
    public int getCount() {
//        if (publishList.size() % 2 == 0 ){
//            return publishList.size()/2;
//        }else{
//            return (publishList.size() + 1) / 2;
//        }
        return publishList.size();
    }

    @Override
    public Object getItem(int position) {
        return publishList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_publish2,null);
            holder=new ViewHolder();

            holder.iv_logo= (ImageView) convertView.findViewById(R.id.iv_main_activity_logo);
            holder.tv_name= (TextView) convertView.findViewById(R.id.tv_main_activity_name);
            holder.tv_noactivity_left = (TextView) convertView.findViewById(R.id.tv_noactivity_left);
            holder.tv_count_down_time_left = (TextView) convertView.findViewById(R.id.tv_count_down_time_left);
            holder.tv_publish = (TextView) convertView.findViewById(R.id.tv_publish);

            holder.rel_content_1 = (RelativeLayout) convertView.findViewById(R.id.rel_content_1);
            holder.rel_content_2 = (RelativeLayout) convertView.findViewById(R.id.rel_content_2);
            holder.rel_content_3 = (RelativeLayout) convertView.findViewById(R.id.rel_content_3);
            holder.rel_content_4 = (RelativeLayout) convertView.findViewById(R.id.rel_content_4);
            holder.rel_content_5 = (RelativeLayout) convertView.findViewById(R.id.rel_content_5);

            holder.tv_win_user_name = (TextView) convertView.findViewById(R.id.tv_win_user_name);
            holder.tv_win_match = (TextView) convertView.findViewById(R.id.tv_win_match);
            holder.tv_luck_num = (TextView) convertView.findViewById(R.id.tv_luck_num);
            holder.tv_win_time = (TextView) convertView.findViewById(R.id.tv_win_time);

            holder.item= (LinearLayout) convertView.findViewById(R.id.ll_main_item);

            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }


        final PublishBean enty =  publishList.get(position);
        //活动的状态
        ImagerLoaderUtil.displayImage(enty.getThumbnail(),holder.iv_logo);

        holder.tv_name.setText(enty.getName());
        holder.tv_noactivity_left.setText("期号: " + enty.getNoactivity());

        LogUtil.i("enty.getDownTime" + enty.getDownTime());
        if (enty.getDownTime() > 0 && enty.getDownTime() < 10 * 60 * 1000){
            TimeLeftEntity2 timeLeftEntity2 = new TimeLeftEntity2(enty.getDownTime());
            String mins=timeLeftEntity2.getStrMin();
            String second=timeLeftEntity2.getstrSecond();
            String milliSecond = timeLeftEntity2.getstrMilliSecond();
            holder.tv_count_down_time_left.setText(mins+": "+second + ": " + milliSecond);
            holder.tv_publish.setText("正在揭晓");
        }else{
            holder.tv_count_down_time_left.setVisibility(View.GONE);
            holder.rel_content_1.setVisibility(View.GONE);

            holder.rel_content_2.setVisibility(View.VISIBLE);
            holder.rel_content_3.setVisibility(View.VISIBLE);
            holder.rel_content_4.setVisibility(View.VISIBLE);
            holder.rel_content_5.setVisibility(View.VISIBLE);

            holder.tv_win_user_name.setText(enty.getNickname());
            holder.tv_win_match.setText(enty.getNumber_participation());
            holder.tv_luck_num.setText(enty.getLottery_number());
            holder.tv_win_time.setText(enty.getWintime());
        }


        //设置监听
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enty.getDownTime() > 0 && enty.getDownTime() < 10 * 60 * 1000){
                    ToastUtil.makeToast("开奖时间未到");
                }else{
                    Intent intent = new Intent(context , PublishDetailAct.class);
                    intent.putExtra("enty" , enty);
                    context.startActivity(intent);
                }
            }
        });

        return convertView;
    }

    class ViewHolder{
        ImageView iv_logo;//活动logo
        TextView tv_name;//活动名称
        TextView tv_noactivity_left;
        TextView tv_count_down_time_left;
        TextView tv_publish;
        LinearLayout item;//第一个item

        RelativeLayout rel_content_1;
        RelativeLayout rel_content_2;
        RelativeLayout rel_content_3;
        RelativeLayout rel_content_4;
        RelativeLayout rel_content_5;

        TextView tv_win_user_name;
        TextView tv_win_match;
        TextView tv_luck_num;
        TextView tv_win_time;

    }


}
