package com.xinspace.csevent.util;

import android.util.Log;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.xinspace.csevent.myinterface.OssPutObjectResponseListener;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;

/**
 * 用于阿里云oss存储文件的工具类
 */
public class OssPutObejectUtil {

    private OSS oss;
    private String testBucket;
    private String testObject;
    private String uploadFilePath;
    private OssPutObjectResponseListener listener;

    //构造方法
    public OssPutObejectUtil(OSS client, String testBucket, String testObject, String uploadFilePath) {
        this.oss = client;
        this.testBucket = testBucket;
        this.testObject = testObject;
        this.uploadFilePath = uploadFilePath;

        File filepath=new File(uploadFilePath);
        if(!filepath.exists()){
            try {
                filepath.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //设置成功上传之后的回调
    public void setOnOssPutObjectResponseListener(OssPutObjectResponseListener listener){
        this.listener=listener;
    }

    // 从本地文件上传，使用非阻塞的异步接口
    public void asyncPutObjectFromLocalFile() {
        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(testBucket, testObject, uploadFilePath);
        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });

        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                Log.d("PutObject", "UploadSuccess");
                Log.d("ETag", result.getETag());
                Log.d("RequestId", result.getRequestId());
                LogUtil.i("异步上传成功");
                listener.onPutObjectSuccess();
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                    LogUtil.i("网络异常");
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });

    }
}
