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

import sdk_sample.sdk.bean.CommunityBean;
import sdk_sample.sdk.utils.ToolsUtil;


public class CommunityAdapter extends BaseAdapter implements SectionIndexer {
    private List<CommunityBean> list = null;
    private Context mContext;

    public CommunityAdapter(Context mContext, List<CommunityBean> list) {
        this.mContext = mContext;
        this.list = list;
    }

    public void updateListView(List<CommunityBean> list) {
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
        CommunityAdapter.ViewHolder viewHolder = null;
        CommunityBean mContent = (CommunityBean)this.list.get(position);
        if(view == null) {
            viewHolder = new CommunityAdapter.ViewHolder();
            view = LayoutInflater.from(this.mContext).inflate(ToolsUtil.getIdByName(this.mContext, "layout", "sayee_item_community"), (ViewGroup)null);
            viewHolder.tvTitle = (TextView)view.findViewById(ToolsUtil.getIdByName(this.mContext, "id", "title"));
            viewHolder.tvLetter = (TextView)view.findViewById(ToolsUtil.getIdByName(this.mContext, "id", "catalog"));
            view.setTag(viewHolder);
        } else {
            viewHolder = (CommunityAdapter.ViewHolder)view.getTag();
        }

        if(mContent != null) {
            if(mContent.getFuser_id() != 0) {
                viewHolder.tvTitle.setText("已进入过该社区");
            } else {
                viewHolder.tvTitle.setText("未进入过该社区");
            }

            viewHolder.tvLetter.setText(mContent.getFneib_name());
        }

        return view;
    }

    public int getSectionForPosition(int position) {
        return ((CommunityBean)this.list.get(position)).getFip().charAt(0);
    }

    public int getPositionForSection(int section) {
        for(int i = 0; i < this.getCount(); ++i) {
            String sortStr = ((CommunityBean)this.list.get(i)).getFip();
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

