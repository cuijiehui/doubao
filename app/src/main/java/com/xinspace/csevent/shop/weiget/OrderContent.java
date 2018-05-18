package com.xinspace.csevent.shop.weiget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by Android on 2017/5/17.
 */

public interface OrderContent {

    public int getLayout();

    public boolean isClickAble();

    public View getView(Context mContext, View convertView, LayoutInflater inflater);
}
