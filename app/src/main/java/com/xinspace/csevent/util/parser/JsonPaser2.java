package com.xinspace.csevent.util.parser;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * json解析
 */
public class JsonPaser2 {
    /**
     * 解析json数组
     * @param data  要解析的字符串
     * @param cls   实体类
     * @return  返回一个List<Object>
     */
    public static  List<Object> parserAry(String data, Class cls,String str){
        List<Object> list=new ArrayList<>();
        try {
            JSONObject obj1=new JSONObject(data);
            JSONArray ary1 = obj1.getJSONArray(str);

            JSONArray ary2=new JSONArray(ary1.toString());
            for (int i=0;i<ary2.length();i++){
                JSONObject obj2 = ary2.getJSONObject(i);
                Gson gson=new Gson();
                Object enty = gson.fromJson(obj2.toString(), cls);
                list.add(enty);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }


    public static Object parserObj(String data, Class cls){
        Object enty=null;
        try {
            Gson gson=new Gson();
            enty = gson.fromJson(data, cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return enty;
    }

    /**
     * 将json解析成一个集合map
     */
    public static Map<String,String> parserForMap(String data){
        Gson gson=new Gson();
        return gson.fromJson(data, Map.class);
    }
}
