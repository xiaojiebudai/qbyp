package cn.net.chenbao.qbypseller.bean;

import cn.net.chenbao.qbypseller.utils.Consts;

import java.util.List;

import cn.net.chenbao.qbypseller.R;

/**
 * 订单bean
 *
 * @author licheng
 */
public class Order {

    public static final int SEND_MODE_STORE = 0;
    public static final int SEND_MODE_SHIP = 1;
    public static final int SEND_MODE_ALL = -1;

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

    public String Address;
    public String Barcode;
    public long City;
    public long County;
    public long CreateTime;// 创建时间
    public long SellerConfirmTime;// 接单时间
    public double DeliverFee;// 配送费
    public double GoodsAmt;// 商品总价
    public double PayAmt;// 线上支付金额
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
    /**
     * 描述
     */
    public String UserExplain;// 描述
    public long UserId;// 用户id
    public String UserName;// 用户名
    public List<Goods> Goods;
    /**
     * 评价订单
     */
    public String PayCode;// 属性PayCode(付款方式)
    public long PayId;// 属性PayId(付款单号)
    public long PayTime;// 属性PayTime(付付款时间)
    /*
    积分信息
     */
    public double PersentIntegral;//赠送积分
    public double PersentIntegralRate;//积分比率
    public double SellerRlAmt;//让利
    public double SellerIntegral;//商家获得直营积分


    public static final int getStateString(int state) {
        int stateString = R.string.empty;
        switch (state) {
            case Order.STATE_WAIT_SELLER_CONFIRM:// 待接单
                stateString = R.string.wait_get;
                break;
            case Order.STATE_WAIT_PAY:// 代付款
                stateString = R.string.wait_pay;
                break;
            case Order.STATE_WAIT_BUYER_CONFIRM:// 待收貨
                stateString = R.string.wait_receive;
                break;
            case Order.STATE_REFUND:// 退款中
                stateString = R.string.refunding;
                break;
            case Order.STATE_CANCEL:// 取消
                stateString = R.string.cancel;
                break;
            case Order.STATE_COMPLETE:// 完成
                stateString = R.string.finish;
                break;
            case Order.STATE_CLOSE:// 完成
                stateString = R.string.closed;
                break;
            default:
                break;
        }
        return stateString;
    }

    public static final int getPayWayString(String way) {
        if (Consts.WX_PAY.equals(way)) {
            return R.string.wechat_pay;
        } else if (Consts.ALI_PAY.equals(way)) {
            return R.string.ali_pay;
        } else if (Consts.UN_PAY.equals(way)) {
            return R.string.union_pay;
        } else if (Consts.BALAN_PAY.equals(way)) {
            return R.string.balan_pay;
        } else if (Consts.INTER_PAY.equals(way)) {
            return R.string.inter_pay;
        } else if (Consts.GHTNET_PAY.equals(way)) {
            return R.string.ghtnet_pay;
        }else if (Consts.FuYou.equals(way)) {
            return R.string.fuyou_pay;
        } else if (Consts.INTEGRAL_PAY.equals(way)) {
            return R.string.integral_pay;
        } else if (Consts.LOCAL_INTEGRAL_PAY.equals(way)) {
            return R.string.integral_pay;
        }else if (Consts.SEL_ACC.equals(way)) {
            return R.string.seller_yue_pay;
        } else if (Consts.FxVip.equals(way)) {
            return R.string.U_pay;
        } else if (Consts.FxAcc.equals(way)) {
            return R.string.FxAcc_pay;
        }
        return R.string.no_pay;
    }
}
