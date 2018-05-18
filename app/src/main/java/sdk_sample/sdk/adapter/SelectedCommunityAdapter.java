package sdk_sample.sdk.adapter;

/**
 * Created by Android on 2017/3/23.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;


import java.util.List;

import sdk_sample.sdk.bean.NeiborIdBean;
import sdk_sample.sdk.utils.ToolsUtil;

public class SelectedCommunityAdapter extends BaseAdapter implements SectionIndexer {
    private List<NeiborIdBean> list = null;
    private Context mContext;

    public SelectedCommunityAdapter(Context mContext, List<NeiborIdBean> list) {
        this.mContext = mContext;
        this.list = list;
    }

    public void updateListView(List<NeiborIdBean> list) {
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
        SelectedCommunityAdapter.ViewHolder viewHolder = null;
        NeiborIdBean mContent = (NeiborIdBean)this.list.get(position);
        if(view == null) {
            viewHolder = new SelectedCommunityAdapter.ViewHolder();
            view = LayoutInflater.from(this.mContext).inflate(ToolsUtil.getIdByName(this.mContext, "layout", "sayee_item_community"), (ViewGroup)null);
            viewHolder.tvTitle = (TextView)view.findViewById(ToolsUtil.getIdByName(this.mContext, "id", "title"));
            viewHolder.tvLetter = (TextView)view.findViewById(ToolsUtil.getIdByName(this.mContext, "id", "catalog"));
            view.setTag(viewHolder);
        } else {
            viewHolder = (SelectedCommunityAdapter.ViewHolder)view.getTag();
        }

        if(mContent != null) {
            viewHolder.tvLetter.setText(mContent.getFneibname());
            viewHolder.tvTitle.setText(mContent.getFaddress());
        }

        return view;
    }

    public int getSectionForPosition(int position) {
        return ((NeiborIdBean)this.list.get(position)).getFip().charAt(0);
    }

    public int getPositionForSection(int section) {
        for(int i = 0; i < this.getCount(); ++i) {
            String sortStr = ((NeiborIdBean)this.list.get(i)).getFip();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if(firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    public Object[] getSections() {
        return null;
    }

    static final class ViewHolder {
        TextView tvLetter;
        TextView tvTitle;

        ViewHolder() {
        }
    }
}

