package com.xinspace.csevent.ui.activity;

import android.os.Bundle;

import com.xinspace.csevent.util.ImagerLoaderUtil;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * 所有Activity都继承这个Activity
 */
public class BaseActivity extends SwipeBackActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置侧滑回到上级页面
        getSwipeBackLayout().setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);

    }

    @Override
    public void onLowMemory() {
        ImagerLoaderUtil.clearImageMemory();
        super.onLowMemory();
    }
}
