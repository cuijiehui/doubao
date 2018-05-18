package com.xinspace.csevent.baskorder.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.xinspace.csevent.R;
import com.xinspace.csevent.app.CoresunApp;
import com.xinspace.csevent.baskorder.adapter.AddPicAdapter2;
import com.xinspace.csevent.baskorder.adapter.DividerGridItemDecoration;
import com.xinspace.csevent.data.biz.GetDataBiz;
import com.xinspace.csevent.data.biz.GetOssKeyBiz;
import com.xinspace.csevent.data.entity.AwardsRecordEntity;
import com.xinspace.csevent.data.entity.OssKeyEntity;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.myinterface.OssPutObjectResponseListener;
import com.xinspace.csevent.util.parser.JsonPaser;
import com.xinspace.csevent.util.Const;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.OssPutObejectUtil;
import com.xinspace.csevent.util.ToastUtil;
import com.xinspace.csevent.ui.activity.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android on 2016/10/8.
 * <p/>
 * 发表晒单
 */
public class AddBaskOrderAct extends BaseActivity {

    private LinearLayout ll_about_us_back;
    private TextView tv_conference;
    private EditText edit_order_title, edit_order_content;

    private TextView tv_awards_record_name;
    private TextView tv_awards_noactivity;
    private TextView tv_awards_record_attend_num;
    private TextView tv_awards_record_price;
    private TextView tv_awards_record_time;

    private Intent intent;
    private AwardsRecordEntity enty;
    private AddPicAdapter2 adapter;

    private GridView gv_send_img;
    private List<String> chImgs;
    private int size;

    private RecyclerView img_recylview;
    private ImageView iv_add_img;

    private LayoutInflater inflater;

    private String title = "";
    private String content = "";

    private String aid;
    private String cid;

    private int upLoadSuc = 0;

    private String jpegPath = Const.APK_STEORY_PATH + "profile.jpeg";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1: // 文字上传成功后上传图片
                    String id = (String) msg.obj;
                    if (size <= 3) {   // 图片小于三张采样
                        for (int i = 0; i < size; i++) {
                            getOssKey(i , id  , "small");
                        }
                    } else if (size > 3) { // 图片大于三张 取前三张 默认前三张小图

                        for (int i = 0; i < 3; i++) {
                            getOssKey(i , id , "small");
                        }

                        for (int i = 3; i < size; i++) {
                            getOssKey(i , id , "big");
                        }

                    }
                    break;
                case 2:
                    ToastUtil.makeToast("晒单发表失败");
                    break;
                case 11: // 图片上传成功
                    upLoadSuc ++;
                    LogUtil.i("www" + upLoadSuc);
                    if (upLoadSuc == chImgs.size()){
                        ToastUtil.makeToast("上传成功");
                    }
                    AddBaskOrderAct.this.finish();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_addbaskorder);
        //EventBus.getDefault().register(this);
        inflater = LayoutInflater.from(this);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // EventBus.getDefault().unregister(this);
        // 内存这个问题怎么办
        handler.removeCallbacksAndMessages(null);
        //itemClickListener = null;
        clickListener = null;
        intent = null;
        chImgs = null;
        enty = null;
    }


    private void initView() {
        intent = getIntent();
        if (intent != null) {
            enty = (AwardsRecordEntity) intent.getSerializableExtra("data");
        }
        chImgs = new ArrayList<String>();

        aid = enty.getAid();
        cid = enty.getPid();

        ll_about_us_back = (LinearLayout) findViewById(R.id.ll_about_us_back);
        tv_conference = (TextView) findViewById(R.id.tv_conference);
        ll_about_us_back.setOnClickListener(clickListener);
        tv_conference.setOnClickListener(clickListener);

        edit_order_title = (EditText) findViewById(R.id.edit_order_title);
        edit_order_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                title = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        edit_order_content = (EditText) findViewById(R.id.edit_order_content);
        edit_order_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                content = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tv_awards_record_name = (TextView) findViewById(R.id.tv_awards_record_name);
        tv_awards_noactivity = (TextView) findViewById(R.id.tv_awards_noactivity);
        tv_awards_record_attend_num = (TextView) findViewById(R.id.tv_awards_record_attend_num);
        tv_awards_record_time = (TextView) findViewById(R.id.tv_awards_record_time);
        tv_awards_record_price = (TextView) findViewById(R.id.tv_awards_record_price);

        iv_add_img = (ImageView) findViewById(R.id.iv_add_img);
        iv_add_img.setOnClickListener(clickListener);

//        gv_send_img = (GridView) findViewById(R.id.gv_send_img);
//        gv_send_img.setOnItemClickListener(itemClickListener);
//        adapter = new AddPicAdapter(AddBaskOrderAct.this , 9);
//        gv_send_img.setAdapter(adapter);

        img_recylview = (RecyclerView) findViewById(R.id.img_recylview);
        img_recylview.setLayoutManager(new StaggeredGridLayoutManager(1,
                StaggeredGridLayoutManager.HORIZONTAL));
        img_recylview.addItemDecoration(new DividerGridItemDecoration(this.getApplicationContext()));
        img_recylview.setItemAnimator(new DefaultItemAnimator());
        if (adapter == null) {
            adapter = new AddPicAdapter2(inflater);
            adapter.setImgs(chImgs);
        }
        img_recylview.setAdapter(adapter);

        tv_awards_record_name.setText("获得商品 ： " + enty.getPname());
        tv_awards_noactivity.setText("参与期号 ： " + enty.getNoactivity());
        tv_awards_record_attend_num.setText(enty.getMatch());
        tv_awards_record_time.setText(enty.getStartdate());
        tv_awards_record_price.setText("¥" + enty.getPrice());

    }

