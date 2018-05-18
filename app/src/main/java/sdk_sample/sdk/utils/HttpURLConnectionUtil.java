package sdk_sample.sdk.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import org.linphone.mediastream.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import sdk_sample.sdk.result.BaseResult;

/**
 * Created by Android on 2017/3/23.
 */

public class HttpURLConnectionUtil {
    public static final int CONNECT_TIMEOUT = 30000;
    public static final int READ_TIMEOUT = 60000;

    private HttpURLConnectionUtil() {
    }

    public static void doGet(String path, Map<String, String> headerParams, Map<String, String> params, Handler handler) throws Exception {
        doGet((Context)null, path, headerParams, params, "UTF-8", handler, false);
    }

    public static void doGet(Context context, String path, Map<String, String> headerParams, Map<String, String> params, Handler handler, boolean isLoop) throws Exception {
        doGet(context, path, headerParams, params, "UTF-8", handler, isLoop);
    }

    public static void doGet(Context context, final String path, final Map<String, String> headParams, final Map<String, String> params, final String encode, final Handler mHandler, boolean isLoop) throws Exception {
        (new Thread() {
            public void run() {
                String returnValue = null;
                if(TextUtils.isEmpty(path)) {
                    if(mHandler != null) {
                        Message urlPath1 = mHandler.obtainMessage();
                        BaseResult url2 = new BaseResult();
                        url2.setMsg("请求 url 不能为空");
                        urlPath1.what = 3;
                        urlPath1.obj = url2;
                        mHandler.sendMessage(urlPath1);
                    }

                } else {
                    StringBuilder urlPath = new StringBuilder(path);
                    urlPath.append('?');
                    Map.Entry url;
                    if(params != null && params.size() > 0) {
                        Iterator connection = params.entrySet().iterator();

                        while(connection.hasNext()) {
                            url = (Map.Entry)connection.next();

                            try {
                                urlPath.append((String)url.getKey()).append('=').append(URLEncoder.encode((String)url.getValue(), encode)).append('&');
                            } catch (UnsupportedEncodingException var16) {
                                var16.printStackTrace();
                            }
                        }

                        urlPath.deleteCharAt(urlPath.length() - 1);
                    }

                    if(!TextUtils.isEmpty(urlPath)) {
                        Log.i("url:   " + urlPath.toString());
                    }

                    url = null;
                    HttpURLConnection connection1 = null;

                    try {
                        URL url1 = new URL(urlPath.toString());
                        connection1 = (HttpURLConnection)url1.openConnection();
                        connection1.setRequestMethod("GET");
                        connection1.setRequestProperty("Accept", "*/*");
                    } catch (IOException var15) {
                        var15.printStackTrace();
                    }

                    connection1.setConnectTimeout(30000);
                    connection1.setReadTimeout('\uea60');
                    Iterator mess;
                    if(headParams != null) {
                        mess = headParams.keySet().iterator();

                        while(mess.hasNext()) {
                            String e = (String)mess.next();
                            connection1.setRequestProperty(e, (String)headParams.get(e));
                            Log.i("key= " + e + " and value= " + (String)headParams.get(e));
                        }
                    }

                    try {
                        int responCode = connection1.getResponseCode();
                        if( responCode == 200) {
                            ByteArrayOutputStream e1 = new ByteArrayOutputStream();
                            InputStream mess2 = null;

                            try {
                                mess2 = connection1.getInputStream();
                                byte[] baseBean2 = new byte[10240];
                                boolean baseBean1 = false;

                                int baseBean5;
                                while((baseBean5 = mess2.read(baseBean2)) > 0) {
                                    e1.write(baseBean2, 0, baseBean5);
                                }

                                mess2.close();
                                returnValue = new String(e1.toByteArray(), encode);
                            } catch (IOException var17) {
                                var17.printStackTrace();
                            } finally {
                                if(mess2 != null) {
                                    mess2.close();
                                    mess = null;
                                }

                            }

                            Log.i("result:    " + returnValue);
                            if(mHandler != null) {
                                Message baseBean3 = mHandler.obtainMessage();
                                BaseResult baseBean6 = new BaseResult();
                                baseBean6.setMsg(returnValue);
                                baseBean3.what = connection1.getResponseCode();
                                baseBean3.obj = baseBean6;
                                mHandler.sendMessage(baseBean3);
                            }
                        } else if(mHandler != null) {
                            Message e2 = mHandler.obtainMessage();
                            BaseResult mess3 = new BaseResult();
                            int baseBean4 = connection1.getResponseCode();
                            if(baseBean4 == 404) {
                                mess3.setMsg("404  接口不存在");
                            } else {
                                mess3.setMsg("服务器错误");
                            }

                            e2.what = connection1.getResponseCode();
                            e2.obj = mess3;
                            mHandler.sendMessage(e2);
                        }
                    } catch (IOException var19) {
                        Log.e("URL CONN", var19.toString());
                        var19.printStackTrace();
                        if(mHandler != null) {
                            Message mess1 = mHandler.obtainMessage();
                            BaseResult baseBean = new BaseResult();
                            baseBean.setMsg("请连接网络");
                            mess1.what = 202;
                            mess1.obj = baseBean;
                            mHandler.sendMessage(mess1);
                        }
                    }

                }
            }
        }).start();
    }

