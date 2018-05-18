package com.xinspace.csevent.data.biz;

import com.xinspace.csevent.app.CoresunApp;
import com.xinspace.csevent.data.entity.SoftwareEntity;
import com.xinspace.csevent.util.Const;
import com.xinspace.csevent.util.LogUtil;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.HttpHandler;

import java.io.File;

/**
 * 软件下载业务
 */
public class DownloadManger {

    private SoftwareEntity enty;
    private HttpHandler<File> handler;
    private int position;
    private int state=Const.DOWNLOAD_STATE_READY;//当前下载状态
    private String path;//apk路径
    private String percentage;
    private boolean isFirst=true;//是否第一次点击下载

    public DownloadManger(SoftwareEntity enty,int position) {
        this.enty=enty;
        this.position=position;
    }
    //下载
    public void download(){
        //设置下载状态为显示
        state=Const.DOWNLOAD_STATE_DOWNLOADING;
        //给主题对象设置数据
        CoresunApp.downloadSubject.setData(state,percentage,position,path);

        path=Const.APK_STEORY_PATH + enty.getName()+".apk";
        //如果当前存在该文件,则删除文件,第一次点击下载时,如果是继续下载时则不删除
        File file=new File(path);
        if(file.exists()&&isFirst){
            file.delete();
            isFirst=false;
        }
        FinalHttp fh = new FinalHttp();
        handler=fh.download(enty.getInteraction(),path, true,new AjaxCallBack<File>() {
            @Override
            public void onStart() {
                super.onStart();
                //设置下载状态为显示
                state=Const.DOWNLOAD_STATE_DOWNLOADING;
            }

            @Override
            public void onLoading(long count, long current) {
                super.onLoading(count, current);

                Float f_curr = Float.valueOf(current);
                Float f_count = Float.valueOf(count);
                float fen = f_curr / f_count;
                LogUtil.i("进度:" + fen * 100 + "%");
                fen = fen * 100;
                String bili = String.valueOf(fen);
                if (bili.contains(".")) {
                    bili = bili.substring(0, bili.lastIndexOf("."));
                }

                //将进度保存到成员
                percentage=bili;

                //给主题对象设置数据
                CoresunApp.downloadSubject.setData(state,percentage,position,path);
            }

            @Override
            public void onSuccess(File file) {
                super.onSuccess(file);
                //设置下载状态为已经完成
                state=Const.DOWNLOAD_STATE_FINISH;
                //给主题对象设置数据
                CoresunApp.downloadSubject.setData(state,percentage,position,path);

                //TODO 下载完成之后将manager对象从全局队列中移除
                CoresunApp.downloadManagers.remove(enty.getInteraction());
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }
    //暂停
    public void pause(){
        handler.stop();
        //设置下载状态为暂停
        state=Const.DOWNLOAD_STATE_PAUSE;

        //给主题对象设置数据
        CoresunApp.downloadSubject.setData(state,percentage,position,path);
    }
    //继续
    public void continueDownload(){
        download();

        //设置下载状态为进行中
        state=Const.DOWNLOAD_STATE_DOWNLOADING;

        //给主题对象设置数据
        CoresunApp.downloadSubject.setData(state,percentage,position,path);
    }
}
