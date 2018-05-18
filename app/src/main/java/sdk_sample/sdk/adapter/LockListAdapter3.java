package sdk_sample.sdk.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xinspace.csevent.R;

import java.util.List;

import sdk_sample.sdk.bean.LockBean;

/**
 * Created by Android on 2017/7/18.
 */

public class LockListAdapter3 extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<LockBean> list;
    private LayoutInflater inflater;
    private Context context;

    public LockListAdapter3(List<LockBean> list, Context context) {
        this.list = list;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    /**
     * 定义点击每项的接口,此处只实现了点击，没有实现长按
     */
    public interface OnItemClickLitener
    {
        void OnItemClick(View view, int positon, int type);
        void OnItemLongClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder mViewHolder = null;

        if(viewType==1) {
            view = inflater.inflate(R.layout.item_com_group2, parent, false);
            mViewHolder = new tViewHolder(view);
        } else if(viewType==2) {
            view = inflater.inflate(R.layout.item_com_child2, parent, false);
            mViewHolder = new gViewHolder(view);
        }
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        switch (getItemViewType(position)) {
            case 1:
                final tViewHolder tHolder = (tViewHolder) holder;
                tHolder.tv_group_name.setText(list.get(position).getLock_parent_name());

//                gHolder.txt_pGValue1.setText(list_data.get(position).getValue1());
//                gHolder.txt_pGValue2.setText(list_data.get(position).getValue2());
//
//                if(mOnItemClickLitener!=null)
//                {
//                    gHolder.linear_g.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            //Toast.makeText(context,list_data.get(position).getName(),Toast.LENGTH_SHORT).show();
//                            mOnItemClickLitener.OnItemClick(gHolder.itemView, position,1);
//                        }
//                    });
//                }
                break;
            case 2:
                final gViewHolder gHolder = (gViewHolder)holder;

                gHolder.tv_child_name.setText(list.get(position).getLock_name());

                if(mOnItemClickLitener!=null) {
                    gHolder.rel_child.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //Toast.makeText(context,list_data.get(position).getName(),Toast.LENGTH_SHORT).show();
                            mOnItemClickLitener.OnItemClick(gHolder.itemView, position , 2);
                        }
                    });
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    @Override
    public int getItemViewType(int position) {
        return list.get(position).getType();
    }


    /**
     * 类别形式的布局数据
     */
    class tViewHolder extends RecyclerView.ViewHolder {

        TextView tv_group_name;


        public tViewHolder(View itemView) {
            super(itemView);
            tv_group_name = (TextView) itemView.findViewById(R.id.tv_group_name);
        }
    }

    /**
     * Grid形式的布局数据
     */
    class gViewHolder extends RecyclerView.ViewHolder {


        TextView tv_child_name;
        RelativeLayout rel_child;

        public gViewHolder(View itemView) {
            super(itemView);
            tv_child_name = (TextView) itemView.findViewById(R.id.tv_child_name);
            rel_child = (RelativeLayout) itemView.findViewById(R.id.rel_child);
        }
    }


}
