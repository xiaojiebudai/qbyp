package cn.net.chenbao.qbypseller.utils;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiSearch;
import cn.net.chenbao.qbypseller.R;

/**
 * 百度地图工具类
 * 
 * @author licheng
 *
 */
public class BaiDuMapUtils {

	/**
	 * 在地图手动设置定位点
	 */
	public static void setLoactionToMap(BaiduMap map, int level, LatLng latLng) {
		BitmapDescriptor fromResource = BitmapDescriptorFactory
				.fromResource(R.drawable.address_gray);// 定位标志
		OverlayOptions markerOptions = new MarkerOptions().position(latLng)
				.icon(fromResource);
		map.setMapStatus(MapStatusUpdateFactory.zoomTo(level));
		map.addOverlay(markerOptions);
		map.setMapStatus(MapStatusUpdateFactory.newLatLng(latLng));
	}

	/**
	 * 无标记,动态设置点
	 * 
	 * @param map
	 * @param latLng
	 */
	public static void setloactionToMap(BaiduMap map, LatLng latLng) {
		MapStatusUpdate newLatLng = MapStatusUpdateFactory.newLatLng(latLng);
		map.setMapStatus(newLatLng);
	}

	/**
	 * 搜索poi兴趣地点
	 */
	public static void getBaiduPoiAddress(PoiSearch poiSearch,
			OnGetPoiSearchResultListener listener, String city, String keyword) {
		PoiCitySearchOption poi = new PoiCitySearchOption().city(city)
				.keyword(keyword).pageNum(10);
		poiSearch.setOnGetPoiSearchResultListener(listener);
		poiSearch.searchInCity(poi);
	}

}
