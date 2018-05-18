package com.xinspace.csevent.util;

import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;

import com.xinspace.csevent.app.AppConfig;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *此类用于上传头像
 */
public class UploadUtil {

    private static StringBuffer totalString;
    public static final int HANDLER_RETURN_IMAGE_DATA=1000;//回传头像数据
    /**
     * 上传头像
     * @param bytes 头像byte数组
     */
    public static void uploadImg(final byte[] bytes, final Handler handler){
        new Thread(){
            @Override
            public void run() {
                uploadHeadImage(bytes,handler);
            }
        }.start();
    }

    private static void uploadHeadImage(byte[] bytes,Handler handler) {
        try {
            URL url=new URL(AppConfig.UPLOAD_PHOTO_URL);
            String boundary = "*****";
            String end = "\r\n";
            String twoHyphens = "--";
            String fileName = "image.png";  //报文中的文件名参数
            String indexName="user_avatar";
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
          /* Output to the connection. Default is false,
             set to true because post method must write something to the connection */
            con.setDoOutput(true);
          /* Read from the connection. Default is true.*/
            con.setDoInput(true);

          /* Post cannot use caches */
            con.setUseCaches(false);
          /* Set the post method. Default is GET*/
            con.setRequestMethod("POST");
          /* 设置请求属性 */
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

          /*设置StrictMode 否则HTTPURLConnection连接失败，因为这是在主进程中进行网络连接*/
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
          /* 设置DataOutputStream，getOutputStream中默认调用connect()*/
            DataOutputStream ds = new DataOutputStream(con.getOutputStream());  //output to the connection
            ds.writeBytes(twoHyphens + boundary + end);

            ds.writeBytes("Content-Disposition: form-data; " +
                    "name=\""+indexName+"s\";filename=\"" +
                    fileName + "\""+ end);
            ds.writeBytes("Content-Type:image/png"+end);

            ds.writeBytes(end);

            OutputStream out = con.getOutputStream();
            ds.write(bytes, 0, bytes.length);
            ds.writeBytes(end);
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
            out.flush();
            out.close();

            //响应码200
            int respCode = con.getResponseCode();
            String currentLine;
            //存放响应结果
            totalString = new StringBuffer();

            if(respCode==200){
                InputStream in = con.getInputStream();
                BufferedReader reader=new BufferedReader(new InputStreamReader(in));
                while((currentLine=reader.readLine())!=null){
                    if(currentLine.length()>0){
                        totalString.append(currentLine.trim());
                    }
                }
                LogUtil.i("上传头像返回结果:" + totalString.toString());
                Message msg = handler.obtainMessage();
                msg.what=HANDLER_RETURN_IMAGE_DATA;
                msg.obj=totalString.toString();
                handler.sendMessage(msg);

                reader.close();
                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.i("上传头像异常:"+e.getMessage());
        }
    }
}
