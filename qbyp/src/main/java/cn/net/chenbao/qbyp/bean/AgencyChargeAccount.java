package cn.net.chenbao.qbyp.bean;

/***
 * Description:代理账户（还款，提现）  Company: jsh Version：1.0
 * 
 * @author ZXJ
 * @date 2016-10-11
 ***/
public class AgencyChargeAccount {
	//还款
	 public int AgentId;
	 public double ArrearAmt;
	 public double CashAmt;
	 public long CashId;
	 public long CreateTime;
	 public long TransferTime;
	 public double OperAmt;
	 public double ApplyAmt; 
	 public int ProcessFlag; 
	 public int TransferFlag;

	public double AgentAmt;
	public double Poundage;
	public double PayAmt;
	public boolean isOpen;
}
