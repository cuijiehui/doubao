package com.xinspace.csevent.login.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.xinspace.csevent.R;
import com.xinspace.csevent.app.CoresunApp;
import com.xinspace.csevent.app.view.CircleImageView;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.biz.GetOssKeyBiz;
import com.xinspace.csevent.data.biz.SupplyUserInfoBiz;
import com.xinspace.csevent.data.entity.OssKeyEntity;
import com.xinspace.csevent.data.entity.ProfileDataEntity;
import com.xinspace.csevent.data.sharedprefs.AppSharedPrefs;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.myinterface.OssPutObjectResponseListener;
import com.xinspace.csevent.ui.widget.OpenDoorService;
import com.xinspace.csevent.util.parser.JsonPaser;
import com.xinspace.csevent.shop.utils.UpLoadImg;
import com.xinspace.csevent.sweepstake.activity.ChangeAddressAct;
import com.xinspace.csevent.util.Const;
import com.xinspace.csevent.util.DialogUtil;
import com.xinspace.csevent.util.ImagerLoaderUtil;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.OssPutObejectUtil;
import com.xinspace.csevent.util.StatusBarUtils;
import com.xinspace.csevent.util.ToastUtil;
import com.xinspace.csevent.ui.activity.BaseActivity;
import com.xinspace.csevent.ui.activity.EditPwdActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.linphone.LinphoneService;

import java.io.File;


/**
 * Created by Android on 2016/9/20.
 */
public class MyMessageAct extends BaseActivity{


    private LinearLayout ll_register_back;
    private RelativeLayout rel_user_img , rel_user_nickname , rel_user_phone , rel_user_address;
    private RelativeLayout rel_user_real_name;

    private PopupWindow popupWindow;
    private String filePath;
    public static final int REQUEST_CODE_LOAD_IMAGE = 210;//启动系统图库请求码
    public static final int REQUEST_CODE_LOAD_CAMERA = 230;//调用系统相机
    public static final int REQUEST_CODE_CROP_IMAGE = 220;//裁剪图片
    public static final int HANDLER_SHOW_PICTURE = 101;//显示图片
    private String jpegPath = Const.APK_STEORY_PATH + "profile.jpeg";
    private CircleImageView img_userhead;
    private SDPreference preference;
    private String userImgPath;

    private TextView tv_user_phone;
    private TextView tv_user_nickname;
    private TextView tv_user_real_name;

    private Intent intent;
    private ProfileDataEntity enty;

    private RelativeLayout rel_edit_pwd;
    private String openid;
    private TextView tv_log_out;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_SHOW_PICTURE:

                    supplyInfo((String) msg.obj);

