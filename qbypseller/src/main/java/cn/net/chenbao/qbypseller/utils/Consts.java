package cn.net.chenbao.qbypseller.utils;

/**
 * 常量
 * 
 * @author xl
 * @date:2016-7-27下午5:26:32
 * @description
 */
public class Consts {

	/** 模块 */
	public static final String KEY_MODULE = "module";
	/** 数据 */
	public static final String KEY_DATA = "data";

	/** 模式 */
	public static final String KEY_MODE = "mode";
	/** 状态 */
	public static final String KEY_STATE = "state";
	/** 微信支付 */
	public static final String WX_PAY = "WXPAY";
	/** 支付宝 */
	public static final String ALI_PAY = "ALIPAY";
	/** 银联 */
	public static final String UN_PAY = "UNIONPAY";
	/** 可消费返现支付 */
	public static final String INTER_PAY = "InterPay";
	/** 余额支付 */
	public static final String BALAN_PAY = "BalanPay";
    /** 商家余额支付 */
	public static final String SEL_ACC = "SelAcc";
	/** 网关支付 */
	public static final String GHTNET_PAY = "GhtNet";
	/**
	 * 富有支付
	 */
	public static final String FuYou = "FuYou";
	/**线下 积分支付 */
	public static final String INTEGRAL_PAY = "IntegPay";
	/**本地积分支付 (个人版支付方式 在商家版用于展示)*/
	public static final String LOCAL_INTEGRAL_PAY = "VipPay";
	/**分销货款*/
	public static final String FxAcc = "FxAcc";
	/**分销积分(优币)*/
	public static final String FxVip = "FxVip";
	/** KEY session */
	public static final String KEY_SESSIONID = "sessionId";
	/** 标题 */
	public static final String TITLE = "title  ";

	/** 刷新 */
	public static final String KEY_REFRESH = "refresh";
	/** 位置 */
	public static final String KEY_POSITION = "position";

	/** 类目 */
	public static final String KET_CATEGORY = "category";
	public static final String KEY_SESSION_ERROR = "sission_error";

	/** 数据2 */
	public static final String KEY_DATA_TWO = "data_two";
	/**flag*/
	public static final String KEY_FLAG = "flag";



	//业务类型
	/**
	 * 商家续费,开通服务
	 */
	public final static int SELLER_RENEW_PAY_WAY = -1;
//	public final static int ORDER_PAY_WAY = 1;
//	public final static int RECHARGE_PAY_WAY = 2;
//	public final static int SELF_ORDER_PAY_WAY = 3;
	/** 线下收款*/
	public final static int OFFLINE_COLLECTION_PAY_WAY = 4;
//	public final static int SERVICE_UPDATE_PAY_WAY = 5;
//	public final static int FENXIAO_PAY_WAY = 6;
}
