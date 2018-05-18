package com.xinspace.csevent.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xinspace.csevent.monitor.view.MyGridView;
import com.xinspace.csevent.R;
import com.xinspace.csevent.app.CoresunApp;
import com.xinspace.csevent.data.biz.GetDataBiz;
import com.xinspace.csevent.login.activity.LoginActivity;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.shop.activity.SearchAllShopAct;
import com.xinspace.csevent.shop.adapter.GoodTypeAdapter;
import com.xinspace.csevent.shop.adapter.TypeCateAdapter;
import com.xinspace.csevent.shop.modle.ChildBean;
import com.xinspace.csevent.shop.modle.ChildGoodsBean;
import com.xinspace.csevent.shop.modle.GoodsTypeBean;
import com.xinspace.csevent.shop.view.CustomMenuListView;
import com.xinspace.csevent.util.ImagerLoaderUtil;
import com.xinspace.csevent.util.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 商城搜索界面fragment
 *
 * Created by Android on 2017/6/27.
 */

public class SearchFragment extends Fragment{

    private View view;
    private EditText et_awards_type_search;
    private TextView tv_search;
    private ImageView iv_vp;

    private CustomMenuListView lv_type;
    private List<GoodsTypeBean> allTypeList = new ArrayList<GoodsTypeBean>();
    private List<ChildBean> allChildList  = new ArrayList<>();
    private List<ChildGoodsBean> allChildBeanList = new ArrayList<ChildGoodsBean>();

    private GoodTypeAdapter goodTypeAdapter;

    private MyGridView gv_child;
    private TypeCateAdapter typeCateAdapter;
    private String searchKey;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:   //广告

                    String url = (String) msg.obj;
                    ImagerLoaderUtil.displayImageWithLoadingIcon(url , iv_vp , R.drawable.icon_detail_load);
                    break;
                case 200:  //分类
                    if (msg.obj != null){
                        allTypeList.clear();
                        allTypeList.addAll((Collection<? extends GoodsTypeBean>) msg.obj);
                        goodTypeAdapter.notifyDataSetChanged();
                        goodTypeAdapter.setPos(0);
                    }
                    break;
                case 300: //子分类
                    if (msg.obj != null){
                        allChildList.addAll((Collection<? extends ChildBean>) msg.obj);
                        allChildBeanList = allChildList.get(0).getChildBeanList();
                        typeCateAdapter.setList(allChildBeanList);
                        typeCateAdapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        boolean isLogin = CoresunApp.isLogin;
        if (!isLogin){
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
        LogUtil.i("--------------------SearchFragment OnCreateView Method-------------------------");

        view = inflater.inflate(R.layout.fra_search , null);
        initView();
        initData();

        return view;
    }

    private void initView() {
        iv_vp = (ImageView) view.findViewById(R.id.iv_vp);

        et_awards_type_search = (EditText) view.findViewById(R.id.et_awards_type_search);
        tv_search = (TextView) view.findViewById(R.id.tv_search);
        tv_search.setOnClickListener(onClickListener);

        lv_type = (CustomMenuListView) view.findViewById(R.id.listMenu);
        goodTypeAdapter = new GoodTypeAdapter(getActivity());
        goodTypeAdapter.setList(allTypeList);
        lv_type.setAdapter(goodTypeAdapter);
        lv_type.setSelection(0);
        lv_type.setOnItemClickListener(onItemClickListener);

        gv_child = (MyGridView) view.findViewById(R.id.gv_child);
        typeCateAdapter = new TypeCateAdapter(getActivity());
        typeCateAdapter.setList(allChildBeanList);
        gv_child.setAdapter(typeCateAdapter);
        gv_child.setOnItemClickListener(onItemClickListener2);
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            goodTypeAdapter.setPos(position);
            goodTypeAdapter.notifyDataSetChanged();

            allChildBeanList = allChildList.get(position).getChildBeanList();
            typeCateAdapter.setList(allChildBeanList);
            typeCateAdapter.notifyDataSetChanged();
        }
    };

    AdapterView.OnItemClickListener onItemClickListener2 = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(getActivity() , SearchAllShopAct.class);
            intent.putExtra("type" , "1");
            intent.putExtra("cate" , allChildBeanList.get(position).getId());
            startActivity(intent);
        }
    };

    private void initData() {
        GetDataBiz.GET_SHOP_TYPE(new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                LogUtil.i("获取商城分类" + result);
                if (result == null && result.equals("")){
                    return;
                }

                JSONObject jsonObject = new JSONObject(result);
                Gson gson = new Gson();
                JSONObject dataJsonobject = jsonObject.getJSONObject("data");

                JSONObject advJsonObject = dataJsonobject.getJSONObject("adv");
                String imgUrl = advJsonObject.getString("advimg");

                JSONObject categoryJsonobject = dataJsonobject.getJSONObject("category");
                JSONArray parentJSONArray = categoryJsonobject.getJSONArray("parent");

                List<GoodsTypeBean> typeBeanList = new ArrayList<GoodsTypeBean>();
                for (int i = 0 ; i < parentJSONArray.length() ; i++){
                    JSONObject beanJsonObject = parentJSONArray.getJSONObject(i);
                    GoodsTypeBean bean = gson.fromJson(beanJsonObject.toString() , GoodsTypeBean.class);
                    typeBeanList.add(bean);
                }

                LogUtil.i("--------------------上边解析完了没-------------------------");

                JSONObject childrenJsonobject = categoryJsonobject.getJSONObject("children");

                List<ChildBean> childList = new ArrayList<ChildBean>();
                for (int i = 0 ; i < parentJSONArray.length() ; i++){
                    LogUtil.i("----11111---" + typeBeanList.get(i).getId());
                    JSONArray jsonArray = childrenJsonobject.getJSONArray(typeBeanList.get(i).getId());

                    List<ChildGoodsBean> childBeanList = new ArrayList<ChildGoodsBean>();
                    for (int j = 0 ; j < jsonArray.length() ; j++){
                        JSONObject childJsonObject = jsonArray.getJSONObject(j);
                        ChildGoodsBean childGoodsBean = gson.fromJson(childJsonObject.toString() , ChildGoodsBean.class);
                        childBeanList.add(childGoodsBean);
                    }
                    ChildBean childBean = new ChildBean();
                    childBean.setId(i+"");
                    childBean.setChildBeanList(childBeanList);
                    childList.add(childBean);
                    LogUtil.i("-------" + typeBeanList.get(i).getId());
                }

                handler.obtainMessage(100 , imgUrl).sendToTarget();
                handler.obtainMessage(200 , typeBeanList).sendToTarget();
                handler.obtainMessage(300 , childList).sendToTarget();
                LogUtil.i("--------------------解析完了没-------------------------" + childList.size());
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_search:
                    searchKey = et_awards_type_search.getText().toString().trim();
                    Intent intent = new Intent(getActivity() , SearchAllShopAct.class);
                    intent.putExtra("type" , "2");
                    intent.putExtra("searchKey" , searchKey);
                    startActivity(intent);
                    break;
            }
        }
    };

}