    public static void doPost(Context context, String path, Map<String, String> headParams, Map<String, String> params, Handler mHandler, boolean isLoop) throws Exception {
        doPost(context, path, headParams, params, "UTF-8", mHandler, isLoop);
    }

    public static void doPost(Context context, final String path, final Map<String, String> headParams, final Map<String, String> params, final String encode, final Handler mHandler, boolean isLoop) throws Exception {
        (new Thread() {
            public void run() {
                String returnValue = null;
                StringBuilder body = new StringBuilder();
                if(TextUtils.isEmpty(path)) {
                    if(mHandler != null) {
                        Message bodyContent2 = mHandler.obtainMessage();
                        BaseResult url2 = new BaseResult();
                        url2.setMsg("请求 url 不能为空");
                        bodyContent2.what = 3;
                        bodyContent2.obj = url2;
                        mHandler.sendMessage(bodyContent2);
                    }

                } else {
                    if(params != null && params.size() > 0) {
                        Iterator url = params.entrySet().iterator();

                        while(url.hasNext()) {
                            Map.Entry bodyContent = (Map.Entry)url.next();

                            try {
                                body.append((String)bodyContent.getKey()).append('=').append(URLEncoder.encode((String)bodyContent.getValue(), encode)).append('&');
                            } catch (UnsupportedEncodingException var37) {
                                var37.printStackTrace();
                            }
                        }

                        body.deleteCharAt(body.length() - 1);
                    }

                    byte[] bodyContent1 = body.toString().getBytes();
                    URL url1 = null;

                    try {
                        url1 = new URL(path);
                    } catch (MalformedURLException var36) {
                        var36.printStackTrace();
                    }

                    HttpURLConnection connection = null;

                    try {
                        connection = (HttpURLConnection)url1.openConnection();
                        connection.setRequestMethod("POST");
                    } catch (IOException var35) {
                        var35.printStackTrace();
                    }

                    connection.setConnectTimeout(30000);
                    connection.setReadTimeout('\uea60');
                    connection.setDoOutput(true);
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    connection.setRequestProperty("Content-Length", String.valueOf(bodyContent1.length));
                    String os;
                    if(headParams != null) {
                        Iterator e = headParams.keySet().iterator();

                        while(e.hasNext()) {
                            os = (String)e.next();
                            connection.setRequestProperty(os, (String)headParams.get(os));
                            System.out.println("key= " + os + " and value= " + (String)headParams.get(os));
                        }
                    }

                    OutputStream os1 = null;

                    Message mess;
                    BaseResult baseBean;
                    try {
                        os1 = connection.getOutputStream();
                        os1.write(bodyContent1);
                        os1.flush();
                    } catch (IOException var41) {
                        if(mHandler != null) {
                            mess = mHandler.obtainMessage();
                            baseBean = new BaseResult();
                            baseBean.setMsg("请连接网络");
                            mess.what = 202;
                            mess.obj = baseBean;
                            mHandler.sendMessage(mess);
                        }
                    } finally {
                        if(os1 != null) {
                            try {
                                os1.close();
                            } catch (IOException var34) {
                                var34.printStackTrace();
                            }

                            os = null;
                        }

                    }

                    try {
                        if(connection.getResponseCode() == 200) {
                            ByteArrayOutputStream e1 = new ByteArrayOutputStream();
                            InputStream mess1 = null;

                            try {
                                mess1 = connection.getInputStream();
                                byte[] baseBean2 = new byte[10240];
                                boolean baseBean1 = false;

                                int baseBean4;
                                while((baseBean4 = mess1.read(baseBean2)) > 0) {
                                    e1.write(baseBean2, 0, baseBean4);
                                }

                                mess1.close();
                                returnValue = new String(e1.toByteArray(), encode);
                            } catch (IOException var38) {
                                var38.printStackTrace();
                            } finally {
                                if(mess1 != null) {
                                    mess1.close();
                                    mess = null;
                                }

                            }

                            Log.i("post result:    " + returnValue);
                            if(mHandler != null) {
                                Message baseBean3 = mHandler.obtainMessage();
                                BaseResult baseBean5 = new BaseResult();
                                baseBean5.setMsg(returnValue);
                                baseBean3.what = connection.getResponseCode();
                                baseBean3.obj = baseBean5;
                                mHandler.sendMessage(baseBean3);
                            }
                        } else {
                            Log.e(returnValue);
                            if(mHandler != null) {
                                Message e2 = mHandler.obtainMessage();
                                BaseResult mess2 = new BaseResult();
                                mess2.setMsg("服务器错误");
                                e2.what = connection.getResponseCode();
                                e2.obj = mess2;
                                mHandler.sendMessage(e2);
                            }
                        }
                    } catch (IOException var40) {
                        if(mHandler != null) {
                            mess = mHandler.obtainMessage();
                            baseBean = new BaseResult();
                            baseBean.setMsg("请连接网络");
                            mess.what = 500;
                            mess.obj = baseBean;
                            mHandler.sendMessage(mess);
                        }
                    }

                }
            }
        }).start();
    }

