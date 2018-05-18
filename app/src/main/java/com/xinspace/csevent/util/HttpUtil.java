package com.xinspace.csevent.util;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xinspace.csevent.app.CoresunApp;
import com.xinspace.csevent.data.entity.Params;
import com.xinspace.csevent.myinterface.HttpRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 联网工具类,此类利用接口回调数据
 */
public class HttpUtil {

    private static final String REQUEST_POST = "post_request";

    private RequestQueue queue;
    private HttpRequestListener listener;
    private JsonObjectRequest request;

    public HttpUtil() {
        if (null == queue) {
            queue = Volley.newRequestQueue(CoresunApp.instance);
        }
    }

    //提供一个设置监听器的方法
    public void setOnHttpRequestFinishListener(HttpRequestListener listener) {
        this.listener = listener;
    }


    /**
     * get请求
     *
     * @param url 地址
     */
    public void sendGet(String url) {
        JSONObject json = new JSONObject();
        Log.i("www", "url" + url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    Log.i("www", "get请求成功:" + jsonObject.toString());
                    if (listener == null) {
                        return;
                    }
                    //回传数据

                    if (jsonObject != null){
                        listener.onHttpRequestFinish(jsonObject.toString());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("www", "get请求失败:");
                listener.onHttpRequestError("");
            }
        });
        queue.add(request);
    }



    public void sendGet2(String url) {
        StringRequest  mStringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Hanjh", "get请求结果:" + response);
                        try {
                            if (response != null){
                                listener.onHttpRequestFinish(response.toString());
                            }
                           // listener.onHttpRequestFinish(response.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Hanjh", "get请求错误:" + error.toString());
                try {
                    listener.onHttpRequestFinish(error.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        );
        // 3 将StringRequest添加到RequestQueue
        queue.add(mStringRequest);
    }


    /**
     * post请求
     * @param url    地址
     * @param params 参数列表
     */
    public void sendPost(String url, List<Params> params) throws Exception {
        final JSONObject json = new JSONObject();
        Log.i("www", "请求url=" + url);
        if (params.size() != 0) {
            for (int i = 0; i < params.size(); i++) {
                Params obj = params.get(i);
                String name = obj.getName();
                String value = obj.getValue();
                Log.i("www", "数据请求参数" + "name=" + name + ";value=" + value);
                json.put(name, value);
            }
        }

        LogUtil.i("json-----------" + json.toString());

        request = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    Log.i("www", "post请求成功:" + jsonObject.toString());
                    if (listener == null) {
                        return;
                    }
                    if (jsonObject != null){
                        listener.onHttpRequestFinish(jsonObject.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("www", "post请求失败:" + volleyError.getMessage());
                listener.onHttpRequestError(volleyError.getMessage());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=UTF-8");
                return super.getHeaders();
            }
    };
        request.setTag(REQUEST_POST);
        queue.add(request);
    }

    public void cancelRequest(){
        String tag = (String) request.getTag();
        if (tag.equals(REQUEST_POST) && (!request.isCanceled())){
            request.cancel();
        }
    }

    public void sendPost2(String url, List<Params> params) throws Exception {
        JSONObject json = new JSONObject();
        Log.i("www", "请求url=" + url);
        if (params.size() != 0) {
            for (int i = 0; i < params.size(); i++) {
                Params obj = params.get(i);
                String name = obj.getName();
                String value = obj.getValue();
                Log.i("www", "数据请求参数" + "name=" + name + ";value=" + value);
                json.put(name, value);
            }
        }

        LogUtil.i("json-----------" + json.toString());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    Log.i("www", "post请求成功:" + jsonObject.toString());
                    if (listener == null) {
                        return;
                    }
                    if (jsonObject != null){
                        listener.onHttpRequestFinish(jsonObject.toString());
                    }
                    //listener.onHttpRequestFinish(jsonObject.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("www", "post请求失败:" + volleyError.getMessage());
                try {
                    listener.onHttpRequestFinish(volleyError.getMessage());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        queue.add(request);
    }


    /**
     * @param url
     * @param parmars
     * @throws Exception
     *
     * 返回String
     */
    public void sendPost3(String url , final String parmars) throws Exception {

        Log.i("www", "请求url=" + url);

        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("www", "post请求成功:" + response.toString());
                            if (listener == null) {
                                return;
                            }

                            if (response != null){
                                listener.onHttpRequestFinish(response.toString());
                            }

                            //listener.onHttpRequestFinish(response.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("www", "post请求失败:" + error.getMessage());
                try {
                    listener.onHttpRequestFinish(error.getMessage());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //在这里设置需要post的参数
                Map<String, String> map = new HashMap<String, String>();
                map.put("moid", parmars);
                return map;
            }
        };
        queue.add(stringRequest);
    }


    public void sendPost4(String url , final List<Params> params) throws Exception {

        Log.i("www", "请求url=" + url);

        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("www", "post请求成功:" + response.toString());
                            if (listener == null) {
                                return;
                            }
                            if (response != null){
                                listener.onHttpRequestFinish(response.toString());
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("www", "post请求失败:" + error.getMessage());
                try {
                    listener.onHttpRequestFinish(error.getMessage());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //在这里设置需要post的参数
                Map<String, String> map = new HashMap<String, String>();
                if (params.size() != 0) {
                    for (int i = 0; i < params.size(); i++) {
                        Params obj = params.get(i);
                        String name = obj.getName();
                        String value = obj.getValue();
                        Log.i("www", "数据请求参数" + "name=" + name + ";value=" + value);
                        map.put(name, value);
                    }
                }
                return map;
            }
        };
        queue.add(stringRequest);
    }


    public void sendPost5(String url, String jsonStr) throws Exception {
        LogUtil.i("json-----------22222");
        JSONObject json = new JSONObject(jsonStr);
        LogUtil.i("json-----------" + json.toString());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    Log.i("www", "post请求成功:" + jsonObject.toString());
                    if (listener == null) {
                        return;
                    }

                    if (jsonObject != null ){
                        listener.onHttpRequestFinish(jsonObject.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("www", "post请求失败:" + volleyError.getMessage());
                listener.onHttpRequestError(volleyError.getMessage());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=UTF-8");
                return super.getHeaders();
            }
        };
        //request.setTag();
        queue.add(request);
    }
}
