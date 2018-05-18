package com.xinspace.csevent.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.app.CoresunApp;
import com.xinspace.csevent.data.biz.DownloadManger;
import com.xinspace.csevent.data.entity.SoftwareEntity;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.myinterface.Observer;
import com.xinspace.csevent.util.Const;
import com.xinspace.csevent.util.DialogUtil;
import com.xinspace.csevent.util.ImagerLoaderUtil;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.ToastUtil;
import com.xinspace.csevent.login.activity.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发现页面的推荐软件列表Adapter
 */
public class DiscoverSoftwareAdapter extends BaseAdapter implements HttpRequestListener,Observer {
    private List<Object> list;
    private Context context;
    private LayoutInflater inflater;

    private Map<Integer,Integer> stateMap;//保存每个item的现在状态
    private Map<Integer,String> apkPaths;//保存下载完成的apk路径

    public DiscoverSoftwareAdapter(List<Object> list, Context context) {
        this.list = list;
        this.context = context;
        this.inflater=LayoutInflater.from(context);
        //创建保存每个软件下载状态的集合,并初始化每个状态为未开始
        stateMap =new HashMap<>();
        //保存下载完成的apk路径
        apkPaths=new HashMap<>();
        //初始化所有的状态为"未开始"
        for (int i=0;i<list.size();i++){
            stateMap.put(i,Const.DOWNLOAD_STATE_READY);
        }
    }
    @Override
    public int getCount() {return list.size();}
    @Override
    public Object getItem(int position) {return list.get(position);}
    @Override
    public long getItemId(int position) { return position;}
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.item_discover_software, null);
        ImageView ivIcon = (ImageView) convertView.findViewById(R.id.iv_discover_icon);
        TextView tvTitle= (TextView) convertView.findViewById(R.id.tv_discover_title);
        TextView tvRemark= (TextView) convertView.findViewById(R.id.tv_discover_remark);
        TextView tvPercentage= (TextView) convertView.findViewById(R.id.tv_discover_percentage);
        TextView tvDownload= (TextView) convertView.findViewById(R.id.tv_discover_download);
        TextView tvIntegral = (TextView) convertView.findViewById(R.id.tv_discover_interaction);

        final SoftwareEntity enty = (SoftwareEntity) list.get(position);
        //显示数据
        ImagerLoaderUtil.displayImage(enty.getIcon(), ivIcon);
        tvTitle.setText(enty.getName());
        tvRemark.setText(enty.getRemark());
        tvIntegral.setText("+" + enty.getIntegral() + "积分");

        //设置监听
        tvDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CoresunApp.USER_ID==null){
                    ToastUtil.makeToast("请先登录");
                    Intent intent=new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                    return;
                }
                //下载
                download(enty, position);
            }
        });
        return convertView;
    }
    //下载
    private void download(SoftwareEntity enty, int position) {
        //首先将当前item的下载状态保存到集合中
        int state= stateMap.get(position);
        DownloadManger manager=null;
        if(state==Const.DOWNLOAD_STATE_READY){//未开始
            //检查网络状态,并且判断是否要下载
            checkNetwork(enty,position);

        }else if(state==Const.DOWNLOAD_STATE_DOWNLOADING){//进行中
            manager= CoresunApp.downloadManagers.get(enty.getInteraction());
            manager.pause();
            LogUtil.i("暂停");
        }else if(state==Const.DOWNLOAD_STATE_PAUSE){//暂停
            manager= CoresunApp.downloadManagers.get(enty.getInteraction());
            manager.continueDownload();
            LogUtil.i("继续");
        }else if(state==Const.DOWNLOAD_STATE_FINISH){//完成
            LogUtil.i("打开");
            String path=apkPaths.get(position);

            //打开安装
            openToInstall(path);
        }
    }
    //开始下载
    private void beginToDownload(SoftwareEntity enty, int position) {
        DownloadManger manager;
        manager=new DownloadManger(enty,position);
        //添加到队列
        CoresunApp.downloadManagers.put(enty.getInteraction(),manager);
        manager.download();
    }
    //检查当前网络状态
    private void checkNetwork(final SoftwareEntity enty, final int position) {
        ConnectivityManager conMan = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobileNetworkInfo = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if(mobileNetworkInfo.isConnected()) {
            //移动网络则提示用户
            DialogUtil.showTipsDailog(context, "下载提示", "当前为移动网络,确定要继续下载吗?", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    beginToDownload(enty,position);
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DialogUtil.close();
                }
            });
        }else{//否则直接下载
            beginToDownload(enty,position);
        }
    }
    //打开安装apk
    private void openToInstall(String path) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(path)),"application/vnd.android.package-archive");
        context.startActivity(intent);
    }
    //观察者回调
    @Override
    public void updateDownloadState(int state, String percentage, int position,String apkPath) {
        LogUtil.i("adapter观察者:"+state+";"+percentage+";"+position);

        //更新相应位置item的下载状态
        stateMap.put(position,state);
        //将下载完成的apk路径保存到集合中去
        if(state==Const.DOWNLOAD_STATE_FINISH){
            apkPaths.put(position,apkPath);
        }
    }
    //联网回调
    @Override
    public void onHttpRequestFinish(String result) {
        try {
            JSONObject obj=new JSONObject(result);
            String res=obj.getString("result");
            if(res.equals("201")){//已经下载过该软件,不能再添加积分
                ToastUtil.makeToast("已获得过该应用积分,不能重复获取");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onHttpRequestError(String error) {

    }
}
