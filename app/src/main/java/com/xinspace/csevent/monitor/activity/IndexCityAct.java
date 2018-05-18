package com.xinspace.csevent.monitor.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.xinspace.csevent.monitor.view.QuickIndexView;
import com.xinspace.csevent.R;
import com.xinspace.csevent.data.biz.GetDataBiz;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.ui.activity.BaseActivity;
import com.xinspace.csevent.util.SharedPreferencesUtil1;

import org.json.JSONException;

import static com.xinspace.csevent.app.AppConfig.COMMUNITY_AREA;

/**
 * Created by Android on 2017/7/19.
 */

public class IndexCityAct extends BaseActivity{

    private QuickIndexView quickIndexView;
    private RecyclerView rv_index_city;
    private String key;
    private String area;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_index_city);

        area = SharedPreferencesUtil1.getString(this, COMMUNITY_AREA, "");
        initView();
        initData();
    }


    private void initView() {

        quickIndexView = (QuickIndexView) findViewById(R.id.quickIndexView);
        rv_index_city = (RecyclerView) findViewById(R.id.rv_index_city);

    }

    private void initData() {

        GetDataBiz.GET_CITY_DATA(area, key, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                if (TextUtils.isEmpty(result)){
                    return;
                }



            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });

    }


}
