package com.xinspace.csevent.util;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.xinspace.csevent.R;
import com.xinspace.csevent.data.biz.UpdateVersionBiz;
import com.xinspace.csevent.data.entity.UpdateVersionEntity;
import com.xinspace.csevent.ui.fragment.MainPageFragment;
import com.xinspace.csevent.myinterface.HttpRequestListener;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.HttpHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;


/**
 * 版本更新工具类
 */
public class UpdateVersionUtil {

    /* 下载结束 */
    private static String versionNumber;//最新版本号
    private static int versionCode;//最新版的备注
    private static String versionAddress;//最新版本的下载地址
    private static String versionDescribe;
    /* 记录进度条数量 */
    private int progress;
    private Context mContext;
    /* 更新进度条 */
    private ProgressBar mProgress;
    private Dialog mDownloadDialog;
    private String path;
    private HttpHandler<File> handler;

    private String currentVersionNumber;//当前版本号
    private int currentVersionCode;//当前版本号

    public UpdateVersionUtil(Context context) {
        this.mContext = context;
    }

    /**
     * 是否需要更新
     */
    public void isUpdate(MainPageFragment fragment) {

        getVersionName();

        // 获取当前软件版本
        //currentVersionNumber = getVersionName();
        LogUtil.i("versionCode:" + currentVersionNumber);
        //获取最新版本的信息
        UpdateVersionBiz.getVersion(fragment, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                LogUtil.i("获取软件版本:" + result);
                JSONObject json = new JSONObject(result);
                if ("200".equals(json.getString("code"))) {
                    JSONObject jsonObject = json.getJSONObject("data");

                    Gson gson = new Gson();
                    UpdateVersionEntity enty = gson.fromJson(jsonObject.toString(), UpdateVersionEntity.class);

//                    UpdateVersionEntity enty= (UpdateVersionEntity) JsonPaser.parserObj(result, UpdateVersionEntity.class);
                    versionNumber = enty.getNumber();
                    versionCode = Integer.valueOf(enty.getCode());
                    versionAddress = enty.getUrl();
                    versionDescribe = enty.getDescribe();
                    LogUtil.i("versionNumber=" + versionNumber);
                    LogUtil.i("versionRemark=" + versionCode);
                    LogUtil.i("versionAddress=" + versionAddress);
                    if (compareVersion(currentVersionCode)) {//对比版本号,是否需要更新
                        showNoticeDialog(versionDescribe);
//                        if ("0".equals(enty.getEnable())) {//0为强制更新
//                        showDownloadDialog();
//                        }else {//否则为可选更新
//                            // 如有新版本显示提示对话框
//                            showNoticeDialog();
//                        }
                    }
                    LogUtil.i("versionNumber:" + versionNumber);
                } else {//201
                    ToastUtil.makeToast("没有可用数据!");
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }

    /**
     * 本地应用和服务器应用版本比较
     */
//    private boolean compareVersion(String versionName) {
//        if (null != versionNumber) {
//            String serviceName = versionNumber;
//            String [] sn = serviceName.split("\\u002E");//  \\u002E 为 "."  的 转义
//            String [] vn = versionName.split("\\u002E");
//            for (int i=0;i<sn.length;i++){
//                if (Integer.parseInt(vn[i]) < Integer.parseInt(sn[i])) {//服务器版本号大于当前版本号,则返回true
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
    private boolean compareVersion(int currentVersionCode) {
        if (currentVersionCode != 0) {
//            String serviceName = versionNumber;
//            String [] sn = serviceName.split("\\u002E");//  \\u002E 为 "."  的 转义
//            String [] vn = versionName.split("\\u002E");
//            for (int i=0;i<sn.length;i++){
//                if (Integer.parseInt(vn[i]) < Integer.parseInt(sn[i])) {//服务器版本号大于当前版本号,则返回true
//                    return true;
//                }
//            }

            if (currentVersionCode < versionCode){
                return true;
            }
        }
        return false;
    }


    /**
     * 显示软件更新对话框
     */
    public void showNoticeDialog(String versionDescribe) {
        // 构造对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("软件更新");
        builder.setMessage("当前版本:(" + currentVersionNumber + ")更新到(" + versionNumber + ")\n\n" + versionDescribe);
        // 更新
        builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // 显示下载对话框
                showDownloadDialog();
            }
        });
        // 稍后更新
        builder.setNegativeButton("稍后更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        Dialog noticeDialog = builder.create();
        noticeDialog.show();
    }

    /**
     * 获取软件版本号
     *
     * @return
     */
    public void getVersionName() {
        try {
            // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
            currentVersionNumber = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
            currentVersionCode = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示软件下载对话框
     */
    public void showDownloadDialog() {
        // 构造软件下载对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("正在更新..");
        // 给下载对话框增加进度条
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.progress_update_version, null);
        mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
        builder.setView(v);
        // 取消更新
        builder.setNegativeButton("取消更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handler.stop();//停止下载
                dialog.dismiss();
            }
        });
        mDownloadDialog = builder.create();
        mDownloadDialog.setCanceledOnTouchOutside(false);
        mDownloadDialog.show();
        // 下载文件
        downloadApk();
    }

    /**
     * 下载apk文件
     */
    public void downloadApk() {
        path = Const.APK_STEORY_PATH + "/shengzhong" + ".apk";
        //如果当前存在该文件,则删除文件,第一次点击下载时,如果是继续下载时则不删除
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        FinalHttp fh = new FinalHttp();
        LogUtil.i("下载地址:" + versionAddress);
        LogUtil.i("下载地址:" + path);
        handler = fh.download(versionAddress, path, true, new AjaxCallBack<File>() {
            @Override
            public void onStart() {
                super.onStart();
                LogUtil.i("onstart");
            }

            @Override//每秒回调(主线程)
            public void onLoading(long count, long current) {
                super.onLoading(count, current);
                LogUtil.i("count/current" + count + "/" + current);
                // 计算进度条位置
                progress = (int) (((float) current / count) * 100);
                mProgress.setProgress(progress);
            }

            @Override
            public void onSuccess(File file) {
                super.onSuccess(file);
                openToInstall();//安装
                // 关闭下载对话框显示
                mDownloadDialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                LogUtil.i("下载失败:" + strMsg + "errorNo" + errorNo);
            }
        });
    }

    /**
     * 安装APK文件
     */
    private void openToInstall() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
        mContext.startActivity(intent);
    }
}
