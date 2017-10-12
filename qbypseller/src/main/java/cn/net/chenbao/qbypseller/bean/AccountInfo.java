package cn.net.chenbao.qbypseller.bean;

/***
 * Description:AccountInfo Company: wangwanglife Version：1.0
 * 
 * @author zxj
 * @date 2016-7-30
 */
public class AccountInfo {
	// 属性SellerId
	public String SellerId;
	// 会员ID
	public String UserId;
	// 商家余额
	public double Balance;
	// 商家待结帐款
	public double Account;
	public double	Consume;//积分余额
	public double	ConsumeSum;//积分总额
	public double	DeblockRate;//解冻速度,
	public double	DeblockAmt;// 已解冻积分
	public double	IntegralUndeblock;// 待解冻积分

}
