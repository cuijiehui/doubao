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
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.data.entity.TimeLeftEntity2;
import com.xinspace.csevent.publish.activity.PublishDetailAct;
import com.xinspace.csevent.publish.model.PublishBean;
import com.xinspace.csevent.util.ImagerLoaderUtil;
import com.xinspace.csevent.util.LogUtil;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 首页活动adapter
 */
public class PublishAdapter extends BaseAdapter{

    private Context context;

    private List<PublishBean> publishList;

    public void setPublishList(List<PublishBean> publishList) {
        this.publishList = publishList;
    }

    public PublishAdapter(Context context) {
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
                if(lasttime > 0 && lasttime < 30 * 60 * 1000 && lasttime2 > 0){
                    enty.setDownTime(lasttime2 - 100);
                }else{
                    enty.setDownTime(0);
                }
            }
        }
        handler.sendEmptyMessage(HANDLER_UPDATE_DATA);
    }

    @Override
    public int getCount() {
        if (publishList.size() % 2 == 0 ){
            return publishList.size()/2;
        }else{
            return (publishList.size() + 1) / 2;
        }
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
            convertView= LayoutInflater.from(context).inflate(R.layout.item_publish,null);
            holder=new ViewHolder();

            holder.iv_logo= (ImageView) convertView.findViewById(R.id.iv_main_activity_logo);
            holder.tv_name= (TextView) convertView.findViewById(R.id.tv_main_activity_name);
            holder.tv_noactivity_left = (TextView) convertView.findViewById(R.id.tv_noactivity_left);
            holder.tv_count_down_time_left = (TextView) convertView.findViewById(R.id.tv_count_down_time_left);

            holder.iv_logo1= (ImageView) convertView.findViewById(R.id.iv_main_activity_logo1);
            holder.tv_name1= (TextView) convertView.findViewById(R.id.tv_main_activity_name1);
            holder.tv_noactivity_right = (TextView) convertView.findViewById(R.id.tv_noactivity_right);
            holder.tv_count_down_time_right = (TextView) convertView.findViewById(R.id.tv_count_down_time_right);

            holder.item= (LinearLayout) convertView.findViewById(R.id.ll_main_item);
            holder.item1= (LinearLayout) convertView.findViewById(R.id.ll_main_item1);

            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }

        //数据
        //position=0  0 1
        //position=1  2 3
        //position=2  4 5
        //position=3  6 7
        //position=4  8 9

        final PublishBean enty =  publishList.get(position + position);
        //活动的状态
        ImagerLoaderUtil.displayImage(enty.getThumbnail(),holder.iv_logo);

        holder.tv_name.setText(enty.getName());
        holder.tv_noactivity_left.setText("期号" + enty.getNoactivity());

        if (enty.getDownTime() > 0 && enty.getDownTime() < 10 * 60 * 1000){
            TimeLeftEntity2 timeLeftEntity2 = new TimeLeftEntity2(enty.getDownTime());
            String mins=timeLeftEntity2.getStrMin();
            String second=timeLeftEntity2.getstrSecond();
            String milliSecond = timeLeftEntity2.getstrMilliSecond();
            holder.tv_count_down_time_left.setText(mins+": "+second + ": " + milliSecond);
        }else{
            holder.tv_count_down_time_left.setText("以开奖");
        }


        //设置监听
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.i("第一个");
                Intent intent = new Intent(context , PublishDetailAct.class);
                intent.putExtra("enty" , enty);
                context.startActivity(intent);
            }
        });

        if (publishList.size() % 2 == 0){
            holder.item1.setVisibility(View.VISIBLE);
            final PublishBean enty1 = (PublishBean) publishList.get(position + position + 1);
            ImagerLoaderUtil.displayImage(enty1.getThumbnail(),holder.iv_logo1);
            holder.tv_name1.setText(enty1.getName());
            holder.tv_noactivity_right.setText("期号" + enty1.getNoactivity());

            if (enty1.getDownTime() > 0 && enty1.getDownTime() < 10 * 60 * 1000){
                TimeLeftEntity2 timeLeftEntityRight = new TimeLeftEntity2(enty1.getDownTime());
                String mins2 = timeLeftEntityRight.getStrMin();
                String second2 = timeLeftEntityRight.getstrSecond();
                String milliSecond2 = timeLeftEntityRight.getstrMilliSecond();
                holder.tv_count_down_time_right.setText(mins2+": "+second2 + ": " + milliSecond2);
            }else{
                holder.tv_count_down_time_right.setText("以开奖");
            }

            holder.item1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    LogUtil.i("第二个");
                    Intent intent = new Intent(context , PublishDetailAct.class);
                    intent.putExtra("enty" , enty1);
                    context.startActivity(intent);
                }
            });
        }else{
            if ((publishList.size() -1) / 2 != position ){
                holder.item1.setVisibility(View.VISIBLE);
                final PublishBean enty1 = (PublishBean) publishList.get(position + position + 1);
                ImagerLoaderUtil.displayImage(enty1.getThumbnail(),holder.iv_logo1);
                holder.tv_name1.setText(enty1.getName());
                holder.tv_noactivity_right.setText("期号" + enty1.getNoactivity());

                if (enty1.getDownTime() > 0 && enty1.getDownTime() < 10 * 60 * 1000){
                    TimeLeftEntity2 timeLeftEntityRight = new TimeLeftEntity2(enty1.getDownTime());
                    String mins2 = timeLeftEntityRight.getStrMin();
                    String second2 = timeLeftEntityRight.getstrSecond();
                    String milliSecond2 = timeLeftEntityRight.getstrMilliSecond();
                    holder.tv_count_down_time_right.setText(mins2+": "+second2 + ": " + milliSecond2);
                }else{
                    holder.tv_count_down_time_right.setText("以开奖");
                }

                holder.item1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LogUtil.i("第三个");
                        Intent intent = new Intent(context , PublishDetailAct.class);
                        intent.putExtra("enty" , enty1);
                        context.startActivity(intent);
                    }
                });
            }
        }
        return convertView;
    }

    class ViewHolder{
        ImageView iv_logo;//活动logo
        TextView tv_name;//活动名称
        TextView tv_noactivity_left;
        TextView tv_count_down_time_left;

        ImageView iv_logo1;//活动logo
        TextView tv_name1;//活动名称
        TextView tv_noactivity_right;
        TextView tv_count_down_time_right;

        LinearLayout item;//第一个item
        LinearLayout item1;//第二个item
    }


}
