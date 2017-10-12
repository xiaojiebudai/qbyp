package cn.net.chenbao.qbypseller.api;

public class ApiSeller {
	/** 卖家类接口公共字段 */
	public static final String SELLER_JSON = "/Seller/Json/";
	/** 用户信息 */
	public static final String SELLER_API = Api.WW_ONLINE_API + SELLER_JSON;

	/**
	 * 商家登录api
	 *
	 * @return
	 */
	public static final String login() {
		return SELLER_API + "Login";
	}

	/**
	 * 商家注册获得验证码
	 *
	 * @return
	 */
	public static final String registerSMS() {
		return SELLER_API + "AppRegisterSms";
	}

	/**
	 * 商家注册
	 *
	 * @return
	 */
	public static final String register() {
		return SELLER_API + "Register";
	}

	/**
	 * 商家上传注册资料
	 *
	 * @return
	 */
	public static final String regInfoUpdate() {
		return SELLER_API + "RegInfoUpdate";

	}


	/**
	 * 商家提交资料审核
	 *
	 * @return
	 */
	public static final String regRealCommit() {
		return SELLER_API + "RegRealCommit";

	}

	/**
	 * 商家实名资料完善
	 *
	 * @return
	 */
	public static final String RealInfoUpdate() {
		return SELLER_API + "RegRealUpdate";

	}

	/**
	 * 续费支付费用获取
	 *
	 * @return
	 */
	public static final String chargeItemGet() {
		return SELLER_API + "ChargeItemGet";
	}

	/**
	 * 请求支付参数
	 *
	 * @return
	 */
	public static final String requestOrder() {
		return SELLER_API + "ChargeSubmit";
	}

	/** 卖家信息 */
	public static final String infoGet() {
		return SELLER_API + "InfoGet";
	}

	/** 商家类别 */
	public static final String classGet() {
		return SELLER_API + "ClassGet";
	}

	/** 更新类别(修改,新增) */
	public static final String classUpdate() {
		return SELLER_API + "ClassUpdate";
	}

	/** 删除类别(删除) */
	public static final String classDelete() {
		return SELLER_API + "ClassDelete";
	}

	/** 商品更新(新增,修改) */
	public static String goodsUpdate() {
		return SELLER_API + "GoodsUpdate";
	}

	/** 根据类别获取商品列表 */
	public static String goodsGet() {
		return SELLER_API + "GoodsGet";
	}

	/** 认证信息 */
	public static String realInfoGet() {
		return SELLER_API + "RealInfoGet";
	}

	/** 配送设置 */
	public static final String infoSendUpdate() {
		return SELLER_API + "InfoSendUpdate";
	}

	/** 停业 */
	public static final String businessStop() {
		return SELLER_API + "BusinessStop";
	}

	/** 营业 */
	public static final String businessStart() {
		return SELLER_API + "BusinessStart";
	}

	/** 修改信息 */
	public static final String infoUpdate() {
		return SELLER_API + "InfoUpdate";
	}

	/**
	 * 设置会员默认地址
	 */
	public static final String getDefaultAddressSet() {
		return SELLER_API + "DefaultAddressSet";
	}

	/**
	 * 取会员默认地址
	 */
	public static final String getDefaultAddressGet() {
		return SELLER_API + "DefaultAddressGet";
	}

	/**
	 * 地址信息提交 post
	 */
	public static final String getAddressSubmit() {
		return SELLER_API + "AddressSubmit";
	}

	/**
	 * 取会员地址
	 */
	public static final String getAddressGet() {
		return SELLER_API + "AddressGet";
	}

	/**
	 * 删除会员地址
	 */
	public static final String getAddressDelete() {
		return SELLER_API + "AddressDelete";
	}

	/**
	 * 银行卡信息提交 POST
	 */
	public static final String getBankSubmit() {
		return SELLER_API + "BankSubmit";
	}

	/**
	 * 取会员银行卡列表
	 */
	public static final String getBanksGet() {
		return SELLER_API + "BanksGet";
	}

	/**
	 * 会员银行卡删除 POST
	 */
	public static final String getBankDelete() {
		return SELLER_API + "BankDelete";
	}

	/***
	 * Description:获取会员资金账号 Company: wangwanglife Version：1.0
	 *
	 * @author ZXJ
	 * @date @2016-7-30
	 * @return
	 ***/
	public static final String getAccountInfo() {
		return SELLER_API + "AccountGet";
	}

	public static final String OrderCurMonthCount() {
		return SELLER_API + "OrderCurMonthCount";
	}

	/**
	 * 申请提现
	 */
	public static final String getCashSubmit() {
		return SELLER_API + "CashSubmit";
	}

	/**
	 * 获取商家余额变化明细
	 *
	 * @return
	 */
	public static final String AccountBalanceDetail() {
		return SELLER_API + "AccountBalanceDetail";
	}

	/**
	 * 提现明细
	 *
	 * @return
	 */
	public static final String CashDetail() {
		return SELLER_API + "CashDetail";
	}

	/**
	 * 获取商家订单明细
	 *
	 * @return
	 */
	public static final String AccountPendingDetail() {
		return SELLER_API + "AccountPendingDetail";
	}

	/**
	 * 获取未读消息,并获得 系统消息
	 *
	 * @return
	 */
	public static final String InterMessageLast() {
		return SELLER_API + "InterMessageLast";
	}
	/**
	 * 获取未读消息数量
	 *
	 * @return
	 */
	public static final String InterMessageCount() {
		return SELLER_API + "InterMessageCount";
	}

	/**
	 * 系统消息
	 *
	 * @return
	 */
	public static final String InterMessage() {
		return SELLER_API + "InterMessage";
	}

	/** 评价列表 */
	public static final String judgesGet() {
		return SELLER_API + "JudgesGet";

	}

	/** 登出 */
	public static final String Logout() {
		return SELLER_API + "Logout";

	}

	/** 更新头像 */
	public static final String infoHeadUpdate() {
		return SELLER_API + "InfoHeadUpdate";
	}

	/** 当月订单数 */
	public static final String orderCurMonthCount() {
		return SELLER_API + "OrderCurMonthCount";
	}

	/** 销售额列表 */
	public static final String SalesList() {
		return SELLER_API + "SalesList";
	}

	/** 更改认证状态*/
	public static String regInfoRewrite() {
		return SELLER_API + "RegInfoRewrite";
	}
	/**
	 * 消息已读
	 *
	 * @return
	 */
	public static final String InterMessageRead() {
		return SELLER_API + "InterMessageRead";
	}
	/**
	 * 修改扣点
	 *
	 * @return
	 */
	public static final String ChangeTradeReate() {
		return SELLER_API + "ChangeTradeReate";
	}


	/***
	 * Description: 取商家可提现金额
	 *
	 * @author ZXJ
	 * @date 2016-10-13
	 * @return
	 ***/
	public static String AllowCashAccount() {
		// TODO Auto-generated method stub
		return SELLER_API + "AllowCashAccount";
	}
	/***
	 * Description: 积分明细
	 *
	 * @author ZXJ
	 * @date 2017年2月14日11:33:30
	 * @return
	 ***/
	public static String AccountIntegralDetail() {
		// TODO Auto-generated method stub
		return SELLER_API + "AccountIntegralDetail";
	}
    /**
     * 修改商家电话
     *
     * @return
     */
    public static final String InfoTelUpdate() {
        return SELLER_API + "InfoTelUpdate";
    }
    /**
     * 修改商家地址
     *
     * @return
     */
    public static final String InfoLocationUpdate() {
        return SELLER_API + "InfoLocationUpdate";
    }
}
