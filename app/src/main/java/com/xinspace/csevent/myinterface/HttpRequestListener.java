package com.xinspace.csevent.myinterface;

import org.json.JSONException;

/**
 * 联网获取数据成功后回传数据的接口
 */
public interface HttpRequestListener {
    /**
     * 回调方法
     * @param result 数据
     */
    void onHttpRequestFinish(String result) throws JSONException;


    void onHttpRequestError(String error);

}
