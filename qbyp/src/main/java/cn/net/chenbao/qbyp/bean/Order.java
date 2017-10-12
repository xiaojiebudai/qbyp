package cn.net.chenbao.qbyp.bean;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.utils.Consts;

import java.util.List;

/**
 * 单个订单,或总订单(订单详情)
 *
 * @author licheng
 */
public class Order {
    /**
     * 待付款
     */
    public static final int STATE_WAIT_PAY = 0;
    /**
     * 待商家确认
     */
    public static final int STATE_WAIT_SELLER_CONFIRM = 1;
    /**
     * 待会员确认
     */
    public static final int STATE_WAIT_BUYER_CONFIRM = 2;
    /**
     * 已完成
     */
    public static final int STATE_COMPLETE = 3;
    /**
     * 已取消
     */
    public static final int STATE_CANCEL = 4;
    /**
     * 退款中
     */
    public static final int STATE_REFUND = 5;
    /**
     * 已关闭
     */
    public static final int STATE_CLOSE = 6;
    public static final int GO_SHOP = 0;
    public static final int PEISONG = 1;

    public String Address;
    public String Barcode;
    public long City;
    public long County;
    public long CreateTime;// 创建时间
    public double DeliverFee;// 配送费
    public double GoodsAmt;// 商品总价
    public long GoodsId;
    public String GoodsImg;
    public String GoodsName;
    public String Mobile;// 电话
    public long OrderId;// 订单号
    public double PackageFee;// 打包费
    public long Provice;
    public int Quantity;// 总数量
    public String ReceiverName;// 收货人姓名
    public long SellerId;
    public String SellerName;// 商家姓名
    public int SendMode;// 送货方式
    public String SendTime;// 配送时间
    public int Status;
    public double TotalAmt;// 总价
    public String UserExplain;// 描述
    public long UserId;// 用户id
    public String UserName;// 用户名
    public List<Goods> Goods;
    public double PayAmt;
    public double BalanceAmt;
    public double BalancePay;
    public double InternalAmt;
    public double InternalPay;
    public double VipAmt;
    public double VipPay;
    public double ConsumePay;
    public double PersentIntegral;
    public double FenxiaoConsumeAmt;//分销U币
    public double FenxiaoConsumePay;//分销U币  (1.05:1)
    /**
     * 评价订单
     */
    public String PayCode;// 属性PayCode(付款方式)
    public long PayId;// 属性PayId(付款单号)
    public long PayTime;// 属性PayTime(付付款时间)

    @Override
    public String toString() {
        return "Order [Address=" + Address + ", Barcode=" + Barcode + ", City="
                + City + ", County=" + County + ", CreateTime=" + CreateTime
                + ", DeliverFee=" + DeliverFee + ", GoodsAmt=" + GoodsAmt
                + ", GoodsId=" + GoodsId + ", GoodsImg=" + GoodsImg
                + ", GoodsName=" + GoodsName + ", Mobile=" + Mobile
                + ", OrderId=" + OrderId + ", PackageFee=" + PackageFee
                + ", Provice=" + Provice + ", Quantity=" + Quantity
                + ", ReceiverName=" + ReceiverName + ", SellerId=" + SellerId
                + ", SellerName=" + SellerName + ", SendMode=" + SendMode
                + ", SendTime=" + SendTime + ", Status=" + Status
                + ", TotalAmt=" + TotalAmt + ", UserExplain=" + UserExplain
                + ", UserId=" + UserId + ", UserName=" + UserName + ", Goods="
                + Goods + "]";
    }

    public static final int getPayWayString(String way) {
        if (Consts.WX_PAY.equals(way)) {
            return R.string.wechat_pay;
        } else if (Consts.ALI_PAY.equals(way)) {
            return R.string.alipay_pay;
        } else if (Consts.UN_PAY.equals(way)) {
            return R.string.yin_pay;
        } else if (Consts.BALAN_PAY.equals(way)) {
            return R.string.yue_pay;
        } else if (Consts.INTER_PAY.equals(way)) {
            return R.string.inter_pay;
        } else if (Consts.GHTNET_PAY.equals(way)) {
            return R.string.ghtnet_pay;
        } else if (Consts.FuYou.equals(way)) {
            return R.string.fuyou_pay;
        } else if (Consts.INTEG_PAY.equals(way)) {
            return R.string.integral_pay;
        } else if (Consts.Offline.equals(way)) {
            return R.string.offline_pay;
        } else if (Consts.FxVip.equals(way)) {
            return R.string.U_pay;
        } else if (Consts.FxAcc.equals(way)) {
            return R.string.FxAcc_pay;
        }
        return R.string.no_pay;
    }

}
