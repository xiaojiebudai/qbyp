package cn.net.chenbao.qbypseller.api;

public class ApiPay {

	/** 支付类接口公共字段 */
	public static final String PAY_JSON = "/Pay/Json/";
	/** 支付信息 */
	public static final String PAY_API = Api.WW_ONLINE_API + PAY_JSON;

	/**
	 * 支付方式获取
	 *
	 * @return
	 */
	public static final String PaymentValid () {
		return PAY_API + "PaymentValid";
	}
	/**
	 * 订单支付
	 *
	 * @return
	 */
	public static final String OrderPayGet() {
		return PAY_API + "OrderPayGet";
	}

}
