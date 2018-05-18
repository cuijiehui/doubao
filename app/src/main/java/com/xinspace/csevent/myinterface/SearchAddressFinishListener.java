package com.xinspace.csevent.myinterface;


import com.baidu.mapapi.model.LatLng;

/**
 * 用于回传定位数据的接口
 */
public interface SearchAddressFinishListener{
    /**
     * @param address 反地理编码
     * @param province 省份
     * @param city 城市
     * @param district 区
     */
    void onSearchFinish(LatLng latLng, String address, String province, String city, String district);
}
