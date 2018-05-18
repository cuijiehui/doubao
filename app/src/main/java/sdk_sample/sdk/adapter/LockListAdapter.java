package sdk_sample.sdk.adapter;

/**
 * Created by Android on 2017/3/23.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import sdk_sample.sdk.bean.LockBean;
import sdk_sample.sdk.utils.ToolsUtil;


public class LockListAdapter extends BaseAdapter {
    private List<LockBean> list = null;
    private Context mContext;

    public LockListAdapter(Context mContext, List<LockBean> list) {
        this.mContext = mContext;
        this.list = list;
    }

    public void updateListView(List<LockBean> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }

    public int getCount() {
        return this.list == null?0:this.list.size();
    }

    public Object getItem(int position) {
        return this.list.get(position);
    }

    public long getItemId(int position) {
        return (long)position;
    }

    public View getView(int position, View view, ViewGroup arg2) {
        LockListAdapter.ViewHolder viewHolder = null;
        LockBean mContent = (LockBean)this.list.get(position);
        if(view == null) {
            viewHolder = new LockListAdapter.ViewHolder();
            view = LayoutInflater.from(this.mContext).inflate(ToolsUtil.getIdByName(this.mContext, "layout", "sayee_item_lock_list"), (ViewGroup)null);
            viewHolder.tvTitle = (TextView)view.findViewById(ToolsUtil.getIdByName(this.mContext, "id", "tv_title"));
            view.setTag(viewHolder);
        } else {
            viewHolder = (LockListAdapter.ViewHolder)view.getTag();
        }

        viewHolder.tvTitle.setText(mContent.getLock_parent_name());
        return view;
    }

    static final class ViewHolder {
        TextView tvTitle;

        ViewHolder() {
        }
    }
}

