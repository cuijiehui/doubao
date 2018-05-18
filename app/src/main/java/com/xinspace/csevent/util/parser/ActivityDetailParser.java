package com.xinspace.csevent.util.parser;

import android.util.Log;

import com.xinspace.csevent.data.entity.ActivityDetailEntity;
import com.xinspace.csevent.data.entity.ActivityInfoEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * 活动详情的解析
 */
public class ActivityDetailParser {

    public static ActivityDetailEntity parser(String str){
        try {
            Log.i("www" , "进来解析即开抽奖活动的数据了");
            JSONObject obj = new JSONObject(str);
            String result = obj.getString("result");
            String msg=obj.getString("msg");
            JSONObject data = obj.getJSONObject("data");

            //活动的信息
            JSONObject activity = data.getJSONObject("activity");

            ActivityInfoEntity enty = new ActivityInfoEntity();
            enty.setCreatetime(activity.getString("createtime"));
            enty.setActivity_id(activity.getString("activity_id"));
            enty.setStarttime(activity.getString("starttime"));
            enty.setEndtime(activity.getString("endtime"));
            enty.setConsume(activity.getString("consume"));
            enty.setHtml_href(activity.getString("html_content"));
            enty.setHtml_is(activity.getString("html_is"));
            enty.setName(activity.getString("name"));
            enty.setRemark(activity.getString("remark"));
            enty.setWinners(activity.getString("winners"));
            enty.setSurplus_prize(activity.getString("surplus_prize"));
            enty.setSurplus_time(activity.getLong("surplus_time"));
            enty.setNoactivity(activity.getString("noactivity"));
            enty.setPid(activity.getString("pid"));

            //enty.setImgUrl(activity.getJSONObject("imgList").getString("3"));

            // 奖品详情 图片
            List<String> thumbnailList = null;
            if (activity.has("thumbnail")){
                JSONArray thumbnail = activity.getJSONArray("thumbnail");
                thumbnailList = new ArrayList<String>();
                for (int i = 0 ; i < thumbnail.length() ; i++){
                    thumbnailList.add(thumbnail.getString(i));
                }
            }

            String imgUrl = "";

            //整个活动包括奖品的实体
            ActivityDetailEntity ade = new ActivityDetailEntity(result, msg , thumbnailList , enty , imgUrl);
            Log.i("www" , "活动详情解析成功");
            return ade;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ActivityDetailEntity();
    }
}
