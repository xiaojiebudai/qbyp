package cn.net.chenbao.qbyp.bean;

/***
 * Description:AccountInfo  Company: wangwanglife Version：1.0
 *
 * @author zxj
 * @date 2016-7-30
 */
public class AccountInfo {


    public double AgentAccount;// 属性AgentAccount(代理分润余额)
    //余额
    public double Balance;
    //可消费返现
    public String InternalBalance;
    //代理分润累计
    public String RebateAgent;
    //三星分润累计
    public String RebateParent;
    //待返金额
    public String RebateRecommendSeller;
    //会员ID
    public String UserId;
    //旺旺币
    public String Wwb;
    //预返补贴
    public String RebatePending;
    //会员专属余额
    public double VipAccount;
    //静态应该分配金额
    public String StaticAmt;
    //静态已分配金额
    public String StaticAmted;
    //提现额度
    public double CashLimit;

    public double DeblockingAccount;//待解冻金额
    public double DeblockingIntegral;//待解冻积分
    public double TotalIntegral;//总积分
    public double TotalConsume;//线下消费累计
    public double TotalCashConsume;//线下现金支付累计


}
