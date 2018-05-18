package com.xinspace.csevent.shop.utils;

import android.os.Handler;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.xinspace.csevent.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

/**
 * com.cjdd.showo.shop.widget
 * on 2016/6/4.
 */
public class UpLoadImg {

    private static MediaType MEDIA_TYPE;

    private static final OkHttpClient client = new OkHttpClient();

    static String imgUrl;

    public static void upLoadImg(String url, String filePath , final Handler handler , String flag) throws Exception {

        //String imgUrl;

        LogUtil.i( "UpLoadPicUpLoadPic" + "   url   "+ url +  "   filePath    " + filePath);
        if (flag.equals("jpg")){
            MEDIA_TYPE = MediaType.parse("image/jpg");
        } else if (flag.equals("png")){
            MEDIA_TYPE = MediaType.parse("image/png");
        }else if (flag.equals("jpeg")){
            MEDIA_TYPE = MediaType.parse("image/jpeg");
        }

        File file = new File(filePath);
        RequestBody requestBody = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addFormDataPart("file", file.getName(), RequestBody.create(MEDIA_TYPE, file))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String json = response.body().string();
                LogUtil.i("上传图片返回json" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if (jsonObject.getString("code").equals("200")){
                        JSONObject dataJsonObject = jsonObject.getJSONObject("data");
                        String url = dataJsonObject.getString("url");
                        handler.obtainMessage(101 , url).sendToTarget();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
