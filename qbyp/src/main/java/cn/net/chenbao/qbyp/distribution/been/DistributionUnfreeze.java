package cn.net.chenbao.qbyp.distribution.been;

/**
 * Created by zxj on 2017/4/16.
 *
 * @description 解冻
 */

public class DistributionUnfreeze {
    public double Consume;// 属性Consume(积分值)
    public double ConsumeAlready ;//属性ConsumeAlready(已解冻积分)
    public double ConsumeGive ;//属性ConsumeAlready(已解冻积分)
    public long CreateTime ;//属性CreateTime(创建时间)
    public long FinishTime ;//属性FinishTime(完成时间或者预计完成时间)
    public long FlowId;// 属性FlowId(流水ID)
    public double SpeedAmt1;// 属性SpeedAmt1(一级加速积分)
    public double SpeedAmt2;// 属性SpeedAmt2(二级加速积分)
    public int Status;// 属性Status(状态)
    public int FinishDays;// 预计完成时间
    public long UserId;// 属性UserId(用户ID)
}
