package cn.net.chenbao.qbypseller.activity;

import cn.net.chenbao.qbypseller.R;
import cn.net.chenbao.qbypseller.bean.Location;
import cn.net.chenbao.qbypseller.utils.BaiDuLocationUtils;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.core.SearchResult;

/**
 * 定位界面
 *
 * @author xl
 * @date 2016-7-29 上午1:28:24
 * @description
 */
public class LocateActivity extends FatherActivity implements
        OnGetGeoCoderResultListener {

    private BaiDuLocationUtils instance;
    private MapView mBaiduMap;
    private BaiduMap map;
    private GeoCoder mSearchCoder = null; // 搜索模块
    private TextView address_info;
    private Location needInfo = new Location();

    @Override
    protected int getLayoutId() {
        return R.layout.act_locate;
    }

    @Override
    protected void initValues() {

    }

    @Override
    protected void initView() {
        initDefautHead(R.string.select_address, true);
        address_info = (TextView) findViewById(R.id.address_info);
        mBaiduMap = (MapView) findViewById(R.id.baidumap);
        map = mBaiduMap.getMap();
        map.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mSearchCoder = GeoCoder.newInstance();
        mSearchCoder.setOnGetGeoCodeResultListener(this);
        map.setOnMapStatusChangeListener(new OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus status) {
            }

            @Override
            public void onMapStatusChangeFinish(MapStatus status) {
                LatLng mCenterLatLng = status.target;
                // 反Geo搜索
                mSearchCoder.reverseGeoCode(new ReverseGeoCodeOption()
                        .location(mCenterLatLng));
            }

            @Override
            public void onMapStatusChange(MapStatus status) {
            }
        });
        instance = new BaiDuLocationUtils(this);
        instance.startLocation();
        instance.setLocationListener(new BaiDuLocationUtils.LocationListener() {

            @Override
            public void LocationMessageCallBack(BDLocation location) {
                // map view 销毁后不在处理新接收的位置
                if (location == null || mBaiduMap == null) {
                    return;
                }
                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(location.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(100).latitude(location.getLatitude())
                        .longitude(location.getLongitude()).build();
                map.setMyLocationData(locData);
                LatLng ll = new LatLng(location.getLatitude(), location
                        .getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll);
                builder.zoom(18);
                map.animateMapStatus(MapStatusUpdateFactory
                        .newMapStatus(builder.build()));
//                needInfo.latitudes = location.getLatitude();
//                needInfo.Longitudes = location.getLongitude();
//                if(location.getPoiList()!=null&&location.getPoiList().size()>0){
//                    needInfo.name =location.getPoiList().get(0).getName();
//                }else{
//                    needInfo.name = location.getLocationDescribe();
//                }
//
//                needInfo.street = location.getStreet();
//                address_info.setText(needInfo.name);

                // 反Geo搜索
                mSearchCoder.reverseGeoCode(new ReverseGeoCodeOption()
                        .location(ll));

                instance.stopLocation();
            }

        });
        findViewById(R.id.ok).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(needInfo.street)) {
                    Intent data = new Intent();
                    data.putExtra("location", JSONObject.toJSONString(needInfo));
                    setResult(RESULT_OK, data);
                    finish();
                }
            }
        });
        findViewById(R.id.location).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if(instance.isStop()){
                    instance.startLocation();
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
        mBaiduMap.onDestroy();
        if (instance != null) {
            instance.stopLocation();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBaiduMap.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBaiduMap.onPause();
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            return;
        }
        try{
            if(result.getPoiList()!=null&&result.getPoiList().size()>0){
                PoiInfo poiInfo=result.getPoiList().get(0);
                needInfo.latitudes = poiInfo.location.latitude;
                needInfo.Longitudes = poiInfo.location.longitude;
                needInfo.name =poiInfo.name;
                needInfo.street = TextUtils.isEmpty(poiInfo.address.trim())?result.getAddress():poiInfo.address;
            }else{
                needInfo.latitudes = result.getLocation().latitude;
                needInfo.Longitudes = result.getLocation().longitude;
                needInfo.name =result.getAddress();
                needInfo.street = result.getAddressDetail().street;
            }

            address_info.setText(needInfo.name);
        }catch (Exception e){

        }

    }
}