//    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            if (position == chImgs.size()){
//                Intent intent = new Intent(AddBaskOrderAct.this, AlbumAct.class);
//                int hasImageSize = chImgs.size();
//                intent.putExtra("size" , hasImageSize);
//                startActivityForResult(intent , 1000);
//            }else{
//
//            }
//        }
//    };


    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_about_us_back:
                    AddBaskOrderAct.this.finish();
                    break;
                case R.id.iv_add_img:
                    if (size < 9) {
                        Intent intent = new Intent(AddBaskOrderAct.this, AlbumAct.class);
                        int hasImageSize = chImgs.size();
                        intent.putExtra("size", hasImageSize);
                        intent.putExtra("flag", "1");
                        startActivityForResult(intent, 1000);
                    } else if (size >= 9) {
                        ToastUtil.makeToast("最多能添加九张图片");
                    }
                    break;
                case R.id.tv_conference:  // 确认晒单
                    if (title.length() < 6) {
                        ToastUtil.makeToast("您输入的晒单主题字数过少......");
                        return;
                    }

                    if (content.length() < 30) {
                        ToastUtil.makeToast("您输入的获奖感言字数过少......");
                        return;
                    }

                    if (size == 0) {
                        ToastUtil.makeToast("您未添加图片");
                    } else {
                        //购买时1 即时2 众筹0
                        addTextData(aid, content, title, "2", cid);
                    }
                    break;
            }
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            switch (resultCode) {
                case 1001:
                    chImgs.addAll((List<String>) data.getSerializableExtra("imgList"));
                    size = chImgs.size();
                    LogUtil.i("www" + "-------------22-----------" + size);
                    if (size > 0) {
                        adapter.setImgs(chImgs);
                        adapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    }

    /**
     * 添加文字
     * @param aid
     * @param orderContent
     * @param orderTilte
     * @param type
     * @param cid
     */
    private void addTextData(String aid, String orderContent, String orderTilte, String type, String cid) {
        GetDataBiz.addOrderList(aid, orderContent, orderTilte, type, cid, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("result").equals("200")) {
                    if (jsonObject.getString("msg").equals("success")) {
                        handler.obtainMessage(1, jsonObject.getString("id")).sendToTarget();
                    }
                } else {
                    handler.obtainMessage(2).sendToTarget();
                }
            }

            @Override
            public void onHttpRequestError(String error) {
                handler.obtainMessage(2).sendToTarget();
            }
        });
    }

    //获取oss授权码
    private void getOssKey(final int num , final String id , final String type) {

        GetOssKeyBiz.getKey(new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                if (type.equals("small")){
                    handleSmallResult(result , num , id);
                }else if (type.equals("big")){
                    handleBigResult(result , num - 3 , id);
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }


    private void handleSmallResult(String result , int num , String id) {
        Log.i("www" , "处理oss返回结果" + result);
        OssKeyEntity enty = (OssKeyEntity) JsonPaser.parserObj(result, OssKeyEntity.class);
        String accessKeyId = enty.getAccessKeyId();
        String accessKeySecret = enty.getAccessKeySecret();
        String token = enty.getSecurityToken();
        String expiration = enty.getExpiration();
        LogUtil.i("accessKeyId=" + accessKeyId);
        LogUtil.i("accessKeySecret=" + accessKeySecret);
        LogUtil.i("token=" + token);
        LogUtil.i("expiration=" + expiration);

        //开始上传
        //OSSCredentialProvider credentialProvider = new STSGetter(AppConfig.OSS_KEY_URL);

        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider("Oz2OYHFBbZBMTWNO", "aSHrFL6Ab9R6vin5EU8JRFwfFIaWBA");
        OSSClient oss = new OSSClient(this, Const.END_POINT_Test, credentialProvider);

        //小图路径
        //http://pictest.coresun.net/showorder/120/UperID:266,ActionID:224,small0.png
        String testObject = "showorder/" + id + "/UperID:" + CoresunApp.USER_ID + ",ActionID:"+ aid +",small" + num + ".png";

        LogUtil.i("www" + "testObject    " + testObject );
        LogUtil.i("www" + "jpegPath      " + jpegPath );


        OssPutObejectUtil util = new OssPutObejectUtil(oss, Const.BUCKETTEST, testObject, chImgs.get(num));
        //文件上传成功后回调
        util.setOnOssPutObjectResponseListener(new OssPutObjectResponseListener() {
            @Override
            public void onPutObjectSuccess() {
                LogUtil.i("上传小图成功");
                handler.obtainMessage(11).sendToTarget();
            }
        });
        util.asyncPutObjectFromLocalFile();
    }

    /**
     * 上传大图
     * @param result
     * @param num
     * @param id
     */
    private void handleBigResult(String result , int num , String id) {
        Log.i("www" , "处理oss返回结果" + result);
        OssKeyEntity enty = (OssKeyEntity) JsonPaser.parserObj(result, OssKeyEntity.class);
        String accessKeyId = enty.getAccessKeyId();
        String accessKeySecret = enty.getAccessKeySecret();
        String token = enty.getSecurityToken();
        String expiration = enty.getExpiration();
        LogUtil.i("accessKeyId=" + accessKeyId);
        LogUtil.i("accessKeySecret=" + accessKeySecret);
        LogUtil.i("token=" + token);
        LogUtil.i("expiration=" + expiration);

        //开始上传
        //OSSCredentialProvider credentialProvider = new STSGetter(AppConfig.OSS_KEY_URL);

        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider("Oz2OYHFBbZBMTWNO", "aSHrFL6Ab9R6vin5EU8JRFwfFIaWBA");
        OSSClient oss = new OSSClient(this, Const.END_POINT_Test, credentialProvider);


        BitmapFactory.Options options = new BitmapFactory.Options();
        //该属性设置为true只会加载图片的边框进来，并不会加载图片具体的像素点
        options.inJustDecodeBounds = true;
        //第一次加载图片，这时只会加载图片的边框进来，并不会加载图片中的像素点
        BitmapFactory.decodeFile(chImgs.get(num), options);
        //获得原图的宽和高
        int outWidth = options.outWidth;
        int outHeight = options.outHeight;

        // 大图路径
        //http://pictest.coresun.net/showorder/120/big/UperID:266,ActionID:224,big0,{2208.000000,1473.000000}.png
        String testObject = "showorder/" + id + "/big/UperID:" + CoresunApp.USER_ID
                + ",ActionID:"+ aid +",big" + num + ",{" + outWidth + "," + outHeight + "}" + ".png";

        LogUtil.i("www" + "testObject    " + testObject );
        LogUtil.i("www" + "jpegPath      " + jpegPath );

        OssPutObejectUtil util = new OssPutObejectUtil(oss, Const.BUCKETTEST, testObject, chImgs.get(num));
        //文件上传成功后回调
        util.setOnOssPutObjectResponseListener(new OssPutObjectResponseListener() {
            @Override
            public void onPutObjectSuccess() {
                LogUtil.i("上传小图成功");
                handler.obtainMessage(11).sendToTarget();
            }
        });
        util.asyncPutObjectFromLocalFile();
    }


}
