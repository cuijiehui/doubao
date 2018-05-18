package com.xinspace.csevent.baskorder.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xinspace.csevent.R;
import com.xinspace.csevent.baskorder.view.ScaleTopHeaderView;
import com.xinspace.csevent.util.ImagerLoaderUtil;
import com.xinspace.csevent.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android on 2016/11/1.
 */
public class AddPicAdapter2 extends RecyclerView.Adapter<ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<String> imgs = new ArrayList<String>();


    public AddPicAdapter2(LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
    }


    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.item_add_pic, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        LogUtil.i("www" + "-----------------------" + imgs.get(position));

        ImagerLoaderUtil.displayImage("file://" + imgs.get(position) , holder.image);

       // ImagerLoaderUtil.displayImage("http://pictest.coresun.net/user_avatar/270/270.png" , holder.image);

    }

    @Override
    public int getItemCount() {
        return imgs.size() ;
    }
}


    class ViewHolder extends RecyclerView.ViewHolder {
    ScaleTopHeaderView image;
    public ViewHolder(View view) {
        super(view);
        image = (ScaleTopHeaderView) view.findViewById(R.id.iv_add_pic);
    }
}