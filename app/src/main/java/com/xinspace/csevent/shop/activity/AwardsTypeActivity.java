package com.xinspace.csevent.shop.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.app.CoresunApp;
import com.xinspace.csevent.data.biz.AwardsTypeBiz;
import com.xinspace.csevent.shop.adapter.GoodsAdapter;
import com.xinspace.csevent.shop.adapter.MenuAdapter;
import com.xinspace.csevent.shop.modle.GoodsBean;
import com.xinspace.csevent.shop.view.CustomMenuListView;
import com.xinspace.csevent.data.entity.ActivityClassEntity;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.parser.JsonPaser;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.StatusBarUtils;
import com.xinspace.csevent.util.ToastUtil;
import com.xinspace.csevent.ui.activity.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 商品类型搜索页面
 */
public class AwardsTypeActivity extends BaseActivity {
    public CustomMenuListView lvMenu;
    private CustomMenuListView lvContent;
    public int currentItem;
    private MenuAdapter menuAdapter;
    private List<Object> classList = new ArrayList<>();
    private GoodsAdapter awardsTypeContentAdapter;
    private EditText etSearch;
    private List<Object> actList = new ArrayList<>();//活动列表
    private List<Object> allActList = new ArrayList<>();
    private boolean isSearch = false;//是否是搜索
    private LinearLayout llBack;
    private boolean isLast;
    private String typeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StatusBarUtils.setWindowStatusBarColor(AwardsTypeActivity.this , R.color.app_bottom_color);

