package cn.net.chenbao.qbyp.bean;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

public class Address {
//	public property LastTime;// 属性LastTime(last_time)
	/**地址*/
	public String Address;
	/**地址ID*/
	public long AddressId;
	/**市*/
	public String City;
	/**收货人*/
	public String Consignee;
	/**县*/
	public String County;
	/**是否默认*/
	public boolean IsDefault;
	/**Latitude*/
	public double Latitude;
	/**Longitude*/
	public double Longitude;
	/**电话*/
	public String Mobile;
	/**省*/
	public String Province;
	/**拼接的地址 */
	public String AddressPre;
	/**性别*/
	public String Sex;
	/**街道*/
	public String Street;
	/**FlowId*/
	public int FlowId;
	/**会员ID*/
	public long UserId;

	//新增身份证
	public String CreditNo;

	public JSONObject toJson() throws JSONException {
		JSONObject localItemObject = new JSONObject();
		localItemObject.put("Address", Address);
		localItemObject.put("AddressId", AddressId);
		localItemObject.put("AddressPre", AddressPre);
		localItemObject.put("UserId", UserId);
		localItemObject.put("FlowId", FlowId);
		localItemObject.put("Street", Street);
		localItemObject.put("Sex", Sex);
		localItemObject.put("Province", Province);
		localItemObject.put("Mobile", Mobile);
		localItemObject.put("Longitude", Longitude);
		localItemObject.put("Latitude", Latitude);
		localItemObject.put("IsDefault", IsDefault);
		localItemObject.put("County", County);
		localItemObject.put("Consignee", Consignee);
		localItemObject.put("City", City);
		return localItemObject;
	}
}
