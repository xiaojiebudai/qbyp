package cn.net.chenbao.qbyp.bean;

/**
 * Created by ppsher on 2017/2/15.
 */

public class OfflineOrder {
    public long OrderId;//订单号,
    public String UserId;//会员ID
    public String UserName;//会员名
    public String UserHeadUrl;//会员头像
    public String SellerHeadUrl;//商家店铺头像
    public long SellerId;//商家ID
    public String SellerName;//商家名称
    public int Status;//状态
    public double OrderAmt;//订单金额
    public double PersentIntegralRate;//商家赠送积分比率
    public String PersentIntegral;//商家赠送积分
    public double SellerRlRate;//让利比率
    public double SellerRlAmt;//让利金额
    public String PayCode;//付款方式
    public long PayId;//付款单号
    public long PayTime;//付款时间,
    public long CreateTime;//创建时间
    public AccountInfo SellerAccount;//商家资金帐户
}
