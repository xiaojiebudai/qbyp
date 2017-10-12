package cn.net.chenbao.qbyp.activity;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.utils.BaiDuLocationUtils;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

public class AgencySellerMapActivity extends FatherActivity implements OnClickListener {

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    // 定位相关
    private BaiDuLocationUtils instance;
    private LatLng latLngNow;
    private double currentLatitude;
    private double currentLongitude;
    private double sellerLatitude;
    private double sellerLongitude;
    private boolean isLocateMe = false;
    protected float zoom;
    private LatLng latlon;

    @Override
    protected int getLayoutId() {
        return R.layout.agency_seller_map;
    }

    @Override
    protected void initValues() {
        initTitle();

    }

    private void initTitle() {
        findViewById(R.id.back).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView center = (TextView) findViewById(R.id.tv_head_center);
        center.setText(R.string.seller_position);
        center.setTextColor(getResources().getColor(R.color.white));
        center.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

    }

    @Override
    protected void initView() {
        sellerLatitude = getIntent().getExtras().getDouble("Latitude");
        sellerLongitude = getIntent().getExtras().getDouble("Longitude");

        mMapView = (MapView) findViewById(R.id.baidumap);
        findViewById(R.id.location).setOnClickListener(this);
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(false);
        // 定位初始化
        instance = new BaiDuLocationUtils(this);
        instance.startLocation();
        instance.setLocationListener(new BaiDuLocationUtils.LocationListener() {

            @Override
            public void LocationMessageCallBack(BDLocation location) {
                // map view 销毁后不在处理新接收的位置
                if (location == null || mMapView == null) {
                    return;
                }
                currentLatitude = location.getLatitude();
                currentLongitude = location.getLongitude();

                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(location.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(100)
                        .latitude((currentLatitude + sellerLatitude) / 2)
                        .longitude((currentLongitude + sellerLongitude) / 2).build();
                mBaiduMap.setMyLocationData(locData);

                if (isLocateMe) {
                    latLngNow = new LatLng(currentLatitude,currentLongitude);
                    mBaiduMap.setMapStatus(MapStatusUpdateFactory
                            .newMapStatus(new MapStatus.Builder()
                                    .target(latLngNow).zoom(18).build()));
                } else {//first into 中间点取经纬度平均值
                    setMapZoom();

                    MarkerOptions overlayOptions1 = new MarkerOptions();
                    overlayOptions1
                            .icon(BitmapDescriptorFactory
                                    .fromResource(R.drawable.icon_marka)).position(
                            new LatLng(currentLatitude, currentLongitude));
//                    overlayOptions1.animateType(MarkerOptions.MarkerAnimateType.grow);
                    mBaiduMap.addOverlay(overlayOptions1);

                    MarkerOptions overlayOptions2 = new MarkerOptions();
                    overlayOptions2
                            .icon(BitmapDescriptorFactory
                                    .fromResource(R.drawable.icon_markb)).position(
                            new LatLng(sellerLatitude, sellerLongitude));//seller 位置
//                    overlayOptions2.animateType(MarkerOptions.MarkerAnimateType.grow);
                    mBaiduMap.addOverlay(overlayOptions2);
                    View view = LayoutInflater.from(AgencySellerMapActivity.this)
                            .inflate(R.layout.location_info, null);
                    ((TextView) view.findViewById(R.id.tv_text_show))
                            .setText(R.string.seller_position);
                    LatLng sellerLatLng = new LatLng(sellerLatitude, sellerLongitude);
                    // infowindow只能显示一个 自己位置就没有显示了
                    mBaiduMap.showInfoWindow(new InfoWindow(view, sellerLatLng, -55));
                }
                instance.stopLocation();
            }

        });
    }

    protected void setMapZoom() {
        int zoomLevel[] = {2000000, 1000000, 500000, 200000, 100000, 50000,
                25000, 20000, 10000, 5000, 2000, 1000, 500, 100, 50, 20, 0};

        final double midlat = (currentLatitude + sellerLatitude) / 2;
        final double midlon = (currentLongitude + sellerLongitude) / 2;
        latlon = new LatLng(midlat, midlon);
        int jl = (int) DistanceUtil.getDistance(new LatLng(currentLatitude,
                currentLongitude), new LatLng(sellerLatitude, sellerLongitude));
        int i;
        for (i = 0; i < 17; i++) {
            if (zoomLevel[i] < jl) {
                zoom = i + 4;
                break;
            }
        }
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(latlon,
                zoom - 1);
        mBaiduMap.animateMapStatus(u);
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

    @Override
    public void onClick(View view) {
        if (instance.isStop()) {
            isLocateMe = true;
            instance.startLocation();
        }

    }
}
