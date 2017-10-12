package cn.net.chenbao.qbyp.bean;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

/***
	 * Description:身份认证信息  Company: wangwanglife Version：1.0
	 * 
	 * @author zxj
	 * @date 2016-8-13
     */
public class UserReal {
	/** 反面照片 */
	public String BackPicture;
	/** 认证时间 */
	public long CreateTime;
	/** 证件姓名 */
	public String CredName;
	/** 证件号码 */
	public String CredNo;
	/** 证件类型 */
	public String CredType;
	/** 签发机构 */
	public String IssuingAuthority;
	/** 认证状态 */
	public int Status;
	/** 正面照片 */
	public String FrontPicture;
	/** 有效期 */
	public String ValidityTime;
	/**用户ID*/
	public String UserId;
	/** Explain */
	public String Explain;

	public JSONObject toJson() throws JSONException {
		JSONObject localItemObject = new JSONObject();
		localItemObject.put("BackPicture", BackPicture);
		localItemObject.put("CreateTime", CreateTime);
		localItemObject.put("CredName", CredName);
		localItemObject.put("CredNo", CredNo);
		localItemObject.put("CredType", CredType);
		localItemObject.put("IssuingAuthority", IssuingAuthority);
		localItemObject.put("Status", Status);
		localItemObject.put("FrontPicture", FrontPicture);
		localItemObject.put("ValidityTime", ValidityTime);
		localItemObject.put("UserId", UserId);
		return localItemObject;
	}
}
