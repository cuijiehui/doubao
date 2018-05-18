package com.xinspace.csevent.monitor.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.google.gson.Gson;
import com.xinspace.csevent.monitor.adapter.ComChildAdapter;
import com.xinspace.csevent.monitor.bean.ChildCommunityBean;
import com.xinspace.csevent.monitor.weiget.IdentityResultListener;
import com.xinspace.csevent.R;
import com.xinspace.csevent.app.utils.ScreenUtils;
import com.xinspace.csevent.data.biz.GetDataBiz;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.SharedPreferencesUtil1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.xinspace.csevent.app.AppConfig.COMMUNITY_AREA;

/**
 * Created by Android on 2017/7/19.
 */

public class ChooseAreaPop extends PopupWindow{

    private Context context;
    private View view;
    private String cityName;
    private ListView lv_choose_area;
    private ComChildAdapter adapter;
    private List<ChildCommunityBean> allList = new ArrayList<>();
    private ChildCommunityBean childBean;
    private IdentityResultListener listener;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 200:
                    if (msg.obj != null){
                        allList.addAll((Collection<? extends ChildCommunityBean>) msg.obj);
                        adapter.setList(allList);
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case 400:

                    break;
            }
        }
    };
    private String area;

    public ChooseAreaPop(Context context , String cityS , IdentityResultListener listener) {
        this.context = context;
        this.cityName = cityS;
        this.listener = listener;
        view = LayoutInflater.from(context).inflate(R.layout.pop_choose_area, null);
        this.setContentView(view);
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        this.setHeight(ScreenUtils.getScreenHeight(context) - ScreenUtils.getStatusBarHeight(context) - ScreenUtils.dpToPx(context , 146));
        setBackgroundDrawable(new BitmapDrawable());
        setTouchable(true);

        area = SharedPreferencesUtil1.getString(context, COMMUNITY_AREA, "");

        initView();
        initData();

    }

    private void initData() {

        GetDataBiz.GET_CITY_AREA_DATA(area, cityName, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                if (TextUtils.isEmpty(result)){
                    return;
                }
                List<ChildCommunityBean> list = new ArrayList<>();
                Gson gson = new Gson();
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getInt("code") == 200){
                    JSONArray dataJsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0 ; i < dataJsonArray.length() ; i++){
                        JSONObject beanJsonObject = dataJsonArray.getJSONObject(i);
                        ChildCommunityBean bean = new ChildCommunityBean();
                        bean.setName(beanJsonObject.getString("area"));
                        bean.setId(String.valueOf(beanJsonObject.getInt("id")));
                        list.add(bean);
                    }
                    handler.obtainMessage(200 , list).sendToTarget();
                }else{

                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });

    }

    private void initView() {

        lv_choose_area = (ListView) view.findViewById(R.id.lv_choose_area);
        adapter = new ComChildAdapter(context);
        adapter.setList(allList);
        lv_choose_area.setAdapter(adapter);

        lv_choose_area.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                adapter.setSelectItem(position);
                childBean = allList.get(position);
                adapter.notifyDataSetChanged();
                listener.identityResult(allList.get(position).getId());
                ChooseAreaPop.this.dismiss();

            }
        });
    }


}
