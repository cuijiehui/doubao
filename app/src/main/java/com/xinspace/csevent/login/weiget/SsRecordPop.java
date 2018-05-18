package com.xinspace.csevent.login.weiget;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.app.utils.ScreenUtils;
import com.xinspace.csevent.login.activity.CrowdRecordAct;
import com.xinspace.csevent.ui.activity.DuobaoRecordActivity;


/**
 * com.cjdd.showo.social.widget
 * Created by Administrator
 * on 2016/8/30.
 *
 * 夺宝记录弹窗
 */
public class SsRecordPop extends PopupWindow{

    private Context context;
    private View view;
    private ImageView img_close_pop;
    private TextView tv_onnow , tv_raise_draw;

    public SsRecordPop(Context context) {
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.pop_ssrecord, null);
        this.setContentView(view);
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        this.setHeight(ScreenUtils.getScreenHeight(context) - ScreenUtils.getStatusBarHeight(context));
        setBackgroundDrawable(new BitmapDrawable());
        setTouchable(true);
        initView();
    }


    private void initView() {
        tv_onnow = (TextView) view.findViewById(R.id.tv_onnow);
        tv_raise_draw = (TextView) view.findViewById(R.id.tv_raise_draw);
        tv_onnow.setOnClickListener(onClickListener);
        tv_raise_draw.setOnClickListener(onClickListener);

        img_close_pop = (ImageView) view.findViewById(R.id.img_close_pop);
        img_close_pop.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_onnow:

                    Intent intent = new Intent(context, DuobaoRecordActivity.class);
                    context.startActivity(intent);

                    break;
                case R.id.tv_raise_draw:
                    Intent intent2 = new Intent(context, CrowdRecordAct.class);
                    context.startActivity(intent2);
                    break;
                case R.id.img_close_pop:

                    break;
            }
            SsRecordPop.this.dismiss();
        }
    };

}