                    showImage();
                    break;
            }
        }
    };
    private SwitchCompat switchCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(this , R.color.app_bottom_color);
        setContentView(R.layout.act_my_message);
        preference = SDPreference.getInstance();
        openid = preference.getContent("openid");
        intent = getIntent();
        if (intent != null){
            enty = (ProfileDataEntity) intent.getSerializableExtra("data");
        }
        initView();
    }

    private void initView() {
        ll_register_back = (LinearLayout) findViewById(R.id.ll_register_back);
        rel_user_img = (RelativeLayout) findViewById(R.id.rel_user_img);
        rel_user_nickname = (RelativeLayout) findViewById(R.id.rel_user_nickname);
        rel_user_real_name = (RelativeLayout) findViewById(R.id.rel_user_real_name);
        rel_user_phone = (RelativeLayout) findViewById(R.id.rel_user_phone);
        rel_user_address = (RelativeLayout) findViewById(R.id.rel_user_address);

        rel_edit_pwd = (RelativeLayout) findViewById(R.id.rel_edit_pwd);
        rel_edit_pwd.setOnClickListener(clickListener);

        ll_register_back.setOnClickListener(clickListener);
        rel_user_img.setOnClickListener(clickListener);
        rel_user_nickname.setOnClickListener(clickListener);
        rel_user_phone.setOnClickListener(clickListener);
        rel_user_address.setOnClickListener(clickListener);
        rel_user_real_name.setOnClickListener(clickListener);

        img_userhead = (CircleImageView) findViewById(R.id.img_userhead);
        userImgPath = enty.getAvatar();
        if (userImgPath != null && !userImgPath.equals("")){
            ImagerLoaderUtil.displayImage(userImgPath, img_userhead);
        }

        tv_user_real_name = (TextView) findViewById(R.id.tv_user_real_name);
        tv_user_real_name.setText(enty.getRealname());

        tv_user_phone = (TextView) findViewById(R.id.tv_user_phone);
        tv_user_nickname = (TextView) findViewById(R.id.tv_user_nickname);
        if (enty != null){
            tv_user_phone.setText(enty.getMobile());
            tv_user_nickname.setText(enty.getNickname());
        }

        tv_log_out = (TextView) findViewById(R.id.tv_log_out);
        tv_log_out.setOnClickListener(clickListener);

        switchCompat = (SwitchCompat)findViewById(R.id.switch_compat);
        //判断用户当前是否已经开启设置社区为默认主页
        boolean isSetDoorDefault = new AppSharedPrefs(this).getDefault();
        if (isSetDoorDefault){
            switchCompat.setChecked(true);
        }
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    new AppSharedPrefs(MyMessageAct.this).setDefault(true);
                }
                else{
                    new AppSharedPrefs(MyMessageAct.this).setDefault(false);
                }
            }
        });
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_register_back:
                    finish();
                    break;
                case R.id.rel_user_img: //头像

                    showPopupWindow();

                    break;
                case R.id.rel_edit_pwd: //修改密码

                    //startActivity(new Intent(MyMessageAct.this , FindMyPasswordActivity.class));

                    startActivity(new Intent(MyMessageAct.this , EditPwdActivity.class));

                    break;
                case R.id.rel_user_nickname: // 昵称
                    Intent nickNameIntent = new Intent(MyMessageAct.this , SetUserMsgAct.class);
                    nickNameIntent.putExtra("flag" , "nickName");
                    nickNameIntent.putExtra("name" , enty.getNickname());
                    nickNameIntent.putExtra("phone" , enty.getMobile());
                    nickNameIntent.putExtra("real" , enty.getRealname());
                    //startActivity(nickNameIntent);
                    startActivityForResult(nickNameIntent , 1000);
                    break;
                case R.id.rel_user_real_name:
                    Intent realNameIntent = new Intent(MyMessageAct.this , SetUserMsgAct.class);
                    realNameIntent.putExtra("flag" , "realName");
                    realNameIntent.putExtra("name" , enty.getNickname());
                    realNameIntent.putExtra("phone" , enty.getMobile());
                    realNameIntent.putExtra("real" , enty.getRealname());
                    //startActivity(nickNameIntent);
                    startActivityForResult(realNameIntent , 1000);
                    break;

                case R.id.rel_user_address: // 地址管理
                    //Intent intent = new Intent(MyMessageAct.this, AddressManagerActivity.class);
                    Intent intent = new Intent(MyMessageAct.this, ChangeAddressAct.class);
                    intent.putExtra("flag" , "home");
                    startActivity(intent);
                    break;
                case R.id.tv_log_out:
                    Logout();
                    break;
            }
        }
    };


    //上传成功后显示图片
    private void showImage() {
        ToastUtil.makeToast("上传成功");
        img_userhead.setImageBitmap(BitmapFactory.decodeFile(jpegPath));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        //相机拍照
        if (requestCode == REQUEST_CODE_LOAD_CAMERA && resultCode == Activity.RESULT_OK) {
            cropPictureForCamera();
        }

        //裁剪图片
        if (requestCode == REQUEST_CODE_CROP_IMAGE && resultCode == Activity.RESULT_OK) {
            Log.i("www" , "-----------裁剪成功----------" + jpegPath);
            //ImagerLoaderUtil.displayImage(userImgPath, img_userhead);
            //getOssKey();

            File file = new File(jpegPath);
            String loadUrl = "https://wx.szshide.shop/app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=util.apiupload";
            try {
                UpLoadImg.upLoadImg( loadUrl , file.getPath() , handler , "jpeg");
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        //点击相册选取图片
        if (requestCode == REQUEST_CODE_LOAD_IMAGE && resultCode == Activity.RESULT_OK) {
            Log.i("www" , "-------" + data.getData());
            cropPicture(data);
        }

        // 修改昵称
        if (requestCode == 1000 && resultCode == 1001) {
            LogUtil.i("www" + "---修改昵称----" + data.getData());
            tv_user_nickname.setText(data.getStringExtra("result"));
        }

        // 修改手机号
        if (requestCode == 1000 && resultCode == 1002) {
            LogUtil.i("www" + "----修改手机号---" + data.getData());
            tv_user_phone.setText(data.getStringExtra("result"));
        }

        // 修改真实姓名
        if (requestCode == 1000 && resultCode == 1003) {
            LogUtil.i("www" + "----修改手机号---" + data.getData());
            tv_user_real_name.setText(data.getStringExtra("result"));
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    //获取oss授权码
    private void getOssKey() {
        GetOssKeyBiz.getKey(new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                handleOssResult(result);
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }

    //相机的裁剪图片
    private void cropPictureForCamera() {
        Uri uriImageData = Uri.fromFile(new File(filePath));
        Intent intent = new Intent();
        intent.setAction("com.android.camera.action.CROP");
        intent.setDataAndType(uriImageData, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 400);// 输出图片大小
        intent.putExtra("outputY", 400);
        intent.putExtra("output", Uri.fromFile(new File(jpegPath)));
        intent.putExtra("outputFormat", "jpeg");
        intent.putExtra("return-data", false);
        startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
    }

    //裁剪图片
    private void cropPicture(Intent data) {
        Log.i("www" , "进来裁剪相册选择的图片了");
        Uri mUri = data.getData();
        Intent intent = new Intent();
        intent.setAction("com.android.camera.action.CROP");
        intent.setDataAndType(mUri, "image/*");// mUri是已经选择的图片Uri
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 400);// 输出图片大小
        intent.putExtra("outputY", 400);
        intent.putExtra("output", Uri.fromFile(new File(jpegPath)));
        intent.putExtra("outputFormat", "jpeg");
        intent.putExtra("return-data", false);
        startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
    }

    //处理oss返回结果
    private void handleOssResult(String result) {
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
        OSSClient oss = new OSSClient(this, Const.END_POINT, credentialProvider);

        String testObject = "user_avatar/" + CoresunApp.USER_ID+"/" + CoresunApp.USER_ID + ".png";

        LogUtil.i("www" + "testObject    " + testObject );
        LogUtil.i("www" + "jpegPath      " + jpegPath );

        OssPutObejectUtil util = new OssPutObejectUtil(oss, Const.BUCKET, testObject, jpegPath);
        //文件上传成功后回调
        util.setOnOssPutObjectResponseListener(new OssPutObjectResponseListener() {
            @Override
            public void onPutObjectSuccess() {
                handler.sendEmptyMessage(HANDLER_SHOW_PICTURE);
                LogUtil.i("上传成功");
            }
        });
        util.asyncPutObjectFromLocalFile();
    }

    private void showPopupWindow() {
        View popView = LayoutInflater.from(this).inflate(R.layout.item_popupwindow_of_choose_camera, null);
        popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAtLocation(this.findViewById(R.id.rel_parent), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        //背景透明
        //setBackgroundAlpha(0.5f);
        //选择相片的弹窗关闭时间
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1.0f);
            }
        });

        //相册监听
        TextView tv_album = (TextView) popView.findViewById(R.id.tv_popupwindow_album);
        tv_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPhoto();
                popupWindow.dismiss();
            }
        });

        //相机监听
        TextView tv_camera = (TextView) popView.findViewById(R.id.tv_popupwindow_camera);
        tv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
                popupWindow.dismiss();
            }
        });

        //取消监听
        TextView tv_cancel = (TextView) popView.findViewById(R.id.tv_popupwindow_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    //打开相机
    private void openCamera() {
        filePath = Const.APK_STEORY_PATH + "temp.jpg";
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(filePath)));
        startActivityForResult(intent, REQUEST_CODE_LOAD_CAMERA);
    }

    //更改头像
    private void openPhoto() {

        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQUEST_CODE_LOAD_IMAGE);
    }
    //设置背景透明度
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        this.getWindow().setAttributes(lp);
    }


    /**
     * 向服务器提交信息
     *
     */
    private void supplyInfo(String url) {
        SupplyUserInfoBiz.supplyInfo3( openid , "" , "",  url ,new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                LogUtil.i("上传头像的url" + result);
                if (result == null || result.equals("")){
                    return;
                }
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("code").equals("200")) {

                }else{
                    handler.obtainMessage(200).sendToTarget();
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }

    private void Logout() {
        //弹出退出提示对话框
        DialogUtil.showTipsDailog(this, "退出提示", "您确定要退出登录吗?", postitiveListener, negativeListener);
    }

    //确定
    private DialogInterface.OnClickListener postitiveListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (preference.getContent("userId").equals("0")) {
                ToastUtil.makeToast("未登录");
                return;
            }
            ToastUtil.makeToast("已退出");

            CoresunApp.isLogin = false;
            preference.putContent("userId" ,"0");
            preference.putContent("tel" , "0");
            preference.putContent("nickName" , "0");
            preference.putContent("openid" , "0");
            preference.putContent("GetNeiborId" , "0");
            preference.putContent("mobile" , "");

            preference.putContent("cUid" , "0");
            preference.putContent("cToken" , "0");
            preference.putContent("com_id" , "0");
            preference.putContent("coresun_token", "0");
            preference.putContent("coresun_dealtime", "0");
            stopService(new Intent(Intent.ACTION_MAIN).setClass(MyMessageAct.this, LinphoneService.class));
            stopService(new Intent(MyMessageAct.this, OpenDoorService.class));
            finish();
        }
    };

    //取消
    private DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            DialogUtil.close();
        }
    };

}
