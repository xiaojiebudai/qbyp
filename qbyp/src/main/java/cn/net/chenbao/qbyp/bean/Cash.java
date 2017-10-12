package cn.net.chenbao.qbyp.bean;

/***
 * Description:提现Item Company: wangwanglife Version：1.0
 * 
 * @author zxj
 * @date 2016-7-31
 */
public class Cash {
	// 属性AccountName(账户名)
	public String AccountName;
	// AccountNo 属性AccountNo(账户号)
	public String AccountNo;
	// AdminId 属性AdminId(处理人ID)
	public String AdminId;
	// AdminName 属性AdminName(处理人名称)
	public String AdminName;
	// ApplyAmt 属性ApplyAmt(申请提现金额)
	public double ApplyAmt;
	// BankAddress 属性BankAddress(开户行)
	public String BankAddress;
	// BankName 属性BankName(银行名称)
	public String BankName;
	// BankNo 属性BankNo(银行缩写)
	public String BankNo;
	// CreateTime 属性CreateTime(申请时间)
	public long CreateTime;
	// Explain 属性Explain(备注)
	public String Explain;
	// FlowId 属性FlowId(自增量)
	public String FlowId;
	// PayAmt 属性PayAmt(实际应转金额)
	public double PayAmt;
	// Poundage 属性Poundage(扣手续费)
	public double Poundage;
	/**处理标志 ProcessFlag 0待处理，1通过，2拒绝*/ 
	public int ProcessFlag;
	/**处理标志 TransferFlag  0待打款，1处理中,2已完成，3被退回(ProcessFlag =1使用)*/ 
	public int TransferFlag;
	// ProcessTime 属性ProcessTime(处理时间)
	public long ProcessTime;
	// ReceiptNo 属性ReceiptNo(回单号)
	public String ReceiptNo;
	// UserId 属性UserId(会员ID)
	public String UserId;

}
