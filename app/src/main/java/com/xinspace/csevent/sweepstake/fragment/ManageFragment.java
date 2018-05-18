package com.xinspace.csevent.sweepstake.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xinspace.csevent.R;
import com.xinspace.csevent.app.utils.ScreenUtils;
import com.xinspace.csevent.sweepstake.activity.WebViewActivity2;

/**
 * Created by Android on 2016/12/6.
 *
 * 智慧社区
 *
 */
public class ManageFragment extends Fragment{

    private View view;

    private int screenWidth;
    private int imgWidth,imgHeight;


    private ImageView iv_11 , iv_12 , iv_13 , iv_14 , iv_15 , iv_16;
    private ImageView iv_21 , iv_22 , iv_23 , iv_24 , iv_25 , iv_26;
    private ImageView iv_31 ,iv_32 , iv_33;

//    private String[] url = new String[]{
//            "http://grandway020.com/bc/app/index.php?i=8&c=entry&eid=896",
//            "http://grandway020.com/bc/app/index.php?i=8&c=entry&eid=897",
//            "http://grandway020.com/bc/app/index.php?i=8&c=entry&eid=898",
//            "http://grandway020.com/bc/app/index.php?i=8&c=entry&eid=902",
//            "http://grandway020.com/bc/app/index.php?i=8&c=entry&eid=903",
//    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_manage , null);
        initView();

