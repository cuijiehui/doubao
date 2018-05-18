package com.xinspace.csevent.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.data.biz.GetActivityListBiz;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.shop.activity.GoodsDetailAct;
import com.xinspace.csevent.shop.modle.DianxinBean;
import com.xinspace.csevent.sweepstake.adapter.DianGoodsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Android on 2017/3/19.
 */

public class DianxinFragment extends Fragment{

    private View view;
    private ListView lv_shop_goods;
    private DianGoodsAdapter adapter;
    private List<DianxinBean> goods_List = new ArrayList<DianxinBean>();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case 100:
                    if (msg.obj != null){
                        goods_List.addAll((Collection<? extends DianxinBean>) msg.obj);
                        Log.i("www" , "8888888888888888888888" + goods_List.size());
                        adapter.setList(goods_List);
                        adapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_dianxin_web , null);

        initView();

        return view;
    }

    private void initView() {
//        webShop = (WebView) view.findViewById(R.id.web_shop_class);
//         WebSettings settings = webShop.getSettings();
//        //设置可以使用javascript
//        settings.setJavaScriptEnabled(true);
//        settings.setDefaultTextEncodingName("gb2312");
//
//        // 这两行代码一定加上否则效果不会出现 加入SSH
//        webShop.setWebViewClient(new WebViewClient() {
//            public void onReceivedSslError(WebView view,
//                                           SslErrorHandler handler, SslError error) {
//                ///handler.cancel(); 默认的处理方式，WebView变成空白页
//                handler.proceed(); // 接受证书
//                //handleMessage(Message msg); 其他处理
//
//            }
//        });
//
//        //加载页面
//        webShop.loadUrl("http://shop.coresun.net/weixin/list_pr.html?classid=1");

        lv_shop_goods = (ListView) view.findViewById(R.id.lv_shop_goods);
        adapter = new DianGoodsAdapter(getActivity());
        adapter.setList(goods_List);
        lv_shop_goods.setAdapter(adapter);

        getGoodsList();

        lv_shop_goods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity() , GoodsDetailAct.class);
                intent.putExtra("goodId" , goods_List.get(position).getCid());
                startActivity(intent);
            }
        });
    }

    private void getGoodsList() {
        GetActivityListBiz.getGoodsList("16", new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                try {
                    //刷新完成
//                    lv_shop_one.onRefreshComplete();
//                    listView.getLoadingLayoutProxy(true, false).setLastUpdatedLabel("上次更新:" + TimeUtil.getTime());
//                    Log.i("www", "微信商城" + result.toString());
                    Log.i("www" , "电信的商品" + result);
                    showGoodsList(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onHttpRequestError(String error) {

                Log.i("www", "微信商城error   " + error.toString());
//                if (null == activityListAdapter) {
//                    activityListAdapter = new GridViewActivityAdapter(getActivity(), act_list);
//                    listView.setAdapter(activityListAdapter);
//                }
//                listView.onRefreshComplete();
            }
        });
    }


    private void showGoodsList(String result) throws Exception {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("result");
            List<DianxinBean> goodsEntities = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject j = jsonArray.getJSONObject(i);
                DianxinBean dianxinBean = new DianxinBean();
                dianxinBean.setCname(j.getString("cname"));
                dianxinBean.setPrice(j.getString("price"));
                dianxinBean.setSort(j.getString("sort"));
                dianxinBean.setImg(j.getString("img"));
                dianxinBean.setDiscount(j.getString("discount"));
                dianxinBean.setCid(j.getString("cid"));
                goodsEntities.add(dianxinBean);
            }
            Log.i("www" , "wwwwwwwwwwww" + goodsEntities.size());
            handler.obtainMessage(100 , goodsEntities).sendToTarget();
    }

}
