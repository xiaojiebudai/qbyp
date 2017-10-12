package cn.net.chenbao.qbyp.bean;

/**
 * Created by 木头 on 2016/11/5.
 */

public class ShopOrderOutline {

    /** 待付款 */
    public static final int STATE_WAIT_PAY = 0;
    /** 待发货 */
    public static final int STATE_WAIT_SELLER_CONFIRM = 1;
    /** 待收货*/
    public static final int STATE_WAIT_BUYER_CONFIRM = 2;
    /** 已完成 */
    public static final int STATE_COMPLETE = 3;
    /** 已取消 */
    public static final int STATE_CANCEL = 4;
    /** 退款中 */
    public static final int STATE_REFUND = 5;
    /** 已关闭 */
    public static final int STATE_CLOSE = 6;
    public String Address;//属性Address(地址)
    public double BalanceAmt;// 属性BalanceAmt(余额抵金额)
    public double BalancePay ;//属性BalancePay(余额支付)
    public int City;// 属性City(市)
    public long ConfirmTime;// 属性ConfirmTime(客户收货时间)
    public String Consignee;// 属性Consignee(收货人姓名)
    public int County;// 属性County(县)
    public long CreateTime;// 属性CreateTime(创建时间)
    public String DeliverName;// 属性DeliverName(物流公司名称)
    public String DeliverNo ;//属性DeliverNo(物流公司)
    public double GoodsAmt;// 属性GoodsAmt(商品金额)
    public int GoodsQuantity;// 商品数量
    public int Quantity;// 商品总数量
    public String ImageUrl ;//商品图片
    public double InternalAmt;// 属性InternalAmt(可消费余额抵金额)
    public double InternalPay;// 属性InternalPay(可消费余额支付)
    public String LogisticsNo;// 属性LogisticsNo(物流单号)
    public long OrderId;// 属性OrderId(订单号)
    public double PayAmt ;//属性PayAmt(待付金额)
    public String PayCode;// 属性PayCode(支付方式)
    public long PayId ;//属性PayId(支付单号)
    public long PayTime ;//属性PayTime(支付时间)
    public double PostFee;// 属性PostFee(邮费)
    public long PreviewId ;//属性PreviewId(preview_id)
    public long ProductId ;//商品ID
    public String ProductName;// 商品名称
    public int Provider ;//属性Provider(省)
    public String ReceiverMobile;// 属性ReceiverMobile(收货人电话)
    public double SalePrice ;//售价
    public long SendTime ;//属性SendTime(发货时间)
    public ShopProductSku Sku;// 销售属性
    public double SourcePrice;// 原价
    public int Status;// 属性Status(状态)
    public int Street ;//属性Street(街道)
    public double TotalAmt ;//属性TotalAmt(订单总金额)
    public String UserExplain;// 属性UserExplain(user_explain)
    public long UserId;// 属性UserId(用户ID)
    public boolean UserJudge;// 属性UserJudge(会员评价标志)
    public String UserName;// 属性UserName(用户名)
    public long VenderId ;//属性VenderId(厂家ID)
    public String VenderName;// 属性VenderName(厂家名称)
    public double VipAmt ;//属性VipAmt(专属积分抵金额)
    public double VipPay;// 属性VipPay(专属积分支付)
    public  boolean IsHaitao;
    public  boolean IsVipLevel;
}
