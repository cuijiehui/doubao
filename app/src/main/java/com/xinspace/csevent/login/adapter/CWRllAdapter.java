package com.xinspace.csevent.login.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.login.model.CrowdWinRecord;
import com.xinspace.csevent.util.ImagerLoaderUtil;

import java.util.List;


/**
 * Created by Android on 2016/10/18.
 */
public class CWRllAdapter extends BaseAdapter{

    private Context context;
    private List<CrowdWinRecord> winRecordList;

    public void setWinRecordList(List<CrowdWinRecord> winRecordList) {
        this.winRecordList = winRecordList;
    }

    public CWRllAdapter(Context context) {
        this.context = context;
    }


    @Override
    public int getCount() {
        return winRecordList.size();
    }

    @Override
    public Object getItem(int position) {
        return winRecordList.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_crowd_win_all, null);
            holder.iv_photo = (ImageView) convertView.findViewById(R.id.iv_goods_image);
            holder.tv_win_time = (TextView) convertView.findViewById(R.id.tv_buy_time);
            holder.tv_goods_name = (TextView) convertView.findViewById(R.id.tv_goods_name);
            holder.tv_goods_price = (TextView) convertView.findViewById(R.id.tv_goods_price);
            holder.tv_goods_match = (TextView) convertView.findViewById(R.id.tv_goods_match);
            holder.tv_express_name = (TextView) convertView.findViewById(R.id.tv_express_name);
            holder.tv_express_num = (TextView) convertView.findViewById(R.id.tv_express_num);
            holder.tv_goods_all_price = (TextView) convertView.findViewById(R.id.tv_goods_all_price);
            holder.tv_crowd_noactivity = (TextView) convertView.findViewById(R.id.tv_crowd_noactivity);
            holder.tv_pay_state = (TextView) convertView.findViewById(R.id.tv_pay_state);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        CrowdWinRecord crowdWinRecord = winRecordList.get(position);

        ImagerLoaderUtil.displayImage(crowdWinRecord.getShowimg() , holder.iv_photo);
        holder.tv_win_time.setText(crowdWinRecord.getWintime());
        holder.tv_goods_name.setText(crowdWinRecord.getName());
        holder.tv_goods_price.setText("¥" + crowdWinRecord.getPrice());
        holder.tv_goods_match.setText(crowdWinRecord.getNumber_participation() + "次");
        holder.tv_crowd_noactivity.setText("期号" + crowdWinRecord.getNoactivity());

        holder.tv_goods_all_price.setText( "¥" + Integer.valueOf(crowdWinRecord.getPrice()) * Integer.valueOf(crowdWinRecord.getNumber_participation()));

        if (crowdWinRecord.getDname() == null){
            holder.tv_express_name.setVisibility(View.GONE);
        }else{
            holder.tv_express_name.setVisibility(View.VISIBLE);
            holder.tv_express_name.setText("物流公司 ：" + crowdWinRecord.getDname());
        }

        if (crowdWinRecord.getNumber() == null){
            holder.tv_express_num.setVisibility(View.GONE);
        }else{
            holder.tv_express_num.setVisibility(View.VISIBLE);
            holder.tv_express_num.setText("快递单号 ：" + crowdWinRecord.getNumber());
        }


        if (crowdWinRecord.getStart() != null && crowdWinRecord.getStart().equals("0")){
            holder.tv_pay_state.setVisibility(View.INVISIBLE);
        }else if (crowdWinRecord.getStart() != null &&crowdWinRecord.getStart().equals("1")){
            if(crowdWinRecord.getConfirm() != null &&crowdWinRecord.getConfirm().equals("0")) {
                holder.tv_pay_state.setVisibility(View.VISIBLE);
                holder.tv_pay_state.setText("确认收货");
            }else if (crowdWinRecord.getConfirm() != null &&crowdWinRecord.getConfirm().equals("1")){
                holder.tv_pay_state.setVisibility(View.INVISIBLE);
                holder.tv_pay_state.setText("确认收货");
            }
        }

        return convertView;
    }

    class ViewHolder {
        ImageView iv_photo;
        TextView tv_win_time;
        TextView tv_goods_name;
        TextView tv_goods_price;

        TextView tv_goods_match;

        TextView tv_express_name;
        TextView tv_express_num;

        TextView tv_goods_all_price;

        TextView tv_crowd_noactivity;

        TextView tv_pay_state;


    }
}
