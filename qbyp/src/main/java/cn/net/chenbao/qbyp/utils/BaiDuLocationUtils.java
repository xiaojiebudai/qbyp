package cn.net.chenbao.qbyp.utils;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

/**
 * 百度定位工具类
 * 
 * @author licheng
 * 
 */
public class BaiDuLocationUtils implements BDLocationListener {

	private LocationClient locationClient;
	private LocationListener locationListener;

	private Context context;
	private boolean isStop=true;
	public void init(Context context) {
		locationClient = new LocationClient(context);
		initLocation();
	}
	public BaiDuLocationUtils(Context context) {
		this.context = context;
	}

	/**
	 * 配置定位
	 */
	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
		int span = 1000;
		option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(true);// 可选，默认false,设置是否使用gps
		option.setLocationNotify(true);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		option.setIgnoreKillProcess(true);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
		option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
		option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
		this.isStop = true;
		locationClient.setLocOption(option);
	}

	/**
	 * 开始定位
	 */
	public void startLocation() {
		init(context);
		locationClient.registerLocationListener(this);
		locationClient.start();
		this.isStop = false;
	}

	/**
	 * 停止定位
	 */
	public void stopLocation() {
		if (locationClient != null) {
			locationClient.stop();
			locationClient = null;
			this.isStop = true;
		}
	}
	public boolean isStop() {
		return isStop;
	}
	/**
	 * 定位回调
	 */
	@Override
	public void onReceiveLocation(BDLocation arg0) {
		if (arg0 == null || arg0.getCity() == null) {
//			ZLog.showPost("定位失败，错误类型：" + BDLocation.TypeServerError);
		}
		if (locationListener != null) {
			locationListener.LocationMessageCallBack(arg0);
		}
	}

	public interface LocationListener {
		void LocationMessageCallBack(BDLocation arg0);

	}

	public void setLocationListener(LocationListener locationListener) {
		this.locationListener = locationListener;
	}

}
