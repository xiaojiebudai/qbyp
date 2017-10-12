package cn.net.chenbao.qbypseller.bean;

/**
 * 支付费用
 * 
 * @author licheng
 *
 */
public class Charge {

	public double Account;// 属性Account(收费金额)

	public int DelayDay;// 属性DelayDay(延长时间(天))

	public long ExpireDate;

	public boolean IsTry;// 属性IsTry(是否为试用项目)

	public int ItemId;// 属性ItemId(项目ID)

	public String ItemName;// 属性ItemName(项目名称)

	public boolean Status;// 属性Status(状态)

	@Override
	public String toString() {
		return "Charge [Account=" + Account + ", DelayDay=" + DelayDay
				+ ", ExpireDate=" + ExpireDate + ", IsTry=" + IsTry
				+ ", ItemId=" + ItemId + ", ItemName=" + ItemName + ", Status="
				+ Status + "]";
	}

}
