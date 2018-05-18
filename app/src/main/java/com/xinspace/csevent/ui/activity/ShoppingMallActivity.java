package com.xinspace.csevent.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.xinspace.csevent.R;

/**
 * 拾得商城页面
 */
public class ShoppingMallActivity extends Activity {
    private LinearLayout back;
    private EditText etKey;
    private ListView menuList;//菜单
    private ListView productList;//产品

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_mall);
        setView();
        setListener();
    }
    //设置监听器
    private void setListener() {
        //退出
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(ShoppingMallActivity.this,ProductDetailActivity.class));
            }
        });

        //搜索框
        etKey.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String keyname=etKey.getText().toString();
                if(TextUtils.isEmpty(keyname)){
                    //获取分类

                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }
    //初始化组件
    private void setView() {
        back= (LinearLayout) findViewById(R.id.ll_shopping_back);
        etKey= (EditText) findViewById(R.id.et_shopping_search);

    }
}
