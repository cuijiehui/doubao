package com.xinspace.csevent.baskorder.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.util.ImagerLoaderUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * com.cjdd.showo.social.adapter
 * Created by Administrator
 * on 2016/8/9.
 */
public class AddPicAdapter extends BaseAdapter {

    private Context context;
    private List<String> imgs = new ArrayList<String>();
    private int width;
    private int imgsCount;

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
        notifyDataSetChanged();
    }

    public AddPicAdapter(Context context,  int imgsCount) {
        super();
        this.context = context;
        width = new DisplayMetrics().widthPixels / 4;
        this.imgsCount = imgsCount;
    }

    @Override
    public int getCount() {
        return imgs.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return imgs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_add_pic, parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.iv_add_pic);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position != imgs.size()) {
            ImagerLoaderUtil.displayImage("file://" +imgs.get(position) , holder.imageView);
        } else {
            holder.imageView.setBackgroundResource(R.drawable.icon_addpic_unfocused);
            if (position == imgsCount) {
                holder.imageView.setVisibility(View.GONE);
            }
        }

        return convertView;
    }

    static class ViewHolder {
        ImageView imageView;
    }

    public void destory() {
        System.gc();
    }
}