        return view;
    }

    private void initView() {

        screenWidth = ScreenUtils.getScreenWidth(getActivity());
        imgWidth = (screenWidth * 3) / 10;
        imgHeight = (imgWidth * 2) / 3;

        iv_11 = (ImageView) view.findViewById(R.id.iv_11);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) iv_11.getLayoutParams();
        params.width = imgWidth;
        params.height = imgHeight;
        iv_11.setLayoutParams(params);
        iv_11.setImageResource(R.drawable.icon_notice);

        iv_12 = (ImageView) view.findViewById(R.id.iv_12);
        LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) iv_12.getLayoutParams();
        params2.width = imgWidth;
        params2.height = imgHeight;
        iv_12.setLayoutParams(params2);
        iv_12.setImageResource(R.drawable.icon_warranty);

        iv_13 = (ImageView) view.findViewById(R.id.iv_13);
        LinearLayout.LayoutParams params3 = (LinearLayout.LayoutParams) iv_13.getLayoutParams();
        params3.width = imgWidth;
        params3.height = imgHeight;
        iv_13.setLayoutParams(params3);
        iv_13.setImageResource(R.drawable.icon_opinion);

        iv_14 = (ImageView) view.findViewById(R.id.iv_14);
        LinearLayout.LayoutParams params4 = (LinearLayout.LayoutParams) iv_14.getLayoutParams();
        params4.width = imgWidth;
        params4.height = imgHeight;
        iv_14.setLayoutParams(params4);
        iv_14.setImageResource(R.drawable.icon_tenement);

        iv_15 = (ImageView) view.findViewById(R.id.iv_15);
        LinearLayout.LayoutParams params5 = (LinearLayout.LayoutParams) iv_15.getLayoutParams();
        params5.width = imgWidth;
        params5.height = imgHeight;
        iv_15.setLayoutParams(params5);
        iv_15.setImageResource(R.drawable.icon_phone);

        iv_16 = (ImageView) view.findViewById(R.id.iv_16);
        LinearLayout.LayoutParams params6 = (LinearLayout.LayoutParams) iv_16.getLayoutParams();
        params6.width = imgWidth;
        params6.height = imgHeight;
        iv_16.setLayoutParams(params6);
        iv_16.setImageResource(R.drawable.icon_query);

        iv_11.setOnClickListener(clickListener);
        iv_12.setOnClickListener(clickListener);
        iv_13.setOnClickListener(clickListener);
        iv_14.setOnClickListener(clickListener);
        iv_15.setOnClickListener(clickListener);
        iv_16.setOnClickListener(clickListener);


        iv_21 = (ImageView) view.findViewById(R.id.iv_21);
        LinearLayout.LayoutParams params21 = (LinearLayout.LayoutParams) iv_21.getLayoutParams();
        params21.width = imgWidth;
        params21.height = imgHeight;
        iv_21.setLayoutParams(params21);
        iv_21.setImageResource(R.drawable.icon_activity);

        iv_22 = (ImageView) view.findViewById(R.id.iv_22);
        LinearLayout.LayoutParams params22 = (LinearLayout.LayoutParams) iv_22.getLayoutParams();
        params22.width = imgWidth;
        params22.height = imgHeight;
        iv_22.setLayoutParams(params21);
        iv_22.setImageResource(R.drawable.icon_used);

        iv_23 = (ImageView) view.findViewById(R.id.iv_23);
        LinearLayout.LayoutParams params23 = (LinearLayout.LayoutParams) iv_23.getLayoutParams();
        params23.width = imgWidth;
        params23.height = imgHeight;
        iv_23.setLayoutParams(params21);
        iv_23.setImageResource(R.drawable.icon_housekeeping);

        iv_24 = (ImageView) view.findViewById(R.id.iv_24);
        LinearLayout.LayoutParams params24 = (LinearLayout.LayoutParams) iv_24.getLayoutParams();
        params24.width = imgWidth;
        params24.height = imgHeight;
        iv_24.setLayoutParams(params24);
        iv_24.setImageResource(R.drawable.icon_lease);

        iv_25 = (ImageView) view.findViewById(R.id.iv_25);
        LinearLayout.LayoutParams params25 = (LinearLayout.LayoutParams) iv_25.getLayoutParams();
        params25.width = imgWidth;
        params25.height = imgHeight;
        iv_25.setLayoutParams(params21);
        iv_25.setImageResource(R.drawable.icon_carpool);

        iv_26 = (ImageView) view.findViewById(R.id.iv_26);
        LinearLayout.LayoutParams params26 = (LinearLayout.LayoutParams) iv_26.getLayoutParams();
        params26.width = imgWidth;
        params26.height = imgHeight;
        iv_26.setLayoutParams(params26);

        iv_21.setOnClickListener(clickListener);
        iv_22.setOnClickListener(clickListener);
        iv_23.setOnClickListener(clickListener);
        iv_24.setOnClickListener(clickListener);
        iv_25.setOnClickListener(clickListener);

        iv_31 = (ImageView) view.findViewById(R.id.iv_31);
        LinearLayout.LayoutParams params31 = (LinearLayout.LayoutParams) iv_25.getLayoutParams();
        params31.width = imgWidth;
        params31.height = imgHeight;
        iv_31.setLayoutParams(params31);
        iv_31.setImageResource(R.drawable.icon_shops);

        iv_32 =(ImageView) view.findViewById(R.id.iv_32);
        LinearLayout.LayoutParams params32 = (LinearLayout.LayoutParams) iv_25.getLayoutParams();
        params32.width = imgWidth;
        params32.height = imgHeight;
        iv_32.setLayoutParams(params32);
        iv_32.setImageResource(R.drawable.icon_supermarket);

        iv_33 =(ImageView) view.findViewById(R.id.iv_33);
        LinearLayout.LayoutParams params33 = (LinearLayout.LayoutParams) iv_33.getLayoutParams();
        params33.width = imgWidth;
        params33.height = imgHeight;
        iv_33.setLayoutParams(params33);

        iv_31.setOnClickListener(clickListener);
        iv_32.setOnClickListener(clickListener);
    }


    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity() , WebViewActivity2.class);
            switch (v.getId()){
                case R.id.iv_11:  //社区公告
                    intent.putExtra("data", "http://grandway020.com/bc/app/index.php?i=8&c=entry&eid=896");
                    break;
                case R.id.iv_12://小区保修
                    intent.putExtra("data", "http://grandway020.com/bc/app/index.php?i=8&c=entry&eid=897");
                    break;
                case R.id.iv_13://意见建议
                    intent.putExtra("data", "http://grandway020.com/bc/app/index.php?i=8&c=entry&eid=898");
                    break;
                case R.id.iv_14://缴物业费
                   // intent.putExtra("data", "http://grandway020.com/bc/app/index.php?i=8&c=entry&eid=898");
                    break;
                case R.id.iv_15://便民号码
                    //intent.putExtra("data", "http://grandway020.com/bc/app/index.php?i=8&c=entry&eid=898");
                    break;
                case R.id.iv_16://常用查询
                    //intent.putExtra("data", "http://grandway020.com/bc/app/index.php?i=8&c=entry&eid=898");
                    break;
                case R.id.iv_21: // 活动
                    intent.putExtra("data", "http://grandway020.com/bc/app/index.php?i=8&c=entry&eid=902");
                    break;
                case R.id.iv_22: // 二手
                    intent.putExtra("data", "http://grandway020.com/bc/app/index.php?i=8&c=entry&eid=903");
                    break;
                case R.id.iv_23: //家政
                    //intent.putExtra("data", "http://grandway020.com/bc/app/index.php?i=8&c=entry&eid=898");
                    break;
                case R.id.iv_24: // 租赁
                    //intent.putExtra("data", "http://grandway020.com/bc/app/index.php?i=8&c=entry&eid=898");
                    break;
                case R.id.iv_25: //拼车
                    //intent.putExtra("data", "http://grandway020.com/bc/app/index.php?i=8&c=entry&eid=898");
                    break;
                case R.id.iv_31: //周边商家
                    intent.putExtra("data", "http://grandway020.com/bc/app/index.php?i=8&c=entry&eid=904");
                    break;
                case R.id.iv_33: //生活超市
                    intent.putExtra("data", "http://grandway020.com/bc/app/index.php?i=8&c=entry&eid=905");
                    break;
            }
            startActivity(intent);
        }
    };



}