        setContentView(R.layout.activity_awards_type);
        setViews();
        setListeners();
        //在进入页面的时候获取活动分类
        getActivityClass();
        /**判断是不是第一次使用程序,是则加载新手指导页面*/
        //NewcomersTutorialUtil.loadToNewcomersTutorial(this,"uniquePage",R.layout.dialog_tutorial_for_unique);
    }

    //提供方法将获取的活动列表添加到成员的集合中
    private void addDataToList(List<Object> list) {
        //actList.clear();
        allActList.addAll(list);
        awardsTypeContentAdapter.setList(allActList);
        awardsTypeContentAdapter.notifyDataSetChanged();
    }


    //根据活动分类获取相应的活动列表
    private void getActivityListByClass(String typeId, int start) {
        AwardsTypeBiz.getTypeContent(this, String.valueOf(start), typeId,
                new HttpRequestListener() {//奖品分类后获取的内容的数据回调
                    @Override
                    public void onHttpRequestFinish(String result) throws JSONException {
                        if (result == null && result.equals("")) {
                            return;
                        }
                        LogUtil.i("具体分类列表" + result);
                        showActivityList(result);
                    }

                    @Override
                    public void onHttpRequestError(String error) {

                    }
                });
    }

    //显示活动列表
    private void showActivityList(String result) throws JSONException {
        LogUtil.i("奖品分类内容数据:" + result);
        if (result == null || result.equals("")) {
            return;
        }

        JSONObject obj = new JSONObject(result);
        String res = obj.getString("result");
        if (res.equals("200")) {
            JSONArray act_ary = obj.getJSONArray("class_data");
            List<Object> awardList = JsonPaser.parserAry(act_ary.toString(), GoodsBean.class);

            //将数据放入成员的集合中
            addDataToList(awardList);

        } else if (res.equals("201")) {
            ToastUtil.makeToast("无相关活动");
        } else {
            ToastUtil.makeToast("获取活动失败");
        }
    }

    //获取活动分类
    private void getActivityClass() {
        //获取奖品分类菜单数据
        AwardsTypeBiz.getClassList(this, CoresunApp.province, CoresunApp.city, CoresunApp.area, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {

                LogUtil.i("商品分类列表" + result);
                if (result == null || result.equals("")) {
                    return;
                }

                //显示分类菜单
                showActivityClass(result);

                //默认获取第一项分类的数据
                ActivityClassEntity enty = (ActivityClassEntity) classList.get(0);
                typeId = enty.getId();

                getActivityListByClass(typeId, allActList.size());
                //将菜单定位到第一项
                lvMenu.setSelection(0);
                menuAdapter.notifyDataSetChanged();
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }

    //显示菜单
    private void showActivityClass(String result) throws JSONException {
        LogUtil.i("奖品分类菜单数据:" + result);
        if (result == null || result.equals("")) {
            return;
        }

        JSONObject json = new JSONObject(result);
        if ("200".equals(json.getString("result"))) {
            JSONArray ary = json.getJSONArray("class_data");
            List<Object> list = JsonPaser.parserAry(ary.toString(), ActivityClassEntity.class);

            //将分类数据添加到成员集合中
            addToMenuList(list);
            currentItem = 0;

            menuAdapter = new MenuAdapter(this, list);
            lvMenu.setAdapter(menuAdapter);
        } else {//202
            ToastUtil.makeToast(json.getString("msg"));
        }
    }

    //将数据添加到分类菜单集合
    private void addToMenuList(List<Object> list) {
        allActList.clear();
        classList.clear();
        classList.addAll(list);
    }

    //初始化
    private void setViews() {
        lvMenu = (CustomMenuListView) findViewById(R.id.listMenu);
        lvContent = (CustomMenuListView) findViewById(R.id.listContent);
        lvContent.setOnScrollListener(onScrollListener);

        awardsTypeContentAdapter = new GoodsAdapter(this);
        awardsTypeContentAdapter.setList(allActList);
        lvContent.setAdapter(awardsTypeContentAdapter);

        etSearch = (EditText) findViewById(R.id.et_awards_type_search);
        llBack = (LinearLayout) findViewById(R.id.ll_awards_type_back);
    }

    //根据关键字搜索活动分类
    private void searchClassByKeyname() {
        String keyname = etSearch.getText().toString();
        if (TextUtils.isEmpty(keyname)) {//输入为空
            ToastUtil.makeToast("请输入关键字");
        } else {
            AwardsTypeBiz.getTypeContentByKeyname(this, keyname, CoresunApp.province, CoresunApp.city, CoresunApp.area, new HttpRequestListener() {
                @Override
                public void onHttpRequestFinish(String result) throws JSONException {
                    LogUtil.i("搜索返回的数据" + result);
                    if (result == null || result.equals("")) {
                        return;
                    }
                    showSearchResponse(result);
                }

                @Override
                public void onHttpRequestError(String error) {

                }
            });
        }
    }

    //显示根据关键搜索获取的数据
    private void showSearchResponse(String result) {
        try {
            JSONObject obj = new JSONObject(result);
            String res = obj.getString("result");
            if (res.equals("200")) {
                JSONArray act_ary = obj.getJSONArray("class_data");
                List<Object> actList = JsonPaser.parserAry(act_ary.toString(), GoodsBean.class);
                //显示活动
                awardsTypeContentAdapter.updateData(actList);
                //将活动添加到成员
                addDataToList(actList);

//                //显示分类
//                JSONArray class_ary = obj.getJSONArray("class_data");
//                List<Object> cls_list = JsonPaser.parserAry(class_ary.toString(), GoodsModle.class);
//                currentItem=0;
//
//                //添加到分类菜单集合
//                addToMenuList(cls_list);

                //通知adapter更新数据
                menuAdapter.updateData(classList);
            } else if (res.equals("201")) {//没有数据
                ToastUtil.makeToast("无相关数据");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //设置监听
    private void setListeners() {
        //返回
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //当清除输入框文字时,调用搜索
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String keyname = etSearch.getText().toString();
                if (TextUtils.isEmpty(keyname)) {
                    //获取分类
                    getActivityClass();
                    isSearch = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //搜索
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //isSearch设置为true,表示为活动搜索
                isSearch = true;

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 先隐藏键盘
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
                    //按照关键字搜索分类
                    searchClassByKeyname();
                    return true;
                }
                return false;
            }
        });

        //设置菜单栏item点击后的状态,以及点击item后显示的内容
        lvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentItem = position;
                menuAdapter.notifyDataSetChanged();
                lvMenu.smoothScrollToPositionFromTop(position, 0);//当前item点击后滑动到第一位置
                //如果是搜索
                if (isSearch) {
                    awardsTypeContentAdapter.updateData(actList);
                } else {
                    actList.clear();
                    allActList.clear();
                    typeId = ((ActivityClassEntity) classList.get(position)).getId();//奖品分类的id
                    getActivityListByClass(typeId, allActList.size());
                }
            }
        });

        //点击列表中的活动跳转到相应的活动抽奖页面
        lvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GoodsBean enty = (GoodsBean) actList.get(position);
                Intent intent = null;
//                if(enty.getType().equals("1")){
//                    //普通抽奖
//                    intent=new Intent(AwardsTypeActivity.this,ActDetailActivity.class);
//                }else if(enty.getType().equals("4")){
//                    //抽奖池抽奖
//                    intent=new Intent(AwardsTypeActivity.this, AwardPoolActivity.class);
//                }
//                intent = new Intent(AwardsTypeActivity.this, CrowdDetailAct.class);
//                intent.putExtra("enty", enty);
//                intent.putExtra("flag", "shop");
//                startActivity(intent);
            }
        });
    }

    AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (isLast && scrollState == SCROLL_STATE_IDLE) {
                getActivityListByClass(typeId, allActList.size());
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (totalItemCount == visibleItemCount + firstVisibleItem) {
                isLast = true;
            }
        }
    };

}
