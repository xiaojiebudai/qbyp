package cn.net.chenbao.qbyp.bean;

/***
 * Description:消费返现  Company: wangwanglife Version：1.0
 *
 * @author zxj
 * @date 2016-8-4
 */
public class AccountRebateInternal {
    /**
     * 会员ID
     */
    public long UserId;
    /**
     * 流水号
     */
    public long FlowId;
    /**
     * 变化时间
     */
    public long CreateTime;
    /**
     * 备注
     */
    public String Explain;
    /**
     * 变化金额
     */
    public String ChangeAmt;
    /**
     * 类型  0 销售，1提现
     */
    public int BusType;
    /**
     * 业务单号
     */
    public long BusId;
    public double RebateAmt;
    public double BusAmt;//订单金额（现金）
    public double DeblockingRate;//积分比率
    public double DeblockingIntegral;//积分
    public int Status;//状态

    public long SellerId;//商家ID,

}
