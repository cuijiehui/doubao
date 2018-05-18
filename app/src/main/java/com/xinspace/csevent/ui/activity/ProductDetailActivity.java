package com.xinspace.csevent.ui.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xinspace.csevent.R;

/**
 * 商品详细信息页面
 */
public class ProductDetailActivity extends BaseActivity{
    private PullToRefreshScrollView scrollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        setView();
    }
    //初始化
    private void setView() {
        View scroll_view = LayoutInflater.from(this).inflate(R.layout.item_product_detail_child_view, null);
    }
}
