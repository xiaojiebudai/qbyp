package cn.net.chenbao.qbyp.distribution.been;

/**
 * Created by zxj on 2017/4/16.
 *
 * @description （货款/提现/优币/积分明细）
 */

public class DistributionPublicAccount {
    public double Account;// 属性Account(账款)
    public double ConsumeBalance;// 属性ConsumeBalance(积分余额)
    public double ConsumeFrozen;// 属性ConsumeFrozen(consume_frozen)
    public double ConsumeUnfrozen;// 属性ConsumeUnfrozen(未解冻积分)
    public double FrozenRate;// 属性FrozenRate(frozen_rate)
    public double SpeedRate1;// 属性SpeedRate1(speed_rate1)
    public double SpeedRate2 ;//属性SpeedRate2(speed_rate2)
    public long UserId ;//属性UserId(会员ID)
    public int LevelId ;//0消费者，2小代，3总代，4专卖店
    public double WaitSpeed1;// 属性WaitSpeed1(一级加速等待)
    public double WaitSpeed2;// 属性WaitSpeed2(二级加速等待)
//账款
    public long BusId;// 属性BusId(业务单号)
    public int BusType;// 属性BusType(类型)
    public double ChangeAmt;// 属性ChangeAmt(变化金额)
    public long CreateTime;// 属性CreateTime(变化时间)
    public String Explain;// 属性Explain(备注)
//提现

    // ApplyAmt 属性ApplyAmt(申请提现金额)
    public double ApplyAmt;
    /**处理标志 ProcessFlag 0待处理，1通过，2拒绝*/
    public int ProcessFlag;
    /**处理标志 TransferFlag  0待打款，1处理中,2已完成，3被退回(ProcessFlag =1使用)*/
    public int TransferFlag;
    // Poundage 属性Poundage(扣手续费)
    public double Poundage;
    // 用户名
    public String UserName;
    //加速积分
    public double SpeedConsume;
    //当前积分
    public double CurrentConsume;
    // 加速度
    public double SpeedRate;
    public double Consume;
    public int DaysLast;



}