    public static String doPost(String path, Map<String, String> params, Map<String, File> files) throws Exception {
        return doPost(path, params, files, "UTF-8");
    }

    public static String doPost(String path, Map<String, String> params, Map<String, File> files, String encode) throws Exception {
        String BOUNDARY = UUID.randomUUID().toString();
        String PREFIX = "--";
        String LINEND = "\r\n";
        String MULTIPART_FROM_DATA = "multipart/form-data";
        String CHARSET = encode;
        URL uri = new URL(path);
        HttpURLConnection conn = (HttpURLConnection)uri.openConnection();
        conn.setConnectTimeout(30000);
        conn.setReadTimeout('\uea60');
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("connection", "keep-alive");
        conn.setRequestProperty("Charsert", encode);
        conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);
        StringBuilder sb = new StringBuilder();
        if(params != null) {
            Iterator end_data = params.entrySet().iterator();

            while(end_data.hasNext()) {
                Map.Entry outStream = (Map.Entry)end_data.next();
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINEND);
                sb.append("Content-Disposition: form-data; name=\"" + (String)outStream.getKey() + "\"" + LINEND);
                sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
                sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
                sb.append(LINEND);
                sb.append((String)outStream.getValue());
                sb.append(LINEND);
            }
        }

        DataOutputStream outStream1 = new DataOutputStream(conn.getOutputStream());
        outStream1.write(sb.toString().getBytes());
        if(files != null) {
            Iterator res = files.entrySet().iterator();

            while(res.hasNext()) {
                Map.Entry end_data1 = (Map.Entry)res.next();
                StringBuilder data = new StringBuilder();
                data.append(PREFIX);
                data.append(BOUNDARY);
                data.append(LINEND);
                data.append("Content-Disposition: form-data; name=\"" + (String)end_data1.getKey() + "\"; filename=\"" + ((File)end_data1.getValue()).getName() + "\"" + LINEND);
                data.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINEND);
                data.append(LINEND);
                outStream1.write(data.toString().getBytes());
                FileInputStream in = new FileInputStream((File)end_data1.getValue());
                byte[] isReader = new byte[1024];
                boolean bufReader = false;

                int bufReader1;
                while((bufReader1 = in.read(isReader)) != -1) {
                    outStream1.write(isReader, 0, bufReader1);
                }

                in.close();
                outStream1.write(LINEND.getBytes());
            }
        }

        byte[] end_data2 = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
        outStream1.write(end_data2);
        outStream1.flush();
        int res1 = conn.getResponseCode();
        String data1 = "";
        if(res1 != 200) {
            throw new Exception("服务器响应错误" + res1);
        } else {
            InputStream in1 = conn.getInputStream();
            InputStreamReader isReader1 = new InputStreamReader(in1);
            BufferedReader bufReader2 = new BufferedReader(isReader1);

            for(String line = null; (line = bufReader2.readLine()) != null; data1 = data1 + line) {
                ;
            }

            bufReader2.close();
            outStream1.close();
            conn.disconnect();
            return data1;
        }
    }

    public static boolean downLoadFile(String urlStr, File file) {
        if(!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        boolean flag = true;
        InputStream inputStream = null;
        FileOutputStream outputStream = null;

        try {
            URL e = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection)e.openConnection();
            connection.setConnectTimeout(30000);
            connection.setReadTimeout('\uea60');
            inputStream = connection.getInputStream();
            outputStream = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            boolean len = false;

            int len1;
            while((len1 = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, len1);
            }
        } catch (MalformedURLException var19) {
            var19.printStackTrace();
            flag = false;
        } catch (Exception var20) {
            var20.printStackTrace();
            flag = false;
        } finally {
            try {
                if(inputStream != null) {
                    inputStream.close();
                    inputStream = null;
                }

                if(outputStream != null) {
                    outputStream.close();
                    outputStream = null;
                }
            } catch (Exception var18) {
                var18.printStackTrace();
                flag = false;
            }

        }

        if(!flag && file != null && file.exists()) {
            file.delete();
        }

        return flag;
    }
}

