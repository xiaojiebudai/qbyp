package cn.net.chenbao.qbyp.api;

public class ApiSeller {

	/** 卖家类接口公共字段 */
	public static final String SELLER_JSON = "/Seller/Json/";
	/** 用户信息 */
	public static final String SELLER_API = Api.WW_ONLINE + SELLER_JSON;

	/**
	 * 搜索行业
	 * 
	 * @return
	 */
	public static final String searchTrade() {
		return SELLER_API + "Search";
	}

	/**
	 * 获得商家信息
	 * 
	 * @return
	 */
	public static final String getSeller() {
		return SELLER_API + "SellerGet";
	}

	/**
	 * 获得商家类别
	 * 
	 * @return
	 */
	public static final String getSellerClass() {
		return SELLER_API + "ClassGet";
	}

	/**
	 * 获得商品
	 * 
	 * @return
	 */
	public static final String getGoods() {
		return SELLER_API + "GoodsGet";
	}

	/**
	 * 获得商家评论
	 * 
	 * @return
	 */
	public static final String getShopComment() {
		return SELLER_API + "JudgesGet";
	}

}
