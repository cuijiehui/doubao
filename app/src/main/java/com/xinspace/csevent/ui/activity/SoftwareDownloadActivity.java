package com.xinspace.csevent.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.app.CoresunApp;
import com.xinspace.csevent.data.biz.DownloadManger;
import com.xinspace.csevent.data.biz.GetSoftwareInfoBiz;
import com.xinspace.csevent.data.entity.SoftwareDetailEntity;
import com.xinspace.csevent.data.entity.SoftwareEntity;
import com.xinspace.csevent.login.activity.LoginActivity;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.myinterface.Observer;
import com.xinspace.csevent.util.parser.SoftwareDetailParser;
import com.xinspace.csevent.util.Const;
import com.xinspace.csevent.util.DialogUtil;
import com.xinspace.csevent.util.ImagerLoaderUtil;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

/***
 * 推荐软件的下载页面
 */
public class SoftwareDownloadActivity extends BaseActivity implements Observer{
    private LinearLayout ll_back;
    private LinearLayout ll_container;//轮播图容器
    private SoftwareEntity enty;

    private ImageView iv_icon;
    private TextView tv_name;
    private TextView tv_integral;//送的积分
    private TextView tv_remark;
    private TextView tv_size;//大小
    private TextView tv_version;//版本
    private TextView tv_date;//日期
    private TextView tv_percentage;//进度

    private Button bt_download;
    private String apkPath;//安装包路径
    private String percentage;//下载进度

    private LinearLayout ll_percentage;

