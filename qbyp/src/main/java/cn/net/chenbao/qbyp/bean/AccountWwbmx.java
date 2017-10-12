package cn.net.chenbao.qbyp.bean;

/***
	 * Description:旺旺币 /消费返现/分润 Company: wangwanglife Version：1.0
	 * 
	 * @author zxj
	 * @date 2016-8-4
     */
public class AccountWwbmx {
	/**会员ID*/
	public long UserId;
	/**流水号*/
	public long FlowId;
	/**变化时间*/
	public long CreateTime;
	/**处理时间*/
	public long ProcessTime;
	/**处理标志*/
	public boolean ProcessFlag;
	/**备注*/
	public String Explain;
	/**去向*/
	public String Gone;
	/**订单金额*/
	public String OrderAmt;
	/**订单号*/
	public String OrderId;
	/**返币数量*/
	public String RebateAmt;
	/**RebateType*/
	public String RebateType;
	/**消费者ID*/
	public String SaleId;
	public String SellerName;
	public String SellerId;

}
