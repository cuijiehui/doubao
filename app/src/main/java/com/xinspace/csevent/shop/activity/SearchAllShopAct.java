package com.xinspace.csevent.shop.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xinspace.csevent.R;
import com.xinspace.csevent.data.biz.GetActivityListBiz;
import com.xinspace.csevent.data.entity.GoodsModle;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.shop.adapter.SearchShopAdapter;
import com.xinspace.csevent.shop.modle.ScreenGoodsBean;
import com.xinspace.csevent.shop.weiget.GoodScreenPop;
import com.xinspace.csevent.shop.weiget.ScreenListener;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.StatusBarUtils;
import com.xinspace.csevent.util.ToastUtil;
import com.xinspace.csevent.ui.activity.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 商城搜索界面
 * <p>
 * Created by Android on 2017/6/27.
 */

public class SearchAllShopAct extends BaseActivity {

    private LinearLayout ll_back;
    private EditText et_goods_search;
    private TextView tv_search;
    private TextView tv_synthetical; //综合

    private LinearLayout lin_sale;
    private TextView tv_sale_num;
    private ImageView img_sale;

    private LinearLayout lin_price;
    private TextView tv_price;
    private ImageView img_price;

    private LinearLayout lin_screen;
    private TextView tv_screen;
    private ImageView img_screen;
    private Intent intent;
    private String cateId;
    private String searchKey;
    private String type;
    private List<GoodsModle> allList = new ArrayList<GoodsModle>();
    private boolean isMore;
    private RecyclerView rv_search_shop;
    private SearchShopAdapter adapter;

    private boolean isScreen = false; //是否筛选
    private boolean isPrice = false;  //是否点击价钱
    private boolean isPriceTop = false;  //点击价钱的排序

    private boolean isSale = false;
    private boolean isSaleTop = false;

    private GoodScreenPop goodScreenPop;
    private String screenType;  //筛选的分类
    private String typeId;      //弹窗选择的分类id

    private ScreenGoodsBean goodsBean = new ScreenGoodsBean();
    private int page = 1;
    private RelativeLayout rel_data_load;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 200:
                    if (msg.obj != null) {
                        allList.addAll((Collection<? extends GoodsModle>) msg.obj);
                        adapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(SearchAllShopAct.this, R.color.color_font);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        setContentView(R.layout.act_search_all_shop);
        intent = getIntent();
        if (intent != null) {
            type = intent.getStringExtra("type");
            if (type.equals("1")) {     //分类
                cateId = intent.getStringExtra("cate");
                LogUtil.i("--------cateId---------" + cateId);

            } else if (type.equals("2")) {   //搜索
                searchKey = intent.getStringExtra("searchKey");
            }
        }

        initView();

        if (type.equals("1")) {
            goodsBean.setCate(cateId);
            goodsBean.setKeywords("");
            goodsBean.setIsdiscount("");
            goodsBean.setIshot("");
            goodsBean.setIsnew("");
            goodsBean.setIsrecommand("");
            goodsBean.setIssendfree("");
            goodsBean.setIstime("");
            initData(String.valueOf(page), goodsBean);

        } else if (type.equals("2")) {

            goodsBean.setKeywords(searchKey);
            goodsBean.setCate("");
            goodsBean.setIsdiscount("");
            goodsBean.setIshot("");
            goodsBean.setIsnew("");
            goodsBean.setIsrecommand("");
            goodsBean.setIssendfree("");
            goodsBean.setIstime("");
            initData(String.valueOf(page), goodsBean);
        }
    }


