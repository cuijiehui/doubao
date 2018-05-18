package com.xinspace.csevent.util.parser;

import android.util.Log;

import com.xinspace.csevent.data.entity.CrowdActDetailEntity;
import com.xinspace.csevent.data.entity.CrowdActEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 众筹活动详情的解析
 */
public class CrowdDetailParser {

    public static CrowdActDetailEntity parser(String str){
        try {
            Log.i("www" , "进来解析即开抽奖活动的数据了");
            JSONObject obj = new JSONObject(str);
            String result = obj.getString("result");
            String msg=obj.getString("msg");
            JSONObject data = obj.getJSONObject("data");

            //活动的信息
            JSONObject activity = data.getJSONObject("activity");

            CrowdActEntity enty = new CrowdActEntity();
            enty.setName(activity.getString("name"));
            enty.setPrice(activity.getString("price"));
            enty.setPid(activity.getString("pid"));
            enty.setTotal_person(activity.getString("total_person"));
            enty.setId(activity.getString("id"));
            enty.setConsume(activity.getString("consume"));
            enty.setAbbreviation(activity.getString("abbreviation"));
            enty.setNoactivity(activity.getString("noactivity"));
            enty.setRemark(activity.getString("remark"));
            enty.setAlready_participate(activity.getString("Already_participate"));
            enty.setSurplus_prize(activity.getString("surplus_prize"));
            enty.setImg(activity.getString("img"));

            enty.setDiscount(activity.getString("discount"));
            enty.setCost_price(activity.getString("cost_price"));

            JSONObject jsonObject = activity.getJSONObject("type");
            enty.setType(jsonObject.getString("type"));

            // 奖品详情 图片
            List<String> thumbnailList = null;
            if (activity.has("thumbnail")){
                JSONArray thumbnail = activity.getJSONArray("thumbnail");
                thumbnailList = new ArrayList<String>();
                for (int i = 0 ; i < thumbnail.length() ; i++){
                    thumbnailList.add(thumbnail.getString(i));
                }
            }


            //产品下拉图
            List<String> adImgList = null;
            if (activity.has("imgList")){
                JSONArray jsonArray = activity.getJSONArray("imgList");
                adImgList = new ArrayList<String>();
                for (int i = 0 ; i < jsonArray.length() ; i++){
                    adImgList.add(jsonArray.getString(i));
                }
            }


            String imgUrl = activity.getString("img");
            //整个活动包括奖品的实体
            CrowdActDetailEntity ade = new CrowdActDetailEntity(result, msg , thumbnailList , enty , imgUrl , adImgList);
            Log.i("www" , "活动详情解析成功");
            return ade;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new CrowdActDetailEntity();
    }
}
