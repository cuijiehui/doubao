package com.xinspace.csevent.ui.activity;

public class RegetAddressActivity extends BaseActivity{
       // implements SearchAddressFinishListener,SearchLocationListener,OnGetSuggestionResultListener,OnGetGeoCoderResultListener
//    private static final int REQUEST_CODE_FOR_CITY = 300;//选择城市的请求码
//    private LinearLayout ll_search;
//    private LinearLayout ll_re_locate;
//    private LinearLayout ll_back;
//
//    private AutoCompleteTextView et_search_text;
//    private TextView tv_address;
//    private MapView mapView;
//    private Button btSecCities;
//    private BdMapUtil map;
//    private String address;
//    private String district;
//    private String city;//自选城市
//    private String currentCity;//当前位置城市
//    private ImageView ivSearch;
//    private String sugCity;
//    private String dis;
//    private BaiduMap mBaiduMap;
//    private GeoCoder mSearch;
//    private String theMoveProvince;
//    private String theMoveCity;
//    private String theMoveDistrict;
//    private String theMoveStreet;
//    private String theMovestreetNumber;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_reget_address);
//        setView();
//        setListener();
//        getLocation();
//
//        // 初始化搜索模块，注册事件监听
//        mSearch = GeoCoder.newInstance();
//        mSearch.setOnGetGeoCodeResultListener(this);
//
//        mBaiduMap=mapView.getMap();
//
//        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
//            @Override
//            public void onMapStatusChangeStart(MapStatus mapStatus) {
//            }
//
//            @Override
//            public void onMapStatusChange(MapStatus mapStatus) {
//            }
//
//            @Override
//            public void onMapStatusChangeFinish(MapStatus mapStatus) {
//                LatLng ptCenter = mBaiduMap.getMapStatus().target; //获取地图中心点坐标
//                // 反Geo搜索
//                mSearch.reverseGeoCode(new ReverseGeoCodeOption()
//                        .location(ptCenter));
//            }
//        });
//
//
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mapView.onDestroy();
//    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mapView.onResume();
//    }
//    @Override
//    protected void onPause() {
//        super.onPause();
//        mapView.onPause();
//    }
//    //获取位置
//    private void getLocation() {
//        map=new BdMapUtil();
//        map.setOnAddressFinishListener(this);
//        map.getLocation();
//    }
//    //设置监听器
//    private void setListener() {
//        //搜索地址
//        ll_search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                searchAddress();
//            }
//        });
//        //点击定位按钮
//        ll_re_locate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getLocation();
//            }
//        });
//        //点击进入选择城市页面
//        btSecCities.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(RegetAddressActivity.this, SelectCitiesActivity.class);
//                intent.putExtra("data",address);
//                intent.putExtra("district",district);
//                startActivityForResult(intent, REQUEST_CODE_FOR_CITY);
//            }
//        });
//
//        //点击返回
//        ll_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.putExtra("theMoveProvince", theMoveProvince);
//                intent.putExtra("theMoveCity", theMoveCity);
//                intent.putExtra("theMoveDistrict", theMoveDistrict);
//                intent.putExtra("theMoveStreet", theMoveStreet);
//                intent.putExtra("theMovestreetNumber", theMovestreetNumber);
//                setResult(RESULT_OK,intent);
//                finish();
//            }
//        });
//
//        //模糊搜索
//        et_search_text.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                searchAddress();
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });
//        //点击搜索地址
//        ivSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String pos = et_search_text.getText().toString();
//                if (TextUtils.isEmpty(pos)) {
//                    et_search_text.setError("地址不能为空!!");
//                    return;
//                }
//                moveMapToPos(pos);
//            }
//        });
//    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        //回传一个城市的数据
//        if(requestCode==REQUEST_CODE_FOR_CITY&&resultCode==RESULT_OK){
//            city=data.getStringExtra("city");
//
//            //将百度地图移动到该位置
//            moveMapToCity(city);
//        }
//    }
//    //选择城市后移动地图到该区
//    private void moveMapToCity(String city) {
//        map.setOnSearchLocationListener(this);
//        map.getLocationByCity(city);
//    }
//    //选择地点后移动地图到地点
//    private void moveMapToPos(String pos) {
//        map.setOnSearchLocationListener(this);
//        map.getLocationByAddress(pos, sugCity + dis);
//    }
//    //模糊查询地址
//    private void searchAddress() {
//        SuggestionSearch search=SuggestionSearch.newInstance();
//        SuggestionSearchOption option=new SuggestionSearchOption();
//        String key=et_search_text.getText().toString();
//        option.keyword(key);
//        //如果城市为空,则默认选择现在定位的城市
//        if(city==null){
//            city=currentCity;
//        }
//        option.city(city);
//        search.requestSuggestion(option);
//        search.setOnGetSuggestionResultListener(this);
//    }
//    //初始化组件
//    private void setView() {
//        ll_re_locate= (LinearLayout) findViewById(R.id.ll_re_location);
//        ll_search= (LinearLayout) findViewById(R.id.ll_search_address);
//        ll_back= (LinearLayout) findViewById(R.id.ll_address_back);
//
//        et_search_text= (AutoCompleteTextView) findViewById(R.id.et_reget_search_text);
//        tv_address= (TextView) findViewById(R.id.tv_reget_address);
//        btSecCities= (Button) findViewById(R.id.bt_relative_select_cities);
//        ivSearch = (ImageView) findViewById(R.id.iv_regaddress_search);
//        mapView= (MapView) findViewById(R.id.mv_reget_mapView);
//        mapView.showZoomControls(false);
//    }
//    //百度地图搜索建议
//    @Override
//    public void onGetSuggestionResult(SuggestionResult suggestionResult) {
//        List<SuggestionResult.SuggestionInfo> list = suggestionResult.getAllSuggestions();
//        //提示建议的集合
//        if (list!=null) {
//            String[] suggest = new String[list.size()];
//            for (int i = 0; i < list.size(); i++) {
//                SuggestionResult.SuggestionInfo sug = list.get(i);
//                sugCity = sug.city;
//                dis = sug.district;
//                String key = sug.key;
//                LogUtil.i("信息:"+city+dis+key);
//                suggest[i] =  key;
//            }
//            for (int i = 0; i < suggest.length; i++) {
//                LogUtil.i("建议地址:" +suggest[i]);
//            }
//            ArrayAdapter adapter = new ArrayAdapter(this, R.layout.item_regaddress_autocomplete_list, suggest);
//            et_search_text.setAdapter(adapter);
//            et_search_text.showDropDown();//把数据显示在AutocompleteTextView中
//        }
//    }
//    //反编译地址的回调方法
//    @Override
//    public void getLocationForPosition(LatLng latLng) {
//        LogUtil.i("经纬度数据已经回传:" + latLng.longitude + ";" + latLng.latitude);
//        if (latLng.longitude==116.403963 && latLng.latitude==39.915112){//默认地址
//            //没有找到地址
//            ToastUtil.makeToast("没有找到您需要的地址!");
//            return;
//        }
//        map.moveToLocationByZoom(latLng,mapView);
//    }
//    //选择城市编译的经纬度回调
//    @Override
//    public void getLocationForCity(LatLng latLng) {
//        LogUtil.i("经纬度数据已经回传:" + latLng.longitude + ";" + latLng.latitude);
//        if (latLng.longitude==116.403963 && latLng.latitude==39.915112){//默认地址
//            //没有找到地址
//            ToastUtil.makeToast("没有找到您需要的地址!");
//            return;
//        }
//        map.moveToLocation(latLng, mapView);
//    }
//    //定位的回调方法
//    @Override
//    public void onSearchFinish(LatLng latLng, String address, String province, String city, String district) {
//        tv_address.setText(address);
//        //移动地图,添加覆盖物
//        map.moveToLocationByZoom(latLng, mapView);
//        map.addMarker(latLng, mapView);
//
//        if(address.contains("中国")){
//            address=address.replace("中国","");
//        }
//        this.address=address;
//        this.district=district;
//        currentCity = city;
//    }
//
//    @Override
//    public void onGetGeoCodeResult(GeoCodeResult result) {
//
//    }
//    /**地址搜索回调接口-反geo*/
//    @Override
//    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
//        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
//            Toast.makeText(RegetAddressActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
//                    .show();
//            return;
//        }
//        //mBaiduMap.clear();
//        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
//                .getLocation()));//改变地图状态？
//        theMoveProvince = result.getAddressDetail().province;
//        theMoveCity = result.getAddressDetail().city;
//        theMoveDistrict = result.getAddressDetail().district;
//        theMoveStreet = result.getAddressDetail().street;
//        theMovestreetNumber = result.getAddressDetail().streetNumber;
//        Toast.makeText(RegetAddressActivity.this, result.getAddressDetail().province+"   "+result.getAddressDetail().city + "  "+
//                        result.getAddressDetail().district +"  "+ result.getAddressDetail().street +
//                        result.getAddressDetail().streetNumber,
//                Toast.LENGTH_SHORT).show();
//    }

}
