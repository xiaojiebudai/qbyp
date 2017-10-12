package cn.net.chenbao.qbyp.bean;//

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuri on 2016/11/4.
 * Description:订单详情所有bean
 */

public class ShopOrderInfo{


    /** 待付款 */
    public static final int STATE_WAIT_PAY = 0;
    /** 待发货 */
    public static final int STATE_WAIT_SELLER_CONFIRM = 1;
    /** 待收获*/
    public static final int STATE_WAIT_BUYER_CONFIRM = 2;
    /** 已完成 */
    public static final int STATE_COMPLETE = 3;
    /** 已取消 */
    public static final int STATE_CANCEL = 4;
    /** 退款中 */
    public static final int STATE_REFUND = 5;
    /** 已关闭 */
    public static final int STATE_CLOSE = 6;


    public String Address;//地址
    public double BalanceAmt;//余额抵金额
    public double BalancePay;//余额支付
    public int City;//属性City(市)
    public long ConfirmTime;//客户收货时间
    public String Consignee;//收货人姓名
    public int County;//县
    public long CreateTime;//创建时间
    public String DeliverName;//物流公司名称
    public String DeliverNo;//物流公司
    public double GoodsAmt;//商品金额
    public double InternalAmt;//可消费余额抵金额
    public double InternalPay;//可消费余额支付
    public String LogisticsNo;//物流单号
    public long OrderId;//订单号
    public List<ShopOrderLog> OrderLogs;//订单日志
    public double PayAmt;//待付金额
    public String PayCode;//支付方式
    public long PayId;//支付单号
    public long PayTime;//支付时间
    public double PostFee;//邮费
    public long PreviewId;//属性PreviewId(preview_id)
    public List<ShopOrderGoods> Products;//产品列表
    public int Provider;//省
    public String ReceiverMobile;//收货人电话
    public double SalePrice;//售价
    public long SendTime;//发货时间
    public int Status;//状态
    public int Street;//街道
    public double TotalAmt;//订单总金额
    public String UserExplain;//属性UserExplain(user_explain)
    public long UserId;//用户ID
    public boolean UserJudge;//会员评价标志
    public String UserName;//用户名
    public long VenderId;//厂家ID
    public String VenderName;//厂家名称
    public String VenderTel;//厂家电话
    public double VipAmt;//专属积分抵金额
    public double VipPay;//专属积分支付
    public int Quantity;// 商品总数量

    public String CreditNo;// 身份证Id
    public ArrayList<InternalAmtPayItem> InternalPayList;
}
