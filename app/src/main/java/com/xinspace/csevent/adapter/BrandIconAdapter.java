package com.xinspace.csevent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.data.entity.BrandEntity;
import com.xinspace.csevent.util.ImagerLoaderUtil;
import com.xinspace.csevent.util.LogUtil;

import java.util.List;

/**
 * 商家品牌adapter
 */
public class BrandIconAdapter extends BaseAdapter{
    private List<Object> list;
    private Context context;

    public BrandIconAdapter(Context context,List<Object> list) {
        this.list = list;
        this.context = context;
    }
    public void updateData(List<Object> list){
        this.list=list;
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_shop_fragment_brand, null);
        ImageView brand_icon = (ImageView) view.findViewById(R.id.iv_shop_brand_icon);
        TextView brand_name= (TextView) view.findViewById(R.id.tv_shop_brand_name);

        //数据
        BrandEntity enty = (BrandEntity) list.get(position);
        //第一个icon设置为拾得活动
        if(position==0){
            brand_icon.setImageResource(R.drawable.shide_logo);
            brand_name.setText("拾得活动");
        }else if(position==1){
            //第二个icon设置为不二印象
            brand_icon.setImageResource(R.drawable.buer_impression);
            brand_name.setText("不二菩提");
        }else{
            LogUtil.i("enty.getname="+enty.getName());
            brand_name.setText(enty.getName());
            ImagerLoaderUtil.displayRoundedImage(enty.getLogo(),brand_icon);
        }

        return view;
    }
}
