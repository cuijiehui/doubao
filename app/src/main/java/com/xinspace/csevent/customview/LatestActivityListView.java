package com.xinspace.csevent.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/***
 * 首页最新活动的自定义listview
 */
public class LatestActivityListView extends ListView{

    public LatestActivityListView(Context context) {
        super(context);
    }

    public LatestActivityListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
