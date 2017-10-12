package cn.net.chenbao.qbyp.api;

/***
 * Description: 代理
 * 
 * @author ZXJ
 * @date 2016-10-11
 ***/
public class ApiAgency {
	public static final String BASEDATA_JSON = "/Agent/Json/";

	public static final String BASEDATA_API = Api.WW_ONLINE + BASEDATA_JSON;

	/**
	 * 查询单个区域资料 
	 * 
	 * @return
	 */
	public static final String getAgentinfo() {
		return BASEDATA_API + "AgentInfo";
	}
	/**
	 * 查询单个区域认证资料 
	 * 
	 * @return
	 */
	public static final String getAgentRealinfo() {
		return BASEDATA_API + "AgentRealInfo";
	}
	/**
	 * 询单个区费用补扣明细，按时间倒序
	 * 
	 * @return
	 */
	public static final String getChargeDetail() {
		return BASEDATA_API + "ChargeDetail";
	}
	/**
	 * 取区域余额日小计，按日期倒序
	 * 
	 * @return
	 */
	public static final String getAccountDayDetail() {
		return BASEDATA_API + "AccountDayDetail";
	}
	/**
	 * 取区域余额变化明细 
	 * 
	 * @return
	 */
	public static final String getAccountDetail() {
		return BASEDATA_API + "AccountDetail";
	}
	/**
	 *区域信息列表
	 * 
	 * @return
	 */
	public static final String getMyAgents() {
		return BASEDATA_API + "MyAgents";
	}
	/**
	 * 区域首页，区域信息
	 * 
	 * @return
	 */
	public static final String getAgentsInfo() {
		return BASEDATA_API + "AgentsInfo";
	}
	/**
	 * 区域余额列表 
	 * 
	 * @return
	 */
	public static final String getAccountsGet() {
		return BASEDATA_API + "AccountsGet";
	}
	/**
	 *提现申请提交 
	 * 
	 * @return
	 */
	public static final String CashCommit() {
		return BASEDATA_API + "CashCommit";
	}
	/**
	 * 可提现金额 
	 * 
	 * @return
	 */
	public static final String AllowCashAccount() {
		return BASEDATA_API + "AllowCashAccount";
	}
	/**
	 * 取区域提现明细 
	 * 
	 * @return
	 */
	public static final String CashDetail() {
		return BASEDATA_API + "CashDetail";
	}
	/**
	 * 按区域取行业列表 
	 * 
	 * @return
	 */
	public static final String Trades() {
		return BASEDATA_API + "Trades";
	}
	/**
	 * 取区域商家列表
	 * 
	 * @return
	 */
	public static final String Sellers() {
		return BASEDATA_API + "Sellers";
	}

}
