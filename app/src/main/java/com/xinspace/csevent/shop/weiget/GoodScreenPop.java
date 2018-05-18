package com.xinspace.csevent.shop.weiget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xinspace.csevent.R;
import com.xinspace.csevent.app.utils.ScreenUtils;
import com.xinspace.csevent.data.biz.GetDataBiz;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.shop.adapter.GoodTypeAdapter;
import com.xinspace.csevent.shop.adapter.PopScreenAdapter;
import com.xinspace.csevent.shop.adapter.TypeCateAdapter2;
import com.xinspace.csevent.shop.modle.ChildBean;
import com.xinspace.csevent.shop.modle.ChildGoodsBean;
import com.xinspace.csevent.shop.modle.FilterBean;
import com.xinspace.csevent.shop.modle.GoodsTypeBean;
import com.xinspace.csevent.shop.view.CustomMenuListView;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 筛选的弹框
 *
 * Created by Android on 2017/6/28.
 */

public class GoodScreenPop extends PopupWindow{

    private Context context;
    private View view;
    private GridView gv_screen;
    private TextView tv_cancel ;
    private TextView tv_ok;
    private CustomMenuListView listMenu;
    private CustomMenuListView listContent;
    //private List<String> list = new ArrayList<>();
    private List<FilterBean> filterBeanList = new ArrayList<>();

    private PopScreenAdapter adapter;

    private List<GoodsTypeBean> allTypeList = new ArrayList<GoodsTypeBean>();
    private List<ChildBean> allChildList  = new ArrayList<>();
    private List<ChildGoodsBean> allChildBeanList = new ArrayList<ChildGoodsBean>();
    private GoodTypeAdapter goodTypeAdapter;
    private TypeCateAdapter2 typeCateAdapter;
    private ScreenListener screenListener;
    private String screenType = "";
    private String typeId = "";
    private String[] screenName = new String[]{
            "推荐商品",
            "新品上市",
            "热卖商品",
            "促销商品",
            "卖家包邮",
            "限时抢购"
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 200:  //分类
                    if (msg.obj != null){
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

    public GoodScreenPop(Context context , ScreenListener screenListener) {
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.pop_good_screen, null);
        this.setContentView(view);
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        this.setHeight(ScreenUtils.getScreenHeight(context) - ScreenUtils.getStatusBarHeight(context) - ScreenUtils.dpToPx(context , 93));
        this.screenListener = screenListener;
        setBackgroundDrawable(new BitmapDrawable());
        setTouchable(true);

        initView();
        initData();
        initTypeData();
    }

    private void initView() {

//        list.add("推荐商品");
//        list.add("新品上市");
//        list.add("热卖商品");
//        list.add("促销商品");
//        list.add("卖家包邮");
//        list.add("限时抢购");

        for (int i = 0 ; i < screenName.length ; i++){
            FilterBean filterBean = new FilterBean();
            filterBean.setSelect(false);
            filterBean.setName(screenName[i]);
            filterBeanList.add(filterBean);
        }

        tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_ok = (TextView) view.findViewById(R.id.tv_ok);

        tv_cancel.setOnClickListener(clickListener);
        tv_ok.setOnClickListener(clickListener);

        gv_screen = (GridView) view.findViewById(R.id.gv_screen);
        gv_screen.setFocusable(true);
        adapter = new PopScreenAdapter(context);
        adapter.setList(filterBeanList);
        gv_screen.setAdapter(adapter);
        gv_screen.setOnItemClickListener(onItemClickListener);

        listMenu = (CustomMenuListView) view.findViewById(R.id.listMenu);
        goodTypeAdapter = new GoodTypeAdapter(context);
        goodTypeAdapter.setList(allTypeList);
        listMenu.setAdapter(goodTypeAdapter);
        listMenu.setSelection(0);
        listMenu.setOnItemClickListener(onItemClickListener2);

        listContent = (CustomMenuListView) view.findViewById(R.id.listContent);
        typeCateAdapter = new TypeCateAdapter2(context);
        typeCateAdapter.setList(allChildBeanList);
        listContent.setAdapter(typeCateAdapter);
        listContent.setOnItemClickListener(onItemClickListener3);
    }

    private void initData() {

    }

    /**
     *
     */
    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            screenType = filterBeanList.get(position).getName();
//            LogUtil.i("------screenType-----" + screenType);
//            //adapter.setPos(position);
//            adapter.notifyDataSetChanged();
        }
    };

    AdapterView.OnItemClickListener onItemClickListener2 = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            goodTypeAdapter.setPos(position);
            goodTypeAdapter.notifyDataSetChanged();
            typeCateAdapter.setPos(-1);
            allChildBeanList = allChildList.get(position).getChildBeanList();
            typeCateAdapter.setList(allChildBeanList);
            typeCateAdapter.notifyDataSetChanged();
        }
    };

    AdapterView.OnItemClickListener onItemClickListener3 = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            typeId = allChildBeanList.get(position).getId();
            typeCateAdapter.setPos(position);
            typeCateAdapter.notifyDataSetChanged();
        }
    };


    private void initTypeData() {
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

                //LogUtil.i("--------------------上边解析完了没-------------------------");

                JSONObject childrenJsonobject = categoryJsonobject.getJSONObject("children");

                List<ChildBean> childList = new ArrayList<ChildBean>();
                for (int i = 0 ; i < parentJSONArray.length() ; i++){
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
                }

                handler.obtainMessage(200 , typeBeanList).sendToTarget();
                handler.obtainMessage(300 , childList).sendToTarget();
                //LogUtil.i("--------------------解析完了没-------------------------");
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
                case R.id.tv_cancel:

                    //GoodScreenPop.this.dismiss();
                    screenListener.canclePop();

                    break;
                case R.id.tv_ok:

                    LogUtil.i("------" + filterBeanList.get(0).isSelect());

                    if (filterBeanList.get(0).isSelect()){
                        screenType = filterBeanList.get(0).getName();
                    }else if (filterBeanList.get(1).isSelect()){
                        screenType = filterBeanList.get(1).getName();
                    }else if (filterBeanList.get(2).isSelect()){
                        screenType = filterBeanList.get(2).getName();
                    }else if (filterBeanList.get(3).isSelect()){
                        screenType = filterBeanList.get(3).getName();
                    }else if (filterBeanList.get(4).isSelect()){
                        screenType = filterBeanList.get(4).getName();
                    }else if (filterBeanList.get(5).isSelect()){
                        screenType = filterBeanList.get(5).getName();
                    }

                    if (!TextUtils.isEmpty(screenType) || !TextUtils.isEmpty(typeId)){
                        screenListener.screenListener(screenType , typeId);
                        GoodScreenPop.this.dismiss();
                    }else {
                        ToastUtil.makeToast("您未选择筛选条件");
                    }

                    break;
            }
        }
    };

}
