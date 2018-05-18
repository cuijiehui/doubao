package com.xinspace.csevent.login.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.login.activity.CrowRecordDetailAct;
import com.xinspace.csevent.login.model.CrowdRecord;
import com.xinspace.csevent.util.ImagerLoaderUtil;
import com.xinspace.csevent.util.LogUtil;

import java.util.List;


/**
 * Created by Android on 2016/10/17.
 */
public class CRAllAdapter extends BaseAdapter {
    private Context context;
    private List<CrowdRecord> recordList;

    public CRAllAdapter(Context context) {
        this.context = context;
    }

    public void setRecordList(List<CrowdRecord> recordList) {
        this.recordList = recordList;
    }

    @Override
    public int getCount() {
        return recordList.size();
    }

    @Override
    public Object getItem(int position) {
        return recordList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_crowd_record_all, null);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_duobao_record_name);
            holder.iv_photo = (ImageView) convertView.findViewById(R.id.iv_duobao_record_image);
            holder.tv_noactivity = (TextView) convertView.findViewById(R.id.tv_noactivity);
            holder.img_has_win = (ImageView) convertView.findViewById(R.id.img_has_win);
            holder.img_luck_icon = (ImageView) convertView.findViewById(R.id.img_luck_icon);
            holder.rel_item_content = (RelativeLayout) convertView.findViewById(R.id.rel_item_content);
            holder.tv_prize_valuee = (TextView) convertView.findViewById(R.id.tv_prize_valuee);
            holder.tv_attend_count = (TextView) convertView.findViewById(R.id.tv_duobao_record_attend_num);
            holder.tv_wins_num = (TextView) convertView.findViewById(R.id.tv_wins_num);
            holder.rel_item = (RelativeLayout) convertView.findViewById(R.id.rel_item);
            holder.tv_look_crowd_detail = (TextView) convertView.findViewById(R.id.tv_look_crowd_detail);
            holder.tv_crowd_win_user = (TextView) convertView.findViewById(R.id.tv_crowd_win_user);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        final CrowdRecord crowdRecord = recordList.get(position);
        String image = crowdRecord.getCommodity_img();
        String name = crowdRecord.getCommodity_name();
        String num = crowdRecord.getMatch();//活动参与次数
        String iswin = crowdRecord.getIswin();//是否中奖
        String winUserName = crowdRecord.getUsername();


        LogUtil.i("iswin" + iswin);

        //显示数据
        if (iswin.equals("1")) {
            holder.rel_item.setBackgroundColor(Color.parseColor("#eeeeee"));
            holder.rel_item_content.setBackgroundColor(Color.parseColor("#fae3ac"));
            holder.img_has_win.setVisibility(View.VISIBLE);
            holder.img_luck_icon.setVisibility(View.VISIBLE);
        }else if (iswin.equals("0")){
            holder.rel_item.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.rel_item_content.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.img_has_win.setVisibility(View.INVISIBLE);
            holder.img_luck_icon.setVisibility(View.INVISIBLE);
        }

        ImagerLoaderUtil.displayImage(image, holder.iv_photo);
        holder.tv_title.setText(name);
        holder.tv_attend_count.setText(num);
        holder.tv_noactivity.setText("期号：" + crowdRecord.getNoactivity());
        holder.tv_crowd_win_user.setText(winUserName);
        holder.tv_wins_num.setText(crowdRecord.getNumber_participation());

        holder.tv_look_crowd_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context , CrowRecordDetailAct.class);
                intent.putExtra("crowdRecord" , crowdRecord);
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHolder {
        ImageView iv_photo;
        TextView tv_title;
        TextView tv_attend_count;//参与次数
        TextView tv_noactivity;
        ImageView img_has_win;
        ImageView img_luck_icon;
        RelativeLayout rel_item_content;
        TextView tv_prize_valuee;
        TextView tv_wins_num;
        RelativeLayout rel_item;

        TextView tv_look_crowd_detail;
        TextView tv_crowd_win_user;

    }
}
