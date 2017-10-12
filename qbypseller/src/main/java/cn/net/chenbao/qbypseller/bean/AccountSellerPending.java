package cn.net.chenbao.qbypseller.bean;

/***
	 * Description:商家帐款明细  Company: wangwanglife Version：1.0
	 * 
	 * @author zxj
	 * @date 2016-8-10
     */
public class AccountSellerPending {
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
	/**处理标志 ProcessFlag 0待处理，1通过，2拒绝*/ 
	public int ProcessFlag;
	/**处理标志 TransferFlag  0待打款，1处理中,2已完成，3被退回(ProcessFlag =1使用)*/ 
	public int TransferFlag;
	/**商家ID*/ 
	public long SellerId;
	/**扣点*/ 
	public double TradeRate;
	/**会员ID*/ 
	public long UserId;
	
	/**申请提现金额*/ 
	public double ApplyAmt;
	/**BusType*/ 
	public int BusType;
	/**BusId*/ 
	public String BusId;
	/**ChangeAmt*/ 
	public double ChangeAmt;
	/**Explain*/ 
	public String Explain;
	
	/**UserName*/ 
	public String UserName;
	/**TotalAmt*/ 
	public double TotalAmt;
	
	

}
