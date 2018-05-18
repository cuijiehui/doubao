package com.xinspace.csevent.shop.weiget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.app.utils.ScreenUtils;

/**
 *
 * 签到提醒弹窗
 *
 * Created by Android on 2017/6/19.
 */

public class SignInPop extends PopupWindow{

    private Context context;
    private View view;

    private TextView tv_content;
    private TextView tv_close_pop;
    private ReimburseReSult reimburseReSult;
    private String result;

    public SignInPop(Context context) {
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.pop_sign_in, null);
        this.setContentView(view);
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        this.setHeight(ScreenUtils.getScreenHeight(context) - ScreenUtils.getStatusBarHeight(context));
        setBackgroundDrawable(new BitmapDrawable());
        setTouchable(true);
        initView();
    }

    private void initView() {

        tv_content = (TextView) view.findViewById(R.id.tv_content);
        tv_close_pop = (TextView) view.findViewById(R.id.tv_close_pop);
        tv_close_pop.setOnClickListener(onClickListener);

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_close_pop:
                    SignInPop.this.dismiss();
                    break;
            }
        }
    };

    public void setTextContent(String text){
        tv_content.setText(text);
    }

}