    private void initView() {
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setFocusable(true);
        ll_back.setOnClickListener(clickListener);

        rel_data_load = (RelativeLayout) findViewById(R.id.rel_data_load);

        et_goods_search = (EditText) findViewById(R.id.et_goods_search);
        tv_search = (TextView) findViewById(R.id.tv_search);
        tv_synthetical = (TextView) findViewById(R.id.tv_synthetical);

        tv_search.setOnClickListener(clickListener);
        tv_synthetical.setOnClickListener(clickListener);

        lin_sale = (LinearLayout) findViewById(R.id.lin_sale);
        tv_sale_num = (TextView) findViewById(R.id.tv_sale_num);
        img_sale = (ImageView) findViewById(R.id.img_sale);
        lin_sale.setOnClickListener(clickListener);

        lin_price = (LinearLayout) findViewById(R.id.lin_price);
        tv_price = (TextView) findViewById(R.id.tv_price);
        img_price = (ImageView) findViewById(R.id.img_price);

        lin_screen = (LinearLayout) findViewById(R.id.lin_screen);
        tv_screen = (TextView) findViewById(R.id.tv_screen);
        img_screen = (ImageView) findViewById(R.id.img_screen);

        lin_price.setOnClickListener(clickListener);
        lin_screen.setOnClickListener(clickListener);

        rv_search_shop = (RecyclerView) findViewById(R.id.rv_search_shop);
        adapter = new SearchShopAdapter(SearchAllShopAct.this, allList);
        rv_search_shop.setLayoutManager(new LinearLayoutManager(SearchAllShopAct.this, LinearLayoutManager.VERTICAL, false));
        rv_search_shop.setAdapter(adapter);

        rv_search_shop.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();

                int lastCompletelyVisibleItemPosition = ((LinearLayoutManager) manager).findLastCompletelyVisibleItemPosition();
                if (adapter.getItemCount() > 4 && lastCompletelyVisibleItemPosition == adapter.getItemCount() - 1 && isMore) {
                    LogUtil.i("加载下一页-----------------");
                    page++;
                    initData(String.valueOf(page), goodsBean);
                    isMore = false;
                }
            }
        });
    }

    private void initData(String page, ScreenGoodsBean bean) {
        adapter.notifyDataSetChanged();
        rel_data_load.setVisibility(View.VISIBLE);
        GetActivityListBiz.getGoodsList3(page, bean, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                LogUtil.i("首页搜索返回的数据" + result);
                if (TextUtils.isEmpty(result)) {
                    return;
                }
                rel_data_load.setVisibility(View.GONE);
                JSONObject obj = new JSONObject(result);
                String type = obj.getString("code");
                if (type.equals("200")) {
                    JSONObject jsonObject = obj.getJSONObject("data");
                    JSONArray act_ary = jsonObject.getJSONArray("list");
                    List<GoodsModle> goodsEntities = new ArrayList<>();
                    for (int i = 0; i < act_ary.length(); i++) {
                        JSONObject j = act_ary.getJSONObject(i);
                        Gson gson = new Gson();
                        GoodsModle goodsModle = gson.fromJson(j.toString(), GoodsModle.class);
                        goodsEntities.add(goodsModle);
                    }
                    LogUtil.i("商品分类列表" + goodsEntities.size());
                    if (goodsEntities.size() != 0) {
                        handler.obtainMessage(200, goodsEntities).sendToTarget();
                        isMore = true;
                    } else {
                        ToastUtil.makeToast("没有更多商品");
                        isMore = false;
                    }
                } else {
                    ToastUtil.makeToast("数据出错");
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
            switch (v.getId()) {
                case R.id.ll_back:
                    SearchAllShopAct.this.finish();
                    break;
                case R.id.tv_search: // 在分类界面搜索
                    searchKey = et_goods_search.getText().toString().trim();
                    if (goodScreenPop != null && goodScreenPop.isShowing()) {
                        goodScreenPop.dismiss();
                    }

                    if (allList.size() != 0) {
                        allList.clear();
                    }
                    page = 1;

                    goodsBean.setKeywords(searchKey);
                    goodsBean.setCate("");
                    goodsBean.setIsdiscount("");
                    goodsBean.setIshot("");
                    goodsBean.setIsnew("");
                    goodsBean.setIsrecommand("");
                    goodsBean.setIssendfree("");
                    goodsBean.setIstime("");

                    initData(String.valueOf(page), goodsBean);

//                    if (searchKey != null && !searchKey.equals("")) {
//
//                    } else {
//                        ToastUtil.makeToast("请输入搜索关键词");
//                    }
                    break;
                case R.id.lin_sale: //销量

                    tv_synthetical.setTextColor(Color.parseColor("#333333"));
                    tv_sale_num.setTextColor(Color.parseColor("#ea5205"));
                    tv_price.setTextColor(Color.parseColor("#333333"));
                    tv_screen.setTextColor(Color.parseColor("#333333"));

                    if (goodScreenPop != null && goodScreenPop.isShowing()) {
                        goodScreenPop.dismiss();
                    }
                    img_price.setImageResource(R.drawable.icon_price_default);
                    isPrice = false;  //是否点击价钱
                    isPriceTop = false;
                    isSale = false;

                    goodsBean.setOrder("sales");

                    if (!isSaleTop) {
                        isSaleTop = false;
                        img_sale.setImageResource(R.drawable.icon_price_top);  //高到低
                        goodsBean.setBy("desc");
                    }


//                    if (isSale) {
//                        if (isSaleTop) {
//                            isSaleTop = false;
//                            img_sale.setImageResource(R.drawable.icon_price_top);  //高到低
//                            goodsBean.setBy("desc");
//                        } else {
//                            isSaleTop = true;
//                            img_sale.setImageResource(R.drawable.icon_price_low);  //低到高
//                            goodsBean.setBy("asc");
//                        }
//                    } else {
//                        isSale = true;
//                        isSaleTop = true;
//                        img_sale.setImageResource(R.drawable.icon_price_low);      //低到高
//                        goodsBean.setBy("asc");
//                    }
                    if (allList.size() != 0) {
                        allList.clear();
                    }
                    page = 1;
                    initData(String.valueOf(page), goodsBean);

                    break;
                case R.id.tv_synthetical: //综合

                    if (goodScreenPop != null && goodScreenPop.isShowing()) {
                        goodScreenPop.dismiss();
                    }
                    img_price.setImageResource(R.drawable.icon_price_default);

                    tv_synthetical.setTextColor(Color.parseColor("#ea5205"));
                    tv_sale_num.setTextColor(Color.parseColor("#333333"));
                    tv_price.setTextColor(Color.parseColor("#333333"));
                    tv_screen.setTextColor(Color.parseColor("#333333"));

                    goodsBean.setKeywords(searchKey);
                    goodsBean.setCate(cateId);
                    goodsBean.setIsdiscount("");
                    goodsBean.setIshot("");
                    goodsBean.setIsnew("");
                    goodsBean.setIsrecommand("");
                    goodsBean.setIssendfree("");
                    goodsBean.setIstime("");
                    goodsBean.setOrder("");
                    goodsBean.setBy("");

                    isPrice = false;  //是否点击价钱
                    isPriceTop = false;

                    isSale = false;
                    isSaleTop = false;

                    img_sale.setImageResource(R.drawable.icon_price_default);
                    img_price.setImageResource(R.drawable.icon_price_default);

                    if (allList.size() != 0) {
                        allList.clear();
                    }
                    page = 1;
                    initData(String.valueOf(page), goodsBean);

                    break;
                case R.id.lin_price: //点击价格

                    tv_synthetical.setTextColor(Color.parseColor("#333333"));
                    tv_sale_num.setTextColor(Color.parseColor("#333333"));
                    tv_price.setTextColor(Color.parseColor("#ea5205"));
                    tv_screen.setTextColor(Color.parseColor("#333333"));

                    if (goodScreenPop != null && goodScreenPop.isShowing()) {
                        goodScreenPop.dismiss();
                    }
                    goodsBean.setOrder("minprice");

                    if (isPrice) {
                        if (isPriceTop) {
                            isPriceTop = false;
                            img_price.setImageResource(R.drawable.icon_price_top);  //高到低
                            goodsBean.setBy("desc");
                        } else {
                            isPriceTop = true;
                            img_price.setImageResource(R.drawable.icon_price_low);  //低到高
                            goodsBean.setBy("asc");
                        }
                    } else {
                        isPrice = true;
                        isPriceTop = true;
                        img_price.setImageResource(R.drawable.icon_price_low);      //低到高
                        goodsBean.setBy("asc");
                    }
                    if (allList.size() != 0) {
                        allList.clear();
                    }
                    page = 1;
                    initData(String.valueOf(page), goodsBean);

                    break;
                case R.id.lin_screen: //点击筛选

                    tv_synthetical.setTextColor(Color.parseColor("#333333"));
                    tv_sale_num.setTextColor(Color.parseColor("#333333"));
                    tv_price.setTextColor(Color.parseColor("#333333"));
                    tv_screen.setTextColor(Color.parseColor("#ea5205"));

                    img_price.setImageResource(R.drawable.icon_price_default);
                    isPrice = false;  //是否点击价钱
                    isPriceTop = false;
                    isSale = false;

                    if (isScreen) {
                        isScreen = false;
                        tv_screen.setTextColor(Color.parseColor("#333333"));
                        img_screen.setImageResource(R.drawable.icon_screen);
                        showPop();
                    } else {
                        isScreen = true;
                        tv_screen.setTextColor(Color.parseColor("#ea5205"));
                        img_screen.setImageResource(R.drawable.icon_screen_press);
                        showPop();
                    }
                    break;
            }
        }
    };

    private void showPop() {

        if (goodScreenPop == null) {
            goodScreenPop = new GoodScreenPop(SearchAllShopAct.this, screenListener);
            goodScreenPop.setOnDismissListener(dismissListener);
        }

        if (!goodScreenPop.isShowing()) {
            goodScreenPop.showAtLocation(lin_screen, Gravity.BOTTOM, 0, 0);
            goodScreenPop.isShowing();
        } else {
            goodScreenPop.dismiss();
        }

    }

    PopupWindow.OnDismissListener dismissListener = new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
            goodScreenPop.dismiss();
//            goodScreenPop = null;
            isScreen = false;
            tv_screen.setTextColor(Color.parseColor("#333333"));
            img_screen.setImageResource(R.drawable.icon_screen);
        }
    };


    ScreenListener screenListener = new ScreenListener() {
        @Override
        public void screenListener(String screen, String id) {

            if (!TextUtils.isEmpty(id)) {
                cateId = id;
            }

            screenType = screen;
            LogUtil.i("typeId" + typeId + "screenType" + screenType + "cateId" + cateId);

            //搜索的筛选
            if (!TextUtils.isEmpty(searchKey)) {

                goodsBean.setCate("");
                goodsBean.setKeywords(searchKey);

                if (!TextUtils.isEmpty(screenType)) {
                    if (screenType.equals("推荐商品")) {
                        goodsBean.setIsrecommand("1");

                        goodsBean.setIsdiscount("");
                        goodsBean.setIshot("");
                        goodsBean.setIsnew("");
                        goodsBean.setIssendfree("");
                        goodsBean.setIstime("");
                    }

                    if (screenType.equals("新品上市")) {
                        goodsBean.setIsnew("1");

                        goodsBean.setIsdiscount("");
                        goodsBean.setIshot("");
                        goodsBean.setIsrecommand("");
                        goodsBean.setIssendfree("");
                        goodsBean.setIstime("");
                    }

                    if (screenType.equals("热卖商品")) {
                        goodsBean.setIsnew("1");
                    }

                    if (screenType.equals("促销商品")) {
                        goodsBean.setIsdiscount("1");

                        goodsBean.setIshot("");
                        goodsBean.setIsnew("");
                        goodsBean.setIsrecommand("");
                        goodsBean.setIssendfree("");
                        goodsBean.setIstime("");
                    }

                    if (screenType.equals("卖家包邮")) {
                        goodsBean.setIssendfree("1");

                        goodsBean.setIsdiscount("");
                        goodsBean.setIshot("");
                        goodsBean.setIsnew("");
                        goodsBean.setIsrecommand("");
                        goodsBean.setIstime("");
                    }

                    if (screenType.equals("限时抢购")) {
                        goodsBean.setIstime("1");
                        goodsBean.setIsdiscount("");
                        goodsBean.setIshot("");
                        goodsBean.setIsnew("");
                        goodsBean.setIsrecommand("");
                        goodsBean.setIssendfree("");
                    }
                } else {
                    goodsBean.setIstime("");
                    goodsBean.setIsdiscount("");
                    goodsBean.setIshot("");
                    goodsBean.setIsnew("");
                    goodsBean.setIsrecommand("");
                    goodsBean.setIssendfree("");
                }
            } else {    //分类的筛选

                if (!TextUtils.isEmpty(typeId)) {   //重新选择了分类
                    goodsBean.setCate(typeId);
                    goodsBean.setKeywords("");
                } else {                              //没有重新选择分类
                    goodsBean.setCate(cateId);
                    goodsBean.setKeywords("");
                }

                if (!TextUtils.isEmpty(screenType)) {
                    if (screenType.equals("推荐商品")) {
                        goodsBean.setIsrecommand("1");
                        goodsBean.setIsdiscount("");
                        goodsBean.setIshot("");
                        goodsBean.setIsnew("");
                        goodsBean.setIssendfree("");
                        goodsBean.setIstime("");
                    }

                    if (screenType.equals("新品上市")) {
                        goodsBean.setIsnew("1");
                        goodsBean.setIsdiscount("");
                        goodsBean.setIshot("");
                        goodsBean.setIsrecommand("");
                        goodsBean.setIssendfree("");
                        goodsBean.setIstime("");
                    }

                    if (screenType.equals("热卖商品")) {
                        goodsBean.setIshot("1");
                        goodsBean.setIsnew("");
                        goodsBean.setIsdiscount("");
                        goodsBean.setIsrecommand("");
                        goodsBean.setIssendfree("");
                        goodsBean.setIstime("");
                    }

                    if (screenType.equals("促销商品")) {
                        goodsBean.setIsdiscount("1");
                        goodsBean.setIshot("");
                        goodsBean.setIsnew("");
                        goodsBean.setIsrecommand("");
                        goodsBean.setIssendfree("");
                        goodsBean.setIstime("");
                    }

                    if (screenType.equals("卖家包邮")) {
                        goodsBean.setIssendfree("1");
                        goodsBean.setIsdiscount("");
                        goodsBean.setIshot("");
                        goodsBean.setIsnew("");
                        goodsBean.setIsrecommand("");
                        goodsBean.setIstime("");
                    }

                    if (screenType.equals("限时抢购")) {
                        goodsBean.setIstime("1");
                        goodsBean.setIsdiscount("");
                        goodsBean.setIshot("");
                        goodsBean.setIsnew("");
                        goodsBean.setIsrecommand("");
                        goodsBean.setIssendfree("");
                    }
                } else {
                    goodsBean.setIstime("");
                    goodsBean.setIsdiscount("");
                    goodsBean.setIshot("");
                    goodsBean.setIsnew("");
                    goodsBean.setIsrecommand("");
                    goodsBean.setIssendfree("");
                }
            }


            if (allList.size() != 0) {
                allList.clear();
            }
            page = 1;
            initData(String.valueOf(page), goodsBean);

        }

        @Override
        public void canclePop() {
            goodScreenPop.dismiss();
            goodScreenPop = null;

            img_price.setImageResource(R.drawable.icon_price_default);
            tv_synthetical.setTextColor(Color.parseColor("#333333"));
            tv_sale_num.setTextColor(Color.parseColor("#333333"));
            tv_price.setTextColor(Color.parseColor("#333333"));
            tv_screen.setTextColor(Color.parseColor("#333333"));

            goodsBean.setKeywords(searchKey);
            goodsBean.setCate(cateId);
            goodsBean.setIsdiscount("");
            goodsBean.setIshot("");
            goodsBean.setIsnew("");
            goodsBean.setIsrecommand("");
            goodsBean.setIssendfree("");
            goodsBean.setIstime("");
            goodsBean.setOrder("");
            goodsBean.setBy("");

            isPrice = false;  //是否点击价钱
            isPriceTop = false;

            isSale = false;
            isSaleTop = false;
            img_sale.setImageResource(R.drawable.icon_price_default);
            img_price.setImageResource(R.drawable.icon_price_default);


            if (allList.size() != 0) {
                allList.clear();
            }
            page = 1;
            initData(String.valueOf(page), goodsBean);


        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clickListener = null;
        handler.removeCallbacksAndMessages(null);
    }

}
