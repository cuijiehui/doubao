package com.xinspace.csevent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.data.entity.GetAddressEntity;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.ui.activity.AddressManagerActivity;

import java.util.List;

/**
 * Created by lizhihong on 2015/11/26.
 * 此类为地址适配器
 */
public class AddressForGetAwardAdapter extends BaseAdapter implements HttpRequestListener{

    Context context;
    List<Object> list;

    LayoutInflater inflater;

    public AddressForGetAwardAdapter(Context context, List<Object> list) {
        this.context = context;
        this.list = list;
        this.inflater=LayoutInflater.from(context);
    }
    //更新数据
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
            convertView = inflater.inflate(R.layout.item_address_for_get_award_list,null);
            holder=new ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_address_manager_name);
            holder.tvPhoneNum = (TextView) convertView.findViewById(R.id.tv_address_manager_phone);
            holder.tvAddress = (TextView) convertView.findViewById(R.id.tv_address_manager_address);
            convertView.setTag(holder);
        }
        holder= (ViewHolder) convertView.getTag();
        //給holder中的控件赋值
        final GetAddressEntity entity=(GetAddressEntity)list.get(position);
        LogUtil.i("holder中的实体类:" + entity.toString());
        holder.tvName.setText(entity.getRealname());
        holder.tvPhoneNum.setText(entity.getMobile());
        holder.tvAddress.setText(entity.getAddress());

        //选择后对号变绿色,其他未选择变灰色
        ImageView ivDuihao=(ImageView)convertView.findViewById(R.id.iv_get_award);
        if(mSelect==position){
            ivDuihao.setImageResource(R.drawable.iv_duihao_green);
        }else{
            ivDuihao.setImageResource(R.drawable.iv_duihao_grey);
        }

        return convertView;
    }

    int mSelect = -1;   //选中项

    public void changeSelected(int positon){ //刷新方法
        if(positon != mSelect){
            mSelect = positon;
            notifyDataSetChanged();
        }
    }

    class ViewHolder{
        TextView tvName;
        TextView tvPhoneNum;
        TextView tvAddress;

    }

    /**发请求后的回调接口*/
    @Override
    public void onHttpRequestFinish(String result) {
        //再次发请求,获取新数据更新listview
        AddressManagerActivity activity=(AddressManagerActivity)context;
        activity.getAddresses();
    }

    @Override
    public void onHttpRequestError(String error) {

    }
}
