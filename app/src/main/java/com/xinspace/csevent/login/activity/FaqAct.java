package com.xinspace.csevent.login.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.app.utils.DataUtils;
import com.xinspace.csevent.app.utils.ScreenUtils;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.ImagerLoaderUtil;
import com.xinspace.csevent.util.StatusBarUtils;
import com.xinspace.csevent.ui.activity.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android on 2016/9/21.
 *
 * 常见问题
 */
public class FaqAct extends BaseActivity{

    private List<String> urlList;
    private int screenWidth;

    private LinearLayout lin_img_content , lin_faq_back;

    private TextView tv_title;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    loadData((List<String>)msg.obj);
                    break;
            }
        }
    };


    private void loadData(List<String> urlList) {

        for (int i = 0 ; i < urlList.size() ; i++){
            ImageView imageView = new ImageView(this);
            lin_img_content.addView(imageView);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
            params.height = (int) (screenWidth * 1.0f);
            params.width = (int) (screenWidth * 1.0f);
            imageView.setLayoutParams(params);
            imageView.setScaleType(ImageView.ScaleType.FIT_START);
            ImagerLoaderUtil.displayImage(urlList.get(i), imageView);
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(this , R.color.app_bottom_color);
        setContentView(R.layout.act_faq);
        screenWidth = ScreenUtils.getScreenWidth(this);
        urlList = new ArrayList<String>();
        initData();
        initView();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        clickListener = null;
    }


    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_faq_back:
                    finish();
                    break;
            }
        }
    };

    private void initData() {
        DataUtils.getFaqList(this, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("url");
                for (int i = 0 ; i < jsonArray.length() ; i++ ){
                    urlList.add(jsonArray.getString(i));
                }
                handler.obtainMessage(1 , urlList).sendToTarget();
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }

    private void initView() {

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("常见问题");

        lin_img_content = (LinearLayout) findViewById(R.id.lin_img_content);
        lin_faq_back = (LinearLayout) findViewById(R.id.ll_faq_back);
        lin_faq_back.setOnClickListener(clickListener);
    }
}