    private int mPosition;//软件的位置
    private int state;//下载状态

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_software_download);

        //添加到观察者队列
        CoresunApp.downloadSubject.Attach(this);

        setView();
        setListener();
        getData();
        getSoftwareInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //从观察者队列中移除
        CoresunApp.downloadSubject.Detach(this);
    }
    //获取软件的详细信息
    private void getSoftwareInfo() {
        GetSoftwareInfoBiz.getInfo(enty.getId(), new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                //软件的详细信息
                showSoftwareInfo(result);
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }
    //显示软件的详细信息
    private void showSoftwareInfo(String result) {
        try {
            JSONObject obj=new JSONObject(result);
            String res=obj.getString("result");
            if(res.equals("200")){
                SoftwareDetailEntity enty = SoftwareDetailParser.parser(result);

                String image=enty.getIcon();
                String name=enty.getName();
                String remark=enty.getRemark();
                String integral=enty.getIntegral();
                String date=enty.getDate();
                String version=enty.getVersion();
                String size=enty.getSize();
                List<String> image_list = enty.getImg_url();

                ImagerLoaderUtil.displayImage(image,iv_icon);
                tv_name.setText(name);
                tv_remark.setText(remark);
                tv_integral.setText("+"+integral+"积分");
                tv_size.setText("大小:"+size+"M");
                tv_date.setText("日期:"+date);
                tv_version.setText("版本:"+version);

                //显示轮播图
                for(int i=0;i<image_list.size();i++){
                    String url=image_list.get(i);
                    //显示广告图片
                    View viewPhoto = LayoutInflater.from(this).inflate(R.layout.item_software_download_picture, null);
                    ImageView img = (ImageView) viewPhoto.findViewById(R.id.imageview);

                    ImagerLoaderUtil.displayImage(url,img);
                    ll_container.addView(viewPhoto);
                }
            }else{
                ToastUtil.makeToast("读取软件信息异常");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //获取上个页面传递过来的数据
    private void getData() {
        Intent intent=getIntent();
        enty = (SoftwareEntity) intent.getSerializableExtra("data");
        mPosition =intent.getIntExtra("position",-1);
        state=intent.getIntExtra("state",-1);
        percentage=intent.getStringExtra("percentage");
        apkPath=intent.getStringExtra("apkPath");

        //进入页面的时候设置按钮的状态
        setDownloadState(state);

        //如果下载状态为暂停,则显示进度
        if(state==Const.DOWNLOAD_STATE_PAUSE){
            setProgress(percentage);
        }
    }
    //设置监听器
    private void setListener() {
        //返回
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //下载
        bt_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断用户是否已经登录
                if(CoresunApp.USER_ID==null){
                    ToastUtil.makeToast("请先登录");
                    Intent intent=new Intent(SoftwareDownloadActivity.this, LoginActivity.class);
                    startActivity(intent);
                    return;
                }

                download();
            }
        });
    }
    //下载软件
    private void download() {
        DownloadManger manager=null;
        if(state==Const.DOWNLOAD_STATE_READY){//未开始
            //判断网络状态,并且判断是否下载
            checkNetwork();
        }else if(state==Const.DOWNLOAD_STATE_DOWNLOADING){//下载中
            manager= CoresunApp.downloadManagers.get(enty.getInteraction());
            manager.pause();
        }else if(state==Const.DOWNLOAD_STATE_PAUSE){//暂停
            manager= CoresunApp.downloadManagers.get(enty.getInteraction());
            manager.download();
        }else if(state==Const.DOWNLOAD_STATE_FINISH){//完成
            openToInstall();
        }
    }
    //开始下载
    private void beginToDownload() {
        DownloadManger manager;
        manager=new DownloadManger(enty,mPosition);
        //加入队列中
        CoresunApp.downloadManagers.put(enty.getInteraction(),manager);
        manager.download();
    }
    //检查当前网络状态
    private void checkNetwork() {
        ConnectivityManager conMan = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo mobileNetworkInfo = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if(mobileNetworkInfo.isConnected()) {
            //移动网络则提示用户
            DialogUtil.showTipsDailog(this,"下载提示","当前为移动网络,确定要继续下载吗?",positiveListener,negativeListener);
        }else{
            //否则直接开始下载
            beginToDownload();
        }
    }
    //确定下载
    DialogInterface.OnClickListener positiveListener=new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            beginToDownload();
        }
    };
    //取消下载
    DialogInterface.OnClickListener negativeListener=new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            DialogUtil.close();
        }
    };
    //打开安装apk的页面
    private void openToInstall() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(apkPath)),"application/vnd.android.package-archive");
        startActivity(intent);
    }
    //初始化组件
    private void setView() {
        ll_back= (LinearLayout) findViewById(R.id.ll_software_download_back);
        ll_container= (LinearLayout) findViewById(R.id.ll_software_photo_container);
        ll_percentage= (LinearLayout) findViewById(R.id.ll_software_download_percentage);

        iv_icon= (ImageView) findViewById(R.id.iv_software_icon);
        tv_name= (TextView) findViewById(R.id.tv_software_title);
        tv_integral= (TextView) findViewById(R.id.tv_software_integral);
        tv_size= (TextView) findViewById(R.id.tv_software_size);
        tv_version= (TextView) findViewById(R.id.tv_software_version);
        tv_date= (TextView) findViewById(R.id.tv_software_date);
        tv_remark= (TextView) findViewById(R.id.tv_discover_remark);
        tv_percentage= (TextView) findViewById(R.id.tv_software_percentage);

        bt_download= (Button) findViewById(R.id.bt_software_download);
    }
    //显示进度
    private void setProgress(String percentage) {
        if(null==percentage){
            percentage="0";
        }
        if(ll_percentage.getVisibility()== View.GONE){
            ll_percentage.setVisibility(View.VISIBLE);
        }
        tv_percentage.setText(percentage+"%");
    }
    //观察者回调
    @Override
    public void updateDownloadState(int state, String percentage, int position,String apkPath) {
        LogUtil.i("activity观察者:"+state+";"+percentage+";"+position);

        //回调的位置是我当前的位置我才进行显示,否则说明是其他item的,则不显示
        if(position==mPosition){
            //更新下载状态
            this.state=state;

            setProgress(percentage);

            //显示状态
            setDownloadState(state);
        }
    }
    //更新按钮的状态
    private void setDownloadState(int state) {
        if(state==Const.DOWNLOAD_STATE_READY){//未开始
            bt_download.setText("安装");
        }else if(state== Const.DOWNLOAD_STATE_DOWNLOADING){//下载中
            bt_download.setText("暂停");
        }else if(state==Const.DOWNLOAD_STATE_PAUSE){//暂停
            bt_download.setText("继续");
        }else if(state==Const.DOWNLOAD_STATE_FINISH){//完成
            bt_download.setText("打开");
            bt_download.setBackgroundResource(R.drawable.selector_tv_state_open_software);
        }
    }
}
