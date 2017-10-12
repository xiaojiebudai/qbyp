package cn.net.chenbao.qbyp.api;

/**
 * Created by zxj on 2017/4/17.
 *
 * @description
 */

public class ApiDistribution {

    /**
     * 支付类接口公共字段
     */
    public static final String DISTRIBUTION_JSON = "/FenXiao/Json/";
    /**
     * 支付信息
     */
    public static final String DISTRIBUTION_API = Api.WW_ONLINE + DISTRIBUTION_JSON;


    /**
     * 分销规则
     */
    public static final String FENXIAO_RULE = "http://www.zgcyk.net/mobile/help/fenxiao.html";

    /**
     * 分销账款明细
     *
     * @return
     */
    public static final String AccountDetail() {
        return DISTRIBUTION_API + "AccountDetail";
    }

    /**
     * 取允许提现金额
     *
     * @return
     */
    public static final String AllowCashAccount() {
        return DISTRIBUTION_API + "AllowCashAccount";
    }

    /**
     * 获取会员提现明细，按发生时间倒序
     *
     * @return
     */
    public static final String CashDetail() {
        return DISTRIBUTION_API + "CashDetail";
    }

    /**
     * 购款提现提交
     *
     * @return
     */
    public static final String CashSubmit() {
        return DISTRIBUTION_API + "CashSubmit";
    }

    /**
     * 积分余额明细
     *
     * @return
     */
    public static final String ConsumeBalanceDetail() {
        return DISTRIBUTION_API + "ConsumeBalanceDetail";
    }

    /**
     * 积分明细
     *
     * @return
     */
    public static final String ConsumeDetail() {
        return DISTRIBUTION_API + "ConsumeDetail";
    }

    /**
     * 取解冻池列表
     *
     * @return
     */
    public static final String ConsumePoolDetail() {
        return DISTRIBUTION_API + "ConsumePoolDetail";
    }

    /**
     * 积分解冻池汇总
     *
     * @return
     */
    public static final String ConsumePoolSum() {
        return DISTRIBUTION_API + "ConsumePoolSum";
    }

    /**
     * 取我的分销账户
     *
     * @return
     */
    public static final String MyAccount() {
        return DISTRIBUTION_API + "MyAccount";
    }

    /**
     * 取我的分销代理信息
     *
     * @return
     */
    public static final String MyAgent() {
        return DISTRIBUTION_API + "MyAgent";
    }

    /**
     * 我的进货单列表
     *
     * @return
     */
    public static final String MyOrdersBuy() {
        return DISTRIBUTION_API + "MyOrdersBuy";
    }

    /**
     * 我的进货单数量统计
     *
     * @return
     */
    public static final String MyOrdersCountBuy() {
        return DISTRIBUTION_API + "MyOrdersCountBuy";
    }

    /**
     * 我的销货单数量统计
     *
     * @return
     */
    public static final String MyOrdersCountSeller() {
        return DISTRIBUTION_API + "MyOrdersCountSeller";
    }

    /**
     * 我的销售单列表
     *
     * @return
     */
    public static final String MyOrdersSeller() {
        return DISTRIBUTION_API + "MyOrdersSeller";
    }

    /**
     * 订单取消
     *
     * @return
     */
    public static final String OrderCancel() {
        return DISTRIBUTION_API + "OrderCancel";
    }

    /**
     * 取单个订单详情
     *
     * @return
     */
    public static final String OrderInfo() {
        return DISTRIBUTION_API + "OrderInfo";
    }

    /**
     * 订单日志
     *
     * @return
     */
    public static final String OrderLogs() {
        return DISTRIBUTION_API + "OrderLogs";
    }

    /**
     * 订单,我没货
     *
     * @return
     */
    public static final String OrderNoGoods() {
        return DISTRIBUTION_API + "OrderNoGoods";
    }

