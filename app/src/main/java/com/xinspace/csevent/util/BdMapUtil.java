package com.xinspace.csevent.util;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.xinspace.csevent.R;
import com.xinspace.csevent.app.CoresunApp;
import com.xinspace.csevent.myinterface.SearchAddressFinishListener;
import com.xinspace.csevent.myinterface.SearchLocationListener;

/**
 * Created by Yangtuhua on 2015/11/30.
 * 百度地图工具类
 */
public class BdMapUtil {
    private double longitude;
    private double latitude;
    private LocationClient mLocationClient;
    private SearchAddressFinishListener listener;//地址回传数据的接口
    private SearchLocationListener locationListener;//反编译地址回调



    public BdMapUtil(){
        initMap();
    }

    //设置监听器
    public void setOnAddressFinishListener(SearchAddressFinishListener listener){
        this.listener=listener;
    }

    //设置查询地址的回调接口
    public void setOnSearchLocationListener(SearchLocationListener locationListener){
        this.locationListener=locationListener;
    }
    /**
     * 根据城市获取具体的位置
     * @param city 城市
     */
    public void getLocationByCity(String city){
        try{
            GeoCoder geoCoder=GeoCoder.newInstance();
            GeoCodeOption option=new GeoCodeOption();
            option.city(city);
            option.address(city);
            geoCoder.geocode(option);
            geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
                @Override
                public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
                    LatLng location = geoCodeResult.getLocation();
                    LogUtil.i("反编码地址:"+location.longitude+","+location.latitude);
                    if(locationListener==null){
                        return;
                    }
                    LatLng latLng = new LatLng(location.latitude, location.longitude);
                    locationListener.getLocationForCity(latLng);
                }

                @Override
                public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                    LogUtil.i("编码地址:");
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 根据地址获取一个经纬度位置
     * @param address
     */
    public void getLocationByAddress(String address,String city){
        try{
            GeoCoder geoCoder=GeoCoder.newInstance();
            GeoCodeOption option=new GeoCodeOption();
            option.city(city);
            option.address(address);
            geoCoder.geocode(option);
            geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
                @Override
                public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
                    LatLng location = geoCodeResult.getLocation();
                    //LogUtil.i("反编码地址:"+location.longitude+","+location.latitude);
                    if (location == null) {
                        //没有获取到地点的经纬度则定位到默认位置 天安门116.403963,39.915112
                        locationListener.getLocationForPosition(new LatLng(39.915112, 116.403963));
                    } else {
                        locationListener.getLocationForPosition(new LatLng(location.latitude, location.longitude));
                    }
                }

                @Override
                public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                    LogUtil.i("编码地址:");
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 添加覆盖物
     * @param latLng 经纬度
     * @param mapView mapview
     */
    public void addMarker(LatLng latLng,MapView mapView){
        mapView.getMap().clear();
        //在地图上加点
        MarkerOptions option=new MarkerOptions();
        option.position(latLng);
        //点的样式
        BitmapDescriptor icon= BitmapDescriptorFactory.fromResource(R.drawable.map_location_icon_blue);
        option.icon(icon);
        mapView.getMap().addOverlay(option);
    }

    /**
     * 将地图移动到特定的位置
     * @param latLng  经纬度位置
     * @param mapView mapView对象
     */
    public void moveToLocation(LatLng latLng,MapView mapView){
      try{
          // map view 销毁后不在处理新接收的位置
          if (latLng == null || mapView == null)
              return;
          MyLocationData locData = new MyLocationData.Builder()
                  // 此处设置开发者获取到的方向信息，顺时针0-360
                  .direction(100)
                  .latitude(latLng.latitude)
                  .longitude(latLng.longitude).build();
          mapView.getMap().setMyLocationData(locData);
          LatLng ll = new LatLng(latLng.latitude,
                  latLng.longitude);
          MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, 14.0f);
          mapView.getMap().animateMapStatus(u);
          //并添加覆盖物
          addMarker(latLng,mapView);
      }catch (Exception e){
          e.printStackTrace();
      }
    }
    /**
     * 将地图移动到特定的位置
     * @param latLng  经纬度位置
     * @param mapView mapView对象
     */
    public void moveToLocationByZoom(LatLng latLng,MapView mapView){
       try{
           // map view 销毁后不在处理新接收的位置
           if (latLng == null || mapView == null)
               return;
           MyLocationData locData = new MyLocationData.Builder()
                   // 此处设置开发者获取到的方向信息，顺时针0-360
                   .direction(100)
                   .latitude(latLng.latitude)
                   .longitude(latLng.longitude).build();
           mapView.getMap().setMyLocationData(locData);
           LatLng ll = new LatLng(latLng.latitude,
                   latLng.longitude);
           MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, 19.0f);
           mapView.getMap().animateMapStatus(u);
           //并添加覆盖物
           addMarker(latLng,mapView);
       }catch (Exception e){
           e.printStackTrace();
       }
    }
    /***
     * 获取经纬度位置
     * @return 返回LatLng对象
     */
    public void getLocation(){
        mLocationClient.start();
    }
    //百度地图回调接口
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
           try{
               longitude=location.getLongitude();
               latitude=location.getLatitude();
               LatLng latLng=new LatLng(latitude,longitude);
               if(listener==null){
                   return;
               }
               listener.onSearchFinish(latLng, location.getAddrStr(), location.getProvince(), location.getCity(), location.getDistrict());
           }catch (Exception e){
               e.printStackTrace();
           }
        }
    }
    //初始化百度地图
    private void initMap() {
       try{
           mLocationClient = new LocationClient(CoresunApp.instance);     //声明LocationClient类
           mLocationClient.registerLocationListener(new MyLocationListener());    //注册监听函数

           LocationClientOption option = new LocationClientOption();
           option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
           );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
           option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
           //int span=1000;
           //option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
           option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
           option.setOpenGps(true);//可选，默认false,设置是否使用gps
           option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
           option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
           option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
           option.setIgnoreKillProcess(false);//可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
           option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
           option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要

           mLocationClient.setLocOption(option);
       }catch (Exception e){
           e.printStackTrace();
       }
    }
}
