package cn.net.chenbao.qbyp.bean;

import java.util.List;

/**
 * Created by wuri on 2016/11/7.
 */

public class ShopOrderPreview {
    public Address Address;// 收货地址
    public double BalanceAmt;// 余额抵扣金额
    public double BalancePay;// 可用余额支付
    public long[] CartIds;// 购物车ID列表，用于提交订单后删除购物车
    public double GoodsAmt;// 商品总金额
    public double InternalAmt;// 可消费积分抵扣金额
    public double TotalInternalAmt;// 订单中会员专区商品总金额
    public double InternalPay;// 可用可消费积分
    public List<ShopOrderInfo> Orders;// 子订单列表
    public double PayAmt;// 待支付金额
    public double PostFee;// 邮费
    public long PreviewId;// 预览ID，提交时用这个ID提交
    public int Quantity;// 商品总数量
    public double TotalAmt;// 订单总金额
    public AccountInfo UserAccount;// 会员账户金额

    public InternalAmtPayItem[] InternalPayList;// 积分item数组
    public double ConsumeAmt;// 积分item数组


    //新增是否包含海淘产品
    public boolean IsHaitao;
}
