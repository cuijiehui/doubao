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
import android.widget.TextView;

import com.google.gson.Gson;
import com.xinspace.csevent.monitor.adapter.ComChildAdapter;
import com.xinspace.csevent.monitor.bean.ChildCommunityBean;
import com.xinspace.csevent.R;
import com.xinspace.csevent.app.utils.ScreenUtils;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.biz.GetDataBiz;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.SharedPreferencesUtil1;
import com.xinspace.csevent.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.xinspace.csevent.app.AppConfig.COMMUNITY_AREA;

/**
 * 选择房号的弹窗
 *
 * Created by Android on 2017/6/2.
 */

public class ChooseCodePop extends PopupWindow{

    private View view;
    private Context context;
    private SDPreference preference;
    private String cUid;
    private String token;

    private TextView tv_submit;
    private TextView tv_cancel;
    private String id;
    private List<ChildCommunityBean> allList = new ArrayList<>();

    private CodeResultListener codeResultListener;
    private int current = -1;
    private ListView lv_code;
    private ComChildAdapter childAdapter;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 200:
                    if (msg.obj != null){
                        allList.addAll((Collection<? extends ChildCommunityBean>) msg.obj);
                        childAdapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };
    private String area;


    public ChooseCodePop(Context context , String id , CodeResultListener codeResultListener) {
        super(context);
        this.context = context;
        this.codeResultListener = codeResultListener;
        this.id = id;
        view = LayoutInflater.from(context).inflate(R.layout.pop_choose_code, null);
        this.setContentView(view);
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        this.setHeight(ScreenUtils.getScreenHeight(context) - ScreenUtils.getStatusBarHeight(context) - ScreenUtils.dpToPx(context , Float.valueOf(45)));
        setBackgroundDrawable(new BitmapDrawable());
        setTouchable(true);
        initView();
        initData();
    }

    private void initView() {
        preference = SDPreference.getInstance();
        cUid = preference.getContent("cUid");
        token = preference.getContent("cToken");
        area = SharedPreferencesUtil1.getString(context, COMMUNITY_AREA, "");

        tv_submit = (TextView) view.findViewById(R.id.tv_submit);
        tv_submit.setOnClickListener(clickListener);

        tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(clickListener);

        lv_code = (ListView) view.findViewById(R.id.lv_code);
        childAdapter = new ComChildAdapter(context);
        childAdapter.setList(allList);
        lv_code.setAdapter(childAdapter);

        lv_code.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                childAdapter.setSelectItem(position);
                current = position;
                childAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initData() {

        GetDataBiz.getCodeListData(area, cUid, token, id ,new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                LogUtil.i("楼栋下级房间列表" + result);
                if (result == null || result.equals("")){
                    return;
                }
                Gson gson = new Gson();
                JSONObject jsonObject = new JSONObject(result);
                List<ChildCommunityBean> list = new ArrayList<ChildCommunityBean>();

                if (jsonObject.getString("code").equals("200")){
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0 ; i < jsonArray.length() ; i++){
                        JSONObject dataJsonObject = jsonArray.getJSONObject(i);
                        ChildCommunityBean communityBean = gson.fromJson(dataJsonObject.toString() , ChildCommunityBean.class);
                        list.add(communityBean);
                    }
                    handler.obtainMessage(200 , list).sendToTarget();
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }


    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_submit:

                    if (current != -1){
                        codeResultListener.codeBean(allList.get(current));
                        ChooseCodePop.this.dismiss();
                    }else{
                        ToastUtil.makeToast("未选择房间号");
                    }

                    break;
                case R.id.tv_cancel:
                    ChooseCodePop.this.dismiss();
                    break;
            }
        }
    };
}
