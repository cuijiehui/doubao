package com.xinspace.csevent.monitor.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.xinspace.csevent.monitor.adapter.AfficheAdapter;
import com.xinspace.csevent.monitor.bean.AfficheBean;
import com.xinspace.csevent.R;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.biz.GetDataBiz;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.SharedPreferencesUtil1;
import com.xinspace.csevent.util.StatusBarUtils;
import com.xinspace.csevent.util.ToastUtil;
import com.xinspace.csevent.ui.activity.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.xinspace.csevent.app.AppConfig.COMMUNITY_AREA;

/**
 * 公告列表
 *
 * Created by Android on 2017/4/6.
 */

public class AfficheAct extends BaseActivity{

    private LinearLayout ll_affiche_back;
    private RecyclerView rc_affiche;
    private AfficheAdapter adapter;
    private List<AfficheBean> allList;
    private SDPreference preference;
    private String cUid;
    private String cToken;
    private int page;
    private boolean isMore;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 200:
                    if (msg.obj != null){

                        allList.addAll((Collection<? extends AfficheBean>) msg.obj);
                        adapter.notifyDataSetChanged();

                    }else{
                        ToastUtil.makeToast("暂无公告");
                    }
                    break;
            }
        }
    };
    private String area;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_affiche);

        StatusBarUtils.setWindowStatusBarColor(this , R.color.app_bottom_color);

        area = SharedPreferencesUtil1.getString(this, COMMUNITY_AREA, "");
        preference = SDPreference.getInstance();
        cUid = preference.getContent("cUid");
        cToken = preference.getContent("cToken");

        page = 1;

        initView();
        initData(page);
    }


    private void initView() {

        allList = new ArrayList<>();

        ll_affiche_back = (LinearLayout) findViewById(R.id.ll_affiche_back);
        ll_affiche_back.setOnClickListener(clickListener);

        rc_affiche = (RecyclerView) findViewById(R.id.lv_affiche);
        adapter = new AfficheAdapter(AfficheAct.this , allList );
        rc_affiche.setAdapter(adapter);

        rc_affiche.setLayoutManager(new LinearLayoutManager(AfficheAct.this, LinearLayoutManager.VERTICAL, false));
        rc_affiche.setAdapter(adapter);

        rc_affiche.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();

                int lastCompletelyVisibleItemPosition = ((LinearLayoutManager) manager).findLastCompletelyVisibleItemPosition();
                if (adapter.getItemCount() > 3 && lastCompletelyVisibleItemPosition == adapter.getItemCount() - 1 && isMore) {
                    LogUtil.i("加载下一页-----------------");
                    page++;
                    initData(page);
                    isMore = false;
                }
            }
        });
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_affiche_back:
                    AfficheAct.this.finish();
                    break;
            }
        }
    };

    private void initData(int page) {

        GetDataBiz.getNoticeListData(area, cUid, cToken, String.valueOf(page), new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                LogUtil.i("公告列表数据" + result);
                if (result == null || result.equals("")){
                    return;
                }

                JSONObject jsonObject = new JSONObject(result);
                List<AfficheBean> list = new ArrayList<AfficheBean>();
                Gson gson = new Gson();
                if (jsonObject.getString("code").equals("200")){
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0 ; i < jsonArray.length() ; i++){
                        JSONObject dataJsonobject = jsonArray.getJSONObject(i);
                        AfficheBean afficheBean = gson.fromJson(dataJsonobject.toString() , AfficheBean.class);
                        list.add(afficheBean);
                    }

                    if (list.size() != 0){
                        isMore = true;
                    }else{
                        isMore = false;
                    }
                    handler.obtainMessage(200 , list).sendToTarget();
                }else{
                    handler.obtainMessage(400).sendToTarget();
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }


    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        this.setContentView(R.layout.empty_view);
        clickListener = null;
        allList = null;
        System.gc();
        super.onDestroy();
    }


}
