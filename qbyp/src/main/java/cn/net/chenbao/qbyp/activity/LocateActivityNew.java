package cn.net.chenbao.qbyp.activity;

import java.util.ArrayList;
import java.util.List;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.adapter.listview.LocationAdapter;
import cn.net.chenbao.qbyp.bean.Location;
import cn.net.chenbao.qbyp.utils.BaiDuLocationUtils;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.utils.ZLog;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;

/**
 * 定位界面
 *
 * @author xl
 * @date 2016-7-29 上午1:28:24
 * @description
 */
public class LocateActivityNew extends FatherActivity implements
        OnGetPoiSearchResultListener, OnGetGeoCoderResultListener {

    private ListView mListView, listview_address_search;
    private LinearLayout ll_map;
    private LocationAdapter mAdapter, mAdapter_search;
    // 定位相关
    private BaiDuLocationUtils instance;
    MapView mMapView;
    BaiduMap mBaiduMap;
    private PoiSearch poiInstance;
    private String city = "深圳";
    private EditText mSearch;// 搜索
    private List<Location> mList = new ArrayList<Location>();
    private List<Location> mList_search = new ArrayList<Location>();
    private LatLng latLngNow;

    /**
     * 定位SDK监听函数
     */

    @Override
    protected int getLayoutId() {
        return R.layout.act_locate_new;
    }

    @Override
    protected void initValues() {
        poiInstance = PoiSearch.newInstance();
        poiInstance.setOnGetPoiSearchResultListener(this);
        initHeadBack();
        initTextHeadRigth(R.string.cancel, new View.OnClickListener() {

            @Override
            public void onClick(View v) {// 搜索
                ll_map.setVisibility(View.VISIBLE);
                listview_address_search.setVisibility(View.GONE);
                hideSoftKeyboard();
            }
        });

    }

    private GeoCoder mSearchCoder = null; // 搜索模块

    @Override
    protected void initView() {
        mMapView = (MapView) findViewById(R.id.baidumap);
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(false);
        // 定位初始化
        instance = new BaiDuLocationUtils(this);
        instance.setLocationListener(new BaiDuLocationUtils.LocationListener() {

            @Override
            public void LocationMessageCallBack(BDLocation location) {
                // map view 销毁后不在处理新接收的位置
                if (location == null || mMapView == null) {
                    return;
                }
                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(location.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(100).latitude(location.getLatitude())
                        .longitude(location.getLongitude()).build();
                mBaiduMap.setMyLocationData(locData);
                latLngNow = new LatLng(location.getLatitude(), location
                        .getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(latLngNow);
                builder.zoom(18);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory
                        .newMapStatus(builder.build()));
                city = location.getCity();
                instance.stopLocation();
            }

        });
        mSearch = (EditText) findViewById(R.id.edt_search);
        mSearch.setHint(R.string.symbol);
        ll_map = (LinearLayout) findViewById(R.id.ll_map);
        mListView = (ListView) findViewById(R.id.listview_address);
        listview_address_search = (ListView) findViewById(R.id.listview_address_search);
        WWViewUtil.setEmptyView(mListView);
        WWViewUtil.setEmptyView(listview_address_search);
        mAdapter = new LocationAdapter(this, LocationAdapter.MAP);
        mAdapter_search = new LocationAdapter(this, LocationAdapter.SEARCH);
        mListView.setAdapter(mAdapter);
        listview_address_search.setAdapter(mAdapter_search);
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent data = new Intent();
                data.putExtra("location",
                        JSONObject.toJSONString(mAdapter.getItem(position)));
                setResult(RESULT_OK, data);
                finish();
            }
        });
        listview_address_search
                .setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Intent data = new Intent();
                        data.putExtra("location",
                                JSONObject.toJSONString(mAdapter_search
                                        .getItem(position)));
                        setResult(RESULT_OK, data);
                        finish();
                    }
                });
        mSearchCoder = GeoCoder.newInstance();
        mSearchCoder.setOnGetGeoCodeResultListener(this);
        mBaiduMap.setOnMapStatusChangeListener(new OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus status) {
                // updateMapState(status);
            }

            @Override
            public void onMapStatusChangeFinish(MapStatus status) {
                latLngNow = status.target;
                // 反Geo搜索
                mSearchCoder.reverseGeoCode(new ReverseGeoCodeOption()
                        .location(latLngNow));
            }

            @Override
            public void onMapStatusChange(MapStatus status) {
                // updateMapState(status);
            }
        });
        Location locationStart = JSON.parseObject(
                getIntent().getStringExtra(Consts.KEY_DATA), Location.class);
        if (locationStart != null) {
            city = locationStart.city;
            latLngNow = new LatLng(locationStart.latitudes,
                    locationStart.Longitudes);
            if (latLngNow == null || mMapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder().accuracy(10)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(latLngNow.latitude)
                    .longitude(latLngNow.longitude).build();
            mBaiduMap.setMyLocationData(locData);
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(latLngNow);
            builder.zoom(18);
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory
                    .newMapStatus(builder.build()));
            // 反Geo搜索
            mSearchCoder.reverseGeoCode(new ReverseGeoCodeOption()
                    .location(latLngNow));
        } else {
            instance.startLocation();
        }
        findViewById(R.id.location).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if(instance.isStop()){
                    instance.startLocation();
                }
            }
        });
        mSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    listview_address_search.setVisibility(View.VISIBLE);
                    ll_map.setVisibility(View.GONE);

                } else {
                    ll_map.setVisibility(View.VISIBLE);
                    listview_address_search.setVisibility(View.GONE);
                    hideSoftKeyboard();
                }
                String searchAddress = s.toString().trim();
                if (searchAddress == null) {
                    WWToast.showShort(R.string.input_can_not_null);
                    return;
                }
                ZLog.showPost("sds" + city);
                // // 搜索市区
                try {
                    poiInstance.searchInCity((new PoiCitySearchOption()).city(city)
                            .keyword(searchAddress).pageNum(0));
                } catch (IllegalArgumentException exception) {
                    ZLog.showPost("city" + "为null");
                }

            }
        });
    }

    @Override
    protected void doOperate() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        poiInstance.destroy();
        if(instance!=null){
            instance.stopLocation();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    private class MyPoiOverlay extends PoiOverlay {

        public MyPoiOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public boolean onPoiClick(int index) {
            super.onPoiClick(index);
            // PoiInfo poi = getPoiResult().get(index);
            // Button button = new Button(getApplicationContext());
            // button.setBackgroundResource(R.drawable.pay_success);
            // // 定义用于显示该InfoWindow的坐标点
            // LatLng pt = poi.location;
            // // 创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
            // InfoWindow mInfoWindow = new InfoWindow(button, pt, -47);
            // // 显示InfoWindow
            // mBaiduMap.showInfoWindow(mInfoWindow);
            return true;
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult arg0) {

    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult arg0) {

    }

    /**
     * 百度地图地理位置检索
     */
    @Override
    public void onGetPoiResult(PoiResult arg0) {
        if (arg0.error == SearchResult.ERRORNO.NO_ERROR) {
            mList_search.clear();
            List<PoiInfo> allPoi = arg0.getAllPoi();
            for (int i = 0; i < allPoi.size(); i++) {
                try{
                    Location location2 = new Location();
                    location2.name = allPoi.get(i).name;
                    location2.city = allPoi.get(i).city;
                    location2.street = allPoi.get(i).address;
                    location2.latitudes = allPoi.get(i).location.latitude;
                    location2.Longitudes = allPoi.get(i).location.longitude;
                    mList_search.add(location2);
                }catch (NullPointerException e){
                }
            }
            mAdapter_search.setDatas(mList_search);
        } else {
            // WWToast.showShort(R.string.no_data);
        }

    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult result) {
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            return;
        }

        setMarkAndPoiIfo(result.getAddressDetail().city, result.getAddress(),
                result.getLocation(), result.getPoiList());
    }

    private void setMarkAndPoiIfo(String city, String address, LatLng location,
                                  List<PoiInfo> poiList) {
        this.city = city;
        if (mList.size() > 0) {
            mList.clear();
        }
        mBaiduMap.clear();

        PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
        mBaiduMap.setOnMarkerClickListener(overlay);
        overlay.setData(poiList);
        overlay.addToMap();

        Location locationitem = new Location();// 自己当前位置组装一个,百度地图自己位置,从定位信息里拿
        locationitem.street = address;
        locationitem.city = city;
        locationitem.name = address;
        locationitem.Longitudes = location.longitude;
        locationitem.latitudes = location.latitude;
        mList.add(locationitem);

        List<PoiInfo> allPoi = poiList;
        if (allPoi != null && allPoi.size() > 0) {
            for (int i = 0; i < allPoi.size(); i++) {
                Location location2 = new Location();
                location2.name = allPoi.get(i).name;
                location2.city = city;
                location2.street = allPoi.get(i).address;
                location2.latitudes = allPoi.get(i).location.latitude;
                location2.Longitudes = allPoi.get(i).location.longitude;
                mList.add(location2);
            }
            mAdapter.setDatas(mList);
        } else {
            // WWToast.showShort(R.string.no_data);
        }
    }
}