    /**
     * 订单预览，异常501，502
     *
     * @return
     */
    public static final String OrderPreview() {
        return DISTRIBUTION_API + "OrderPreview";
    }

    /**
     * 订单收货
     *
     * @return
     */
    public static final String OrderReceive() {
        return DISTRIBUTION_API + "OrderReceive";
    }

    /**
     * 订单发货
     *
     * @return
     */
    public static final String OrderSend() {
        return DISTRIBUTION_API + "OrderSend";
    }

    /**
     * 订单提交，异常501，502，503，504
     *
     * @return
     */
    public static final String OrderSubmit() {
        return DISTRIBUTION_API + "OrderSubmit";
    }

    /**
     * 取产品详情，异常501，502
     *
     * @return
     */
    public static final String ProductDescribe() {
        return DISTRIBUTION_API + "ProductDescribe";
    }

    /**
     * 获取商品分销属性，异常501，502
     *
     * @return
     */
    public static final String ProductFenxiaoGet() {
        return DISTRIBUTION_API + "ProductFenxiaoGet";
    }

    /**
     * 获取商品信息(包括分销属性)，异常501，502
     *
     * @return
     */
    public static final String ProductGet() {
        return DISTRIBUTION_API + "ProductGet";
    }

    /**
     * 是否是分销商
     *
     * @return
     */
    public static final String isFenxiaoAgent() {
        return DISTRIBUTION_API + "IsFenxiaoAgent";
    }

    /**
     * 一级二级加速
     *
     * @return
     */
    public static final String ConsumePoolSpeeds() {
        return DISTRIBUTION_API + "ConsumePoolSpeeds";
    }

    /**
     * 一级二级加速des
     *
     * @return
     */
    public static final String ConsumeSpeeds() {
        return DISTRIBUTION_API + "ConsumeSpeeds";
    }


    /**
     * 取分销产品列表
     *
     * @return
     */
    public static final String Products() {
        return DISTRIBUTION_API + "Products";
    }

    /**
     * 加入购物车(数量+1)
     *
     * @return
     */
    public static final String CartAdd() {
        return DISTRIBUTION_API + "CartAdd";
    }

    /**
     * 减购物车数量(数量-1)
     *
     * @return
     */
    public static final String CartSub() {
        return DISTRIBUTION_API + "CartSub";
    }

    /**
     * 修改购物车数量
     *
     * @return
     */
    public static final String CartNum() {
        return DISTRIBUTION_API + "CartNum";
    }

    /**
     * 取购物车产品列表,按最后修改时间倒序
     *
     * @return
     */
    public static final String CartGet() {
        return DISTRIBUTION_API + "CartGet";
    }

    /**
     * 会员取购物车商品数量等
     *
     * @return
     */
    public static final String CartSumGet() {
        return DISTRIBUTION_API + "CartSumGet";
    }

    /**
     * 删除一组购物车记录
     *
     * @return
     */
    public static final String CartsDelete() {
        return DISTRIBUTION_API + "CartsDelete";
    }

    /**
     * 订单预览
     *
     * @return
     */
    public static final String OrderPreviewByCart() {
        return DISTRIBUTION_API + "OrderPreviewByCart";
    }

    /**
     * 订单提交
     *
     * @return
     */
    public static final String OrderSubmitByCart() {
        return DISTRIBUTION_API + "OrderSubmitByCart";
    }

    /**
     * 是否有待签定的分销协议
     *
     * @return
     */
    public static final String HaveFenxiaoProtocol() {
        return DISTRIBUTION_API + "HaveFenxiaoProtocol";
    }

    /**
     * 确认签订分销协议
     *
     * @return
     */
    public static final String ConfirmFenxiaoProtocol() {
        return DISTRIBUTION_API + "ConfirmFenxiaoProtocol";
    }
    /**
     * 取邮费
     *
     * @return
     */
    public static final String ComputePostFee() {
        return DISTRIBUTION_API + "ComputePostFee";
    }
}
