package cn.net.chenbao.qbypseller.api;

public class ApiTrade extends Api {
	public static final String TRADE_JSON = "/Trade/Json/";
	public static final String TRADE_API = WW_ONLINE_API + TRADE_JSON;

	/** 訂單列表 */
	public static final String ordersGet() {
		return TRADE_API + "OrdersGet";
	}

	/** 訂單详情 */
	public static final String orderInfo() {
		return TRADE_API + "OrderInfo";
	}

	/** 确认接单 */
	public static final String orderSellerAccept() {
		return TRADE_API + "OrderSellerAccept";
	}

	/** 订单数 */
	public static final String orderStatusCount() {
		return TRADE_API + "OrderStatusCount";
	}
}
