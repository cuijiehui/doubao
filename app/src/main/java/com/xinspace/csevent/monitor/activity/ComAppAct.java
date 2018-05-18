package com.xinspace.csevent.monitor.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListAdapter;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListItem;
import com.google.gson.Gson;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.xinspace.csevent.R;
import com.xinspace.csevent.app.CoresunApp;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.biz.CommunityUnit;
import com.xinspace.csevent.data.biz.GetDataBiz;
import com.xinspace.csevent.monitor.adapter.MySection;
import com.xinspace.csevent.monitor.adapter.SectionAdapter;
import com.xinspace.csevent.monitor.bean.AppComBean;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.ui.activity.BaseActivity;
import com.xinspace.csevent.ui.fragment.CommunityUnitFragment;
import com.xinspace.csevent.util.ImageUtil;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.SharedPreferencesUtil1;
import com.xinspace.csevent.util.StatusBarUtils;
import com.xinspace.csevent.util.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sdk_sample.sdk.bean.ComBean;

import static com.xinspace.csevent.app.AppConfig.COMMUNITY_AREA;

/**
 * 小区申请
 *
 * Created by Android on 2017/6/23.
 */

public class ComAppAct extends BaseActivity{

    private RecyclerView ex_com_app;
    private SDPreference preference;
    private String cUid;
    private String cToken;
    private LinearLayout ll_list_back;
    private TextView tv_apply;
    private Gson gson = new Gson();
    private List<ComBean> comList = new ArrayList<>();
    private SectionAdapter adapter;
    ImageView imgQTcodeAdd;
    ImageView imgUserAdd;
    /**
     * 扫描跳转Activity RequestCode
     */
    private static final int REQUEST_CODE = 101;
    private static final int REQUEST_IMAGE = 102;
    private static final int REQUEST_CAMERA_PERM = 103;
    private ImageView btn_unit_add;
    private CommunityUnitFragment unitFragment;
    private FragmentTransaction ft;
    private String area;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_comapp);
        StatusBarUtils.setWindowStatusBarColor(this , R.color.app_bottom_color);
        area = SharedPreferencesUtil1.getString(this, COMMUNITY_AREA, "");
        preference = SDPreference.getInstance();
        cUid = preference.getContent("cUid");
        cToken = preference.getContent("cToken");
        ToastUtils.init(this);

        initView();
        initData();
    }

    private void initData() {

        String area = SharedPreferencesUtil1.getString(this, COMMUNITY_AREA, "");

        GetDataBiz.getAppCom(area, cUid, cToken, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                LogUtil.e("ComAppActivity当前线程：" + Thread.currentThread().getName());
                if (result == null || result.equals("")){
                    return;
                }
                JSONObject jsonObject = new JSONObject(result);
                String code = jsonObject.getString("code");
                if (code.equals("200")){
                    List<AppComBean> allBeanList = new ArrayList<>();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    List<MySection> sections = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String json = jsonArray.getJSONObject(i).toString();
                        AppComBean appComBean = gson.fromJson(json, AppComBean.class);//获取单个对象
                        sections.add(new MySection(appComBean));
                    }

                    adapter = new SectionAdapter(R.layout.com_app_content, sections);
                    adapter.openLoadAnimation();
                    ex_com_app.setAdapter(adapter);

                }
                else{
                    ToastUtils.showToast("加载列表失败");
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }

    private void initView() {
        ft = getSupportFragmentManager().beginTransaction();

        ll_list_back = (LinearLayout) findViewById(R.id.ll_list_back);
        ll_list_back.setOnClickListener(onClickListener);

        btn_unit_add = (ImageView)findViewById(R.id.img_setting);
        btn_unit_add.setOnClickListener(onClickListener);

        ex_com_app = (RecyclerView) findViewById(R.id.ex_com_app);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ex_com_app.setLayoutManager(layoutManager);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_list_back:
                    ComAppAct.this.finish();
                    break;

                case R.id.img_setting:
                    popVerifyDialog();
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    if (!result.contains("|")){
                        ToastUtils.showToast("无法识别此二维码");
                        return;
                    }
                    String[] ids = result.split("\\|");
                    String communityId = ids[0];
                    String unitId = ids[1];
                    preference.putContent("community_id", communityId);
                    preference.putContent("unit_id", unitId);
                    GetDataBiz.GET_COMMUNITY_LIST(area, cUid, cToken, unitId, new HttpRequestListener() {
                        @Override
                        public void onHttpRequestFinish(String result) throws JSONException {
                            CommunityUnit bean = gson.fromJson(result, CommunityUnit.class);
                            if (bean.getCode() == 200){

                                if (unitFragment==null){
                                    unitFragment = CommunityUnitFragment.newInstance();
                                }
                                Bundle bundle = new Bundle();
                                bundle.putParcelable("data", bean);
                                bundle.putString("flag", "2");
                                unitFragment.setArguments(bundle);
                                ft.add(R.id.com_app_comtainer, unitFragment, "commuintyunit");
                                ft.addToBackStack(null);
                                ft.commit();
                            }
                            else{
                                ToastUtils.showToast("获取列表失败");
                            }
                        }

                        @Override
                        public void onHttpRequestError(String error) {
                            LogUtil.e(error);
                        }
                    });
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    ToastUtils.showToast("解析二维码失败");
                }
            }
        }

        /**
         * 选择系统图片并解析
         */
        else if (requestCode == REQUEST_IMAGE) {
            if (data != null) {
                Uri uri = data.getData();
                try {
                    CodeUtils.analyzeBitmap(ImageUtil.getImageAbsolutePath(this, uri), new CodeUtils.AnalyzeCallback() {
                        @Override
                        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                            ToastUtils.showToast("解析结果:" + result);
                        }

                        @Override
                        public void onAnalyzeFailed() {
                            ToastUtils.showToast("解析二维码失败");
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        else if (requestCode == REQUEST_CAMERA_PERM) {
            ToastUtils.showToast("从设置页面返回");
        }
    }

    /**
     * 弹窗让用户添加社区
     */
    private void popVerifyDialog() {

        MaterialSimpleListAdapter listAdapter = new MaterialSimpleListAdapter(new MaterialSimpleListAdapter.Callback() {
            @Override
            public void onMaterialListItemSelected(MaterialDialog dialog, int index, MaterialSimpleListItem item) {
                switch (index){
                    case 0:
                        //正常的二维码扫描
                        Intent scanIntent = new Intent(CoresunApp.context, CaptureActivity.class);
                        startActivityForResult(scanIntent, REQUEST_CODE);
                        dialog.dismiss();
                        break;
                    case 1:
                        Intent intent = new Intent(ComAppAct.this , SubmitDataAct.class);
                        startActivity(intent);
                        dialog.dismiss();
                        break;
                }
            }
        });

        listAdapter.add(
                new MaterialSimpleListItem.Builder(ComAppAct.this)
                        .content("扫一扫")
                        .icon(R.drawable.qtcode)
                        .backgroundColor(Color.WHITE)
                        .build()
        );

        listAdapter.add(
                new MaterialSimpleListItem.Builder(ComAppAct.this)
                        .content("添加社区")
                        .icon(R.drawable.icon_userhead_no_login)
                        .backgroundColor(Color.WHITE)
                        .build()
        );

        new MaterialDialog.Builder(ComAppAct.this)
                .title(R.string.unit_choose_notice)
                .items(R.array.unit_add_items)
                .adapter(listAdapter, null)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}


