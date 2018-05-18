package sdk_sample.sdk.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.util.LogUtil;
import java.util.List;
import sdk_sample.sdk.bean.ComBean;


/**
 * 收费种类 适配器
 * <p>
 * Created by Android on 2017/5/23.
 */

public class LockListAdapter2 implements ExpandableListAdapter {

    private Context context;
    private List<ComBean> list;

    public void setList(List<ComBean> list) {
        this.list = list;

    }

    public LockListAdapter2(Context context ) {
        this.context = context;
    }

    @Override
    public int getGroupCount() {

        //LogUtil.i("--------list.size()-----" + list.size());

        return list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return list.get(groupPosition).getList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return list.get(groupPosition).getList().get(childPosition);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_com_group, null);
            groupViewHodler.groupName = (TextView) convertView.findViewById(R.id.tv_group_name);
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

        groupViewHodler.groupName.setText(list.get(groupPosition).getComTitle());
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHodler childViewHodler = null;
        if (null == convertView) {
            childViewHodler = new ChildViewHodler();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_com_child, null);
            childViewHodler.tv_child_name = (TextView) convertView.findViewById(R.id.tv_child_name);
            convertView.setTag(childViewHodler);
        } else {
            childViewHodler = (ChildViewHodler) convertView.getTag();
        }

        childViewHodler.tv_child_name.setText(list.get(groupPosition).getList().get(childPosition).getLock_name());

//        childViewHodler.tv_go_payment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LogUtil.i("-------------------" + bean.getId());
//                Intent intent = new Intent(context , CommunityPayAct.class);
//                intent.putExtra("bean" , bean);
//                intent.putExtra("typeName" , groupList.get(groupPosition));
//                context.startActivity(intent);
//            }
//        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
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
        ImageView iv_indicate;
    }

    static class ChildViewHodler {
        TextView tv_child_name;
    }

}
