package cn.net.chenbao.qbypseller.bean;

/**
 * 地理位置
 * 
 * @author licheng
 *
 */
public class Location {
	/** 街道 */
	public String street;
	public String name;
	public double latitudes;// 纬度
	public double Longitudes;// 经度
	@Override
	public String toString() {
		return "Location [street=" + street + ", name=" + name + "]";
	}

}
