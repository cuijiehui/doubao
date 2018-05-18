package com.xinspace.csevent.monitor.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinspace.csevent.monitor.activity.CommunityPayAct;
import com.xinspace.csevent.monitor.bean.PayTypeBean;
import com.xinspace.csevent.R;
import com.xinspace.csevent.util.LogUtil;

import java.util.List;


/**
 * 收费种类 适配器
 * <p>
 * Created by Android on 2017/5/23.
 */

public class PaymentAdapter implements ExpandableListAdapter {

    private List<String> groupList;
    private List<List<PayTypeBean>> itemList;
    private Context context;

    public PaymentAdapter(List<String> groupList, List<List<PayTypeBean>> itemList, Context context) {
        this.groupList = groupList;
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return itemList.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return itemList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHodler groupViewHodler = null;
        if (null == convertView) {
            groupViewHodler = new GroupViewHodler();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_pay_group, null);
            groupViewHodler.groupName = (TextView) convertView.findViewById(R.id.tv_group_name);
            groupViewHodler.groupPic = (ImageView) convertView.findViewById(R.id.iv_group_pic);
            groupViewHodler.iv_indicate = (ImageView) convertView.findViewById(R.id.iv_indicate);
            convertView.setTag(groupViewHodler);
        } else {
            groupViewHodler = (GroupViewHodler) convertView.getTag();
        }

        if (isExpanded){
            groupViewHodler.iv_indicate.setImageResource(R.drawable.icon_group_down);
        }else{
            groupViewHodler.iv_indicate.setImageResource(R.drawable.icon_group_right);
        }

        groupViewHodler.groupName.setText(groupList.get(groupPosition));
        if (groupList.get(groupPosition).equals("水费")) {
            groupViewHodler.groupPic.setImageResource(R.drawable.icon_pay_water);
        }else if (groupList.get(groupPosition).equals("电费")) {
            groupViewHodler.groupPic.setImageResource(R.drawable.icon_energy_charge);
    }else if (groupList.get(groupPosition).equals("煤气费")) {
        groupViewHodler.groupPic.setImageResource(R.drawable.icon_fuel_gas);
    }else if (groupList.get(groupPosition).equals("物业费")) {
        groupViewHodler.groupPic.setImageResource(R.drawable.icon_property_fee);
    }else if (groupList.get(groupPosition).equals("停车费")) {
        groupViewHodler.groupPic.setImageResource(R.drawable.icon_parking_charge);
    }else if (groupList.get(groupPosition).equals("管理费")) {
        groupViewHodler.groupPic.setImageResource(R.drawable.icon_pay_other);
    }else{
        groupViewHodler.groupPic.setImageResource(R.drawable.icon_pay_other);
    }
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHodler childViewHodler = null;
        if (null == convertView) {

            childViewHodler = new ChildViewHodler();

            convertView = LayoutInflater.from(context).inflate(R.layout.item_pay_child, null);

            childViewHodler.tv_instruct = (TextView) convertView.findViewById(R.id.tv_instruct);
            childViewHodler.tv_sum = (TextView) convertView.findViewById(R.id.tv_sum);
            childViewHodler.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
            childViewHodler.tv_go_payment = (TextView) convertView.findViewById(R.id.tv_go_payment);

            convertView.setTag(childViewHodler);
        } else {
            childViewHodler = (ChildViewHodler) convertView.getTag();
        }

        final PayTypeBean bean = itemList.get(groupPosition).get(childPosition);
        childViewHodler.tv_instruct.setText(bean.getInstruct());
        childViewHodler.tv_sum.setText("¥" + bean.getSum());
        childViewHodler.tv_num.setText(bean.getNum() + "");

        childViewHodler.tv_go_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.i("-------------------" + bean.getId());
                Intent intent = new Intent(context , CommunityPayAct.class);
                intent.putExtra("bean" , bean);
                intent.putExtra("typeName" , groupList.get(groupPosition));
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void onGroupExpanded(int groupPosition) {

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }


    static class GroupViewHodler {
        TextView groupName;
        ImageView groupPic;
        ImageView iv_indicate;
    }

    static class ChildViewHodler {
        TextView tv_instruct;
        TextView tv_sum;
        TextView tv_num;
        TextView tv_go_payment;
    }


}
