package cn.net.chenbao.qbypseller.bean;

/**
 * @author 木头
 *
 */
public class AccountPendingDetail {
	/**帐款金额*/ 
	public double AccAmt;
	/**帐款时间*/ 
	public long CreateTime;
	/**FlowId*/ 
	public long FlowId;
	/**订单金额*/ 
	public double OrderAmt;
	/**订单号*/ 
	public String OrderId;
	/**处理时间*/ 
	public long ProcessTime;
	/**处理标志 ProcessFlag*/ 
	public boolean ProcessFlag;
	/**商家ID*/ 
	public long SellerId;
	/**扣点*/ 
	public double TradeRate;
	/**会员ID*/ 
	public long UserId;
}
