package com.xinspace.csevent.myinterface;

import com.baidu.mapapi.model.LatLng;

/***
 * 百度地图 反编译地址的回调接口 (地址-->经纬度)
 */
public interface SearchLocationListener {
    void getLocationForPosition(LatLng latLng);
    void getLocationForCity(LatLng latLng);
}
