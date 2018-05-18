package com.xinspace.csevent.monitor.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.xinspace.csevent.monitor.bean.LeaseRoomBean;
import com.xinspace.csevent.R;
import com.xinspace.csevent.ui.activity.BaseActivity;

/**
 * Created by Android on 2017/7/20.
 */

public class LeaseDetailAct extends BaseActivity{

    private Intent intent;
    private LinearLayout lin_book_tel;
    private LinearLayout lin_lease_book;
    private LinearLayout ll_lease_back;
    private LeaseRoomBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_lease_detail);
        intent = getIntent();
        if (intent != null){
            bean = (LeaseRoomBean) intent.getSerializableExtra("bean");
        }

        initView();
        initData();
    }

    private void initView() {

        ll_lease_back = (LinearLayout) findViewById(R.id.ll_lease_back);
        lin_book_tel = (LinearLayout) findViewById(R.id.lin_book_tel);
        lin_lease_book = (LinearLayout) findViewById(R.id.lin_lease_book);

        ll_lease_back.setOnClickListener(onClickListener);
        lin_book_tel.setOnClickListener(onClickListener);
        lin_lease_book.setOnClickListener(onClickListener);

    }

    private void initData() {

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_lease_back:

                    finish();

                    break;
                case R.id.lin_lease_book:

                    Intent intent1 = new Intent(LeaseDetailAct.this , OrderRoomAct.class);
                    intent1.putExtra("bean" , bean);
                    startActivity(intent1);

                    break;
                case R.id.lin_book_tel:
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    Uri data = Uri.parse("tel:" + bean.getMobile());
                    intent.setData(data);
                    startActivity(intent);
                    break;
            }
        }
    };



}
