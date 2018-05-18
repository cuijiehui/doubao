package com.xinspace.csevent.monitor.weiget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.google.gson.Gson;
import com.xinspace.csevent.monitor.adapter.PayTypeAdapter;
import com.xinspace.csevent.monitor.bean.TypeBean;
import com.xinspace.csevent.R;
import com.xinspace.csevent.app.utils.ScreenUtils;
import com.xinspace.csevent.app.weiget.SDPreference;
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
 * Created by Android on 2017/5/26.
 */

public class PayTypePop extends PopupWindow{

    private Context context;
    private View view;
    private ListView lv_pay_type;
    private PayTypeAdapter adapter;
    private List<TypeBean> allList = new ArrayList<>();
    private SDPreference preference;
    String uid;
    String token;
    String com_id;

    private ReturnTypeListener listener;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 200:
                    if (msg.obj != null){
                        allList.addAll((Collection<? extends TypeBean>) msg.obj);
                        adapter.setList(allList);
                        adapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };
    private String area;


    public PayTypePop(Context context , ReturnTypeListener listener) {
        this.context = context;
        this.listener = listener;
        view = LayoutInflater.from(context).inflate(R.layout.pop_pay_type, null);
        this.setContentView(view);
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        //this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setHeight(ScreenUtils.getScreenHeight(context) - ScreenUtils.getStatusBarHeight(context) - ScreenUtils.dpToPx(context , Float.valueOf(45)));
        setBackgroundDrawable(new BitmapDrawable());
        setTouchable(true);

        preference = SDPreference.getInstance();
        uid = preference.getContent("cUid");
        token = preference.getContent("cToken");
        com_id = preference.getContent("com_id");
        area = SharedPreferencesUtil1.getString(context, COMMUNITY_AREA, "");

        initView();
        initData();
    }


    private void initView() {

        lv_pay_type = (ListView) view.findViewById(R.id.lv_pay_type);
        adapter = new PayTypeAdapter(context);
        adapter.setList(allList);
        lv_pay_type.setAdapter(adapter);

        lv_pay_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TypeBean typeBean = allList.get(position);
                listener.returnBean(typeBean);
            }
        });

    }

    private void initData() {

        GetDataBiz.getPayTypeData(area, uid, token, com_id, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                if (result == null || result.equals("")){
                    return;
                }
                Gson gson = new Gson();
                List<TypeBean> list = new ArrayList<TypeBean>();
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("code").equals("200")){
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0 ; i < jsonArray.length() ; i++){
                        JSONObject dataJsonObject = jsonArray.getJSONObject(i);
                        TypeBean bean = gson.fromJson(dataJsonObject.toString() , TypeBean.class);
                        list.add(bean);
                    }
                    handler.obtainMessage(200 , list).sendToTarget();
                }else{
                    handler.obtainMessage(200 , list).sendToTarget();
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });

    }

}
