package cn.net.chenbao.qbypseller.api;


public class ApiVariable {
	/** 用户类接口公共字段 */
	public static final String VARIABLE_JSON = "/Variable/Json/";
	/** 用户信息 */
	public static final String VARIABLE_API = Api.WW_ONLINE_API + VARIABLE_JSON;
	/**
	 * 关于我们 
	 */
	public static final String AboutUs() {
		return VARIABLE_API + "AboutUs";
	}
	/**
	 * 联系我们 
	 */
	public static final String LinkUs() {
		return VARIABLE_API + "LinkUs";
	}
	/**
	 * 商家注册协议 
	 */
	public static final String SellerProtocol() {
		return VARIABLE_API + "SellerProtocol";
	}
	/**
	 * 用户注册协议 
	 */
	public static final String UserProtocol() {
		return VARIABLE_API + "UserProtocol";
	}
}
