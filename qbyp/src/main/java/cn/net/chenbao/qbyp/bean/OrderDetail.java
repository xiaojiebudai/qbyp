package cn.net.chenbao.qbyp.bean;

import java.util.List;

/**
 * 订单详情
 *
 * @author licheng
 */
public class OrderDetail {

    public long CreateTime;// 属性CreateTime(创建时间)
    public double DeliverFee;// 属性DeliverFee(配送费)
    public List<Goods> Goods;// 商品列表
    public double GoodsAmt;// //属性GoodsAmt(商品金额)
    public long OrderId;// 属性OrderId(订单号)
    public double PackageFee;// 属性PackageFee(包装费)
    public int Quantity;// 属性Quantity(商品数量)
    public long SellerId;// 属性SellerId(商家ID)
    public String SellerName;// 属性SellerName(商家名称)
    public int SendMode;// 属性SendMode(送货方式)
    public int Status;// 属性Status(状态)
    public double TotalAmt;// 属性TotalAmt(应付金额)
    public long UserId;// 属性UserId(会员ID)
    public String UserName;// 属性UserName(会员名)

    public double BalanceAmt;// 余额抵扣金额
    public double BalancePay;// 可用余额支付
    public double InternalAmt;// 可消费积分抵扣金额
    public double InternalPay;// 可用可消费积分
    public double VipAmt;// 专属积分抵扣金额
    public double VipAmt1;// 可用专属积分  到店
    public double VipPay;// 可用专属积分
    public double VipPay1;// 可用专属积分 到店
    public double PayAmt;// 应付金额
    public double FenxiaoConsumeAmt;
    public double FenxiaoConsumeAmtNoDeliver;
    public double FenxiaoConsumePay;
    public double FenxiaoConsumePayNoDeliver;
    public AccountInfo UserAccount;// 会员账户金额

    @Override
    public String toString() {
        return "OrderDetail [CreateTime=" + CreateTime + ", DeliverFee="
                + DeliverFee + ", Goods=" + Goods + ", GoodsAmt=" + GoodsAmt
                + ", OrderId=" + OrderId + ", PackageFee=" + PackageFee
                + ", Quantity=" + Quantity + ", SellerId=" + SellerId
                + ", SellerName=" + SellerName + ", SendMode=" + SendMode
                + ", Status=" + Status + ", TotalAmt=" + TotalAmt + ", UserId="
                + UserId + ", UserName=" + UserName + "]";
    }

}
