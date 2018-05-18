package com.xinspace.csevent.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 自定义首页活动列表
 */
public class CustomActivityGridView extends GridView{


    public CustomActivityGridView(Context context) {
        super(context);
    }
    public CustomActivityGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
