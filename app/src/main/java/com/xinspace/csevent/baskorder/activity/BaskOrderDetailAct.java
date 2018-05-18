package com.xinspace.csevent.baskorder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.app.utils.ScreenUtils;
import com.xinspace.csevent.baskorder.model.BaskOrder;
import com.xinspace.csevent.util.ImagerLoaderUtil;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.ui.activity.BaseActivity;

import java.util.List;

/**
 * Created by Android on 2016/10/27.
 *
 * 晒单单条记录详情
 */
public class BaskOrderDetailAct extends BaseActivity{

    private Intent intent;
    private BaskOrder baskOrder;

    private TextView tv_title;
    private TextView tv_user_name;
    private TextView tv_bask_time;
    private TextView tv_goods_name;

    private TextView tv_noactivity;
    private TextView tv_match;
    private TextView tv_win_num;
    private TextView tv_publish_time;

    private TextView tv_content;

    private LinearLayout lin_content;

    private List<String> imgUrlList;

    private int screenWidth;

    private LinearLayout ll_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_bask_order_detail);
        intent = getIntent();
        if (intent != null){
            baskOrder = (BaskOrder) intent.getSerializableExtra("data");
        }
        initView();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        clickListener = null;
        baskOrder = null;
        intent = null;
    }

    private void initView() {

        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(clickListener);

        screenWidth = ScreenUtils.getScreenWidth(this);

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_goods_name = (TextView) findViewById(R.id.tv_goods_name);
        tv_user_name = (TextView) findViewById(R.id.tv_user_name);
        tv_bask_time = (TextView) findViewById(R.id.tv_bask_time);
        tv_noactivity = (TextView) findViewById(R.id.tv_noactivity);
        tv_match = (TextView) findViewById(R.id.tv_match);
        tv_win_num = (TextView) findViewById(R.id.tv_win_num);
        tv_publish_time = (TextView) findViewById(R.id.tv_publish_time);
        tv_content = (TextView) findViewById(R.id.tv_content);
        lin_content = (LinearLayout) findViewById(R.id.lin_img_content);

        LogUtil.i("-------" + baskOrder.getTitle());

        tv_title.setText(baskOrder.getTitle());
        tv_user_name.setText(baskOrder.getUsername());
        tv_bask_time.setText(baskOrder.getDatetime());
        tv_goods_name.setText("");
        tv_noactivity.setText(baskOrder.getNoactivity());
        tv_match.setText(baskOrder.getMatch());
        tv_win_num.setText("");
        tv_publish_time.setText(baskOrder.getStartdate());

        tv_content.setText(baskOrder.getComment());

        imgUrlList = baskOrder.getBigList();


        for (int i = 0; i < imgUrlList.size(); i++) {
            ImageView imageView = new ImageView(this);
            lin_content.addView(imageView);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
            params.setMargins(0 , 0 , 0 ,10);
            params.height = (int) (screenWidth * 1.0f);
            params.width = (int) (screenWidth * 1.0f);
            imageView.setLayoutParams(params);
            imageView.setScaleType(ImageView.ScaleType.FIT_START);
            ImagerLoaderUtil.displayImage(imgUrlList.get(i), imageView);
        }

    }


    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_back:
                    BaskOrderDetailAct.this.finish();
                    break;
            }
        }
    };


}
