package com.xinspace.csevent.util.parser;

import com.xinspace.csevent.data.entity.SoftwareDetailEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 软件下载的详细信息解析类
 */
public class SoftwareDetailParser {
    /**
     * @param result 要解析的字符串
     */
    public static SoftwareDetailEntity parser(String result){
        try {
            JSONObject object=new JSONObject(result);
            JSONObject json = object.getJSONObject("data");
            JSONArray img_url_ary = json.getJSONArray("img_url");
            JSONArray img_thumbnailurl_ary = json.getJSONArray("img_thumbnailurl");

            List<String> list_img_url=new ArrayList<>();//原图集合
            List<String> list_img_thumbnailurl=new ArrayList<>();//原图集合

            for (int i=0;i<img_url_ary.length();i++){
                JSONObject obj_url = img_url_ary.getJSONObject(i);
                String url=obj_url.getString("img_url");

                JSONObject obj_thumb=img_thumbnailurl_ary.getJSONObject(i);
                String thumb_url=obj_thumb.getString("img_thumbnailurl");

                //添加到集合中
                list_img_url.add(url);
                list_img_thumbnailurl.add(thumb_url);
            }
            String id =json.getString("id");
            String icon=json.getString("icon");
            String remark=json.getString("remark");
            String name=json.getString("name");
            String interaction=json.getString("interaction");
            String integral=json.getString("integral");
            String date=json.getString("date");
            String version=json.getString("version");
            String size=json.getString("size");

            return new SoftwareDetailEntity(id,icon,interaction,remark,integral,name,date,size,version,list_img_url,list_img_thumbnailurl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
